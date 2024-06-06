package otter.jet.proto;

import com.google.protobuf.DescriptorProtos.FileDescriptorProto;
import com.google.protobuf.DescriptorProtos.FileDescriptorSet;
import com.google.protobuf.Descriptors.DescriptorValidationException;
import com.google.protobuf.Descriptors.FileDescriptor;
import com.google.protobuf.DynamicMessage;
import com.google.protobuf.util.JsonFormat;
import com.google.protobuf.util.JsonFormat.Printer;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import otter.jet.reader.DeserializationException;
import otter.jet.reader.DeserializedMessage;
import otter.jet.reader.MessageDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ProtoBufMessageDeserializer implements MessageDeserializer {

  private final String fullDescFile;
  private final MessageTypeNameSelector messageTypeNameSelector;
  private final ProtoMessageToDynamicMessageDeserializer protoMessageToDynamicMessageDeserializer;

  private static final Logger LOG = LoggerFactory.getLogger(ProtoBufMessageDeserializer.class);

  ProtoBufMessageDeserializer(
      String fullDescFile,
      MessageTypeNameSelector messageTypeNameSelector,
      ProtoMessageToDynamicMessageDeserializer protoMessageToDynamicMessageDeserializer) {
    this.fullDescFile = fullDescFile;
    this.messageTypeNameSelector = messageTypeNameSelector;
    this.protoMessageToDynamicMessageDeserializer = protoMessageToDynamicMessageDeserializer;
  }

  @Override
  public DeserializedMessage deserializeMessage(ByteBuffer buffer) {
    try (InputStream input = new FileInputStream(fullDescFile)) {
      String messageTypeName = messageTypeNameSelector.selectMessageTypeName(buffer);
      FileDescriptorSet set = FileDescriptorSet.parseFrom(input);
      List<FileDescriptor> descs = new ArrayList<>();
      for (FileDescriptorProto ffdp : set.getFileList()) {
        var fd = tryToReadFileDescriptor(ffdp, descs, messageTypeName);
        descs.add(fd);
      }

      final var descriptors =
          descs.stream()
              .flatMap(desc -> desc.getMessageTypes().stream())
              .toList();
      final var messageDescriptor =
          descriptors.stream()
              .filter(desc -> messageTypeName.equals(desc.getName()) || messageTypeName.equals(desc.getFullName()))
              .findFirst()
              .orElseThrow(
                  () -> {
                    var errorMsg = "No message with type: " + messageTypeName;
                    LOG.error(errorMsg);
                    return new DeserializationException(errorMsg);
                  });

      DynamicMessage message =
          protoMessageToDynamicMessageDeserializer.deserialize(messageDescriptor, buffer);

      JsonFormat.TypeRegistry typeRegistry =
          JsonFormat.TypeRegistry.newBuilder().add(descriptors).build();
      Printer printer = JsonFormat.printer().usingTypeRegistry(typeRegistry);

      String content =
          printer
              .print(message)
              .replace("\n", ""); // collapse mode
      return new DeserializedMessage(messageDescriptor.getName(), content);
    } catch (FileNotFoundException e) {
      final String errorMsg = "Cannot find descriptor file: " + fullDescFile;
      LOG.error(errorMsg, e);
      throw new DeserializationException(errorMsg);
    } catch (IOException e) {
      final String errorMsg = "Can't decode Protobuf message";
      LOG.error(errorMsg, e);
      throw new DeserializationException(errorMsg);
    }
  }

  private FileDescriptor tryToReadFileDescriptor(
      FileDescriptorProto ffdp, List<FileDescriptor> descs, String messageTypeName) {
    try {
      return FileDescriptor.buildFrom(ffdp, descs.toArray(new FileDescriptor[0]));
    } catch (DescriptorValidationException e) {
      final String errorMsg = "Can't compile proto message type: " + messageTypeName;
      LOG.error(errorMsg, e);
      throw new DeserializationException(errorMsg);
    }
  }
}
