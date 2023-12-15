package org.jetstreamDrop;

import io.nats.client.Connection;
import io.nats.client.JetStream;
import io.nats.client.JetStreamApiException;
import io.nats.client.Message;
import io.nats.client.Nats;
import io.nats.client.Subscription;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayDeque;
import java.util.List;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReaderService {

  private static final Logger LOG = LoggerFactory.getLogger(ReaderService.class);

  private final String natsServerUrl;
  private final MessageDeserializer messageDeserializer;
  private final String subject;

  ArrayDeque<ReadMessage> msgs = new ArrayDeque<>();

  public ReaderService(
      String natsServerUrl, MessageDeserializer messageDeserializer, String subject) {
    this.natsServerUrl = natsServerUrl;
    this.messageDeserializer = messageDeserializer;
    this.subject = subject;
  }

  @PostConstruct
  public void startReadingMessages() {
    // This method will be invoked after the service is initialized
    startMessageListener();
  }

  private void startMessageListener() {
    new Thread(
            () -> {
              try {
                // Connect to NATS server
                try (Connection natsConnection = Nats.connect(natsServerUrl)) {
                  LOG.info("Connected to NATS server at: {}", natsServerUrl);

                  JetStream jetStream = natsConnection.jetStream();
                  LOG.info("Connected to JetStream server at: {}", natsServerUrl);
                  // Subscribe to the subject

                  Subscription subscription = tryToSubscribe(jetStream);
                  LOG.info("Subscribed to subject: {}", natsServerUrl);

                  continuouslyReadMessages(subscription, messageDeserializer);
                }

              } catch (Exception e) {
                LOG.error("Error during message reading: ", e);
              }
            })
        .start();
  }

  private Subscription tryToSubscribe(JetStream jetStream)
      throws IOException, JetStreamApiException, InterruptedException {

    Subscription subscription;
    try {
      subscription = jetStream.subscribe(subject);
    } catch (IllegalStateException e) {
      if (e.getMessage().contains("SUB-90007")) { // No matching streams for subject
        // try again after 5 seconds
        LOG.warn(
            "Unable to subscribe to subject: "
                + subject
                + " . No matching streams. Trying again in 5sec...");
        Thread.sleep(5000);
        return tryToSubscribe(jetStream);
      }
      throw new RuntimeException(e);
    }
    return subscription;
  }

  private void continuouslyReadMessages(
      Subscription subscription, MessageDeserializer messageDeserializer)
      throws InterruptedException {
    while (true) {
      // Wait for a message
      Message message = subscription.nextMessage(100);
      // Print the message
      if (message != null) {
        try {
          DeserializedMessage deserializedMessage =
              messageDeserializer.deserializeMessage(ByteBuffer.wrap(message.getData()));
          ReadMessage msg =
              new ReadMessage(
                  message.getSubject(),
                  deserializedMessage.name(),
                  deserializedMessage.content(),
                  message.metaData().timestamp());
          msgs.addFirst(msg);
          message.ack();
        } catch (Exception e) {
          LOG.warn("Unable to deserialize message", e);
        }
      }
    }
  }

  public List<ReadMessage> getMessages() {
    return msgs.stream().toList();
  }

  public List<ReadMessage> filter(String subject, String type) {
    return msgs.stream()
        .filter(
            m -> {
              if (subject != null && !subject.isBlank()) {
                return m.subject().contains(subject);
              }
              return true;
            })
        .filter(
            m -> {
              if (type != null && !type.isBlank()) {
                return m.name().contains(type);
              }
              return true;
            })
        .toList();
  }
}
