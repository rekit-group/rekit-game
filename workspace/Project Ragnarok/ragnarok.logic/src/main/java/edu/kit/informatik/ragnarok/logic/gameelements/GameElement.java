package edu.kit.informatik.ragnarok.logic.gameelements;

import edu.kit.informatik.ragnarok.logic.scene.Scene;
import edu.kit.informatik.ragnarok.primitives.Direction;
import edu.kit.informatik.ragnarok.primitives.Frame;
import edu.kit.informatik.ragnarok.primitives.Vec;

public abstract class GameElement implements Collidable, Comparable<GameElement> {

	private boolean visible = true;

	public boolean deleteMe = false;

	private Vec size = new Vec(1, 1);

	protected Team team;

	protected Vec pos;

	private Vec vel = new Vec();

	private Vec lastPos;

	public GameElement(Vec startPos, Team team) {
		if (startPos == null) {
			return;
		}
		this.team = team;
		this.setPos(startPos);

	}

	public void destroy() {
		this.deleteMe = true;
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

	public final void render(Field f) {
		if (this.isVisible()) {
			this.internalRender(f);
		}
	}

	protected abstract void internalRender(Field f);

	public int getOrderZ() {
		return 0;
	}

	@Override
	public int compareTo(GameElement other) {
		return this.getOrderZ() - other.getOrderZ();
	}

	protected boolean isVisible() {
		return this.visible;
	}

	public final Team getTeam() {
		return this.team;
	}
}
