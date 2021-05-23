package com.gianvittorio.estore.ProductService.query;

import com.gianvittorio.estore.ProductService.core.data.ProductEntity;
import com.gianvittorio.estore.ProductService.core.data.ProductRepository;
import com.gianvittorio.estore.ProductService.core.event.ProductCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.messaging.interceptors.ExceptionHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductEventsHandler {

    private final ProductRepository productRepository;

    @ExceptionHandler(resultType = Exception.class)
    public void handle(Exception exception) throws Exception{
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
}
