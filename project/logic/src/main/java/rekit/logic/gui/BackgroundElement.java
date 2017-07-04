package rekit.logic.gui;

import rekit.config.GameConf;
import rekit.core.GameGrid;
import rekit.logic.IScene;
import rekit.primitives.geometry.Vec;

/**
 * The standard background of the Game.
 *
 */
public final class BackgroundElement extends GuiElement {
	/**
	 * The image name.
	 */
	private String imageSource;
	/**
	 * The image size.
	 */
	private Vec imageSize = new Vec(GameConf.GRID_W, GameConf.GRID_H);

	/**
	 * Create the background by scene and image.
	 *
	 * @param scene
	 *            the scene
	 * @param imageSource
	 *            the path to the image
	 */
	public BackgroundElement(IScene scene, String imageSource) {
		super(scene);
		this.imageSource = imageSource;
	}

	@Override
	public void internalRender(GameGrid f) {
		f.drawImage(this.getPos().add(new Vec(11, 5)), this.imageSize, this.imageSource, false, false);
	}
}
