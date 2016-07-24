package edu.kit.informatik.ragnarok.controller.commands;

public class PlayPauseCommand implements Command {

	/**
	 * The command's supervisor
	 */
	private CommandSupervisor supervisor;

	public PlayPauseCommand(CommandSupervisor supervisor) {
		this.supervisor = supervisor;

	}

	@Override
	public void execute(InputMethod inputMethod) {
		if (inputMethod == InputMethod.RELEASE) {
			this.supervisor.getLevelScene().togglePause();
		}
	}

}
