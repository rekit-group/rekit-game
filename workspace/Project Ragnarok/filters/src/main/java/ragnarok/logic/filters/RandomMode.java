package ragnarok.logic.filters;

import java.util.Arrays;

import ragnarok.config.GameConf;
import ragnarok.primitives.image.Filter;
import ragnarok.primitives.image.RGBAColor;
import ragnarok.primitives.image.RGBColor;
import ragnarok.util.ReflectUtils.LoadMe;
import ragnarok.util.ThreadUtils;

/**
 * This filter realizes a filter which will map a color to a random color.
 *
 * @author Dominik Fuchss
 *
 */
@LoadMe
public final class RandomMode implements Filter {
	/**
	 * The mapping for all colors.
	 */
	private Integer[] map = new Integer[256 << 16];
	private boolean changed = false;

	public RandomMode() {
		ThreadUtils.runDaemon(RandomMode.class.getSimpleName(), this::periodicallyReset);
	}

	private void periodicallyReset() {
		while (true) {
			synchronized (this) {
				Arrays.fill(this.map, null);
				this.changed = true;
			}
			ThreadUtils.sleep(10000);
		}
	}

	/**
	 * Flyweight getter method for getting a random value between 1 and 255 for
	 * every value between 0 and 255.
	 *
	 * @param color
	 *            the extrinsic, original color
	 * @return the intrinsic, random color
	 */
	private synchronized RGBAColor getMapping(RGBAColor color) {
		if (this.map[color.red + (color.green << 8) + (color.blue << 16)] == null) {
			if (this.map[color.red + (color.green << 8) + (color.blue << 16)] == null) {
				int red = GameConf.PRNG.nextInt(256);
				int green = GameConf.PRNG.nextInt(256);
				int blue = GameConf.PRNG.nextInt(256);
				this.map[color.red + (color.green << 8) + (color.blue << 16)] = (red << 16) | (green << 8) | blue;
			}
		}
		return new RGBAColor(this.map[color.red + (color.green << 8) + (color.blue << 16)] | (color.alpha << 24));
	}

	@Override
	public boolean changed() {
		if (this.changed) {
			this.changed = false;
			return true;
		}
		return false;
	}

	/**
	 * Get the mapping of an RGBColor to RGBAColor.
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

	@Override
	public boolean isApplyImage() {
		return false;
	}

}