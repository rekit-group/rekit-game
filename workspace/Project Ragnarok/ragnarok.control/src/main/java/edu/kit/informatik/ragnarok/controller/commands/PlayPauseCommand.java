package edu.kit.informatik.ragnarok.controller.commands;

import edu.kit.informatik.ragnarok.logic.GameState;

/**
 * This class will be used while {@link GameState#INGAME ingame} to toggle
 * pause.
 *
 * @author Matthias Schmitt
 *
 */
public class PlayPauseCommand extends InputCommand {
	/**
	 * Create the command.
	 * 
	 * @param supervisor
	 *            the {@link CommandSupervisor}
	 */
	public PlayPauseCommand(CommandSupervisor supervisor) {
		super(supervisor);

	}

	@Override
	public void execute(InputMethod inputMethod) {
		if (inputMethod == InputMethod.RELEASE) {
			this.supervisor.getScene().togglePause();
		}
	}

}
