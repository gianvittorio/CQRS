package com.gianvittorio.estore.ProductService.web.controller;

import lombok.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private Environment environment;

    public String getPort() {
        return environment.getProperty("local.server.port");
    }

    @PostMapping(produces = MediaType.TEXT_PLAIN_VALUE)
    public String createProduct() {
        return "http post is handled on " + getPort();
    }

    @GetMapping(produces = MediaType.TEXT_PLAIN_VALUE)
    public String getProduct() {
        return "http get is handled on " + getPort();
    }

    @PutMapping(produces = MediaType.TEXT_PLAIN_VALUE)
    public String updateProduct() {
        return "http put is handled on " + getPort();
    }

    @DeleteMapping(produces = MediaType.TEXT_PLAIN_VALUE)
    public String deleteProduct() {
        return "http delete is handled on " + getPort();
    }
}
