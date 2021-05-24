package com.gianvittorio.estore.OrdersService.OrdersService.command.rest;

import com.gianvittorio.estore.OrdersService.OrdersService.command.CreateOrderCommand;
import com.gianvittorio.estore.OrdersService.OrdersService.command.OrderStatus;
import com.gianvittorio.estore.OrdersService.OrdersService.command.rest.model.OrderCreateRest;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrdersCommandController {

    private final CommandGateway commandGateway;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public String createOrder(@Valid @RequestBody OrderCreateRest order) {
        String userId = "27b95829-4f3f-4ddf-8983-151ba010e35b";
        String orderId = UUID.randomUUID().toString();

        CreateOrderCommand createOrderCommand = CreateOrderCommand.builder().addressId(order.getAddressId())
                .productId(order.getProductId()).userId(userId).quantity(order.getQuantity()).orderId(orderId)
                .orderStatus(OrderStatus.CREATED).build();

        String result;

        try {
            result = commandGateway.sendAndWait(createOrderCommand);
        } catch (Exception e) {
            result = e.getLocalizedMessage();
        }

        return result;
    }
}
