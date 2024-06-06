package otter.jet.proto;

import com.google.protobuf.CodedInputStream;
import com.google.protobuf.Descriptors;
import com.google.protobuf.DynamicMessage;
import java.io.IOException;
import java.nio.ByteBuffer;

class SimpleProtoMessageToDynamicMessageDeserializer
    implements ProtoMessageToDynamicMessageDeserializer {
  @Override
  public DynamicMessage deserialize(Descriptors.Descriptor messageDescriptor, ByteBuffer buffer)
      throws IOException {
    return DynamicMessage.parseFrom(messageDescriptor, CodedInputStream.newInstance(buffer));
  }
}
