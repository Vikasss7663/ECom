package com.ecom.config;

import com.ecom.properties.KafkaProducerProperties;
import com.ecom.schema.product.ProductEvent;
import com.ecom.schema.product.ProductEventKey;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

import static com.ecom.constants.ApplicationConstants.SCHEMA_REGISTRY_URL;

@Configuration
@RequiredArgsConstructor
public class KafkaProducerConfig {

    private final KafkaProducerProperties kafkaProducerProperties;

    @Bean
    public KafkaProducer<ProductEventKey, ProductEvent> kafkaProducer() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProducerProperties.getBootstrapServers());
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                io.confluent.kafka.serializers.KafkaAvroSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                io.confluent.kafka.serializers.KafkaAvroSerializer.class);
        configProps.put(SCHEMA_REGISTRY_URL, kafkaProducerProperties.getSchemaRegistryUrl());
        return new KafkaProducer<>(configProps);
    }

}