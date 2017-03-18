package rekit.primitives.time;

/**
 * Data class that holds an <i>start</i> and an <i>end</i> float. It can return
 * the corresponding value in between in the same ratio as a given number
 * <i>progress</i> to 0 and 1.
 *
 * @author Angelo Aracri
 * @version 1.0
 */
public final class Progress {

	/**
	 * Saved version of the start value.
	 */
	private float start;

	/**
	 * Saved version of the delta value, calculated by end-initial.
	 */
	private float delta;

	/**
	 * Constructor that takes the start and end value for calculating values in
	 * between relative to a <i>progress</i> between 0 and 1.
	 *
	 * @param start
	 *            the start value that will be returned for progress = 0
	 * @param end
	 *            the end value that will be returned for progress = 1
	 */
	public Progress(float start, float end) {
		this.start = start;
		this.delta = end - start;
	}

	/**
	 * Calculates a value between <i>start</i> and <i>end</i> in the same ratio
	 * as progress has to 0 and 1. Has no defined behavior for other numbers.
	 *
	 * @param progress
	 *            a value between 0 and 1 that defines the ratio
	 * @return the calculated value between <i>start</i> and <i>end</i>
	 *         (inclusive)
	 */
	public float getNow(float progress) {
		// if no change required then there must be no calculation
		return this.isStatic() ? this.start : this.start + this.delta * progress;
	}

	/**
	 * Returns true if start = end, which means there are no calculations
	 * required in <i>getNow(float progress)</i>.
	 *
	 * @return true if getNows output never changes, false otherwise
	 */
	public boolean isStatic() {
		return this.delta == 0;
	}
}
