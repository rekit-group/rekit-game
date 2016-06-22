package edu.kit.informatik.ragnarok.config;

import edu.kit.informatik.ragnarok.primitives.Vec2D;
import edu.kit.informatik.ragnarok.util.RGBColor;
import edu.kit.informatik.ragnarok.util.TextOptions;
import edu.kit.informatik.ragnarok.visitor.AfterVisit;
import edu.kit.informatik.ragnarok.visitor.VisitInfo;
import edu.kit.informatik.ragnarok.visitor.Visitable;

/**
 * Configuration class that holds static options
 *
 * @author Angelo Aracri
 * @author Dominik Fuchß
 * @version 1.1
 */
@VisitInfo(res = "conf/game", visit = true)
public class GameConf implements Visitable {
	private GameConf() {
	}

	/**
	 * The Name of the Game
	 */
	public static String NAME;

	/**
	 * Size of one in-game unit in pixels. So a Vec2D(1, 0) will be projected to
	 * (pxPerUnit, 0)
	 */
	public static int PX_PER_UNIT;
	/**
	 * Grid width. Determines how broad the window game will be
	 */
	public static int GRID_W;
	/**
	 * Grid height. Determines how high the window game will be
	 */
	public static int GRID_H;

	/**
	 * Time in milliseconds to wait after each renderLoop, that refreshes all
	 * graphical elements
	 */
	public static int RENDER_DELTA;
	/**
	 * Time in milliseconds to wait after each logicLoop, that simulates physics
	 * changes positions, detects collisions, ...
	 */
	public static int LOGIC_DELTA;

	/**
	 * Gravitational constant g in pxPerUnit/s^2. Determines how fast something
	 * accelerates upon falling.
	 */
	public static float G;

	public static float PLAYER_CAMERA_OFFSET;

	public static float PLAYER_WALK_ACCEL;
	public static float PLAYER_STOP_ACCEL;

	public static float PLAYER_WALK_MAX_SPEED;

	public static float PLAYER_JUMP_BOOST;
	public static float PLAYER_JUMP_TIME;
	public static float PLAYER_BOTTOM_BOOST;

	public static int PLAYER_LIFES;

	public static float SLURP_SPEED;
	public static float SLURP_POPOFFS_PER_SEC;

	public static float WARPER_WARP_DELTA;

	public static RGBColor GAME_BACKGROUD_COLOR;
	public static RGBColor MENU_BACKGROUND_COLOR;

	public static RGBColor GAME_TEXT_COLOR;
	public static String GAME_TEXT_FONT;
	public static int GAME_TEXT_SIZE;

	public static TextOptions DEFAULT_TEXT;
	public static TextOptions HINT_TEXT;

	@AfterVisit
	public static void afterVisit() {
		GameConf.DEFAULT_TEXT = new TextOptions(new Vec2D(-1, 0), GameConf.GAME_TEXT_SIZE, GameConf.GAME_TEXT_COLOR,
				GameConf.GAME_TEXT_FONT, 1);
		GameConf.HINT_TEXT = GameConf.DEFAULT_TEXT.clone().setHeight(GameConf.GAME_TEXT_SIZE - 8);
	}
}
