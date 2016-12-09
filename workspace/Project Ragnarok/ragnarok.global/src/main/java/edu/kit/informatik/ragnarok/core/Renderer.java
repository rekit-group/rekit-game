package edu.kit.informatik.ragnarok.core;

/**
 * This interface defines an renderer, which can draw on a {@link Field}.
 *
 * @author Dominik Fuchss
 *
 */
public interface Renderer {
	/**
	 * Draw on a field.
	 *
	 * @param f
	 *            the field
	 */
	void render(Field f);
}
