package edu.kit.informatik.ragnarok.core;

/**
 * This class manages the time of the game and replaces
 * {@link System#currentTimeMillis()}.
 *
 * @author Dominik Fuchß
 *
 */
public final class GameTime {
	/**
	 * Prevent instantiation.
	 */
	private GameTime() {
	}

	/**
	 * Time the game is paused.
	 */
	private static long paused = 0;
	/**
	 * The time when the last pause started.
	 */
	private static long started = 0;
	/**
	 * Indicates whether game is paused.
	 */
	private static boolean pause = false;

	/**
	 * Get the current time in the game.
	 *
	 * @return the current time
	 */
	public static long getTime() {
		if (GameTime.pause) {
			return GameTime.started;
		}
		return System.currentTimeMillis() - GameTime.paused;
	}

	/**
	 * Pause the game.
	 */
	public synchronized static void pause() {
		if (GameTime.pause) {
			return;
		}
		GameTime.pause = true;
		GameTime.started = System.currentTimeMillis();
	}

	/**
	 * Resume from pause.
	 */
	public synchronized static void resume() {
		if (!GameTime.pause) {
			return;
		}
		GameTime.pause = false;
		GameTime.paused += System.currentTimeMillis() - GameTime.started;

	}
}
