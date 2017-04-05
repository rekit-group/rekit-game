package rekit.logic.gui;

import rekit.core.GameGrid;
import rekit.logic.ILevelScene;
import rekit.logic.gameelements.entities.Player;
import rekit.primitives.geometry.Vec;

/**
 *
 * This {@link GuiElement} realizes a status view of the {@link Player Player's}
 * Lives.
 *
 */
public class LifeGui extends LevelGuiElement {
	/**
	 * The image name.
	 */
	private String image = "life.png";
	/**
	 * The image size.
	 */
	private Vec imageSize = new Vec(10);
	/**
	 * The amount of the player's lives.
	 */
	private int playerLives;

	/**
	 * Create the status view for lives.
	 *
	 * @param scene
	 *            the scene
	 */
	public LifeGui(ILevelScene scene) {
		super(scene);
	}

	@Override
	public void logicLoop() {
		this.playerLives = this.getScene().getPlayer().getLives();
	}

	@Override
	public void internalRender(GameGrid f) {
		for (int i = 0; i < this.playerLives; i++) {
			f.drawImage(this.getPos().addX(50 * i), this.imageSize, this.image, false, true);
		}
	}
}
