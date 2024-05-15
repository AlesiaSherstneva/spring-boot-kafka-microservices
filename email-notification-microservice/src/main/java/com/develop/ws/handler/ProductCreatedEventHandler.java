package com.develop.ws.handler;

import com.develop.ws.ProductCreatedEvent;
import com.develop.ws.exception.NotRetryableException;
import com.develop.ws.exception.RetryableException;
import com.develop.ws.io.ProcessedEventEntity;
import com.develop.ws.io.ProcessedEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import static org.springframework.kafka.support.KafkaHeaders.RECEIVED_KEY;

@Slf4j
@Component
@KafkaListener(topics = "product-created-events-topic" /*, groupId = "product-created-events" */)
@RequiredArgsConstructor
public class ProductCreatedEventHandler {
    private final RestTemplate restTemplate;
    private final ProcessedEventRepository processedEventRepository;

    @KafkaHandler
    @Transactional
    public void handle(@Payload ProductCreatedEvent productCreatedEvent,
                       @Header("messageId") String messageId,
                       @Header(RECEIVED_KEY) String messageKey) {
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

        try {
            processedEventRepository.save(new ProcessedEventEntity(messageId, productCreatedEvent.getProductId()));
        } catch (DataIntegrityViolationException exception) {
            throw new NotRetryableException(exception);
        }
    }
}