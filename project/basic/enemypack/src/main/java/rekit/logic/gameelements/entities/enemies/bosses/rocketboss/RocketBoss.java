package rekit.logic.gameelements.entities.enemies.bosses.rocketboss;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import rekit.config.GameConf;
import rekit.core.GameGrid;
import rekit.logic.gameelements.GameElement;
import rekit.logic.gameelements.entities.enemies.bosses.rocketboss.arm.Arm;
import rekit.logic.gameelements.entities.enemies.bosses.rocketboss.damagestate.DamageState;
import rekit.logic.gameelements.entities.enemies.bosses.rocketboss.damagestate.State3;
import rekit.logic.gameelements.inanimate.Inanimate;
import rekit.logic.gameelements.type.Boss;
import rekit.primitives.geometry.Direction;
import rekit.logic.level.BossStructure;
import rekit.primitives.geometry.Vec;
import rekit.primitives.image.RGBColor;
import rekit.primitives.operable.OpProgress;
import rekit.primitives.time.Progress;
import rekit.primitives.time.Timer;
import rekit.util.ReflectUtils.LoadMe;
import rekit.util.state.TimeStateMachine;

@LoadMe
public class RocketBoss extends Boss {

	private TimeStateMachine machine;

	private Vec startPos;

	private float calcX;

	private Mouth mouth;
	private List<Arm> arms;
	
	private Brain brain;

	private static int LIVES = 3;
	
	public static Vec MOVEMENT_PERIOD = new Vec(1.6f, 0.9f);
	public static Vec MOVEMENT_RANGE = new Vec(0.3f, 0.3f);
	
	public static Vec BRAIN_SIZE = new Vec(1.4f, 0.6f);
	
	public static Vec HEAD_SIZE = new Vec(2, 1.6f);
	public static Vec HEAD_PADDING = new Vec(0.2f, 0.2f);

	public static Vec EYE_SIZE = new Vec(0.4f, 0.4f);
	public static Vec EYE_LEFT_POS = RocketBoss.EYE_SIZE.scalar(0.5f).add(RocketBoss.HEAD_PADDING).sub(RocketBoss.HEAD_SIZE.scalar(0.5f));
	public static Vec EYE_RIGHT_POS = RocketBoss.EYE_LEFT_POS.scalar(-1, 1);

	public static Vec MOUTH_SIZE = new Vec(1.6f, 0.4f);
	public static Vec MOUTH_POS = (RocketBoss.MOUTH_SIZE.scalar(-0.5f).sub(RocketBoss.HEAD_PADDING).add(RocketBoss.HEAD_SIZE.scalar(0.5f))).setX(0);
	public static RGBColor MOUTH_BG_COL = new RGBColor(183, 183, 183);
	
	public static Vec[] ARM_POSITIONS = new Vec[]{new Vec(0.85f, 0.8f), new Vec(-0.85f, 0.8f)};
	public static float[][] ARM_SHAPE_SETTINGS = new float[][]{new float[]{0.3f, 0.2f, 2f, 0.3f}, new float[]{0.2f, 0.1f, 0.6f, 0.3f}};
	public static float[] ARM_ACTION_PROGRESS_THRESHOLDS = new float[]{0.1f, 0.4f};
	
	public static Vec ARM_ACTION_ROCKET_LAUNCHER_SIZE = new Vec(0.8f, 0.4f);
	public static RGBColor ARM_ACTION_ROCKET_LAUNCHER_COLOR = new RGBColor(160, 160, 160);
	
	public static float ARM_SEGMENT_DIST = 0.1f;
	public static Vec ARM_SEGMENT_SIZE = new Vec(0.25f, 0.25f);
	
	public static long ARM_STATE_TIME_IDLE = 2000;
	public static long ARM_STATE_TIME_BUILD = 2000;
	public static long ARM_STATE_TIME_ACTION = 4000;
	public static long ARM_STATE_TIME_UNBUILD = 2000;

	public static RGBColor ARM_SEGMENT_COL = new RGBColor(160, 160, 160);
	public static RGBColor ARM_SEGMENT_BORDER_COL = new RGBColor(77, 7, 7);
	
	public static Vec[] POSITIONS = new Vec[]{new Vec(), new Vec(-8, 0), new Vec(3, 0), new Vec(-14, 0.6f), new Vec(3, 0.6f), new Vec(-3, -1.8f)};
	public static Direction[] DIRECTIONS = new Direction[]{Direction.LEFT, Direction.RIGHT, Direction.LEFT, Direction.RIGHT, Direction.LEFT, Direction.LEFT};
	private static long NEXT_POS_DURATION = 2;
	
	private int positionId = 0;
	
	private Timer nextPosTimer;
	
	private OpProgress<Vec> nextPosProgress;

	private Direction currentDirection;
	
	
	
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
		this.setLives(RocketBoss.LIVES);
		this.arms = new LinkedList<Arm>();
		
		this.moveToNextPosition(0);
		
		for (int i = 0; i < RocketBoss.ARM_POSITIONS.length; ++i) {
			float[] shapeSettings = ARM_SHAPE_SETTINGS[i];
			this.arms.add(new Arm(this, RocketBoss.ARM_POSITIONS[i], shapeSettings, ARM_ACTION_PROGRESS_THRESHOLDS[i]));
		}
		
	}
	
	public Vec getCurrentBasePos() {
		return startPos.add(this.nextPosProgress.getNow(this.nextPosTimer.getProgress()));
	}
	
	public Direction getDirection() {	
		return this.currentDirection;
	}
	public float getXSignum() {	
		return (this.currentDirection == Direction.RIGHT) ? 1 : -1;
	}
	
	/**
	 * Moves to the next position of the RocketBoss.
	 * The current positions can be accessed via the OpProgress nextPosProgress and
	 * the Timer nextPosTimer. 
	 * @param i the id of the position to start moving to. Can be the old position id too.
	 */
	private void moveToNextPosition(int i) {
		// if currently still moving: return
		if (this.nextPosTimer != null && !this.nextPosTimer.timeUp()) {
			return;
		}
		Vec oldPosition = RocketBoss.POSITIONS[this.positionId];
		Vec newPosition = RocketBoss.POSITIONS[i];
		this.currentDirection = RocketBoss.DIRECTIONS[i];
		
		System.out.println(this.positionId + " / " + oldPosition + " -> " + i + " / " + newPosition);
		this.nextPosProgress = new OpProgress<Vec>(oldPosition, newPosition);
		
		this.nextPosTimer = new Timer((long) (RocketBoss.NEXT_POS_DURATION / this.getState().getTimeFactor()));
		this.positionId = i;
	}
	
	public void moveToNextPosition() {
		moveToNextPosition(GameConf.PRNG.nextInt(RocketBoss.POSITIONS.length));
	}

	public DamageState getState() {
		return (DamageState) this.getMachine().getState();
	}
	

	@Override
	public void innerLogicLoop() {
		
		super.innerLogicLoop();
		
		if (this.nextPosTimer != null) {
			this.nextPosTimer.logicLoop();
		}
				
		// add deltaTime with factor to local x
		float deltaX = this.deltaTime * this.getState().getTimeFactor();
		this.calcX += deltaX;

		// calculate and update position
		Vec scaleVec = new Vec( //
				(float) Math.sin(RocketBoss.MOVEMENT_PERIOD.getX() * this.calcX), //
				(float) Math.cos(RocketBoss.MOVEMENT_PERIOD.getY() * this.calcX));
		Vec scaledUnit = RocketBoss.MOVEMENT_RANGE.multiply(scaleVec);
		
		this.setPos(this.getCurrentBasePos().add(scaledUnit));

		this.mouth.logicLoop(this.calcX, deltaX);
		
		Iterator<Arm> it = this.arms.iterator();
		while (it.hasNext()) {
			it.next().logicLoop(deltaX, calcX);
		}
		
		if (this.brain == null) {
			this.brain = new Brain(this, team);
			this.getScene().addGameElement(brain);
		}
	}

	@Override
	public void internalRender(GameGrid f) {
		
		// Render arms
		Iterator<Arm> it = this.arms.iterator();
		while (it.hasNext()) {
			it.next().internalRender(f);
		}	
		
		// Render head background image
		Vec backgroundPos = this.getPos().add(RocketBoss.BRAIN_SIZE.scalar(-1/2f).setX(0));
		f.drawImage(backgroundPos, this.getSize().addY(RocketBoss.BRAIN_SIZE.getY()), this.getState().getHeadImgSrc());
		
		// Render mouth
		this.mouth.internalRender(f);
	}
	
	@Override
	public void addDamage(int damage) {
		// If Boss is not already taking damage
		if (!this.isHarmless) {
			this.getMachine().nextState();
		}
		super.addDamage(damage);
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
	public void reactToCollision(GameElement element, Direction dir) {
		if (this.isHarmless) {
			return;
		}
		if (this.getTeam().isHostile(element.getTeam())) {
			element.addDamage(1);
			element.collidedWith(this.getCollisionFrame(), dir);
		}
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
