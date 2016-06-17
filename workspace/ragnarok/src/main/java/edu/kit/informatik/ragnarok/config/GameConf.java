package edu.kit.informatik.ragnarok.config;

import java.util.ResourceBundle;

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
	public static final int pxPerUnit = GameConf.BUNDLE.getInt("pxPerUnit");
	/**
	 * Grid width. Determines how broad the window game will be
	 */
	public static final int gridW = GameConf.BUNDLE.getInt("gridW");
	/**
	 * Grid height. Determines how high the window game will be
	 */
	public static final int gridH = GameConf.BUNDLE.getInt("gridH");;

	/**
	 * Time in milliseconds to wait after each renderLoop, that refreshes all
	 * graphical elements
	 */
	public static final int renderDelta = GameConf.BUNDLE.getInt("renderDelta");
	/**
	 * Time in milliseconds to wait after each logicLoop, that simulates physics
	 * changes positions, detects collisions, ...
	 */
	public static final int logicDelta = GameConf.BUNDLE.getInt("logicDelta");

	/**
	 * Gravitational constant g in pxPerUnit/s^2. Determines how fast something
	 * accelerates upon falling.
	 */
	public static final float g = GameConf.BUNDLE.getFloat("g");

	public static final float playerWalkAccel = GameConf.BUNDLE.getFloat("playerWalkAccel");
	public static final float playerStopAccel = GameConf.BUNDLE.getFloat("playerStopAccel");

	public static final float playerWalkMaxSpeed = GameConf.BUNDLE.getFloat("playerWalkMaxSpeed");

	public static final float playerJumpBoost = GameConf.BUNDLE.getFloat("playerJumpBoost");
	public static final float playerBottomBoost = GameConf.BUNDLE.getFloat("playerBottomBoost");

	public static final float playerDist = GameConf.BUNDLE.getFloat("playerDist");
	public static final int playerLifes = GameConf.BUNDLE.getInt("playerLifes");

	public static final float slurpSpeed = GameConf.BUNDLE.getFloat("slurpSpeed");
	public static final float slurpPopOffsPerSec = GameConf.BUNDLE.getFloat("slurpPopOffsPerSec");

}
