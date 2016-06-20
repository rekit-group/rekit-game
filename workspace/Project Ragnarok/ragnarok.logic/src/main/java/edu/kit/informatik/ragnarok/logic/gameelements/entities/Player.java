package edu.kit.informatik.ragnarok.logic.gameelements.entities;

import edu.kit.informatik.ragnarok.config.GameConf;
import edu.kit.informatik.ragnarok.logic.gameelements.Field;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.particles.ParticleSpawner;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.particles.ParticleSpawnerOption;
import edu.kit.informatik.ragnarok.primitives.Direction;
import edu.kit.informatik.ragnarok.primitives.Frame;
import edu.kit.informatik.ragnarok.primitives.Vec2D;

public class Player extends Entity implements CameraTarget {

	private Vec2D startPos;

	private ParticleSpawner damageParticles;

	private float currentCameraOffset;

	public Player(Vec2D startPos) {
		super(startPos);
		this.startPos = startPos;
		this.init();
	}

	public void init() {
		this.setPos(this.startPos);
		this.lifes = GameConf.playerLifes;
		this.points = 0;
		this.setTeam(0);
		this.currentDirection = Direction.RIGHT;
		this.setVel(new Vec2D(0, 0));
		this.deleteMe = false;
		this.currentCameraOffset = 0;

		this.damageParticles = new ParticleSpawner();
		this.damageParticles.colorR = new ParticleSpawnerOption(222, 242, -10, 10);
		this.damageParticles.colorG = new ParticleSpawnerOption(138, 158, -10, 10);
		this.damageParticles.colorB = new ParticleSpawnerOption(6, 26, -10, 10);
		this.damageParticles.colorA = new ParticleSpawnerOption(255, 255, -255, -255);

	}

	@Override
	public Vec2D getSize() {
		return new Vec2D(0.8f, 0.8f);
	}

	private Direction currentDirection;

	@Override
	public void render(Field f) {
		// determine if direction needs to be changed
		if (this.getVel().getX() > 0) {
			this.currentDirection = Direction.RIGHT;
		} else if (this.getVel().getX() < 0) {
			this.currentDirection = Direction.LEFT;
		}

		// draw player background image
		f.drawImage(this.getPos(), this.getSize(), "mrRekt_background.png");
		// draw player glasses image
		String src = this.currentDirection == Direction.RIGHT ? "mrRekt_glasses_right.png" // When
																							// facing
																							// right
				: "mrRekt_glasses_left.png"; // When facing left
		f.drawImage(this.getPos().addY(-0.025f * this.getVel().getY()), this.getSize(), src);

	}

	@Override
	public void collidedWith(Frame collision, Direction dir) {
		super.collidedWith(collision, dir);
		if (dir == Direction.UP) {
			this.setVel(this.getVel().setY(GameConf.playerBottomBoost));
		}
	}

	@Override
	public void addDamage(int damage) {

		// spawn particles
		this.damageParticles.spawn(this.getGameModel(), this.getPos());

		// Do usual life logic
		super.addDamage(damage);
	}

	@Override
	public int getZ() {
		return 10;
	}

	@Override
	public float getCameraOffset() {
		// get maximum player x and adjust level offset
		float offsetNow = this.getPos().getX() - GameConf.playerCameraOffset;
		if (offsetNow > this.currentCameraOffset) {
			this.currentCameraOffset = offsetNow;
		}
		return this.currentCameraOffset;
	}

	@Override
	public Entity create(Vec2D startPos) {
		throw new UnsupportedOperationException("Create not supported for Players");
	}

}
