package otter.jet.avro;

import java.io.IOException;
import java.nio.ByteBuffer;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.BinaryDecoder;
import org.apache.avro.io.DecoderFactory;
import otter.jet.reader.DeserializedMessage;
import otter.jet.reader.MessageDeserializer;

public class AvroMessageDeserializer implements MessageDeserializer {

  private final Schema schema;

  public AvroMessageDeserializer(Schema schema) {
    this.schema = schema;
  }

  @Override
  public DeserializedMessage deserializeMessage(ByteBuffer buffer) {
    BinaryDecoder decoder = DecoderFactory.get().binaryDecoder(buffer.array(), null);
    GenericDatumReader<GenericRecord> reader = new GenericDatumReader<>(schema);

    GenericRecord read = null;
    try {
      read = reader.read(null, decoder);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return new DeserializedMessage(read.getSchema().getName(), read.toString());
  }
}