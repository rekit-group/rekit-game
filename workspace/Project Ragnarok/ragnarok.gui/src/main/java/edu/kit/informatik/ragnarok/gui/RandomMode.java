package edu.kit.informatik.ragnarok.gui;

import edu.kit.informatik.ragnarok.config.GameConf;

public class RandomMode extends GrayScaleMode {

	public RandomMode() {
		super();
	}
	
	protected byte[] valueMapping = new byte[256];
	
	/**
	 * Flyweight getter method for getting a random value between 1 and 255 for every
	 * value between 0 and 255.
	 * @param value the extrinsic, original color value between 0 and 255.
	 * @return the intrinsic, random color value between 1 and 255.
	 */
	protected byte getMapping(int value) {
		int index = value + 128;
		if (this.valueMapping[index] == 0) {
			this.valueMapping[index] = (byte)(1 + GameConf.PRNG.nextInt(255));
		}
		return (byte)(this.valueMapping[index] - 128);
	}
	
	@Override
	protected void runIt(int taskSize, int task) {
		int start = (task * taskSize);
		int stop = (task == this.numThreads - 1) ? this.h : ((task + 1) * taskSize);
		
		for (int i = start * this.w * 4; i < (this.w + stop * this.w) * 4; i += 4) {
			this.result[i + 0] = getMapping(this.orig[i + 0]);
			this.result[i + 1] = getMapping(this.orig[i + 1]);
			this.result[i + 2] = getMapping(this.orig[i + 2]);
		}


	}

}