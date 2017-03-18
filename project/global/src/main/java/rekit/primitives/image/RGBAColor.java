package rekit.primitives.image;

import org.fuchss.configuration.annotations.ClassParser;

import rekit.parser.RGBAColorParser;

/**
 * This class defines a Color with RGBA channels.
 *
 * @author Dominik Fuchss
 *
 */
@ClassParser(RGBAColorParser.class)
public final class RGBAColor {
	/**
	 * The red channel.
	 */
	public final int red;
	/**
	 * The green channel.
	 */
	public final int green;
	/**
	 * The blue channel.
	 */
	public final int blue;
	/**
	 * The alpha channel.
	 */
	public final int alpha;

	/**
	 * Create a new RGBA Color.
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
	 * Create a color by an int coded ARGB color.
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
	 * Darken the color.
	 *
	 * @param p
	 *            the percentage
	 * @return the darken color
	 */
	public RGBAColor darken(float p) {
		return new RGBAColor((int) (this.red * p), (int) (this.green * p), (int) (this.blue * p), (this.alpha));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + this.alpha;
		result = prime * result + this.blue;
		result = prime * result + this.green;
		result = prime * result + this.red;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || this.getClass() != obj.getClass()) {
			return false;
		}
		RGBAColor other = (RGBAColor) obj;
		return this.alpha == other.alpha && this.blue == other.blue && this.green == other.green && this.red == other.red;
	}

	/**
	 * Convert to {@link RGBColor}, losing the alpha channel.
	 *
	 * @return the RGBColor
	 */
	public RGBColor toRGB() {
		return new RGBColor(this.red, this.green, this.blue);
	}

	@Override
	public String toString() {
		return "RGBA: " + this.red + "," + this.green + "," + this.blue + "," + this.alpha;
	}
}
