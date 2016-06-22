package edu.kit.informatik.ragnarok.logic.gameelements.gui;

import edu.kit.informatik.ragnarok.config.GameConf;
import edu.kit.informatik.ragnarok.logic.gameelements.Field;
import edu.kit.informatik.ragnarok.logic.scene.LevelScene;
import edu.kit.informatik.ragnarok.primitives.Vec2D;
import edu.kit.informatik.ragnarok.util.CalcUtil;
import edu.kit.informatik.ragnarok.util.TextOptions;

public class ScoreGui extends GuiElement{

	private Text points;
	private Text highscore;
	private TextOptions op;
	
	public ScoreGui(LevelScene scene) {
		super(scene);
		op = new TextOptions(new Vec2D(-1, 0), GameConf.GAME_TEXT_SIZE, GameConf.GAME_TEXT_COLOR, GameConf.GAME_TEXT_FONT, 1);
		points = new Text(scene, op);
		highscore = new Text(scene, op);
		points.setPos(new Vec2D(CalcUtil.units2pixel(GameConf.GRID_W) - 10, 10));
		highscore.setPos(new Vec2D(CalcUtil.units2pixel(GameConf.GRID_W) - 10, 50));
	}
	
	@Override
	public void logicLoop(float deltaTime) {
		LevelScene lvlScene = (LevelScene) this.getScene();
		points.setText(lvlScene.getScore() + " Points");
		highscore.setText(lvlScene.getHighScore() + " Highscore");
	}
	
	@Override
	public void render(Field f) {
		points.render(f);
		highscore.render(f);
	}

}
