package ragnarok.logic.gameelements.entities;

import ragnarok.config.GameConf;
import ragnarok.core.CameraTarget;
import ragnarok.core.GameGrid;
import ragnarok.core.Renderer;
import ragnarok.core.Team;
import ragnarok.logic.gameelements.particles.ParticleSpawner;
import ragnarok.logic.gameelements.particles.ParticleSpawnerOption;
import ragnarok.primitives.geometry.Direction;
import ragnarok.primitives.geometry.Frame;
import ragnarok.primitives.geometry.Vec;
import ragnarok.primitives.image.RGBAColor;
import ragnarok.primitives.time.Timer;

/**
 * The (maybe) most important {@link Entity} of the Game:<br>
 * The Player (most likely you).
 *
 * @author Dominik Fuchss
 * @author Angelo Aracri
 *
 */
public final class Player extends Entity implements CameraTarget {
	/**
	 * The start position.
	 */
	private Vec startPos;
	/**
	 * The particle spawner for taking damage.
	 */
	private ParticleSpawner damageParticles;
	/**
	 * The current walk direction.
	 */
	private Direction currentDirection;
	/**
	 * The player's camera offset.
	 */
	private float currentCameraOffset;

	/**
	 * A temporary renderer.
	 */
	private Renderer tmpRenderer;
	/**
	 * The temporary renderer's time.
	 */
	private Timer renderTimer;

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
		this.setPos(this.startPos);
		this.lives = GameConf.PLAYER_LIVES;
		this.points = 0;
		this.currentDirection = Direction.RIGHT;
		this.setVel(new Vec(0, 0));
		this.deleteMe = false;
		this.currentCameraOffset = 0;
		this.damageParticles = new ParticleSpawner();
		this.damageParticles.colorR = new ParticleSpawnerOption(222, 242, -10, 10);
		this.damageParticles.colorG = new ParticleSpawnerOption(138, 158, -10, 10);
		this.damageParticles.colorB = new ParticleSpawnerOption(6, 26, -10, 10);
		this.damageParticles.colorA = new ParticleSpawnerOption(255, 255, -255, -255);

	}

	@Override
	public void internalRender(GameGrid f) {
		if (this.renderTimer != null && !this.renderTimer.timeUp() && this.tmpRenderer != null) {
			this.tmpRenderer.render(f);
			this.renderTimer.logicLoop();
			return;
		}
		// determine if direction needs to be changed +- delta: 0.15
		if (this.getVel().getX() > 0.15) {
			this.currentDirection = Direction.RIGHT;
		} else if (this.getVel().getX() < -0.15) {
			this.currentDirection = Direction.LEFT;
		}

		f.drawRoundRectangle(this.getPos(), this.getSize(), new RGBAColor(232, 148, 16, 255), 0.45F, 0.45F);
		String src = this.currentDirection == Direction.RIGHT //
				? "mrRekt_glasses_right.png" // facing right
				: "mrRekt_glasses_left.png"; // facing left
		f.drawImage(this.getPos().addY(-0.025f * this.getVel().getY()), this.getSize(), src);

	}

	@Override
	public void collidedWith(Frame collision, Direction dir) {
		super.collidedWith(collision, dir);
		if (dir == Direction.UP) {
			this.setVel(this.getVel().setY(GameConf.PLAYER_BOTTOM_BOOST));
		}
	}

	@Override
	public void addDamage(int damage) {
		// spawn particles
		this.damageParticles.spawn(this.getScene(), this.getPos());
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

	@Override
	public byte getZ() {
		return 127;
	}

	/**
	 * Reset current camera offset.
	 */
	public final void resetCameraOffset() {
		this.currentCameraOffset = 0;
	}

	@Override
	public final float getCameraOffset() {
		// get maximum player x and adjust level offset
		float offsetNow = this.getPos().getX() - GameConf.PLAYER_CAMERA_OFFSET;
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
	public void setTemporaryApperance(Renderer r, long millis) {
		this.tmpRenderer = r;
		this.renderTimer = new Timer(millis);

	}

	/**
	 * Get the current direction.
	 *
	 * @return the current direction
	 */
	public final Direction getCurrentDirection() {
		return this.currentDirection;
	}
}
