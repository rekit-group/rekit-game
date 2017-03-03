package ragnarok.logic.level;

import java.io.IOException;
import java.io.InputStream;

import ragnarok.config.GameConf;
import ragnarok.logic.level.parser.token.UnexpectedTokenException;

/**
 *
 * This class holds all necessary information about a level.
 *
 */
public final class Level implements Comparable<Level> {

	/**
	 * The type of a level.
	 *
	 * @author Dominik Fuchss
	 *
	 */
	public enum Type {
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
		public static Type byString(String string) {
			try {
				return Type.valueOf(string);
			} catch (IllegalArgumentException e) {
				return null;
			}
		}

		/**
		 * Indicate whether level has this type.
		 *
		 * @param lv
		 *            the level
		 * @return {@code true} if lv has this level type
		 */
		public final boolean hasType(Level lv) {
			if (lv == null) {
				return false;
			}
			return this == lv.getType();
		}

		/**
		 * Get the next identifier for this type of level.
		 *
		 * @return the next identifier
		 */
		private String getID() {
			if (this != Type.Arcade) {
				return this.toString();
			} else {
				return Type.Arcade + "-" + Level.nextLevel();
			}
		}
	}

	/**
	 * Name of the level that is used as extrinsic state and filename for level
	 * structure template.
	 */
	private final String id;
	/**
	 * The level's highscore.
	 */
	private int highScore = 0;
	/**
	 * The structure manager.
	 */
	private final StructureManager structureManager;

	/**
	 * The level's assebler.
	 */
	private final LevelAssembler levelAssembler;
	/**
	 * The type of the level.
	 */
	private final Type type;
	/**
	 * The name of the level (file).
	 */
	private final String name;
	/**
	 * The arcade counter.
	 */
	private static int ARCADE_CTR = 0;

	/**
	 * Get the next level number.
	 *
	 * @return the next level number
	 * @see #ARCADE_CTR
	 */
	private static final int nextLevel() {
		return ++Level.ARCADE_CTR;
	}

	/**
	 * Create a new level by data and type.
	 *
	 * @param levelStructure
	 *            the structure data
	 * @param type
	 *            the type
	 * @throws IOException
	 *             iff file cannot accessed
	 * @throws UnexpectedTokenException
	 *             iff syntax is wrong
	 */
	public Level(InputStream levelStructure, Type type) throws UnexpectedTokenException, IOException {
		this(levelStructure, type, GameConf.PRNG.nextInt());
	}

	/**
	 * Create a new level by data and type.
	 *
	 * @param levelStructure
	 *            the structure data
	 * @param type
	 *            the type
	 * @param seed
	 *            the rnd seed
	 * @throws IOException
	 *             iff file cannot accessed
	 * @throws UnexpectedTokenException
	 *             iff syntax is wrong
	 */
	public Level(InputStream levelStructure, Type type, int seed) throws UnexpectedTokenException, IOException {
		this(StructureManager.load(levelStructure, seed), type);
	}

	/**
	 * Create a new level by data and type.
	 *
	 * @param manager
	 *            the structure manager
	 * @param type
	 *            the type
	 */
	public Level(StructureManager manager, Type type) {
		this.type = type;
		this.structureManager = manager;
		this.levelAssembler = new LevelAssembler(this.structureManager);
		this.levelAssembler.reset();
		String name = this.structureManager.getSettingValue("name");
		this.id = type.getID();
		this.name = (name == null ? this.id : name).replace('_', ' ');
		this.highScore = 0;

	}

	/**
	 * Reset level.
	 */
	public void reset() {
		this.levelAssembler.reset();
		this.structureManager.reset();
	}

	/**
	 * Getter of the score of this Level.
	 *
	 * @return the currently highest score of this level
	 */
	public int getHighScore() {
		return this.highScore;
	}

	/**
	 * Setter for saving a new highScore if its higher than the old HighScore.
	 *
	 * @param highScore
	 *            the HighScore that will be saved if it's better than the last.
	 */
	public void setHighScore(int highScore) {
		if (highScore > this.highScore) {
			this.highScore = highScore;
			LevelManager.contentChanged();
		}
	}

	/**
	 * Get the associate {@link Configurable}.
	 *
	 * @return the configurable
	 */
	public Configurable getConfigurable() {
		return this.structureManager;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
		result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
		result = prime * result + ((this.type == null) ? 0 : this.type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || this.getClass() != obj.getClass()) {
			return false;
		}
		Level other = (Level) obj;
		return this.name.equals(other.name) && this.id.equals(other.id) && this.type == other.type;
	}

	@Override
	public String toString() {
		return this.name + ":{score:" + this.highScore + "}";
	}

	/**
	 * Get the level id.
	 *
	 * @return the id
	 */
	public String getID() {
		return this.id;
	}

	/**
	 * Get the level type.
	 *
	 * @return the type
	 */
	public Type getType() {
		return this.type;
	}

	/**
	 * Get the level name.
	 *
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	@Override
	public int compareTo(Level o) {
		boolean thisIsNumbered = this.getName().matches("level_\\d+\\.dat"), otherIsNumbered = o.getName().matches("level_\\d+\\.dat");
		if (thisIsNumbered != otherIsNumbered) {
			// NonNumbered before Numbered
			return otherIsNumbered ? -1 : 1;
		}
		// Numbered will be compared by number
		if (thisIsNumbered) {
			String n1 = this.getName(), n2 = o.getName();
			n1 = n1.substring("level_".length()).split("\\.")[0];
			n2 = n2.substring("level_".length()).split("\\.")[0];
			return Integer.compare(Integer.parseInt(n1), Integer.parseInt(n2));
		}

		// Else order by string order (and secondly by type)
		return 2 * this.getName().compareTo(o.getName()) + (this.getType().compareTo(o.getType()));
	}

	/**
	 * Orders the {@link LevelAssembler} of the level to build {@link Structure
	 * Structures} if it must. This decision depends on the {@link Structure
	 * Structures} build so far and the parameter <i>max</i> that specifies
	 * which x position is the smallest that needs to be generated at.
	 *
	 * @param max
	 *            the lowest x position that must still be generated at.
	 * @see LevelAssembler#generate(int)
	 */
	public void generate(int max) {
		this.levelAssembler.generate(max);
	}

}