package ragnarok.logic.gameelements.inanimate.filters;

import ragnarok.logic.filters.RandomMode;
import ragnarok.primitives.image.Filter;
import ragnarok.util.ReflectUtils.LoadMe;

/**
 * Realizes a {@link FilterBox} with {@link RandomMode} as {@link Filter}.
 *
 * @author Dominik Fuchss
 *
 */
@LoadMe
public final class CrazyBox extends FilterBox {
	/**
	 * Create the filter.
	 */
	protected CrazyBox() {
		super(new RandomMode());
	}
}