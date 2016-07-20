package edu.kit.informatik.ragnarok.primitives.operable;

public class OpProgress<T extends Operable<T>> {

	private boolean isStatic = false;

	/**
	 * Saved version of the start value
	 */
	private Operable<T> start;

	/**
	 * Saved version of the delta value, calculated by end-initial
	 */
	private Operable<T> delta;

	/**
	 * Constructor that takes the start and end value for calculating values in
	 * between relative to a <i>progress</i> between 0 and 1.
	 *
	 * @param start
	 *            the start value that will be returned for progress = 0
	 * @param end
	 *            the end value that will be returned for progress = 1
	 */
	public OpProgress(Operable<T> start, Operable<T> end) {
		this.start = start;
		this.delta = end.sub(start.get());

		if (start.equals(end)) {
			this.isStatic = true;
		}
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
	@SuppressWarnings("unchecked")
	public T getNow(float progress) {
		// if no change required then there must be no calculation
		return this.isStatic ? (T) this.start : this.start.add(this.delta.scalar(progress));
	}
}
