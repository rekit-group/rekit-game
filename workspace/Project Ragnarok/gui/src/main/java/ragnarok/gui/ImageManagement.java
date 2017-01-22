package ragnarok.gui;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ConcurrentHashMap;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;

import ragnarok.primitives.image.AbstractImage;

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
	private final static ConcurrentHashMap<String, BufferedImage> CACHE = new ConcurrentHashMap<>();

	/**
	 * The loader Object for the Resource loading.
	 */
	private static final Object LOADER = new Object();

	/**
	 * Get the {@link Image} from the resources by name.<br>
	 *
	 * @param src
	 *            the path relative to "/images/"
	 *
	 * @return the Image
	 */
	public static final Image get(String src) {
		if (!ImageManagement.CACHE.containsKey(src)) {
			synchronized (ImageManagement.class) {
				if (!ImageManagement.CACHE.containsKey(src)) {
					InputStream res = ImageManagement.LOADER.getClass().getResourceAsStream("/images/" + src);
					try {
						ImageManagement.CACHE.put(src, ImageIO.read(res));
					} catch (IOException e) {
						Logger.getRootLogger().warn("Image " + src + " not found!");
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
	public static final AbstractImage getAsAbstractImage(String src) {
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
				abstractData[i] = (byte) data[i + 1];
				abstractData[i + 1] = (byte) data[i + 2];
				abstractData[i + 2] = (byte) data[i + 3];
				abstractData[i + 3] = (byte) data[i];
			}
		}

		return new AbstractImage(image.getHeight(), image.getWidth(), abstractData);
	}

	public static final Image toImage(AbstractImage in) {
		if (in == null) {
			return null;
		}
		BufferedImage res = new BufferedImage(in.width, in.height, BufferedImage.TYPE_INT_ARGB);
		WritableRaster raster = res.getRaster();

		int[] pixels = new int[in.pixels.length];
		for (int i = 0; i < pixels.length; i += 4) {
			pixels[i] = in.pixels[i + 3] & 0xFF;
			pixels[i + 1] = in.pixels[i] & 0xFF;
			pixels[i + 2] = in.pixels[i + 1] & 0xFF;
			pixels[i + 3] = in.pixels[i + 2] & 0xFF;
		}
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
	private static final BufferedImage convertToRGB(BufferedImage image) {
		BufferedImage res = null;
		try {
			if (image.getType() == BufferedImage.TYPE_INT_RGB || image.getType() == BufferedImage.TYPE_INT_ARGB) {
				return image;
			}
			int colormodel = image.getColorModel().hasAlpha() ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB;
			res = new BufferedImage(image.getWidth(), image.getHeight(), colormodel);
			new ColorConvertOp(null).filter(image, res);
		} catch (Exception e) {
			Logger.getRootLogger().error("Image could not be converted to (A)RGB.");
			return image;
		}
		return res;
	}

	// private static int counter = 0;

	// private static void save(BufferedImage img) {
	// try {
	// ImageIO.write(img, "png", new File("C:\\Users\\dominik\\Desktop\\img_" +
	// (ImageManagement.counter++) + ".png"));
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }
}
