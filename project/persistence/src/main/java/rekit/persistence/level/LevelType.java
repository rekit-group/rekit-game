package rekit.persistence.level;

import rekit.util.LambdaUtil;
import rekit.util.LambdaUtil.FunctionWithException;

/**
 * The type of a level.
 *
 * @author Dominik Fuchss
 *
 */
public enum LevelType {
	/**
	 * Infinite level.
	 */
	Infinite_Fun,
	/**
	 * Level of the day.
	 */
	Level_of_the_Day,
	/**
	 * Arcade level.
	 */
	Arcade,
	/**
	 * Boss Rush Mode.
	 */
	Boss_Rush;
	/**
	 * Same as {@link #valueOf(String)}, but no exception.
	 *
	 * @param string
	 *            the representing String
	 * @return the type or {@code null} iff none found
	 */
	public static LevelType byString(String string) {
		return LambdaUtil.invoke((FunctionWithException<String, LevelType>) LevelType::valueOf, string);
	}
}