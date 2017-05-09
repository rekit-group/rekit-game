package rekit.logic.gui.parallax;

import rekit.config.GameConf;
import rekit.core.GameGrid;
import rekit.logic.IScene;

public abstract class ParallaxLayer {

	protected float perspectiveZ;

	protected float generateUntil = 0;

	protected IScene scene = null;

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

	public void render(GameGrid f) {

	}

	protected float fieldXtoLayerX(float fieldX) {
		return fieldX / this.perspectiveZ;
	}

	public void setScene(IScene scene) {
		this.scene = scene;
	}

}
