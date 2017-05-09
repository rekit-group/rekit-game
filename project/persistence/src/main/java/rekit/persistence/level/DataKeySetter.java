package rekit.persistence.level;

/**
 * This class defines the necessary methods to update the data of a level.
 *
 * @author Dominik Fuchss
 * @see DataKey
 */
public interface DataKeySetter {
	/**
	 * Get the current score of the level.
	 *
	 * @return the score
	 * @see DataKey#HIGH_SCORE
	 */
	int getScore();

	/**
	 * Get whether the game was successful.
	 *
	 * @return {@code true} if success
	 * @see DataKey#SUCCESS
	 */
	boolean getSuccess();
	
	/**
	 * Get whether the game was won.
	 *
	 * @return {@code true} if won
	 * @see DataKey#WON
	 */
	boolean getWon();

	/**
	 * Get the current level's definition.
	 *
	 * @return the definition
	 */
	LevelDefinition getDefinition();


}
