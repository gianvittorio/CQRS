package com.gianvittorio.estore.OrdersService.OrdersService.query;

import com.gianvittorio.estore.OrdersService.OrdersService.core.data.OrderEntity;
import com.gianvittorio.estore.OrdersService.OrdersService.core.data.OrdersRepository;
import com.gianvittorio.estore.OrdersService.OrdersService.core.event.OrderApprovedEvent;
import com.gianvittorio.estore.OrdersService.OrdersService.core.event.OrderCreatedEvent;
import com.gianvittorio.estore.OrdersService.OrdersService.core.event.OrderRejectedEvent;
import lombok.RequiredArgsConstructor;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OrdersEventHandler {

    private final OrdersRepository ordersRepository;

    @EventHandler
    public void on(OrderCreatedEvent orderCreatedEvent) {

        OrderEntity orderEntity = new OrderEntity();

        BeanUtils.copyProperties(orderCreatedEvent, orderEntity);

        try {
            ordersRepository.save(orderEntity);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    @EventHandler
    public void on(OrderApprovedEvent orderApprovedEvent) {
        Optional.ofNullable(ordersRepository.findByOrderId(orderApprovedEvent.getOrderId()))
                .ifPresent(orderEntity -> {
                    orderEntity.setOrderStatus(orderApprovedEvent.getOrderStatus());

                    ordersRepository.save(orderEntity);
                });
    }

    @EventHandler
    public void on(OrderRejectedEvent orderRejectedEvent) {
        Optional.ofNullable(ordersRepository.findByOrderId(orderRejectedEvent.getOrderId()))
                .ifPresent(orderEntity -> {
                    orderEntity.setOrderStatus(orderRejectedEvent.getOrderStatus());
                    ordersRepository.save(orderEntity);
                });
    }
}
