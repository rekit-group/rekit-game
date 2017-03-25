package rekit.util;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;

import rekit.primitives.image.RGBAColor;

/**
 * This class contains several tools for working with Graphics.
 *
 * @author Dominik Fuchss
 *
 */
public final class Utils {
	/**
	 * Prevent instantiation.
	 */
	private Utils() {
	}

	/**
	 * Convert a {@link RGBAColor} to a {@link Color}.
	 *
	 * @param color
	 *            the color
	 * @return the converted color
	 */
	public static Color calcRGBA(RGBAColor color) {
		return new Color(color.red, color.green, color.blue, color.alpha);

	}

	/**
	 * Center {@link Frame} relative to monitor.
	 *
	 * @param frame
	 *            the frame
	 */
	public static void center(Frame frame) {
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
		int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
		frame.setLocation(x, y);

	}

}
