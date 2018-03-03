package rekit.util;

import org.fuchss.tools.lambda.VoidFunctionWithException;

/**
 * This class realized a {@link VoidFunctionWithException} which can executed
 * once. (After that execution the invocation has no effect anymore.)
 *
 * @author Dominik Fuchss
 *
 */
public final class Once implements VoidFunctionWithException {
	private boolean invoked = false;
	private final VoidFunctionWithException run;

	/**
	 * Create a Once-Object.
	 *
	 * @param run
	 *            the command which shall executed once
	 */
	public Once(VoidFunctionWithException run) {
		this.run = run;
	}

	@Override
	public void execute() throws Exception {
		if (this.invoked || this.run == null) {
			return;
		}
		this.invoked = true;
		this.run.execute();
	}
}
