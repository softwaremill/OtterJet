package org.jetstreamDrop.plaintext;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import org.jetstreamDrop.DeserializedMessage;
import org.jetstreamDrop.MessageDeserializer;

class PlainTextMessageDeserializer implements MessageDeserializer {
  @Override
  public DeserializedMessage deserializeMessage(ByteBuffer buffer) {
    String content = StandardCharsets.UTF_8.decode(buffer).toString();
    return new DeserializedMessage("", content);
  }
}
