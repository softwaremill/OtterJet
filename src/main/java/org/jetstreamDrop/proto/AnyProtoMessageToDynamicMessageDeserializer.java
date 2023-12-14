package org.jetstreamDrop.proto;

import com.google.protobuf.Any;
import com.google.protobuf.Descriptors;
import com.google.protobuf.DynamicMessage;
import com.google.protobuf.InvalidProtocolBufferException;
import java.nio.ByteBuffer;

class AnyProtoMessageToDynamicMessageDeserializer
    implements ProtoMessageToDynamicMessageDeserializer {

  @Override
  public DynamicMessage deserialize(Descriptors.Descriptor messageDescriptor, ByteBuffer buffer)
      throws InvalidProtocolBufferException {
    return DynamicMessage.parseFrom(messageDescriptor, Any.parseFrom(buffer).getValue());
  }
}
