package org.jetstreamDrop;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = JetStreamContainerInitializer.class)
@ActiveProfiles("test")
public abstract class AbstractIntegrationTest {

  @BeforeAll
  public static void setup() {
    JetStreamContainerInitializer.natsJetStream.start();
  }

  @AfterAll
  public static void cleanup() {
    JetStreamContainerInitializer.natsJetStream.stop();
  }
}
