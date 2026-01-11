package com.astrodev.shared.http.exceptionmappers;

import com.astrodev.shared.http.response.HttpErrorDetails;
import com.astrodev.shared.http.response.HttpErrorResponseData;
import com.astrodev.shared.http.response.HttpResponse;
import io.quarkus.security.UnauthorizedException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class UnauthorizedExceptionMapper implements ExceptionMapper<UnauthorizedException> {
    @Override
    public Response toResponse(UnauthorizedException exception) {
        return Response.status(Response.Status.UNAUTHORIZED)
                .entity(HttpResponse.error(new HttpErrorResponseData(
                        null,
                        HttpErrorDetails.fromThrowable(exception)
                ))).build();
    }
}
