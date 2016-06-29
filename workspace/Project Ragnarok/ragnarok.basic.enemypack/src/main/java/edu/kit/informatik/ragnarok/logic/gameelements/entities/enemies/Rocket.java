package edu.kit.informatik.ragnarok.logic.gameelements.entities.enemies;

import edu.kit.informatik.ragnarok.config.GameConf;
import edu.kit.informatik.ragnarok.logic.gameelements.Field;
import edu.kit.informatik.ragnarok.logic.gameelements.GameElement;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.Entity;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.particles.ParticleSpawner;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.particles.ParticleSpawnerOption;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.type.Enemy;
import edu.kit.informatik.ragnarok.primitives.Direction;
import edu.kit.informatik.ragnarok.primitives.Frame;
import edu.kit.informatik.ragnarok.primitives.Polygon;
import edu.kit.informatik.ragnarok.primitives.Vec;
import edu.kit.informatik.ragnarok.util.RGBColor;

public class Rocket extends Enemy {
	/**
	 * Prototype Constructor
	 */
	public Rocket() {
		super();
	}

	private static RGBColor innerColor = new RGBColor(90, 90, 90);
	private static RGBColor frontColor = new RGBColor(150, 30, 30);
	private static RGBColor outerColor = new RGBColor(50, 50, 50);

	private static ParticleSpawner sparkParticles = null;
	static {
		Rocket.sparkParticles = new ParticleSpawner();
		Rocket.sparkParticles.angle = new ParticleSpawnerOption((float) ((1 / 4f) * Math.PI), (float) ((3 / 4f) * Math.PI), 0, 0);
		Rocket.sparkParticles.colorR = new ParticleSpawnerOption(200, 230, 10, 25);
		Rocket.sparkParticles.colorG = new ParticleSpawnerOption(200, 250, -140, -120);
		Rocket.sparkParticles.colorB = new ParticleSpawnerOption(150, 200, -140, -120);
		Rocket.sparkParticles.colorA = new ParticleSpawnerOption(230, 250, -150, -230);
		Rocket.sparkParticles.timeMin = 0.1f;
		Rocket.sparkParticles.amountMin = 1;
		Rocket.sparkParticles.amountMax = 3;
		Rocket.sparkParticles.speed = new ParticleSpawnerOption(3, 6, -1, 1);
	}
	private static ParticleSpawner explosionParticles = null;
	static {
		Rocket.explosionParticles = new ParticleSpawner();
		Rocket.explosionParticles.angle = new ParticleSpawnerOption(0, (float) (2 * Math.PI), 0, 0);
		Rocket.explosionParticles.colorR = new ParticleSpawnerOption(200, 230, 10, 25);
		Rocket.explosionParticles.colorG = new ParticleSpawnerOption(200, 250, -130, -110);
		Rocket.explosionParticles.colorB = new ParticleSpawnerOption(150, 200, -130, -110);
		Rocket.explosionParticles.colorA = new ParticleSpawnerOption(230, 250, -120, -200);
		Rocket.explosionParticles.timeMin = 0.1f;
		Rocket.explosionParticles.timeMax = 0.2f;
		Rocket.explosionParticles.amountMin = 40;
		Rocket.explosionParticles.amountMax = 50;
		Rocket.explosionParticles.speed = new ParticleSpawnerOption(4, 9, -1, 1);
	}

	public Rocket(Vec startPos) {
		super(startPos, new Vec(), new Vec(1.8f, 0.5f));
	}

	@Override
	public void internalRender(Field f) {
		Rocket.sparkParticles.spawn(this.scene, this.getPos().addX(this.getSize().getX() / 2));

		// draw body
		f.drawRectangle(this.getPos(), this.getSize().multiply(0.8f, 0.6f), Rocket.innerColor);
		// draw spike at front
		Vec startPt = this.getPos().addX(-this.getSize().multiply(0.5f).getX());
		Vec[] relPts = new Vec[] { //
				new Vec(this.getSize().multiply(0.1f).getX(), -this.getSize().multiply(0.5f).getY()),
				new Vec(this.getSize().multiply(0.1f).getX(), this.getSize().multiply(0.5f).getY()), //
				new Vec() //
		};
		f.drawPolygon(new Polygon(startPt, relPts), Rocket.frontColor);

		// draw stripes
		Vec stripeStart = this.getPos().addX(-this.getSize().multiply(0.4f - 0.05f - 0.025f).getX());
		for (int x = 0; x < 9; x++) {
			f.drawRectangle(stripeStart.addX(0.15f * x), this.getSize().multiply(0.05f, 0.75f), Rocket.outerColor);
		}

		// draw drive at back
		startPt = this.getPos().addX(this.getSize().multiply(0.5f).getX()).addY(-this.getSize().multiply(0.5f).getY());
		relPts = new Vec[] { //
				new Vec(0, this.getSize().getY()), new Vec(-this.getSize().getX() * 0.1f, this.getSize().getY() * 0.8f),
				new Vec(-this.getSize().getX() * 0.1f, this.getSize().getY() * 0.2f), //
				new Vec() //
		};
		f.drawPolygon(new Polygon(startPt, relPts), Rocket.outerColor);
	}

	@Override
	public void logicLoop(float deltaTime) {
		// move ahead with player max speed
		this.setPos(this.getPos().addX(-GameConf.PLAYER_WALK_MAX_SPEED * deltaTime));

	}

	@Override
	public void reactToCollision(GameElement element, Direction dir) {
		if (this.deleteMe) {
			return;
		}

		if (this.team.isHostile(element.getTeam())) {

			if (dir == Direction.UP) {
				element.setVel(element.getVel().setY(GameConf.PLAYER_JUMP_BOOST));
				element.addPoints(20);

				// Kill the rocket itself
				this.addDamage(1);
			} else {
				// Give player damage
				element.addDamage(1);

				// Kill the rocket itself
				this.addDamage(1);

				Rocket.explosionParticles.spawn(this.scene, this.getPos());
			}
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
		return new Rocket(startPos);
	}

	@Override
	public int getID() {
		return 3;
	}

}
