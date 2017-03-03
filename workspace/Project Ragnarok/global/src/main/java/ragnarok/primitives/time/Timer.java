package ragnarok.primitives.time;

import ragnarok.config.GameConf;
import ragnarok.core.GameTime;
import ragnarok.util.ThreadUtils;

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
	 * The last time {@link #logicLoop()} was invoked.
	 */
	private long lastTime = GameTime.getTime();

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
	 * This method shall be invoked by the logic every
	 * {@link GameConf#LOGIC_DELTA} ms.
	 */
	public void logicLoop() {
		long now = GameTime.getTime();
		this.removeTime(now - this.lastTime);
		this.lastTime = now;
	}

	/**
	 * Remove some time.
	 *
	 * @param deltaTime
	 *            the time millis
	 */
	private void removeTime(long deltaTime) {
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

	/**
	 * Subtract an offset at beginning.
	 *
	 * @param offset
	 *            the offset
	 */
	public void offset(long offset) {
		this.removeTime(offset);
	}

	/**
	 * This method can be used instead of {@link ThreadUtils#sleep(long)} if
	 * {@link GameTime#pause()} shall take effect.
	 *
	 * @param offset
	 *            the time to wait in millis
	 */
	public static void sleep(long offset) {
		Timer t = new Timer(offset);
		while (!t.timeUp()) {
			t.logicLoop();
			ThreadUtils.sleep(GameConf.LOGIC_DELTA);
		}
	}

	/**
	 * This method can be used instead to execute a job after a specified time.
	 *
	 * @param offset
	 *            the time to wait in millis
	 * @param r
	 *            the job
	 */
	public static void execute(int offset, Runnable r) {
		ThreadUtils.runThread(r.toString(), () -> {
			ThreadUtils.sleep(offset);
			r.run();
		});
	}
}
