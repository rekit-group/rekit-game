package edu.kit.informatik.ragnarok.logic.gameelements.gui;

import edu.kit.informatik.ragnarok.logic.GameModel;
import edu.kit.informatik.ragnarok.logic.gameelements.Field;
import edu.kit.informatik.ragnarok.primitives.TimeDependency;

public class TimeDecorator extends GuiElement {
	
	private GuiElement element;
	private TimeDependency timer;

	public TimeDecorator(GameModel model, GuiElement element, TimeDependency timer) {
		super(model);
		this.element = element;
		this.timer = timer;
	}
	
	@Override
	public void logicLoop(float deltaTime) {
		timer.removeTime(deltaTime);
		if (timer.timeUp()) {
			this.setVisible(false);
			this.getGameModel().removeGuiElement(this);
		}
		
		element.logicLoop(deltaTime);
	}

	@Override
	public void render(Field f) {
		element.render(f);
	}

}
