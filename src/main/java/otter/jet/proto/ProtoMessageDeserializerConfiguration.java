package otter.jet.proto;

import java.io.File;
import java.io.FileNotFoundException;
import otter.jet.MessageDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.AnnotatedTypeMetadata;

@Configuration
@ConditionalOnProperty(value = "read.mode", havingValue = "proto")
public class ProtoMessageDeserializerConfiguration {

  @Bean
  @Conditional(MessageTypeNameNotDefined.class)
  public MessageDeserializer anyProtoMessageDeserializer(
      @Value("${read.proto.pathToDescriptor}") String pathToDescriptor)
      throws FileNotFoundException {
    String fullDescFile = readDescriptorFile(pathToDescriptor);
    return new ProtoBufMessageDeserializer(
        fullDescFile,
        new FromAnyProtoMessageTypeNameSelector(),
        new AnyProtoMessageToDynamicMessageDeserializer());
  }

  @Bean
  @ConditionalOnProperty(value = "read.proto.messageTypeName")
  public MessageDeserializer simpleProtoMessageDeserializer(
      @Value("${read.proto.pathToDescriptor}") String pathToDescriptor,
      @Value("${read.proto.messageTypeName}") String messageTypeName)
      throws FileNotFoundException {
    String fullDescFile = readDescriptorFile(pathToDescriptor);
    return new ProtoBufMessageDeserializer(
        fullDescFile,
        new ProvidedProtoMessageTypeNameSelector(messageTypeName),
        new SimpleProtoMessageToDynamicMessageDeserializer());
  }

  private String readDescriptorFile(String pathToDesc) throws FileNotFoundException {
    File file = new File(pathToDesc);
    if (!file.exists()) {
      throw new FileNotFoundException("File not found!");
    }
    return file.getPath();
  }

  static class MessageTypeNameNotDefined implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
      return !context.getEnvironment().containsProperty("read.proto.messageTypeName");
    }
  }
}
