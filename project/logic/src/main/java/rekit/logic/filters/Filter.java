package rekit.logic.filters;

import java.util.Set;

import rekit.config.GameConf;
import rekit.persistence.JarManager;
import rekit.primitives.image.AbstractImage;
import rekit.primitives.image.RGBAColor;
import rekit.util.ReflectUtils;

/**
 * This interface defines all kind of image filters for the game.
 *
 * @author Dominik Fuchss
 *
 */
public interface Filter {
	/**
	 * All filters which was found at init time.
	 */
	Set<Filter> ALL_FILTERS = ReflectUtils.loadInstances(GameConf.SEARCH_PATH, JarManager.SYSLOADER, Filter.class);

	/**
	 * Search for filter instance by class.
	 *
	 * @param filter
	 *            the filter class
	 * @return the filter or {@code null} if not found
	 * @see #ALL_FILTERS
	 */
	static Filter get(Class<? extends Filter> filter) {
		for (Filter f : Filter.ALL_FILTERS) {
			if (f.getClass() == filter) {
				return f;
			}
		}
		return null;
	}

	/**
	 * This boolean indicates whether this filter can be applied pixel per pixel
	 * (fast).
	 *
	 * @return {@code true} if {@link #apply(RGBAColor)} shall be used
	 */
	boolean isApplyPixel();

	/**
	 * This boolean indicates whether this filter can be applied pixel per pixel
	 * (slow).
	 *
	 * @return {@code true} if {@link #apply(AbstractImage)} shall be used
	 */
	boolean isApplyImage();

	/**
	 * Indicates whether the internal state of the {@link Filter} has been
	 * changed.
	 *
	 * @return {@code true} if state changed, {@code false} otherwise
	 */
	default boolean changed() {
		return false;
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
