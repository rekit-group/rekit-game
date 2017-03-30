package rekit.logic.gameelements.inanimate.filters;

import rekit.logic.filters.InvertedMode;
import rekit.primitives.image.Filter;
import rekit.util.ReflectUtils.LoadMe;

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