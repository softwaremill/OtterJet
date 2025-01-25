package otter.jet.avro;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.apache.avro.Schema;
import org.apache.avro.SchemaParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import otter.jet.reader.MessageDeserializer;

@Configuration
@ConditionalOnProperty(value = "read.mode", havingValue = "avro")
public class AvroMessageDeserializerConfiguration {

  @Bean
  public MessageDeserializer simpleAvroMessageDeserializer(
      @Value("${read.avro.pathToSchema}") String pathToSchema) throws IOException {
    readSchemaFile(pathToSchema);
    Schema schema = new SchemaParser().parse(readSchemaFile(pathToSchema)).mainSchema();
    return new AvroMessageDeserializer(schema);
  }

  private File readSchemaFile(String pathToDesc) throws FileNotFoundException {
    File file = new File(pathToDesc);
    if (!file.exists()) {
      throw new FileNotFoundException("File not found!");
    }
    return file;
  }

}
