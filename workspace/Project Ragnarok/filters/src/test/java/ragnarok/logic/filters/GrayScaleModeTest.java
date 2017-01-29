package ragnarok.logic.filters;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ragnarok.primitives.image.AbstractImage;
import ragnarok.primitives.image.RGBAColor;

public class GrayScaleModeTest {
	private GrayScaleMode filter;

	@Before
	public void setUp() {
		this.filter = new GrayScaleMode();
	}

	@Test
	public void testPixels() {
		Assert.assertEquals(new RGBAColor(0, 0, 0, 0), this.filter.apply(new RGBAColor(0, 0, 0, 0)));
		Assert.assertEquals(new RGBAColor(255, 255, 255, 0), this.filter.apply(new RGBAColor(255, 255, 255, 0)));
		Assert.assertEquals(new RGBAColor(127, 127, 127, 12), this.filter.apply(new RGBAColor(255, 127, 0, 12)));
		Assert.assertEquals(new RGBAColor(2, 2, 2, 0), this.filter.apply(new RGBAColor(1, 2, 3, 0)));
	}

	@Test
	public void testImage1() {
		AbstractImage image = new AbstractImage(2, 2,
				new byte[] {
						// 1.1
						3, 3, 3, 1,
						// 1.2
						4, 4, 4, 2,
						// 2.1
						5, 5, 5, 3,
						// 2.2
						(byte) 255, (byte) 255, (byte) 255, (byte) 255 });
		AbstractImage res = this.filter.apply(image);
		Assert.assertEquals(image.width, res.width);
		Assert.assertEquals(image.height, res.height);

		Assert.assertArrayEquals(image.pixels, res.pixels);

	}
}
