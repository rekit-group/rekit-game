package rekit.logic.gameelements.entities.enemies.bosses.rocketboss.arm;

import java.util.LinkedList;
import java.util.List;

import rekit.core.GameGrid;
import rekit.core.Team;
import rekit.logic.gameelements.GameElement;
import rekit.logic.gameelements.entities.enemies.bosses.rocketboss.RocketBoss;
import rekit.primitives.geometry.Polygon;
import rekit.primitives.geometry.Vec;

public class ArmSegment extends GameElement {

	private Arm parent;
	private Vec relPos;

	private Polygon shape;
	private List<Vec> pathLeft;
	private List<Vec> pathRight;

	protected ArmSegment(Arm parent, Vec relPos, float angle) {
		super(parent.getPos().add(relPos), new Vec(), RocketBoss.ARM_SEGMENT_SIZE, Team.ENEMY);
		this.parent = parent;
		this.relPos = relPos;

		// calculate half height and width
		float hw = this.getSize().getX() / 2;
		float hh = this.getSize().getY() / 2;

		// create Polygon around center
		this.shape = new Polygon(new Vec(), new Vec[] {});
		this.shape.addRelPt(new Vec(-hw, -hh));
		this.shape.addRelPt(new Vec(-hw, hh));
		this.shape.addRelPt(new Vec(hw, hh));
		this.shape.addRelPt(new Vec(hw, -hh));
		this.shape.addRelPt(new Vec(-hw, -hh));

		// rotate shape by given angle
		this.shape = this.shape.rotate(-angle, new Vec());
		float[] rotatedPts = this.shape.getAbsoluteArray();

		// create paths using rotated Pts
		this.pathLeft = new LinkedList<>();
		this.pathLeft.add(new Vec(rotatedPts[2], rotatedPts[3]));
		this.pathLeft.add(new Vec(rotatedPts[4], rotatedPts[5]));

		this.pathRight = new LinkedList<>();
		this.pathRight.add(new Vec(rotatedPts[6], rotatedPts[7]));
		this.pathRight.add(new Vec(rotatedPts[8], rotatedPts[9]));

	}

	public void setRelPos(Vec relPos) {
		this.relPos = relPos;
	}

	public void logicLoop(float deltaTime) {
		// Do nothing
		this.setPos(this.parent.getPos().add(this.relPos));
		this.shape.moveTo(this.getPos());
	}

	@Override
	public void internalRender(GameGrid f) {
		f.drawPolygon(this.shape, RocketBoss.ARM_SEGMENT_COL.toRGBA(), true);

		f.drawPath(this.getPos(), this.pathRight, RocketBoss.ARM_SEGMENT_BORDER_COL.toRGBA(), 2, true);
		f.drawPath(this.getPos(), this.pathLeft, RocketBoss.ARM_SEGMENT_BORDER_COL.toRGBA(), 2, true);
	}
}
