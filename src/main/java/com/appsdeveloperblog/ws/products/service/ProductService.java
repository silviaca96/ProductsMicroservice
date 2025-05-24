package com.appsdeveloperblog.ws.products.service;

import com.appsdeveloperblog.ws.products.rest.CreateProductRestModel;

public interface ProductService {
    public String createProduct(CreateProductRestModel productRestModel);
    public String createProduct2(CreateProductRestModel productRestModel) throws Exception;

}
