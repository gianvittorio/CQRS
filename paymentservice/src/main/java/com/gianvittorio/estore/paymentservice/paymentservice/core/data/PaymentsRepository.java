package com.gianvittorio.estore.paymentservice.paymentservice.core.data;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentsRepository extends JpaRepository<PaymentEntity, String> {
}
