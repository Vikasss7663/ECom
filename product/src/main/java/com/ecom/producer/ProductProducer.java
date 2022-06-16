package com.ecom.producer;

import com.ecom.domain.Product;
import com.ecom.schema.product.ProductEvent;
import com.ecom.schema.product.ProductEventKey;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import static com.ecom.constants.ApplicationConstants.TOPIC_NAME;

@Slf4j
public class ProductProducer {


    public static void sendProductEvent(KafkaProducer<ProductEventKey, ProductEvent> producer, Product product) {

        log.debug("Sending Product Event");

        ProductEventKey productEventKey = new ProductEventKey();
        ProductEvent productEvent = new ProductEvent();

        productEventKey.setProductId(product.getProductId());
        productEventKey.setProductName(product.getProductName());
        productEventKey.setCategoryId(product.getCategoryId());

        productEvent.setProductId(product.getProductId());
        productEvent.setProductName(product.getProductName());
        productEvent.setProductPrice(product.getProductPrice());
        productEvent.setCategoryId(product.getCategoryId());

        ProducerRecord<ProductEventKey, ProductEvent> record
                = new ProducerRecord<>(TOPIC_NAME, productEventKey, productEvent);

        producer.send(record);
    }
}
