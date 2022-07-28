package org.fuchss.tools.lambda;

import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * A converter class to convert Functions/Consumers .. with {@link Exception} to
 * "normal" Functions/Consumers/..
 *
 * @author Dominik Fuchss
 *
 */

public final class LambdaConvert {
	private LambdaConvert() {
		throw new IllegalAccessError();
	}

	/**
	 * This handler will do nothing.
	 */
	public static final Consumer<Exception> NIL = e -> {
	};
	/**
	 * This handler will print a stack trace.
	 */
	public static final Consumer<Exception> STACK_TRACE = e -> e.printStackTrace();

	// Functions ..
	/**
	 * Wrap a {@link FunctionWithException} to a {@link Function}.
	 *
	 * @param <I>
	 *            the input type
	 * @param <O>
	 *            the output type
	 * @param func
	 *            the function
	 * @param handler
	 *            the exception handler
	 * @return the wrapped function
	 */
	public static <I, O> Function<I, O> wrap(FunctionWithException<I, O> func, Consumer<Exception> handler) {
		return i -> {
			try {
				return func.apply(i);
			} catch (Exception e) {
				handler.accept(e);
				return null;
			}
		};
	}

	/**
	 * Wrap a {@link BiFunctionWithException} to a {@link BiFunction}.
	 *
	 * @param <I1>
	 *            the input one type
	 * @param <I2>
	 *            the input two type
	 * @param <O>
	 *            the output type
	 * @param func
	 *            the function
	 * @param handler
	 *            the exception handler
	 * @return the wrapped function
	 */
	public static <I1, I2, O> BiFunction<I1, I2, O> wrap(BiFunctionWithException<I1, I2, O> func, Consumer<Exception> handler) {
		return (i1, i2) -> {
			try {
				return func.apply(i1, i2);
			} catch (Exception e) {
				handler.accept(e);
				return null;
			}
		};
	}

	// Consumers & Producers ..
	/**
	 * Wrap a {@link ConsumerWithException} to a {@link Consumer}.
	 *
	 * @param <I>
	 *            the input type
	 * @param consumer
	 *            the consumer
	 * @param handler
	 *            the exception handler
	 * @return the wrapped consumer
	 */
	public static <I> Consumer<I> wrap(ConsumerWithException<I> consumer, Consumer<Exception> handler) {
		return i -> {
			try {
				consumer.accept(i);
			} catch (Exception e) {
				handler.accept(e);
			}
		};
	}

	/**
	 * Wrap a {@link SupplierWithException} to a {@link Supplier}.
	 *
	 * @param <O>
	 *            the output type
	 * @param supplier
	 *            the supplier
	 * @param handler
	 *            the exception handler
	 * @return the wrapped supplier
	 */
	public static <O> Supplier<O> wrap(SupplierWithException<O> supplier, Consumer<Exception> handler) {
		return () -> {
			try {
				return supplier.get();
			} catch (Exception e) {
				handler.accept(e);
				return null;
			}
		};
	}

	// Void Functions ..
	/**
	 * Wrap a {@link VoidFunctionWithException} to a {@link VoidFunction}.
	 *
	 * @param func
	 *            the function
	 * @param handler
	 *            the exception handler
	 * @return the wrapped function
	 */
	public static VoidFunction wrap(VoidFunctionWithException func, Consumer<Exception> handler) {
		return () -> {
			try {
				func.execute();
			} catch (Exception e) {
				handler.accept(e);
			}
		};
	}

}
