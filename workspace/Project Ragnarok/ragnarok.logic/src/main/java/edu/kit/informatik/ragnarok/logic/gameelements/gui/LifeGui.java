package edu.kit.informatik.ragnarok.logic.gameelements.gui;

import edu.kit.informatik.ragnarok.logic.GameModel;
import edu.kit.informatik.ragnarok.logic.gameelements.Field;
import edu.kit.informatik.ragnarok.logic.gameelements.GuiElement;
import edu.kit.informatik.ragnarok.primitives.Vec2D;

public class LifeGui extends GuiElement {
	
	private String image = "mrRekt_glasses_right.png";	
	private Vec2D imageSize = new Vec2D(10);
	private int playerLifes;

	public LifeGui(GameModel model) {
		super(model);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void logicLoop(float deltaTime) {
		playerLifes = this.getGameModel().getPlayer().getLifes();
	}

	@Override
	public void render(Field f) {
		for (int i = 0; i < this.playerLifes; i++) {
			f.drawGuiImage(this.getPos().addX(50 * i), this.imageSize, this.image);
		}
	}
}
