package otter.jet.examples.avro;

import org.apache.avro.Schema;
import otter.jet.avro.RandomPersonAvro;

public class RandomAvroPersonGenerator {

  public static RandomPersonAvro randomPerson(Schema schema) {
    var faker = new com.github.javafaker.Faker();
    return new RandomPersonAvro(
        faker.number().numberBetween(1, Integer.MAX_VALUE),
        faker.name().firstName(),
        faker.bothify("????##@gmail.com"),
        faker.phoneNumber().phoneNumber(),
        schema);
  }

}
