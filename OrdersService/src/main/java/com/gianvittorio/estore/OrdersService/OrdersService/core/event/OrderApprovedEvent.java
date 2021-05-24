package com.gianvittorio.estore.OrdersService.OrdersService.core.event;

import com.gianvittorio.estore.OrdersService.OrdersService.command.OrderStatus;
import lombok.Value;

@Value
public class OrderApprovedEvent {

    private final String orderId;

    private final OrderStatus orderStatus = OrderStatus.APPROVED;
}
