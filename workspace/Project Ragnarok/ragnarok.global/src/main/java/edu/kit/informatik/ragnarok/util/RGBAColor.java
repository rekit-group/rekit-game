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
	 * Create a color by an int coded ARGB color
	 *
	 * @param color
	 *            the ARGB color
	 */
	public RGBAColor(int color) {
		this.alpha = (color >> 24) & 0xFF;
		this.red = (color >> 16) & 0xFF;
		this.green = (color >> 8) & 0xFF;
		this.blue = (color) & 0xFF;
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
	
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof RGBAColor)) {
			return false;
		}
		RGBAColor converted = (RGBAColor) other;
		return converted.red == this.red && converted.green == this.green &&
				converted.blue == this.blue && converted.alpha == this.alpha;
	}
	
	@Override
	public int hashCode() {
		return red + green + blue + alpha;
	}
	
	/**
	 * Convert to {@link RGBColor}, losing the alpha channel. 
	 *
	 * @return the RGBColor
	 */
	public RGBColor toRGB() {
		return new RGBColor(this.red, this.green, this.blue);
	}

}
