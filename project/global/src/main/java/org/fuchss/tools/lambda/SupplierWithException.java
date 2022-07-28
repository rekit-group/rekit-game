package org.fuchss.tools.lambda;

import java.util.function.Supplier;

/**
 * Same as {@link Supplier} but with {@link Exception}.
 *
 * @author Dominik Fuchss
 *
 */
public interface SupplierWithException<O> {
	O get() throws Exception;
}
