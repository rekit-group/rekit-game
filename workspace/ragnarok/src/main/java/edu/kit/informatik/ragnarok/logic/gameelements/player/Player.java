package edu.kit.informatik.ragnarok.logic.gameelements.player;

import org.eclipse.swt.graphics.RGB;

import edu.kit.infomatik.config.c;
import edu.kit.informatik.ragnarok.gui.Field;
import edu.kit.informatik.ragnarok.logic.Direction;
import edu.kit.informatik.ragnarok.logic.Frame;
import edu.kit.informatik.ragnarok.logic.Vec2D;
import edu.kit.informatik.ragnarok.logic.gameelements.Entity;

public class Player extends Entity {
	/**
	 * <pre>
	 *           1..1     1..1
	 * Player ------------------------- PlayerState
	 *           player        &gt;       playerState
	 * </pre>
	 */
	private PlayerState playerState;
	
	public Player(Vec2D startPos) {
		this.setPos(startPos);
		this.setVel(new Vec2D(0, 0));
		this.setPlayerState(new DefaultState());
		this.lifes = c.playerLifes;
		this.setTeam(0);
	}

	public void setPlayerState(PlayerState value) {
		this.playerState = value;
	}

	public PlayerState getPlayerState() {
		return this.playerState;
	}
	
	public Vec2D getSize() {
		return new Vec2D(0.8f, 0.8f);
	}

	@Override
	public void render(Field f) {
		f.drawCircle(this.getPos(), this.getSize(), new RGB(200, 50, 0));
	}

	@Override
	public void logicLoop(float deltaTime) {
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
		// check if player fell out of the world
		if (this.getPos().getY() > c.gridH) {
			this.addDamage(this.getLifes());
		}
	}

	@Override
	public void collidedWith(Frame collision, Direction dir) {
		if (dir != Direction.DOWN) {
			System.out.println("Collision " + dir.toString() + " with " + collision.toString() + " while at " + this.getPos().toString());
		}
		
		// saving last position
		Vec2D lastPos = this.getLastPos();
		
		switch (dir) {
		case UP:
			this.setPos(this.getPos().setY(
					collision.getBorder(dir) + this.getSize().getY() / 1.9f));
			// if he was jumping, stop his jump boost
			if (this.getVel().getY() < 0) {
				this.setVel(this.getVel().setY(0));
			}
			break;
		case RIGHT:
			this.setPos(this.getPos().setX(
					collision.getBorder(dir.getOpposite()) - this.getSize().getX() / 1.9f));
			break;
		case DOWN:
			this.setPos(this.getPos().setY(
					collision.getBorder(dir) - this.getSize().getY() / 1.9f));
			if (this.getPlayerState() instanceof JumpState) {
				this.setPlayerState(new DefaultState());
			}
			break;
		case LEFT:
			this.setPos(this.getPos().setX(
					collision.getBorder(dir.getOpposite()) + this.getSize().getX() / 1.9f));
			break;
		}
		
		// resetting lastPos
		this.setLastPos(lastPos);
	}

}
