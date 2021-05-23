package com.gianvittorio.estore.OrdersService.OrdersService.core.data;

import com.gianvittorio.estore.OrdersService.OrdersService.command.OrderStatus;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "orders")
public class OrderEntity {

    @Id
    @Column(unique = true)
    private String orderId;

    private String productId;

    private String userId;

    private int quantity;

    private String addressId;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
}
