package rekit.logic.gameelements.entities.enemies.cannon;

import org.fuchss.configuration.Configurable;
import org.fuchss.configuration.annotations.NoSet;
import org.fuchss.configuration.annotations.SetterInfo;

import rekit.core.GameGrid;
import rekit.logic.gameelements.entities.Player;
import rekit.logic.gameelements.entities.enemies.cannon.state.AimingState;
import rekit.logic.gameelements.entities.enemies.cannon.state.CannonState;
import rekit.logic.gameelements.entities.enemies.cannon.state.ChargingState;
import rekit.logic.gameelements.entities.enemies.cannon.state.IdleState;
import rekit.logic.gameelements.entities.enemies.cannon.state.ShootingState;
import rekit.logic.gameelements.inanimate.Inanimate;
import rekit.logic.gameelements.particles.Particle;
import rekit.logic.gameelements.particles.ParticleSpawnerOption;
import rekit.logic.gameelements.type.Enemy;
import rekit.primitives.geometry.Direction;
import rekit.primitives.geometry.Polygon;
import rekit.primitives.geometry.Vec;
import rekit.primitives.image.RGBAColor;
import rekit.util.ReflectUtils.LoadMe;

/**
 * <p>
 * Enemy that resembles a cannon and periodically tries to shoot the
 * {@link Player} with a ray of ...laser? The Cannon itself cannot be killed,
 * however the {@link Player} can hide behind {@link Inanimate Inanimates} to
 * escape the laser.
 * </p>
 * <p>
 * The shooting consists of 4 time-based phases that are circularly repeated:
 * <ul>
 * <li><i>Idle</i>, where the Cannon aims directly downwards.</li>
 * <li><i>Aiming</i>, where the Cannon rotates to the {@link Player Players}
 * position with a fixed speed.</li>
 * <li><i>Charging</i>, where the rotation of the Cannon is frozed for a short
 * period of time an a graphical "shaking" starts and increases.</li>
 * <li><i>Shooting</i>, where the Cannon spawns {@link CannonParticle
 * CannonParticles} in a straight line.</li>
 * </ul>
 * <p>
 * Internally, it uses the {@link CannonStateMachine} and corresponding
 * {@link CannonState CannonStates} to implement the phase-like behavior as
 * described above.
 *
 * @author Angelo Aracri
 */
@LoadMe
@SetterInfo(res = "conf/cannon")
public class Cannon extends Enemy implements Configurable {

	/**
	 * Configurable Vector that holds the size of the Cannon.
	 */
	private static Vec SIZE;

	/**
	 * Configurable time in seconds, that the <i>idle</i> {@link CannonState} (
	 * {@link IdleState}) is going to take before switching to the next
	 * {@link CannonState}. before switching to the next state
	 */
	public static float STATE_IDLE_DURATION;

	/**
	 * Configurable time in seconds, that the <i>aiming</i> {@link CannonState}
	 * ( {@link AimingState}) is going to take before switching to the next
	 * {@link CannonState}. before switching to the next state
	 */
	public static float STATE_AIMING_DURATION;

	/**
	 * Configurable time in seconds, that the <i>charging</i>
	 * {@link CannonState} ( {@link ChargingState}) is going to take before
	 * switching to the next {@link CannonState}. before switching to the next
	 * state
	 */
	public static float STATE_CHARGING_DURATION;

	/**
	 * Configurable time in seconds, that the <i>shooting</i>
	 * {@link CannonState} ( {@link ShootingState}) is going to take before
	 * switching to the next {@link CannonState}. before switching to the next
	 * state
	 */
	public static float STATE_SHOOTING_DURATION;

	/**
	 * Configurable speed in radians per second that specify how fast the Cannon
	 * can rotate.
	 */
	public static float ANGLE_SPEED;

	/**
	 * Configurable {@link } that will be used as the color of the base (the
	 * non-rotating part of the {@link Cannon}).
	 */
	public static RGBAColor COLOR_BASE;

	/**
	 * Configurable {@link } that will be used as the color of the pipe (the
	 * rotating part of the {@link Cannon}).
	 */
	public static RGBAColor COLOR_CANNON;

	/**
	 * Configurable width of the {@link Cannon Cannons} pipe (the rotating part
	 * of the {@link Cannon}).
	 */
	public static float PIPE_W;

	/**
	 * Configurable height of the {@link Cannon Cannons} pipe (the rotating part
	 * of the {@link Cannon}).
	 */
	public static float PIPE_H;

	/**
	 * Configurable ratio of the rotating, inner circle to the non-rotating
	 * outer circle.
	 */
	public static float JOINT_RATIO;

	/**
	 * Configurable distance that defines the maximum shaking the pipe can do.
	 */
	public static float MAX_SHAKING;

	public static int PARTICLE_AMOUNT_MIN;
	public static int PARTICLE_AMOUNT_MAX;
	public static ParticleSpawnerOption PARTICLE_COLOR_R;
	public static ParticleSpawnerOption PARTICLE_COLOR_G;
	public static ParticleSpawnerOption PARTICLE_COLOR_B;
	public static ParticleSpawnerOption PARTICLE_COLOR_A;
	public static ParticleSpawnerOption PARTICLE_SPEED;
	public static float PARTICLE_TIME_MIN;
	public static float PARTICLE_TIME_MAX;

	/**
	 * Configurable average distance between the {@link Particle Particles}.
	 */
	public static float PARTICLE_DISTANCE_MU;

	/**
	 * Configurable sigma of the distance between the {@link Particle Particles}
	 * .
	 */
	public static float PARTICLE_DISTANCE_SIGMA;

	/**
	 * The inner, decorated {@link CannonStateMachine} that implements the
	 * phase-like behavior.
	 */
	@NoSet
	private CannonStateMachine innerStateMachine;

	/**
	 * The angle in radians the {@link Cannon Cannons} pipe currently aims at,
	 * where 0 is down.
	 */
	@NoSet
	private float currentAngle;

	/**
	 * The {@link Polygon} that will be used for rendering the {@link Cannon
	 * Cannons} pipe.
	 */
	@NoSet
	private Polygon pipePolygon;

	/**
	 * The {@link Direction} this Cannon will be (graphically) attached to.
	 */
	@NoSet
	private Direction attachedSide;

	/**
	 * Prototype constructor. Should not be used unless you know what you are
	 * doing.
	 */
	public Cannon() {
	}

	/**
	 * Main constructor that instantiates the inner {@link CannonStateMachine}
	 * and the {@link Polygon}.
	 *
	 * @param pos
	 *            the position of the {@link Cannon}.
	 * @param attachedSide
	 *            the attached side
	 */
	public Cannon(Vec pos, Direction attachedSide) {
		super(pos.addY(-0.5f + Cannon.SIZE.y / 2f), new Vec(), Cannon.SIZE);

		this.innerStateMachine = new CannonStateMachine(this, new IdleState());

		this.currentAngle = this.innerStateMachine.getState().getTargetAngle();

		this.attachedSide = attachedSide;

		this.pipePolygon = new Polygon(new Vec(),
				new Vec[] { new Vec(Cannon.PIPE_W / 2, 0), new Vec(Cannon.PIPE_W / 2, Cannon.PIPE_H),
						new Vec(-Cannon.PIPE_W / 2, Cannon.PIPE_H), new Vec(-Cannon.PIPE_W / 2, 0), new Vec(0, 0) });
	}

	@Override
	public Cannon create(Vec startPos, String... options) {
		Direction attachedSide = Direction.UP;
		if (options.length >= 1 && options[0] != null && options[0].matches("[0-3]+")) {
			attachedSide = Direction.values()[Integer.parseInt(options[0])];
		}

		return new Cannon(startPos, attachedSide);
	}

	@Override
	public void internalRender(GameGrid f) {

		// draw cannon base
		f.drawCircle(this.getPos(), this.getSize(), Cannon.COLOR_BASE);

		Vec size;
		Vec pos;
		switch (this.attachedSide) {
		case UP:
			pos = this.getPos().addY(-this.getSize().y / 4f);
			size = this.getSize().scalar(1, 0.5f);
			break;
		case DOWN:
			pos = this.getPos().addY(this.getSize().y / 4f);
			size = this.getSize().scalar(1, 0.5f);
			break;
		case LEFT:
			pos = this.getPos().addX(-this.getSize().y / 4f);
			size = this.getSize().scalar(0.5f, 1);
			break;
		case RIGHT:
			pos = this.getPos().addX(this.getSize().y / 4f);
			size = this.getSize().scalar(0.5f, 1);
			break;
		default:
			pos = new Vec();
			size = new Vec();
		}
		f.drawRectangle(pos, size, Cannon.COLOR_BASE);

		// draw rotated cannon with (optional) shaking
		Vec cannonPos = this.getPos().addX(this.innerStateMachine.getState().getCannonShake());
		f.drawCircle(cannonPos, this.getSize().scalar(Cannon.JOINT_RATIO), Cannon.COLOR_CANNON);
		this.pipePolygon.moveTo(cannonPos);
		f.drawPolygon(this.pipePolygon.rotate(-this.currentAngle, this.getPos()), Cannon.COLOR_CANNON, true);
	}

	@Override
	protected void innerLogicLoop() {
		this.innerStateMachine.logicLoop();

		// move angle in right direction
		this.currentAngle += Math.signum(this.innerStateMachine.getState().getTargetAngle() - this.currentAngle)
				* this.deltaTime / 1000F * Cannon.ANGLE_SPEED;

		if (Math.abs(this.innerStateMachine.getState().getTargetAngle() - this.currentAngle) < Cannon.ANGLE_SPEED
				/ 20) {
			this.currentAngle = this.innerStateMachine.getState().getTargetAngle();
		}
	}

	/**
	 * Getter for the inner, decorated {@link CannonStateMachine}.
	 *
	 * @return the inner {@link CannonStateMachine}.
	 */
	public CannonStateMachine getInnerStateMachine() {
		return this.innerStateMachine;
	}

	/**
	 * Signal that one of the {@link Particle Particles} collided with something
	 * and the laser should stop. Is only used while in the ShootingState.
	 */
	public void hitSomething() {
		this.innerStateMachine.getState().hitSomething();
	}

	@Override
	public boolean isAddableToGroup() {
		return false;
	}

}
