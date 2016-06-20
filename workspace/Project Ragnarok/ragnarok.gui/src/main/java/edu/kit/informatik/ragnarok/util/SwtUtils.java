package edu.kit.informatik.ragnarok.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;

/**
 * This class contains several tools for working with SWT
 *
 * @author Dominik Fuchß
 * @author Matthias Schmitt
 *
 */
public final class SwtUtils {
	private SwtUtils() {
	}

	/**
	 * Calculate the position for a {@link Shell} relative to its parent
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
	 * Calculate the position for a {@link Shell} relative to the first screen
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
	 * The RGB Pattern
	 */
	private static final Pattern patternRGB = Pattern.compile("([0-9]+);([0-9]+);([0-9]+)");

	/**
	 * Get the {@link RGB} value of an String (Regex: [0-9]+;[0-9]+;[0-9]+)
	 *
	 * @param color
	 *            the color sting
	 * @return {@code null} if malformed, the {@link RGB} otherwise
	 */
	public static final RGB getRGB(String color) {
		Matcher matcher = SwtUtils.patternRGB.matcher(color);
		if (!matcher.find()) {
			System.err.println("BundleHelper: " + color + " is no RBG");
			return null;
		}

		int r = Integer.parseInt(matcher.group(1));
		int g = Integer.parseInt(matcher.group(2));
		int b = Integer.parseInt(matcher.group(3));
		if (r > 255 || g > 255 || b > 255) {
			System.err.println("Color out of range: " + color);
			return null;
		}
		return new RGB(r, g, b);
	}
}
