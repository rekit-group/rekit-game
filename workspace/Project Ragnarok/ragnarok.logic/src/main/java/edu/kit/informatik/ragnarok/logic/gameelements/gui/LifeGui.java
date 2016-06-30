package edu.kit.informatik.ragnarok.logic.gameelements.gui;

import edu.kit.informatik.ragnarok.logic.gameelements.Field;
import edu.kit.informatik.ragnarok.logic.scene.Scene;
import edu.kit.informatik.ragnarok.primitives.Vec;

public class LifeGui extends GuiElement {

	private String image = "mrRekt_glasses_right.png";
	private Vec imageSize = new Vec(10);
	private int playerLifes;

	public LifeGui(Scene scene) {
		super(scene);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void logicLoop(float deltaTime) {
		this.playerLifes = this.getScene().getPlayer().getLifes();
	}

	@Override
	public void internalRender(Field f) {
		for (int i = 0; i < this.playerLifes; i++) {
			f.drawImage(this.getPos().addX(50 * i), this.imageSize, this.image, false);
		}
	}
}
