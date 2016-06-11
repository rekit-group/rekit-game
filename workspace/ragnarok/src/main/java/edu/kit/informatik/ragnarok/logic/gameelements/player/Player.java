package edu.kit.informatik.ragnarok.logic.gameelements.player;

import org.eclipse.swt.graphics.GC;

import edu.kit.infomatik.config.c;
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
	}

	public void setPlayerState(PlayerState value) {
		this.playerState = value;
	}

	public PlayerState getPlayerState() {
		return this.playerState;
	}

	@Override
	public void render(GC gc) {
		Vec2D pos = this.getPos();
		gc.drawOval((int) ((pos.getX() - 0.5f) * c.pxPerUnit),
				(int) ((pos.getY() - 0.5f) * c.pxPerUnit), 1 * c.pxPerUnit,
				1 * c.pxPerUnit);
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
		// save new velocity
		this.setVel(newVel);

	}

	@Override
	public void damage(int damage) {
		// TODO implement this operation
		throw new UnsupportedOperationException("not implemented");

	}

	@Override
	public void collidedWith(Frame collision, Direction dir) {
		//System.out.println("COL " + dir.toString());

		switch (dir) {
		case UP:
			this.setPos(this.getPos().setY(
					collision.getBorder(dir) + this.getSize().getY() / 2.0f));
			break;
		case RIGHT:
			this.setPos(this.getPos().setX(
					collision.getBorder(dir) - this.getSize().getX() / 2.0f));
			break;
		case DOWN:
			this.setPos(this.getPos().setY(
					collision.getBorder(dir) - this.getSize().getY() / 2.0f));
			break;
		case LEFT:
			this.setPos(this.getPos().setX(
					collision.getBorder(dir) + this.getSize().getX() / 2.0f));
			break;
		}

	}

}
