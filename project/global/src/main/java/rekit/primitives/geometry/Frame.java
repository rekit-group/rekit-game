package rekit.primitives.geometry;

/**
 *
 * This class defines a Frame which will be used to check collisions.
 *
 */
public final class Frame {
	/**
	 * The first anchor of the frame.
	 */
	private Vec upperLeftAnchor;
	/**
	 * The second anchor of the frame.
	 */
	private Vec bottomRightAnchor;

	/**
	 * Create a frame by two anchors.
	 *
	 * @param v1
	 *            the upperLeft anchor
	 * @param v2
	 *            the bottomRight anchor
	 */
	public Frame(Vec v1, Vec v2) {
		this.upperLeftAnchor = v1;
		this.bottomRightAnchor = v2;
	}

	/**
	 * Check whether this frame collides with another.
	 *
	 * @param otherFrame
	 *            the other frame
	 * @return {@code true} if collision detected, {@code false} otherwise
	 */
	public boolean collidesWith(Frame otherFrame) {
		return this.upperLeftAnchor.getX() < otherFrame.bottomRightAnchor.getX() && this.upperLeftAnchor.getY() < otherFrame.bottomRightAnchor.getY()
				&& this.bottomRightAnchor.getX() > otherFrame.upperLeftAnchor.getX()
				&& this.bottomRightAnchor.getY() > otherFrame.upperLeftAnchor.getY();
	}

	/**
	 * Check whether this frame collides with another object.
	 *
	 * @param position
	 *            of the object
	 * @return {@code true} if collision detected, {@code false} otherwise
	 */
	public boolean collidesWith(Vec position) {
		return position.getX() > this.upperLeftAnchor.getX() && position.getX() < this.bottomRightAnchor.getX()
				&& position.getY() > this.upperLeftAnchor.getY() && position.getY() < this.bottomRightAnchor.getY();
	}

	/**
	 * Gets the component of the frames border at given direction.
	 *
	 * @param dir
	 *            The direction of border
	 * @return the corresponding coordinate component of the border or
	 *         {@code -1} on failure
	 */
	public float getBorder(Direction dir) {
		switch (dir) {
		case UP:
			// In case we want the upper border: take highest y
			return this.upperLeftAnchor.getY() > this.bottomRightAnchor.getY() ? this.upperLeftAnchor.getY() : this.bottomRightAnchor.getY();
		case RIGHT:
			// In case we want the right border: take highest x
			return this.upperLeftAnchor.getX() > this.bottomRightAnchor.getX() ? this.upperLeftAnchor.getX() : this.bottomRightAnchor.getX();
		case DOWN:
			// In case we want the lower border: take lowest y
			return this.upperLeftAnchor.getY() > this.bottomRightAnchor.getY() ? this.bottomRightAnchor.getY() : this.upperLeftAnchor.getY();
		case LEFT:
			// In case we want the left border: take lowest x
			return this.upperLeftAnchor.getX() > this.bottomRightAnchor.getX() ? this.bottomRightAnchor.getX() : this.upperLeftAnchor.getX();
		}
		return -1;
	}

	@Override
	public String toString() {
		return "(" + this.upperLeftAnchor.toString() + ", " + this.bottomRightAnchor.toString() + ")";
	}

}
