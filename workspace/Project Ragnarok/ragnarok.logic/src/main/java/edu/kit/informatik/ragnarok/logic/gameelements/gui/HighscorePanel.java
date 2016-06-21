package edu.kit.informatik.ragnarok.logic.gameelements.gui;

import edu.kit.informatik.ragnarok.config.GameConf;
import edu.kit.informatik.ragnarok.logic.GameModel;
import edu.kit.informatik.ragnarok.logic.gameelements.Field;
import edu.kit.informatik.ragnarok.logic.gameelements.GuiElement;
import edu.kit.informatik.ragnarok.primitives.Vec2D;
import edu.kit.informatik.ragnarok.util.CalcUtil;
import edu.kit.informatik.ragnarok.util.TextOptions;

public class HighscorePanel extends GuiElement{
	
	private Text points;
	private Text highscore;
	private TextOptions op;
	
	public HighscorePanel(GameModel model) {
		super(model);
		op = new TextOptions(new Vec2D(-1, 0), GameConf.gameTextSize, GameConf.gameTextColor, GameConf.gameTextFont, 1);
		points = new Text(model, op);
		highscore = new Text(model, op);
		points.setPos(new Vec2D(CalcUtil.units2pixel(GameConf.gridW) - 10, 1));
		highscore.setPos(new Vec2D(CalcUtil.units2pixel(GameConf.gridW) - 10, 50));
	}
	
	@Override
	public void logicLoop(float deltaTime) {
		points.setText(this.getGameModel().getScore() + " Points");
		highscore.setText(this.getGameModel().getHighScore() + " Highscore");
		
		double sin = Math.sin((System.currentTimeMillis() / 300.0));
		op.setHeight((int)(GameConf.gameTextSize + sin));
		
	}
	
	@Override
	public void render(Field f) {
		points.render(f);
		highscore.render(f);
	}

}
