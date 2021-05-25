package com.gianvittorio.estore.OrdersService.OrdersService.core.event;

import com.gianvittorio.estore.OrdersService.OrdersService.command.OrderStatus;
import lombok.Data;

@Data
public class OrderCreatedEvent {

    private String orderId;

    private String productId;

    private String userId;

    private int quantity;

    private String addressId;

    private OrderStatus orderStatus;
}
