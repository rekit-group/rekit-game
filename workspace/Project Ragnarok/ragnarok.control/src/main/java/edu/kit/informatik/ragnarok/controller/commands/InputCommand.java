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

	public InputCommand(Entity entity) {
		if (entity == null) {
			throw new IllegalArgumentException("Entity cannot be null");
		}
		this.entity = entity;
	}

}
