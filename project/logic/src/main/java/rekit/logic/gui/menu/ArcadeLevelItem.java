package rekit.logic.gui.menu;

import java.util.List;

import rekit.core.GameGrid;
import rekit.logic.GameModel;
import rekit.logic.IScene;
import rekit.logic.scene.Scenes;
import rekit.persistence.level.DataKey;
import rekit.persistence.level.LevelManager;
import rekit.primitives.geometry.Vec;

/**
 *
 * This class realizes a {@link MenuItem} with contains an arcade level (switch to).
 *
 */
public final class ArcadeLevelItem extends MenuActionItem {

	private final boolean possible;

	/**
	 * Create MenuActionItem.
	 *
	 * @param scene
	 *            the scene
	 * @param text
	 *            the text
	 * @param size
	 *            the size
	 * @param arcadeGroup
	 *            the group of the level
	 * @param id
	 *            the id of the level
	 * @param model
	 *            the model
	 */
	public ArcadeLevelItem(IScene scene, Vec size, String text, List<String> arcadeGroup, String id, GameModel model) {
		super(scene, size, text, () -> model.switchScene(Scenes.ARCADE, id));
		this.possible = this.checkEnable(arcadeGroup, id);
	}

	private boolean checkEnable(List<String> arcadeGroup, String id) {
		int idx = arcadeGroup.indexOf(id);
		if (idx < 1) {
			return true;
		}
		String prev = arcadeGroup.get(idx - 1);
		return (boolean) LevelManager.getLevelById(prev).getData(DataKey.SUCCESS);

	}

	@Override
	protected void renderItem(GameGrid f) {
		if (this.possible) {
			super.renderItem(f);
		}
	}

	@Override
	public boolean isSelectable() {
		return this.possible;
	}
}
