package edu.kit.informatik.ragnarok.gui.parallax;

import edu.kit.informatik.ragnarok.config.GameConf;
import edu.kit.informatik.ragnarok.gui.ImageLoader;
import edu.kit.informatik.ragnarok.logic.gameelements.Field;
import edu.kit.informatik.ragnarok.primitives.Vec2D;
import edu.kit.informatik.ragnarok.util.RGBAColor;

public class ParallaxLayer {

	private String imagePath;
	private float distanceFromFront;

	private int repeated = 0;

	public ParallaxLayer(String imagePath, float distanceFromFront) {
		this.imagePath = imagePath;
		this.distanceFromFront = distanceFromFront;
	}

	public void render(Field f, float currentOffset) {
		// width of image in units
		double w = ImageLoader.get(this.imagePath).getBounds().width / (double)GameConf.PX_PER_UNIT;
		
		// x position of where the middle of the image is rendered
		double x = repeated * w + (currentOffset / this.distanceFromFront) + (w / 2);
		
		// while right border of image is left of right camera border
		if (x + w / 2 < currentOffset + GameConf.GRID_W + 1) {
			repeated ++;
			x += w;
		}

		f.drawImage(new Vec2D((float) (x - w), GameConf.GRID_H / 2), new Vec2D((float) w, GameConf.GRID_H), this.imagePath);
		f.drawImage(new Vec2D((float) x, GameConf.GRID_H / 2), new Vec2D((float) w, GameConf.GRID_H), this.imagePath);
		
		f.drawRectangle(new Vec2D(currentOffset + GameConf.GRID_W / 2, GameConf.GRID_H / 2), new Vec2D(GameConf.GRID_W, GameConf.GRID_H), new RGBAColor(255, 255, 255, 100));
	}
}
