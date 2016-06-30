package edu.kit.informatik.ragnarok.util;

public final class ThreadUtils {
	private ThreadUtils() {
	}

	public static final boolean sleep(long time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			return false;
		}
		return true;
	}

	public static final void runDaemon(Runnable r) {
		ThreadUtils.runThread(r, true);
	}

	public static final void runThread(Runnable r) {
		ThreadUtils.runThread(r, false);
	}

	public static final void runThread(Runnable r, boolean daemon) {
		Thread t = new Thread(r);
		t.setDaemon(daemon);
		t.start();
	}

}
