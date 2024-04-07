package com.develop.ws.service;

import com.develop.ws.rest.CreateProductRestModel;

public interface ProductService {
    String createProduct(CreateProductRestModel productRestModel);
}