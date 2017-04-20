package rekit.primitives.geometry;

import org.fuchss.configuration.annotations.ClassParser;

import net.jafama.FastMath;
import rekit.parser.VecParser;
import rekit.primitives.operable.Operable;

/**
 * A three dimensional Vector with operations.
 *
 * @author Angelo Aracri
 *
 */
@ClassParser(VecParser.class)
public final class Vec implements Cloneable, Operable<Vec> {

	/**
	 * The x-component of the vector.
	 */
	public final float x;
	/**
	 * The y-component of the vector.
	 */
	public final float y;
	/**
	 * The optional z-component of the vector.
	 */
	public final float z;

	/**
	 * Constructor that takes the initial coordinates an saves them.
	 *
	 * @param x
	 *            the initial x-component of the vector
	 * @param y
	 *            the initial y-component of the vector
	 * @param z
	 *            the initial z-component of the vector
	 */
	public Vec(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * Constructor that takes the initial coordinates an saves them.
	 *
	 * @param x
	 *            the initial x-component of the vector
	 * @param y
	 *            the initial y-component of the vector
	 */
	public Vec(float x, float y) {
		this(x, y, 0);
	}
	
	/**
	 * Constructor that takes the initial coordinates an saves them.
	 * Note that only floats are used internally.
	 *
	 * @param x
	 *            the initial x-component of the vector
	 * @param y
	 *            the initial y-component of the vector
	 */
	public Vec(double x, double y) {
		this((float) x, (float) y, 0);
	}

	/**
	 * Constructor that takes the initial coordinates an saves them.
	 *
	 * @param x
	 *            the initial x-component of the vector
	 * @param y
	 *            the initial y-component of the vector
	 */
	public Vec(int x, int y) {
		this(x, y, 0);
	}

	/**
	 * Constructor that takes one initial coordinate an saves them as x and y.
	 *
	 * @param xy
	 *            the initial x- and y-component of the vector
	 */
	public Vec(float xy) {
		this(xy, xy, 0);
	}

	/**
	 * Short-hand constructor that takes the default value (0|0).
	 */
	public Vec() {
		this.x = 0;
		this.y = 0;
		this.z = 0;
	}

	/**
	 * Sets number to the x-component and <b>returns a new Vector</b>.
	 *
	 * @param x
	 *            the new x-component of the vector
	 * @return the vector with the new x-component
	 */
	public Vec setX(float x) {
		return new Vec(x, this.y, this.z);
	}

	/**
	 * Adds a given number to the x-component and <b>returns a new Vector</b>.
	 *
	 * @param deltaX
	 *            the number to add to the original x-component of the vector
	 * @return the vector with the new x-component
	 */
	public Vec addX(float deltaX) {
		return new Vec(this.x + deltaX, this.y, this.z);
	}

	/**
	 * Sets number to the y-component and <b>returns a new Vector</b>.
	 *
	 * @param y
	 *            the new y-component of the vector
	 * @return the vector with the new y-component
	 */
	public Vec setY(float y) {
		return new Vec(this.x, y, this.z);
	}

	/**
	 * Adds a given number to the y-component and <b>returns a new Vector</b>.
	 *
	 * @param deltaY
	 *            the number to add to the original y-component of the vector
	 * @return the vector with the new y-component
	 */
	public Vec addY(float deltaY) {
		return new Vec(this.x, this.y + deltaY, this.z);
	}

	/**
	 * Adds another vector to this vector and <b>returns a new Vector</b>.
	 *
	 * @param vec
	 *            the other vector to add
	 * @return the sum of the vectors
	 */
	@Override
	public Vec add(Vec vec) {
		return new Vec(this.x + vec.x, this.y + vec.y, this.z);
	}

	@Override
	public Vec sub(Vec other) {
		return this.add(other.scalar(-1));
	}

	/**
	 * Calculate the sin of x and y.
	 *
	 * @return a new vector (sinx, siny)
	 */
	public Vec sin() {
		return new Vec((float) FastMath.sinQuick(this.x), (float) FastMath.sinQuick(this.y));

	}

	/**
	 * Multiplies the vector with a scalar and <b>returns a new Vector</b>.
	 *
	 * @param scalar
	 *            the scalar to multiply the vector with
	 * @return the resulting vector
	 */
	@Override
	public Vec scalar(float scalar) {
		return this.scalar(scalar, scalar);
	}

	/**
	 * Multiplies the vectors x-component and its y-component with separate
	 * scalars and <b>returns a new Vector</b>.
	 *
	 * @param scalarX
	 *            the scalar to multiply the vectors x-component with
	 * @param scalarY
	 *            the scalar to multiply the vectors x-component with
	 * @return the resulting vector
	 */
	public Vec scalar(float scalarX, float scalarY) {
		return new Vec(this.x * scalarX, this.y * scalarY, this.z);
	}

	/**
	 * Multiplies the vectors x-component and its y-component with the
	 * x-component and y-component of another Vector and <b>returns a new
	 * Vector</b>.
	 *
	 * @param vec
	 *            vector to multiply with
	 * @return the resulting vector
	 */
	@Override
	public Vec multiply(Vec vec) {
		return new Vec(this.x * vec.x, this.y * vec.y, this.z);
	}

	/**
	 * Get the angle to another vector.
	 *
	 * @param other
	 *            the other vector
	 * @return the angle (rad)
	 */
	public float getAngleTo(Vec other) {
		return (float) FastMath.atan2(other.x - this.x, other.y - this.y);
	}

	/**
	 * Rotate this vector relative to a vector.
	 *
	 * @param angle
	 *            the angle
	 * @param relative
	 *            the relative vector for rotation
	 * @return the new vector
	 */
	public Vec rotate(double angle, Vec relative) {
		// translate toTurn to (0, 0) relative to relative
		Vec shifted = this.add(relative.scalar(-1));

		// rotate shifted vector
		Vec rotated = new Vec();
		rotated = rotated.setX((float) (shifted.x * FastMath.cosQuick(angle) - shifted.y * FastMath.sinQuick(angle)));
		rotated = rotated.setY((float) (shifted.x * FastMath.sinQuick(angle) + shifted.y * FastMath.cosQuick(angle)));
		rotated = rotated.setZ(this.z);

		return rotated.add(relative);
	}

	/**
	 * Rotate this vector relative to (0|0).
	 *
	 * @param angle
	 *            the angle
	 * @return the new vector
	 */
	public Vec rotate(double angle) {
		return this.rotate(angle, new Vec());
	}

	@Override
	public Vec clone() {
		return new Vec(this.x, this.y, this.z);
	}

	@Override
	public String toString() {
		return "(" + this.x + "|" + this.y + (this.z != 0 ? "|" + this.z : "") + ")";
	}

	/**
	 * Create a new vector with this x,y values but a new z value.
	 *
	 * @param z
	 *            the new z value
	 * @return a new vector (x,y,z)
	 */
	public Vec setZ(float z) {
		return new Vec(this.x, this.y, z);
	}

	/**
	 * Translate a 3D-Vector to 2D.
	 *
	 * @param offset
	 *            the offset (3D)
	 * @return the new vector
	 */
	public Vec translate2D(float offset) {
		if (this.z != 0) {
			return new Vec(this.x + offset / this.z, this.y);
		}
		return this;
	}

	@Override
	public Vec get() {
		return this;
	}

	/**
	 * Apply {@link Math#abs(float)} to x and y component and create new vector.
	 *
	 * @return the resulting vector
	 */
	public Vec abs() {
		return new Vec(FastMath.abs(this.x), FastMath.abs(this.y), this.z);
	}

}
