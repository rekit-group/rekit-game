package rekit.core;

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
	ENEMY((byte) 110, (byte) 120),
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
	TRIGGER((byte) -120, (byte) -90),
	/**
	 * An special effect.
	 */
	EFFECT((byte) 127, (byte) 127);
	/**
	 * The z-range of this type of Element.
	 */
	public final Range zRange;

	/**
	 * Create a team by its min and max z-layer.
	 *
	 * @param min
	 *            the min z-layer
	 * @param max
	 *            the max z-layer
	 */
	Team(byte min, byte max) {
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
			return t == ENEMY || t == PICKUP || t == TRIGGER || t == INANIMATE;
		}
		if (this == ENEMY || this == PICKUP || this == TRIGGER || this == INANIMATE) {
			return t == PLAYER;
		}
		return false;
	}

	/**
	 * Is this the {@link Team} neutral (no interactions possible).
	 *
	 * @return {@code true} if neutral, {@code false} otherwise
	 */
	public final boolean isNeutral() {
		return this == Team.BACKGROUND || this == Team.EFFECT;
	}

	/**
	 * This class defines a range to define z-layers.
	 *
	 * @author Dominik Fuchss
	 *
	 */
	public static final class Range {
		/**
		 * The most behind z-layer.
		 */
		public final byte min;
		/**
		 * The most in-front-of z-layer.
		 */
		public final byte max;
		/**
		 * The standard z-layer.
		 */
		public final byte std;

		/**
		 * Create a range.
		 *
		 * @param min
		 *            the {@link #min} and {@link #std}
		 * @param max
		 *            the {@link #max}
		 */
		private Range(byte min, byte max) {
			this(min, max, min);
		}

		/**
		 * Create a range.
		 *
		 * @param min
		 *            the {@link #min}
		 * @param max
		 *            the {@link #max}
		 * @param std
		 *            the standard value {@link #std}
		 */
		private Range(byte min, byte max, byte std) {
			this.min = min;
			this.max = max;
			this.std = std;
		}

		/**
		 * Normalize to {@link Range}.
		 *
		 * @param expected
		 *            the expected z-layer.
		 * @return the actual z-layer (trimmed to {@code this}
		 */
		public byte normalize(int expected) {
			if (expected < this.min) {
				return this.min;
			}
			if (expected > this.max) {
				return this.max;
			}
			return (byte) expected;
		}

		/**
		 * Get the standard value.
		 *
		 * @return the std value
		 */
		public byte getStd() {
			return this.std;
		}
	}
}
