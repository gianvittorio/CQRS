package com.gianvittorio.estore.OrdersService.OrdersService.command.rest;

import com.gianvittorio.estore.OrdersService.OrdersService.command.CreateOrderCommand;
import com.gianvittorio.estore.OrdersService.OrdersService.command.rest.model.CreateOrderRestModel;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrdersCommandController {

    private final CommandGateway commandGateway;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public String createOrder(@Valid @RequestBody CreateOrderRestModel createOrderRestModel) {
        CreateOrderCommand createOrderCommand = CreateOrderCommand.builder()
                .productId(createOrderRestModel.getProductId())
                .quantity(createOrderRestModel.getQuantity())
                .addressId(createOrderRestModel.getAddressId())
                .build();

        return commandGateway.sendAndWait(createOrderCommand);
    }
}
