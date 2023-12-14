package org.jetstreamDrop;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(ReaderConfigurationProperties.class)
class ReaderConfiguration {

  private final ReaderConfigurationProperties readerConfigurationProperties;

  ReaderConfiguration(ReaderConfigurationProperties readerConfigurationProperties) {
    this.readerConfigurationProperties = readerConfigurationProperties;
  }

  @Bean
  public ReaderService readerService(
      @Value("${nats.server.url}") String natsServerUrl, MessageDeserializer messageDeserializer) {
    return new ReaderService(
        natsServerUrl, messageDeserializer, readerConfigurationProperties.getSubject());
  }
}
