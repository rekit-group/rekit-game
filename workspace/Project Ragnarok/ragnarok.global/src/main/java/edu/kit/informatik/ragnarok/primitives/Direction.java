package edu.kit.informatik.ragnarok.primitives;

/**
 * Representation of the 4 directions there are
 *
 * @author Angelo Aracri
 * @version 1.0
 */
public enum Direction {
	/**
	 * Represents the Direction left with the Vector (-1|0)
	 */

	LEFT(Vec.create(-1, 0), 1.5 * Math.PI),
	/**
	 * Represents the Direction right with the Vector (1|0)
	 */
	RIGHT(Vec.create(1, 0), 0.5 * Math.PI),
	/**
	 * Represents the Direction up with the Vector (0|-1)
	 */
	UP(Vec.create(0, -1), 0),
	/**
	 * Represents the Direction down with the Vector (0|1)
	 */
	DOWN(Vec.create(0, 1), 1 * Math.PI);
	/**
	 * The representing vector
	 */
	private Vec vec;
	/**
	 * The representing angle
	 */
	private double angle;

	/**
	 * Create a Direction
	 *
	 * @param vec
	 *            the vector
	 * @param angle
	 *            the angle
	 */
	private Direction(Vec vec, double angle) {
		this.vec = vec;
		this.angle = angle;
	}

	/**
	 * Get the vector to a corresponding direction
	 *
	 * @return the vector
	 */
	public Vec getVector() {
		return this.vec;
	}

	/**
	 * Get the angle to a corresponding direction relative to direction up
	 *
	 * @return the angle
	 */
	public double getAngle() {
		return this.angle;
	}

	/**
	 * Get the opposite direction to a direction
	 *
	 * @return the direction or {@code null} if none defined
	 */
	public Direction getOpposite() {
		switch (this) {
		case UP:
			return Direction.DOWN;
		case RIGHT:
			return Direction.LEFT;
		case DOWN:
			return Direction.UP;
		case LEFT:
			return Direction.RIGHT;
		default:
			return null;
		}
	}

	/**
	 * Get the next direction to a direction (clockwise)
	 *
	 * @return the direction or {@code null} if none defined
	 */
	public Direction getNextClockwise() {
		switch (this) {
		case UP:
			return Direction.RIGHT;
		case RIGHT:
			return Direction.DOWN;
		case DOWN:
			return Direction.LEFT;
		case LEFT:
			return Direction.UP;
		default:
			return null;
		}
	}

	/**
	 * Get the next direction to a direction (anticlockwise)
	 *
	 * @return the direction or {@code null} if none defined
	 */
	public Direction getNextAntiClockwise() {
		switch (this) {
		case UP:
			return Direction.LEFT;
		case RIGHT:
			return Direction.UP;
		case DOWN:
			return Direction.RIGHT;
		case LEFT:
			return Direction.DOWN;
		default:
			return null;
		}
	}

	/**
	 * Get a random Direction
	 * 
	 * @return the direction or {@code null} if none defined
	 */
	public static Direction getRandom() {
		return Direction.values()[(int) (Math.random() * Direction.values().length)];
	}
}
