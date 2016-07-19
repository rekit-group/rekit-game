package edu.kit.informatik.ragnarok.gui.filters;

import edu.kit.informatik.ragnarok.primitives.AbstractImage;
import edu.kit.informatik.ragnarok.util.RGBAColor;
import edu.kit.informatik.ragnarok.util.RGBColor;

public interface Filter {
	/**
	 * This boolean indicates whether this filter can be applied pixel per pixel
	 * (fast) or only on the final image (slow)
	 *
	 * @return {@code true} if {@link #apply(RGBAColor)} and
	 *         {@link #apply(RGBColor)} shall be used, {@code false} otherwise
	 *         ({@link #apply(AbstractImage)})
	 */
	boolean isApplyPixel();

	/**
	 * Apply Filter
	 *
	 * @param color
	 *            the original color
	 * @return the new color
	 */
	default RGBColor apply(final RGBColor color) {
		throw new UnsupportedOperationException("Not supported by " + this.getClass().getSimpleName());
	}

	/**
	 * Apply Filter
	 *
	 * @param color
	 *            the original color
	 * @return the new color
	 */
	default RGBAColor apply(final RGBAColor color) {
		throw new UnsupportedOperationException("Not supported by " + this.getClass().getSimpleName());
	}

	/**
	 * Apply Filter
	 *
	 * @param imageData
	 *            the original data
	 * @return the new Data
	 */
	default AbstractImage apply(final AbstractImage imageData) {
		throw new UnsupportedOperationException("Not supported by " + this.getClass().getSimpleName());
	}

}
