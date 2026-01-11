package com.astrodev.shared.http.response;

public class HttpErrorResponseDataBuilder {
    private Object data;
    private HttpErrorDetails details;

    public HttpErrorResponseDataBuilder data(Object data) {
        this.data = data;
        return this;
    }

    public HttpErrorResponseDataBuilder details(HttpErrorDetails details) {
        this.details = details;
        return this;
    }

    public HttpErrorResponseDataBuilder detailsFromThrowable(Throwable error) {
        this.details = HttpErrorDetails.fromThrowable(error);
        return this;
    }

    public HttpErrorResponseData build() {
        return new HttpErrorResponseData(this.data, this.details);
    }
}
