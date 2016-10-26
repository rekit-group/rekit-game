package edu.kit.informatik.ragnarok.logic.gameelements.inanimate.filters;

import edu.kit.informatik.ragnarok.logic.filters.RandomMode;
import edu.kit.informatik.ragnarok.primitives.image.Filter;
import edu.kit.informatik.ragnarok.util.ReflectUtils.LoadMe;

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