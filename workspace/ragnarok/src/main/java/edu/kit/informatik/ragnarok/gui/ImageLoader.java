package edu.kit.informatik.ragnarok.gui;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Image;

public class ImageLoader {
	
	private static Map<String, Image> images = new HashMap<String, Image>();
	
	private static Device device;
	
	public static void init(Device device) {
		ImageLoader.device = device;
	}
	
	public static Image get(String src) {
		if (!images.containsKey(src)) {
			images.put(src, new Image(ImageLoader.device, src));
		}
		return images.get(src);
	}
}
