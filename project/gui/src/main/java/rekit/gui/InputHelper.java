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
	int ARROW_UP = KeyEvent.VK_UP;

	/**
	 * Key ID ArrowUp.
	 */
	int ARROW_DOWN = KeyEvent.VK_DOWN;

	/**
	 * Key ID Arrow Left.
	 */
	int ARROW_LEFT = KeyEvent.VK_LEFT;

	/**
	 * Key ID Arrow Right.
	 */
	int ARROW_RIGHT = KeyEvent.VK_RIGHT;

	/**
	 * Key ID Space.
	 */
	int SPACE = KeyEvent.VK_SPACE;

	/**
	 * Key ID Escape.
	 */
	int ESCAPE = KeyEvent.VK_ESCAPE;

	/**
	 * Key ID Enter.
	 */
	int ENTER = KeyEvent.VK_ENTER;

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
