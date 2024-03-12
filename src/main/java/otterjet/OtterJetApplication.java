package otterjet;

import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class OtterJetApplication {

  public static void main(String[] args) {
    createApplicationBuilder().run(args);
  }

  public static SpringApplicationBuilder createApplicationBuilder() {
    return new SpringApplicationBuilder(OtterJetApplication.class).bannerMode(Banner.Mode.OFF);
  }
}
