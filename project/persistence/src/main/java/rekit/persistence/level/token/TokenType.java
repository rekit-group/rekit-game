package rekit.persistence.level.token;

/**
 * This class defines the different TokenTypes which exist in the LevelLanguage.
 *
 * @author Dominik Fuchss
 *
 */
public enum TokenType {
	/**
	 * A normal setting for the level.
	 */
	SETTING,
	/**
	 * A setting which belongs to a boss.
	 */
	BOSS_SETTING,
	/**
	 * An Alias.
	 */
	ALIAS,
	/**
	 * Mapping.
	 */
	MAPPING,
	/**
	 * Begin of an Level or an LevelLine (equals "{").
	 */
	BEGIN,
	/**
	 * End of an Level or an LevelLine (equals "}").
	 */
	END,
	/**
	 * End of String Token.
	 */
	EOS,
	/**
	 * Token for RAW content.
	 */
	RAW,
	/**
	 * Token for an delimiter of an Setting or Alias (equals "::").
	 */
	DELIMITER
}
