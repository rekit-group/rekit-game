package edu.kit.informatik.ragnarok.logic.gameelements.entities.enemies.cannon.state;

import edu.kit.informatik.ragnarok.config.GameConf;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.Player;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.enemies.cannon.Cannon;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.enemies.cannon.CannonParticle;
import edu.kit.informatik.ragnarok.logic.gameelements.inanimate.Inanimate;
import edu.kit.informatik.ragnarok.logic.gameelements.particles.ParticleSpawner;
import edu.kit.informatik.ragnarok.logic.gameelements.particles.ParticleSpawnerOption;
import edu.kit.informatik.ragnarok.logic.state.TimeStateMachine;
import edu.kit.informatik.ragnarok.primitives.geometry.Vec;
import edu.kit.informatik.ragnarok.util.CalcUtil;

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
	 */
	public ShootingState(float angle) {
		super(angle);
	}

	@Override
	public void enter(TimeStateMachine parent) {
		super.enter(parent);

		spawner = new ParticleSpawner(new CannonParticle(this.parentCannon));

		spawner.amountMin = Cannon.PARTICLE_AMOUNT_MIN;
		spawner.amountMax = Cannon.PARTICLE_AMOUNT_MAX;
		spawner.colorR = Cannon.PARTICLE_COLOR_R;
		spawner.colorG = Cannon.PARTICLE_COLOR_G;
		spawner.colorB = Cannon.PARTICLE_COLOR_B;
		spawner.colorA = Cannon.PARTICLE_COLOR_A;
		spawner.speed = Cannon.PARTICLE_SPEED;
		spawner.timeMin = Cannon.PARTICLE_TIME_MIN;
		spawner.timeMax = Cannon.PARTICLE_TIME_MAX;
	}

	@Override
	public void logicLoop(float deltaTime) {
		super.logicLoop(deltaTime);

		if (this.keepShooting) {
			currentDistance += CalcUtil.randomize(Cannon.PARTICLE_DISTANCE_MU, Cannon.PARTICLE_DISTANCE_SIGMA);

			// move cannon position down and rotate it around cannon.
			Vec pos = this.parentCannon.getPos().addY(currentDistance).rotate(-this.angle, this.parentCannon.getPos());
			spawner.rotation = new ParticleSpawnerOption(-this.angle, CalcUtil.randomize(Math.PI / 4, Math.PI / 8));

			// set angle to move either right or left at 90 degree angle
			spawner.angle = new ParticleSpawnerOption((float) (-this.angle + ((GameConf.PRNG.nextBoolean()) ? 1 : -1) * Math.PI / 2));

			// Spawn the killer particle at pos
			spawner.spawn(this.parentCannon.getScene(), pos);
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
	public float getTimerTime() {
		return Cannon.STATE_SHOOTING_DURATION;
	}

	@Override
	public void hitSomething() {
		this.keepShooting = false;
	}

}
