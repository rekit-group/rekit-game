package edu.kit.informatik.ragnarok.logic.gameelements;

import org.eclipse.swt.graphics.GC;

import edu.kit.informatik.ragnarok.logic.Direction;
import edu.kit.informatik.ragnarok.logic.Frame;
import edu.kit.informatik.ragnarok.logic.GameModel;
import edu.kit.informatik.ragnarok.logic.Vec2D;

public abstract class GameElement implements Collidable {
	public void destroy() {
		// TODO implement this operation
		throw new UnsupportedOperationException("not implemented");
	}

	private int team;

	private void setTeam(int value) {
		this.team = value;
	}

	private int getTeam() {
		return this.team;
	}

	public void reactToCollision(GameElement element, Direction dir) {
		// Do nothing
	}

	public void logicLoop(float deltaTime) {
		// Do nothing
	}

	private Vec2D pos;

	public void setPos(Vec2D value) {
		this.pos = value;
	}

	public Vec2D getPos() {
		return this.pos;
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

	public abstract void render(GC gc);

}
