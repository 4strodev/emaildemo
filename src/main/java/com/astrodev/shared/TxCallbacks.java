package com.astrodev.shared;

import jakarta.transaction.Synchronization;

import java.util.function.Consumer;

public class TxCallbacks {
    public static Synchronization afterComplete(Consumer<Integer> afterCommitCallback) {
        return new Synchronization() {
            @Override
            public void beforeCompletion() {
            }

            @Override
            public void afterCompletion(int status) {
                afterCommitCallback.accept(status);
            }
        };
    }
}
