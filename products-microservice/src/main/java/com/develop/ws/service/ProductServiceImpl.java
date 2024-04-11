package com.develop.ws.service;

import com.develop.ws.rest.CreateProductRestModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate;

    @Override
    public String createProduct(CreateProductRestModel productRestModel) throws Exception {
        String productId = UUID.randomUUID().toString();
        // Persist product details into a database (mock)

        ProductCreatedEvent productCreatedEvent = new ProductCreatedEvent(productId,
                productRestModel.getTitle(), productRestModel.getPrice(), productRestModel.getQuantity());

        // code for sending message asynchronously
        /* CompletableFuture<SendResult<String, ProductCreatedEvent>> future =
                kafkaTemplate.send("product-created-events-topic", productId, productCreatedEvent);
        future.whenComplete((result, exception) -> {
            if (exception != null) {
                log.error("******* Failed to send message: {}", exception.getMessage());
            } else {
                log.info("******* Message sent successfully: {}", result.getRecordMetadata());
            }
        });*/

        // if uncomment this line, message will send synchronously with asynchronous code
        // future.join();

        // code for sending message synchronously
        SendResult<String, ProductCreatedEvent> result =
                kafkaTemplate.send("product-created-events-topic", productId, productCreatedEvent).get();

        log.info("******* Returning product id");

        return productId;
    }
}