package ragnarok.logic.gui;

import ragnarok.core.GameGrid;
import ragnarok.logic.IScene;
import ragnarok.logic.gameelements.entities.Player;
import ragnarok.primitives.geometry.Vec;

/**
 *
 * This {@link GuiElement} realizes a status view of the {@link Player Player's}
 * Lives.
 *
 */
public class LifeGui extends GuiElement {
	/**
	 * The image name.
	 */
	private String image = "mrRekt_glasses_right.png";
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
	public LifeGui(IScene scene) {
		super(scene);
	}

	@Override
	public void logicLoop() {
		this.playerLives = this.getScene().getPlayer().getLives();
	}

	@Override
	public void internalRender(GameGrid f) {
		for (int i = 0; i < this.playerLives; i++) {
			f.drawImage(this.getPos().addX(50 * i), this.imageSize, this.image, false);
		}
	}
}
