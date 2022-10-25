package io.github.joedegiovanni.stuff;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public interface Sneaky {

    static <T extends Exception> T wrap(Exception t) throws T {
        return (T) t;
    }

    static <T> Supplier<T> wrap(ThrowingSupplier<T> thing) {
        return thing.wrap();
    }

    static <T> Consumer<T> wrap(ThrowingConsumer<T> thing) {
        return thing.wrap();
    }

    static <T, R> Function<T, R> wrap(ThrowingFunction<T, R> thing) {
        return thing.wrap();
    }

    interface WrapCheckedException<T> {
        T wrap();
    }

    interface ThrowingRunnable extends WrapCheckedException<Runnable> {
        void run() throws Exception;

        default Runnable wrap() {
            return () -> {
                try {
                    this.run();
                } catch (Exception e) {
                    throw Sneaky.wrap(e);
                }
            };
        }
    }

    interface ThrowingFunction<I, O> extends WrapCheckedException<Function<I, O>> {
        O apply(I input) throws Exception;

        default Function<I, O> wrap() {
            return input -> {
                try {
                    return apply(input);
                } catch (Exception e) {
                    throw Sneaky.wrap(e);
                }
            };
        }
    }

    interface ThrowingSupplier<T> extends WrapCheckedException<Supplier<T>>{
        T get() throws Exception;

        default Supplier<T> wrap() {
            return () -> {
                try {
                    return get();
                } catch (Exception e) {
                    throw Sneaky.wrap(e);
                }
            };
        }

    }

    interface ThrowingConsumer<T> extends WrapCheckedException<Consumer<T>>{
        void accept(T input) throws Exception;

        default Consumer<T> wrap() {
            return input -> {
                try {
                    accept(input);
                } catch (Exception e) {
                    throw Sneaky.wrap(e);
                }
            };
        }
    }
}
