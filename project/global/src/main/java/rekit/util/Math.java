package rekit.util;

/**
 * Faster implementation of some methods of {@link java.lang.Math Math}.
 *
 * @author Dominik Fuchss
 *
 */
public final class Math {
	/**
	 * {@link java.lang.Math#PI}.
	 */
	public static final double PI = java.lang.Math.PI;

	/**
	 * Prevent instantiation.
	 */
	private Math() {
	}

	/**
	 * Fast method of {@link java.lang.Math#sin(double)}
	 *
	 * @param in
	 *            the input
	 * @return the sin(in)
	 */
	public static double sin(double in) {
		double x = in;
		while (x < -3.14159265) {
			x += 6.28318531;
		}
		while (x > 3.14159265) {
			x -= 6.28318531;
		}

		if (x < 0) {
			return 1.27323954 * x + 0.405284735 * x * x;
		}

		return 1.27323954 * x - 0.405284735 * x * x;
	}

	/**
	 * Fast method of {@link java.lang.Math#cos(double)}
	 *
	 * @param in
	 *            the input
	 * @return the cos(in)
	 */
	public static double cos(double in) {
		return Math.sin(in + 1.57079632);
	}

	/**
	 * Fast method of {@link java.lang.Math#tan(double)}
	 *
	 * @param in
	 *            the input
	 * @return the tan(in)
	 */
	public static double tan(double in) {
		return Math.sin(in) / Math.cos(in);
	}

	/**
	 * Fast method of {@link java.lang.Math#atan(double)}
	 *
	 * @param in
	 *            the input
	 * @return the atan(in)
	 */
	public static double atan(double in) {
		return StrictMath.atan(in);
	}

	/**
	 * Fast method of {@link java.lang.Math#atan2(double, double)}
	 *
	 * @param in1
	 *            the input (1)
	 * @param in2
	 *            the input (2)
	 * @return the atan2(in1,in2)
	 */
	public static double atan2(double in1, double in2) {
		return StrictMath.atan2(in1, in2);
	}

	/**
	 * Fast method of {@link java.lang.Math#abs(float)}
	 *
	 * @param in
	 *            the input
	 *
	 * @return the abs(in)
	 */
	public static float abs(float in) {
		return (in <= 0.0F) ? 0.0F - in : in;
	}

}
