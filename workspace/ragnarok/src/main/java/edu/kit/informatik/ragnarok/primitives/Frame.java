package edu.kit.informatik.ragnarok.primitives;

public class Frame {

	private Vec2D vec1;
	private Vec2D vec2;

	public Frame(Vec2D v1, Vec2D v2) {
		this.SetFrame(v1, v2);
	}

	public Frame(float x1, float y1, float x2, float y2) {
		this.SetFrame(new Vec2D(x1, y1), new Vec2D(x2, y2));
	}

	public void SetFrame(Vec2D v1, Vec2D v2) {
		this.vec1 = v1;
		this.vec2 = v2;
	}

	public boolean collidesWith(Frame otherFrame) {
		return vec1.getX() < otherFrame.vec2.getX()
				&& vec1.getY() < otherFrame.vec2.getY()
				&& vec2.getX() > otherFrame.vec1.getX()
				&& vec2.getY() > otherFrame.vec1.getY();
	}

	public boolean collidesWith(Vec2D vec) {
		return vec.getX() > vec1.getX() && vec.getX() < vec2.getX()
				&& vec.getY() > vec1.getY() && vec.getY() < vec2.getY();
	}
	
	/**
	 * Gets the component of the frames border at given direction
	 * @param dir The direction of border
	 * @return the corresponding coordinate component of the border
	 */
	public float getBorder(Direction dir) {
		switch (dir) {
		case UP:
			// In case we want the upper border: take highest y
			return this.vec1.getY() > this.vec2.getY() ? this.vec1.getY()
					: this.vec2.getY();
		case RIGHT:
			// In case we want the right border: take highest x
			return this.vec1.getX() > this.vec2.getX() ? this.vec1.getX()
					: this.vec2.getX();
		case DOWN:
			// In case we want the lower border: take lowest y
			return this.vec1.getY() > this.vec2.getY() ? this.vec2.getY()
					: this.vec1.getY();
		case LEFT:
			// In case we want the left border: take lowest x
			return this.vec1.getX() > this.vec2.getX() ? this.vec2.getX()
					: this.vec1.getX();
		}
		return -1;
	}
	
	public String toString() {
		return "(" + this.vec1.toString() + ", " + this.vec2.toString() + ")";
	}

}