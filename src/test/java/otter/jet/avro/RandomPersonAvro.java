package otter.jet.avro;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.EncoderFactory;

public record RandomPersonAvro(int id, String name, String email, String phoneNumber,
                               Schema schema) {

  public byte[] toByteArray() throws IOException {
    GenericData.Record record = new GenericData.Record(schema);
    record.put("id", id);
    record.put("name", name);
    record.put("email", email);
    record.put("numbers", List.of(phoneNumber));

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    BinaryEncoder encoder = EncoderFactory.get().binaryEncoder(outputStream, null);
    GenericDatumWriter<GenericRecord> writer = new GenericDatumWriter<>(schema);
    writer.write(record, encoder);
    encoder.flush();

    return outputStream.toByteArray();
  }
}
