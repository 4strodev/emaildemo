package com.astrodev.shared.monads;

import java.util.function.Consumer;
import java.util.function.Function;

public record Ok<R, E extends Throwable>(R value) implements Result<R, E> {
    public boolean isOk() {
        return true;
    }

    public boolean isErr() {
        return false;
    }

    @Override
    public R unwrap() throws E {
        return value;
    }

    @Override
    public R unwrapOr(R defaultValue) {
        return value;
    }

    @Override
    public Result<R, E> peekErr(Consumer<E> consumer) {
        return this;
    }

    @Override
    public <U> Result<U, E> map(Function<R, U> mapper) {
        return Result.ok(mapper.apply(value));
    }

    @Override
    public <F extends Throwable> Result<R, F> mapErr(Function<E, F> mapper) {
        return Result.ok(value);
    }

    @Override
    public <U> Result<U, E> flatMap(Function<R, Result<U, E>> mapper) {
        return mapper.apply(value);
    }

    @Override
    public <F extends Throwable> Result<R, F> flatMapError(Function<E, Result<R, F>> mapper) {
        return Result.ok(value);
    }

    @Override
    public Result<R, E> peekOk(Consumer<R> consumer) {
        consumer.accept(value);
        return this;
    }
}
