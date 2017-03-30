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
	HIGH_SCORE(0),
	/**
	 * Indicates whether the last try was successful.
	 */
	SUCCESS(false);
	/**
	 * The DataKey's default value.
	 */
	private final Serializable defaultVal;

	/**
	 * Create a new DataKey default value.
	 *
	 * @param defaultVal
	 *            the default value
	 */
	DataKey(Serializable defaultVal) {
		this.defaultVal = defaultVal;
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
