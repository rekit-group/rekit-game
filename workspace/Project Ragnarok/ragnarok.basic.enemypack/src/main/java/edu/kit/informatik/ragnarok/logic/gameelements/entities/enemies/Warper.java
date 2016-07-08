package edu.kit.informatik.ragnarok.logic.gameelements.entities.enemies;

import edu.kit.informatik.ragnarok.config.GameConf;
import edu.kit.informatik.ragnarok.logic.gameelements.Field;
import edu.kit.informatik.ragnarok.logic.gameelements.GameElement;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.Entity;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.particles.ParticleSpawner;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.particles.ParticleSpawnerOption;
import edu.kit.informatik.ragnarok.logic.gameelements.type.Enemy;
import edu.kit.informatik.ragnarok.primitives.Direction;
import edu.kit.informatik.ragnarok.primitives.Frame;
import edu.kit.informatik.ragnarok.primitives.TimeDependency;
import edu.kit.informatik.ragnarok.primitives.Vec;
import edu.kit.informatik.ragnarok.util.RGBColor;

public class Warper extends Enemy {

	private TimeDependency warpAction = new TimeDependency(GameConf.WARPER_WARP_DELTA);
	private static ParticleSpawner warpParticles = null;

	static {
		Warper.warpParticles = new ParticleSpawner();
		Warper.warpParticles.angle = new ParticleSpawnerOption(0, (float) (2 * Math.PI), (float) (2 * Math.PI), (float) (4 * Math.PI));
		Warper.warpParticles.colorR = new ParticleSpawnerOption(250, 0);
		Warper.warpParticles.colorG = new ParticleSpawnerOption(250, -250);
		Warper.warpParticles.colorB = new ParticleSpawnerOption(150);
		Warper.warpParticles.colorA = new ParticleSpawnerOption(220, -220);
		Warper.warpParticles.timeMin = 1f;
		Warper.warpParticles.speed = new ParticleSpawnerOption(2, 3, -1, 1);
	}

	/**
	 * Prototype Constructor
	 */
	public Warper() {
		super();
	}

	public Warper(Vec startPos) {
		super(startPos, new Vec(), new Vec(0.6f, 0.6f));
	}

	@Override
	public void internalRender(Field f) {
		float progress = this.warpAction.getProgress();
		for (float i = 1; i >= 0.2; i -= 0.1) {
			RGBColor innerColor = new RGBColor((int) (250 * i), (int) (250 * (1 - progress)), (150));
			// draw body
			f.drawCircle(this.getPos(), this.getSize().multiply(i), innerColor);
		}

	}

	@Override
	public void logicLoop(float deltaTime) {
		// decrease time left
		this.warpAction.removeTime(deltaTime);

		// animate particles
		Warper.warpParticles.amountMin = -5;
		Warper.warpParticles.amountMax = 2;
		Warper.warpParticles.spawn(this.scene, this.getPos());

		// if time is up
		if (this.warpAction.timeUp()) {
			// reset time
			this.warpAction.reset();

			// get target (player)
			Vec target = this.scene.getPlayer().getPos();

			// animate particles
			Warper.warpParticles.amountMin = 5;
			Warper.warpParticles.amountMax = 8;
			Warper.warpParticles.spawn(this.scene, this.getPos());

			// determine if x or y is greater in distance
			Vec dif = this.getPos().add(target.multiply(-1));
			if (Math.abs(dif.getX()) > Math.abs(dif.getY())) {
				this.setPos(this.getPos().addX(-Math.signum(dif.getX())));
			} else {
				this.setPos(this.getPos().addY(-Math.signum(dif.getY())));
			}
		}
	}

	@Override
	public void reactToCollision(GameElement element, Direction dir) {
		if (this.deleteMe) {
			return;
		}

		if (this.team.isHostile(element.getTeam())) {

			// Give player damage
			element.addDamage(1);

			// Kill the warper itself
			this.addDamage(1);
		}
	}

	@Override
	public void collidedWith(Frame collision, Direction dir) {
		// Do nothing, pass right through everything
	}

	@Override
	public void addDamage(int damage) {
		this.destroy();
	}

	@Override
	public Entity create(Vec startPos) {
		return new Warper(startPos);
	}

	@Override
	public int getID() {
		return 4;
	}

}
