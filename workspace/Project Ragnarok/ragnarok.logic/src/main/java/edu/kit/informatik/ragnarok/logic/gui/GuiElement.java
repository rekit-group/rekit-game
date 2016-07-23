package edu.kit.informatik.ragnarok.logic.gui;

import edu.kit.informatik.ragnarok.logic.Field;
import edu.kit.informatik.ragnarok.logic.gameelements.GameElement;
import edu.kit.informatik.ragnarok.logic.scene.Scene;
import edu.kit.informatik.ragnarok.primitives.geometry.Vec;

/**
 *
 * In contrast to {@link GameElement GameElements} this class represents stuff
 * like score banners, texts etc.<br>
 * So everything which will not interact in the game and has to be shown is a
 * GuiElement
 *
 */
public abstract class GuiElement implements Comparable<GuiElement> {
	/**
	 * Indicates whether the Element is visible
	 */
	protected boolean visible = true;
	/**
	 * The current position
	 */
	protected Vec pos;
	/**
	 * The current size
	 */
	protected Vec size;
	/**
	 * The corresponding scene
	 */
	protected final Scene scene;

	/**
	 * Create by scene
	 *
	 * @param scene
	 *            the scene
	 */
	protected GuiElement(Scene scene) {
		this.pos = new Vec(0);
		this.size = new Vec(1);
		this.scene = scene;
	}

	/**
	 * Create by scene and size
	 *
	 * @param scene
	 *            the scene
	 * @param size
	 *            the size
	 */
	public GuiElement(Scene scene, Vec size) {
		this(scene);
		this.size = size;
	}

	/**
	 * Set the position
	 *
	 * @param value
	 *            the new position
	 */
	public final void setPos(Vec value) {
		this.pos = value;
	}

	/**
	 * Get the current position
	 *
	 * @return the current position
	 */
	public final Vec getPos() {
		return this.pos;
	}

	/**
	 * Get the current size
	 *
	 * @return the current size
	 */
	public final Vec getSize() {
		return this.size;
	}

	/**
	 * Get the corresponding scene
	 *
	 * @return the scene
	 */
	public Scene getScene() {
		return this.scene;
	}

	/**
	 * <p>
	 * Getter for an imaginary z-position of the {@link GuiElement}.
	 * </p>
	 * <p>
	 * Is used to determine the rendering order of every {@link GuiElement}.
	 * </p>
	 *
	 * @return a number that represents the {@link GuiElement GuiElements}
	 *         rendering-order
	 */
	public int getZ() {
		return 0;
	}

	/**
	 * <p>
	 * Template method that will be called periodically after being added to a
	 * {@link Scene}.
	 * </p>
	 * <p>
	 * Should be overwritten in sub classes for implementing custom logic,
	 * physics emulation, time-based actions, ...
	 * </p>
	 *
	 * @param deltaTime
	 *            the time past since the last invoke of this method in seconds
	 */
	public void logicLoop(float deltaTime) {
		// Do nothing
	}

	/**
	 * <p>
	 * Method that will be called periodically after being added to a
	 * {@link Scene}.
	 * </p>
	 * <p>
	 * Its only task is to call the template method <i>internalRender(Field
	 * f)</i> if the {@link GuiElement} is supposed to be rendered.
	 * </p>
	 *
	 * @param f
	 *            the {@link Field} that represents the games field and supplies
	 *            primitive drawing operations.
	 */
	public final void render(Field f) {
		if (this.visible) {
			this.internalRender(f);
		}
	}

	/**
	 * <p>
	 * Template method that will be called periodically after being added to a
	 * {@link Scene}. Must be as performant as possible to keep the FPS low,
	 * since it will be called in every render-loop.
	 * </p>
	 * <p>
	 * Should be overwritten in sub classes for custom visualization using the
	 * {@link Field}.
	 * </p>
	 *
	 * @param f
	 *            the {@link Field} that represents the games field and supplies
	 *            primitive drawing operations.
	 */
	protected abstract void internalRender(Field f);

	@Override
	public final int compareTo(GuiElement other) {
		return this.getZ() - other.getZ();
	}
}
