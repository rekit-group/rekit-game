package rekit.logic.scene;

import java.io.ByteArrayInputStream;

import rekit.logic.GameModel;
import rekit.persistence.level.LevelDefinition;
import rekit.persistence.level.LevelDefinition.Type;

/**
 * This class realizes a LevelScene for BossRush levels.
 */
final class BossRushScene extends LevelScene {
	/**
	 * Create a new BossRush Scene.
	 *
	 * @param model
	 *            the model
	 */
	private BossRushScene(GameModel model) {
		super(model, BossRushScene.getBossRushLevel());
	}

	private static final LevelDefinition getBossRushLevel() {
		LevelDefinition def = new LevelDefinition(new ByteArrayInputStream(new byte[0]), Type.Boss_Rush);
		return def;
	}

	/**
	 * Create method of the scene.
	 *
	 * @param model
	 *            the model
	 * @param options
	 *            the options
	 * @return a new arcade scene.
	 */
	public static Scene create(GameModel model, String[] options) {
		return new BossRushScene(model);
	}

}
