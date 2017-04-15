package rekit.logic.gui;

import rekit.config.GameConf;
import rekit.core.GameGrid;
import rekit.logic.IScene;
import rekit.primitives.geometry.Vec;

public class BackgroundElement extends GuiElement {
	/**
	 * The image name.
	 */
	private String imageSource;
	/**
	 * The image size.
	 */
	private Vec imageSize = new Vec(GameConf.GRID_W, GameConf.GRID_H);

	public BackgroundElement(IScene scene, String imageSource) {
		super(scene);
		this.imageSource = imageSource;
	}

	@Override
	public void internalRender(GameGrid f) {
		f.drawImage(this.getPos().add(new Vec(11, 5)), this.imageSize, this.imageSource, false, false);
	}
}
