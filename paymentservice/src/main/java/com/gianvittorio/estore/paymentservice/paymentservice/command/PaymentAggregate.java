package com.gianvittorio.estore.paymentservice.paymentservice.command;

import com.gianvittorio.estore.core.command.ProcessPaymentCommand;
import com.gianvittorio.estore.core.event.PaymentProcessedEvent;
import lombok.NoArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

@Aggregate
@NoArgsConstructor
public class PaymentAggregate {

    private String orderId;

    @AggregateIdentifier
    private String paymentId;

    @CommandHandler
    public PaymentAggregate(ProcessPaymentCommand processPaymentCommand) {
        if (processPaymentCommand.getPaymentId() == null || processPaymentCommand.getPaymentId().isBlank()) {
            throw new IllegalArgumentException("Invalid payment id was provided");
        }

        if (processPaymentCommand.getOrderId() == null || processPaymentCommand.getOrderId().isBlank()) {
            throw new IllegalArgumentException("Invalid order id was provided");
        }

        if (processPaymentCommand.getPaymentDetails() == null) {
            throw new IllegalArgumentException("No payment details were provided");
        }

        PaymentProcessedEvent paymentProcessedEvent = PaymentProcessedEvent.builder()
                .paymentId(processPaymentCommand.getPaymentId())
                .orderId(processPaymentCommand.getOrderId())
                .build();

        AggregateLifecycle.apply(paymentProcessedEvent);
    }

    @EventSourcingHandler
    public void on(PaymentProcessedEvent paymentProcessedEvent) {
        this.paymentId = paymentProcessedEvent.getPaymentId();
        this.orderId = paymentProcessedEvent.getOrderId();
    }
}
