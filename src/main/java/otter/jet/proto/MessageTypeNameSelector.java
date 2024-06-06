package otter.jet.proto;

import java.io.IOException;
import java.nio.ByteBuffer;

@FunctionalInterface
public interface MessageTypeNameSelector {
  String selectMessageTypeName(ByteBuffer message) throws IOException;
}
