package otterjet.plaintext;

import otterjet.MessageDeserializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(value = "read.mode", havingValue = "plaintext")
public class PlainTextMessageDeserializerConfiguration {

  @Bean
  public MessageDeserializer plainTextMessageDeserializer() {
    return new PlainTextMessageDeserializer();
  }
}
