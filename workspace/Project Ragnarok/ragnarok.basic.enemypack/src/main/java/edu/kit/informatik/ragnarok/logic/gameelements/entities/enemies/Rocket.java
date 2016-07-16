package edu.kit.informatik.ragnarok.logic.gameelements.entities.enemies;

import edu.kit.informatik.ragnarok.config.GameConf;
import edu.kit.informatik.ragnarok.logic.Field;
import edu.kit.informatik.ragnarok.logic.gameelements.GameElement;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.Entity;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.particles.ParticleSpawner;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.particles.ParticleSpawnerOption;
import edu.kit.informatik.ragnarok.logic.gameelements.type.Enemy;
import edu.kit.informatik.ragnarok.primitives.Direction;
import edu.kit.informatik.ragnarok.primitives.Frame;
import edu.kit.informatik.ragnarok.primitives.Polygon;
import edu.kit.informatik.ragnarok.primitives.TimeDependency;
import edu.kit.informatik.ragnarok.primitives.Vec;
import edu.kit.informatik.ragnarok.util.RGBColor;
import edu.kit.informatik.ragnarok.util.ReflectUtils.LoadMe;

/**
 *
 * As its name says, this Enemy is a rocket
 *
 */
@LoadMe
public class Rocket extends Enemy {
	/**
	 * Prototype Constructor
	 */
	public Rocket() {
		super();
	}

	/**
	 * The inner color of the rocket
	 */
	private final static RGBColor INNER_COLOR = new RGBColor(90, 90, 90);
	/**
	 * The color of the front
	 */
	private final static RGBColor FRONT_COLOR = new RGBColor(150, 30, 30);

	/**
	 * The outer color of the rocket
	 */
	private final static RGBColor OUTER_COLOR = new RGBColor(50, 50, 50);
	/**
	 * The Particles's spawn time
	 */
	private final static float PARTICLE_SPAWN_TIME = 0.05f;
	/**
	 * The particle spawner for the rocket
	 */
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
	/**
	 * The timer of the particles
	 */
	private TimeDependency paricleTimer;

	/**
	 * Create a rocket by start position
	 *
	 * @param startPos
	 *            the start position
	 */
	public Rocket(Vec startPos) {
		super(startPos, new Vec(), new Vec(1.8f, 0.5f));
		this.paricleTimer = new TimeDependency(Rocket.PARTICLE_SPAWN_TIME);
	}

	@Override
	public void internalRender(Field f) {
		// draw body
		f.drawRectangle(this.getPos(), this.getSize().multiply(0.8f, 0.6f), Rocket.INNER_COLOR);
		// draw spike at front
		Vec startPt = this.getPos().addX(-this.getSize().multiply(0.5f).getX());
		Vec[] relPts = new Vec[] { //
				new Vec(this.getSize().multiply(0.1f).getX(), -this.getSize().multiply(0.5f).getY()),
				new Vec(this.getSize().multiply(0.1f).getX(), this.getSize().multiply(0.5f).getY()), //
				new Vec() //
		};
		f.drawPolygon(new Polygon(startPt, relPts), Rocket.FRONT_COLOR, true);

		// draw stripes
		Vec stripeStart = this.getPos().addX(-this.getSize().multiply(0.4f - 0.05f - 0.025f).getX());
		for (int x = 0; x < 9; x++) {
			f.drawRectangle(stripeStart.addX(0.15f * x), this.getSize().multiply(0.05f, 0.75f), Rocket.OUTER_COLOR);
		}

		// draw drive at back
		startPt = this.getPos().addX(this.getSize().multiply(0.5f).getX()).addY(-this.getSize().multiply(0.5f).getY());
		relPts = new Vec[] { //
				new Vec(0, this.getSize().getY()), new Vec(-this.getSize().getX() * 0.1f, this.getSize().getY() * 0.8f),
				new Vec(-this.getSize().getX() * 0.1f, this.getSize().getY() * 0.2f), //
				new Vec() //
		};
		f.drawPolygon(new Polygon(startPt, relPts), Rocket.OUTER_COLOR, true);
	}

	@Override
	public void logicLoop(float deltaTime) {
		// move ahead with player max speed
		this.setPos(this.getPos().addX(-GameConf.PLAYER_WALK_MAX_SPEED * deltaTime));

		// spawn particles
		this.paricleTimer.removeTime(deltaTime);
		if (this.paricleTimer.timeUp()) {
			this.paricleTimer.reset();
			Rocket.sparkParticles.spawn(this.getScene(), this.getPos().addX(this.getSize().getX() / 2));
		}
	}

	@Override
	public void reactToCollision(GameElement element, Direction dir) {
		if (this.getTeam().isHostile(element.getTeam())) {

			if (dir == Direction.UP) {
				element.setVel(element.getVel().setY(GameConf.PLAYER_JUMP_BOOST));
				element.addPoints(20);
				// Kill the rocket itself
				this.destroy();
			} else {
				// Give player damage
				element.addDamage(1);
				// Kill the rocket itself
				this.destroy();
				Rocket.explosionParticles.spawn(this.getScene(), this.getPos());
			}
		}
	}

	@Override
	public void collidedWith(Frame collision, Direction dir) {
		// Do nothing, pass right through everything
	}

	@Override
	public Entity create(Vec startPos, String[] options) {
		return new Rocket(startPos);
	}

}
