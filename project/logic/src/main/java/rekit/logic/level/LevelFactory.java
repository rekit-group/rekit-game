package rekit.logic.level;

import java.io.ByteArrayInputStream;
import java.util.Objects;

import rekit.persistence.level.LevelDefinition;
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
	public static final Level createLevel(LevelDefinition definition) {
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
	public static final Level createBossRushLevel() {
		LevelMtx mtx = LevelFactory.getBossRushLevelMtx();
		StructurePart sp = new BossRushStructurePart(mtx);
		LogicalPart lp = new LogicalPart(mtx);
		Level lv = new Level(mtx, sp, lp);
		return lv;
	}

	private static LevelMtx getBossRushLevelMtx() {
		StringBuilder builder = new StringBuilder();
		builder.append("#SETTING::").append(SettingKey.INFINITE).append("->true").append("\n");
		ByteArrayInputStream is = new ByteArrayInputStream(builder.toString().getBytes());
		return new LevelMtx(new LevelDefinition(is, LevelType.Boss_Rush));
	}

}
