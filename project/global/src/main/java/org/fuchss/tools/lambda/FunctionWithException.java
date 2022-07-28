package org.fuchss.tools.lambda;

import java.util.function.Function;

/**
 * Same as {@link Function} but with {@link Exception}.
 * 
 * @author Dominik Fuchss
 *
 * @param <I>
 *            the input type
 * @param <O>
 *            the output type
 */
@FunctionalInterface
public interface FunctionWithException<I, O> {
	O apply(I i) throws Exception;
}
