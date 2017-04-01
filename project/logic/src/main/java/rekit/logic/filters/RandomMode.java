package rekit.logic.filters;

import java.util.Arrays;

import rekit.config.GameConf;
import rekit.primitives.image.RGBAColor;
import rekit.util.ROContainer;
import rekit.util.ReflectUtils.LoadMe;
import rekit.util.ThreadUtils;

/**
 * This filter realizes a filter which will map a color to a random color.
 *
 * @author Dominik Fuchss
 *
 */
@LoadMe
public final class RandomMode implements Filter {
	/**
	 * The one and only instance of {@link RandomMode}.
	 */
	public static final ROContainer<RandomMode> INSTANCE = new ROContainer<>();

	/**
	 * The mapping for all colors.
	 */
	private Integer[] map = new Integer[256 << 16];
	/**
	 * Indicates whether the internal state has been changed.
	 */
	private boolean changed = false;

	/**
	 * Create a new RandomMode filter.
	 */
	private RandomMode() {
		ThreadUtils.runDaemon(RandomMode.class.getSimpleName(), this::periodicallyReset);
		RandomMode.INSTANCE.setE(this);
	}

	/**
	 * Update random values periodically.
	 */
	private void periodicallyReset() {
		while (true) {
			if (!this.changed) {
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
		Integer mapping = this.map[color.red + (color.green << 8) + (color.blue << 16)];
		if (mapping == null) {
			int red = GameConf.PRNG.nextInt(256);
			int green = GameConf.PRNG.nextInt(256);
			int blue = GameConf.PRNG.nextInt(256);
			mapping = this.map[color.red + (color.green << 8) + (color.blue << 16)] = (red << 16) | (green << 8) | blue;
		}
		return new RGBAColor(mapping | (color.alpha << 24));
	}

	@Override
	public boolean changed() {
		if (this.changed) {
			this.changed = false;
			return true;
		}
		return false;
	}

	@Override
	public RGBAColor apply(RGBAColor color) {
		return this.getMapping(color);
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