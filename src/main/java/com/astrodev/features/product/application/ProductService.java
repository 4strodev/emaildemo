package com.astrodev.features.product.application;

import com.astrodev.features.product.Product;
import com.astrodev.features.product.application.dtos.SaveProductDTO;
import com.astrodev.features.product.infrastructure.ProductRepository;
import com.astrodev.shared.monads.Result;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class ProductService {
    @Inject
    ProductRepository productRepository;


    @Transactional
    public Result<Void, Throwable> saveProduct(SaveProductDTO productDTO) {
        return Result.fromSupplier(() -> {
            var product = new Product();
            product.id = productDTO.id();
            product.price = productDTO.price();
            product.name = productDTO.productType();

            this.productRepository.getEntityManager().merge(product);
            return null;
        });
    }
}
