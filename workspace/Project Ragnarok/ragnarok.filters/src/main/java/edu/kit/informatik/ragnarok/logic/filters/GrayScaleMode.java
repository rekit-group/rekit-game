package edu.kit.informatik.ragnarok.logic.filters;

import edu.kit.informatik.ragnarok.primitives.image.Filter;
import edu.kit.informatik.ragnarok.primitives.image.RGBAColor;
import edu.kit.informatik.ragnarok.primitives.image.RGBColor;
import edu.kit.informatik.ragnarok.util.ReflectUtils.LoadMe;

/**
 * This filter realizes a filter which will convert all colors to grayscale
 *
 * @author Dominik Fuchß
 *
 */
@LoadMe
public class GrayScaleMode implements Filter {

	// protected int numThreads;
	// protected int w;
	// protected int h;
	// protected byte[] orig;
	// protected byte[] result;
	//
	// public GrayScaleMode() {
	// this.numThreads = Runtime.getRuntime().availableProcessors();
	// }

	// @Override
	// public AbstractImage apply(final AbstractImage image) {
	// this.w = image.width;
	// this.h = image.height;
	//
	// this.orig = image.pixels;
	// this.result = Arrays.copyOf(image.pixels, image.pixels.length);
	//
	// int taskSize = (this.h > this.numThreads) ? (this.h / this.numThreads) :
	// (this.h);
	// if (taskSize == this.h) {
	// this.numThreads = 1;
	// }
	//
	// ExecutorService executor = Executors.newFixedThreadPool(this.numThreads);
	// try {
	// for (int i = 0; i < this.numThreads; i++) {
	// final int task = i;
	// executor.submit(() -> this.runIt(taskSize, task));
	// }
	// executor.shutdown();
	// executor.awaitTermination(Long.MAX_VALUE, TimeUnit.HOURS);
	// } catch (InterruptedException ex) {
	// ex.printStackTrace();
	// }
	// return new AbstractImage(this.h, this.w, this.result);
	//
	// }
	// protected void runIt(int taskSize, int task) {
	// int start = (task * taskSize);
	// int stop = (task == this.numThreads - 1) ? this.h : ((task + 1) *
	// taskSize);
	// for (int i = start * this.w * 4; i < (this.w + stop * this.w) * 4; i +=
	// 4) {
	// int r = this.orig[i];
	// int g = this.orig[i + 1];
	// int b = this.orig[i + 2];
	// r = g = b = (r + g + b) / 3;
	// this.result[i] = (byte) r;
	// this.result[i + 1] = (byte) g;
	// this.result[i + 2] = (byte) b;
	// }
	// }

	@Override
	public RGBAColor apply(RGBAColor color) {
		int gray = color.red + color.green + color.blue;
		gray /= 3;
		return new RGBAColor(gray, gray, gray, color.alpha);
	}

	@Override
	public RGBColor apply(RGBColor color) {
		int gray = color.red + color.green + color.blue;
		gray /= 3;
		return new RGBColor(gray, gray, gray);
	}

	@Override
	public boolean isApplyPixel() {
		return true;
	}

}