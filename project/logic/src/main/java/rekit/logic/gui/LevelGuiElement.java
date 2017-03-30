package rekit.logic.gui;

import rekit.logic.ILevelScene;

public abstract class LevelGuiElement extends GuiElement {
	public LevelGuiElement(ILevelScene scene) {
		super(scene);
	}

	@Override
	public ILevelScene getScene() {
		return (ILevelScene) super.getScene();
	}
}
