package rekit.logic.gameelements.entities.enemies.piston;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.fuchss.configuration.Configurable;
import org.fuchss.configuration.annotations.NoSet;
import org.fuchss.configuration.annotations.SetterInfo;

import rekit.config.GameConf;
import rekit.core.GameGrid;
import rekit.logic.gameelements.GameElement;
import rekit.logic.gameelements.entities.enemies.piston.state.OpenState;
import rekit.logic.gameelements.type.Enemy;
import rekit.primitives.geometry.Direction;
import rekit.primitives.geometry.Vec;
import rekit.primitives.image.RGBAColor;
import rekit.primitives.time.Progress;
import rekit.util.ReflectUtils.LoadMe;
import rekit.util.state.TimeStateMachine;

/**
 *
 * This enemy is a piston that periodically smashes towards direction. Its
 * extension length, open and closed times, movement speed and phase offset can
 * be configured.
 *
 */
@LoadMe
@SetterInfo(res = "conf/piston")
public final class Piston extends Enemy implements Configurable, IPistonForState {

	/**
	 * The height of the non-moving base of the piston.
	 */
	protected static float BASE_HEIGHT;

	/**
	 * The short distance between piston tip and the actual defined
	 * {@link Piston#expansionLength}.
	 */
	protected static float LOWER_MARGIN;

	/**
	 * The amount of segments to draw for the Pistons base in colors
	 * {@link Piston#BASE_COLOR_1} and {@link Piston#BASE_COLOR_2}
	 */
	private static int BASE_SEGMENTS;

	/**
	 * The first color of the segments of the non-moving base of the piston.
	 */
	private static RGBAColor BASE_COLOR_1;

	/**
	 * The second color of the segments of the non-moving base of the piston.
	 */
	private static RGBAColor BASE_COLOR_2;

	/**
	 * The first, outer color of the moving part of the piston.
	 */
	protected static RGBAColor PISTON_COLOR_1;

	/**
	 * The second, inner color of the moving part of the piston.
	 */
	protected static RGBAColor PISTON_COLOR_2;

	/**
	 * The width in units of the pistons inner graphic elements.
	 */
	@NoSet
	protected static float[] PISTON_CIRCLE_WIDTHS = new float[] { 0.62f, 0.54f, 0.46f, 0.38f, 0.3f, 0.22f, 0.14f, 0.06f };

	/**
	 * The minimum and maximum time the piston stays still in open state in
	 * milliseconds. See how the actual time can be defined in the parameters of
	 * the
	 * {@link Piston#Piston(Vec, int, Direction, float, float, float, float)}.
	 */
	private static Progress OPEN_TIME;

	/**
	 * The minimum and maximum time the piston stays still in closed state in
	 * milliseconds. See how the actual time can be defined in the parameters of
	 * the
	 * {@link Piston#Piston(Vec, int, Direction, float, float, float, float)}.
	 */
	private static Progress CLOSED_TIME;

	/**
	 * The minimum and maximum time the piston stays still in closing and opening state
	 * in milliseconds. See how the actual time can be defined in the parameters of the
	 * {@link Piston#Piston(Vec, int, Direction, float, float, float, float)}.
	 */
	private static Progress TRANSITION_TIME;

	/**
	 * The minimum and maximum shaking while opening or closing the
	 * {@link Piston#inner InnerPiston}
	 */
	protected static Progress SHAKING;

	/**
	 * The reference to the inner, moving part of the piston.
	 */
	@NoSet
	private PistonInner inner;

	/**
	 * The length in units, the piston expands. The actual size varies by
	 * optical means such as BASE_HEIGHT and LOWER_MARGIN.
	 */
	@NoSet
	protected int expansionLength;

	/**
	 * The direction that piston is directed to.
	 */
	@NoSet
	protected Direction direction;

	/**
	 * The id of the phase to start with.
	 */
	@NoSet
	private int startPhaseId;

	/**
	 * The internal StateMachine that handles everything time related.
	 */
	@NoSet
	protected TimeStateMachine machine;

	@NoSet
	private long calcTimeOpen;

	@NoSet
	private long calcTimeClosed;

	@NoSet
	private long calcTimeTransition;

	/**
	 * Prototype Constructor.
	 */
	public Piston() {
		super();
	}

	public Piston(Vec startPos, int expansionLength, Direction direction, float timeOpen, float timeClosed, float timeTransition, float startPhaseId) {
		super(startPos, new Vec(), new Vec());

		// save trivial parameters
		this.direction = direction;
		this.expansionLength = expansionLength;

		// calculate base position (determined by Direction and BASE_HEIGHT)
		Vec basePos = new Vec(0, 0.5f - Piston.BASE_HEIGHT / 2f); // case
		// upwards
		basePos = this.rotatePosToDir(basePos);
		this.setPos(startPos.add(basePos));

		// set size (determined by Direction and BASE_HEIGHT)
		Vec size = new Vec(1, Piston.BASE_HEIGHT);
		this.setSize(this.rotateSizeToDir(size));

		// calculate all durations
		this.calcTimeOpen = (long) Piston.OPEN_TIME.getNow(timeOpen);
		this.calcTimeClosed = (long) Piston.CLOSED_TIME.getNow(timeClosed);
		this.calcTimeTransition = (long) Piston.TRANSITION_TIME.getNow(timeTransition);

		// Create TimeStateMachine for opening/closing behavior.
		this.machine = new TimeStateMachine(new OpenState(this));

		// go to the right start phase
		for (int i = 0; i < startPhaseId % 4; i++) {
			this.machine.nextState();
		}
	}

	/**
	 * Rotate a give relative position to the current {@link Piston#direction}.
	 * Add {@link GameElement#getPos} for the concrete position.
	 *
	 * @param relPos
	 *            the relative position to rotate
	 * @return the rotated relative position
	 */
	public Vec rotatePosToDir(Vec relPos) {
		return relPos.rotate(this.direction.getAngle());
	}

	/**
	 * Rotate a give size vector to the current {@link Piston#direction}.
	 *
	 * @param size
	 *            the size vector to rotate
	 * @return the rotated size vector
	 */
	public Vec rotateSizeToDir(Vec size) {
		return (this.direction == Direction.LEFT || this.direction == Direction.RIGHT) ? size.setX(size.y).setY(size.x) : size;
	}

	@Override
	public void internalRender(GameGrid f) {

		// paint background in color 1
		f.drawRectangle(this.getPos(), this.getSize(), Piston.BASE_COLOR_1);

		// paint half of the segments in color 2
		float segmentWidth = 1f / Piston.BASE_SEGMENTS;

		float relX = segmentWidth / 2f - this.rotateSizeToDir(this.getSize()).x / 2f;
		for (int x = 1; x < Piston.BASE_SEGMENTS; x += 2) {
			Vec relPos = new Vec(relX + x * segmentWidth, 0);
			Vec relSize = new Vec(segmentWidth, 1);
			f.drawRectangle(this.getPos().add(this.rotatePosToDir(relPos)), this.getSize().multiply(this.rotateSizeToDir(relSize)), Piston.BASE_COLOR_2);
		}
	}

	@Override
	protected void innerLogicLoop() {
		if (this.inner == null && this.getScene() != null) {
			this.inner = new PistonInner(this);
			this.getScene().addGameElement(this.inner);
		}

		// Let the machine work...
		this.machine.logicLoop();

	}

	@Override
	public void reactToCollision(GameElement element, Direction dir) {
		// Do nth.
	}

	@Override
	public Piston create(Vec startPos, String... options) {
		// Create a list and iterator of all given options
		List<Float> params = new LinkedList<Float>();
		for (String option : options) {
			if (option != null) {
				if (option.matches("(\\+|-)?[0-9].[0-9]F+")) {
					params.add(Float.parseFloat(option));
				} else {
					GameConf.GAME_LOGGER.error("Could not parse parameter of Piston to float: \"" + option + "\", must be in format: [-]0.0F");
				}
			}
		}
		Iterator<Float> it = params.iterator();

		// Now iterate through params or start taking default values if not
		// specified.
		int expansionLength = (it.hasNext()) ? it.next().intValue() : 1;
		Direction direction = ((it.hasNext()) ? Direction.values()[it.next().intValue()] : Direction.DOWN);
		float timeOpen = (it.hasNext()) ? it.next() : 0.5f;
		float timeClosed = (it.hasNext()) ? it.next() : 0.5f;
		float movementSpeed = (it.hasNext()) ? it.next() : 0.5f;
		int startPhaseId = (it.hasNext()) ? it.next().intValue() : 0;

		// return fully populated instance of Piston
		return new Piston(startPos, expansionLength, direction, timeOpen, timeClosed, movementSpeed, startPhaseId);
	}

	@Override
	public long getCalcTimeOpen() {
		return this.calcTimeOpen;
	}

	@Override
	public long getCalcTimeClosed() {
		return this.calcTimeClosed;
	}

	@Override
	public long getCalcTimeTransistion() {
		return this.calcTimeTransition;
	}

	@Override
	public Integer getZHint() {
		return (int) this.team.zRange.normalize(this.team.zRange.min + 1);
	}

	@Override
	public boolean isAddableToGroup() {
		return false;
	}

}
