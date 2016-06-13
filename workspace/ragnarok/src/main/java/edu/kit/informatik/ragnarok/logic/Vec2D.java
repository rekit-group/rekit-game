package edu.kit.informatik.ragnarok.logic;

/**
 * A two dimensional Vector with operations
 * @author Angelo Aracri
 * @version 1.0
 */
public class Vec2D {
	/**
	 * The x-component of the vector
	 */
	private float x;
	/**
	 * The y-component of the vector
	 */
	private float y;
	
	/**
	 * Constructor that takes the initial coordinates an saves them
	 * @param x the initial x-component of the vector
	 * @param y the initial y-component of the vector
	 */
	public Vec2D(float x, float y) {
		this.x = x;
		this.y = y;
	}
	/**
	 * Short-hand constructor that takes the default value (0|0)
	 */
	public Vec2D() {
		this.x = 0;
		this.y = 0;
	}

	/**
	 * Sets number to the x-component and <b>returns a new Vector</b>
	 * @param x the new x-component of the vector
	 * @return the vector with the new x-component
	 */
	public Vec2D setX(float x) {
		return new Vec2D(x, this.getY());
	}
	
	/**
	 * Adds a given number to the x-component and <b>returns a new Vector</b>
	 * @param deltaX the number to add to the original x-component of the vector
	 * @return the vector with the new x-component
	 */
	public Vec2D addX(float deltaX) {
		return new Vec2D(this.getX() + deltaX, this.getY());
	}

	/**
	 * Getter for the x-component of the vector
	 * @return the x-component
	 */
	public float getX() {
		return this.x;
	}

	/**
	 * Sets number to the y-component and <b>returns a new Vector</b>
	 * @param y the new y-component of the vector
	 * @return the vector with the new y-component
	 */
	public Vec2D setY(float y) {
		return new Vec2D(this.getX(), y);
	}
	
	/**
	 * Adds a given number to the y-component and <b>returns a new Vector</b>
	 * @param deltaY the number to add to the original y-component of the vector
	 * @return the vector with the new y-component
	 */
	public Vec2D addY(float deltaY) {
		return new Vec2D(this.getX(), this.getY() + deltaY);
	}
	
	/**
	 * Getter for the y-component of the vector
	 * @return the y-component
	 */
	public float getY() {
		return this.y;
	}
	
	/**
	 * Adds another vector to this vector and <b>returns a new Vector</b>
	 * @param vec the other vector to add
	 * @return the sum of the vectors
	 */
	public Vec2D add(Vec2D vec) {
		return new Vec2D(this.getX() + vec.getX(), this.getY() + vec.getY());
	}
	
	/**
	 * Multiplies the vector with a scalar and <b>returns a new Vector</b>
	 * @param scalar the scalar to multiply the vector with
	 * @return the resulting vector
	 */
	public Vec2D multiply(float scalar) {
		return new Vec2D(this.getX() * scalar, this.getY() * scalar);
	}
	
	
	public Vec2D rotate(double angle, Vec2D relative) {
		// translate toTurn to (0, 0) relative to relative
		Vec2D shifted = this.add(relative.multiply(-1));
		
		// rotate shifted vector
		Vec2D rotated = new Vec2D();
		rotated = rotated.setX((float)(shifted.getX() * Math.cos(angle) - shifted.getY() * Math.sin(angle)));
		rotated = rotated.setY((float)(shifted.getX() * Math.sin(angle) + shifted.getY() * Math.cos(angle)));
		
		return rotated.add(relative);
	}
	public Vec2D rotate(double angle) {
		return rotate(angle, new Vec2D());
	}
	
	
	public String toString() {
		return "(" + this.getX() + "|" + this.getY() + ")"; 
	}

}
