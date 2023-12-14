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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class SimpleProtobufMessagePublisherConfiguration {
  @Bean
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
                  .setId(faker.number().numberBetween(1, 10))
                  .setName(faker.name().firstName())
                  .setEmail(faker.bothify("????##@gmail.com"))
                  .addNumbers(faker.phoneNumber().phoneNumber())
                  .build();

          System.out.println("About to publish " + person);
          PublishAck pa =
              js.publish(
                  NatsMessage.builder()
                      .subject("person")
                      .data(Any.pack(person).toByteArray())
                      .build());
          System.out.println(pa);
          Thread.sleep(2000);
        }
      } catch (Exception e) {
        System.err.println(e);
      }
    };
  }
}
