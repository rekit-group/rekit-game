package rekit.logic.gameelements.entities.enemies.bosses.rocketboss.arm;

import java.util.LinkedList;

import rekit.core.GameGrid;
import rekit.logic.gameelements.GameElement;
import rekit.logic.gameelements.entities.enemies.bosses.rocketboss.RocketBoss;
import rekit.logic.gameelements.entities.enemies.bosses.rocketboss.arm.armaction.ArmAction;
import rekit.logic.gameelements.entities.enemies.bosses.rocketboss.arm.armstate.ArmIdleState;
import rekit.logic.gameelements.entities.enemies.bosses.rocketboss.arm.armstate.ArmState;
import rekit.primitives.geometry.Direction;
import rekit.primitives.geometry.Vec;
import rekit.util.CalcUtil;
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
		curveAMu = shapeSettings[0];
		curveASigma = shapeSettings[1];
		maxLengthYMu = shapeSettings[2];
		maxLengthYSigma = shapeSettings[3];
		
		this.actionProgressThreshold = actionProgressThreshold;
		
		this.machine = new TimeStateMachine(new ArmIdleState(this));
	}
	
	public void nextArmAction() {
		this.armAction = (ArmAction) ArmAction.getRandomArmAction().create(this.parent, this.getHandPos().sub(this.parent.getPos()));
	}
	
	/**
	 * Getter for when to trigger the arms action during the ArmActionState
	 * @return the number between 0 and 1, representing when to perform an action.
	 */
	public float getActionProgressThreshold() {
		return this.actionProgressThreshold;
	}

	public void createArmSegments() {
		this.curveA = CalcUtil.randomize(curveAMu, curveASigma);
		this.maxLengthY = CalcUtil.randomize(maxLengthYMu, maxLengthYSigma);
		this.armSegments = new LinkedList<>();
		for (float dy = 0; dy <= this.maxLengthY; dy += RocketBoss.ARM_SEGMENT_DIST) {
			// calculate angle
			float dxdy = fndy(dy);
			float angle = (float) (2.0 * Math.atan(dxdy));

			ArmSegment segment = new ArmSegment(this, fnVec(dy), angle);

			armSegments.add(segment);
		}
	}


	public ArmState getState() {
		return (ArmState) this.machine.getState();
	}

	public void logicLoop(float calcX, float deltaX) {
		if (this.armSegments == null) {
			createArmSegments();
		}

		this.getState().logicLoop();
		
		if (this.armAction != null) {
			this.armAction.logicLoop(calcX, deltaX);
		}

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
		
		armAction.internalRender(f);
	}

	public TimeStateMachine getMachine() {
		return this.machine;
	}

	public Vec fnVec(float y) {
		return new Vec(fn(y), y);
	}
	
	private float fn(float y) {
		return this.parent.getXSignum() * this.curveA  * (float) Math.sin(this.getParent().getState().getTimeFactor() * (y / maxLengthY) * (2 * Math.PI));
	}

	private float fndy(float y) {
		return this.parent.getXSignum() * this.curveA * (float) Math.cos(this.getParent().getState().getTimeFactor() * (y / maxLengthY) * (2 * Math.PI));
	}

	private Vec getHandPos() {
		return this.armSegments.getLast().getPos();
	}

	@Override
	public RocketBossChild create(RocketBoss parent, Vec relPos) {
		return new Arm(parent, relPos, new float[]{0,0,0,0}, 0);
	}

}
