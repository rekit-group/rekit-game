package edu.kit.informatik.ragnarok.util;

import org.eclipse.swt.SWT;

import edu.kit.informatik.ragnarok.gui.View;

/**
 * This interface establishes a Listener to Components of the View.<br>
 *
 * @author Dominik Fuchß
 *
 */
public interface InputHelper {
	/**
	 * Key ID ArrowUp.
	 */
	public static final int ARROW_UP = SWT.ARROW_UP;

	/**
	 * Key ID ArrowUp.
	 */
	public static final int ARROW_DOWN = SWT.ARROW_DOWN;

	/**
	 * Key ID Arrow Left.
	 */
	public static final int ARROW_LEFT = SWT.ARROW_LEFT;

	/**
	 * Key ID Arrow Right.
	 */
	public static final int ARROW_RIGHT = SWT.ARROW_RIGHT;

	/**
	 * Key ID Space.
	 */
	public static final int SPACE = 32;

	/**
	 * Key ID Escape.
	 */
	public static final int ESCAPE = SWT.ESC;

	/**
	 * Key ID Enter.
	 */
	public static final int ENTER = 13;

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
