package otterjet.monitoring;

import feign.Feign;
import feign.Logger;
import feign.jackson.JacksonDecoder;
import feign.slf4j.Slf4jLogger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@AutoConfiguration
class NatsMonitoringAutoConfiguration {

  @Configuration
  @ConditionalOnExpression("'${nats.server.monitoring.port:}' != ''")
  static class NatsMonitoringEnabledConfiguration {

    @Bean
    NatsMonitoringApiClient natsMonitoringApiClient(
        @Value("${nats.server.monitoring.protocol:https}") String natServerMonitoringProtocol,
        @Value("${nats.server.host}") String natsServerHost,
        @Value("${nats.server.monitoring.port}") String natServerMonitoringPort) {
      return new Feign.Builder()
          .contract(new SpringMvcContract())
          .logger(new Slf4jLogger(NatsMonitoringApiClient.class))
          .decoder(new JacksonDecoder())
          .logLevel(Logger.Level.BASIC)
          .target(
              NatsMonitoringApiClient.class,
              createNatsMonitoringUrl(
                  natServerMonitoringProtocol, natsServerHost, natServerMonitoringPort));
    }

    @Bean
    NatsMonitoringDataLoader natsMonitoringDataLoader(
        NatsMonitoringApiClient natsMonitoringApiClient) {
      return new DirectNatsMonitoringDataLoader(natsMonitoringApiClient);
    }

    private static String createNatsMonitoringUrl(
        String natServerMonitoringProtocol, String natsServerHost, String natServerMonitoringPort) {
      return natServerMonitoringProtocol + "://" + natsServerHost + ":" + natServerMonitoringPort;
    }
  }

  @Configuration
  @ConditionalOnExpression("'${nats.server.monitoring.port:}' == ''")
  @AutoConfigureAfter(NatsMonitoringEnabledConfiguration.class)
  static class NatsMonitoringDisabledConfiguration {

    @Bean
    NatsMonitoringDataLoader natsMonitoringDataLoader() {
      return new NoMonitoringConfiguredDataLoader();
    }
  }
}
