package edu.kit.informatik.ragnarok.logic.parallax;

import edu.kit.informatik.ragnarok.config.GameConf;
import edu.kit.informatik.ragnarok.logic.gameelements.Field;

public abstract class ParallaxLayer {

	private float distanceFromFront;

	protected float generateUntil = 0;

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
		//f.drawRectangle(new Vec2D(x + GameConf.GRID_W / 2f, GameConf.GRID_H / 2), new Vec2D(GameConf.GRID_W, GameConf.GRID_H), new RGBAColor(255, 255, 255, 100));
	}
	
	protected float fieldXtoLayerX (float fieldX) {
		return fieldX / this.distanceFromFront;
	}

	protected float layerXtoFieldX (float layerX) {
		return layerX * this.distanceFromFront;
	}
}
