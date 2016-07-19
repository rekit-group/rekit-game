package edu.kit.informatik.ragnarok.gui.filters;

import edu.kit.informatik.ragnarok.config.GameConf;
import edu.kit.informatik.ragnarok.util.RGBAColor;
import edu.kit.informatik.ragnarok.util.RGBColor;

public class RandomMode implements Filter {

	protected int[] valueMapping = new int[256];

	/**
	 * Flyweight getter method for getting a random value between 1 and 255 for
	 * every value between 0 and 255.
	 *
	 * @param index
	 *            the extrinsic, original color value between 0 and 255.
	 * @return the intrinsic, random color value between 1 and 255.
	 */
	protected int getMapping(int index) {
		if (this.valueMapping[index] == 0) {
			synchronized (this) {
				if (this.valueMapping[index] == 0) {
					this.valueMapping[index] = GameConf.PRNG.nextInt(256);
				}
			}
		}
		return this.valueMapping[index];
	}

	@Override
	public RGBAColor apply(RGBAColor color) {
		return new RGBAColor(this.getMapping(color.red), this.getMapping(color.green), this.getMapping(color.blue), color.alpha);
	}

	@Override
	public RGBColor apply(RGBColor color) {
		return new RGBColor(this.getMapping(color.red), this.getMapping(color.green), this.getMapping(color.blue));
	}

	@Override
	public boolean isApplyPixel() {
		return true;
	}

}