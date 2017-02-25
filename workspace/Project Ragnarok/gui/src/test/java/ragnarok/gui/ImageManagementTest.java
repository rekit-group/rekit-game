package ragnarok.gui;

import java.awt.image.BufferedImage;

import org.junit.Assert;
import org.junit.Test;

import ragnarok.primitives.image.AbstractImage;

public class ImageManagementTest {
	@Test
	public void testConversionToImage() {
		AbstractImage image = new AbstractImage(3, 2,
				new byte[] {
						// 1.1
						1, 2, 3, 4,
						// 1.2
						5, 6, 7, 8,
						// 2.1
						9, 10, 11, 12,
						// 2.2
						(byte) 127, (byte) 128, (byte) 129, (byte) 255,
						// 3.1
						13, 14, 15, 16,
						// 3.2
						17, 18, 19, 20 });

		BufferedImage res = (BufferedImage) ImageManagement.toImage(image);
		Assert.assertEquals(image.height, res.getHeight());
		Assert.assertEquals(image.width, res.getWidth());
		Assert.assertEquals(BufferedImage.TYPE_INT_ARGB, res.getType());
		int idx = 0;
		for (int y = 0; y < res.getHeight(); y++) {
			for (int x = 0; x < res.getWidth(); x++) {
				int pixel = res.getRGB(x, y);
				int a = (pixel >> 24) & 0xFF, r = (pixel >> 16) & 0xFF, g = (pixel >> 8) & 0xFF, b = (pixel) & 0xFF;
				Assert.assertEquals("x:" + x + ", y:" + y + ", idx:" + idx, image.pixels[idx] & 0xFF, r);
				Assert.assertEquals("x:" + x + ", y:" + y + ", idx:" + (idx + 1), image.pixels[idx + 1] & 0xFF, g);
				Assert.assertEquals("x:" + x + ", y:" + y + ", idx:" + (idx + 2), image.pixels[idx + 2] & 0xFF, b);
				Assert.assertEquals("x:" + x + ", y:" + y + ", idx:" + (idx + 3), image.pixels[idx + 3] & 0xFF, a);
				idx += 4;

			}

		}

	}

}
