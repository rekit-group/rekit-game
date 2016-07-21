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

		spawner.amountMin = 1;
		spawner.amountMax = 1;
		spawner.colorR = new ParticleSpawnerOption(50);
		spawner.colorG = new ParticleSpawnerOption(255);
		spawner.colorB = new ParticleSpawnerOption(100);
		spawner.colorA = new ParticleSpawnerOption(200, -100);
		spawner.speed = new ParticleSpawnerOption(0.03f);
		spawner.timeMin = spawner.timeMax = Cannon.STATE_SHOOTING_DURATION;

		// spawner.rotation = new ParticleSpawnerOption(-this.angle,
		// -this.angle, (float)Math.PI / 4, (float)Math.PI / 8);

	}

	@Override
	public void logicLoop(float deltaTime) {
		super.logicLoop(deltaTime);

		float distanceMu = 0.2f;
		float distanceSigma = 0.05f;
		float maxDistance = 40;

		if (currentDistance < maxDistance && this.keepShooting) {
			currentDistance += CalcUtil.randomize(distanceMu, distanceSigma);

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
