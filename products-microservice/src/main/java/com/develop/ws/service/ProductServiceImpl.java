package com.develop.ws.service;

import com.develop.ws.rest.CreateProductRestModel;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {
    @Override
    public String createProduct(CreateProductRestModel productRestModel) {
        String productId = UUID.randomUUID().toString();
        // Persist product details into a database (mock)

        ProductCreatedEvent productCreatedEvent = new ProductCreatedEvent(productId,
                productRestModel.getTitle(), productRestModel.getPrice(), productRestModel.getQuantity());
        return null;
    }
}