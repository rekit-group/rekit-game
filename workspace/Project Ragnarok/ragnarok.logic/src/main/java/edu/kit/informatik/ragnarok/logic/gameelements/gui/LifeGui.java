package edu.kit.informatik.ragnarok.logic.gameelements.gui;

import edu.kit.informatik.ragnarok.logic.gameelements.Field;
import edu.kit.informatik.ragnarok.logic.scene.Scene;
import edu.kit.informatik.ragnarok.primitives.Vec2D;

public class LifeGui extends GuiElement {

	private String image = "mrRekt_glasses_right.png";
	private Vec2D imageSize = new Vec2D(10);
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
	public void internRender(Field f) {
		for (int i = 0; i < this.playerLifes; i++) {
			f.drawGuiImage(this.getPos().addX(50 * i), this.imageSize, this.image);
		}
	}
}
