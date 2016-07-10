package edu.kit.informatik.ragnarok.logic.parallax;

import edu.kit.informatik.ragnarok.config.GameConf;
import edu.kit.informatik.ragnarok.logic.gameelements.Field;
import edu.kit.informatik.ragnarok.logic.scene.Scene;

public abstract class ParallaxLayer {

	protected float perspectiveZ;

	protected float generateUntil = 0;

	private final Object sync = new Object();

	protected Scene scene;

	protected float currentlyGeneratedUntil = 0;

	protected float x;

	public ParallaxLayer(float perspectiveZ) {
		this.perspectiveZ = perspectiveZ;
	}

	public void logicLoop(float currentOffset) {
		// x position of where the middle of the image is rendered
		this.x = currentOffset;

		this.generateUntil = this.x + GameConf.GRID_W;
	}

	public void render(Field f) {

	}

	protected float fieldXtoLayerX(float fieldX) {
		return fieldX / this.perspectiveZ;
	}

	protected float layerXtoFieldX(float layerX) {
		return layerX * this.perspectiveZ;
	}

	public void setScene(Scene scene) {
		this.scene = scene;
	}

	public Object synchronize() {
		return this.sync;
	}
}
