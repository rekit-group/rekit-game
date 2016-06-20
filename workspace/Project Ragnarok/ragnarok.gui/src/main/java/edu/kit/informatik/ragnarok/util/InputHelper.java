package edu.kit.informatik.ragnarok.util;

import org.eclipse.swt.SWT;

import edu.kit.informatik.ragnarok.gui.View;

/**
 * This interface establishes a Listener to Components of the View<br>
 *
 * @author Dominik Fuchß
 *
 */
public interface InputHelper {
	/**
	 * Key ID ArrowUp
	 */
	public static final int ARROW_UP = SWT.ARROW_UP;
	/**
	 * Key ID Arrow Left
	 */
	public static final int ARROW_LEFT = SWT.ARROW_LEFT;
	/**
	 * Key ID Arrow Right
	 */
	public static final int ARROW_RIGHT = SWT.ARROW_RIGHT;

	/**
	 * Initialize the InputHelper
	 * 
	 * @param view
	 */
	default void initialize(View view) {
		view.attachMe(this);
	}

	/**
	 * This method will invoked when a key is pressed
	 * 
	 * @param keyCode
	 *            the keycode
	 */
	void press(int keyCode);

	/**
	 * This method will invoked when a key is released
	 * 
	 * @param keyCode
	 *            the keycode
	 */
	void release(int keyCode);

}
