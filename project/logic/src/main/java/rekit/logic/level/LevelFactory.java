package rekit.logic.level;

import java.io.ByteArrayInputStream;
import java.nio.charset.Charset;
import java.util.Objects;

import rekit.persistence.level.LevelDefinition;
import rekit.persistence.level.LevelManager;
import rekit.persistence.level.LevelType;
import rekit.persistence.level.SettingKey;

/**
 * Factory class which creates all {@link Level Levels}.
 *
 * @author Dominik Fuchss
 *
 */
public final class LevelFactory {
	/**
	 * Prevent instantiation.
	 */
	private LevelFactory() {
	}

	/**
	 * Create a new {@link Level} by {@link LevelDefinition}.
	 *
	 * @param definition
	 *            the definition
	 * @return the level
	 */
	public static Level createLevel(LevelDefinition definition) {
		Objects.requireNonNull(definition);
		LevelMtx mtx = new LevelMtx(definition);
		StructurePart struct = new StructurePart(mtx);
		LogicalPart logic = new LogicalPart(mtx);
		Level lv = new Level(mtx, struct, logic);
		return lv;
	}

	/**
	 * Create the BossRush Level (a special level with bosses).
	 *
	 * @return the BossRush Level
	 */
	public static synchronized Level getBossRushLevel() {

		LevelDefinition brLevel = LevelManager.getLevelById(LevelFactory.getBossRushLevelDefinition().getID());
		if (brLevel == null) {
			brLevel = LevelFactory.getBossRushLevelDefinition();
			LevelManager.addLevel(brLevel, true);
		}

		LevelMtx mtx = new LevelMtx(brLevel);
		StructurePart sp = new BossRushStructurePart(mtx);
		LogicalPart lp = new LogicalPart(mtx);
		return new Level(mtx, sp, lp);

	}

	private static LevelDefinition getBossRushLevelDefinition() {
		StringBuilder builder = new StringBuilder();
		builder.append("#SETTING::").append(SettingKey.INFINITE).append("->true").append("\n");
		ByteArrayInputStream is = new ByteArrayInputStream(builder.toString().getBytes(Charset.forName("UTF-8")));
		return new LevelDefinition(is, LevelType.Boss_Rush);
	}

}
