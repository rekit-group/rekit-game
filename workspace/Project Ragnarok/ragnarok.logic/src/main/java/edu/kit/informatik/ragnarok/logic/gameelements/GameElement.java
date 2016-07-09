package edu.kit.informatik.ragnarok.logic.gameelements;

import edu.kit.informatik.ragnarok.logic.scene.Scene;
import edu.kit.informatik.ragnarok.primitives.Direction;
import edu.kit.informatik.ragnarok.primitives.Frame;
import edu.kit.informatik.ragnarok.primitives.Vec;

public abstract class GameElement implements Collidable, Comparable<GameElement> {

	protected boolean visible = true;

	protected boolean deleteMe = false;

	protected Vec size;
	protected Vec vel;
	protected Vec pos;
	protected Vec lastPos;

	protected Team team;
	protected Scene scene;

	protected GameElement(Team team) {
		this.team = team;
	}

	protected GameElement(Vec startPos, Vec vel, Vec size, Team team) {
		this.team = team;
		this.vel = vel;
		this.size = size;
		this.setPos(startPos);
	}

	public GameElement create(Vec startPos, int[] options) {
		throw new UnsupportedOperationException("Create not supported for " + this.getClass().getSimpleName());
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

	public final Vec getVel() {
		return this.vel;
	}

	public final void setPos(Vec value) {
		if (this.pos == null) {
			this.lastPos = value.clone();
		} else {
			this.lastPos = this.pos.clone();
		}

		this.pos = value.clone();
	}

	public final Vec getPos() {
		return this.pos;
	}

	public final Vec getLastPos() {
		return this.lastPos;
	}

	public final Vec getSize() {
		return this.size;
	}

	public final void setScene(Scene value) {
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

	protected void internalRender(Field f) {
		// do nothing
	};

	protected int getOrderZ() {
		return 0;
	}

	@Override
	public final int compareTo(GameElement other) {
		return this.getOrderZ() - other.getOrderZ();
	}

	protected boolean isVisible() {
		return this.visible;
	}

	public final Team getTeam() {
		return this.team;
	}

	public final boolean getDeleteMe() {
		return this.deleteMe;
	}

	public final void setVel(Vec newVel) {
		this.vel = newVel;
	}
}
