package org.jetstreamDrop;

import com.google.protobuf.Any;
import io.nats.client.Connection;
import io.nats.client.JetStream;
import io.nats.client.Message;
import io.nats.client.Nats;
import io.nats.client.Subscription;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.ByteBuffer;
import java.util.ArrayDeque;
import java.util.List;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ReaderService {

    String natsServerUrl = "nats://localhost:4222"; // Change this to your NATS server URL
    String subject = "*"; // Change this to your desired subject

    @Value("${pathToDesc}")
    private String pathToDesc;

    ArrayDeque<ReadMessage> msgs = new ArrayDeque<>();

    @PostConstruct
    public void postConstruct() {
        // This method will be invoked after the service is initialized
        startMessageListener();
    }

    private void startMessageListener() {
        new Thread(() -> {
            try {
                // Connect to NATS server
                Connection natsConnection = Nats.connect(natsServerUrl);
                System.out.println("Connected to NATS server at " + natsServerUrl);

                JetStream jetStream = natsConnection.jetStream();
                System.out.println("Connected to JetStream server at " + natsServerUrl);
                // Subscribe to the subject

                Subscription subscription = jetStream.subscribe(subject);
                System.out.println("Subscribed to subject: " + subject);
                MessageDeserializer messageDeserializer = getMessageDeserializer();

                // Continuously read messages
                while (true) {
                    // Wait for a message
                    Message message = subscription.nextMessage(100);
                    // Print the message
                    if (message != null) {
                        try {

                            String typeUrl = Any.parseFrom(ByteBuffer.wrap(message.getData())).getTypeUrl();
                            String[] splittedTypeUrl = typeUrl.split("/");
                            // the last part in the type url is always the FQCN for this proto
                            var name = splittedTypeUrl[splittedTypeUrl.length - 1];
                            //This code need to be moved somewhere else

                            String s = messageDeserializer.deserializeMessage(ByteBuffer.wrap(message.getData()));
                            ReadMessage msg = new ReadMessage(message.getSubject(), name, s, message.metaData().timestamp());
                            System.out.println("deserialized msg: " + msg);
                            msgs.addFirst(msg);
                            message.ack();
                        } catch (Exception e) {
                            System.out.println("Exception " + e);

                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }).start();
    }

    public MessageDeserializer getMessageDeserializer() throws FileNotFoundException {
        File file = new File(pathToDesc);
        if (!file.exists()) {
            throw new FileNotFoundException("File not found!");
        }
        boolean isAnyProto = true;
        String msgTypeName = "";
        String fullDescFile = file.getPath();

        System.out.println("file " + fullDescFile + "  exists");
        return new ProtobufMessageDeserializer(fullDescFile, msgTypeName, isAnyProto);
    }

    public List<ReadMessage> getMsgs() {
        return msgs.stream().toList();
    }

    public List<ReadMessage> filter(String subject, String type) {
        return msgs.stream().filter(m -> {
                    if (subject != null && !subject.isBlank()) {
                        return m.subject().contains(subject);
                    }
                    return true;
                }).
                filter(m -> {
                    if (type != null && !type.isBlank()) {
                        return m.name().contains(type);
                    }
                    return true;
                }).toList();
    }
}
