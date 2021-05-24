package com.gianvittorio.estore.OrdersService.OrdersService.saga;

import com.gianvittorio.estore.OrdersService.OrdersService.event.OrderCreatedEvent;
import com.gianvittorio.estore.core.command.ReservedProductCommand;
import com.gianvittorio.estore.core.event.ProductReservedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandCallback;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.commandhandling.CommandResultMessage;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;

@Saga
@Slf4j
public class OrderSaga {

    @Autowired
    private transient CommandGateway commandGateway;

    @StartSaga
    @SagaEventHandler(associationProperty = "orderId")
    public void handle(OrderCreatedEvent event) {
        ReservedProductCommand reservedProductCommand = ReservedProductCommand.builder()
                .orderId(event.getOrderId())
                .productId(event.getProductId())
                .quantity(event.getQuantity())
                .userId(event.getUserId())
                .build();

        log.info("OrderCreatedEvent handled for orderId: {} and productId: {}", reservedProductCommand.getOrderId(), reservedProductCommand.getProductId());

        commandGateway.send(reservedProductCommand,
                new CommandCallback<ReservedProductCommand, Object>() {

                    @Override
                    public void onResult(CommandMessage<? extends ReservedProductCommand> commandMessage, CommandResultMessage<?> commandResultMessage) {
                        if (commandResultMessage.isExceptional()) {
                            // Start a compensating transaction
                        }
                    }
                }
        );
    }

    @SagaEventHandler(associationProperty = "orderId")
    public void handle(ProductReservedEvent event) {
        // Process user payment
        log.info("ProductReservedEvent is called for productId: {} and orderId: {}", event.getProductId(), event.getOrderId());
    }
}
