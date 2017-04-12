package rekit.logic.gui;

import rekit.config.GameConf;
import rekit.core.GameGrid;
import rekit.logic.IScene;
import rekit.logic.gameelements.entities.Player;
import rekit.primitives.geometry.Vec;

/**
 *
 * This {@link GuiElement} realizes a status view of the {@link Player Player's}
 * Lives.
 *
 */
public class BackgroundElement extends GuiElement {
	/**
	 * The image name.
	 */
	private String imageSource;
	/**
	 * The image size.
	 */
	private Vec imageSize = new Vec(GameConf.GRID_W, GameConf.GRID_H);

	/**
	 * Create the status view for lives.
	 *
	 * @param scene
	 *            the scene
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
