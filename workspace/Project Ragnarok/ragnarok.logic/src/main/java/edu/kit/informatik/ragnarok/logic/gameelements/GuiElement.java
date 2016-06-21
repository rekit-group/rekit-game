package edu.kit.informatik.ragnarok.logic.gameelements;

import edu.kit.informatik.ragnarok.logic.GameModel;
import edu.kit.informatik.ragnarok.primitives.Vec2D;

public abstract class GuiElement implements Comparable<GuiElement> {

	private boolean visible = true;
	
	private Vec2D pos;
	
	/**
	 * <pre>
	 *           1..*     1..1
	 * GameElement ------------------------- GameModel
	 *           gameElement        &lt;       gameModel
	 * </pre>
	 */
	private GameModel gameModel;
	
	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public void setPos(Vec2D value) {
		this.pos = value;
	}

	public Vec2D getPos() {
		return this.pos;
	}

	public void setGameModel(GameModel value) {
		this.gameModel = value;
	}

	public GameModel getGameModel() {
		return this.gameModel;
	}
	
	public int getZ() {
		return 0;
	}
	
	public GuiElement(GameModel model) {
		this.gameModel = model;
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
