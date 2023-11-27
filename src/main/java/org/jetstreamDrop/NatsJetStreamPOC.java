package org.jetstreamDrop;

import com.google.protobuf.Any;
import com.google.protobuf.util.JsonFormat;
import io.nats.client.Connection;
import io.nats.client.Message;
import io.nats.client.Nats;
import io.nats.client.Subscription;

//Old version based on generated classes from proto files
public class NatsJetStreamPOC {

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

            // Continuously read messages
            while (true) {
                // Wait for a message
                Message message = subscription.nextMessage(100);

                // Print the message
                if (message != null) {
                    Any event = Any.parseFrom(message.getData());
//                    System.out.println("Any: " + event);

                    String typeUrl = event.getTypeUrl();

                    int indexOfSlash = typeUrl.indexOf("/");
                    String type = typeUrl.substring(indexOfSlash + 1);

                    if (type.contains("platform")) {

                        Class<? extends com.google.protobuf.Message> cls = (Class<? extends com.google.protobuf.Message>) Class.forName(type);
                        var unpack = event.unpack(cls);
                        String print = JsonFormat.printer().includingDefaultValueFields().omittingInsignificantWhitespace().print(unpack);
                        System.out.println("Print: " + print);
                    } else {
                        System.out.println("Is not platform: " + type);

                    }
                    System.out.println("");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}