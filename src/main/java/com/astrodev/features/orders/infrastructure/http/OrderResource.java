package com.astrodev.features.orders.infrastructure.http;

import com.astrodev.features.orders.application.OrderService;
import com.astrodev.features.orders.application.dtos.CreateOrderDTO;
import com.astrodev.features.orders.infrastructure.http.dtos.CreateOrderReqDTO;
import com.astrodev.shared.http.response.HttpResponse;
import com.astrodev.shared.monads.Err;
import com.astrodev.shared.monads.Ok;
import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.UUID;

@Path("/order")
public class OrderResource {
    @Inject
    JsonWebToken jwt;

    @Inject
    OrderService orderService;

    @PUT
    @Path("{id}")
    @Authenticated
    public Response createOrder(@PathParam("id") UUID id, CreateOrderReqDTO body) throws Throwable {
        var userId = this.jwt.getSubject();
        var result = this.orderService.createOrder(new CreateOrderDTO(
                id, UUID.fromString(userId), body.productId(),
                body.amount()
        ));
        return switch (result) {
            case Err(var err) -> throw err;
            case Ok(_) -> Response.status(Response.Status.CREATED).entity(HttpResponse.success(null)).build();
        };
    }
}
