package com.gianvittorio.estore.OrdersService.OrdersService.core.model;

import com.gianvittorio.estore.OrdersService.OrdersService.command.OrderStatus;
import lombok.Value;

@Value
public class OrderSummary {

    private final String orderId;
    private final OrderStatus orderStatus;
    private final String message;
}
