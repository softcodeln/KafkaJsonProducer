package com.example.kafkajsonproducer.config;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.config.TopicConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin.NewTopics;

@Configuration
@RequiredArgsConstructor
public class KafkaTopicConfig {

    private final KafkaProperties kafkaProperties;

    /**
     * Declares the topic bean. Spring Kafka's KafkaAdmin automatically detects
     * all NewTopic / NewTopics beans on startup and creates any missing topics
     * on the broker — it does NOT recreate or modify topics that already exist.
     *
     * TopicBuilder lets you set:
     *   - name            : topic name
     *   - partitions      : number of partitions (controls parallelism)
     *   - replicas        : replication factor (fault-tolerance; must be <= broker count)
     *   - config(key,val) : any broker-side topic config (retention, compaction, etc.)
     */
    @Bean
    public NewTopics appTopics() {
        return new NewTopics(
                TopicBuilder.name(kafkaProperties.topic())
                        .partitions(kafkaProperties.partitions())
                        .replicas(kafkaProperties.replicationFactor())
                        .config(TopicConfig.RETENTION_MS_CONFIG, String.valueOf(kafkaProperties.retentionMs()))
                        .build()
        );
    }
}
