package ragnarok.logic.gameelements.entities.enemies.bosses.rocketboss.arm;

import java.util.LinkedList;
import java.util.List;

import ragnarok.core.GameGrid;
import ragnarok.logic.gameelements.GameElement;
import ragnarok.logic.gameelements.entities.enemies.bosses.rocketboss.RocketBoss;
import ragnarok.logic.gameelements.entities.enemies.bosses.rocketboss.arm.armstate.ArmBuildState;
import ragnarok.logic.gameelements.entities.enemies.bosses.rocketboss.arm.armstate.ArmState;
import ragnarok.primitives.geometry.Vec;
import ragnarok.util.CalcUtil;
import ragnarok.util.state.TimeStateMachine;

public class Arm {

	private GameElement parent;

	private Vec relPos;

	public float curveA;
	public float maxLengthY;

	private TimeStateMachine machine;

	private List<ArmSegment> armSegments;

	public Arm(GameElement parent, Vec relPos) {
		this.parent = parent;
		this.relPos = relPos;
		this.machine = new TimeStateMachine(new ArmBuildState(this));
	}

	public void createArmSegments() {
		this.curveA = CalcUtil.randomize(0.6f, 0.5f);
		this.maxLengthY = CalcUtil.randomize(2.5f, 0.5f);
		this.armSegments = new LinkedList<>();
		for (float dy = 0; dy <= this.maxLengthY; dy += RocketBoss.ARM_SEGMENT_DIST) {
			// calculate angle
			float dxdy = fndy(dy);
			float angle = (float) Math.atan(dxdy);

			ArmSegment segment = new ArmSegment(this, fnVec(dy), angle);

			armSegments.add(segment);
		}
	}

	public Vec getPos() {
		return this.parent.getPos().add(this.relPos);
	}

	public ArmState getState() {
		return (ArmState) this.machine.getState();
	}

	public void logicLoop(float calcX, float deltaX) {
		if (this.armSegments == null) {
			createArmSegments();
		}

		this.getState().logicLoop();

		for (ArmSegment segment : this.armSegments) {
			segment.logicLoop(deltaX);
		}
	}

	public void internalRender(GameGrid f) {
		int c = 0;
		int l = (int) (this.getState().getSegmentAmount() * this.armSegments.size());

		for (ArmSegment segment : this.armSegments) {
			c++;
			if (c > l) {
				return;
			}
			segment.render(f);
		}
	}

	public TimeStateMachine getMachine() {
		return this.machine;
	}

	public Vec fnVec(float y) {
		return new Vec(fn(y), y);
	}

	public float fn(float y) {
		return -this.curveA * (float) Math.sin((y / maxLengthY) * (2 * Math.PI));
	}

	public float fndy(float y) {
		return -this.curveA * (float) Math.cos((y / maxLengthY) * (2 * Math.PI));
	}

}
