package rekit.logic.gui.parallax;

import java.util.LinkedList;
import java.util.List;

import rekit.config.GameConf;
import rekit.core.GameGrid;
import rekit.primitives.geometry.Polygon;
import rekit.primitives.geometry.Vec;
import rekit.primitives.image.RGBAColor;

public class TriangulationLayer extends ParallaxLayer {

	private Edge lastIterationEdge;

	private float minY = GameConf.GRID_H - 3.2f;
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
			if (this.lastIterationEdge.start.x >= this.lastIterationEdge.end.x) {
				x = this.lastIterationEdge.start.x + GameConf.PRNG.nextFloat() * 3 + 3;
				y = this.lastIterationEdge.end.y;
				smallestX = this.lastIterationEdge.end.x;
			} else {
				x = this.lastIterationEdge.end.x + GameConf.PRNG.nextFloat() * 3 + 3;
				y = this.lastIterationEdge.start.y;
				smallestX = this.lastIterationEdge.start.x;
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
			if (this.lastIterationEdge.start.x >= this.lastIterationEdge.end.x) {
				this.lastIterationEdge = new Edge(this.lastIterationEdge.start, nextCorner);
			} else {
				this.lastIterationEdge = new Edge(this.lastIterationEdge.end, nextCorner);
			}
		}
	}

	public void recursiveTriangulation(List<Triangle> yet, int depthLeft, Triangle triangle) {
		// is number between 0 and 2 (inclusively)
		int randCorner = GameConf.PRNG.nextInt(3);

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
		private RGBAColor col;
		private RGBAColor darkCol;

		Triangle(Vec corner0, Vec corner1, Vec corner2) {
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
				minX = Math.min(this.corners[i].x, minX);
				maxX = Math.max(this.corners[i].x, maxX);
				minY = Math.min(this.corners[i].y, minY);
				maxY = Math.max(this.corners[i].y, maxY);
			}
			this.setPos(new Vec(minX + (maxX - minX) / 2f, minY + (maxY - minY) / 2f));
			this.setSize(new Vec(maxX - minX, maxY - minY));

			this.polygon = new Polygon(new Vec(), new Vec[] { corner1.add(corner0.scalar(-1)), corner2.add(corner0.scalar(-1)), new Vec() });
		}

		public void initToRender() {
			this.col = new RGBAColor((int) HeapLayer.calcWithVariance(240, 15), (int) HeapLayer.calcWithVariance(206, 10),
					(int) HeapLayer.calcWithVariance(140, 10));
			this.darkCol = this.col.scalar(0.9f);
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
		public void internalRender(GameGrid f) {
			this.polygon.moveTo(this.corners[0]);
			f.drawPolygon(this.polygon, this.col, true);
			f.drawPolygon(this.polygon, this.darkCol, false);
		}
	}

	private static class Edge {
		private Vec start;
		private Vec end;

		Edge(Vec start, Vec end) {
			this.start = start;
			this.end = end;
		}

		public Vec getRandomVecInBetween() {
			return this.start.add((this.end.add(this.start.scalar(-1))).scalar(0.4f + GameConf.PRNG.nextFloat() / 5f));
		}
	}

}
