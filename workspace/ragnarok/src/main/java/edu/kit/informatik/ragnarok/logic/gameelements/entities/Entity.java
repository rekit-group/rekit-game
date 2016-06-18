package edu.kit.informatik.ragnarok.logic.gameelements.entities;

import edu.kit.informatik.ragnarok.config.GameConf;
import edu.kit.informatik.ragnarok.logic.gameelements.GameElement;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.state.DefaultState;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.state.EntityState;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.state.JumpState;
import edu.kit.informatik.ragnarok.primitives.Direction;
import edu.kit.informatik.ragnarok.primitives.Frame;
import edu.kit.informatik.ragnarok.primitives.Vec2D;

public abstract class Entity extends GameElement {
	
	/**
	 * The amount of lifes the Entity has.
	 * Upon reaching 0 lifes, he dies
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
	
	/**
	 * Constructor that initializes attributes and takes a start position
	 * @param startPos the position this entity shall be in
	 */
	public Entity(Vec2D startPos) {
		// Set to default state 
		this.setEntityState(new DefaultState(this));
		
		// Set initial position and velocity
		this.setPos(startPos);
		this.setVel(new Vec2D(0, 0));
	}
	
	/**
	 * Set the Entities <i>EntitiyState</i> that determines its jump behavior
	 * @param value
	 */
	public void setEntityState(EntityState value) {
		this.entityState = value;
	}
	
	/**
	 * Return the Entities current <i>EntitiyState</i> that determines its jump behavior.
	 * @return
	 */
	public EntityState getEntityState() {
		return this.entityState;
	}
	
	@Override
	public void addDamage(int damage) {
		this.lifes -= damage;
		if (this.lifes <= 0) {
			this.lifes = 0;
			this.destroy();
		}
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
		if (deltaTime > GameConf.logicDelta) {
			// ..so recursively split work up into smaller parts
			logicLoop(deltaTime / 2);
			logicLoop(deltaTime / 2);
			return;
		}
		
		// calculate new position
		// s1 = s0 + v*t because physics, thats why!
		this.setPos(this.getPos().add(this.getVel().multiply(deltaTime)));
		
		Vec2D newVel = this.getVel();
		// apply gravity
		newVel = newVel.addY(GameConf.g);
		// apply slowing down walk
		newVel = newVel.addX(-Math.signum(newVel.getX()) * GameConf.playerStopAccel);
		// we dont want weird floating point velocities
		if (Math.abs(newVel.getX()) < 0.01) {
			newVel = newVel.setX(0);
		}
		// save new velocity
		this.setVel(newVel);
		// check if entity fell out of the world
		if (this.getPos().getY() > GameConf.gridH) {
			this.addDamage(this.getLifes());
		}
	}
	
	@Override
	public void collidedWith(Frame collision, Direction dir) {
		// saving last position
		Vec2D lastPos = this.getLastPos();
		
		int signum = 1;
		switch (dir) {
		case LEFT:
			signum = -1;
		case RIGHT:
			// move entities right side to collisions left side / vice versa
			float newX = collision.getBorder(dir) + signum * this.getSize().getX() / 1.9f;
			this.setPos(this.getPos().setX(newX));
			// stop velocity in x dimension
			this.setVel(this.getVel().setX(0));
			break;
		case UP:
			signum = -1;
			if (this.getEntityState() instanceof JumpState) {
				this.setEntityState(new DefaultState(this));
			}
		case DOWN:
			// move entities lower side to collisions top side / vice versa
			float newY = collision.getBorder(dir.getOpposite()) + signum * this.getSize().getY() / 1.9f;
			this.setPos(this.getPos().setY(newY));
			// stop velocity in y dimension 
			this.setVel(this.getVel().setY(0));
			break;
		}
		
		// resetting lastPos
		this.setLastPos(lastPos);
	}
	
	@Override
	public int getZ() {
		return 1;
	}
}
