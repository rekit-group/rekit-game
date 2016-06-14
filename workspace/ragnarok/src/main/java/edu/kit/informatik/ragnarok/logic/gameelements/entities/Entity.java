package edu.kit.informatik.ragnarok.logic.gameelements.entities;

import edu.kit.infomatik.config.c;
import edu.kit.informatik.ragnarok.logic.Direction;
import edu.kit.informatik.ragnarok.logic.Frame;
import edu.kit.informatik.ragnarok.logic.Vec2D;
import edu.kit.informatik.ragnarok.logic.gameelements.GameElement;

public abstract class Entity extends GameElement {
	
	
	protected int lifes;
	protected int points;
	
	/**
	 * <pre>
	 *           1..1     1..1
	 * Entity ------------------------- EntityState
	 *           entity        &gt;       entityState
	 * </pre>
	 */
	private EntityState entityState;
	
	public Entity(Vec2D startPos) {
		// Set to default state 
		this.setEntityState(new DefaultState());
		
		// Set initial position and velocity
		this.setPos(startPos);
		this.setVel(new Vec2D(0, 0));
	}
	
	public void setEntityState(EntityState value) {
		this.entityState = value;
	}

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
		if (deltaTime > c.logicDelta) {
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
		newVel = newVel.addY(c.g);
		// apply slowing down walk
		newVel = newVel.addX(-Math.signum(newVel.getX()) * c.playerStopAccel);
		// we dont want weird floating point velocities
		if (Math.abs(newVel.getX()) < 0.01) {
			newVel = newVel.setX(0);
		}
		// save new velocity
		this.setVel(newVel);
		// check if entity fell out of the world
		if (this.getPos().getY() > c.gridH) {
			this.addDamage(this.getLifes());
		}
	}
	
	@Override
	public void collidedWith(Frame collision, Direction dir) {
		// saving last position
		Vec2D lastPos = this.getLastPos();
		
		switch (dir) {
		case DOWN:
			this.setPos(this.getPos().setY(
					collision.getBorder(dir.getOpposite()) + this.getSize().getY() / 1.9f));
			// stop velocity in y dimension 
			this.setVel(this.getVel().setY(0));
			break;
		case RIGHT:
			this.setPos(this.getPos().setX(
					collision.getBorder(dir.getOpposite()) - this.getSize().getX() / 1.9f));
			// stop velocity in x dimension
			this.setVel(this.getVel().setX(0));
			break;
		case UP:
			this.setPos(this.getPos().setY(collision.getBorder(dir.getOpposite()) - this.getSize().getY() / 1.9f));
			if (this.getEntityState() instanceof JumpState) {
				this.setEntityState(new DefaultState());
			}
			// stop velocity in y dimension
			this.setVel(this.getVel().setY(0));
			break;
		case LEFT:
			this.setPos(this.getPos().setX(
					collision.getBorder(dir.getOpposite()) + this.getSize().getX() / 1.9f));
			// stop velocity in x dimension
			this.setVel(this.getVel().setX(0));
			break;
		}
		
		// resetting lastPos
		this.setLastPos(lastPos);
	}
	
	public int getZ() {
		return 1;
	}
}
