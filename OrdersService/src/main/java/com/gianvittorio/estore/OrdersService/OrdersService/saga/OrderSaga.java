package com.gianvittorio.estore.OrdersService.OrdersService.saga;

import com.gianvittorio.estore.OrdersService.OrdersService.command.commands.ApproveOrderCommand;
import com.gianvittorio.estore.OrdersService.OrdersService.command.commands.RejectOrderCommand;
import com.gianvittorio.estore.OrdersService.OrdersService.core.event.OrderApprovedEvent;
import com.gianvittorio.estore.OrdersService.OrdersService.core.event.OrderCreatedEvent;
import com.gianvittorio.estore.OrdersService.OrdersService.core.event.OrderRejectedEvent;
import com.gianvittorio.estore.core.command.CancelProductReservationCommand;
import com.gianvittorio.estore.core.command.ProcessPaymentCommand;
import com.gianvittorio.estore.core.command.ReservedProductCommand;
import com.gianvittorio.estore.core.data.User;
import com.gianvittorio.estore.core.event.PaymentProcessedEvent;
import com.gianvittorio.estore.core.event.ProductReservationCanceledEvent;
import com.gianvittorio.estore.core.event.ProductReservedEvent;
import com.gianvittorio.estore.core.query.FetchUserPaymentDetailsQuery;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandCallback;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.commandhandling.CommandResultMessage;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.deadline.DeadlineManager;
import org.axonframework.deadline.annotation.DeadlineHandler;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Saga
@Slf4j
public class OrderSaga {

    public static final String PAYMENT_PROCESSING_DEADLINE = "payment-processing-deadline";

    @Autowired
    private transient CommandGateway commandGateway;

    @Autowired
    private transient QueryGateway queryGateway;

    @Autowired
    private transient DeadlineManager deadlineManager;

    private String scheduleId;

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
    public void handle(ProductReservedEvent productReservedEvent) {
        // Process user payment
        log.info("ProductReservedEvent is called for productId: {} and orderId: {}", productReservedEvent.getProductId(), productReservedEvent.getOrderId());

        FetchUserPaymentDetailsQuery fetchUserPaymentDetailsQuery = new FetchUserPaymentDetailsQuery(productReservedEvent.getUserId());

        User userPaymentDetails = null;
        try {
            userPaymentDetails = queryGateway.query(fetchUserPaymentDetailsQuery, ResponseTypes.instanceOf(User.class)).join();
        } catch (Exception e) {
            log.error(e.getMessage());
            // Start compensating transaction

            cancelProductReservation(productReservedEvent, e.getMessage());
            return;
        }

        if (userPaymentDetails == null) {
            // Start compensating transaction

            cancelProductReservation(productReservedEvent, "Could not fetch user payment details");
            return;
        }

        log.info("Successfully fetched user payment details for user {}", userPaymentDetails.getFirstName());

        scheduleId = deadlineManager.schedule(
                Duration.of(120, ChronoUnit.SECONDS),
                PAYMENT_PROCESSING_DEADLINE,
                productReservedEvent
        );

        ProcessPaymentCommand processPaymentCommand = ProcessPaymentCommand.builder()
                .orderId(productReservedEvent.getOrderId())
                .paymentDetails(userPaymentDetails.getPaymentDetails())
                .paymentId(UUID.randomUUID().toString())
                .build();

        String result = null;
        try {
            result = commandGateway.sendAndWait(processPaymentCommand, 10, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error(e.getMessage());
            // Start compensating transaction
            cancelProductReservation(productReservedEvent, e.getMessage());

            return;
        }

        if (result == null) {
            log.info("The ProcessPaymentCommand resulted in NULL. Initiating a compensating transaction");
            // Start compensating transaction
            cancelProductReservation(productReservedEvent, "Could not process user payment with provided payment details");
        }
    }

    private void cancelProductReservation(ProductReservedEvent productReservedEvent, String reason) {

        cancelDeadline();

        CancelProductReservationCommand cancelProductReservationCommand = CancelProductReservationCommand.builder()
                .orderId(productReservedEvent.getOrderId())
                .quantity(productReservedEvent.getQuantity())
                .productId(productReservedEvent.getProductId())
                .userId(productReservedEvent.getUserId())
                .reason(reason)
                .build();

        commandGateway.send(cancelProductReservationCommand);
    }

    @SagaEventHandler(associationProperty = "orderId")
    public void handle(PaymentProcessedEvent paymentProcessedEvent) {

        cancelDeadline();

        // Send an ApproveOrderCommand
        ApproveOrderCommand approveOrderCommand = new ApproveOrderCommand(paymentProcessedEvent.getOrderId());

        commandGateway.send(approveOrderCommand);
    }

    private void cancelDeadline() {
        if (scheduleId == null) {
            return;
        }

        deadlineManager.cancelSchedule(PAYMENT_PROCESSING_DEADLINE, scheduleId);

        scheduleId = null;
    }

    @EndSaga
    @SagaEventHandler(associationProperty = "orderId")
    public void handle(OrderApprovedEvent orderApprovedEvent) {
        log.info("Order is approved. Order saga is complete for orderId: {}", orderApprovedEvent.getOrderId());

        //SagaLifecycle.end();
    }

    @SagaEventHandler(associationProperty = "orderId")
    public void handle(ProductReservationCanceledEvent productReservationCanceledEvent) {
        // Create and send a RejectOrderCommand
        RejectOrderCommand rejectOrderCommand = new RejectOrderCommand(productReservationCanceledEvent.getOrderId(), productReservationCanceledEvent.getReason());

        commandGateway.send(rejectOrderCommand);
    }

    @EndSaga
    @SagaEventHandler(associationProperty = "orderId")
    public void handle(OrderRejectedEvent orderRejectedEvent) {
        log.info("Successfully rejected order with id: {}", orderRejectedEvent.getOrderId());
    }

    @DeadlineHandler(deadlineName = PAYMENT_PROCESSING_DEADLINE)
    public void handlePaymentDeadline(ProductReservedEvent productReservedEvent) {
        log.info("Payment processing deadline took place. Sending a compensating command to cancel the product reservation");

        cancelProductReservation(productReservedEvent, "Payment timeout");
    }
}
