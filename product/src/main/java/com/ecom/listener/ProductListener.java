package com.ecom.listener;

import com.ecom.schema.product.ProductEvent;
import com.ecom.schema.product.ProductEventKey;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import static com.ecom.constants.ApplicationConstants.TOPIC_NAME;

@Slf4j
@Component
public class ProductListener {

    @KafkaListener(topics = TOPIC_NAME)
    public void listen(ConsumerRecord<ProductEventKey, ProductEvent> record) {

        ProductEventKey productEventKey = record.key();
        ProductEvent productEvent = record.value();
        String topicName = record.topic();
        int partition = record.partition();
        long offset = record.offset();

        log.debug("Topic Name: " + topicName);
        log.debug("Partition: " + partition);
        log.debug("Offset: " + offset);
        log.debug("Key: " + productEventKey);
        log.debug("Value: " + productEvent);
    }
}