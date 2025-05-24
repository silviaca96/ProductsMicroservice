package com.appsdeveloperblog.ws.products.rest;

import java.util.Date;

import org.apache.catalina.connector.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.appsdeveloperblog.ws.products.service.ProductService;

@RestController
@RequestMapping("/products")
public class ProductController {

    ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

 //   @PostMapping
  //  public ResponseEntity<String> createProduct(@RequestBody CreateProductRestModel product) {
    //    String productID = productService.createProduct(product);
      //  return ResponseEntity.status(Response.SC_CREATED).body(productID);
   // }

    
    @PostMapping
    public ResponseEntity<Object> createProduct2(@RequestBody CreateProductRestModel product) {
        String productID;
        try {
            productID = productService.createProduct2(product);
        } catch (Exception e) {
    
            e.printStackTrace();
            return ResponseEntity.status(Response.SC_INTERNAL_SERVER_ERROR).body(new ErrorMessage(new Date(), e.getMessage(), "/product"));
        }
        return ResponseEntity.status(Response.SC_CREATED).body(productID);
    }
}
