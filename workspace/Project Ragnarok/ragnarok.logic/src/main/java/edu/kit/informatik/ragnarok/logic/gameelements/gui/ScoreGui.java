package edu.kit.informatik.ragnarok.logic.gameelements.gui;

import edu.kit.informatik.ragnarok.config.GameConf;
import edu.kit.informatik.ragnarok.logic.GameModel;
import edu.kit.informatik.ragnarok.logic.gameelements.Field;
import edu.kit.informatik.ragnarok.primitives.Vec2D;
import edu.kit.informatik.ragnarok.util.CalcUtil;
import edu.kit.informatik.ragnarok.util.TextOptions;

public class ScoreGui extends GuiElement {

	private Text points;
	private Text highscore;
	private TextOptions op;

	public ScoreGui(GameModel model) {
		super(model);
		this.op = new TextOptions(new Vec2D(-1, 0), GameConf.GAME_TEXT_SIZE, GameConf.GAME_TEXT_COLOR, GameConf.GAME_TEXT_FONT, 1);
		this.points = new Text(model, this.op);
		this.highscore = new Text(model, this.op);
		this.points.setPos(new Vec2D(CalcUtil.units2pixel(GameConf.GRID_W) - 10, 10));
		this.highscore.setPos(new Vec2D(CalcUtil.units2pixel(GameConf.GRID_W) - 10, 50));
	}

	@Override
	public void logicLoop(float deltaTime) {
		this.points.setText(this.getGameModel().getScore() + " Points");
		this.highscore.setText(this.getGameModel().getHighScore() + " Highscore");
	}

	@Override
	public void render(Field f) {
		this.points.render(f);
		this.highscore.render(f);
	}

}
