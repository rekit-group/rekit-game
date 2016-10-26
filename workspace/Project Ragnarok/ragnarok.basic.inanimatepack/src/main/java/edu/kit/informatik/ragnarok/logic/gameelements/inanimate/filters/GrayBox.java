package edu.kit.informatik.ragnarok.logic.gameelements.inanimate.filters;

import edu.kit.informatik.ragnarok.logic.filters.GrayScaleMode;
import edu.kit.informatik.ragnarok.primitives.image.Filter;
import edu.kit.informatik.ragnarok.util.ReflectUtils.LoadMe;

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
