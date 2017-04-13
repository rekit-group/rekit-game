package rekit.logic.gameelements.entities.enemies.bosses.rocketboss.arm;

import java.util.LinkedList;

import rekit.core.GameGrid;
import rekit.logic.gameelements.entities.enemies.bosses.rocketboss.RocketBoss;
import rekit.logic.gameelements.entities.enemies.bosses.rocketboss.arm.armaction.ArmAction;
import rekit.logic.gameelements.entities.enemies.bosses.rocketboss.arm.armstate.ArmIdleState;
import rekit.logic.gameelements.entities.enemies.bosses.rocketboss.arm.armstate.ArmState;
import rekit.primitives.geometry.Vec;
import rekit.util.CalcUtil;
import rekit.util.Math;
import rekit.util.state.TimeStateMachine;

public class Arm extends RocketBossChild {

	private float curveAMu;
	private float curveASigma;
	private float maxLengthYMu;
	private float maxLengthYSigma;

	private float actionProgressThreshold;

	private float curveA;
	private float maxLengthY;

	private TimeStateMachine machine;

	private LinkedList<ArmSegment> armSegments;

	public ArmAction armAction;

	public Arm(RocketBoss parent, Vec relPos, float[] shapeSettings, float actionProgressThreshold) {
		super(parent, relPos);

		// TODO passing float arrays is not exactly best practice
		this.curveAMu = shapeSettings[0];
		this.curveASigma = shapeSettings[1];
		this.maxLengthYMu = shapeSettings[2];
		this.maxLengthYSigma = shapeSettings[3];

		this.actionProgressThreshold = actionProgressThreshold;

		this.machine = new TimeStateMachine(new ArmIdleState(this));
	}

	public void nextArmAction() {
		this.armAction = (ArmAction) ArmAction.getRandomArmAction().create(this.parent, this.getHandPos().sub(this.parent.getPos()));
	}

	/**
	 * Getter for when to trigger the arms action during the ArmActionState
	 *
	 * @return the number between 0 and 1, representing when to perform an
	 *         action.
	 */
	public float getActionProgressThreshold() {
		return this.actionProgressThreshold;
	}

	public void createArmSegments() {
		this.curveA = CalcUtil.randomize(this.curveAMu, this.curveASigma);
		this.maxLengthY = CalcUtil.randomize(this.maxLengthYMu, this.maxLengthYSigma);
		this.armSegments = new LinkedList<>();
		for (float dy = 0; dy <= this.maxLengthY; dy += RocketBoss.ARM_SEGMENT_DIST) {
			// calculate angle
			float dxdy = this.fndy(dy);
			float angle = (float) (2.0 * Math.atan(dxdy));

			ArmSegment segment = new ArmSegment(this, this.fnVec(dy), angle);

			this.armSegments.add(segment);
		}
	}

	public ArmState getState() {
		return (ArmState) this.machine.getState();
	}

	@Override
	public void logicLoop(float calcX, float deltaX) {
		if (this.armSegments == null) {
			this.createArmSegments();
		}

		this.getState().logicLoop();

		if (this.armAction != null) {
			this.armAction.logicLoop(calcX, deltaX);
		}

		for (ArmSegment segment : this.armSegments) {
			segment.logicLoop(deltaX);
		}
	}

	@Override
	public void internalRender(GameGrid f) {

		// Render all segments of the arm
		if (this.armSegments != null) {
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

		if (this.armAction != null) {
			// Render Hand/ArmAction if there is any
			this.armAction.internalRender(f);
		}
	}

	public TimeStateMachine getMachine() {
		return this.machine;
	}

	public Vec fnVec(float y) {
		return new Vec(this.fn(y), y);
	}

	private float fn(float y) {
		return this.parent.getXSignum() * this.curveA * (float) Math.sin(this.getParent().getState().getTimeFactor() * (y / this.maxLengthY) * (2 * Math.PI));
	}

	private float fndy(float y) {
		return this.parent.getXSignum() * this.curveA * (float) Math.cos(this.getParent().getState().getTimeFactor() * (y / this.maxLengthY) * (2 * Math.PI));
	}

	private Vec getHandPos() {
		return this.armSegments.getLast().getPos();
	}

	@Override
	public RocketBossChild create(RocketBoss parent, Vec relPos) {
		return new Arm(parent, relPos, new float[] { 0, 0, 0, 0 }, 0);
	}

}
