package edu.kit.informatik.ragnarok.primitives.geometry;

import edu.kit.informatik.ragnarok.primitives.operable.Operable;

/**
 * A two dimensional Vector with operations
 *
 * @author Angelo Aracri
 * @version 1.0
 */

public final class Vec implements Cloneable, Operable<Vec> {
	/**
	 * Create a new Vector
	 *
	 * @param x
	 *            posX
	 * @param y
	 *            posY
	 * @return the vector
	 */
	public static Vec create(float x, float y) {
		return new Vec(x, y);
	}

	/**
	 * The x-component of the vector
	 */
	private float x;
	/**
	 * The y-component of the vector
	 */
	private float y;
	/**
	 * The optional z-component of the vector
	 */
	private float z;

	/**
	 * Constructor that takes the initial coordinates an saves them
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
	 * Constructor that takes the initial coordinates an saves them
	 *
	 * @param x
	 *            the initial x-component of the vector
	 * @param y
	 *            the initial y-component of the vector
	 */
	public Vec(float x, float y) {
		this.x = x;
		this.y = y;
		this.z = 1;
	}

	/**
	 * Constructor that takes one initial coordinate an saves them as x and y
	 *
	 * @param x
	 *            the initial x- and y-component of the vector
	 */
	public Vec(float xy) {
		this.x = xy;
		this.y = xy;
		this.z = 1;
	}

	/**
	 * Short-hand constructor that takes the default value (0|0)
	 */
	public Vec() {
		this.x = 0;
		this.y = 0;
		this.z = 0;
	}

	/**
	 * Sets number to the x-component and <b>returns a new Vector</b>
	 *
	 * @param x
	 *            the new x-component of the vector
	 * @return the vector with the new x-component
	 */
	public Vec setX(float x) {
		return new Vec(x, this.getY(), this.z);
	}

	/**
	 * Adds a given number to the x-component and <b>returns a new Vector</b>
	 *
	 * @param deltaX
	 *            the number to add to the original x-component of the vector
	 * @return the vector with the new x-component
	 */
	public Vec addX(float deltaX) {
		return new Vec(this.getX() + deltaX, this.getY(), this.z);
	}

	/**
	 * Getter for the x-component of the vector
	 *
	 * @return the x-component
	 */
	public float getX() {
		return this.x;
	}

	/**
	 * Sets number to the y-component and <b>returns a new Vector</b>
	 *
	 * @param y
	 *            the new y-component of the vector
	 * @return the vector with the new y-component
	 */
	public Vec setY(float y) {
		return new Vec(this.getX(), y, this.z);
	}

	/**
	 * Adds a given number to the y-component and <b>returns a new Vector</b>
	 *
	 * @param deltaY
	 *            the number to add to the original y-component of the vector
	 * @return the vector with the new y-component
	 */
	public Vec addY(float deltaY) {
		return new Vec(this.getX(), this.getY() + deltaY, this.z);
	}

	/**
	 * Getter for the y-component of the vector
	 *
	 * @return the y-component
	 */
	public float getY() {
		return this.y;
	}

	/**
	 * Getter for the z-component of the vector
	 *
	 * @return the z-component
	 */
	public float getZ() {
		return this.z;
	}

	/**
	 * Adds another vector to this vector and <b>returns a new Vector</b>
	 *
	 * @param vec
	 *            the other vector to add
	 * @return the sum of the vectors
	 */
	@Override
	public Vec add(Vec vec) {
		return new Vec(this.getX() + vec.getX(), this.getY() + vec.getY(), this.z);
	}

	@Override
	public Vec sub(Vec other) {
		return this.add(other.scalar(-1));
	}

	/**
	 * Multiplies the vector with a scalar and <b>returns a new Vector</b>
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
	 * scalars and <b>returns a new Vector</b>
	 *
	 * @param scalarX
	 *            the scalar to multiply the vectors x-component with
	 * @param scalarY
	 *            the scalar to multiply the vectors x-component with
	 * @return the resulting vector
	 */
	public Vec scalar(float scalarX, float scalarY) {
		return new Vec(this.getX() * scalarX, this.getY() * scalarY, this.z);
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
		return new Vec(this.getX() * vec.getX(), this.getY() * vec.getY(), this.z);
	}

	public Vec sin() {
		return new Vec((float) Math.sin(this.getX()), (float) Math.sin(this.getY()));
	}
	
	public float getAngleTo(Vec other) {
		return (float)Math.atan2(other.x - this.x, other.y - this.y);
	}

	public Vec rotate(double angle, Vec relative) {
		// translate toTurn to (0, 0) relative to relative
		Vec shifted = this.add(relative.scalar(-1));

		// rotate shifted vector
		Vec rotated = new Vec();
		rotated = rotated.setX((float) (shifted.getX() * Math.cos(angle) - shifted.getY() * Math.sin(angle)));
		rotated = rotated.setY((float) (shifted.getX() * Math.sin(angle) + shifted.getY() * Math.cos(angle)));
		rotated = rotated.setZ(this.z);

		return rotated.add(relative);
	}

	public Vec rotate(double angle) {
		return this.rotate(angle, new Vec());
	}

	@Override
	public Vec clone() {
		return new Vec(this.getX(), this.getY(), this.z);
	}

	@Override
	public String toString() {
		return "(" + this.getX() + "|" + this.getY() + (this.getZ() != 0 ? "|" + this.getZ() : "") + ")";
	}

	public Vec setZ(float z) {
		return new Vec(this.x, this.y, z);
	}

	public Vec translate2D(float offset) {
		if (this.getZ() != 1) {
			return new Vec(this.getX() + offset / this.getZ(), this.getY());
		}
		return this;
	}

	@Override
	public Vec get() {
		return this;
	}

}
