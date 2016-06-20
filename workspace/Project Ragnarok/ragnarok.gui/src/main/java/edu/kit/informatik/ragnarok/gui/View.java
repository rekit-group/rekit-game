package edu.kit.informatik.ragnarok.gui;

import edu.kit.informatik.ragnarok.logic.Model;
import edu.kit.informatik.ragnarok.util.InputHelper;

public interface View {
	static View getView(Model model) {
		return new GameView(model);
	}

	void start();

	void attachMe(InputHelper inputHelper);
}
