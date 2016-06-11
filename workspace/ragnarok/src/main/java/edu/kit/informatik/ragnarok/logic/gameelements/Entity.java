package edu.kit.informatik.ragnarok.logic.gameelements;

import edu.kit.infomatik.config.c;
import edu.kit.informatik.ragnarok.logic.Vec2D;

public abstract class Entity extends GameElement {
	private Vec2D vel;

	public void setVel(Vec2D value) {
		this.vel = value;
	}

	public Vec2D getVel() {
		return this.vel;
	}

	public abstract void logicLoop(float deltaTime);

}
