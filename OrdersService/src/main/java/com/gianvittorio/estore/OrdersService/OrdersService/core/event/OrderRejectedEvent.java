package com.gianvittorio.estore.OrdersService.OrdersService.core.event;

import com.gianvittorio.estore.OrdersService.OrdersService.command.OrderStatus;
import lombok.Value;

@Value
public class OrderRejectedEvent {

    private final String orderId;
    private final String reason;
    private final OrderStatus orderStatus = OrderStatus.REJECTED;
}
