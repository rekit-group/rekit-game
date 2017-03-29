package rekit.logic;

import rekit.logic.gameelements.entities.Player;

/**
 * This interface extends {@link IScene} and adds necessary methods to
 * encapsulate Levels.
 *
 * @author Matthias Schmitt
 *
 */
public interface ILevelScene extends IScene {
	/**
	 * Get the current player of {@code null} if none set.
	 *
	 * @return the current player of {@code null}
	 */
	Player getPlayer();

	/**
	 * Get the current score of the player.
	 *
	 * @return the current score or 0 if none available
	 */
	int getScore();

	/**
	 * Get the current high score for the current level.
	 *
	 * @return the current high score or 0 if none available
	 */
	int getHighScore();

	/**
	 * End a level.
	 *
	 * @param won
	 *            indicates whether successful or died
	 */
	void end(boolean won);

	/**
	 * Indicates whether the level has ended.
	 *
	 * @return {@code true} if ended, {@code false} otherwise
	 */
	boolean hasEnded();

}
