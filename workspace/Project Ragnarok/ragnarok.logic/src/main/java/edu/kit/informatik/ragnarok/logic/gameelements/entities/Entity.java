package edu.kit.informatik.ragnarok.logic.gameelements.entities;

import edu.kit.informatik.ragnarok.config.GameConf;
import edu.kit.informatik.ragnarok.logic.gameelements.Field;
import edu.kit.informatik.ragnarok.logic.gameelements.GameElement;
import edu.kit.informatik.ragnarok.logic.gameelements.Team;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.state.DefaultState;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.state.EntityState;
import edu.kit.informatik.ragnarok.primitives.Direction;
import edu.kit.informatik.ragnarok.primitives.Frame;
import edu.kit.informatik.ragnarok.primitives.TimeDependency;
import edu.kit.informatik.ragnarok.primitives.Vec;

public abstract class Entity extends GameElement {

	/**
	 * The amount of lifes the Entity has. Upon reaching 0 lifes, he dies
	 */
	protected int lifes = 1;

	/**
	 * The amount of points the Entity has scored
	 */
	protected int points = 0;

	/**
	 * The current State the Entity is in and determines the jump behavior
	 */
	private EntityState entityState;

	protected TimeDependency invincibility = null;

	/**
	 * Constructor that initializes attributes and takes a start position
	 *
	 * @param startPos
	 *            the position this entity shall be in
	 */

	public Entity(Team t, Vec startPos) {
		super(t);
		if (startPos == null) {
			return;
		}
		// Set to default state
		this.setEntityState(new DefaultState(this));

		// Set initial position and velocity
		this.setPos(startPos);
		this.setVel(new Vec(0, 0));
		this.points = 0;

	}

	/**
	 * Set the Entities <i>EntitiyState</i> that determines its jump behavior
	 *
	 * @param value
	 */
	public void setEntityState(EntityState value) {
		this.entityState = value;
	}

	/**
	 * Return the Entities current <i>EntitiyState</i> that determines its jump
	 * behavior.
	 *
	 * @return
	 */
	public EntityState getEntityState() {
		return this.entityState;
	}

	@Override
	public void addDamage(int damage) {
		// no damage taken while invincibility time is not up
		if (damage > 0 && this.invincibility != null && !this.invincibility.timeUp()) {
			return;
		}

		this.lifes -= damage;

		if (damage > 0) {
			this.invincibility = new TimeDependency(2);
		}

		if (this.lifes <= 0) {
			this.lifes = 0;
			this.destroy();
		}
	}

	public void setLifes(int lifes) {
		this.lifes = lifes;
	}

	@Override
	public int getLifes() {
		return this.lifes;
	}

	@Override
	public void addPoints(int points) {
		this.points += points;
	}

	@Override
	public int getPoints() {
		return this.points;
	}

	@Override
	public void logicLoop(float deltaTime) {

		// if delta is too big, clipping likely to appear...
		if (deltaTime > GameConf.LOGIC_DELTA) {
			// ..so recursively split work up into smaller parts
			this.logicLoop(deltaTime / 2);
			this.logicLoop(deltaTime / 2);
			return;
		}

		if (this.invincibility != null) {
			this.invincibility.removeTime(deltaTime);
		}

		this.getEntityState().logicLoop(deltaTime);

		// calculate new position
		// s1 = s0 + v*t because physics, thats why!
		this.setPos(this.getPos().add(this.getVel().multiply(deltaTime)));

		Vec newVel = this.getVel();
		// apply gravity
		newVel = newVel.addY(GameConf.G);
		// apply slowing down walk
		newVel = newVel.addX(-Math.signum(newVel.getX()) * GameConf.PLAYER_STOP_ACCEL);
		// we dont want weird floating point velocities
		if (Math.abs(newVel.getX()) < 0.01) {
			newVel = newVel.setX(0);
		}
		// save new velocity
		this.setVel(newVel);
		// check if entity fell out of the world
		if (this.getPos().getY() > GameConf.GRID_H) {
			this.addDamage(this.getLifes());
		}
	}

	@Override
	public void collidedWith(Frame collision, Direction dir) {
		// saving last position
		Vec lastPos = this.getLastPos();

		int signum = 1;
		switch (dir) {
		case LEFT:
			signum = -1;
		case RIGHT:
			// move entities right side to collisions left side / vice versa
			float newX = collision.getBorder(dir) + signum * this.size.getX() / 1.9f;
			this.setPos(this.getPos().setX(newX));
			// stop velocity in x dimension
			this.setVel(this.getVel().setX(0));
			break;
		case UP:
			signum = -1;
			this.getEntityState().floorCollision();
		case DOWN:
			// move entities lower side to collisions top side / vice versa
			float newY = collision.getBorder(dir.getOpposite()) + signum * this.size.getY() / 1.9f;
			this.setPos(this.getPos().setY(newY));
			// stop velocity in y dimension
			this.setVel(this.getVel().setY(0));
			break;
		}

		// resetting lastPos
		this.setLastPos(lastPos);
	}

	public Entity create(Vec startPos) {
		throw new UnsupportedOperationException("Create not supported for " + this.getClass().getSimpleName() + "s");
	}

	@Override
	public int getOrderZ() {
		return 1;
	}

	@Override

	public final void render(Field f) {
		if (this.isVisible()) {
			this.internRender(f);
		}
	}

	public abstract void internRender(Field f);

	protected boolean isVisible() {
		if (this.invincibility != null && !this.invincibility.timeUp()) {
			return (int) (this.invincibility.getProgress() * 20) % 2 == 0;
		}
		return true;
	}
}
