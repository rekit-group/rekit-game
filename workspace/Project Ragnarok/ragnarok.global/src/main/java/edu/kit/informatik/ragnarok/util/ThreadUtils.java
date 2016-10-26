package edu.kit.informatik.ragnarok.util;

/**
 * This class contains several methods for a better usability of Threads.
 *
 * @author Dominik Fuchss
 *
 */
public final class ThreadUtils {
	/**
	 * Prevent instantiation.
	 */
	private ThreadUtils() {
	}

	/**
	 * Same as {@link Thread#sleep(long)}.
	 *
	 * @param time
	 *            length of time to sleep in milliseconds
	 * @return {@code true} if successfully sleeped the time, {@code false}
	 *         otherwise
	 */
	public static final boolean sleep(long time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			return false;
		}
		return true;
	}

	/**
	 * Run a {@link Runnable} as daemon.
	 *
	 * @param r
	 *            the runnable
	 */
	public static final void runDaemon(Runnable r) {
		ThreadUtils.runThread(r, true);
	}

	/**
	 * Run a {@link Runnable} not a daemon.
	 *
	 * @param r
	 *            the runnable
	 */
	public static final void runThread(Runnable r) {
		ThreadUtils.runThread(r, false);
	}

	/**
	 * Run a {@link Runnable}.
	 *
	 * @param r
	 *            the runnable
	 * @param daemon
	 *            daemon?
	 */
	private static final void runThread(Runnable r, boolean daemon) {
		Thread t = new Thread(r);
		t.setDaemon(daemon);
		t.start();
	}

}
