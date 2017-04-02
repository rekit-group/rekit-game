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
		return this.upperLeftAnchor.x < otherFrame.bottomRightAnchor.x && this.upperLeftAnchor.y < otherFrame.bottomRightAnchor.y
				&& this.bottomRightAnchor.x > otherFrame.upperLeftAnchor.x && this.bottomRightAnchor.y > otherFrame.upperLeftAnchor.y;
	}

	/**
	 * Check whether this frame collides with another object.
	 *
	 * @param position
	 *            of the object
	 * @return {@code true} if collision detected, {@code false} otherwise
	 */
	public boolean collidesWith(Vec position) {
		return position.x > this.upperLeftAnchor.x && position.x < this.bottomRightAnchor.x && position.y > this.upperLeftAnchor.y
				&& position.y < this.bottomRightAnchor.y;
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
			return this.upperLeftAnchor.y > this.bottomRightAnchor.y ? this.upperLeftAnchor.y : this.bottomRightAnchor.y;
		case RIGHT:
			// In case we want the right border: take highest x
			return this.upperLeftAnchor.x > this.bottomRightAnchor.x ? this.upperLeftAnchor.x : this.bottomRightAnchor.x;
		case DOWN:
			// In case we want the lower border: take lowest y
			return this.upperLeftAnchor.y > this.bottomRightAnchor.y ? this.bottomRightAnchor.y : this.upperLeftAnchor.y;
		case LEFT:
			// In case we want the left border: take lowest x
			return this.upperLeftAnchor.x > this.bottomRightAnchor.x ? this.bottomRightAnchor.x : this.upperLeftAnchor.x;
		default:
			return -1;
		}

	}

	@Override
	public String toString() {
		return "(" + this.upperLeftAnchor + ", " + this.bottomRightAnchor + ")";
	}

}
