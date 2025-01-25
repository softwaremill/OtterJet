package otter.jet.avro;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import org.apache.avro.Schema;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;
import otter.jet.AbstractIntegrationTest;
import otter.jet.JetStreamContainerInitializer;
import otter.jet.JetStreamUtils;
import otter.jet.assertions.ComparisonConfiguration;
import otter.jet.examples.avro.RandomAvroPersonGenerator;
import otter.jet.reader.ReadMessage;
import otter.jet.reader.ReaderConfigurationProperties;
import otter.jet.store.Filters;
import otter.jet.store.MessageStore;

@TestPropertySource(
    properties = {
        "read.mode=avro",
        "read.subject=avro_person",
        "read.avro.pathToSchema=src/test/resources/person.avsc"
    })
class SimpleAvroMessageReaderTest extends AbstractIntegrationTest {

  private static final LocalDateTime ignoredMessageTimestamp =
      LocalDateTime.ofInstant(Instant.EPOCH, ZoneOffset.UTC);
  @Autowired
  private MessageStore messageStore;
  @Autowired
  private ReaderConfigurationProperties readerConfigurationProperties;

  @Test
  public void shouldReadProtoMessageSentAsSpecificType() throws IOException {
    // given
    JetStreamUtils.createSubjectStream(
        readerConfigurationProperties.getSubject(),
        JetStreamContainerInitializer.getNatsServerUrl());
    var schema = new Schema.Parser().parse(new File("src/test/resources/person.avsc"));
    var person = RandomAvroPersonGenerator.randomPerson(schema);
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
                assertThat(messageStore.filter(Filters.empty(), 0, 10))
                    .usingRecursiveFieldByFieldElementComparator(
                        ComparisonConfiguration.configureReadMessageComparisonWithJSONBody())
                    .contains(
                        new ReadMessage(
                            readerConfigurationProperties.getSubject(),
                            "Person",
                            new JSONObject()
                                .put("id", person.id())
                                .put("name", person.name())
                                .put("email", person.email())
                                .put("numbers", new JSONArray().put(person.phoneNumber()))
                                .toString(),
                            ignoredMessageTimestamp)));
  }
}
