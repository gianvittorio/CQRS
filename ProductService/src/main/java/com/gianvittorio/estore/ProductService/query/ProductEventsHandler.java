package com.gianvittorio.estore.ProductService.query;

import com.gianvittorio.estore.ProductService.core.data.ProductEntity;
import com.gianvittorio.estore.ProductService.core.data.ProductRepository;
import com.gianvittorio.estore.ProductService.core.event.ProductCreatedEvent;
import com.gianvittorio.estore.core.event.ProductReservationCanceledEvent;
import com.gianvittorio.estore.core.event.ProductReservedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.messaging.interceptors.ExceptionHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@ProcessingGroup("product-group")
@RequiredArgsConstructor
@Slf4j
public class ProductEventsHandler {

    private final ProductRepository productRepository;

    @ExceptionHandler(resultType = Exception.class)
    public void handle(Exception exception) throws Exception {
        throw exception;
    }

    @ExceptionHandler(resultType = IllegalArgumentException.class)
    public void handle(IllegalStateException exception) {

    }

    @EventHandler
    public void on(ProductCreatedEvent event) {
        ProductEntity productEntity = new ProductEntity();

        BeanUtils.copyProperties(event, productEntity);

        try {
            productRepository.save(productEntity);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    @EventHandler
    public void on(ProductReservedEvent productReservedEvent) {
        ProductEntity productEntity = productRepository.findByProductId(productReservedEvent.getProductId());

        log.debug("ProductReservedEvent: Current product quantity: {}", productReservedEvent.getQuantity());

        productEntity.setQuantity(productEntity.getQuantity() - productReservedEvent.getQuantity());

        productRepository.save(productEntity);

        log.debug("ProductReservedEvent: New product quantity: {}", productReservedEvent.getQuantity());

        log.info("ProductReservedEvent is called for productId: {} and orderId: {}", productReservedEvent.getProductId(), productReservedEvent.getOrderId());
    }

    @EventHandler
    public void on(ProductReservationCanceledEvent productReservationCanceledEvent) {
        Optional.ofNullable(productRepository.findByProductId(productReservationCanceledEvent.getProductId()))
                .ifPresent(productEntity -> {
                    int newQuantity = productEntity.getQuantity() + productReservationCanceledEvent.getQuantity();

                    log.debug("ProductReservationCancelledEvent: Current product quantity: {}", productEntity.getQuantity());

                    productEntity.setQuantity(newQuantity);

                    productRepository.save(productEntity);

                    log.debug("ProductReservationCancelledEvent: New product quantity: {}", productEntity.getQuantity());
                });
    }
}
