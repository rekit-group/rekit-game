package edu.kit.informatik.ragnarok.controller.commands;

import edu.kit.informatik.ragnarok.logic.gameelements.entities.Entity;

public abstract class InputCommand {
	private Entity entity;

	public void setEntity(Entity entity) {
		this.entity = entity;
	}

	public Entity getEntity() {
		return this.entity;
	}

	public abstract void apply();

}
