package otterjet;

import java.nio.ByteBuffer;

@FunctionalInterface
public interface MessageDeserializer {
  DeserializedMessage deserializeMessage(ByteBuffer buffer);
}
