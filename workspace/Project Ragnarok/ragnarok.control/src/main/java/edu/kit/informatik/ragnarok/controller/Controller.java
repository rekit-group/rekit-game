package edu.kit.informatik.ragnarok.controller;

import edu.kit.informatik.ragnarok.logic.Model;
import edu.kit.informatik.ragnarok.util.InputHelper;

public interface Controller {

	static Controller getController(Model model) {
		return new ControllerImpl(model);
	}

	void start();

	InputHelper getInputHelper();

}
