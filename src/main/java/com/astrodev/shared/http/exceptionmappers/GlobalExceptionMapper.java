package com.astrodev.shared.http.exceptionmappers;


import com.astrodev.shared.http.HttpErrorDetails;
import com.astrodev.shared.http.HttpErrorResponseData;
import com.astrodev.shared.http.HttpResponse;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.Logger;

import java.util.HashMap;

@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Throwable> {
    private final Logger LOG = Logger.getLogger(GlobalExceptionMapper.class);

    @Override
    public Response toResponse(Throwable exception) {
        int status = 500;
        var errorDetails = HttpErrorDetails.fromThrowable(exception);
        var errorData = new HashMap<String, Object>();

        LOG.info("Handling exception");

        // Customize based on exception type
        if (exception instanceof WebApplicationException webEx) {
            status = webEx.getResponse().getStatus();
        } else if (exception instanceof ValidationException) {
            status = 400;
            if (exception instanceof ConstraintViolationException constraintViolationException) {
                errorData.put("violations", constraintViolationException.getConstraintViolations());
            }
        }

        return Response
                .status(status)
                .entity(HttpResponse.error(new HttpErrorResponseData(
                        errorData,
                        errorDetails
                ))).build();
    }
}