package rekit.logic.gui;

import rekit.logic.ILevelScene;

/**
 * This class represents a {@link GuiElement} attached to a {@link ILevelScene}.
 *
 */
public abstract class LevelGuiElement extends GuiElement {
	/**
	 * Create LevelGuiElement by ILevelScene.
	 * 
	 * @param scene
	 *            the scene
	 */
	public LevelGuiElement(ILevelScene scene) {
		super(scene);
	}

	@Override
	public ILevelScene getScene() {
		return (ILevelScene) super.getScene();
	}
}
