package edu.kit.informatik.ragnarok.logic.gameelements.entities.enemies;

import edu.kit.informatik.ragnarok.config.GameConf;
import edu.kit.informatik.ragnarok.logic.Field;
import edu.kit.informatik.ragnarok.logic.gameelements.GameElement;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.Entity;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.particles.ParticleSpawner;
import edu.kit.informatik.ragnarok.logic.gameelements.type.Enemy;
import edu.kit.informatik.ragnarok.primitives.geometry.Direction;
import edu.kit.informatik.ragnarok.primitives.geometry.Frame;
import edu.kit.informatik.ragnarok.primitives.geometry.Polygon;
import edu.kit.informatik.ragnarok.primitives.geometry.Vec;
import edu.kit.informatik.ragnarok.primitives.image.RGBColor;
import edu.kit.informatik.ragnarok.primitives.time.Timer;
import edu.kit.informatik.ragnarok.util.ReflectUtils.LoadMe;
import edu.kit.informatik.ragnarok.visitor.NoVisit;
import edu.kit.informatik.ragnarok.visitor.VisitInfo;
import edu.kit.informatik.ragnarok.visitor.Visitable;

/**
 *
 * As its name says, this Enemy is a rocket
 *
 */
@LoadMe
@VisitInfo(res = "conf/rocket", visit = true)
public class Rocket extends Enemy implements Visitable {
	/**
	 * Prototype Constructor
	 */
	public Rocket() {
		super();
	}

	/**
	 * The inner color of the rocket
	 */
	private static RGBColor INNER_COLOR;
	/**
	 * The color of the front
	 */
	private static RGBColor FRONT_COLOR;

	/**
	 * The outer color of the rocket
	 */
	private static RGBColor OUTER_COLOR;
	/**
	 * The Particles's spawn time
	 */
	private static float PARTICLE_SPAWN_TIME;

	// Explosion particles
	// private static ParticleSpawnerOption angleExp;
	// private static ParticleSpawnerOption colorRExp;
	// private static ParticleSpawnerOption colorGExp;
	// private static ParticleSpawnerOption colorBExp;
	// private static ParticleSpawnerOption colorAExp;
	// private static float timeMinExp;
	// private static float timeMaxExp;
	// private static int amountMinExp;
	// private static int amountMaxExp;
	// private static ParticleSpawnerOption speedExp;

	/**
	 * The particle spawner for the rocket's flight
	 */
	private static ParticleSpawner sparkParticles = null;
	/**
	 * The particle spawner for the rocket's explosion
	 */
	private static ParticleSpawner explosionParticles = null;

	/**
	 * The timer of the particles
	 */
	@NoVisit
	private Timer paricleTimer;

	/**
	 * Create a rocket by start position
	 *
	 * @param startPos
	 *            the start position
	 */
	public Rocket(Vec startPos) {
		super(startPos, new Vec(), new Vec(1.8f, 0.5f));
		this.paricleTimer = new Timer(Rocket.PARTICLE_SPAWN_TIME);
	}

	@Override
	public void internalRender(Field f) {
		// draw body
		f.drawRectangle(this.getPos(), this.getSize().scalar(0.8f, 0.6f), Rocket.INNER_COLOR);
		// draw spike at front
		Vec startPt = this.getPos().addX(-this.getSize().scalar(0.5f).getX());
		Vec[] relPts = new Vec[] { //
				new Vec(this.getSize().scalar(0.1f).getX(), -this.getSize().scalar(0.5f).getY()),
				new Vec(this.getSize().scalar(0.1f).getX(), this.getSize().scalar(0.5f).getY()), //
				new Vec() //
		};
		f.drawPolygon(new Polygon(startPt, relPts), Rocket.FRONT_COLOR, true);

		// draw stripes
		Vec stripeStart = this.getPos().addX(-this.getSize().scalar(0.4f - 0.05f - 0.025f).getX());
		for (int x = 0; x < 9; x++) {
			f.drawRectangle(stripeStart.addX(0.15f * x), this.getSize().scalar(0.05f, 0.75f), Rocket.OUTER_COLOR);
		}

		// draw drive at back
		startPt = this.getPos().addX(this.getSize().scalar(0.5f).getX()).addY(-this.getSize().scalar(0.5f).getY());
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
				element.setVel(element.getVel().setY(GameConf.PLAYER_KILL_BOOST));
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

	// @AfterVisit
	// private static void afterVisit() {
	//
	// Rocket.explosionParticles = new ParticleSpawner();
	//
	// // Explosion
	// Rocket.explosionParticles.angle = Rocket.angleExp;
	// Rocket.explosionParticles.colorR = Rocket.colorRExp;
	// Rocket.explosionParticles.colorG = Rocket.colorGExp;
	// Rocket.explosionParticles.colorB = Rocket.colorBExp;
	// Rocket.explosionParticles.colorA = Rocket.colorAExp;
	// Rocket.explosionParticles.timeMin = Rocket.timeMinExp;
	// Rocket.explosionParticles.timeMax = Rocket.timeMaxExp;
	// Rocket.explosionParticles.amountMin = Rocket.amountMinExp;
	// Rocket.explosionParticles.amountMax = Rocket.amountMaxExp;
	// Rocket.explosionParticles.speed = Rocket.speedExp;
	//
	// }

}
