package ragnarok.gui;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ConcurrentHashMap;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;

/**
 * This class helps to load images from the resources.
 *
 * @author Dominik Fuchss
 *
 *
 */
public final class ImageLoader {
	/**
	 * Prevent instantiation.
	 */
	private ImageLoader() {
	}

	/**
	 * The cache.
	 */
	private final static ConcurrentHashMap<String, BufferedImage> CACHE = new ConcurrentHashMap<>();

	/**
	 * The loader Object for the Resource loading.
	 */
	private static final Object LOADER = new Object();

	/**
	 * Get the {@link Image} from the resources by name.<br>
	 * <b>IMPORTANT: Do NOT dispose the images</b>
	 *
	 * @param src
	 *            the path relative to "/images/"
	 *
	 * @return the Image
	 */
	public static final BufferedImage get(String src) {
		if (!ImageLoader.CACHE.containsKey(src)) {
			synchronized (ImageLoader.class) {
				if (!ImageLoader.CACHE.containsKey(src)) {
					InputStream res = ImageLoader.LOADER.getClass().getResourceAsStream("/images/" + src);
					try {
						ImageLoader.CACHE.put(src, ImageIO.read(res));
					} catch (IOException e) {
						Logger.getRootLogger().warn("Image " + src + " not found!");
					}
				}
			}
		}
		return ImageLoader.CACHE.get(src);
	}
}
