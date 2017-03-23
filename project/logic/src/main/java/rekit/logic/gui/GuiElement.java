package rekit.logic.gui;

import rekit.core.GameGrid;
import rekit.logic.IScene;
import rekit.logic.gameelements.GameElement;
import rekit.primitives.geometry.Vec;

/**
 *
 * In contrast to {@link GameElement GameElements} this class represents stuff
 * like score banners, texts etc.<br>
 * So everything which will not interact in the game and has to be shown is a
 * GuiElement
 *
 */
public abstract class GuiElement {
	/**
	 * Indicates whether the Element is visible.
	 */
	protected boolean visible = true;
	/**
	 * The current position.
	 */
	protected Vec pos;
	/**
	 * The current size.
	 */
	protected Vec size;
	/**
	 * The corresponding scene.
	 */
	protected final IScene scene;

	/**
	 * Create by scene.
	 *
	 * @param scene
	 *            the scene
	 */
	protected GuiElement(IScene scene) {
		this.pos = new Vec(0);
		this.size = new Vec(1);
		this.scene = scene;
	}

	/**
	 * Create by scene and size.
	 *
	 * @param scene
	 *            the scene
	 * @param size
	 *            the size
	 */
	public GuiElement(IScene scene, Vec size) {
		this(scene);
		this.size = size;
	}

	/**
	 * Set the position.
	 *
	 * @param value
	 *            the new position
	 */
	public final void setPos(Vec value) {
		this.pos = value;
	}

	/**
	 * Get the current position.
	 *
	 * @return the current position
	 */
	public final Vec getPos() {
		return this.pos;
	}

	/**
	 * Get the current size.
	 *
	 * @return the current size
	 */
	public final Vec getSize() {
		return this.size;
	}

	/**
	 * Get the current visibility.
	 * 
	 * @return the current visibility
	 */
	public boolean isVisible() {
		return visible;
	}


	/**
	 * Set the visibility. An invisible Element will not be rendered.
	 * 
	 * @param visible
	 * 				the new visibility
	 */
	public void setVisible(boolean visible) {
		this.visible = visible;
	}


	/**
	 * Get the corresponding scene.
	 *
	 * @return the scene
	 */
	public IScene getScene() {
		return this.scene;
	}

	/**
	 * <p>
	 * Template method that will be called periodically after being added to a
	 * {@link IScene}.
	 * </p>
	 * <p>
	 * Should be overwritten in sub classes for implementing custom logic,
	 * physics emulation, time-based actions, ...
	 * </p>
	 *
	 */
	public void logicLoop() {
		// Do nothing
	}

	/**
	 * <p>
	 * Method that will be called periodically after being added to a
	 * {@link IScene}.
	 * </p>
	 * <p>
	 * Its only task is to call the template method <i>internalRender(Field
	 * f)</i> if the {@link GuiElement} is supposed to be rendered.
	 * </p>
	 *
	 * @param f
	 *            the {@link GameGrid} that represents the games field and
	 *            supplies primitive drawing operations.
	 * @return {@code null} (for lambda usage)
	 */
	public final Void render(GameGrid f) {
		if (this.visible) {
			this.internalRender(f);
		}
		return null;
	}

	/**
	 * <p>
	 * Template method that will be called periodically after being added to a
	 * {@link IScene}. Must be as performant as possible to keep the FPS low,
	 * since it will be called in every render-loop.
	 * </p>
	 * <p>
	 * Should be overwritten in sub classes for custom visualization using the
	 * {@link GameGrid}.
	 * </p>
	 *
	 * @param f
	 *            the {@link GameGrid} that represents the games field and
	 *            supplies primitive drawing operations.
	 */
	protected abstract void internalRender(GameGrid f);

}
