package com.gianvittorio.estore.OrdersService.OrdersService.core.data;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdersRepository extends JpaRepository<OrderEntity, String> {
}
