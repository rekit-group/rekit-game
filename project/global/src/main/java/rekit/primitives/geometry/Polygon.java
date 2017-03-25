package rekit.primitives.geometry;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * This class defines a polygon.
 *
 */
public final class Polygon implements Cloneable {
	/**
	 * The first point's position.
	 */
	private Vec startPoint;
	/**
	 * The relative positions of the next points relative to
	 * {@link #startPoint}.
	 */
	private List<Vec> relPts;

	/**
	 * Create the polygon by start position and other points.
	 *
	 * @param startPoint
	 *            the first point's position <b>in units</b>
	 * @param relPts
	 *            the relative positions of the next points relative to
	 *            {@link #startPoint} <b>in units</b>
	 */
	public Polygon(Vec startPoint, Vec[] relPts) {
		this.startPoint = startPoint;
		this.relPts = new LinkedList<>();
		for (Vec relPt : relPts) {
			this.addRelPt(relPt);
		}
	}

	/**
	 * Create the polygon by start position and other points (used for cloning).
	 *
	 * @param startPoint
	 *            the first point's position <b>in units</b>
	 * @param relPts
	 *            the relative positions of the next points relative to
	 *            {@link #startPoint} <b>in units</b>
	 */
	private Polygon(Vec startPoint, List<Vec> relPts) {
		this.startPoint = startPoint;
		this.relPts = new LinkedList<>();
		for (Vec relPt : relPts) {
			this.addRelPt(relPt);
		}
	}

	/**
	 * Create the polygon by start position and other points
	 * {@link #rotate(float)} or {@link #rotate(float, Vec)}.
	 *
	 * @param startPoint
	 *            the first point's position <b>in units</b>
	 */
	private Polygon(Vec startPoint) {
		this.startPoint = startPoint;
		this.relPts = new LinkedList<>();
	}

	/**
	 * Get the start point of the polygon.
	 *
	 * @return the start point <b>in units</b>
	 */
	public Vec getStartPoint() {
		return this.startPoint;
	}

	@Override
	public Polygon clone() {
		return new Polygon(this.startPoint, this.relPts);
	}

	/**
	 * Move a polygon to target location.
	 *
	 * @param targetLocation
	 *            the target location <b>in units</b>
	 */
	public void moveTo(Vec targetLocation) {
		this.startPoint = targetLocation;
	}

	/**
	 * Add a new relative point.
	 *
	 * @param relPt
	 *            the point
	 */
	public void addRelPt(Vec relPt) {
		this.relPts.add(relPt);
	}

	/**
	 * Rotate the polygon by angle.
	 *
	 * @param angle
	 *            the angle
	 * @return the rotated polygon
	 */
	public Polygon rotate(float angle) {
		return this.rotate(angle, new Vec());
	}

	/**
	 * Rotate the polygon by angle and anchor.
	 *
	 * @param angle
	 *            the angle
	 * @param rotationAnchor
	 *            the rotation anchor <b>in units</b>
	 * @return the rotated polygon
	 */
	public Polygon rotate(float angle, Vec rotationAnchor) {
		// Create new Polygon with rotated start point
		Polygon result = new Polygon(this.startPoint.rotate(angle, rotationAnchor));

		// Rotate path for each point
		Iterator<Vec> it = this.relPts.iterator();
		while (it.hasNext()) {
			// We dont need rotationAnchor here since points are relative
			// startPoint
			result.addRelPt(it.next().rotate(angle));
		}

		return result;
	}

	/**
	 * Get the absolute positions of all points.
	 *
	 * @return the points <b>in units</b>
	 */
	public float[] getAbsoluteArray() {
		// prepare actual array {x1, y1, x2, y2, ...}
		float[] actualArray = new float[2 + this.relPts.size() * 2];

		// save first (absolute) point x1, y1
		actualArray[0] = this.startPoint.x;
		actualArray[1] = this.startPoint.y;

		// calculate rest of absolute points from relative points to start
		int i = 0;
		for (Vec relPt : this.relPts) {
			actualArray[2 * i + 2] = this.startPoint.x + relPt.x;
			actualArray[2 * i + 3] = this.startPoint.y + relPt.y;
			i++;
		}

		return actualArray;
	}

	/**
	 * Scale this polygon.
	 *
	 * @param scale
	 *            the factor
	 * @return the result polygon
	 */
	public Polygon scale(float scale) {
		List<Vec> newRelPts = new LinkedList<>();
		for (Vec relPt2scale : this.relPts) {
			newRelPts.add(relPt2scale.scalar(scale));
		}
		return new Polygon(this.startPoint, newRelPts);
	}
}
