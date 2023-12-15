package org.jetstreamDrop.examples.plaintext;

import com.github.javafaker.Faker;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.jetstreamDrop.JetStreamUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
class PlainTextMessagePublisherConfiguration {

  @Bean
  @Profile("!test")
  @ConditionalOnProperty(value = "read.mode", havingValue = "plaintext")
  CommandLineRunner simplePublisher(@Value("${nats.server.url}") String serverUrl) {
    return (args) -> {
      String subject = "plaintext";
      JetStreamUtils.createSubjectStream(subject, serverUrl);

      ScheduledExecutorService scheduledExecutorService =
          Executors.newSingleThreadScheduledExecutor();
      scheduledExecutorService.scheduleAtFixedRate(
          () -> {
            Faker faker = new Faker();
            String randomJson = "{\"name\": \"" + faker.name().fullName() + "\"}";
            JetStreamUtils.tryToSendMessage(
                randomJson.getBytes(StandardCharsets.UTF_8), subject, serverUrl);
          },
          0,
          2,
          TimeUnit.SECONDS);
    };
  }
}
