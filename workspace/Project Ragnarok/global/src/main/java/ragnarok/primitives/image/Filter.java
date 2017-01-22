package ragnarok.primitives.image;

import java.util.Set;

import ragnarok.config.GameConf;
import ragnarok.util.ReflectUtils;

/**
 * This interface defines all kind of image filters for the game.
 *
 * @author Dominik Fuchss
 *
 */
public interface Filter {

	/**
	 * Get all dynamic filters.
	 *
	 * @return the set of all filters
	 */
	static Set<Filter> getAllFilters() {
		return ReflectUtils.loadInstances(GameConf.SEARCH_PATH, Filter.class);
	}

	/**
	 * This boolean indicates whether this filter can be applied pixel per pixel
	 * (fast).
	 *
	 * @return {@code true} if {@link #apply(RGBAColor)} and
	 *         {@link #apply(RGBColor)} shall be used
	 */
	boolean isApplyPixel();

	/**
	 * This boolean indicates whether this filter can be applied pixel per pixel
	 * (slow).
	 *
	 * @return {@code true} if {@link #apply(AbstractImage))} shall be used
	 */
	boolean isApplyImage();

	/**
	 * Apply Filter.
	 *
	 * @param color
	 *            the original color
	 * @return the new color
	 */
	default RGBColor apply(final RGBColor color) {
		throw new UnsupportedOperationException("Not supported by " + this.getClass().getSimpleName());
	}

	/**
	 * Apply Filter.
	 *
	 * @param color
	 *            the original color
	 * @return the new color
	 */
	default RGBAColor apply(final RGBAColor color) {
		throw new UnsupportedOperationException("Not supported by " + this.getClass().getSimpleName());
	}

	/**
	 * Apply Filter.
	 *
	 * @param imageData
	 *            the original data
	 * @return the new Data
	 */
	default AbstractImage apply(final AbstractImage imageData) {
		throw new UnsupportedOperationException("Not supported by " + this.getClass().getSimpleName());
	}

}
