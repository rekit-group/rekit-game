package edu.kit.informatik.ragnarok.logic.parallax;

import edu.kit.informatik.ragnarok.config.GameConf;
import edu.kit.informatik.ragnarok.logic.gameelements.Field;
import edu.kit.informatik.ragnarok.primitives.Vec2D;
import edu.kit.informatik.ragnarok.util.RGBAColor;

public class ParallaxLayer {

	private String imagePath;
	private float distanceFromFront;

	private int repeated = 0;

	private float w;
	
	private float x;
	
	public ParallaxLayer(String imagePath, int imageW, float distanceFromFront) {
		this.imagePath = imagePath;
		this.distanceFromFront = distanceFromFront;
		this.w = imageW / GameConf.PX_PER_UNIT;
	}
	
	public void logicLoop(float currentOffset) {
		// x position of where the middle of the image is rendered
		this.x = repeated * w + (currentOffset / this.distanceFromFront) + (w / 2);
		
		// if right border of image is left of right camera border
		if (x + w / 2 < currentOffset + GameConf.GRID_W + 1) {
			repeated ++;
			x += w;
		}

	}

	public void render(Field f) {
		f.drawImage(new Vec2D((float) (x - w), GameConf.GRID_H / 2), new Vec2D((float) w, GameConf.GRID_H), this.imagePath);
		f.drawImage(new Vec2D((float) x, GameConf.GRID_H / 2), new Vec2D((float) w, GameConf.GRID_H), this.imagePath);
		
		f.drawRectangle(new Vec2D((x - w / 2), GameConf.GRID_H / 2), new Vec2D(2 * w, GameConf.GRID_H), new RGBAColor(255, 255, 255, 100));
	}
}
