package rekit.primitives.geometry;

import rekit.config.GameConf;

/**
 * Representation of the 4 directions there are.
 *
 * @author Angelo Aracri
 * @version 1.0
 */
public enum Direction {

	/**
	 * Represents the Direction up with the Vector (0|-1).
	 */
	UP(new Vec(0, -1), 0),
	/**
	 * Represents the Direction right with the Vector (1|0).
	 */
	RIGHT(new Vec(1, 0), 0.5 * Math.PI),
	/**
	 * Represents the Direction down with the Vector (0|1).
	 */
	DOWN(new Vec(0, 1), 1 * Math.PI),
	/**
	 * Represents the Direction left with the Vector (-1|0).
	 */
	LEFT(new Vec(-1, 0), 1.5 * Math.PI);
	/**
	 * The representing vector.
	 */
	private final Vec vec;
	/**
	 * The representing angle.
	 */
	private final double angle;

	/**
	 * Create a Direction.
	 *
	 * @param vec
	 *            the vector
	 * @param angle
	 *            the angle
	 *
	 */
	private Direction(Vec vec, double angle) {
		this.vec = vec;
		this.angle = angle;
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
		if (dir == null) {
			return Direction.getRandom();
		}
		return Direction.values()[Math.floorMod(dir.ordinal() + 2, Direction.values().length)];
	}

	/**
	 * Get the next direction to a direction (clockwise).
	 *
	 * @return the direction or {@code null} if none defined
	 */
	public Direction getNextClockwise() {
		return Direction.values()[Math.floorMod(this.ordinal() + 1, Direction.values().length)];
	}

	/**
	 * Get the next direction to a direction (anticlockwise).
	 *
	 * @return the direction or {@code null} if none defined
	 */
	public Direction getNextAntiClockwise() {
		return Direction.values()[Math.floorMod(this.ordinal() - 1, Direction.values().length)];
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
