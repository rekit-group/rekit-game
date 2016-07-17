package edu.kit.informatik.ragnarok.gui;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.eclipse.swt.graphics.ImageData;

/**
 * Detects edges via the Sobel filter operator.
 */
public class GrayScaleMode implements Filter {

	protected int numThreads;
	protected int w;
	protected int h;
	protected byte[] orig;
	protected byte[] result;

	public GrayScaleMode() {
		this.numThreads = Runtime.getRuntime().availableProcessors();
	}

	@Override
	public ImageData apply(final ImageData image) {
		this.w = image.width;
		this.h = image.height;
		ImageData res = (ImageData) image.clone();
		this.orig = image.data;
		this.result = res.data;

		int taskSize = (this.h > this.numThreads) ? (this.h / this.numThreads) : (this.h);
		if (taskSize == this.h) {
			this.numThreads = 1;
		}

		ExecutorService executor = Executors.newFixedThreadPool(this.numThreads);
		try {
			for (int i = 0; i < this.numThreads; i++) {
				final int task = i;
				executor.submit(() -> this.runIt(taskSize, task));
			}
			executor.shutdown();
			executor.awaitTermination(Long.MAX_VALUE, TimeUnit.HOURS);
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
		return res;

	}

	protected void runIt(int taskSize, int task) {
		int start = (task * taskSize);
		int stop = (task == this.numThreads - 1) ? this.h : ((task + 1) * taskSize);
		for (int i = start * this.w * 4; i < (this.w + stop * this.w) * 4; i += 4) {
			int r = this.orig[i];
			int g = this.orig[i + 1];
			int b = this.orig[i + 2];
			r = g = b = (r + g + b) / 3;
			this.result[i] = (byte) r;
			this.result[i + 1] = (byte) g;
			this.result[i + 2] = (byte) b;
		}

	}

}