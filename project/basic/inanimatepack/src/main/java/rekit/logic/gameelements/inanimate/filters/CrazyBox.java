package rekit.logic.gameelements.inanimate.filters;

import rekit.logic.filters.RandomMode;
import rekit.primitives.image.Filter;
import rekit.util.ReflectUtils.LoadMe;

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
		super(RandomMode.INSTANCE);
	}
}