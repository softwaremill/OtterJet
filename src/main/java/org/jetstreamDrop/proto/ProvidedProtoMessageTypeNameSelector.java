package org.jetstreamDrop.proto;

import java.nio.ByteBuffer;

public class ProvidedProtoMessageTypeNameSelector implements MessageTypeNameSelector {

  private final String messageTypeName;

  public ProvidedProtoMessageTypeNameSelector(String messageTypeName) {
    this.messageTypeName = messageTypeName;
  }

  @Override
  public String selectMessageTypeName(ByteBuffer message) {
    return messageTypeName;
  }
}
