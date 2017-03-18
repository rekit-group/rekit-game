package rekit.logic.gameelements.inanimate.filters;

import rekit.logic.filters.GrayScaleMode;
import rekit.primitives.image.Filter;
import rekit.util.ReflectUtils.LoadMe;

/**
 * Realizes a {@link FilterBox} with {@link GrayScaleMode} as {@link Filter}.
 *
 * @author Dominik Fuchss
 *
 */
@LoadMe
public final class GrayBox extends FilterBox {
	/**
	 * Create the filter.
	 */
	protected GrayBox() {
		super(new GrayScaleMode());
	}
}
