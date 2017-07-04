package rekit.logic.level;

import java.util.List;

import rekit.logic.gameelements.entities.Player;
import rekit.persistence.level.LevelDefinition;
import rekit.persistence.level.LevelManager;
import rekit.persistence.level.LevelType;
import rekit.persistence.level.SettingKey;
import rekit.primitives.geometry.Vec;

/**
 * The logical part of a {@link Level}.
 *
 * @author Dominik Fuchss
 *
 */
public final class LogicalPart {
	private final LevelDefinition definition;
	private final Player player;

	/**
	 * Create LogicalPart by interlink (LevelMtx).
	 *
	 * @param mtx
	 *            the interlink
	 */
	LogicalPart(LevelMtx mtx) {
		this.definition = mtx.getDefinition();
		this.player = new Player(new Vec(6, 5));
	}

	/**
	 * Get the player of the level.
	 *
	 * @return the player
	 */
	public Player getPlayer() {
		return this.player;
	}

	/**
	 * Returns the next level iff existing.
	 *
	 * @return the id of next level or {@code null} if none exists
	 */
	public String getNextLevel() {
		if (this.definition.getType() != LevelType.Arcade) {
			return null;
		}

		String group = this.definition.getSetting(SettingKey.GROUP);
		List<String> levels = LevelManager.getArcadeLevelGroups().get(group);
		int thatIdx = levels.indexOf(this.definition.getID());
		if (thatIdx + 1 >= levels.size()) {
			return null;
		}
		return levels.get(thatIdx + 1);
	}

	/**
	 * Reset the logic of the {@link Level}.
	 */
	void reset() {
		this.player.init();
	}

}