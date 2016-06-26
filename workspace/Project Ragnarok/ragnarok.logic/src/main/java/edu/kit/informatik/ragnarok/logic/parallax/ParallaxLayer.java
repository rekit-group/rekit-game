package edu.kit.informatik.ragnarok.logic.parallax;

import java.util.Random;

import edu.kit.informatik.ragnarok.config.GameConf;
import edu.kit.informatik.ragnarok.logic.gameelements.Field;

public abstract class ParallaxLayer {

	private float distanceFromFront;

	protected float generateUntil = 0;

	private final Object sync = new Object();

	protected static final Random RNG = new Random();

	protected float currentlyGeneratedUntil = 0;

	protected float x;

	public ParallaxLayer(float distanceFromFront) {
		this.distanceFromFront = distanceFromFront;
	}

	public void logicLoop(float currentOffset) {
		// x position of where the middle of the image is rendered
		this.x = currentOffset;

		this.generateUntil = this.x + GameConf.GRID_W;
	}

	public void render(Field f) {

	}

	protected float fieldXtoLayerX(float fieldX) {
		return fieldX / this.distanceFromFront;
	}

	protected float layerXtoFieldX(float layerX) {
		return layerX * this.distanceFromFront;
	}

	public Object synchronize() {
		return this.sync;
	}

	public int getElementCount() {
		return 0;
	}
}
