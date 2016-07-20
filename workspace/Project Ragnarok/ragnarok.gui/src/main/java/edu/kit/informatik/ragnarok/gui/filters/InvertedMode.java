package edu.kit.informatik.ragnarok.gui.filters;

import edu.kit.informatik.ragnarok.primitives.image.RGBAColor;
import edu.kit.informatik.ragnarok.primitives.image.RGBColor;

public class InvertedMode implements Filter {

	@Override
	public RGBAColor apply(RGBAColor color) {
		return new RGBAColor(255 - color.red, 255 - color.green, 255 - color.blue, color.alpha == 255 ? 255 : 255 - color.alpha);
	}

	@Override
	public RGBColor apply(RGBColor color) {
		return new RGBColor(255 - color.red, 255 - color.green, 255 - color.blue);
	}

	@Override
	public boolean isApplyPixel() {
		return true;
	}

}