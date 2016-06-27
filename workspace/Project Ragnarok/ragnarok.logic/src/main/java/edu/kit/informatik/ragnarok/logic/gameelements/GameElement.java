package edu.kit.informatik.ragnarok.logic.gameelements;

import edu.kit.informatik.ragnarok.logic.scene.Scene;
import edu.kit.informatik.ragnarok.primitives.Direction;
import edu.kit.informatik.ragnarok.primitives.Frame;
import edu.kit.informatik.ragnarok.primitives.Vec;

public abstract class GameElement implements Collidable, Comparable<GameElement> {

	private boolean visible = true;

	public boolean deleteMe = false;

	private Vec size = new Vec(1, 1);

	private int team = 1;

	protected Vec pos;

	private Vec vel = new Vec();

	private Vec lastPos;

	public GameElement(Vec startPos) {
		if (startPos == null) {
			return;
		}
		this.setPos(startPos);

	}

	public void destroy() {
		this.deleteMe = true;
	}

	protected void setTeam(int value) {
		this.team = value;
	}

	protected int getTeam() {
		return this.team;
	}

	protected boolean isHostile(GameElement other) {
		return this.getTeam() != other.getTeam();
	}

	@Override
	public void reactToCollision(GameElement element, Direction dir) {
		// Do nothing
	}

	public void logicLoop(float deltaTime) {
		// Do nothing
	}

	public void setVel(Vec value) {
		this.vel = value;
	}

	public Vec getVel() {
		return this.vel;
	}

	public void setPos(Vec value) {
		if (this.pos == null) {
			this.setLastPos(value);
		} else {
			this.setLastPos(this.pos);
		}

		this.pos = value;
	}

	public Vec getPos() {
		return this.pos;
	}

	public void setLastPos(Vec value) {
		this.lastPos = value;
	}

	public Vec getLastPos() {
		return this.lastPos;
	}

	public Vec getSize() {
		return this.size;
	}

	public void setSize(Vec size) {
		this.size = size;
	}

	/**
	 * <pre>
	 *           1..*     1..1
	 * GameElement ------------------------- GameModel
	 *           gameElement        &lt;       gameModel
	 * </pre>
	 */
	private Scene scene;

	public void setScene(Scene value) {
		this.scene = value;
	}

	public Scene getScene() {
		return this.scene;
	}

	public Frame getCollisionFrame() {
		Vec v1 = this.getPos().add(this.getSize().multiply(-0.5f));
		Vec v2 = this.getPos().add(this.getSize().multiply(0.5f));

		return new Frame(v1, v2);
	}

	public abstract void render(Field f);

	public int getOrderZ() {
		return 0;
	}

	@Override
	public int compareTo(GameElement other) {
		return this.getOrderZ() - other.getOrderZ();
	}

	public boolean isVisible() {
		return this.visible;
	}
}
