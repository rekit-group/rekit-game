package edu.kit.informatik.ragnarok.logic.gameelements.gui;

import edu.kit.informatik.ragnarok.config.GameConf;
import edu.kit.informatik.ragnarok.logic.GameModel;
import edu.kit.informatik.ragnarok.logic.gameelements.Field;
import edu.kit.informatik.ragnarok.logic.gameelements.GuiElement;
import edu.kit.informatik.ragnarok.primitives.Vec2D;
import edu.kit.informatik.ragnarok.util.TextOptions;

public class HighscorePanel extends GuiElement{
	
	private Text points;
	private Text highscore;
	
	public HighscorePanel(GameModel model) {
		super(model);
		TextOptions op = new TextOptions(false, 18, GameConf.gameTextColor, "Tahoma", 1);
		points = new Text(model, op);
		highscore = new Text(model, op);
		points.setPos(new Vec2D(GameConf.gridW - 1, 1));
		points.setPos(new Vec2D(10, 100));
		highscore.setPos(new Vec2D(GameConf.gridW - 1, 2));
		highscore.setPos(new Vec2D(10, 130));
	}
	
	@Override
	public void logicLoop(float deltaTime) {
		points.setText(this.getGameModel().getScore() + " Points");
		highscore.setText(this.getGameModel().getHighScore() + " Highscore");
	}
	
	@Override
	public void render(Field f) {
		points.render(f);
		highscore.render(f);
	}

}
