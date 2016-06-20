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
	public static final int ARROW_UP = SWT.ARROW_UP;
	public static final int ARROW_LEFT = SWT.ARROW_LEFT;
	public static final int ARROW_RIGHT = SWT.ARROW_RIGHT;

	default void initialize(View view) {
		view.attachMe(this);
	}

	void press(int keyCode);

	void release(int keyCode);

}
