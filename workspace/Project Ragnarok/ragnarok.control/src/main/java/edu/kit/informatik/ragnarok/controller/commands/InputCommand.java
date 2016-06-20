package edu.kit.informatik.ragnarok.controller.commands;

import edu.kit.informatik.ragnarok.controller.Controller;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.Entity;

/**
 * This class defines an Command which will be executed by a {@link Controller}
 *
 * @author Dominik Fuchß
 *
 */
public abstract class InputCommand {
	/**
	 * The corresponding entity
	 */
	protected Entity entity;

	/**
	 * This enum is used to indicate a press or release state of a key
	 *
	 * @author Dominik Fuchß
	 *
	 */
	public enum InputMethod {
		PRESS, RELEASE
	}

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

	/**
	 * Execute the command
	 *
	 * @param inputMethod
	 *            the key state
	 */
	public abstract void execute(InputMethod inputMethod);

}
