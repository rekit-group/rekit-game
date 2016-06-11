package edu.kit.informatik.ragnarok.logic.gameelements;

import org.eclipse.swt.graphics.GC;

import edu.kit.infomatik.config.c;
import edu.kit.informatik.ragnarok.logic.PlayerState;
import edu.kit.informatik.ragnarok.logic.Vec2D;

public class Player extends Entity {
	/**
	 * <pre>
	 *           1..1     1..1
	 * Player ------------------------- PlayerState
	 *           player        &gt;       playerState
	 * </pre>
	 */
	private PlayerState playerState;

	private Vec2D lastPos; 
	
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
		// save position in case we have to reset
		this.lastPos = new Vec2D(this.getPos().getX(), this.getPos().getY());
		
		// calculate new position
		// s1 = s0 + v*t
		this.setPos(this.getPos().add(this.getVel().multiply(deltaTime)));
		
		// apply gravity
		this.getVel().addY(c.g);
	}

	@Override
	public void damage(int damage) {
		// TODO implement this operation
		throw new UnsupportedOperationException("not implemented");

	}

	@Override
	public void resetPosition() {
		this.setPos(this.lastPos);

	}

}
