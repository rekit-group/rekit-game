package edu.kit.informatik.ragnarok.logic.gameelements;

import edu.kit.informatik.ragnarok.gui.Field;
import edu.kit.informatik.ragnarok.logic.Direction;
import edu.kit.informatik.ragnarok.logic.Frame;
import edu.kit.informatik.ragnarok.logic.GameModel;
import edu.kit.informatik.ragnarok.logic.Vec2D;

public abstract class GameElement implements Collidable {
	public boolean deleteMe = false;
	public void destroy() {
		this.deleteMe = true;
	}

	private int team = 1;

	protected void setTeam(int value) {
		this.team = value;
	}

	protected int getTeam() {
		return this.team;
	}
	
	protected boolean isHostile(GameElement other) {
		return this.getTeam() != other.getTeam();
	}

	public void reactToCollision(GameElement element, Direction dir) {
		// Do nothing
	}

	public void logicLoop(float deltaTime) {
		// Do nothing
	}

	private Vec2D vel;
	
	public void setVel(Vec2D value) {
		this.vel = value;
	}

	public Vec2D getVel() {
		return this.vel;
	}
	
	private Vec2D pos;

	public void setPos(Vec2D value) {
		if (this.pos == null) {
			this.setLastPos(value);
		}
		else {
			this.setLastPos(this.pos);
		}
		
		this.pos = value;
	}

	public Vec2D getPos() {
		return this.pos;
	}
	
	
	
	private Vec2D lastPos;

	public void setLastPos(Vec2D value) {
		this.lastPos = value;
	}

	public Vec2D getLastPos() {
		return this.lastPos;
	}

	public Vec2D getSize() {
		return new Vec2D(1, 1);
	}

	/**
	 * <pre>
	 *           1..*     1..1
	 * GameElement ------------------------- GameModel
	 *           gameElement        &lt;       gameModel
	 * </pre>
	 */
	private GameModel gameModel;

	public void setGameModel(GameModel value) {
		this.gameModel = value;
	}

	public GameModel getGameModel() {
		return this.gameModel;
	}

	public Frame getCollisionFrame() {
		Vec2D v1 = this.getPos().add(this.getSize().multiply(-0.5f));
		Vec2D v2 = this.getPos().add(this.getSize().multiply(0.5f));
		
		return new Frame(v1, v2);
	}

	public abstract void render(Field f);

}
