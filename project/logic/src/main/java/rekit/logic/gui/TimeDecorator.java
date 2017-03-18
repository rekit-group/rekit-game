package rekit.logic.gui;

import rekit.core.GameGrid;
import rekit.logic.IScene;
import rekit.primitives.time.Timer;

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
	/*
	 * The last time of invoking {@link #logicLoop()}.
	 */
	// private long lastTime = GameTime.getTime();

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
	public void logicLoop() {
		// long deltaTime = GameTime.getTime() - this.lastTime;
		// this.lastTime += deltaTime;
		this.timer.logicLoop();
		// this.timer.removeTime(deltaTime);
		if (this.timer.timeUp()) {
			this.visible = false;
			this.getScene().removeGuiElement(this);
		}

		this.element.logicLoop();
	}

	@Override
	public void internalRender(GameGrid f) {
		this.element.render(f);
	}

}
