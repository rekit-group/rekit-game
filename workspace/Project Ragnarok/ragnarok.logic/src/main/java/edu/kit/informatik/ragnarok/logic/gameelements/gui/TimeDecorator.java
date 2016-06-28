package edu.kit.informatik.ragnarok.logic.gameelements.gui;

import edu.kit.informatik.ragnarok.logic.gameelements.Field;
import edu.kit.informatik.ragnarok.logic.scene.Scene;
import edu.kit.informatik.ragnarok.primitives.TimeDependency;

public class TimeDecorator extends GuiElement {
	
	private GuiElement element;
	private TimeDependency timer;

	public TimeDecorator(Scene scene, GuiElement element, TimeDependency timer) {
		super(scene);
		this.element = element;
		this.timer = timer;
	}
	
	@Override
	public void logicLoop(float deltaTime) {
		timer.removeTime(deltaTime);
		if (timer.timeUp()) {
			this.setVisible(false);
			this.getScene().removeGuiElement(this);
		}
		
		element.logicLoop(deltaTime);
	}

	@Override
	public void internalRender(Field f) {
		element.internalRender(f);
	}

}
