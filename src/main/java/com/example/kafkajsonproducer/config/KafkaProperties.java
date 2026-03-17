package com.example.kafkajsonproducer.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Binds all properties under the "app.kafka" prefix into a single typed object.
 * This eliminates scattered @Value annotations and resolves IDE config-property warnings.
 *
 * Bound from application.yaml:
 *   app:
 *     kafka:
 *       topic: my-test-topic
 *       partitions: 3
 *       replication-factor: 1
 *       retention-ms: 604800000
 */
@ConfigurationProperties(prefix = "app.kafka")
public record KafkaProperties(
        String topic,
        int partitions,
        int replicationFactor,
        long retentionMs,
        int maxBurstSize
) {}

