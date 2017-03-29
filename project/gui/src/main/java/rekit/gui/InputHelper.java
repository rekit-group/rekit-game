package rekit.gui;

import java.awt.event.KeyEvent;

/**
 * This interface establishes a Listener to Components of the View.<br>
 *
 * @author Dominik Fuchss
 *
 */
public interface InputHelper {
	/**
	 * Key ID ArrowUp.
	 */
	public static final int ARROW_UP = KeyEvent.VK_UP;

	/**
	 * Key ID ArrowUp.
	 */
	public static final int ARROW_DOWN = KeyEvent.VK_DOWN;

	/**
	 * Key ID Arrow Left.
	 */
	public static final int ARROW_LEFT = KeyEvent.VK_LEFT;

	/**
	 * Key ID Arrow Right.
	 */
	public static final int ARROW_RIGHT = KeyEvent.VK_RIGHT;

	/**
	 * Key ID Space.
	 */
	public static final int SPACE = KeyEvent.VK_SPACE;

	/**
	 * Key ID Escape.
	 */
	public static final int ESCAPE = KeyEvent.VK_ESCAPE;

	/**
	 * Key ID Enter.
	 */
	public static final int ENTER = KeyEvent.VK_ENTER;

	/**
	 * Initialize the InputHelper.
	 *
	 * @param view
	 *            the view of the MVC
	 */
	default void initialize(View view) {
		view.attachMe(this);
	}

	/**
	 * This method will invoked when a key is pressed.
	 *
	 * @param keyCode
	 *            the keycode
	 */
	void press(int keyCode);

	/**
	 * This method will invoked when a key is released.
	 *
	 * @param keyCode
	 *            the keycode
	 */
	void release(int keyCode);

}
