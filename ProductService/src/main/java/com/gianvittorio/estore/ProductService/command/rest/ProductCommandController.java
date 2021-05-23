package com.gianvittorio.estore.ProductService.command.rest;

import com.gianvittorio.estore.ProductService.command.CreateProductCommand;
import com.gianvittorio.estore.ProductService.command.rest.model.CreateProductRestModel;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductCommandController {

    private final CommandGateway commandGateway;

    private final Environment environment;

    public String getPort() {
        return environment.getProperty("local.server.port");
    }

    @PostMapping(produces = MediaType.TEXT_PLAIN_VALUE)
    public String createProduct(@Valid @RequestBody CreateProductRestModel createProductRestModel) {

        CreateProductCommand createProductCommand = CreateProductCommand.builder()
                .price(createProductRestModel.getPrice())
                .quantity(createProductRestModel.getQuantity())
                .title(createProductRestModel.getTitle())
                .productId(UUID.randomUUID().toString())
                .build();

        String returnValue;
//        try {
//            returnValue = commandGateway.sendAndWait(createProductCommand);
//        } catch (Exception e) {
//            returnValue = e.getLocalizedMessage();
//        }

        returnValue = commandGateway.sendAndWait(createProductCommand);

        return returnValue;
    }

//    @GetMapping(produces = MediaType.TEXT_PLAIN_VALUE)
//    public String getProduct() {
//        return "http get is handled on " + getPort();
//    }

//    @PutMapping(produces = MediaType.TEXT_PLAIN_VALUE)
//    public String updateProduct() {
//        return "http put is handled on " + getPort();
//    }
//
//    @DeleteMapping(produces = MediaType.TEXT_PLAIN_VALUE)
//    public String deleteProduct() {
//        return "http delete is handled on " + getPort();
//    }
}
