package edu.kit.informatik.ragnarok.logic.gameelements.entities.enemies.cannon.state;

import edu.kit.informatik.ragnarok.config.GameConf;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.enemies.cannon.Cannon;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.enemies.cannon.CannonParticle;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.particles.ParticleSpawner;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.particles.ParticleSpawnerOption;
import edu.kit.informatik.ragnarok.logic.state.TimeStateMachine;
import edu.kit.informatik.ragnarok.primitives.geometry.Vec;
import edu.kit.informatik.ragnarok.util.CalcUtil;

public class ShootingState extends ChargingState {

	private float currentDistance = 1;

	private ParticleSpawner spawner;

	private boolean keepShooting = true;

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
