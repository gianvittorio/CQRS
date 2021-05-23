package com.gianvittorio.estore.OrdersService.OrdersService.command;

import com.gianvittorio.estore.OrdersService.OrdersService.command.OrderStatus;
import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.util.UUID;

@Data
@Builder
public class CreateOrderCommand {
    private static final String DEFAULT_USER_ID = "27b95829-4f3f-4ddf-8983-151ba010e35b.";


    @TargetAggregateIdentifier
    private final String orderId = UUID.randomUUID().toString();

    private final String userId = DEFAULT_USER_ID;

    private final String productId;

    private final int quantity;

    private final String addressId;

    private final OrderStatus orderStatus;
}
