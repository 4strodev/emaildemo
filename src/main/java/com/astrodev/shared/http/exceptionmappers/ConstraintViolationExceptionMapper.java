package com.astrodev.shared.http.exceptionmappers;

import com.astrodev.shared.http.HttpErrorDetails;
import com.astrodev.shared.http.HttpErrorResponseData;
import com.astrodev.shared.http.HttpResponse;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.HashMap;

@Provider
public class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    @Override
    public Response toResponse(ConstraintViolationException exception) {
        var data = new HashMap<String, Object>();
        var details = HttpErrorDetails.fromThrowable(exception);

        data.put(
                "violations",
                exception.getConstraintViolations().stream().map(violation -> {
                    var mapping = new HashMap<String, Object>();
                    mapping.put("property", violation.getPropertyPath().toString());
                    mapping.put("message", violation.getMessage());
                    return mapping;
                })
        );

        return Response.status(400).entity(HttpResponse.error(new HttpErrorResponseData(data, null))).build();
    }
}
