package otter.jet.examples.avro;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.apache.avro.Schema;
import org.apache.avro.SchemaParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import otter.jet.JetStreamUtils;
import otter.jet.avro.RandomPersonAvro;

@Configuration
class AvroMessagePublisherConfiguration {

  @Bean
  @Profile("!test")
  @ConditionalOnProperty(value = "read.mode", havingValue = "avro")
  CommandLineRunner simplePublisher(@Value("${nats.server.url}") String serverUrl) {
    return (args) -> {
      String subject = "avro";
      JetStreamUtils.createSubjectStream(subject, serverUrl);

      ScheduledExecutorService scheduledExecutorService =
          Executors.newSingleThreadScheduledExecutor();

      Schema schema = new SchemaParser().parse(new File("src/test/resources/person.avsc"))
          .mainSchema();

      scheduledExecutorService.scheduleAtFixedRate(
          () -> {
            RandomPersonAvro randomPersonAvro = RandomAvroPersonGenerator.randomPerson(schema);
            try {
              JetStreamUtils.tryToSendMessage(randomPersonAvro.toByteArray(), subject,
                  serverUrl);
            } catch (IOException e) {
              throw new RuntimeException(e);
            }
          },
          0,
          2,
          TimeUnit.SECONDS);
    };
  }

}
