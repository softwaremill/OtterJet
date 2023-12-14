package org.jetstreamDrop;

import java.nio.ByteBuffer;

@FunctionalInterface
public interface MessageDeserializer {
  DeserializedMessage deserializeMessage(ByteBuffer buffer);
}
