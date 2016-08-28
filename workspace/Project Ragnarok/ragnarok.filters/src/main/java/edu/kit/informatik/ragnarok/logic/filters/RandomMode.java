package edu.kit.informatik.ragnarok.logic.filters;

import edu.kit.informatik.ragnarok.config.GameConf;
import edu.kit.informatik.ragnarok.primitives.image.Filter;
import edu.kit.informatik.ragnarok.primitives.image.RGBAColor;
import edu.kit.informatik.ragnarok.primitives.image.RGBColor;
import edu.kit.informatik.ragnarok.util.ReflectUtils.LoadMe;

/**
 * This filter realizes a filter which will map a color to a random color
 * 
 * @author Dominik Fuchß
 *
 */
@LoadMe
public final class RandomMode implements Filter {
	/**
	 * The mapping for all colors
	 */
	private Integer[] map = new Integer[256 << 16];

	/**
	 * Flyweight getter method for getting a random value between 1 and 255 for
	 * every value between 0 and 255.
	 *
	 * @param index
	 *            the extrinsic, original color
	 * @return the intrinsic, random color
	 */
	private RGBAColor getMapping(RGBAColor color) {
		if (this.map[color.red + (color.green << 8) + (color.blue << 16)] == null) {
			synchronized (this) {
				if (this.map[color.red + (color.green << 8) + (color.blue << 16)] == null) {
					int red = GameConf.PRNG.nextInt(256);
					int green = GameConf.PRNG.nextInt(256);
					int blue = GameConf.PRNG.nextInt(256);
					this.map[color.red + (color.green << 8) + (color.blue << 16)] = (red << 16) | (green << 8) | blue;
				}
			}
		}
		return new RGBAColor(this.map[color.red + (color.green << 8) + (color.blue << 16)] | (color.alpha << 24));
	}

	/**
	 * Get the mapping of an RGBColor to RGBAColor
	 * 
	 * @param color
	 *            the RGBColor
	 * @return the corresponding RGBAColor
	 */
	private RGBAColor getMapping(RGBColor color) {
		return this.getMapping(color.toRGBA());
	}

	@Override
	public RGBAColor apply(RGBAColor color) {
		return this.getMapping(color);
	}

	@Override
	public RGBColor apply(RGBColor color) {
		return this.getMapping(color).toRGB();
	}

	@Override
	public boolean isApplyPixel() {
		return true;
	}

}