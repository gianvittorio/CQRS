package com.gianvittorio.estore.ProductService.query;

import com.gianvittorio.estore.ProductService.core.data.ProductEntity;
import com.gianvittorio.estore.ProductService.core.data.ProductRepository;
import com.gianvittorio.estore.ProductService.core.event.ProductCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductEventsHandler {

    private final ProductRepository productRepository;

    @EventHandler
    public void on(ProductCreatedEvent event) {
        ProductEntity productEntity = new ProductEntity();

        BeanUtils.copyProperties(event, productEntity);

        productRepository.save(productEntity);
    }
}
