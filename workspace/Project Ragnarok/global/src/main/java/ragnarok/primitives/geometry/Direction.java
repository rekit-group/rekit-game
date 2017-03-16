package ragnarok.primitives.geometry;

import ragnarok.config.GameConf;

/**
 * Representation of the 4 directions there are.
 *
 * @author Angelo Aracri
 * @version 1.0
 */
public enum Direction {
	/**
	 * Represents the Direction left with the Vector (-1|0).
	 */

	LEFT(new Vec(-1, 0), 1.5 * Math.PI, 3),
	/**
	 * Represents the Direction right with the Vector (1|0).
	 */
	RIGHT(new Vec(1, 0), 0.5 * Math.PI, 1),
	/**
	 * Represents the Direction up with the Vector (0|-1).
	 */
	UP(new Vec(0, -1), 0, 0),
	/**
	 * Represents the Direction down with the Vector (0|1).
	 */
	DOWN(new Vec(0, 1), 1 * Math.PI, 2);
	/**
	 * The representing vector.
	 */
	private final Vec vec;
	/**
	 * The representing angle.
	 */
	private final double angle;
	/**
	 * The position id; 0 represents UP, the rest is clockwise.
	 */
	private final int posId;

	/**
	 * Create a Direction.
	 *
	 * @param vec
	 *            the vector
	 * @param angle
	 *            the angle
	 */
	private Direction(Vec vec, double angle, int pos) {
		this.vec = vec;
		this.angle = angle;
		this.posId = pos;
	}

	/**
	 * Get the vector to a corresponding direction.
	 *
	 * @return the vector
	 */
	public Vec getVector() {
		return this.vec;
	}

	/**
	 * Get the angle to a corresponding direction relative to direction up.
	 *
	 * @return the angle
	 */
	public double getAngle() {
		return this.angle;
	}

	/**
	 * Get the opposite direction to a direction.
	 *
	 * @param dir
	 *            the direction
	 *
	 * @return the direction or random if dir == null
	 */
	public static Direction getOpposite(Direction dir) {
		switch (dir) {
		case UP:
			return Direction.DOWN;
		case RIGHT:
			return Direction.LEFT;
		case DOWN:
			return Direction.UP;
		case LEFT:
			return Direction.RIGHT;
		default:
			return Direction.getRandom();
		}
	}

	/**
	 * Get the next direction to a direction (clockwise).
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
	 * Get the next direction to a direction (anticlockwise).
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
	 * Get the position id.
	 * 
	 * @return the position id
	 */
	public int getPosId() {
		return this.posId;
	}

	/**
	 * Get a random Direction.
	 *
	 * @return the direction or {@code null} if none defined
	 */
	public static Direction getRandom() {
		return Direction.values()[(int) (GameConf.PRNG.nextDouble() * Direction.values().length)];
	}

}
