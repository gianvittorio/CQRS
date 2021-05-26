package com.gianvittorio.estore.OrdersService.OrdersService.query;

import com.gianvittorio.estore.OrdersService.OrdersService.core.data.OrderEntity;
import com.gianvittorio.estore.OrdersService.OrdersService.core.data.OrdersRepository;
import com.gianvittorio.estore.OrdersService.OrdersService.core.model.OrderSummary;
import lombok.RequiredArgsConstructor;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderQueriesHandler {

    private final OrdersRepository ordersRepository;

    @QueryHandler
    public OrderSummary findOrder(FindOrderQuery findOrderQuery) {
        OrderEntity orderEntity = ordersRepository.findByOrderId(findOrderQuery.getOrderId());

        return new OrderSummary(orderEntity.getOrderId(), orderEntity.getOrderStatus(), "");
    }
}
