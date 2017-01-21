package ragnarok.core;

/**
 * This enum defines all different teams and their relationships.
 *
 * @author Dominik Fuchss
 *
 */
public enum Team {
	/**
	 * The player itself.
	 */
	PLAYER,
	/**
	 * An enemy of the player.
	 */
	ENEMY,
	/**
	 * An inanimate.
	 */
	INANIMATE,
	/**
	 * A pickup.
	 */
	PICKUP,
	/**
	 * A neutral object.
	 */
	NEUTRAL,
	/**
	 * A trigger.
	 */
	TRIGGER;
	/**
	 * Check whether team t is a hostile of this team.
	 *
	 * @param t
	 *            the other team
	 * @return {@code true}, if hostile {@code false} otherwise
	 */
	public final boolean isHostile(Team t) {
		if (this == PLAYER) {
			return t == ENEMY || t == PICKUP || this == TRIGGER;
		}
		if (this == ENEMY || this == PICKUP || this == TRIGGER) {
			return t == PLAYER;
		}
		return false;
	}

	/**
	 * Is this the {@link Team#NEUTRAL Neutral-Team}.
	 *
	 * @return {@code true} if neutral, {@code false} otherwise
	 */
	public final boolean isNeutral() {
		return this == Team.NEUTRAL;
	}
}
