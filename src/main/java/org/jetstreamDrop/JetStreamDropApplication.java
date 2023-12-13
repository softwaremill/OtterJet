package org.jetstreamDrop;

import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
@EntityScan
public class JetStreamDropApplication {

  public static void main(String[] args) {
    createApplicationBuilder().run(args);
  }

  public static SpringApplicationBuilder createApplicationBuilder() {
    return new SpringApplicationBuilder(JetStreamDropApplication.class).bannerMode(Banner.Mode.OFF);
  }
}
