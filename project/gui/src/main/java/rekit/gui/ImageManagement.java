package rekit.gui;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import rekit.config.GameConf;
import rekit.persistence.JarManager;
import rekit.primitives.image.AbstractImage;

/**
 * This class helps to load images from the resources.
 *
 * @author Dominik Fuchss
 *
 */
public final class ImageManagement {
	/**
	 * Prevent instantiation.
	 */
	private ImageManagement() {
	}

	/**
	 * The cache.
	 */
	private static final Map<String, BufferedImage> CACHE = new HashMap<>();

	/**
	 * The loader Object for the Resource loading.
	 */
	private static final PathMatchingResourcePatternResolver LOAD = new PathMatchingResourcePatternResolver(JarManager.SYSLOADER);

	/**
	 * Get the {@link Image} from the resources by name.<br>
	 *
	 * @param src
	 *            the path relative to "/images/"
	 *
	 * @return the Image
	 */
	public static Image get(String src) {
		if (!ImageManagement.CACHE.containsKey(src)) {
			synchronized (ImageManagement.class) {
				if (!ImageManagement.CACHE.containsKey(src)) {
					Resource res = ImageManagement.LOAD.getResource("/images/" + src);
					if (res == null) {
						return null;
					}
					try {
						ImageManagement.CACHE.put(src, ImageManagement.convertToRGB(ImageIO.read(res.getInputStream())));
					} catch (IOException e) {
						GameConf.GAME_LOGGER.warn("Image " + src + " not found!");
					}
				}
			}
		}
		return ImageManagement.CACHE.get(src);
	}

	/**
	 * Get the {@link AbstractImage} from the resources by name.<br>
	 *
	 * @param src
	 *            the path relative to "/images/"
	 *
	 * @return the Image
	 */
	public static AbstractImage getAsAbstractImage(String src) {
		BufferedImage image = (BufferedImage) ImageManagement.get(src);
		if (image == null) {
			return null;
		}
		image = ImageManagement.convertToRGB(image);
		int bandwidth = image.getColorModel().hasAlpha() ? 4 : 3;
		int[] data = new int[bandwidth * image.getWidth() * image.getHeight()];
		image.getRaster().getPixels(0, 0, image.getWidth(), image.getHeight(), data);

		byte[] abstractData = new byte[4 * image.getWidth() * image.getHeight()];
		if (bandwidth == 3) {
			for (int i = 0, j = 0; i < abstractData.length; i += 4, j += 3) {
				abstractData[i] = (byte) data[j];
				abstractData[i + 1] = (byte) data[j + 1];
				abstractData[i + 2] = (byte) data[j + 2];
				abstractData[i + 3] = (byte) 255;
			}
		} else {
			for (int i = 0; i < abstractData.length; i += 4) {
				abstractData[i] = (byte) data[i];
				abstractData[i + 1] = (byte) data[i + 1];
				abstractData[i + 2] = (byte) data[i + 2];
				abstractData[i + 3] = (byte) data[i + 3];
			}
		}

		return new AbstractImage(image.getHeight(), image.getWidth(), abstractData);
	}

	/**
	 * Convert {@link AbstractImage} to {@link Image}.
	 *
	 * @param in
	 *            the input image
	 * @return the converted image
	 */
	public static Image toImage(AbstractImage in) {
		if (in == null) {
			return null;
		}
		BufferedImage res = new BufferedImage(in.width, in.height, BufferedImage.TYPE_INT_ARGB);
		int[] pixels = new int[in.pixels.length];
		for (int i = 0; i < pixels.length; i += 1) {
			pixels[i] = in.pixels[i] & 0xFF;
		}
		WritableRaster raster = Raster.createWritableRaster(res.getSampleModel(), null);
		raster.setPixels(0, 0, in.width, in.height, pixels);
		res.setData(raster);
		res.flush();
		return res;

	}

	/**
	 * Try to convert image to (A)RGB-Image.
	 *
	 * @param image
	 *            the image
	 * @return the original image or the converted image
	 */
	private static BufferedImage convertToRGB(BufferedImage image) {
		BufferedImage res = null;
		try {
			if (image.getType() == BufferedImage.TYPE_INT_RGB || image.getType() == BufferedImage.TYPE_INT_ARGB) {
				return image;
			}
			int colormodel = image.getColorModel().hasAlpha() ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB;
			res = new BufferedImage(image.getWidth(), image.getHeight(), colormodel);
			new ColorConvertOp(null).filter(image, res);
		} catch (RuntimeException e) {
			GameConf.GAME_LOGGER.error("Image could not be converted to (A)RGB.");
			return image;
		}
		return res;
	}

}
