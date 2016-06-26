package edu.kit.informatik.ragnarok.primitives;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Polygon implements Cloneable {

	private Vec2D startPoint;
	private List<Vec2D> relPts;

	public Polygon(Vec2D startPoint, Vec2D[] relPts) {
		this.startPoint = startPoint;

		this.relPts = new LinkedList<>();
		for (Vec2D relPt : relPts) {
			this.addRelPt(relPt);
		}
	}

	public Polygon(Vec2D startPoint, List<Vec2D> relPts) {
		this.startPoint = startPoint;

		this.relPts = new LinkedList<>();
		for (Vec2D relPt : relPts) {
			this.addRelPt(relPt);
		}
	}

	Polygon(Vec2D startPoint) {
		this.startPoint = startPoint;
		this.relPts = new LinkedList<>();
	}

	@Override
	public Polygon clone() {
		return new Polygon(this.startPoint, this.relPts);
	}

	public void moveTo(Vec2D startPoint) {
		this.startPoint = startPoint;
	}

	public void addRelPt(Vec2D relPt) {
		this.relPts.add(relPt);
	}

	public Polygon rotate(float angle) {
		return this.rotate(angle, new Vec2D());
	}

	public Polygon rotate(float angle, Vec2D rotationAnchor) {
		// Create new Polygon with rotated start point
		Polygon result = new Polygon(this.startPoint.rotate(angle, rotationAnchor));

		// Rotate path for each point
		Iterator<Vec2D> it = this.relPts.iterator();
		while (it.hasNext()) {
			// We dont need rotationAnchor here since points are relative
			// startPoint
			result.addRelPt(it.next().rotate(angle));
		}

		return result;
	}

	public float[] getAbsoluteArray() {
		// prepare actual array {x1, y1, x2, y2, ...}
		float[] actualArray = new float[2 + this.relPts.size() * 2];

		// save first (absolute) point x1, y1
		actualArray[0] = this.startPoint.getX();
		actualArray[1] = this.startPoint.getY();

		// calculate rest of absolute points from relative points to start
		int i = 0;
		for (Vec2D relPt : this.relPts) {
			actualArray[2 * i + 2] = this.startPoint.getX() + relPt.getX();
			actualArray[2 * i + 3] = this.startPoint.getY() + relPt.getY();
			i++;
		}

		return actualArray;
	}

}
