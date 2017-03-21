package rekit.logic.scene;

import java.io.ByteArrayInputStream;

import rekit.logic.GameModel;
import rekit.logic.gameelements.GameElement;
import rekit.logic.gameelements.inanimate.Inanimate;
import rekit.logic.gameelements.type.Boss;
import rekit.persistence.level.LevelDefinition;
import rekit.persistence.level.LevelDefinition.Type;
import rekit.persistence.level.LevelManager;

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
		StringBuilder builder = new StringBuilder();
		builder.append("#SETTING::infinite->true").append("\n");
		int idx = 25;
		for (GameElement boss : Boss.getPrototypes()) {
			builder.append("#BOSS_SETTING::AT" + (idx += 5) + "->" + boss.getClass().getSimpleName()).append("\n");
		}
		builder.append("{{").append(Inanimate.class.getSimpleName()).append("}}");
		ByteArrayInputStream is = new ByteArrayInputStream(builder.toString().getBytes());
		LevelDefinition bossrush = new LevelDefinition(is, Type.Boss_Rush);
		LevelManager.addLevel(bossrush);
		return bossrush;
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
