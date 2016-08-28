package edu.kit.informatik.ragnarok.util;

import edu.kit.informatik.ragnarok.config.GameConf;
import edu.kit.informatik.ragnarok.primitives.geometry.Vec;

/**
 * This class contains several methods to calculate between units
 *
 */
public final class CalcUtil {
	/**
	 * Prevent instantiation
	 */
	private CalcUtil() {
	}

	/**
	 * Units -> Pixels
	 *
	 * @param units
	 *            the units
	 * @return the pixels
	 */
	public static int units2pixel(float units) {
		return (int) (units * GameConf.PX_PER_UNIT);
	}

	/**
	 * Units to Pixels
	 *
	 * @param pos
	 *            the position
	 * @return the position in units
	 */
	public static Vec units2pixel(Vec pos) {
		return new Vec(pos.getX() * GameConf.PX_PER_UNIT, pos.getY() * GameConf.PX_PER_UNIT);
	}

	public static float randomize(double mu, double sigma) {
		return CalcUtil.randomize((float) mu, (float) sigma);
	}

	public static float randomize(float mu, float sigma) {
		return mu + (GameConf.PRNG.nextFloat() * 2 - 1) * sigma;
	}
}
