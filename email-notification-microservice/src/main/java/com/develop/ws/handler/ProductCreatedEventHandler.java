package com.develop.ws.handler;

import com.develop.ws.ProductCreatedEvent;
import com.develop.ws.exception.NotRetryableException;
import com.develop.ws.exception.RetryableException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
@KafkaListener(topics = "product-created-events-topic" /*, groupId = "product-created-events" */)
@RequiredArgsConstructor
public class ProductCreatedEventHandler {
    private final RestTemplate restTemplate;

    @KafkaHandler
    public void handle(ProductCreatedEvent productCreatedEvent) {
        log.info("Received a new event: {}", productCreatedEvent.getTitle());

        // simulate throwing retryable exception with mockservice status 200
        String requestUrl = "http://localhost:8082/response/200";

        try {
            ResponseEntity<String> response = restTemplate.exchange(requestUrl, HttpMethod.GET, null, String.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("Received response: {}", response.getBody());
            }
        } catch (ResourceAccessException exception) {
            log.error(exception.getMessage());
            throw new RetryableException(exception);
        } catch (HttpServerErrorException exception) {
            log.error(exception.getMessage());
            throw new NotRetryableException(exception);
        }
    }
}