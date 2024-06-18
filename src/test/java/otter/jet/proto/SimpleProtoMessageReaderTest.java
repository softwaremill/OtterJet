package otter.jet.proto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import otter.jet.AbstractIntegrationTest;
import otter.jet.JetStreamContainerInitializer;
import otter.jet.JetStreamUtils;
import otter.jet.reader.ReadMessage;
import otter.jet.reader.ReaderConfigurationProperties;
import otter.jet.reader.ReaderService;
import otter.jet.assertions.ComparisonConfiguration;
import otter.jet.examples.RandomProtoPersonGenerator;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;
import otter.jet.examples.protobuf.PersonProtos.Person;

@TestPropertySource(
    properties = {
      "read.mode=proto",
      "read.subject=typed_person",
      "read.proto.messageTypeName=protobuf.Person",
      "read.proto.pathToDescriptor=src/test/resources/person.desc"
    })
class SimpleProtoMessageReaderTest extends AbstractIntegrationTest {

  private static final LocalDateTime ignoredMessageTimestamp =
      LocalDateTime.ofInstant(Instant.EPOCH, ZoneOffset.UTC);
  @Autowired private ReaderService readerService;
  @Autowired private ReaderConfigurationProperties readerConfigurationProperties;

  private final String subjectFilter = "";
  private final String typeFilter = "";

  @Test
  public void shouldReadProtoMessageSentAsSpecificType() {
    // given
    JetStreamUtils.createSubjectStream(
        readerConfigurationProperties.getSubject(),
        JetStreamContainerInitializer.getNatsServerUrl());
    Person person = RandomProtoPersonGenerator.randomPerson();
    byte[] data = person.toByteArray();

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
