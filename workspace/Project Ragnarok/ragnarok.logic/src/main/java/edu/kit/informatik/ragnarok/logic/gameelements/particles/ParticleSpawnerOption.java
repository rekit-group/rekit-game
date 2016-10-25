package edu.kit.informatik.ragnarok.logic.gameelements.particles;

import edu.kit.informatik.ragnarok.config.GameConf;
import edu.kit.informatik.ragnarok.primitives.time.Progress;
import home.fox.visitors.annotations.ClassParser;

/**
 * Represents an option for a <i>ProgressDependency</i>. A ProgressDependency
 * requires a start and an end value. With the ParticleSpawnerOption these can
 * be randomly generated multiple times. Given a minimum and a maximum value for
 * each start and delta (startMin, startMax, deltaMin, deltaMax) the method
 * randomizeProgressDependency() will return the ProgressDependency with random
 * start and end values accordingly. <b>Note:</b> the delta value will be added
 * to the start value to calculate the end value. So if variation in negative
 * direction is desired you must supply a negative delta.
 *
 * @author Angelo Aracri
 * @version 1.0
 */
@ClassParser(ParticleSpawnerOptionParser.class)
public class ParticleSpawnerOption {
	/**
	 * The minimum of the start-value.
	 */
	private float startMin;
	/**
	 * The maximum of the start-value.
	 */
	private float startMax;
	/**
	 * The minimum of the delta-value.
	 */
	private float deltaMin;
	/**
	 * The maximum of the delta-value.
	 */
	private float deltaMax;

	/**
	 * Constructor that takes arguments for lower and upper start and delta
	 * values for full randomized variation.
	 *
	 * @param startMin
	 *            the minimal value for the ProgressDependencies start value
	 * @param startMax
	 *            the maximum value for the ProgressDependencies start value
	 * @param deltaMin
	 *            the minimal value for delta that will be used for the
	 *            ProgressDependencies end value
	 * @param deltaMax
	 *            the maximal value for delta that will be used for the
	 *            ProgressDependencies end value
	 */
	public ParticleSpawnerOption(float startMin, float startMax, float deltaMin, float deltaMax) {
		this.startMin = startMin;
		this.startMax = startMax;
		this.deltaMin = deltaMin;
		this.deltaMax = deltaMax;
	}

	/**
	 * Short-hand constructor that takes arguments start and delta values for no
	 * randomized variation.
	 *
	 * @param start
	 *            the value for the ProgressDependencies start value
	 * @param delta
	 *            the value for delta that will be used for the
	 *            ProgressDependencies end value
	 */
	public ParticleSpawnerOption(float start, float delta) {
		this.startMin = start;
		this.startMax = start;
		this.deltaMin = delta;
		this.deltaMax = delta;
	}

	/**
	 * Short-hand constructor that takes argument value and uses it as the start
	 * value and sets delta to 0 for no randomized variation and no change of
	 * the ProgressDependencies value.
	 *
	 * @param value
	 *            the value for the ProgressDependencies start value
	 */
	public ParticleSpawnerOption(float value) {
		this.startMin = value;
		this.startMax = value;
		this.deltaMin = 0;
		this.deltaMax = 0;
	}

	/**
	 * Returns a randomized ProgressDependency with start and end values
	 * randomly generated according to specified options (startMin, startMax,
	 * deltaMin, deltaMax).
	 *
	 * @return the randomized ProgressDependency
	 */
	public Progress randomize() {
		// calculate random start value between startMin and startMax
		// (only if startMin != startMax)
		float start = this.startMin == this.startMax ? this.startMin
				: (float) (this.startMin + GameConf.PRNG.nextDouble() * (this.startMax - this.startMin));

		// calculate random delta value between deltaMin and deltaMax
		// (only if deltaMin != deltaMax)
		float delta = Math.abs(this.deltaMin - this.deltaMax) < 1E-8 ? this.deltaMin
				: (float) (this.deltaMin + GameConf.PRNG.nextDouble() * (this.deltaMax - this.deltaMin));

		// we don't wanna optimize case delta=0 => start+delta = delta,
		// ProgressDependency does that
		return new Progress(start, start + delta);
	}

}
