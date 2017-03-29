package rekit.persistence.level;

import java.io.Serializable;

/**
 * This enum contains the keys for level-data which can be saved to a user-dat
 * file.
 *
 * @author Dominik Fuchss
 * @see LevelDefinition#getData(DataKey)
 * @see LevelDefinition#setData(DataKey, Serializable)
 * @see LevelManager#contentChanged()
 *
 */
public enum DataKey {
	/**
	 * The highscore of a level.
	 */
	HIGH_SCORE("highscore", 0),
	/**
	 * Indicates whether the last try was successful.
	 */
	SUCCESS("success", false);
	/**
	 * The id which identifies the DataKey.
	 */
	private final String id;
	/**
	 * The DataKey's default value.
	 */
	private final Serializable defaultVal;

	/**
	 * Create a new DataKey by id and default value.
	 *
	 * @param id
	 *            the id
	 * @param defaultVal
	 *            the default value
	 */
	DataKey(String id, Serializable defaultVal) {
		this.id = id;
		this.defaultVal = defaultVal;
	}

	/**
	 * Get the id of the DataKey.
	 *
	 * @return the id
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * Get the default value of the DataKey.
	 *
	 * @return the default value
	 */
	public Serializable getDefaultVal() {
		return this.defaultVal;
	}

}
