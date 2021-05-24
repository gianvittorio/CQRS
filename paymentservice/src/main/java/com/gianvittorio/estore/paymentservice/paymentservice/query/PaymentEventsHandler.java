package com.gianvittorio.estore.paymentservice.paymentservice.query;

import com.gianvittorio.estore.core.event.PaymentProcessedEvent;
import com.gianvittorio.estore.paymentservice.paymentservice.core.data.PaymentEntity;
import com.gianvittorio.estore.paymentservice.paymentservice.core.data.PaymentsRepository;
import lombok.RequiredArgsConstructor;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentEventsHandler {

    private final PaymentsRepository paymentsRepository;

    @EventHandler
    public void on(PaymentProcessedEvent paymentProcessedEvent) {
        PaymentEntity paymentEntity = PaymentEntity.builder()
                .paymentId(paymentProcessedEvent.getPaymentId())
                .orderId(paymentProcessedEvent.getOrderId())
                .build();

        try {
            paymentsRepository.save(paymentEntity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
