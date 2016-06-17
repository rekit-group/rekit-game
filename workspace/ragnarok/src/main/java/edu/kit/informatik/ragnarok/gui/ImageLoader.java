package edu.kit.informatik.ragnarok.gui;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

public class ImageLoader {

	private final static Map<String, Image> CACHE = new HashMap<String, Image>();

	private static final Device DEVICE = Display.getDefault();
	private static final Object LOADER = new Object();

	public static Image get(String src) {
		if (!ImageLoader.CACHE.containsKey(src)) {
			InputStream res = ImageLoader.LOADER.getClass().getResourceAsStream("/images/" + src);
			ImageLoader.CACHE.put(src, new Image(ImageLoader.DEVICE, res));
		}
		return ImageLoader.CACHE.get(src);
	}
}
