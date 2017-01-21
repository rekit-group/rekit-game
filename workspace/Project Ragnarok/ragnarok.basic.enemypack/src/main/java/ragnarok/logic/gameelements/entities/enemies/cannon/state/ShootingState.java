package ragnarok.logic.gameelements.entities.enemies.cannon.state;

import ragnarok.config.GameConf;
import ragnarok.logic.gameelements.entities.Player;
import ragnarok.logic.gameelements.entities.enemies.cannon.Cannon;
import ragnarok.logic.gameelements.entities.enemies.cannon.CannonParticle;
import ragnarok.logic.gameelements.inanimate.Inanimate;
import ragnarok.logic.gameelements.particles.ParticleSpawner;
import ragnarok.logic.gameelements.particles.ParticleSpawnerOption;
import ragnarok.primitives.geometry.Vec;
import ragnarok.util.CalcUtil;
import ragnarok.util.state.TimeStateMachine;

/**
 * <p>
 * IMA FIRIN MAH LAZOR!
 * </p>
 * <p>
 * Fourth and last {@link CannonState} of the {@link State} that represents the
 * phase where the {@link Cannon} unleashed his fierce and widow-creating laser
 * beam.
 * </p>
 * <p>
 * The beam consists of a number of {@link CannonParticle CannonParticles} that
 * will be spawned one at a time until colliding with the {@link Player} or an
 * {@link Inanimate}.
 * </p>
 * <p>
 * It extends the {@link ChargingState} to enable the shaking effect.
 * </p>
 *
 * @author Angelo Aracri
 */
public class ShootingState extends ChargingState {

	/**
	 * Distance from the {@link Cannon} to the next {@link CannonParticle} to be
	 * spawned.
	 */
	private float currentDistance = 0.5f;

	/**
	 * The {@link ParticleSpawner} that can spawn specialized
	 * {@link CannonParticle CannonParticles}.
	 */
	private ParticleSpawner spawner;

	/**
	 * Boolean flag to indicate whether the {@link Cannon} is supposed to spawn
	 * more {@link CannonParticles} (a.k.a shoot more laser) or not.
	 */
	private boolean keepShooting = true;

	/**
	 * Specialized constructor that saves the angle in radians to shoot at.
	 *
	 * @param angle
	 *            the angle
	 */
	public ShootingState(float angle) {
		super(angle);
	}

	@Override
	public void enter(TimeStateMachine parent) {
		super.enter(parent);

		this.spawner = new ParticleSpawner(new CannonParticle(this.parentCannon));

		this.spawner.amountMin = Cannon.PARTICLE_AMOUNT_MIN;
		this.spawner.amountMax = Cannon.PARTICLE_AMOUNT_MAX;
		this.spawner.colorR = Cannon.PARTICLE_COLOR_R;
		this.spawner.colorG = Cannon.PARTICLE_COLOR_G;
		this.spawner.colorB = Cannon.PARTICLE_COLOR_B;
		this.spawner.colorA = Cannon.PARTICLE_COLOR_A;
		this.spawner.speed = Cannon.PARTICLE_SPEED;
		this.spawner.timeMin = Cannon.PARTICLE_TIME_MIN;
		this.spawner.timeMax = Cannon.PARTICLE_TIME_MAX;
	}

	@Override
	public void logicLoop() {
		super.logicLoop();

		if (this.keepShooting) {
			this.currentDistance += CalcUtil.randomize(Cannon.PARTICLE_DISTANCE_MU, Cannon.PARTICLE_DISTANCE_SIGMA);

			// move cannon position down and rotate it around cannon.
			Vec pos = this.parentCannon.getPos().addY(this.currentDistance).rotate(-this.angle, this.parentCannon.getPos());
			this.spawner.rotation = new ParticleSpawnerOption(-this.angle, CalcUtil.randomize((float) Math.PI / 4, (float) Math.PI / 8));

			// set angle to move either right or left at 90 degree angle
			this.spawner.angle = new ParticleSpawnerOption((float) (-this.angle + ((GameConf.PRNG.nextBoolean()) ? 1 : -1) * Math.PI / 2));

			// Spawn the killer particle at pos
			this.spawner.spawn(this.parentCannon.getScene(), pos);
		}
	}

	@Override
	public float shakeStrength() {
		return 1;
	}

	@Override
	public CannonState getNextState() {
		return new IdleState();
	}

	@Override
	public long getTimerTime() {
		return (long) (1000 * Cannon.STATE_SHOOTING_DURATION);
	}

	@Override
	public void hitSomething() {
		this.keepShooting = false;
	}

}
