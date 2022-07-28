package org.fuchss.tools.lambda;

import java.util.function.BiFunction;

/**
 * Same as {@link BiFunction} but with {@link Exception}.
 *
 * @author Dominik Fuchss
 *
 * @param <I1>
 *            input one type
 * @param <I2>
 *            input two type
 * @param <O>
 *            output type
 */
@FunctionalInterface
public interface BiFunctionWithException<I1, I2, O> {
	O apply(I1 i1, I2 i2) throws Exception;
}
