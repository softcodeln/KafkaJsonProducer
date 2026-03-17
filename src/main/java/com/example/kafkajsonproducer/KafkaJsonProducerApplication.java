package com.example.kafkajsonproducer;

import com.example.kafkajsonproducer.config.KafkaProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(KafkaProperties.class)
public class KafkaJsonProducerApplication {

    public static void main(String[] args) {
        SpringApplication.run(KafkaJsonProducerApplication.class, args);
    }

}
