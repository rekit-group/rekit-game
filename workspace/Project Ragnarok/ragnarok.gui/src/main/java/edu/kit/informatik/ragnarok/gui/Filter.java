package edu.kit.informatik.ragnarok.gui;

import org.eclipse.swt.graphics.ImageData;

public interface Filter {
	/**
	 * Apply Filter
	 *
	 * @param imageData
	 *            the original data
	 * @return the new Data
	 */
	ImageData apply(final ImageData imageData);

}
