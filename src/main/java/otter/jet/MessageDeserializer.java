package otter.jet;

import java.nio.ByteBuffer;

@FunctionalInterface
public interface MessageDeserializer {
  DeserializedMessage deserializeMessage(ByteBuffer buffer);
}
