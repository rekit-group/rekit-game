package edu.kit.informatik.ragnarok.gui.filters;

import java.util.HashMap;
import java.util.Map;

import edu.kit.informatik.ragnarok.config.GameConf;
import edu.kit.informatik.ragnarok.util.RGBAColor;
import edu.kit.informatik.ragnarok.util.RGBColor;

public class RandomMode implements Filter {

	protected Map<RGBAColor, RGBAColor> colMapping = new HashMap<>();

	/**
	 * Flyweight getter method for getting a random value between 1 and 255 for
	 * every value between 0 and 255.
	 *
	 * @param index
	 *            the extrinsic, original color
	 * @return the intrinsic, random color
	 */
	protected RGBAColor getMapping(RGBAColor color) {
		System.out.println(this.colMapping.size());
		if (!this.colMapping.containsKey(color)) {
			synchronized (this) {
				this.colMapping.put(color, new RGBAColor(GameConf.PRNG.nextInt(256), GameConf.PRNG.nextInt(256), GameConf.PRNG.nextInt(256), color.alpha));
			}
		}
		return this.colMapping.get(color);
	}
	
	protected RGBAColor getMapping(RGBColor color) {
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