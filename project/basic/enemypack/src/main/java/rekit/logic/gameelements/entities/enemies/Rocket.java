package rekit.logic.gameelements.entities.enemies;

import org.fuchss.configuration.Configurable;
import org.fuchss.configuration.annotations.NoSet;
import org.fuchss.configuration.annotations.SetterInfo;

import rekit.config.GameConf;
import rekit.core.GameGrid;
import rekit.logic.gameelements.GameElement;
import rekit.logic.gameelements.entities.Entity;
import rekit.logic.gameelements.particles.ParticleSpawner;
import rekit.logic.gameelements.type.Enemy;
import rekit.primitives.geometry.Direction;
import rekit.primitives.geometry.Frame;
import rekit.primitives.geometry.Polygon;
import rekit.primitives.geometry.Vec;
import rekit.primitives.image.RGBColor;
import rekit.primitives.time.Timer;
import rekit.util.ReflectUtils.LoadMe;

/**
 *
 * As its name says, this Enemy is a rocket.
 *
 */
@LoadMe
@SetterInfo(res = "conf/rocket")
public final class Rocket extends Enemy implements Configurable {
	/**
	 * Prototype Constructor.
	 */
	public Rocket() {
		super();
	}

	/**
	 * The inner color of the rocket.
	 */
	private static RGBColor INNER_COLOR;
	/**
	 * The color of the front.
	 */
	private static RGBColor FRONT_COLOR;
	/**
	 * The outer color of the rocket.
	 */
	private static RGBColor OUTER_COLOR;
	/**
	 * The Particles's spawn time.
	 */
	private static float PARTICLE_SPAWN_TIME;
	
	/**
	 * The Rockets speed
	 */
	private static float SPEED;

	/**
	 * The particle spawner for the rocket's flight.
	 */
	private static ParticleSpawner sparkParticles;
	/**
	 * The particle spawner for the rocket's explosion.
	 */
	private static ParticleSpawner explosionParticles;

	/**
	 * The timer of the particles.
	 */
	@NoSet
	private Timer paricleTimer;

	/**
	 * Create a rocket by start position.
	 *
	 * @param startPos
	 *            the start position
	 */
	public Rocket(Vec startPos) {
		super(startPos, new Vec(), new Vec(1.8f, 0.5f));
		this.paricleTimer = new Timer((long) (1000 * Rocket.PARTICLE_SPAWN_TIME));
	}

	@Override
	public void internalRender(GameGrid f) {
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
	protected void innerLogicLoop() {
		// move ahead with player max speed
		this.setPos(this.getPos().addX(-Rocket.SPEED * this.deltaTime / 1000F));

		// spawn particles
		this.paricleTimer.logicLoop();
		// this.paricleTimer.removeTime(this.deltaTime);
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
				this.getScene().getPlayer().addPoints(20);
			} else {
				// Give player damage
				element.addDamage(1);
				Rocket.explosionParticles.spawn(this.getScene(), this.getPos());
			}
			// Kill the rocket itself
			this.destroy();
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
