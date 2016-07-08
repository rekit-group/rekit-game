package edu.kit.informatik.ragnarok.logic.parallax;

import java.util.LinkedList;
import java.util.List;

import edu.kit.informatik.ragnarok.config.GameConf;
import edu.kit.informatik.ragnarok.logic.gameelements.Field;
import edu.kit.informatik.ragnarok.primitives.Polygon;
import edu.kit.informatik.ragnarok.primitives.Vec;
import edu.kit.informatik.ragnarok.util.RGBColor;

public class TriangulationLayer extends ParallaxLayer {

	private Edge lastIterationEdge;

	private float minY = GameConf.GRID_H - 3.5f;
	private float maxY = GameConf.GRID_H;

	private int triangleId;

	public TriangulationLayer(float distanceFromFront) {
		super(distanceFromFront);

		this.lastIterationEdge = new Edge(new Vec(0, this.maxY), new Vec(0, this.minY));
	}

	@Override
	public void logicLoop(float currentOffset) {
		// calculates this.x and this.generateUntil
		super.logicLoop(currentOffset);

		// while we need to generate
		while (this.currentlyGeneratedUntil + this.fieldXtoLayerX(this.x) < this.generateUntil) {

			float x;
			float y;
			float smallestX;

			// determine which Point of the last Edge is further right, then:
			// go even further (x-wise) and to the other side (y-wise)
			if (this.lastIterationEdge.start.getX() >= this.lastIterationEdge.end.getX()) {
				x = this.lastIterationEdge.start.getX() + ParallaxLayer.RNG.nextFloat() * 3 + 3;
				y = this.lastIterationEdge.end.getY();
				smallestX = this.lastIterationEdge.end.getX();
			} else {
				x = this.lastIterationEdge.end.getX() + ParallaxLayer.RNG.nextFloat() * 3 + 3;
				y = this.lastIterationEdge.start.getY();
				smallestX = this.lastIterationEdge.start.getX();
			}
			Vec nextCorner = new Vec(x, y);

			// create Triangle from last two and newly created points
			Triangle iterationTriangle = new Triangle(this.lastIterationEdge.start, this.lastIterationEdge.end, nextCorner);

			// recursively split this triangle
			LinkedList<Triangle> triangles = new LinkedList<>();
			this.recursiveTriangulation(triangles, 2, iterationTriangle);

			// add all triangles to model
			for (Triangle t : triangles) {
				t.backgroundZ -= this.triangleId--;
				this.scene.addGameElement(t);
			}

			this.currentlyGeneratedUntil = smallestX;
			if (this.lastIterationEdge.start.getX() >= this.lastIterationEdge.end.getX()) {
				this.lastIterationEdge = new Edge(this.lastIterationEdge.start, nextCorner);
			} else {
				this.lastIterationEdge = new Edge(this.lastIterationEdge.end, nextCorner);
			}
		}

		/*
		 * synchronized (this.synchronize()) { Iterator<Triangle> it =
		 * this.triangles.iterator(); while (it.hasNext()) { Triangle t =
		 * it.next(); if (t.getCorner(0)) } }
		 */
	}

	public void recursiveTriangulation(List<Triangle> yet, int depthLeft, Triangle triangle) {
		// is number between 0 and 2 (inclusively)
		int randCorner = ParallaxLayer.RNG.nextInt(3);

		// get corresponding corner
		Vec separatingCorner = triangle.getCorner(randCorner);
		// and opposite edge
		Edge sharedEdge = triangle.getEdge(randCorner);
		// now determine point on this edge
		Vec separatingEdgePt = sharedEdge.getRandomVecInBetween();

		// create 2 triangles with random corner, the point on the edge and with
		// each of the remaining two corners
		Triangle t1 = new Triangle(separatingCorner, separatingEdgePt, triangle.getCorner((randCorner + 1) % 3));
		Triangle t2 = new Triangle(separatingCorner, separatingEdgePt, triangle.getCorner((randCorner + 2) % 3));

		// if recursion depth is exhausted
		if (depthLeft <= 0) {
			// prepare thess triangles as actually used for drawing
			t1.initToRender();
			t2.initToRender();
			// and add them to the result of the recursion
			yet.add(t1);
			yet.add(t2);
		} else {
			// we need to go deeper - split two triangles further up
			this.recursiveTriangulation(yet, depthLeft - 1, t1);
			this.recursiveTriangulation(yet, depthLeft - 1, t2);
		}
	}

	private class Triangle extends BackgroundElement {
		private Vec[] corners = new Vec[3];
		private Polygon polygon;
		private RGBColor col;
		private RGBColor darkCol;

		public Triangle(Vec corner0, Vec corner1, Vec corner2) {
			super(TriangulationLayer.this, new Vec());

			this.corners[0] = corner0.setZ(TriangulationLayer.this.perspectiveZ);
			this.corners[1] = corner1;
			this.corners[2] = corner2;

			// calculate pos and size
			float minX = Float.POSITIVE_INFINITY;
			float maxX = Float.NEGATIVE_INFINITY;
			float minY = Float.POSITIVE_INFINITY;
			float maxY = Float.NEGATIVE_INFINITY;

			for (int i = 0; i < 3; i++) {
				minX = Math.min(this.corners[i].getX(), minX);
				maxX = Math.max(this.corners[i].getX(), maxX);
				minY = Math.min(this.corners[i].getY(), minY);
				maxY = Math.max(this.corners[i].getY(), maxY);
			}
			this.setPos(new Vec(minX + (maxX - minX) / 2f, minY + (maxY - minY) / 2f));
			this.size = new Vec(maxX - minX, maxY - minY);

			this.polygon = new Polygon(new Vec(), new Vec[] { corner1.add(corner0.multiply(-1)), corner2.add(corner0.multiply(-1)), new Vec() });
		}

		public void initToRender() {
			this.col = new RGBColor((int) HeapLayer.calcWithVariance(240, 15), (int) HeapLayer.calcWithVariance(206, 10),
					(int) HeapLayer.calcWithVariance(140, 10));
			this.darkCol = this.col.darken(0.9f);
			this.setPos(this.getPos().setZ(TriangulationLayer.this.perspectiveZ));
		}

		public Vec getCorner(int i) {
			return this.corners[i];
		}

		public Edge getEdge(int i) {
			switch (i) {
			case 0:
				return new Edge(this.getCorner(1), this.getCorner(2));
			case 1:
				return new Edge(this.getCorner(2), this.getCorner(0));
			default:
				return new Edge(this.getCorner(1), this.getCorner(0));
			}
		}

		@Override
		public void internalRender(Field f) {
			this.polygon.moveTo(this.corners[0]);
			f.drawPolygon(this.polygon, this.col, true);
			f.drawPolygon(this.polygon, this.darkCol, false);
		}
	}

	private class Edge {
		private Vec start;
		private Vec end;

		public Edge(Vec start, Vec end) {
			this.start = start;
			this.end = end;
		}

		public Vec getRandomVecInBetween() {
			return this.start.add((this.end.add(this.start.multiply(-1))).multiply(0.4f + ParallaxLayer.RNG.nextFloat() / 5f));
		}
	}

}
