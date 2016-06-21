package edu.kit.informatik.ragnarok.config;

import java.util.ResourceBundle;

import edu.kit.informatik.ragnarok.primitives.Vec2D;
import edu.kit.informatik.ragnarok.util.RGBColor;
import edu.kit.informatik.ragnarok.util.TextOptions;

/**
 * Configuration class that holds static options
 *
 * @author Angelo Aracri
 * @author Dominik Fuchß
 * @version 1.1
 */
public class GameConf {
	/**
	 * The Bundle which contains all configuration stuff
	 */
	public static final BundleHelper BUNDLE = new BundleHelper(ResourceBundle.getBundle("conf/game"));

	/**
	 * Size of one in-game unit in pixels. So a Vec2D(1, 0) will be projected to
	 * (pxPerUnit, 0)
	 */
	public static final int PX_PER_UNIT = GameConf.BUNDLE.getInt("pxPerUnit");
	/**
	 * Grid width. Determines how broad the window game will be
	 */
	public static final int GRID_W = GameConf.BUNDLE.getInt("gridW");
	/**
	 * Grid height. Determines how high the window game will be
	 */
	public static final int GRID_H = GameConf.BUNDLE.getInt("gridH");;

	/**
	 * Time in milliseconds to wait after each renderLoop, that refreshes all
	 * graphical elements
	 */
	public static final int RENDER_DELTA = GameConf.BUNDLE.getInt("renderDelta");
	/**
	 * Time in milliseconds to wait after each logicLoop, that simulates physics
	 * changes positions, detects collisions, ...
	 */
	public static final int LOGIC_DELTA = GameConf.BUNDLE.getInt("logicDelta");

	/**
	 * Gravitational constant g in pxPerUnit/s^2. Determines how fast something
	 * accelerates upon falling.
	 */
	public static final float G = GameConf.BUNDLE.getFloat("g");

	public static final float PLAYER_CAMERA_OFFSET = GameConf.BUNDLE.getFloat("playerCameraOffset");

	public static final float PLAYER_WALK_ACCEL = GameConf.BUNDLE.getFloat("playerWalkAccel");
	public static final float PLAYER_STOP_ACCEL = GameConf.BUNDLE.getFloat("playerStopAccel");

	public static final float PLAYER_WALK_MAX_SPEED = GameConf.BUNDLE.getFloat("playerWalkMaxSpeed");

	public static final float PLAYER_JUMP_BOOST = GameConf.BUNDLE.getFloat("playerJumpBoost");
	public static final float PLAYER_JUMP_TIME = GameConf.BUNDLE.getFloat("playerJumpTime");
	public static final float PLAYER_BOTTOM_BOOST = GameConf.BUNDLE.getFloat("playerBottomBoost");

	public static final int PLAYER_LIFES = GameConf.BUNDLE.getInt("playerLifes");

	public static final float SLURP_SPEED = GameConf.BUNDLE.getFloat("slurpSpeed");
	public static final float SLURP_POPOFFS_PER_SEC = GameConf.BUNDLE.getFloat("slurpPopOffsPerSec");

	public static final RGBColor GAME_BACKGROUD_COLOR = GameConf.BUNDLE.getRGBColor("gameBackgroundColor");
	public static final RGBColor MENU_BACKGROUND_COLOR = GameConf.BUNDLE.getRGBColor("menuBackgroundColor");
	
	
	public static final RGBColor GAME_TEXT_COLOR = GameConf.BUNDLE.getRGBColor("gameTextColor");
	public static final String GAME_TEXT_FONT = GameConf.BUNDLE.getString("gameTextFont");
	public static final int GAME_TEXT_SIZE = GameConf.BUNDLE.getInt("gameTextSize");
	
	public static final TextOptions DEFAULT_TEXT = new TextOptions(new Vec2D(-1, 0), GameConf.GAME_TEXT_SIZE, GameConf.GAME_TEXT_COLOR,	GameConf.GAME_TEXT_FONT, 1);
	public static final TextOptions HINT_TEXT = GameConf.DEFAULT_TEXT.clone().setHeight(GameConf.GAME_TEXT_SIZE - 8);

}
