package rekit.persistence.level;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * This enum contains the keys for level-data which can be saved to a user-dat
 * file.
 *
 * @author Dominik Fuchss
 * @see LevelDefinition#getData(DataKey)
 * @see LevelDefinition#setData(DataKey, Serializable)
 * @see LevelManager#contentChanged()
 * @see DataKeySetter
 *
 */
public enum DataKey {
	/**
	 * The highscore of a level.
	 */
	HIGH_SCORE(0, HelperMethods::setHighScore),
	/**
	 * Indicates whether the last try was successful.
	 */
	SUCCESS(false, HelperMethods::setSuccess),
	/**
	 * Indicates whether any try was successful.
	 */
	WON(false, HelperMethods::setWon);

	/**
	 * Invoke after a level has ended.
	 *
	 * @param parent
	 *            the data key setter for the level
	 */
	public static void atEnd(DataKeySetter parent) {
		Objects.requireNonNull(parent);
		for (DataKey dk : DataKey.values()) {
			dk.updater.accept(parent);
		}
	}

	/**
	 * The DataKey's default value.
	 */
	private final Serializable defaultVal;
	/**
	 * The updater of the value.
	 */
	private final Consumer<DataKeySetter> updater;

	/**
	 * Create a new DataKey default value.
	 *
	 * @param defaultVal
	 *            the default value
	 * @param updater
	 *            the updater of the value
	 */
	DataKey(Serializable defaultVal, Consumer<DataKeySetter> updater) {
		this.defaultVal = defaultVal;
		this.updater = updater;
	}

	/**
	 * Get the default value of the DataKey.
	 *
	 * @return the default value
	 */
	public Serializable getDefaultVal() {
		return this.defaultVal;
	}

	/**
	 * This class contains the helper methods to set the DataKeys.
	 *
	 * @author Dominik Fuchss
	 *
	 */
	private static final class HelperMethods {
		/**
		 * Set the new highscore.
		 */
		static void setHighScore(DataKeySetter dks) {
			int newScore = dks.getScore();
			int oldScore = (Integer) dks.getDefinition().getData(DataKey.HIGH_SCORE);
			if (oldScore < newScore) {
				dks.getDefinition().setData(DataKey.HIGH_SCORE, newScore);
			}
		}

		/**
		 * Set the new success.
		 */
		static void setSuccess(DataKeySetter dks) {
			boolean won = dks.getSuccess();
			dks.getDefinition().setData(DataKey.SUCCESS, won);
		}

		/**
		 * Set the new won.
		 */
		static void setWon(DataKeySetter dks) {
			boolean won = dks.getWon();
			dks.getDefinition().setData(DataKey.WON, won || (boolean) dks.getDefinition().getData(DataKey.WON));
		}
	}
}
