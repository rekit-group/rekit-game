package edu.kit.informatik.ragnarok.controller.commands;

import edu.kit.informatik.ragnarok.logic.gameelements.entities.Entity;

public abstract class InputCommand {
	private Entity entity;
	
	public enum InputMethod {
		PRESS,
		RELEASE
	}
	/**
	 * Set the corresponding entity for the command
	 *
	 * @param entity
	 *            the entity
	 * @return {@code this} for code chaining
	 */
	public final InputCommand setEntity(Entity entity) {
		this.entity = entity;
		return this;
	}

	public Entity getEntity() {
		return this.entity;
	}

	public abstract void apply(InputMethod inputMethod);

}
