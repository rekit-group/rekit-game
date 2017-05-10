package rekit.logic.gameelements.inanimate;

import org.fuchss.configuration.Configurable;
import org.fuchss.configuration.annotations.NoSet;
import org.fuchss.configuration.annotations.SetterInfo;

import rekit.config.GameConf;
import rekit.core.GameGrid;
import rekit.core.GameTime;
import rekit.logic.gameelements.GameElement;
import rekit.logic.gameelements.entities.Player;
import rekit.logic.gameelements.entities.state.FallState;
import rekit.logic.gameelements.type.DynamicInanimate;
import rekit.primitives.geometry.Direction;
import rekit.primitives.geometry.Polygon;
import rekit.primitives.geometry.Vec;
import rekit.primitives.image.RGBAColor;
import rekit.primitives.time.Timer;
import rekit.util.ReflectUtils.LoadMe;

@LoadMe
@SetterInfo(res = "conf/acceleratorbox")
public final class AcceleratorBox extends DynamicInanimate implements Configurable {

	/**
	 * The size of the actual block in units
	 */
	private static Vec SIZE;

	/**
	 * The primary color of the main part of the block
	 */
	private static RGBAColor COL_1;

	/**
	 * The secondary, accent color of the block
	 */
	private static RGBAColor COL_2;
	
	/**
	 * The secondary, accent color of the block while being active
	 */
	private static RGBAColor COL_2_ACTIVE;

	/**
	 * The color that will be used to visualize the boundary angle for the
	 * selection while {@link AcceleratorBox#playerCaught} is true.
	 */
	private static RGBAColor ANGLE_BOUND_COLOR;

	/**
	 * The color that will be used to visualize the current angle of the
	 * selection while {@link AcceleratorBox#playerCaught} is true.
	 */
	private static RGBAColor ANGLE_CURRENT_COLOR;

	/**
	 * The line width in pixels that will be used to visualize angles while
	 * {@link AcceleratorBox#playerCaught} is true.
	 */
	private static int ANGLE_LINE_WIDTH;

	/**
	 * The line length in units that will be used to visualize angles while
	 * {@link AcceleratorBox#playerCaught} is true.
	 */
	private static int ANGLE_LINE_LENGTH;

	/**
	 * The factor of angle range where
	 * <ul>
	 * <li>0 is angle 0</li>
	 * <li>1 is angle 45</li>
	 * <li>2 is angle 90</li>
	 */
	private static float ANGLE_RANGE_FACTOR;

	/**
	 * The boost in units that the player will be given.
	 */
	private static float BOOST;

	/**
	 * The width of the blocks border in units.
	 */
	private static float BORDER_WIDTH;

	/**
	 * The inner radius of the visual arrow on the block.
	 */
	private static float INNER_RADIUS;

	/**
	 * The Polygon of the inner arrow of the block
	 */
	@NoSet
	private Polygon innerPolygon = new Polygon(new Vec(),
			new Vec[] { new Vec(-AcceleratorBox.INNER_RADIUS, AcceleratorBox.INNER_RADIUS), //
					new Vec(0, -AcceleratorBox.INNER_RADIUS), //
					new Vec(AcceleratorBox.INNER_RADIUS, AcceleratorBox.INNER_RADIUS), //
					new Vec(0, 0), //
	});

	/**
	 * The direction this block is facing.
	 */
	@NoSet
	private Direction direction;

	/**
	 * Indicates whether the player is currently in aiming mode of not. This
	 * changes most behavior of the block and activates the aiming sequence.
	 */
	@NoSet
	private boolean playerCaught = false;

	/**
	 * The left-most boundary angle to aim at while
	 * {@link AcceleratorBox#playerCaught} is true.
	 */
	@NoSet
	private double angleLeft;

	/**
	 * The right-most boundary angle to aim at while
	 * {@link AcceleratorBox#playerCaught} is true.
	 */
	@NoSet
	private double angleRight;

	/**
	 * The angle that is currently aimed on while
	 * {@link AcceleratorBox#playerCaught} is true.
	 */
	@NoSet
	private double angleCurrent;

	/**
	 * The position in units where to hold the player at, relative to the blocks
	 * center and for case {@link Direction#UP}.
	 */
	@NoSet
	private Vec catchPos;

	/**
	 * The time in ms for the warmUp of the aiming.
	 */
	private static long WARM_UP_TIME;

	/**
	 * The last time when {@link #logicLoop(float)} was invoked.
	 */
	@NoSet
	private long lastTime = GameTime.getTime();

	/**
	 * The timer for the warmUp of the aiming.
	 */
	@NoSet
	private Timer warmUp;
	
	/**
	 * Is multiplied to every direction-related control.
	 * Should be -1 or 1 to rotate the controls.
	 */
	@NoSet
	private final int invertControls;

	/**
	 * Prototype Constructor.
	 */
	public AcceleratorBox() {
		super();
		this.invertControls = 1;
	}

	/**
	 * Actual Constructor that takes a start position and a direction to face.
	 * This Direction will determine where to accelerate the player to.
	 *
	 * @param startPos
	 *            the start position
	 * @param dir
	 *            the direction
	 */
	protected AcceleratorBox(Vec startPos, Direction dir) {
		super(startPos, AcceleratorBox.SIZE, null);
		this.direction = dir;
		
		this.innerPolygon = innerPolygon.rotate((float) this.direction.getAngle());
		
		// Make controls more natural for case DOWN
		this.invertControls = (dir == Direction.DOWN) ? -1 : 1;
	}

	@Override
	public void reactToCollision(GameElement element, Direction dir) {

		if (this.direction.getOpposite() != dir) {
			this.catchPlayer(dir);
		}

		super.reactToCollision(element, dir);
	}

	/**
	 * Adds an angle to the {@link AcceleratorBox#angleCurrent} keeping track of
	 * overflow between 2*pi and 0.
	 *
	 * @param add
	 *            the angle to add in radians
	 */
	public void addToCurrentAngle(double add) {
		this.angleCurrent = this.normAngle(this.angleCurrent);
		this.angleLeft = this.normAngle(this.angleLeft);
		this.angleRight = this.normAngle(this.angleRight);

		// update angle
		this.angleCurrent += add;

		double diff = this.angleLeft - this.angleCurrent;
		if (add > 0 && diff < 0 && diff > -Math.PI) {
			this.angleCurrent = this.angleLeft;
		}
		diff = this.angleRight - this.angleCurrent;
		if (add < 0 && diff > 0 && diff < Math.PI) {
			this.angleCurrent = this.angleRight;
		}
	}

	private double normAngle(double angle) {
		double pi = Math.PI;
		return ((angle + 3 * pi) % (2 * pi)) - pi;
	}

	/**
	 * Method that activates the aiming mode by setting
	 * {@link AcceleratorBox#playerCaught} to true. Afterwards, the
	 * {@link Player} will be hold still and the velocity of the Player is used
	 * to control the angle of current aim. A {@link Direction} can be specified
	 * to determine on which side (relative to the block) to hold the
	 * {@link Player}.
	 *
	 * @param dir
	 *            the {@link Direction} relative to the block where to hold the
	 *            player
	 */
	private void catchPlayer(Direction dir) {
		if (this.playerCaught) {
			return;
		}
		
		// Set player velocity to 0 to prevent false commands
		this.getScene().getPlayer().setVel(new Vec());
		
		// Prevent application of gravity 
		this.getScene().getPlayer().getEntityState().floorCollision();
		
		// Initialize warmUp timer
		this.warmUp = new Timer(AcceleratorBox.WARM_UP_TIME);

		this.playerCaught = true;

		double pi = Math.PI;

		// NOTE: Everything is for case UP and rotated in the end
		// assuming UP is 0 degrees and angles go anti-clockwise
		// Calc bounds of possible angles
		this.angleLeft = (pi / 4f) * AcceleratorBox.ANGLE_RANGE_FACTOR;
		this.angleRight = (2 * pi) - (pi / 4f) * AcceleratorBox.ANGLE_RANGE_FACTOR;
		
		// If hit from right, set left angle to 0
		if (this.direction.getNextClockwise() == dir) {
			this.angleLeft = 0;
		} else {
			// If hit from left, set right angle to 0
			if (this.direction.getNextAntiClockwise() == dir) {
				this.angleRight = 0;
			}
		}
		
		this.angleLeft = this.angleLeft - this.direction.getAngle();
		this.angleRight = this.angleRight - this.direction.getAngle();
		
		// Calc middle between the bounds
		this.angleCurrent = ((this.angleLeft + this.angleRight) % 2 * Math.PI) / 2f;
		double delta = ((this.angleLeft - this.angleRight + 3 * pi) % (2 * pi)) - pi;
		this.angleCurrent = (2 * pi + this.angleRight + (delta / 2)) % (2 * pi);

		this.catchPos = this.getPos().add(dir.getVector());
	}

	@Override
	public void logicLoop() {
		if (this.warmUp != null) {
			this.warmUp.logicLoop();
		}

		Player player = this.getScene().getPlayer();

		if (this.playerCaught) {			
			// if aiming is already activated:
			if (this.warmUp != null && this.warmUp.timeUp()) {
				long deltaTime = GameTime.getTime() - this.lastTime;
				Vec vel = player.getVel();
				
				if (!player.getEntityState().canJump()) { // JUMP
					this.shootPlayer();
					return;
				}
				
				if (vel.x < -0.1) { // LEFT
					this.addToCurrentAngle(this.invertControls * 0.000002 * deltaTime);
				}
				if (vel.x > 0.1) { // RIGHT
					this.addToCurrentAngle(this.invertControls * -0.000002 * deltaTime);
				}
			}
			
			this.getScene().getPlayer().getEntityState().floorCollision();

			// holds player still
			player.setPos(this.catchPos);
			player.setVel(new Vec());			
		}

		super.logicLoop();
	}

	/**
	 * Shoots the {@link Player} to the {@link AcceleratorBox#angleCurrent} with
	 * the boost {@link AcceleratorBox#BOOST}. Will have no effect if
	 * {@link AcceleratorBox#playerCaught} is false and sets it to false
	 * afterwards.
	 */
	private void shootPlayer() {
		if (!this.playerCaught) {
			return;
		}
		
		Vec unitVec = new Vec(-Math.sin(this.angleCurrent), -Math.cos(this.angleCurrent));
		this.getScene().getPlayer().setVel(unitVec.scalar(AcceleratorBox.BOOST));
		
		this.getScene().getPlayer().setEntityState(new FallState(this.getScene().getPlayer()));
				
		this.playerCaught = false;
	}

	@Override
	public void internalRender(GameGrid f) {
		RGBAColor col2 = this.playerCaught ? AcceleratorBox.COL_2_ACTIVE : AcceleratorBox.COL_2;
		
		f.drawRectangle(this.getPos(), this.getSize(), col2);
		f.drawRectangle(this.getPos(), this.getSize().add(new Vec(-2 * AcceleratorBox.BORDER_WIDTH)), AcceleratorBox.COL_1);

		this.innerPolygon.moveTo(this.getPos());
		f.drawPolygon(this.innerPolygon, col2, true, true);

		if (this.playerCaught) {
			this.drawAngleLineTo(f, this.angleLeft, AcceleratorBox.ANGLE_BOUND_COLOR);
			this.drawAngleLineTo(f, this.angleRight, AcceleratorBox.ANGLE_BOUND_COLOR);
			this.drawAngleLineTo(f, this.angleCurrent, AcceleratorBox.ANGLE_CURRENT_COLOR);
		}
	}

	/**
	 * Helper method for drawing a line to show visual feedback of the angles.
	 * Note that this method will do nothing while
	 * {@link AcceleratorBox#playerCaught} is false.
	 *
	 * @param f
	 *            the {@link GameGrid} to draw on.
	 * @param angle
	 *            the angle to visualize
	 * @param color
	 *            the color of the angle
	 */
	private void drawAngleLineTo(GameGrid f, double angle, RGBAColor color) {
		if (!this.playerCaught) {
			return;
		}

		Vec unitVec = new Vec(-Math.sin(angle), -Math.cos(angle));
		Vec otherPos = this.catchPos.add(unitVec.scalar(AcceleratorBox.ANGLE_LINE_LENGTH));

		f.drawLine(this.catchPos, otherPos, AcceleratorBox.ANGLE_LINE_WIDTH, color, true, false);
	}

	@Override
	public AcceleratorBox create(Vec startPos, String... options) {

		Direction dir = Direction.UP;

		// if option 0 is given: set defined direction
		if (options.length >= 1 && options[0] != null && options[0].matches("(\\+|-)?[0-3]+")) {
			int opt = Integer.parseInt(options[0]);
			if (opt >= 0 && opt < Direction.values().length) {
				dir = Direction.values()[opt];
			} else {
				GameConf.GAME_LOGGER.error("RektKiller was supplied invalid option " + options[0] + " at index 0 for Direction");
			}
		}

		return new AcceleratorBox(startPos, dir);
	}
}
