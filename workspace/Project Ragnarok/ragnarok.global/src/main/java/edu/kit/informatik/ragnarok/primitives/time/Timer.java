package edu.kit.informatik.ragnarok.primitives.time;

/**
 * Data class that holds an duration time.
 *
 * @author Angelo Aracri
 * @version 1.0
 */
public final class Timer {

	/**
	 * The duration in millis.
	 */
	private long duration;
	/**
	 * The time left millis.
	 */
	private long timeLeft;

	/**
	 * Create a TimeDependency by duration time.
	 *
	 * @param duration
	 *            the duration time in millis
	 */
	public Timer(long duration) {
		this.duration = duration;
		this.timeLeft = duration;
	}

	/**
	 * Remove some time.
	 *
	 * @param deltaTime
	 *            the time millis
	 */
	public void removeTime(long deltaTime) {
		this.timeLeft -= deltaTime;
	}

	/**
	 * Indicates whether the time is up.
	 *
	 * @return {@code true} if time is up; {@code false} otherwise
	 */
	public boolean timeUp() {
		return this.timeLeft <= 0;
	}

	/**
	 * Reset the time.
	 */
	public void reset() {
		this.timeLeft = this.timeUp() ? this.duration + this.timeLeft : this.duration;
	}

	/**
	 * Get current progress.
	 *
	 * @return percentage as float in [0,1]
	 */
	public float getProgress() {
		return this.timeUp() ? 1 : 1 - ((1F * this.timeLeft) / this.duration);
	}

}
