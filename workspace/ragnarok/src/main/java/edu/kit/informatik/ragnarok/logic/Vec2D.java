package edu.kit.informatik.ragnarok.logic;

public class Vec2D {
	private float x;

	public Vec2D(float x, float y) {
		this.setX(x);
		this.setY(y);
	}

	private void setX(float value) {
		this.x = value;
	}

	public void addX(float value) {
		this.x += value;
	}

	public float getX() {
		return this.x;
	}

	private float y;

	private void setY(float value) {
		this.y = value;
	}

	public void addY(float value) {
		this.y += value;
	}

	public float getY() {
		return this.y;
	}

	public Vec2D add(Vec2D vec) {
		return new Vec2D(this.getX() + vec.getX(), this.getY() + vec.getY());
	}

	public Vec2D multiply(float val) {
		return new Vec2D(this.getX() * val, this.getY() * val);
	}

}