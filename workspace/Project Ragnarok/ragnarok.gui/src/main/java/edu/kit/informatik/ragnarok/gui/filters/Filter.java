package edu.kit.informatik.ragnarok.gui.filters;

import edu.kit.informatik.ragnarok.primitives.AbstractImage;

public interface Filter {
	/**
	 * Apply Filter
	 *
	 * @param imageData
	 *            the original data
	 * @return the new Data
	 */
	AbstractImage apply(final AbstractImage imageData);

}
