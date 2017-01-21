package ragnarok.logic.gameelements.inanimate.filters;

import ragnarok.logic.filters.GrayScaleMode;
import ragnarok.primitives.image.Filter;
import ragnarok.util.ReflectUtils.LoadMe;

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
