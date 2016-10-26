package edu.kit.informatik.ragnarok.logic.gameelements.inanimate.filters;

import edu.kit.informatik.ragnarok.logic.filters.InvertedMode;
import edu.kit.informatik.ragnarok.primitives.image.Filter;
import edu.kit.informatik.ragnarok.util.ReflectUtils.LoadMe;

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