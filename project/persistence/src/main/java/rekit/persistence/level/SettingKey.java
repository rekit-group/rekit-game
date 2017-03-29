package rekit.persistence.level;

/**
 * This enum defines all valid settings of a {@link LevelDefinition}.
 * 
 * @author Dominik Fuchss
 * @see LevelDefinition#setSetting(SettingKey, String)
 * @see LevelDefinition#getSetting(SettingKey)
 *
 */
public enum SettingKey {
	/**
	 * The name of the level (id=name).
	 */
	NAME("name"),
	/**
	 * The group of the level for arcade grouping (id=group).
	 */
	GROUP("group"),
	/**
	 * Indicates whether coins shall automatically spawn (id=autoCoinSpawn).
	 */
	AUTO_COIN_SPAWN("autoCoinSpawn"),
	/**
	 * Indicates whether the level is infinite (does not end) (id=infinite).
	 */
	INFINITE("infinite"),
	/**
	 * Indicates whether the structures can be shuffled (id=shuffle).
	 */
	SHUFFLE("shuffle"),
	/**
	 * Indicates whether it is allowed to create gaps (only floor) sections
	 * between different structures (id=doGaps).
	 */
	DO_GAPS("doGaps");
	/**
	 * The id for the level-files.
	 */
	private final String id;

	/**
	 * Create a new SettingKey by id.
	 * 
	 * @param id
	 *            the id
	 */
	SettingKey(String id) {
		this.id = id;
	}

	/**
	 * Get the id of the SettingKey
	 * 
	 * @return the id
	 */
	String getID() {
		return this.id;
	}

	/**
	 * Find SettingKey by id.
	 * 
	 * @param id
	 *            the id
	 * @return the key or {@code null} if not found
	 */
	public static final SettingKey getByString(String id) {
		for (SettingKey key : SettingKey.values()) {
			if (key.id.matches(id)) {
				return key;
			}
		}
		return null;
	}

	@Override
	public String toString() {
		return this.id;
	}
}
