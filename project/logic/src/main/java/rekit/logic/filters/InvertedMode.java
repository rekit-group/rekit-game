package rekit.logic.filters;

import rekit.primitives.image.Filter;
import rekit.primitives.image.RGBAColor;
import rekit.primitives.image.RGBColor;
import rekit.util.ReflectUtils.LoadMe;

/**
 * This filter realizes a filter which will invert all colors.
 *
 * @author Dominik Fuchss
 *
 */
@LoadMe
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

	@Override
	public boolean isApplyImage() {
		return false;
	}

}