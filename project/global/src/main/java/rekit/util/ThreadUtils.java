package rekit.util;

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
	 * @param name
	 *            the name of the daemon
	 * @param r
	 *            the runnable
	 * @return the thread
	 */
	public static final Thread runDaemon(String name, Runnable r) {
		return ThreadUtils.runThread(name, r, true);
	}

	/**
	 * Run a {@link Runnable} not a daemon.
	 *
	 * @param name
	 *            the name of the thread
	 * @param r
	 *            the runnable
	 * @return the thread
	 */
	public static final Thread runThread(String name, Runnable r) {
		return ThreadUtils.runThread(name, r, false);
	}

	/**
	 * Run a {@link Runnable}.
	 *
	 * @param name
	 *            the name of the thread
	 * @param r
	 *            the runnable
	 * @param daemon
	 *            daemon?
	 * @return the thread
	 */
	private static final Thread runThread(String name, Runnable r, boolean daemon) {
		Thread t = new Thread(r);
		t.setName(name);
		t.setDaemon(daemon);
		t.start();
		return t;
	}

}
