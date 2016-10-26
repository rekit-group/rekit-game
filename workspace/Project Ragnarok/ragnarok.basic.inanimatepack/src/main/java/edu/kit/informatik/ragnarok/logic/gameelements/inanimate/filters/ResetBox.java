package edu.kit.informatik.ragnarok.logic.gameelements.inanimate.filters;

import edu.kit.informatik.ragnarok.util.ReflectUtils.LoadMe;

/**
 * Realizes a {@link FilterBox} which resets all filters.
 *
 * @author Dominik Fuchss
 *
 */
@LoadMe
public final class ResetBox extends FilterBox {
	/**
	 * Prototype Constructor.
	 */
	public ResetBox() {
		super(null);
	}
}
