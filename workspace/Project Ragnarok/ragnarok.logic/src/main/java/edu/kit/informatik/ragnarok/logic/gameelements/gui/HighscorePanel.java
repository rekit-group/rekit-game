package edu.kit.informatik.ragnarok.logic.gameelements.gui;

import edu.kit.informatik.ragnarok.config.GameConf;
import edu.kit.informatik.ragnarok.logic.GameModel;
import edu.kit.informatik.ragnarok.logic.gameelements.Field;
import edu.kit.informatik.ragnarok.logic.gameelements.GuiElement;
import edu.kit.informatik.ragnarok.primitives.Vec2D;
import edu.kit.informatik.ragnarok.util.CalcUtil;

public class HighscorePanel extends GuiElement{
	
	private Text points;
	private Text highscore;
	
	public HighscorePanel(GameModel model) {
		super(model);
		points = new Text(model, GameConf.defaultText);
		highscore = new Text(model, GameConf.defaultText);
		points.setPos(new Vec2D(CalcUtil.units2pixel(GameConf.gridW) - 10, 1));
		highscore.setPos(new Vec2D(CalcUtil.units2pixel(GameConf.gridW) - 10, 50));
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
