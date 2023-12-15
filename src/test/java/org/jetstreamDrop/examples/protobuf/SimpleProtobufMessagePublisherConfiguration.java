package org.jetstreamDrop.examples.protobuf;

import com.github.javafaker.Faker;
import com.google.protobuf.Any;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
class SimpleProtobufMessagePublisherConfiguration {

  private static final Logger LOG =
      LoggerFactory.getLogger(SimpleProtobufMessagePublisherConfiguration.class);

  @Bean
  @Profile("!test")
  @ConditionalOnProperty(value = "read.mode", havingValue = "proto")
  CommandLineRunner simplePublisher(@Value("${nats.server.url}") String serverUrl) {
    return (args) -> {
      try (Connection nc = Nats.connect(serverUrl)) {

        JetStreamManagement jsm = nc.jetStreamManagement();

        // Build the configuration
        StreamConfiguration streamConfig =
            StreamConfiguration.builder()
                .name("hello")
                .storageType(StorageType.Memory)
                .subjects("person")
                .build();
        // Create the stream
        StreamInfo streamInfo = jsm.addStream(streamConfig);

        JsonUtils.printFormatted(streamInfo);
        JetStream js = nc.jetStream();

        Faker faker = new Faker();
        while (true) {
          PersonProtos.Person person =
              PersonProtos.Person.newBuilder()
                  .setId(faker.number().numberBetween(1, Integer.MAX_VALUE))
                  .setName(faker.name().firstName())
                  .setEmail(faker.bothify("????##@gmail.com"))
                  .addNumbers(faker.phoneNumber().phoneNumber())
                  .build();
          LOG.info("About to publish: {}", person);
          PublishAck pa =
              js.publish(
                  NatsMessage.builder()
                      .subject("person")
                      .data(Any.pack(person).toByteArray())
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
