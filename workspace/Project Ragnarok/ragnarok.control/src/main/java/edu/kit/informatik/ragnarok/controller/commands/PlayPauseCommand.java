package edu.kit.informatik.ragnarok.controller.commands;

public class PlayPauseCommand extends InputCommand {

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
