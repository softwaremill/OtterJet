package org.jetstreamDrop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan
public class JetstreamDropApplication {

    public static void main(String[] args) {
        SpringApplication.run(JetstreamDropApplication.class, args);
    }

}
