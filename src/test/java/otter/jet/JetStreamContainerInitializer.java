package otter.jet;

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

  private static final int natsPort = 4222;
  private static final int monitoringPort = 8222;
  static GenericContainer natsJetStream =
      new GenericContainer("nats:2.10.7")
          .withCommand("--jetstream", "-m", monitoringPort + "")
          .withExposedPorts(natsPort, monitoringPort)
          .waitingFor(new LogMessageWaitStrategy().withRegEx(".*Server is ready.*"));

  @Override
  public void initialize(ConfigurableApplicationContext applicationContext) {
    Startables.deepStart(List.of(natsJetStream)).join();
    var env = applicationContext.getEnvironment();
    env.getPropertySources()
        .addFirst(
            new MapPropertySource(
                "testcontainers",
                Map.of(
                    "nats.server.host",
                    natsJetStream.getHost(),
                    "nats.server.port",
                    natsJetStream.getMappedPort(natsPort),
                    "nats.server.url",
                    getNatsServerUrl(),
                    "nats.server.monitoring.protocol",
                    "http",
                    "nats.server.monitoring.port",
                    natsJetStream.getMappedPort(monitoringPort))));
  }

  public static String getNatsServerUrl() {
    return "nats://" + natsJetStream.getHost() + ":" + natsJetStream.getMappedPort(4222);
  }
}
