package com.gianvittorio.estore.ProductService.command;

import com.gianvittorio.estore.ProductService.core.data.ProductLookupEntity;
import com.gianvittorio.estore.ProductService.core.data.ProductLookupRepository;
import com.gianvittorio.estore.ProductService.core.event.ProductCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventhandling.ResetHandler;
import org.springframework.stereotype.Component;

@Component
@ProcessingGroup("product-group")
@RequiredArgsConstructor
public class ProductLookupEventHandler {

    private final ProductLookupRepository productLookupRepository;

    @EventHandler
    public void on(ProductCreatedEvent event) {
        ProductLookupEntity productLookupEntity = ProductLookupEntity.builder()
                .productId(event.getProductId())
                .title(event.getTitle())
                .build();

        productLookupRepository.save(productLookupEntity);
    }

    @ResetHandler
    public void reset() {
        productLookupRepository.deleteAll();
    }
}
