package org.fuchss.tools.lambda;

/**
 * Same as {@link VoidFunction} but with {@link Exception}.
 *
 * @author Dominik Fuchss
 *
 */
public interface VoidFunctionWithException {
	void execute() throws Exception;
}
