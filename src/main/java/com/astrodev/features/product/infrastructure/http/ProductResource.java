package com.astrodev.features.product.infrastructure.http;

import com.astrodev.features.product.application.ProductService;
import com.astrodev.features.product.application.dtos.ProductStatsDTO;
import com.astrodev.features.product.application.dtos.SaveProductDTO;
import com.astrodev.features.product.infrastructure.http.dtos.SaveProductReqDTO;
import com.astrodev.shared.http.response.HttpResponse;
import com.astrodev.shared.monads.Err;
import com.astrodev.shared.monads.Ok;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Path("/product")
public class ProductResource {
    @Inject
    ProductService productService;

    @PUT
    @Path("{id}")
    public Response saveProduct(@PathParam("id") UUID id, SaveProductReqDTO saveProductReqDTO) throws Throwable {
        var result = this.productService.saveProduct(new SaveProductDTO(
                id, saveProductReqDTO.productType(),
                saveProductReqDTO.price()
        ));

        return switch (result) {
            case Err(var err) -> throw err;
            case Ok(_) -> Response.status(201).entity(HttpResponse.success(null)).build();
        };
    }

    @GET
    @Path("{id}/stats")
    public Optional<ProductStatsDTO> getProductStat(@PathParam("id") UUID id) throws Throwable {
        var result = this.productService.findProductStats(id);

        return switch (result) {
            case Err(var err) -> throw err;
            case Ok(var stat) -> stat;
        };
    }

    @GET
    @Path("stats")
    public List<ProductStatsDTO> getProductsStats() throws Throwable {
        var result = this.productService.getProductStats();

        return switch (result) {
            case Err(var err) -> throw err;
            case Ok(var stats) -> stats;
        };
    }
}
