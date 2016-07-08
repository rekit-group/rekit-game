package edu.kit.informatik.ragnarok.util;

/**
 * This class defines a Color with RGB channels
 *
 * @author Dominik Fuchß
 *
 */
public class RGBColor implements Cloneable {
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
	 * Create a new RGBA Color
	 *
	 * @param r
	 *            the red channel
	 * @param g
	 *            the green channel
	 * @param b
	 *            the blue channel
	 */
	public RGBColor(int r, int g, int b) {
		this.red = r;
		this.green = g;
		this.blue = b;
	}

	@Override
	public RGBColor clone() {
		return new RGBColor(this.red, this.green, this.blue);
	}

	/**
	 * Darken the color
	 *
	 * @param p
	 *            the percentage
	 * @return the darken color
	 */
	public RGBColor darken(float p) {
		return new RGBColor((int) (this.red * p), (int) (this.green * p), (int) (this.blue * p));
	}

	/**
	 * Convert to {@link RGBAColor}
	 *
	 * @return the RGBAColor
	 */
	public RGBAColor toRGBA() {
		return new RGBAColor(this.red, this.green, this.blue, 255);
	}

}
