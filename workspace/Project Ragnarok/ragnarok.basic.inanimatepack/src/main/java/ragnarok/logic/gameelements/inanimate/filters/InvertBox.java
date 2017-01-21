package ragnarok.logic.gameelements.inanimate.filters;

import ragnarok.logic.filters.InvertedMode;
import ragnarok.primitives.image.Filter;
import ragnarok.util.ReflectUtils.LoadMe;

/**
 * Realizes a {@link FilterBox} with {@link InvertedMode} as {@link Filter}.
 *
 * @author Dominik Fuchss
 *
 */
@LoadMe
public final class InvertBox extends FilterBox {
	/**
	 * Create the filter.
	 */
	protected InvertBox() {
		super(new InvertedMode());
	}
}