package edu.kit.informatik.ragnarok.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;

import edu.kit.informatik.ragnarok.config.GameConf;

public final class SwtUtils {
	private SwtUtils() {
	}

	public static Point calcCenter(Point size_parent, Point location_parent, Shell me) {
		int newLeftPos = (size_parent.x - me.getSize().x) / 2 + location_parent.x;
		int newTopPos = (size_parent.y - me.getSize().y) / 2 + location_parent.y;
		return new Point(newLeftPos, newTopPos);

	}

	public static Point calcCenter(Shell me) {
		Monitor mon = Display.getDefault().getMonitors()[0];
		int newLeftPos = (mon.getBounds().width - me.getSize().x) / 2;
		int newTopPos = (mon.getBounds().height - me.getSize().y) / 2;
		return new Point(newLeftPos, newTopPos);

	}

	private static final Pattern patternRGB = Pattern.compile("([0-9]+);([0-9]+);([0-9]+)");

	public RGB getRGB(String key) {
		String res = GameConf.BUNDLE.getString(key);
		if (res == null) {
			return null;
		}
		Matcher matcher = SwtUtils.patternRGB.matcher(res);
		if (!matcher.find()) {
			System.err.println("BundleHelper: " + res + " is no RBG");
			return null;
		}

		int r = Integer.parseInt(matcher.group(1));
		int g = Integer.parseInt(matcher.group(2));
		int b = Integer.parseInt(matcher.group(3));
		return new RGB(r, g, b);
	}
}
