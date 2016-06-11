package edu.kit.infomatik.config;

/**
 * Configuration class that holds static options
 * @author Angelo Aracri
 * @version 1.0
 */
public class c {
	
	/**
	 * Size of one in-game unit in pixels.
	 * So a Vec2D(1, 0) will be projected to (pxPerUnit, 0)
	 */
	public static final int pxPerUnit = 50;
	/**
	 * Grid width.
	 * Determines how broad the window game will be
	 */
	public static final int gridW = 16;
	/**
	 * Grid height.
	 * Determines how high the window game will be
	 */
	public static final int gridH = 8;
	
	/**
	 * Time in milliseconds to wait after each renderLoop, that refreshes all graphical elements
	 */
	public static final int renderDelta = 20;
	/**
	 * Time in milliseconds to wait after each logicLoop, that simulates physics changes positions,
	 * detects collisions, ... 
	 */
	public static final int logicDelta = 20;
	
	/**
	 * Gravitational constant g in pxPerUnit/s^2.
	 * Determines how fast something accelerates upon falling.
	 */
	public static final float g = 0.0981f;
	
	
	public static final float playerWalkAccel = 7*g;
	public static final float playerStopAccel = g;
	
	public static final float playerWalkMaxSpeed = 9*playerWalkAccel;
	
	public static final float playerJumpBoost = -50*g;
}
