package rekit.util;

import rekit.util.LambdaUtil.RunnableWithException;

/**
 * This class realized a {@link RunnableWithException} which can executed once.
 * (After that execution the invocation has no effect anymore.)
 *
 * @author Dominik Fuchss
 *
 */
public final class Once implements RunnableWithException {
	private boolean invoked = false;
	private final RunnableWithException run;

	/**
	 * Create a Once-Object.
	 *
	 * @param run
	 *            the command which shall executed once
	 */
	public Once(RunnableWithException run) {
		this.run = run;
	}

	@Override
	public void run() throws Exception {
		if (this.invoked || this.run == null) {
			return;
		}
		this.invoked = true;
		this.run.run();
	}
}
