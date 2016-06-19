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

}
