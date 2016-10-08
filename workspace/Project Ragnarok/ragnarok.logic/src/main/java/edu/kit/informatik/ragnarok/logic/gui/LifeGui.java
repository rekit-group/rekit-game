package edu.kit.informatik.ragnarok.logic.gui;

import edu.kit.informatik.ragnarok.core.Field;
import edu.kit.informatik.ragnarok.core.GuiElement;
import edu.kit.informatik.ragnarok.core.IScene;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.Player;
import edu.kit.informatik.ragnarok.primitives.geometry.Vec;

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
	public void internalRender(Field f) {
		for (int i = 0; i < this.playerLives; i++) {
			f.drawImage(this.getPos().addX(50 * i), this.imageSize, this.image, false);
		}
	}
}
