package com.gianvittorio.estore.ProductService.web.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductController {

    @PostMapping(produces = MediaType.TEXT_PLAIN_VALUE)
    public String createProduct() {
        return "http post is handled";
    }

    @GetMapping(produces = MediaType.TEXT_PLAIN_VALUE)
    public String getProduct() {
        return "http get is handled";
    }

    @PutMapping(produces = MediaType.TEXT_PLAIN_VALUE)
    public String updateProduct() {
        return "http put is handled";
    }

    @DeleteMapping(produces = MediaType.TEXT_PLAIN_VALUE)
    public String deleteProduct() {
        return "http delete is handled";
    }
}
