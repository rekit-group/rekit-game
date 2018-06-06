package rekit.gui.controller.commands;

import rekit.logic.Model.GameState;

/**
 * This class will be used while {@link GameState#INGAME ingame} to toggle
 * pause.
 *
 * @author Matthias Schmitt
 *
 */
public class PlayPauseCommand extends MenuCommand {
	/**
	 * Create the command.
	 *
	 * @param supervisor
	 *            the {@link CommandSupervisor}
	 */
	public PlayPauseCommand(CommandSupervisor supervisor) {
		super(supervisor, null);

	}

	@Override
	public void execute(InputMethod inputMethod) {
		if (inputMethod == InputMethod.RELEASE) {
			this.supervisor.getScene().togglePause();
		}
	}

}
