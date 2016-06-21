package edu.kit.informatik.ragnarok.logic.gameelements.gui;

import edu.kit.informatik.ragnarok.config.GameConf;
import edu.kit.informatik.ragnarok.logic.GameModel;
import edu.kit.informatik.ragnarok.logic.gameelements.Field;
import edu.kit.informatik.ragnarok.primitives.Vec2D;
import edu.kit.informatik.ragnarok.util.CalcUtil;
import edu.kit.informatik.ragnarok.util.TextOptions;

public class ScoreGui extends GuiElement{
	
	private Text points;
	private Text highscore;
	private TextOptions op;
	
	public ScoreGui(GameModel model) {
		super(model);
		op = new TextOptions(new Vec2D(-1, 0), GameConf.GAME_TEXT_SIZE, GameConf.GAME_TEXT_COLOR, GameConf.GAME_TEXT_FONT, 1);
		points = new Text(model, op);
		highscore = new Text(model, op);
		points.setPos(new Vec2D(CalcUtil.units2pixel(GameConf.GRID_W) - 10, 10));
		highscore.setPos(new Vec2D(CalcUtil.units2pixel(GameConf.GRID_W) - 10, 50));
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
