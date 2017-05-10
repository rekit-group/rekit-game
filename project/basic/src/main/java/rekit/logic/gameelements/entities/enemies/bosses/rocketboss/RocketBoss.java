package rekit.logic.gameelements.entities.enemies.bosses.rocketboss;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.fuchss.configuration.Configurable;
import org.fuchss.configuration.annotations.NoSet;
import org.fuchss.configuration.annotations.SetterInfo;

import net.jafama.FastMath;
import rekit.config.GameConf;
import rekit.core.GameGrid;
import rekit.logic.gameelements.GameElement;
import rekit.logic.gameelements.entities.Player;
import rekit.logic.gameelements.entities.enemies.bosses.rocketboss.arm.Arm;
import rekit.logic.gameelements.entities.enemies.bosses.rocketboss.damagestate.DamageState;
import rekit.logic.gameelements.entities.enemies.bosses.rocketboss.damagestate.State3;
import rekit.logic.gameelements.inanimate.Inanimate;
import rekit.logic.gameelements.particles.ParticleSpawner;
import rekit.logic.gameelements.type.Boss;
import rekit.logic.level.BossStructure;
import rekit.primitives.geometry.Direction;
import rekit.primitives.geometry.Vec;
import rekit.primitives.image.RGBAColor;
import rekit.primitives.operable.OpProgress;
import rekit.primitives.time.Timer;
import rekit.util.CalcUtil;
import rekit.util.ReflectUtils.LoadMe;
import rekit.util.state.TimeStateMachine;

@LoadMe
@SetterInfo(res = "conf/rocketBoss")
public class RocketBoss extends Boss implements Configurable {

	/**
	 * The particle spawner for the jets sparks
	 */
	private static ParticleSpawner jetSparkSpawner;

	/**
	 * The jet Particles's spawn time.
	 */
	private static long JET_SPARK_SPAWN_DELTA;

	/**
	 * The position where to spawn the particles of the right jet, relative to
	 * the middle point of the RocketBoss. Invert x to get the left point.
	 */
	private static Vec PARTICLE_SPAWN_POS;

	/**
	 * The duration fur a full movement cycle in x and y direction
	 */
	public static Vec MOVEMENT_PERIOD;

	/**
	 * The length the RocketBoss moves "randomly" on spot
	 */
	public static Vec MOVEMENT_RANGE;

	/**
	 * The size of the Brain that will also determine the hitbox for the only
	 * damage-sensitive place of the RocketBoss
	 */
	public static Vec BRAIN_SIZE;

	/**
	 * The size of the robots main corpus.
	 */
	public static Vec HEAD_SIZE;

	/**
	 * The size of the robots mouth, that will be used for starting and ending
	 * the mouth-function (x) as well as its amplitude (y).
	 */
	public static Vec MOUTH_SIZE;

	/**
	 * The mouths position relative to the RocketBosses position
	 */
	public static Vec MOUTH_POS;

	/**
	 * A list where to position the Arms relative to the RocketBoss.
	 */
	@NoSet
	public static Vec[] ARM_POSITIONS = new Vec[] { new Vec(0.85f, 0.8f), new Vec(-0.85f, 0.8f) };

	/**
	 * A list of settings concerning the arms shaping parameters. Each consists
	 * of:
	 * <ul>
	 * <li>Mu of the curves amplitude in x-direction</li>
	 * <li>Sigma of the curves amplitude in x-direction</li>
	 * <li>Mu of the curves length in y-direction</li>
	 * <li>Sigma of the curves length in y-direction</li>
	 * </ul>
	 */
	@NoSet
	public static float[][] ARM_SHAPE_SETTINGS = new float[][] { new float[] { 0.3f, 0.2f, 2f, 0.3f }, new float[] { 0.15f, 0.08f, 0.65f, 0.2f } };

	/**
	 * A list of thresholds in time, when to trigger an Arms Action during its
	 * ArmActionState. The thresholds are fractions of the ArmActionStates
	 * duration and should be smaller than or equal to 1
	 */
	@NoSet
	public static float[] ARM_ACTION_PROGRESS_THRESHOLDS = new float[] { 0.1f, 0.4f };

	/**
	 * The rocket launchers size
	 */
	public static Vec ROCKET_LAUNCHER_SIZE;

	/**
	 * The image source for the rocket launcher when facing left
	 */
	public static String ROCKET_LAUNCHER_SOURCE_LEFT;

	/**
	 * The image source for the rocket launcher when facing right
	 */
	public static String ROCKET_LAUNCHER_SOURCE_RIGHT;

	/**
	 * The size of one square/segment of the arm
	 */
	public static Vec ARM_SEGMENT_SIZE;

	/**
	 * The distance on x axis to render the ArmSegments and calculate the
	 * corresponding y. Warning: This is not the actual 2d-euclidian distance
	 * but only on the x axis!
	 */
	public static float ARM_SEGMENT_DIST;

	/**
	 * The Color to fill the ArmSegment with.
	 */
	public static RGBAColor ARM_SEGMENT_COL;

	/**
	 * The Color to give the ArmSegments border.
	 */
	public static RGBAColor ARM_SEGMENT_BORDER_COL;

	/**
	 * The ArmStates duration in ms for the IDLE phase, where the RocketBoss has
	 * no arms
	 */
	public static long ARM_STATE_TIME_IDLE;

	/**
	 * The ArmStates duration in ms for the BUILD phase, where the RocketBoss
	 * slowly grows Arms
	 */
	public static long ARM_STATE_TIME_BUILD;

	/**
	 * The ArmStates duration in ms for the ACTION phase, where the Arms perform
	 * their actions.
	 */
	public static long ARM_STATE_TIME_ACTION;

	/**
	 * The ArmStates duration in ms for the UNBUILD phase, where the RocketBoss
	 * rolls the arms back in.
	 */
	public static long ARM_STATE_TIME_UNBUILD;

	/**
	 * The source for the image of the jet.
	 */
	public static String JET_SOURCE;

	/**
	 * The size of the jet, used for rendering the image
	 */
	public static Vec JET_SIZE;

	/**
	 * Mu used for the "random shaking" of the jet.
	 */
	public static float JET_SHAKE_MU;

	/**
	 * Sigma used for the "random shaking" of the jet.
	 */
	public static float JET_SHAKE_SIGMA;

	/**
	 * A list of locations where the RocketBoss can move relative to the
	 * startPosition.
	 */
	@NoSet
	public static Vec[] POSITIONS = new Vec[] { new Vec(), new Vec(-8, 0), new Vec(3, 0), new Vec(-14, 0.3f), new Vec(3, 0.3f), new Vec(-3, -1.8f) };

	/**
	 * A list of Directions to face in correspondence to the list POSITIONS.
	 */
	@NoSet
	public static Direction[] DIRECTIONS = new Direction[] { Direction.LEFT, Direction.RIGHT, Direction.LEFT, Direction.RIGHT, Direction.LEFT, Direction.LEFT };

	/**
	 * The time it takes to switch from one position to another. Is later
	 * modified by the DamageStates timeFactor.
	 */
	private static long NEXT_POS_DURATION;

	@NoSet
	private TimeStateMachine machine;

	@NoSet
	private Vec startPos;

	@NoSet
	private float calcX;

	@NoSet
	private Mouth mouth;

	@NoSet
	private List<Arm> arms;

	@NoSet
	private Brain brain;

	@NoSet
	private static int LIVES = 3;

	@NoSet
	private Timer particleTimer;

	@NoSet
	private int positionId = 0;

	@NoSet
	private Timer nextPosTimer;

	@NoSet
	private OpProgress<Vec> nextPosProgress;

	@NoSet
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
		this.mouth = new Mouth(this, RocketBoss.MOUTH_POS, RocketBoss.MOUTH_SIZE);
		this.setLives(RocketBoss.LIVES);
		this.arms = new LinkedList<Arm>();

		this.moveToNextPosition(0);

		this.particleTimer = new Timer(RocketBoss.JET_SPARK_SPAWN_DELTA);

		for (int i = 0; i < RocketBoss.ARM_POSITIONS.length; ++i) {
			float[] shapeSettings = RocketBoss.ARM_SHAPE_SETTINGS[i];
			this.arms.add(new Arm(this, RocketBoss.ARM_POSITIONS[i], shapeSettings, RocketBoss.ARM_ACTION_PROGRESS_THRESHOLDS[i]));
		}

	}

	public Vec getCurrentBasePos() {
		return this.startPos.add(this.nextPosProgress.getNow(this.nextPosTimer.getProgress()));
	}

	public Direction getDirection() {
		return this.currentDirection;
	}

	public float getXSignum() {
		return (this.currentDirection == Direction.RIGHT) ? 1 : -1;
	}

	/**
	 * Moves to the next position of the RocketBoss. The current positions can
	 * be accessed via the OpProgress nextPosProgress and the Timer
	 * nextPosTimer.
	 *
	 * @param i
	 *            the id of the position to start moving to. Can be the old
	 *            position id too.
	 */
	private void moveToNextPosition(int i) {
		// if currently still moving: return
		if (this.nextPosTimer != null && !this.nextPosTimer.timeUp()) {
			return;
		}

		Vec oldPosition = RocketBoss.POSITIONS[this.positionId];
		Vec newPosition = RocketBoss.POSITIONS[i];

		this.currentDirection = RocketBoss.DIRECTIONS[i];

		this.nextPosProgress = new OpProgress<Vec>(oldPosition, newPosition);

		this.nextPosTimer = new Timer((long) (RocketBoss.NEXT_POS_DURATION / this.getState().getTimeFactor()));
		this.positionId = i;

		this.particleTimer = new Timer((long) (RocketBoss.JET_SPARK_SPAWN_DELTA / this.getState().getTimeFactor()));
	}

	public void moveToNextPosition() {
		this.moveToNextPosition(GameConf.PRNG.nextInt(RocketBoss.POSITIONS.length));
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

		// add deltaTime with factor to local x (1000 to get u/s)
		float deltaX = this.deltaTime * this.getState().getTimeFactor() / 1000;
		this.calcX += deltaX;

		// calculate and update position
		Vec scaleVec = new Vec(//
				(float) FastMath.sinQuick(RocketBoss.MOVEMENT_PERIOD.x * this.calcX), (float) FastMath.cosQuick(RocketBoss.MOVEMENT_PERIOD.y * this.calcX));
		Vec scaledUnit = RocketBoss.MOVEMENT_RANGE.multiply(scaleVec);

		this.setPos(this.getCurrentBasePos().add(scaledUnit));

		this.mouth.logicLoop(this.calcX, deltaX);

		// spawn particles
		this.particleTimer.logicLoop();
		// this.paricleTimer.removeTime(this.deltaTime);
		if (this.particleTimer.timeUp()) {
			this.particleTimer.reset();
			// RocketBoss.sparkParticles.spawn(this.getScene(),
			// this.getPos().addX(-this.getXSignum() * this.getSize().x /
			// 2));

			for (int i = -1; i <= 1; i += 2) {
				RocketBoss.jetSparkSpawner.spawn(this.getScene(), this.getPos().add(RocketBoss.PARTICLE_SPAWN_POS.scalar(i, 1)));
			}

		}

		Iterator<Arm> it = this.arms.iterator();
		while (it.hasNext()) {
			it.next().logicLoop(deltaX, this.calcX);
		}

		if (this.brain == null) {
			this.brain = new Brain(this, this.team);
			this.getScene().addGameElement(this.brain);
		}
	}

	@Override
	public void internalRender(GameGrid f) {

		// Render arms
		Iterator<Arm> it = this.arms.iterator();
		while (it.hasNext()) {
			it.next().internalRender(f);
		}

		// Render shaking jets
		float jetMu = this.getState().getTimeFactor() * RocketBoss.JET_SHAKE_MU;
		float jetSigma = this.getState().getTimeFactor() * RocketBoss.JET_SHAKE_SIGMA;
		float jetX = (GameConf.PRNG.nextBoolean() ? 1 : -1) * CalcUtil.randomize(jetMu, jetSigma);
		float jetY = (GameConf.PRNG.nextBoolean() ? 1 : -1) * CalcUtil.randomize(jetMu, jetSigma);
		Vec jetPos = this.getPos().add(new Vec(jetX, jetY));

		f.drawImage(jetPos, RocketBoss.JET_SIZE, RocketBoss.JET_SOURCE);

		// Render head background image
		Vec backgroundPos = this.getPos().add(RocketBoss.BRAIN_SIZE.scalar(-1 / 2f).setX(0));
		f.drawImage(backgroundPos, this.getSize().addY(RocketBoss.BRAIN_SIZE.y), this.getState().getHeadImgSrc());

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
			element.collidedWith(this.getCollisionFrame(), dir.getOpposite());
			element.setVel(element.getVel().add(Direction.DOWN.getVector().scalar(-Player.KILL_BOOST)));
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
	public RocketBoss create(Vec startPos, String... options) {
		return new RocketBoss(startPos);
	}
}
