package org.jetstreamDrop.proto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

import com.google.protobuf.Any;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import org.jetstreamDrop.AbstractIntegrationTest;
import org.jetstreamDrop.JetStreamContainerInitializer;
import org.jetstreamDrop.JetStreamUtils;
import org.jetstreamDrop.ReadMessage;
import org.jetstreamDrop.ReaderConfigurationProperties;
import org.jetstreamDrop.ReaderService;
import org.jetstreamDrop.assertions.ComparisonConfiguration;
import org.jetstreamDrop.examples.RandomProtoPersonGenerator;
import org.jetstreamDrop.examples.protobuf.PersonProtos;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource(
    properties = {
      "read.mode=proto",
      "read.subject=any_person",
      "read.proto.pathToDescriptor=src/test/resources/person.desc"
    })
class AnyProtoMessageReaderTest extends AbstractIntegrationTest {

  private static final LocalDateTime ignoredMessageTimestamp =
      LocalDateTime.ofInstant(Instant.EPOCH, ZoneOffset.UTC);
  @Autowired private ReaderService readerService;
  @Autowired private ReaderConfigurationProperties readerConfigurationProperties;

  @Test
  public void shouldReadProtoMessageSentAsAny() {
    // given
    JetStreamUtils.createSubjectStream(
        readerConfigurationProperties.getSubject(),
        JetStreamContainerInitializer.getNatsServerUrl());
    readerService.startReadingMessages();
    PersonProtos.Person person = RandomProtoPersonGenerator.randomPerson();
    byte[] data = Any.pack(person).toByteArray();

    // when
    JetStreamUtils.tryToSendMessage(
        data,
        readerConfigurationProperties.getSubject(),
        JetStreamContainerInitializer.getNatsServerUrl());

    // then
    await()
        .untilAsserted(
            () ->
                assertThat(readerService.getMessages())
                    .usingRecursiveFieldByFieldElementComparator(
                        ComparisonConfiguration.configureReadMessageComparisonWithJSONBody())
                    .contains(
                        new ReadMessage(
                            readerConfigurationProperties.getSubject(),
                            "Person",
                            new JSONObject()
                                .put("id", person.getId())
                                .put("name", person.getName())
                                .put("email", person.getEmail())
                                .put("numbers", new JSONArray().put(person.getNumbers(0)))
                                .toString(),
                            ignoredMessageTimestamp)));
  }
}
