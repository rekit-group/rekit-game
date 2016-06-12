package edu.kit.informatik.ragnarok.logic.gameelements.entities;

import edu.kit.informatik.ragnarok.logic.gameelements.Entity;


public abstract class EntityState {
	private Entity entity;

	public void setEntity(Entity entity) {
		this.entity = entity;
	}

	public Entity getEntity() {
		return this.entity;
	}

	public boolean canJump() {
		return true;
	}

	public abstract void render();

}
