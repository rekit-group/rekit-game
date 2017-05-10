package rekit.logic.gameelements.entities.enemies;

import org.fuchss.configuration.Configurable;
import org.fuchss.configuration.annotations.NoSet;
import org.fuchss.configuration.annotations.SetterInfo;

import rekit.config.GameConf;
import rekit.core.GameGrid;
import rekit.logic.gameelements.GameElement;
import rekit.logic.gameelements.entities.Player;
import rekit.logic.gameelements.particles.ParticleSpawner;
import rekit.logic.gameelements.type.Enemy;
import rekit.primitives.geometry.Direction;
import rekit.primitives.geometry.Frame;
import rekit.primitives.geometry.Polygon;
import rekit.primitives.geometry.Vec;
import rekit.primitives.image.RGBAColor;
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
	private static RGBAColor INNER_COLOR;
	/**
	 * The color of the front.
	 */
	private static RGBAColor FRONT_COLOR;
	/**
	 * The outer color of the rocket.
	 */
	private static RGBAColor OUTER_COLOR;
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
	 * The score the {@link Player} receives upon killing this {@link Enemy}
	 */
	public static int POINTS;

	/**
	 * The timer of the particles.
	 */
	@NoSet
	private Timer paricleTimer;

	@NoSet
	private Direction direction;

	@NoSet
	private float currentSpeed = Rocket.SPEED;

	/**
	 * Create a rocket by start position.
	 *
	 * @param startPos
	 *            the start position
	 */
	public Rocket(Vec startPos) {
		super(startPos, new Vec(), new Vec(1.8f, 0.5f));
		this.direction = Direction.LEFT;
		this.paricleTimer = new Timer((long) (1000 * Rocket.PARTICLE_SPAWN_TIME));
	}

	@Override
	public void internalRender(GameGrid f) {
		// draw body
		f.drawRectangle(this.getPos(), this.getSize().scalar(0.8f, 0.6f), Rocket.INNER_COLOR);
		// draw spike at front
		Vec startPt = this.getPos().addX(this.getXSignum() * this.getSize().scalar(0.5f).x);
		Vec[] relPts = new Vec[] { //
				new Vec(-this.getXSignum() * this.getSize().scalar(0.1f).x, -this.getSize().scalar(0.5f).y),
				new Vec(-this.getXSignum() * this.getSize().scalar(0.1f).x, this.getSize().scalar(0.5f).y), //
				new Vec() //
		};
		f.drawPolygon(new Polygon(startPt, relPts), Rocket.FRONT_COLOR, true);

		// draw stripes
		Vec stripeStart = this.getPos().addX(this.getSize().scalar(this.getXSignum() * (0.05f + 0.025f - 0.4f)).x);
		for (int x = 0; x < 9; x++) {
			f.drawRectangle(stripeStart.addX(this.getXSignum() * 0.15f * x), this.getSize().scalar(0.05f, 0.75f), Rocket.OUTER_COLOR);
		}

		// draw drive at back
		startPt = this.getPos().addX(this.getSize().scalar(-this.getXSignum() * 0.5f).x).addY(-this.getSize().scalar(0.5f).y);
		relPts = new Vec[] { //
				new Vec(0, this.getSize().y), new Vec(this.getXSignum() * this.getSize().x * 0.1f, this.getSize().y * 0.8f),
				new Vec(this.getXSignum() * this.getSize().x * 0.1f, this.getSize().y * 0.2f), //
				new Vec() //
		};
		f.drawPolygon(new Polygon(startPt, relPts), Rocket.OUTER_COLOR, true);
	}

	@Override
	protected void innerLogicLoop() {
		// move ahead with player max speed
		this.setPos(this.getPos().addX(this.getXSignum() * this.currentSpeed * this.deltaTime / 1000F));

		// spawn particles
		this.paricleTimer.logicLoop();
		// this.paricleTimer.removeTime(this.deltaTime);
		if (this.paricleTimer.timeUp()) {
			this.paricleTimer.reset();
			Rocket.sparkParticles.spawn(this.getScene(), this.getPos().addX(-this.getXSignum() * this.getSize().x / 2));
		}
	}

	/**
	 * Sets the speed of the rocket
	 *
	 * @param speed
	 *            the new speed
	 */
	public void setSpeed(float speed) {
		this.currentSpeed = speed;
	}

	/**
	 * Resets the Rockets speed to the default speed as specified by
	 * configuration.
	 */
	public void resetSpeed() {
		this.currentSpeed = Rocket.SPEED;
	}

	@Override
	public void reactToCollision(GameElement element, Direction dir) {
		if (this.getTeam().isHostile(element.getTeam())) {
			if (dir == Direction.UP) {
				element.killBoost();
				// give the player points
				this.getScene().getPlayer().addPoints(Rocket.POINTS);
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
	public Rocket create(Vec startPos, String... options) {
		Rocket inst = new Rocket(startPos);

		// if option 0 is given: set defined direction
		if (options.length >= 1 && options[0] != null && options[0].matches("(\\+|-)?[0-3]+")) {
			int opt = Integer.parseInt(options[0]);
			if (opt >= 0 && opt < Direction.values().length) {
				inst.setDirection(Direction.values()[opt]);
			} else {
				GameConf.GAME_LOGGER.error("RektKiller was supplied invalid option " + options[0] + " at index 0 for Direction");
			}
		}

		return inst;
	}

	private int getXSignum() {
		return (this.direction == Direction.RIGHT) ? 1 : -1;
	}

	private void setDirection(Direction direction) {
		this.direction = direction;
	}

}
