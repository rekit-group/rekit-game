package edu.kit.informatik.ragnarok.logic.gameelements.entities;

import edu.kit.informatik.ragnarok.config.GameConf;
import edu.kit.informatik.ragnarok.logic.Field;
import edu.kit.informatik.ragnarok.logic.gameelements.Team;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.particles.ParticleSpawner;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.particles.ParticleSpawnerOption;
import edu.kit.informatik.ragnarok.primitives.Direction;
import edu.kit.informatik.ragnarok.primitives.Frame;
import edu.kit.informatik.ragnarok.primitives.Vec;
import edu.kit.informatik.ragnarok.util.RGBAColor;

/**
 * The (maybe) most important {@link Entity} of the Game:<br>
 * The Player (most likely you)
 *
 * @author Dominik Fuchß
 * @author Angelo Aracri
 *
 */
public final class Player extends Entity implements CameraTarget {
	/**
	 * The start position
	 */
	private Vec startPos;
	/**
	 * The particle spawner for taking damage
	 */
	private ParticleSpawner damageParticles;
	/**
	 * The current walk direction
	 */
	private Direction currentDirection;
	/**
	 * The player's camera offset
	 */
	private float currentCameraOffset;

	/**
	 * Create a player by start position
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
	 * Initialize the player
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
	public void internalRender(Field f) {
		// determine if direction needs to be changed
		if (this.getVel().getX() > 0) {
			this.currentDirection = Direction.RIGHT;
		} else if (this.getVel().getX() < 0) {
			this.currentDirection = Direction.LEFT;
		}

		f.drawRoundRectangle(this.getPos(), this.getSize(), new RGBAColor(232, 148, 16, 255), 0.4F, 0.4F);
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

	@Override
	public int getOrderZ() {
		return 10;
	}

	/**
	 * Reset current camera offset
	 */
	public void resetCameraOffset() {
		this.currentCameraOffset = 0;
	}

	@Override
	public float getCameraOffset() {
		// get maximum player x and adjust level offset
		float offsetNow = this.getPos().getX() - GameConf.PLAYER_CAMERA_OFFSET;
		if (offsetNow > this.currentCameraOffset) {
			this.currentCameraOffset = offsetNow;
		}
		return this.currentCameraOffset;
	}

}
