package otter.jet.proto;

import com.google.protobuf.Descriptors;
import com.google.protobuf.DynamicMessage;
import java.io.IOException;
import java.nio.ByteBuffer;

interface ProtoMessageToDynamicMessageDeserializer {
  DynamicMessage deserialize(Descriptors.Descriptor messageDescriptor, ByteBuffer buffer)
      throws IOException;
}
