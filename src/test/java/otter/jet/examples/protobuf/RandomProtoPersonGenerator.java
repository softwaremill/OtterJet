package otter.jet.examples.protobuf;

import com.github.javafaker.Faker;
import org.jetbrains.annotations.NotNull;

public class RandomProtoPersonGenerator {

  @NotNull
  public static PersonProtos.Person randomPerson() {
    Faker faker = new Faker();
    return PersonProtos.Person.newBuilder()
        .setId(faker.number().numberBetween(1, Integer.MAX_VALUE))
        .setName(faker.name().firstName())
        .setEmail(faker.bothify("????##@gmail.com"))
        .addNumbers(faker.phoneNumber().phoneNumber())
        .build();
  }
}
