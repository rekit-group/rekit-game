package edu.kit.informatik.ragnarok.logic.gameelements.gui;

import edu.kit.informatik.ragnarok.logic.gameelements.Field;
import edu.kit.informatik.ragnarok.logic.scene.Scene;
import edu.kit.informatik.ragnarok.primitives.Vec;

public abstract class GuiElement implements Comparable<GuiElement> {

	private boolean visible = true;
	
	private Vec pos = new Vec(0);
	
	/**
	 * <pre>
	 *           1..*     1..1
	 * GameElement ------------------------- GameModel
	 *           gameElement        &lt;       gameModel
	 * </pre>
	 */
	private Scene scene;
	
	public boolean isVisible() {
		return this.visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public void setPos(Vec value) {
		this.pos = value;
	}

	public Vec getPos() {
		return this.pos;
	}

	public void setScene(Scene value) {
		this.scene = value;
	}

	public Scene getScene() {
		return this.scene;
	}
	
	public int getZ() {
		return 0;
	}
	
	public GuiElement(Scene scene) {
		this.scene = scene;
	}
	
	public void logicLoop(float deltaTime) {
		// Do nothing
	}
	
	public abstract void render(Field f);
	
	@Override
	public int compareTo(GuiElement other) {
		return this.getZ() - other.getZ();
	}
}
