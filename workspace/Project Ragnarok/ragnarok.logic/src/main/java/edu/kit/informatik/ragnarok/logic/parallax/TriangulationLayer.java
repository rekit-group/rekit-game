package edu.kit.informatik.ragnarok.logic.parallax;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import edu.kit.informatik.ragnarok.config.GameConf;
import edu.kit.informatik.ragnarok.logic.gameelements.Field;
import edu.kit.informatik.ragnarok.primitives.Polygon;
import edu.kit.informatik.ragnarok.primitives.Vec2D;
import edu.kit.informatik.ragnarok.util.RGBColor;

public class TriangulationLayer extends ParallaxLayer {

	private Edge lastIterationEdge;

	private List<Triangle> triangles;

	private float minY = GameConf.GRID_H - 3.5f;
	private float maxY = GameConf.GRID_H;

	public TriangulationLayer(float distanceFromFront) {
		super(distanceFromFront);

		this.lastIterationEdge = new Edge(new Vec2D(0, this.maxY), new Vec2D(0, this.minY));
		this.triangles = new LinkedList<Triangle>();
	}

	@Override
	public void logicLoop(float currentOffset) {
		// calculates this.x and this.generateUntil
		super.logicLoop(currentOffset);

		// while we need to generate
		while (this.currentlyGeneratedUntil + this.fieldXtoLayerX(this.x) < this.generateUntil) {
			// System.out.println((this.currentlyGeneratedUntil +
			// this.fieldXtoLayerX(this.x)) + " < " + this.generateUntil);

			float x;
			float y;

			if (this.lastIterationEdge.start.getX() >= this.lastIterationEdge.end.getX()) {
				x = this.lastIterationEdge.start.getX() + ParallaxLayer.RNG.nextFloat() * 3 + 3;
				y = this.lastIterationEdge.end.getY();
			} else {
				x = this.lastIterationEdge.end.getX() + ParallaxLayer.RNG.nextFloat() * 3 + 3;
				y = this.lastIterationEdge.start.getY();
			}
			Vec2D nextCorner = new Vec2D(x, y);

			Triangle iterationTriangle = new Triangle(this.lastIterationEdge.start, this.lastIterationEdge.end, nextCorner);

			LinkedList<Triangle> triangles = new LinkedList<Triangle>();
			this.recursiveTriangulation(triangles, 2, iterationTriangle);

			synchronized (this.synchronize()) {
				this.triangles.addAll(triangles);
			}

			float smallestX = Float.MAX_VALUE;
			for (int i = 0; i < 3; i++) {
				float cornerX = iterationTriangle.getCorner(i).getX();
				if (cornerX < smallestX) {
					smallestX = cornerX;
				}
			}

			this.currentlyGeneratedUntil = smallestX;
			if (this.lastIterationEdge.start.getX() >= this.lastIterationEdge.end.getX()) {
				this.lastIterationEdge = new Edge(this.lastIterationEdge.start, nextCorner);
			} else {
				this.lastIterationEdge = new Edge(this.lastIterationEdge.end, nextCorner);
			}

		}
	}

	public void recursiveTriangulation(List<Triangle> yet, int depthLeft, Triangle triangle) {
		// is number between 0 and 2 (inclusively)

		int randCorner = ParallaxLayer.RNG.nextInt(3);

		Vec2D separatingCorner = triangle.getCorner(randCorner);
		Edge sharedEdge = triangle.getEdge(randCorner);
		Vec2D separatingEdgePt = sharedEdge.getRandomVecInBetween();

		Triangle t1 = new Triangle(separatingCorner, separatingEdgePt, triangle.getCorner((randCorner + 1) % 3));
		Triangle t2 = new Triangle(separatingCorner, separatingEdgePt, triangle.getCorner((randCorner + 2) % 3));

		if (depthLeft <= 0) {
			t1.initToRender();
			t2.initToRender();
			yet.add(t1);
			yet.add(t2);
		} else {
			this.recursiveTriangulation(yet, depthLeft - 1, t1);
			this.recursiveTriangulation(yet, depthLeft - 1, t2);
		}
	}

	@Override
	public void render(Field f) {
		// call super method before this (drawing white rectangle)
		super.render(f);
		synchronized (this.synchronize()) {
			Iterator<Triangle> it = this.triangles.iterator();
			while (it.hasNext()) {
				Triangle t = it.next();
				t.render(f);
			}
		}
	}

	@Override
	public int getElementCount() {
		return this.triangles.size();
	}

	private class Triangle {
		private Vec2D[] corners = new Vec2D[3];
		private Polygon polygon;
		private RGBColor col;

		public Triangle(Vec2D corner0, Vec2D corner1, Vec2D corner2) {
			this.corners[0] = corner0;
			this.corners[1] = corner1;
			this.corners[2] = corner2;

			this.polygon = new Polygon(new Vec2D(), new Vec2D[] { corner1.add(corner0.multiply(-1)), corner2.add(corner0.multiply(-1)), new Vec2D() });
		}

		public void initToRender() {
			this.col = new RGBColor((int) HeapLayer.calcWithVariance(190, 23), (int) HeapLayer.calcWithVariance(120, 40),
					(int) HeapLayer.calcWithVariance(25, 10));
		}

		public Vec2D getCorner(int i) {
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

		public void render(Field f) {
			this.polygon.moveTo(this.corners[0].addX(TriangulationLayer.this.fieldXtoLayerX(TriangulationLayer.this.x)));
			f.drawPolygon(this.polygon, this.col);
		}

	}

	private class Edge {
		private Vec2D start;
		private Vec2D end;

		public Edge(Vec2D start, Vec2D end) {
			this.start = start;
			this.end = end;
		}

		public Vec2D getRandomVecInBetween() {
			return this.start.add((this.end.add(this.start.multiply(-1))).multiply(0.4f + ParallaxLayer.RNG.nextFloat() / 5f));
		}
	}

}
