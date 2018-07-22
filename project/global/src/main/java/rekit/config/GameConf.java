package rekit.config;

import java.awt.Font;
import java.util.Random;

import org.apache.log4j.Logger;
import org.fuchss.configuration.Configurable;
import org.fuchss.configuration.annotations.AfterSetting;
import org.fuchss.configuration.annotations.NoSet;
import org.fuchss.configuration.annotations.SetterInfo;
import org.fuchss.configuration.parser.Parser;

import rekit.primitives.TextOptions;
import rekit.primitives.geometry.Vec;
import rekit.primitives.image.RGBAColor;
import rekit.util.ReflectUtils;

/**
 * Configuration class that holds static options.
 *
 * @author Angelo Aracri
 * @author Dominik Fuchss
 */
@SetterInfo(res = "conf/game")
public final class GameConf implements Configurable {
	/**
	 * Prevent instantiation.
	 */
	private GameConf() {
	}

	/**
	 * The search path for dynamically loaded classes; see {@link ReflectUtils}.
	 */
	@NoSet
	public static final String SEARCH_PATH = "rekit";
	/**
	 * The GameWide randomness source.
	 */
	@NoSet
	public static final Random PRNG = new Random();

	/**
	 * The about text.
	 */
	public static String ABOUT;
	/**
	 * The text options for {@link #ABOUT}.
	 */
	@NoSet
	public static TextOptions ABOUT_TEXT;
	/**
	 * This boolean indicates whether the game is in debug mode.
	 */
	@NoSet
	public static boolean DEBUG = false;

	/**
	 * Indicates whether user attacks shall handled as continuous events or a
	 * single event.
	 */
	@NoSet
	public static boolean CONTINUOUS_ATTACK = false;

	/**
	 * The Name of the Game.
	 */
	public static String NAME;

	/**
	 * The version of the Game.
	 */
	public static String VERSION;

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
	 * Height in units that GameElement are tolerated to fall below the screens
	 * border before they will be deleted.
	 */
	public static float GRID_TOLERANCE_BELOW;

	/**
	 * Width of the window.<br>
	 * Calculated by {@link GameConf#GRID_W} * {@link GameConf#PX_PER_UNIT}.
	 */
	@NoSet
	public static int PIXEL_W;
	/**
	 * Height of the window.<br>
	 * Calculated by {@link GameConf#GRID_H} * {@link GameConf#PX_PER_UNIT}.
	 */
	@NoSet
	public static int PIXEL_H;

	/**
	 * Time in milliseconds to wait after each renderLoop, that refreshes all
	 * graphical elements.
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

	/**
	 * The menu's background color.
	 */
	public static RGBAColor MENU_BACKGROUND_COLOR;
	/**
	 * The menu's box color (default).
	 */
	public static RGBAColor MENU_BOX_COLOR;
	/**
	 * The menu's box color (selected).
	 */
	public static RGBAColor MENU_BOX_SELECT_COLOR;
	/**
	 * The menu's box color (option).
	 */
	public static RGBAColor MENU_BOX_OPTION_COLOR;
	/**
	 * The menu's text color.
	 */
	public static RGBAColor MENU_TEXT_COLOR;
	/**
	 * The menu's text font.
	 */
	public static String MENU_TEXT_FONT;
	/**
	 * The menu's text size.
	 */
	public static int MENU_TEXT_SIZE;
	/**
	 * The game's background color.
	 */
	public static RGBAColor GAME_BACKGROUD_COLOR;
	/**
	 * The game's text color.
	 */
	public static RGBAColor GAME_TEXT_COLOR;
	/**
	 * The game's text font.
	 */
	public static String GAME_TEXT_FONT;
	/**
	 * The game's text size.
	 */
	public static int GAME_TEXT_SIZE;
	/**
	 * The game's debug text color.
	 */
	@NoSet
	public static RGBAColor DEBUG_TEXT_COLOR;
	/**
	 * The game's text options.
	 */
	@NoSet
	public static TextOptions DEFAULT_TEXT;
	/**
	 * The game's text options (menu).
	 */
	@NoSet
	public static TextOptions MENU_TEXT;
	/**
	 * The game's text options (hints).
	 */
	@NoSet
	public static TextOptions HINT_TEXT;
	/**
	 * The Global GAME_LOGGER.
	 */
	@NoSet
	public static final Logger GAME_LOGGER = Logger.getLogger("@ReKiT");

	/**
	 * Set values which cannot be loaded by {@link Parser Parsers}.
	 */
	@AfterSetting
	public static void afterVisit() { // NO_UCD (unused code)
		GameConf.PIXEL_W = GameConf.GRID_W * GameConf.PX_PER_UNIT;
		GameConf.PIXEL_H = GameConf.GRID_H * GameConf.PX_PER_UNIT;

		GameConf.DEFAULT_TEXT = new TextOptions(new Vec(-1, 0), GameConf.GAME_TEXT_SIZE, GameConf.GAME_TEXT_COLOR, GameConf.GAME_TEXT_FONT, Font.PLAIN);
		GameConf.MENU_TEXT = new TextOptions(new Vec(-0.5F, -0.65f), GameConf.MENU_TEXT_SIZE, GameConf.MENU_TEXT_COLOR, GameConf.MENU_TEXT_FONT, Font.PLAIN);
		GameConf.DEBUG_TEXT_COLOR = new RGBAColor(255, 255, 255);
		GameConf.HINT_TEXT = GameConf.DEFAULT_TEXT.clone().setHeight(GameConf.GAME_TEXT_SIZE - 5).setColor(GameConf.DEBUG_TEXT_COLOR);
		GameConf.ABOUT_TEXT = GameConf.HINT_TEXT.clone().setHeight(GameConf.GAME_TEXT_SIZE - 2).setAlignmentLeft(new Vec(0, -0.5F));
	}
}
