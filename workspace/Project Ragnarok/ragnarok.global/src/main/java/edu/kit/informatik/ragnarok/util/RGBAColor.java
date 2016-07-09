package edu.kit.informatik.ragnarok.util;

/**
 * This class defines a Color with RGBA channels
 *
 * @author Dominik Fuchß
 *
 */
public class RGBAColor {
	/**
	 * The red channel
	 */
	public final int red;
	/**
	 * The green channel
	 */
	public final int green;
	/**
	 * The blue channel
	 */
	public final int blue;
	/**
	 * The alpha channel
	 */
	public final int alpha;

	/**
	 * Create a new RGBA Color
	 *
	 * @param r
	 *            the red channel
	 * @param g
	 *            the green channel
	 * @param b
	 *            the blue channel
	 * @param a
	 *            the alpha channel
	 */
	public RGBAColor(int r, int g, int b, int a) {
		this.red = r;
		this.green = g;
		this.blue = b;
		this.alpha = a;
	}

	/**
	 * Darken the color
	 *
	 * @param p
	 *            the percentage
	 * @return the darken color
	 */
	public RGBAColor darken(float p) {
		return new RGBAColor((int) (this.red * p), (int) (this.green * p), (int) (this.blue * p), (this.alpha));
	}

}
