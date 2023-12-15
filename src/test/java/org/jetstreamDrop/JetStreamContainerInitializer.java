package org.jetstreamDrop;

import java.util.List;
import java.util.Map;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.MapPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.LogMessageWaitStrategy;
import org.testcontainers.lifecycle.Startables;

public class JetStreamContainerInitializer
    implements ApplicationContextInitializer<ConfigurableApplicationContext> {

  static GenericContainer natsJetStream =
      new GenericContainer("nats:2.10.7")
          .withCommand("--jetstream")
          .withExposedPorts(4222)
          .waitingFor(new LogMessageWaitStrategy().withRegEx(".*Server is ready.*"));

  @Override
  public void initialize(ConfigurableApplicationContext applicationContext) {
    Startables.deepStart(List.of(natsJetStream)).join();
    var env = applicationContext.getEnvironment();
    env.getPropertySources()
        .addFirst(
            new MapPropertySource("testcontainers", Map.of("nats.server.url", getNatsServerUrl())));
  }

  public static String getNatsServerUrl() {
    return "nats://" + natsJetStream.getHost() + ":" + natsJetStream.getMappedPort(4222);
  }
}
