package edu.kit.informatik.ragnarok.controller.commands;

import edu.kit.informatik.ragnarok.controller.Controller;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.Entity;

/**
 * This class defines an Command which will be executed by a {@link Controller}
 * and is linked to an {@link Entity}
 *
 * @author Dominik Fuchß
 *
 */
public abstract class InputCommand implements Command {
	/**
	 * The corresponding entity
	 */
	protected Entity entity;

	/**
	 * Set the corresponding {@link Entity} for the command
	 *
	 * @param entity
	 *            the entity
	 * @return {@code this} for code chaining
	 */
	public final InputCommand setEntity(Entity entity) {
		this.entity = entity;
		return this;
	}

}
