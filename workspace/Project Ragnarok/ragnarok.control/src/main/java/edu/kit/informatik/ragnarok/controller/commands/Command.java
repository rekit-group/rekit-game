package edu.kit.informatik.ragnarok.controller.commands;

public interface Command {
	/**
	 * Execute the command
	 *
	 * @param inputMethod
	 *            the key state
	 */
	public void execute(InputMethod inputMethod);

}
