package org.jetstreamDrop;

import io.nats.client.Connection;
import io.nats.client.JetStream;
import io.nats.client.JetStreamManagement;
import io.nats.client.Nats;
import io.nats.client.api.StorageType;
import io.nats.client.api.StreamConfiguration;
import io.nats.client.impl.NatsMessage;

public class JetStreamUtils {
  public static void createSubjectStream(String subject, String natsServerUrl) {
    try (Connection nc = Nats.connect(natsServerUrl)) {

      JetStreamManagement jsm = nc.jetStreamManagement();
      StreamConfiguration streamConfig =
          StreamConfiguration.builder()
              .name("hello")
              .storageType(StorageType.Memory)
              .subjects(subject)
              .build();
      jsm.addStream(streamConfig);
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  public static void tryToSendMessage(byte[] data, String subject, String natsServerUrl) {
    try (Connection nc = Nats.connect(natsServerUrl)) {
      JetStream js = nc.jetStream();

      NatsMessage sentMessage = NatsMessage.builder().subject(subject).data(data).build();
      js.publish(sentMessage);
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }
}
