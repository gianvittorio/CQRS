package com.gianvittorio.estore.ProductService.query;

import com.gianvittorio.estore.ProductService.core.data.ProductEntity;
import com.gianvittorio.estore.ProductService.core.data.ProductRepository;
import com.gianvittorio.estore.ProductService.query.rest.model.ProductRestModel;
import lombok.RequiredArgsConstructor;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@ProcessingGroup("product-group")
@RequiredArgsConstructor
public class ProductsQueryHandler {

    private final ProductRepository productRepository;

    @QueryHandler
    public List<ProductRestModel> findProducts(FindProductsQuery findProductsQuery) {
        List<ProductRestModel> productsRest = new ArrayList<>();

        List<ProductEntity> storedProducts = productRepository.findAll();
        storedProducts.forEach(storedProduct -> {
            ProductRestModel productRest = new ProductRestModel();
            BeanUtils.copyProperties(storedProduct, productRest);

            productsRest.add(productRest);
        });

        return productsRest;
    }
}
