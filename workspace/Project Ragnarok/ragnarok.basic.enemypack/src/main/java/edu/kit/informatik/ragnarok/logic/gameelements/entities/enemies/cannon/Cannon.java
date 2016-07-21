package edu.kit.informatik.ragnarok.logic.gameelements.entities.enemies.cannon;

import edu.kit.informatik.ragnarok.logic.Field;
import edu.kit.informatik.ragnarok.logic.gameelements.GameElement;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.enemies.cannon.state.IdleState;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.particles.ParticleSpawnerOption;
import edu.kit.informatik.ragnarok.logic.gameelements.type.Enemy;
import edu.kit.informatik.ragnarok.logic.state.TimeStateMachine;
import edu.kit.informatik.ragnarok.primitives.geometry.Polygon;
import edu.kit.informatik.ragnarok.primitives.geometry.Vec;
import edu.kit.informatik.ragnarok.primitives.image.RGBColor;
import edu.kit.informatik.ragnarok.util.ReflectUtils.LoadMe;
import edu.kit.informatik.ragnarok.visitor.NoVisit;
import edu.kit.informatik.ragnarok.visitor.VisitInfo;
import edu.kit.informatik.ragnarok.visitor.Visitable;

@LoadMe
@VisitInfo(res = "conf/cannon", visit = true)
/**
 * <p>
 * Enemy that resembles a cannon that periodically tries to shoot the {@link Player}.
 * </p>
 * <p>
 * The shooting consists of 4 time-based phases that are circularly repeated:
 * <ul>
 * <li><i>Idle</i>, where the Cannon aims directly downwards.</li>
 * <li><i>Aiming</i>, where the Cannon rotates to the {@link Player Players} position with a fixed speed.</li>
 * <li><i>Charging</i>, where the rotation of the Cannon is frozed for a short period of time an a graphical "shaking" starts and increases.</li>
 * <li><i>Shooting</i>, where the Cannon spawns {@link CannonParticle CannonParticles} in a straight line.</li>  
 * </ul>
 * </p>
 * <p>
 * It is designed to be attached on
 * </p> 
 * @author Angelo Aracri
 */
public class Cannon extends Enemy implements Visitable {

	private static Vec SIZE;
	public static float STATE_IDLE_DURATION;
	public static float STATE_AIMING_DURATION;
	public static float STATE_CHARGING_DURATION;
	public static float STATE_SHOOTING_DURATION;

	public static float ANGLE_SPEED;

	public static RGBColor COLOR_BASE;
	public static RGBColor COLOR_CANNON;

	public static float PIPE_W;
	public static float PIPE_H;

	public static float JOINT_RATIO;

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

	public static float PARTICLE_DISTANCE_MU;
	public static float PARTICLE_DISTANCE_SIGMA;

	@NoVisit
	private CannonStateMachine innerStateMachine;

	@NoVisit
	private float currentAngle;

	@NoVisit
	private Polygon polygon;

	/**
	 * Prototype constructor
	 */
	public Cannon() {
	}

	public Cannon(Vec pos) {
		super(pos.addY(-0.5f + SIZE.getY() / 2f), new Vec(), SIZE);

		this.innerStateMachine = new CannonStateMachine(this, new IdleState());

		this.currentAngle = innerStateMachine.getState().getTargetAngle();

		this.polygon = new Polygon(new Vec(), new Vec[] { new Vec(PIPE_W / 2, 0), new Vec(PIPE_W / 2, PIPE_H), new Vec(-PIPE_W / 2, PIPE_H),
				new Vec(-PIPE_W / 2, 0), new Vec(0, 0) });
	}

	@Override
	public GameElement create(Vec startPos, String[] options) {
		return new Cannon(startPos);
	}

	@Override
	public void internalRender(Field f) {

		// draw cannon base
		f.drawCircle(this.getPos(), this.getSize(), COLOR_BASE);
		f.drawRectangle(this.getPos().addY(-this.getSize().getY() / 4f), this.getSize().scalar(1, 0.5f), COLOR_BASE);

		// draw rotated cannon with (optional) shaking
		Vec cannonPos = this.getPos().addX(this.innerStateMachine.getState().getCannonShake());
		f.drawCircle(cannonPos, this.getSize().scalar(JOINT_RATIO), COLOR_CANNON);
		this.polygon.moveTo(cannonPos);
		f.drawPolygon(this.polygon.rotate(-this.currentAngle, this.getPos()), COLOR_CANNON, true);
	}

	@Override
	public void logicLoop(float deltaTime) {
		this.innerStateMachine.getState().logicLoop(deltaTime);

		// move angle in right direction
		this.currentAngle += Math.signum(this.innerStateMachine.getState().getTargetAngle() - this.currentAngle) * deltaTime * ANGLE_SPEED;

		if (Math.abs(this.innerStateMachine.getState().getTargetAngle() - this.currentAngle) < ANGLE_SPEED / 20) {
			this.currentAngle = this.innerStateMachine.getState().getTargetAngle();
		}
	}

	public TimeStateMachine getInnerStateMachine() {
		return this.innerStateMachine;
	}

	public void hitSomething() {
		this.innerStateMachine.getState().hitSomething();
	}

}
