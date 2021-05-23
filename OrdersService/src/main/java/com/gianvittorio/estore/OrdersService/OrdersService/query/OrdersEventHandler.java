package com.gianvittorio.estore.OrdersService.OrdersService.query;

import com.gianvittorio.estore.OrdersService.OrdersService.core.data.OrderEntity;
import com.gianvittorio.estore.OrdersService.OrdersService.core.data.OrdersRepository;
import com.gianvittorio.estore.OrdersService.OrdersService.event.OrderCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrdersEventHandler {

    private final OrdersRepository ordersRepository;

    @EventHandler
    public void on(OrderCreatedEvent event) {

        OrderEntity orderEntity = new OrderEntity();

        BeanUtils.copyProperties(event, orderEntity);

        try {
            ordersRepository.save(orderEntity);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }
}
