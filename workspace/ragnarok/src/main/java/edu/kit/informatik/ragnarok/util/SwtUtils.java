package edu.kit.informatik.ragnarok.util;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;

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
}
