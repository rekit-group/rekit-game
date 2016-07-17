package edu.kit.informatik.ragnarok.gui.filters;

public class InvertedMode extends GrayScaleMode {

	public InvertedMode() {
		super();
	}
	
	@Override
	protected void runIt(int taskSize, int task) {
		int start = (task * taskSize);
		int stop = (task == this.numThreads - 1) ? this.h : ((task + 1) * taskSize);
		for (int i = start * this.w * 4; i < (this.w + stop * this.w) * 4; i += 4) {
			int r = this.orig[i];
			int g = this.orig[i + 1];
			int b = this.orig[i + 2];
			//r = g = b = (r + g + b) / 3;
			this.result[i] = (byte) (255 - r);
			this.result[i + 1] = (byte) (255 - g);
			this.result[i + 2] = (byte) (255 - b);
		}

	}

}