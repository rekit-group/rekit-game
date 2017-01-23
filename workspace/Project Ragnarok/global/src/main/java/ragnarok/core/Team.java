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
	PLAYER((byte) 100, (byte) 100),
	/**
	 * An enemy of the player.
	 */
	ENEMY((byte) 50, (byte) 60),
	/**
	 * An inanimate.
	 */
	INANIMATE((byte) 0, (byte) 10),
	/**
	 * A pickup.
	 */
	PICKUP((byte) 20, (byte) 30),
	/**
	 * A background object.
	 */
	BACKGROUND((byte) -110, (byte) -100),
	/**
	 * A trigger.
	 */
	TRIGGER((byte) -120, (byte) -120),
	/**
	 * An special effect.
	 */
	EFFECT((byte) 110, (byte) 110);

	public final Range zRange;

	private Team(byte min, byte max) {
		this.zRange = new Range(min, max);
	}

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

	public final byte getZIndex(int hint) {
		return 0;
	}

	/**
	 * Is this the {@link Team#BACKGROUND Neutral-Team}.
	 *
	 * @return {@code true} if neutral, {@code false} otherwise
	 */
	public final boolean isNeutral() {
		return this == Team.BACKGROUND;
	}

	public static final class Range {
		public final byte min;
		public final byte max;
		public final byte std;

		private Range(byte min, byte max) {
			this.min = min;
			this.max = max;
			this.std = min;
		}

		public final byte normalize(int expected) {
			if (expected < this.min) {
				return this.min;
			}
			if (expected > this.max) {
				return this.max;
			}
			return (byte) expected;
		}

	}
}
