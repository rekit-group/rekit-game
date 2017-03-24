package rekit.logic.gui;

import rekit.logic.ILevelScene;

public abstract class LevelGuiElement extends GuiElement {
	
	private ILevelScene levelScene;

	public LevelGuiElement(ILevelScene levelScene) {
		super(levelScene);
		this.levelScene = levelScene;
	}
	
	@Override
	public ILevelScene getScene() {
		return this.levelScene;
	}
}
