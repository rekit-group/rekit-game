package ragnarok.core;

/**
 * This interface defines an renderer, which can draw on a {@link GameGrid}.
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
	void render(GameGrid f);
}
