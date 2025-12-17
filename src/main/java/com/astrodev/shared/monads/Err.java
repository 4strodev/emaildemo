package com.astrodev.shared.monads;

import java.util.function.Consumer;
import java.util.function.Function;

public record Err<R, E extends Throwable>(E error) implements Result<R, E> {
    public boolean isOk() {
        return false;
    }

    public boolean isErr() {
        return true;
    }

    @Override
    public R unwrap() throws E {
        throw error;
    }

    @Override
    public R unwrapOr(R defaultValue) {
        return defaultValue;
    }

    @Override
    public void ifErr(Consumer<E> consumer) {
        consumer.accept(error);
    }

    @Override
    public <U> Result<U, E> map(Function<R, U> mapper) {
        return Result.err(error);
    }

    @Override
    public <F extends Throwable> Result<R, F> mapErr(Function<E, F> mapper) {
        return Result.err(mapper.apply(error));
    }

    @Override
    public <U> Result<U, E> flatMap(Function<R, Result<U, E>> mapper) {
        return Result.err(error);
    }

    @Override
    public void ifOk(Consumer<R> consumer) {
    }
}
