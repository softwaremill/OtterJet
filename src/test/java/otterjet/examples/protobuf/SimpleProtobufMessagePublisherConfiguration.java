package otterjet.examples.protobuf;

import com.google.protobuf.Any;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import otterjet.JetStreamUtils;
import otterjet.examples.RandomProtoPersonGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
class SimpleProtobufMessagePublisherConfiguration {

  @Bean
  @Profile("!test")
  @ConditionalOnProperty(value = "read.mode", havingValue = "proto")
  CommandLineRunner simplePublisher(@Value("${nats.server.url}") String serverUrl) {
    return (args) -> {
      String subject = "person";
      JetStreamUtils.createSubjectStream(subject, serverUrl);

      ScheduledExecutorService scheduledExecutorService =
          Executors.newSingleThreadScheduledExecutor();
      scheduledExecutorService.scheduleAtFixedRate(
          () -> {
            PersonProtos.Person person = RandomProtoPersonGenerator.randomPerson();
            JetStreamUtils.tryToSendMessage(Any.pack(person).toByteArray(), subject, serverUrl);
          },
          0,
          2,
          TimeUnit.SECONDS);
    };
  }
}
