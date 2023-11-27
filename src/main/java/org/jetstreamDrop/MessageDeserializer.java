package org.jetstreamDrop;

import java.nio.ByteBuffer;

@FunctionalInterface
public interface MessageDeserializer {
  String deserializeMessage(ByteBuffer buffer);
}
