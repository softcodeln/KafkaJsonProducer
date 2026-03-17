package com.example.kafkajsonproducer.Service;

import com.example.kafkajsonproducer.config.KafkaProperties;
import com.example.kafkajsonproducer.dto.Customer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class ProducerService {
    private final KafkaTemplate<String, Object>  kafkaTemplate;
    private final String topic;

    public ProducerService(KafkaTemplate<String, Object> kafkaTemplate, KafkaProperties kafkaProperties) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = kafkaProperties.topic();
    }

    public CompletableFuture<SendResult<String, Object>> sendCustomerEvent(Customer customer){
    CompletableFuture<SendResult<String, Object>> future;
        try {
            future = kafkaTemplate.send(topic, customer);
            future.whenComplete((result, throwable) -> {
                if (throwable != null) {
                    log.error(
                            "Kafka publish failed. topic={}, key={}, partition={}, offset={}, error={}",
                            topic,
                            "customer-event",
                            "unavailable",
                            "unavailable",
                            throwable.getMessage(),
                            throwable
                    );
                    return;
                }

                var metadata = result.getRecordMetadata();
                var record = result.getProducerRecord();
                log.info(
                        "Kafka publish succeeded. topic={}, key={}, value={}, partition={}, offset={}",
                        record.topic(),
                        record.key(),
                        record.value(),
                        metadata.partition(),
                        metadata.offset()
                );
            });
        } catch (Exception e) {
            log.info("Kafka publish request failed. topic={}, key={}, partition={}, offset={}, error={}",
                    topic,
                    "customer-event",
                    "unavailable",
                    "unavailable",
                    e.getMessage(),
                    e
            );
            future = CompletableFuture.failedFuture(e);
        }

        return future;
    }
}
