package rekit.logic.gameelements.entities.enemies.bosses.rocketboss;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import rekit.core.GameGrid;
import rekit.logic.gameelements.GameElement;
import rekit.logic.gameelements.entities.enemies.bosses.rocketboss.arm.Arm;
import rekit.logic.gameelements.entities.enemies.bosses.rocketboss.damagestate.DamageState;
import rekit.logic.gameelements.entities.enemies.bosses.rocketboss.damagestate.State3;
import rekit.logic.gameelements.inanimate.Inanimate;
import rekit.logic.gameelements.type.Boss;
import rekit.logic.level.bossstructure.BossStructure;
import rekit.primitives.geometry.Vec;
import rekit.primitives.image.RGBColor;
import rekit.util.ReflectUtils.LoadMe;
import rekit.util.state.TimeStateMachine;

@LoadMe
public class RocketBoss extends Boss {

	private TimeStateMachine machine;

	private Vec startPos;

	private float calcX;

	private Mouth mouth;
	private List<Arm> arms;

	public static Vec MOVEMENT_PERIOD = new Vec(1.6f, 0.9f);
	public static Vec MOVEMENT_RANGE = new Vec(0.3f, 0.3f);

	public static Vec HEAD_SIZE = new Vec(2, 1.6f);
	public static Vec HEAD_PADDING = new Vec(0.2f, 0.2f);
	public static RGBColor HEAD_COL = new RGBColor(100, 100, 100);

	public static Vec EYE_SIZE = new Vec(0.4f, 0.4f);
	public static Vec EYE_LEFT_POS = RocketBoss.EYE_SIZE.scalar(0.5f).add(RocketBoss.HEAD_PADDING).sub(RocketBoss.HEAD_SIZE.scalar(0.5f));
	public static Vec EYE_RIGHT_POS = RocketBoss.EYE_LEFT_POS.scalar(-1, 1);

	public static Vec MOUTH_SIZE = new Vec(1.6f, 0.4f);
	public static Vec MOUTH_POS = (RocketBoss.MOUTH_SIZE.scalar(-0.5f).sub(RocketBoss.HEAD_PADDING).add(RocketBoss.HEAD_SIZE.scalar(0.5f))).setX(0);
	public static RGBColor MOUTH_BG_COL = new RGBColor(200, 200, 200);
	
	public static Vec[] ARM_POSITIONS = new Vec[]{new Vec(0.85f, 0.8f), new Vec(-0.85f, 0.8f)};
	public static float[][] ARM_SHAPE_SETTINGS = new float[][]{new float[]{0.3f, 0.2f, 2f, 0.3f}, new float[]{0.2f, 0.1f, 0.6f, 0.3f}};
	public static float[] ARM_ACTION_PROGRESS_THRESHOLDS = new float[]{0.1f, 0.8f};
	
	public static float ARM_SEGMENT_DIST = 0.1f;
	public static Vec ARM_SEGMENT_SIZE = new Vec(0.25f, 0.25f);
	
	public static long ARM_STATE_TIME_BUILD = 2000;
	public static long ARM_STATE_TIME_ACTION = 2000;
	public static long ARM_STATE_TIME_UNBUILD = 2000;

	public static RGBColor ARM_SEGMENT_COL = new RGBColor(100, 100, 100);

	/**
	 * Standard constructor
	 */
	public RocketBoss() {

	}

	public RocketBoss(Vec startPos) {
		super(startPos, new Vec(), RocketBoss.HEAD_SIZE);
		this.startPos = startPos;
		this.machine = new TimeStateMachine(new State3());
		this.mouth = new Mouth(this, RocketBoss.MOUTH_POS, RocketBoss.MOUTH_SIZE, RocketBoss.MOUTH_BG_COL);
		
		this.arms = new LinkedList<Arm>();
		for (int i = 0; i < RocketBoss.ARM_POSITIONS.length; ++i) {
			float[] shapeSettings = ARM_SHAPE_SETTINGS[i];
			this.arms.add(new Arm(this, RocketBoss.ARM_POSITIONS[i], shapeSettings, ARM_ACTION_PROGRESS_THRESHOLDS[i]));
		}
		
	}

	public DamageState getState() {
		return (DamageState) this.getMachine().getState();
	}

	@Override
	public void innerLogicLoop() {
		// add deltaTime with factor to local x
		float deltaX = this.deltaTime * this.getState().getTimeFactor();
		this.calcX += deltaX;

		// calculate and update position
		Vec scaleVec = new Vec( //
				(float) Math.sin(RocketBoss.MOVEMENT_PERIOD.getX() * this.calcX), //
				(float) Math.cos(RocketBoss.MOVEMENT_PERIOD.getY() * this.calcX));
		Vec scaledUnit = RocketBoss.MOVEMENT_RANGE.multiply(scaleVec);
		this.setPos(this.startPos.add(scaledUnit));

		this.mouth.logicLoop(this.calcX, deltaX);
		
		Iterator<Arm> it = this.arms.iterator();
		while (it.hasNext()) {
			it.next().logicLoop(deltaX, calcX);
		}
	}

	@Override
	public void internalRender(GameGrid f) {
		f.drawRectangle(this.getPos(), this.getSize(), RocketBoss.HEAD_COL);

		// Render eyes
		f.drawImage(this.getPos().add(RocketBoss.EYE_LEFT_POS), RocketBoss.EYE_SIZE, this.getState().getEyeImgSrc());
		f.drawImage(this.getPos().add(RocketBoss.EYE_RIGHT_POS), RocketBoss.EYE_SIZE, this.getState().getEyeImgSrc());

		// Render mouth
		this.mouth.internalRender(f);
		
		Iterator<Arm> it = this.arms.iterator();
		while (it.hasNext()) {
			it.next().internalRender(f);
		}
	}

	/**
	 * @return the machine
	 */
	public TimeStateMachine getMachine() {
		return this.machine;
	}

	@Override
	public BossStructure getBossStructure() {
		String i = Inanimate.class.getSimpleName();
		String n = null;
		String[][] struct = new String[][] { //
			{ i, i, i, i, i, i, i, i, i, i, i, i, i, i, i, i, i, i, i, i, i, i, i, i, i, i, i, i },
			{ i, n, n, n, n, n, i, n, n, n, n, n, n, n, n, n, n, n, n, n, n, n, n, n, n, n, n, n },
			{ i, n, n, n, n, n, i, n, n, n, n, n, n, n, n, n, n, n, n, n, n, n, n, n, n, n, n, n },
			{ i, n, n, n, n, n, i, n, n, n, n, n, n, n, n, n, n, n, n, n, n, n, n, n, n, n, n, n },
			{ i, n, n, n, n, n, i, n, n, n, n, n, n, n, n, n, n, n, n, n, n, n, n, n, n, n, n, n },
			{ i, n, n, n, n, i, n, n, n, n, n, n, n, n, n, n, n, n, n, n, n, n, n, n, n, n, n, n },
			{ i, i, i, i, i, n, n, n, n, n, n, n, n, n, n, n, n, n, n, n, n, n, n, n, n, n, n, n },
			{ n, n, n, n, n, n, n, n, n, n, n, n, n, n, n, n, n, n, n, n, n, n, n, n, n, n, n, n },
			{ i, i, i, i, i, i, i, i, i, i, i, i, i, i, i, i, i, i, i, i, i, i, i, i, i, i, i, i } //
		};

		BossStructure structure = new BossStructure(struct, this);
		this.setBossStructure(structure);
		return structure;
	}

	@Override
	public Vec getStartPos() {
		return new Vec(22, 3.5f);
	}

	@Override
	public String getName() {
		return "Unintentionally world-dominating Robot";
	}

	@Override
	public GameElement create(Vec startPos, String[] options) {
		return new RocketBoss(startPos);
	}
}
