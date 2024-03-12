package otterjet.plaintext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

import com.github.javafaker.Faker;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import otterjet.AbstractIntegrationTest;
import otterjet.JetStreamContainerInitializer;
import otterjet.JetStreamUtils;
import otterjet.ReadMessage;
import otterjet.ReaderConfigurationProperties;
import otterjet.ReaderService;
import otterjet.assertions.ComparisonConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource(properties = {"read.mode=plaintext", "read.subject=plaintext"})
class PlainTextMessageReaderTest extends AbstractIntegrationTest {

  private static final LocalDateTime ignoredMessageTimestamp =
      LocalDateTime.ofInstant(Instant.EPOCH, ZoneOffset.UTC);
  @Autowired private ReaderService readerService;
  @Autowired private ReaderConfigurationProperties readerConfigurationProperties;
  private final String subjectFilter = "";
  private final String typeFilter = "";

  @Test
  public void shouldReadMessagesSentInPlaintext() {
    // given
    JetStreamUtils.createSubjectStream(
        readerConfigurationProperties.getSubject(),
        JetStreamContainerInitializer.getNatsServerUrl());
    String randomName = new Faker().name().fullName();
    byte[] data = randomName.getBytes(StandardCharsets.UTF_8);

    // when
    JetStreamUtils.tryToSendMessage(
        data,
        readerConfigurationProperties.getSubject(),
        JetStreamContainerInitializer.getNatsServerUrl());

    // then
    await()
        .untilAsserted(
            () ->
                assertThat(readerService.filter(subjectFilter, typeFilter, 0, 10, ""))
                    .usingRecursiveFieldByFieldElementComparator(
                        ComparisonConfiguration.configureReadMessageComparison())
                    .contains(
                        new ReadMessage(
                            readerConfigurationProperties.getSubject(),
                            "",
                            randomName,
                            ignoredMessageTimestamp)));
  }
}
