package rekit.logic.level;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import rekit.logic.gameelements.type.Boss;
import rekit.persistence.level.LevelDefinition;
import rekit.persistence.level.LevelType;
import rekit.persistence.level.SettingKey;

/**
 * This class holds all necessary information about a level.
 *
 */
public final class BossRushLevel extends Level {
	private final List<Boss> bosses;
	private int next;

	/**
	 * Create the level.
	 */
	public BossRushLevel() {
		super(BossRushLevel.getData());
		this.bosses = new ArrayList<>();
		Boss.getPrototypes().forEach(e -> this.bosses.add((Boss) e));
		this.next = this.bosses.size();
	}

	private static LevelDefinition getData() {
		StringBuilder builder = new StringBuilder();
		builder.append("#SETTING::" + SettingKey.INFINITE + "->true").append("\n");
		ByteArrayInputStream is = new ByteArrayInputStream(builder.toString().getBytes());
		return new LevelDefinition(is, LevelType.Boss_Rush);
	}

	@Override
	public Structure next() {
		if (this.next == this.bosses.size()) {
			this.next = 0;
			this.shuffle();
			return this.getInitialStructure();
		}
		return this.bosses.get(this.next++).getBossStructure();
	}

	private void shuffle() {
		List<Boss> newList = new ArrayList<>();
		while (!this.bosses.isEmpty()) {
			int idx = this.random.nextInt(this.bosses.size());
			newList.add(this.bosses.get(idx));
			this.bosses.remove(idx);
		}
		newList.forEach(this.bosses::add);
	}

}