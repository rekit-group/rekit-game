package edu.kit.informatik.ragnarok.logic.gameelements.entities.enemies.canon.canonstate;

import edu.kit.informatik.ragnarok.config.GameConf;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.enemies.canon.Canon;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.enemies.canon.CanonParticle;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.enemies.canon.StateMachine;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.particles.ParticleSpawner;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.particles.ParticleSpawnerOption;
import edu.kit.informatik.ragnarok.primitives.geometry.Vec;
import edu.kit.informatik.ragnarok.util.CalcUtil;

public class ShootingState extends ChargingState {
	
	private float currentDistance = 1;
	
	private ParticleSpawner spawner;
	
	public ShootingState(float angle) {
		super(angle);
	}
	
	@Override
	public void enter(StateMachine parent) {
		super.enter(parent);
		
		spawner = new ParticleSpawner(new CanonParticle());
		
		spawner.amountMin = 1;
		spawner.amountMax = 1;
		spawner.colorR = new ParticleSpawnerOption(50);
		spawner.colorG = new ParticleSpawnerOption(255);
		spawner.colorB = new ParticleSpawnerOption(100);
		spawner.colorA = new ParticleSpawnerOption(200, -100);
		spawner.speed = new ParticleSpawnerOption(0.1f);
		spawner.timeMin = Canon.STATE_SHOOTING_DURATION;
		spawner.timeMax = Canon.STATE_SHOOTING_DURATION;
		
		spawner.rotation = new ParticleSpawnerOption(-this.angle, -this.angle, (float)Math.PI / 4, (float)Math.PI / 8);

		
	}
	
	@Override
	public void logicLoop(float deltaTime) {
		super.logicLoop(deltaTime);
		
		float distanceMu = 0.2f;
		float distanceSigma = 0.05f;
		float maxDistance = 40;
		
		if (currentDistance < maxDistance) {
			currentDistance += CalcUtil.randomize(distanceMu, distanceSigma);
			
			// move canon position down and rotate it around canon.
			Vec pos = this.parent.getPos().addY(currentDistance).rotate(-this.angle, this.parent.getPos());
			spawner.rotation = new ParticleSpawnerOption(-this.angle, CalcUtil.randomize(Math.PI / 4, Math.PI / 8));
			
			// set angle to move either right or left at 90 degree angle
			spawner.angle = new ParticleSpawnerOption((float) (-this.angle + ((GameConf.PRNG.nextBoolean()) ? 1 : -1) * Math.PI / 2));
			
			// Spawn the killer particle at pos
			spawner.spawn(this.parent.getScene(), pos);
		}
		
	}
	
	@Override
	public float shakeStrength() {
		return 1;
	}
	
	@Override
	public State getNextState() {
		return new IdleState();
	}

	@Override
	public float getTimerTime() {
		return Canon.STATE_SHOOTING_DURATION;
	}
	
}
