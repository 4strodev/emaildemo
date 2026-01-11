package com.astrodev.shared.http.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public sealed interface HttpResponse {
    static <T> HttpSuccessResponse<T> success(T data) {
        return new HttpSuccessResponse<>(data);
    }

    static HttpErrorResponse error(HttpErrorResponseData responseData) {
        return new HttpErrorResponse(responseData);
    }


    boolean ok();

    record HttpSuccessResponse<T>(@JsonInclude(JsonInclude.Include.NON_NULL) T data) implements HttpResponse {
        @JsonProperty
        @Override
        public boolean ok() {
            return true;
        }
    }

    record HttpErrorResponse(HttpErrorResponseData error) implements HttpResponse {
        @Override
        @JsonProperty
        public boolean ok() {
            return false;
        }
    }
}
