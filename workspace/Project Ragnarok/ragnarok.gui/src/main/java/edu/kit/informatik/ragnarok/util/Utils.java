package edu.kit.informatik.ragnarok.util;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import edu.kit.informatik.ragnarok.primitives.image.RGBAColor;
import edu.kit.informatik.ragnarok.primitives.image.RGBColor;

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
	 * Convert a {@link RGBColor} to a {@link Color}.
	 *
	 * @param color
	 *            the color
	 * @return the converted color
	 */
	public static Color calcRGB(RGBColor color) {
		return new Color(color.red, color.green, color.blue);
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
	 * Center frame relative to monitor
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

	/**
	 * Get Image by {@link InputStream}.
	 * 
	 * @param is
	 *            the stream
	 * @return the image or {@code null} if error occured
	 */
	public static Image getImage(InputStream is) {
		try {
			return ImageIO.read(is);
		} catch (IOException e) {
			return null;
		}
	}
}
