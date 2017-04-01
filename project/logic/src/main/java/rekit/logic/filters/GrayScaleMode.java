package rekit.logic.filters;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import rekit.primitives.image.AbstractImage;
import rekit.primitives.image.RGBAColor;
import rekit.util.LambdaUtil;
import rekit.util.ReflectUtils.LoadMe;

/**
 * This filter realizes a filter which will convert all colors to grayscale.
 *
 * @author Dominik Fuchss
 *
 */
@LoadMe
public final class GrayScaleMode implements Filter {

	@Override
	public AbstractImage apply(final AbstractImage image) {
		return this.applyParallel(image);
	}

	/**
	 * {@link #apply(AbstractImage)} sequential.
	 *
	 * @param image
	 *            the image
	 * @return the result
	 */
	private AbstractImage applySeq(AbstractImage image) {
		AbstractImage res = new AbstractImage(image.height, image.width, new byte[image.pixels.length]);
		for (int i = 0; i < image.pixels.length; i += 4) {
			int gray = ((image.pixels[i] & 0xFF) + (image.pixels[i + 1] & 0xFF) + (image.pixels[i + 2] & 0xFF)) / 3;
			res.pixels[i] = res.pixels[i + 1] = res.pixels[i + 2] = (byte) gray;
			res.pixels[i + 3] = (byte) (image.pixels[i + 3] & 0xFF);
		}
		return res;
	}

	/**
	 * {@link #apply(AbstractImage)} parallel.
	 *
	 * @param image
	 *            the image
	 * @return the result
	 */
	private AbstractImage applyParallel(final AbstractImage image) {

		int threads = Runtime.getRuntime().availableProcessors();
		int taskSize = (image.height > threads) ? (image.height / threads) : image.height;
		if (taskSize == image.height) {
			// Sequential:
			return this.applySeq(image);
		}

		ExecutorService executor = Executors.newFixedThreadPool(threads);
		final int threadNums = threads;
		byte[] result = Arrays.copyOf(image.pixels, image.pixels.length);
		for (int i = 0; i < threads; i++) {
			final int task = i;
			executor.submit(() -> this.runIt(image.width, image.height, taskSize, task, threadNums, image.pixels, result));
		}
		executor.shutdown();
		LambdaUtil.invoke(() -> executor.awaitTermination(Long.MAX_VALUE, TimeUnit.HOURS));
		return new AbstractImage(image.height, image.width, result);

	}

	/**
	 * Task for one thread.
	 *
	 * @param w
	 *            img width
	 * @param h
	 *            img height
	 * @param taskSize
	 *            the size per task (rows)
	 * @param task
	 *            the number of the task
	 * @param threads
	 *            number of all threads for the filter task
	 * @param orig
	 *            the original data
	 * @param result
	 *            the result data
	 */
	private void runIt(int w, int h, int taskSize, int task, int threads, byte[] orig, byte[] result) {
		int start = (task * taskSize);
		int stop = (task == threads - 1) ? h : ((task + 1) * taskSize);
		for (int i = start * w * 4; i < (w + stop * w) * 4; i += 4) {
			int r = orig[i] & 0xFF;
			int g = orig[i + 1] & 0xFF;
			int b = orig[i + 2] & 0xFF;
			r = g = b = (r + g + b) / 3;
			result[i] = (byte) r;
			result[i + 1] = (byte) g;
			result[i + 2] = (byte) b;
		}
	}

	@Override
	public RGBAColor apply(RGBAColor color) {
		int gray = color.red + color.green + color.blue;
		gray /= 3;
		return new RGBAColor(gray, gray, gray, color.alpha);
	}

	@Override
	public boolean isApplyPixel() {
		return true;
	}

	@Override
	public boolean isApplyImage() {
		return true;
	}

}