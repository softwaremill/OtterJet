package org.jetstreamDrop;

import io.nats.client.Connection;
import io.nats.client.Message;
import io.nats.client.Nats;
import io.nats.client.Subscription;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.ByteBuffer;

//POC for approach with dsc files
public class NatsJetStreamProto {

    public static void main(String[] args) {
        String natsServerUrl = "nats://localhost:4222"; // Change this to your NATS server URL
        String subject = "*"; // Change this to your desired subject

        try {
            // Connect to NATS server
            Connection natsConnection = Nats.connect(natsServerUrl);
            System.out.println("Connected to NATS server at " + natsServerUrl);

            // Subscribe to the subject

            Subscription subscription = natsConnection.subscribe(subject);
            System.out.println("Subscribed to subject: " + subject);
            MessageDeserializer messageDeserializer = getMessageDeserializer();

            // Continuously read messages
            while (true) {
                // Wait for a message
                Message message = subscription.nextMessage(100);

                // Print the message
                if (message != null) {
                    try {
                        String s = messageDeserializer.deserializeMessage(ByteBuffer.wrap(message.getData()));
                        System.out.println("deserialized event: " + s);
                    } catch (Exception e) {
                        System.out.println("Exception " + e);

                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static MessageDeserializer getMessageDeserializer() throws FileNotFoundException, URISyntaxException {
        URL resource = NatsJetStreamProto.class.getClassLoader().getResource("main.dsc");
        if (resource == null) {
            throw new FileNotFoundException("File not found!");
        }
        boolean isAnyProto = true; //false was default
        String msgTypeName = "";
        String fullDescFile = resource.getPath();

        System.out.println("file " + resource + "  exists");
        return new ProtobufMessageDeserializer(fullDescFile, msgTypeName, isAnyProto);
    }
}