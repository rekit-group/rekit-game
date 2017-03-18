package rekit.primitives.image;

/**
 * This class represents an abstract version of an Image.
 *
 * @author Dominik Fuchss
 *
 */
public final class AbstractImage {
	/**
	 * The width.
	 */
	public final int width;
	/**
	 * The height.
	 */
	public final int height;
	/**
	 * This array contains the RGBA values of the image in this order
	 * R->G->B->A.
	 */
	public final byte[] pixels;

	/**
	 * Create an Abstract Image.
	 *
	 * @param height
	 *            the height
	 * @param width
	 *            the width
	 * @param pixels
	 *            the pixels
	 */
	public AbstractImage(int height, int width, byte[] pixels) {
		this.height = height;
		this.width = width;
		this.pixels = pixels;
	}

	/**
	 * Create an Abstract Image.
	 *
	 * @param height
	 *            the height
	 * @param width
	 *            the width
	 * @param pixels
	 *            the pixels (one entry is equal to one RGBA)
	 */

	public AbstractImage(int height, int width, int[] pixels) {
		this.height = height;
		this.width = width;
		this.pixels = new byte[4 * pixels.length];
		for (int i = 0; i < pixels.length; i++) {
			this.pixels[4 * i] = (byte) ((pixels[i] >> 16) & 0xFF);
			this.pixels[4 * i + 1] = (byte) ((pixels[i] >> 8) & 0xFF);
			this.pixels[4 * i + 2] = (byte) ((pixels[i]) & 0xFF);
			this.pixels[4 * i + 3] = (byte) ((pixels[i] >> 24) & 0xFF);
		}
	}
}
