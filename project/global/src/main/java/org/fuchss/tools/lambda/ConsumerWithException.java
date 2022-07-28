package org.fuchss.tools.lambda;

import java.util.function.Consumer;

/**
 * Same as {@link Consumer} but with {@link Exception}.
 *
 * @param <I>
 *            the input type
 * @author Dominik Fuchss
 *
 */
public interface ConsumerWithException<I> {
	void accept(I i) throws Exception;
}
