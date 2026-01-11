package com.astrodev.shared.http.response;

import com.fasterxml.jackson.annotation.JsonInclude;

public record HttpErrorResponseData(@JsonInclude(JsonInclude.Include.NON_NULL) Object data,
                                    @JsonInclude(JsonInclude.Include.NON_NULL) HttpErrorDetails details) {
    public static HttpErrorResponseDataBuilder builder() {
        return new HttpErrorResponseDataBuilder();
    }
}
