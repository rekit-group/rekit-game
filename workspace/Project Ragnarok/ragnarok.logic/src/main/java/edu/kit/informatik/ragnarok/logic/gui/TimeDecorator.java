package edu.kit.informatik.ragnarok.logic.gui;

import edu.kit.informatik.ragnarok.core.Field;
import edu.kit.informatik.ragnarok.core.GuiElement;
import edu.kit.informatik.ragnarok.core.IScene;
import edu.kit.informatik.ragnarok.primitives.time.Timer;

/**
 *
 * This class can decorate all {@link GuiElement GuiElements} so that they will
 * be deleted after a specific time.
 *
 */
public final class TimeDecorator extends GuiElement {
	/**
	 * The decorated GuiElement.
	 */
	private GuiElement element;
	/**
	 * The timer.
	 */
	private Timer timer;

	/**
	 * Create a TimeDecorator.
	 *
	 * @param scene
	 *            the scene
	 * @param element
	 *            the decorated element
	 * @param timer
	 *            the timer
	 */
	public TimeDecorator(IScene scene, GuiElement element, Timer timer) {
		super(scene);
		this.element = element;
		this.timer = timer;
	}

	@Override
	public void logicLoop(float deltaTime) {
		this.timer.removeTime(deltaTime);
		if (this.timer.timeUp()) {
			this.visible = false;
			this.getScene().removeGuiElement(this);
		}

		this.element.logicLoop(deltaTime);
	}

	@Override
	public void internalRender(Field f) {
		// TODO Internal render not accessible
		this.element.render(f);
	}

}
