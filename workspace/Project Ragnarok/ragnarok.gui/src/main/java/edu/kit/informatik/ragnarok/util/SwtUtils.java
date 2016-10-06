package edu.kit.informatik.ragnarok.util;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.RGBA;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;

import edu.kit.informatik.ragnarok.primitives.image.RGBAColor;
import edu.kit.informatik.ragnarok.primitives.image.RGBColor;

/**
 * This class contains several tools for working with SWT.
 *
 * @author Dominik Fuchß
 *
 */
public final class SwtUtils {
	/**
	 * Prevent instantiation.
	 */
	private SwtUtils() {
	}

	/**
	 * Calculate the position for a {@link Shell} relative to its parent.
	 *
	 * @param size_parent
	 *            the size of the parent
	 * @param location_parent
	 *            the location of the parent
	 * @param me
	 *            the {@link Shell} itself
	 * @return the position
	 */
	public static Point calcCenter(Point size_parent, Point location_parent, Shell me) {
		int newLeftPos = (size_parent.x - me.getSize().x) / 2 + location_parent.x;
		int newTopPos = (size_parent.y - me.getSize().y) / 2 + location_parent.y;
		return new Point(newLeftPos, newTopPos);

	}

	/**
	 * Calculate the position for a {@link Shell} relative to the first screen.
	 *
	 * @param me
	 *            the {@link Shell} itself
	 * @return the position
	 */
	public static Point calcCenter(Shell me) {
		Monitor mon = Display.getDefault().getMonitors()[0];
		int newLeftPos = (mon.getBounds().width - me.getSize().x) / 2;
		int newTopPos = (mon.getBounds().height - me.getSize().y) / 2;
		return new Point(newLeftPos, newTopPos);

	}

	/**
	 * Convert a {@link RGBColor} to a {@link RGB}.
	 *
	 * @param color
	 *            the color
	 * @return the converted color
	 */
	public static RGB calcRGB(RGBColor color) {
		return new RGB(color.red, color.green, color.blue);
	}

	/**
	 * Convert a {@link RGBAColor} to a {@link RGBA}.
	 *
	 * @param color
	 *            the color
	 * @return the converted color
	 */
	public static RGBA calcRGBA(RGBAColor color) {
		return new RGBA(color.red, color.green, color.blue, color.alpha);

	}
}
