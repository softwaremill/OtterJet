package otter.jet.plaintext;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import otter.jet.reader.DeserializedMessage;
import otter.jet.reader.MessageDeserializer;

class PlainTextMessageDeserializer implements MessageDeserializer {
  @Override
  public DeserializedMessage deserializeMessage(ByteBuffer buffer) {
    String content = StandardCharsets.UTF_8.decode(buffer).toString();
    return new DeserializedMessage("", content);
  }
}
