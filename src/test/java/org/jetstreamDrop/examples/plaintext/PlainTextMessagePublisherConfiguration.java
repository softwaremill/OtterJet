package org.jetstreamDrop.examples.plaintext;

import com.github.javafaker.Faker;
import io.nats.client.Connection;
import io.nats.client.JetStream;
import io.nats.client.JetStreamManagement;
import io.nats.client.Nats;
import io.nats.client.api.PublishAck;
import io.nats.client.api.StorageType;
import io.nats.client.api.StreamConfiguration;
import io.nats.client.api.StreamInfo;
import io.nats.client.impl.NatsMessage;
import io.nats.client.support.JsonUtils;
import java.nio.charset.StandardCharsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
class PlainTextMessagePublisherConfiguration {

  private static final Logger LOG =
      LoggerFactory.getLogger(PlainTextMessagePublisherConfiguration.class);

  @Bean
  @Profile("!test")
  @ConditionalOnProperty(value = "read.mode", havingValue = "plaintext")
  CommandLineRunner simplePublisher(@Value("${nats.server.url}") String serverUrl) {
    return (args) -> {
      try (Connection nc = Nats.connect(serverUrl)) {

        JetStreamManagement jsm = nc.jetStreamManagement();

        // Build the configuration
        StreamConfiguration streamConfig =
            StreamConfiguration.builder()
                .name("hello")
                .storageType(StorageType.Memory)
                .subjects("plaintext")
                .build();
        // Create the stream
        StreamInfo streamInfo = jsm.addStream(streamConfig);

        JsonUtils.printFormatted(streamInfo);
        JetStream js = nc.jetStream();

        Faker faker = new Faker();
        while (true) {
          PublishAck pa =
              js.publish(
                  NatsMessage.builder()
                      .subject("plaintext")
                      .data(
                          "{\"name\": \"" + faker.name().fullName() + "\"}", StandardCharsets.UTF_8)
                      .build());
          LOG.info("Publish ack received: {}", pa);
          Thread.sleep(2000);
        }
      } catch (Exception e) {
        LOG.error("Error during message publish", e);
      }
    };
  }
}
