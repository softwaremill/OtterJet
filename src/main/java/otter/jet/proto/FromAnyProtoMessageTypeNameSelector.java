package otter.jet.proto;

import com.google.protobuf.Any;
import java.io.IOException;
import java.nio.ByteBuffer;

public class FromAnyProtoMessageTypeNameSelector implements MessageTypeNameSelector {

  @Override
  public String selectMessageTypeName(ByteBuffer message) throws IOException {
    String typeUrl = Any.parseFrom(message).getTypeUrl();
    String[] splittedTypeUrl = typeUrl.split("/");
    // the last part in the type url is always the FQCN for this proto
    return splittedTypeUrl[splittedTypeUrl.length - 1];
  }
}
