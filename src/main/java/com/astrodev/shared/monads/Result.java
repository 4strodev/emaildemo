package com.astrodev.shared.monads;

import com.astrodev.shared.functionalinterfaces.ThrowableSupplier;

import java.util.function.Consumer;
import java.util.function.Function;

public sealed interface Result<R, E extends Throwable> permits Ok, Err {
    static <R, E extends Throwable> Result<R, E> ok(R value) {
        return new Ok<>(value);
    }

    static <R, E extends Throwable> Result<R, E> err(E error) {
        return new Err<>(error);
    }

    static <R, E extends Throwable> Result<R, E> fromSupplier(ThrowableSupplier<R, E> supplier) {
        try {
            var resultValue = supplier.get();
            return Result.ok(resultValue);
        } catch (Throwable t) {
            @SuppressWarnings("unchecked")
            E e = (E) t;
            return Result.err(e);
        }
    }

    boolean isOk();

    boolean isErr();

    R unwrap() throws E;

    R unwrapOr(R defaultValue);

    void ifOk(Consumer<R> consumer);

    void ifErr(Consumer<E> consumer);

    <U> Result<U, E> map(Function<R, U> mapper);

    <F extends Throwable> Result<R, F> mapErr(Function<E, F> mapper);

    <U> Result<U, E> flatMap(Function<R, Result<U, E>> mapper);
}

