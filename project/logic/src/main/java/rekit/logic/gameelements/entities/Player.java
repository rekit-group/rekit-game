package rekit.logic.gameelements.entities;

import java.util.function.Consumer;

import org.fuchss.configuration.Configurable;
import org.fuchss.configuration.annotations.NoSet;
import org.fuchss.configuration.annotations.SetterInfo;

import rekit.core.CameraTarget;
import rekit.core.GameGrid;
import rekit.core.Team;
import rekit.logic.gameelements.particles.ParticleSpawner;
import rekit.logic.gameelements.particles.ParticleSpawnerOption;
import rekit.primitives.geometry.Direction;
import rekit.primitives.geometry.Frame;
import rekit.primitives.geometry.Vec;
import rekit.primitives.image.RGBAColor;
import rekit.primitives.time.Timer;

/**
 * The (maybe) most important {@link Entity} of the Game:<br>
 * The Player (most likely you).
 *
 * @author Dominik Fuchss
 * @author Angelo Aracri
 *
 */
@SetterInfo(res = "conf/player")
public final class Player extends Entity implements CameraTarget, Configurable {
	/**
	 * The default camera offset for a player.
	 */
	public static float CAMERA_OFFSET;
	/**
	 * The player's walk acceleration.
	 */
	public static float WALK_ACCEL;
	/**
	 * The player's stop acceleration.
	 */
	public static float STOP_ACCEL;
	/**
	 * The player's walk max speed.
	 */
	public static float WALK_MAX_SPEED;
	/**
	 * The player's jump boost.
	 */
	public static float JUMP_BOOST;
	/**
	 * The player's boost upon jumping on an enemy.
	 */
	public static float KILL_BOOST;
	/**
	 * The player's maximum jump time in millis.
	 */
	public static long JUMP_TIME;
	/**
	 * The player's bottom boost (used when colliding from bottom).
	 */
	public static float FLOOR_BOOST;
	/**
	 * The player's default amount of lives.
	 */
	public static int LIVES;
	/**
	 * The start position.
	 */
	@NoSet
	private Vec startPos;
	/**
	 * The particle spawner for taking damage.
	 */
	@NoSet
	private ParticleSpawner damageParticles;
	/**
	 * The current walk direction.
	 */
	@NoSet
	private Direction currentDirection;
	/**
	 * The player's camera offset.
	 */
	@NoSet
	private float currentCameraOffset;

	/**
	 * A temporary renderer.
	 */
	@NoSet
	private Consumer<GameGrid> tmpRenderer;
	/**
	 * The temporary renderer's time.
	 */
	@NoSet
	private Timer renderTimer;
	/**
	 * The points of the player.
	 */
	@NoSet
	private int points;

	/**
	 * Create a player by start position.
	 *
	 * @param startPos
	 *            the start position
	 */
	public Player(Vec startPos) {
		super(startPos, new Vec(), new Vec(0.8f, 0.8f), Team.PLAYER);
		this.startPos = startPos;
		this.init();
	}

	/**
	 * Initialize the player.
	 */
	public void init() {
		this.deleteMe = false;
		this.setPos(this.startPos);
		this.lives = Player.LIVES;
		this.points = 0;
		this.currentDirection = Direction.RIGHT;
		this.setVel(new Vec(0, 0));
		this.currentCameraOffset = 0;
		this.damageParticles = new ParticleSpawner();
		this.damageParticles.amountMin = 30;
		this.damageParticles.amountMax = 40;
		this.damageParticles.colorR = new ParticleSpawnerOption(222, 242, -10, 10);
		this.damageParticles.colorG = new ParticleSpawnerOption(138, 158, -10, 10);
		this.damageParticles.colorB = new ParticleSpawnerOption(6, 26, -10, 10);
		this.damageParticles.colorA = new ParticleSpawnerOption(255, 255, -255, -255);

	}

	@Override
	public void internalRender(GameGrid f) {
		if (this.renderTimer != null && !this.renderTimer.timeUp() && this.tmpRenderer != null) {
			this.tmpRenderer.accept(f);
			this.renderTimer.logicLoop();
			return;
		}
		
		// determine if direction needs to be changed +- delta: 0.15
		if (this.getVel().x > 0.15) {
			this.currentDirection = Direction.RIGHT;
		} else if (this.getVel().x < -0.15) {
			this.currentDirection = Direction.LEFT;
		}

		f.drawRoundRectangle(this.getPos(), this.getSize(), new RGBAColor(232, 148, 16, 255), 0.45F, 0.45F);
		String src = this.currentDirection == Direction.RIGHT //
				? "mrRekt_glasses_right.png" // facing right
				: "mrRekt_glasses_left.png"; // facing left
		f.drawImage(this.getPos().addY(-0.025f * this.getVel().y), this.getSize(), src);

	}

	@Override
	public void collidedWith(Frame collision, Direction dir) {
		super.collidedWith(collision, dir);
		if (dir == Direction.UP) {
			this.setVel(this.getVel().setY(Player.FLOOR_BOOST));
		}
	}

	@Override
	public void addDamage(int damage) {
		if (this.invincibility == null || this.invincibility.timeUp()) {
			// spawn particles
			this.damageParticles.spawn(this.getScene(), this.getPos());
		}
		// Do usual life logic
		super.addDamage(damage);
	}

	/**
	 * Set Player invincible for a time.
	 *
	 * @param millis
	 *            the millis
	 */
	public void setInvincible(long millis) {
		this.invincibility = new Timer(millis);
	}

	/**
	 * Reset current camera offset.
	 */
	public void resetCameraOffset() {
		this.currentCameraOffset = 0;
	}

	@Override
	public float getCameraOffset() {
		
		// get maximum player x and adjust level offset
		float offsetNow = this.getPos().x - Player.CAMERA_OFFSET;
		if (offsetNow > this.currentCameraOffset) {
			this.currentCameraOffset = offsetNow;
		}
		return this.currentCameraOffset;
	}

	/**
	 * Set a temporary renderer for the Player.
	 *
	 * @param r
	 *            the renderer
	 * @param millis
	 *            the time
	 */
	public void setTemporaryApperance(Consumer<GameGrid> r, long millis) {
		this.tmpRenderer = r;
		this.renderTimer = new Timer(millis);

	}

	/**
	 * Get the current direction.
	 *
	 * @return the current direction
	 */
	public Direction getCurrentDirection() {
		return this.currentDirection;
	}

	/**
	 * Adds points to the Players score.
	 *
	 * @param points
	 *            the points to add (or subtract, iff negative)
	 */
	public void addPoints(int points) {
		if (points < 0 && this.invincibility != null && !this.invincibility.timeUp()) {
			return;
		}

		this.points += points;
	}

	/**
	 * Getter for the collected Points of the Player.
	 *
	 * @return the points of the Player
	 */
	public int getPoints() {
		return this.points;
	}
	
	@Override
	public void killBoost() {
		this.setVel(this.getVel().setY(Player.KILL_BOOST));
	}
}
