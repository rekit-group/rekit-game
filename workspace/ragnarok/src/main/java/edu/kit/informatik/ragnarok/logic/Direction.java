package edu.kit.informatik.ragnarok.logic;

/**
 * Representation of the 4 directions there are
 * @author Angelo Aracri
 * @version 1.0
 */
public enum Direction {
	/**
	 * Represents the Direction left with the Vector (-1|0)
	 */
	LEFT(new Vec2D(-1, 0)),
	/**
	 * Represents the Direction right with the Vector (1|0)
	 */
	RIGHT(new Vec2D(1, 0)),
	/**
	 * Represents the Direction up with the Vector (0|-1)
	 */
	UP(new Vec2D(0, -1)),
	/**
	 * Represents the Direction down with the Vector (0|1)
	 */
	DOWN(new Vec2D(0, 1));
	
	private final Vec2D vec;
	
	private Direction(Vec2D vec) {
		this.vec = vec;
	}
	
	/**
	 * Get the vector to a corresponding direction
	 * @return
	 */
	public Vec2D getVector() {
		return this.vec;
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
}
