package edu.kit.informatik.ragnarok.primitives;

/**
 * Representation of the 4 directions there are
 * @author Angelo Aracri
 * @version 1.0
 */
public enum Direction {
	/**
	 * Represents the Direction left with the Vector (-1|0)
	 */
	LEFT(new Vec2D(-1, 0), 1.5 * Math.PI),
	/**
	 * Represents the Direction right with the Vector (1|0)
	 */
	RIGHT(new Vec2D(1, 0), 0.5 * Math.PI),
	/**
	 * Represents the Direction up with the Vector (0|-1)
	 */
	UP(new Vec2D(0, -1), 0),
	/**
	 * Represents the Direction down with the Vector (0|1)
	 */
	DOWN(new Vec2D(0, 1), 1 * Math.PI);
	
	private Vec2D vec;
	private double angle;
	
	private Direction(Vec2D vec, double angle) {
		this.vec = vec;
		this.angle = angle;
	}
	
	/**
	 * Get the vector to a corresponding direction
	 * @return
	 */
	public Vec2D getVector() {
		return this.vec;
	}
	
	/**
	 * Get the angle to a corresponding direction relative to direction up
	 * @return
	 */
	public double getAngle() {
		return this.angle;
	}
	
	/**
	 * Get the opposite direction to a direction
	 * @return
	 */
	public Direction getOpposite() {
		switch(this) {
		case UP: return Direction.DOWN;
		case RIGHT: return Direction.LEFT;
		case DOWN: return Direction.UP;
		default: return Direction.RIGHT;
		}
	}
	
	/**
	 * Get the next direction to a direction (clockwise)
	 * @return
	 */
	public Direction getNextClockwise() {
		switch(this) {
		case UP: return Direction.RIGHT;
		case RIGHT: return Direction.DOWN;
		case DOWN: return Direction.LEFT;
		default: return Direction.UP;
		}
	}
	
	/**
	 * Get the next direction to a direction (anticlockwise)
	 * @return
	 */
	public Direction getNextAntiClockwise() {
		switch(this) {
		case UP: return Direction.LEFT;
		case RIGHT: return Direction.UP;
		case DOWN: return Direction.RIGHT;
		default: return Direction.DOWN;
		}
	}
}
