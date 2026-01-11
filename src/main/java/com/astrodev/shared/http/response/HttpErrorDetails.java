package com.astrodev.shared.http.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.io.PrintWriter;
import java.io.StringWriter;

public record HttpErrorDetails(@Nonnull String message,
                               @Nullable @JsonInclude(JsonInclude.Include.NON_NULL) String stackTrace) {
    public static HttpErrorDetails fromThrowable(Throwable err) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        err.printStackTrace(pw);

        return new HttpErrorDetails(err.getMessage(), sw.toString());
    }
}
