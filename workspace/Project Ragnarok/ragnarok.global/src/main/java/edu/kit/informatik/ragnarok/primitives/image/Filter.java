package edu.kit.informatik.ragnarok.primitives.image;

import java.util.Set;

import edu.kit.informatik.ragnarok.primitives.image.AbstractImage;
import edu.kit.informatik.ragnarok.primitives.image.RGBAColor;
import edu.kit.informatik.ragnarok.primitives.image.RGBColor;
import edu.kit.informatik.ragnarok.util.ReflectUtils;

public interface Filter {

	/**
	 * Get all dynamic filters
	 *
	 * @return the set of all filters
	 */
	static Set<Filter> getAllFilters() {
		return ReflectUtils.loadInstances("edu.kit.informatik", Filter.class);
	}

	/**
	 * This boolean indicates whether this filter can be applied pixel per pixel
	 * (fast) or only on the final image (slow)
	 *
	 * @return {@code true} if {@link #apply(RGBAColor)} and
	 *         {@link #apply(RGBColor)} shall be used,<br>
	 * 		{@code false} otherwise ({@link #apply(AbstractImage)})
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
