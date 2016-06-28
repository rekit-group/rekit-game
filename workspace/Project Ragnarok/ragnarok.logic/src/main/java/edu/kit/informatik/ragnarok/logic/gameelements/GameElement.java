package edu.kit.informatik.ragnarok.logic.gameelements;

import edu.kit.informatik.ragnarok.logic.scene.Scene;
import edu.kit.informatik.ragnarok.primitives.Direction;
import edu.kit.informatik.ragnarok.primitives.Frame;
import edu.kit.informatik.ragnarok.primitives.Vec;

public abstract class GameElement implements Collidable, Comparable<GameElement> {

	protected boolean visible = true;

	protected boolean deleteMe = false;

	protected Vec size = new Vec(1, 1);
	protected Vec vel = new Vec();
	protected Vec pos;
	protected Vec lastPos;

	protected Team team;
	protected Scene scene;

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
		this.vel = value.clone();
	}

	public Vec getVel() {
		return this.vel.clone();
	}

	public final void setPos(Vec value) {
		if (this.pos == null) {
			this.lastPos = value.clone();
		} else {
			this.lastPos = this.pos.clone();
		}

		this.pos = value.clone();
	}

	public Vec getPos() {
		return this.pos.clone();
	}

	public Vec getLastPos() {
		return this.lastPos.clone();
	}

	public Vec getSize() {
		return this.size;
	}

	public void setScene(Scene value) {
		this.scene = value;
	}

	public final Frame getCollisionFrame() {
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

	protected int getOrderZ() {
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

	public boolean getDeleteMe() {
		return this.deleteMe;
	}
}
