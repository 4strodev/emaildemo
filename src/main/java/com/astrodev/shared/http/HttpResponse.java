package com.astrodev.shared.http;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public sealed interface HttpResponse {
    static <T> HttpSuccessResponse<T> success(T data) {
        return new HttpSuccessResponse<>(data);
    }

    static HttpErrorResponse error(HttpErrorDetails error) {
        return new HttpErrorResponse(error);
    }


    boolean ok();

    record HttpSuccessResponse<T>(@JsonInclude(JsonInclude.Include.NON_NULL) T data) implements HttpResponse {
        @JsonProperty
        @Override
        public boolean ok() {
            return true;
        }
    }

    record HttpErrorResponse(HttpErrorDetails error) implements HttpResponse {
        @Override
        @JsonProperty
        public boolean ok() {
            return false;
        }
    }
}
