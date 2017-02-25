package ragnarok.logic.level;

import java.io.IOException;
import java.io.InputStream;

import ragnarok.config.GameConf;

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
		INFINITE,
		/**
		 * Level of the day.
		 */
		LOTD,
		/**
		 * Arcade level.
		 */
		ARCADE;
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
	}

	/**
	 * Name of the level that is used as extrinsic state and filename for level
	 * structure template.
	 */
	private String stringID;
	/**
	 * The level's seed.
	 */
	private int levelSeed;
	/**
	 * The level's highscore.
	 */
	private int highScore = 0;
	/**
	 * The level's assebler.
	 */
	private LevelAssembler levelAssembler;
	/**
	 * The type of the level.
	 */
	private final Type type;
	/**
	 * The structure data.
	 */
	private final InputStream data;
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
	 * @param name
	 *            the name
	 * @param levelStructure
	 *            the structure data
	 * @param type
	 *            the type
	 */
	public Level(String name, InputStream levelStructure, Type type) {
		this.type = type;
		this.data = levelStructure;
		this.name = name;
		if (type == Type.INFINITE || type == Type.LOTD) {
			this.stringID = "" + this.type;
		} else {
			this.stringID = Type.ARCADE + "-" + Level.nextLevel();
		}
		this.levelSeed = GameConf.PRNG.nextInt();
		this.highScore = 0;
	}

	/**
	 * Optional invokable method to set the seed for all randomized actions of
	 * the level generation.
	 *
	 * @param seed
	 *            the seed
	 */
	public void setSeed(int seed) {
		this.levelSeed = seed;
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
			this.notifyChange();
		}
	}

	/**
	 * Notification method that informs the LevelManager mediator that some
	 * content changed.
	 */
	public void notifyChange() {
		LevelManager.contentChanged();
	}

	/**
	 * Creates and returns a LevelAssember for this level if not created
	 * already. Singleton.
	 *
	 * @return the only instance of a LevelAssembler
	 */
	public LevelAssembler getLevelAssember() {
		if (this.levelAssembler == null) {
			try {
				this.levelAssembler = new LevelAssembler(this.data, this.levelSeed);
			} catch (IOException e) {
				GameConf.GAME_LOGGER.error("Cannot instantiate level assembler for level " + this);
			}
		}
		return this.levelAssembler;
	}

	/**
	 * Initialize level & generate level.
	 */
	public void init() {
		this.getLevelAssember().init();
		// TODO this value has to be changed to generate structures in a block
		// See Lv. 4 as example that periods are not synchronized if *2 not
		// written
		this.getLevelAssember().generate(GameConf.GRID_W * 2);
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
		return this.stringID;
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
		boolean thisIsNumbered = this.isNumbered(), otherIsNumbered = o.isNumbered();
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
	 * Indicates whether the level is numbered.
	 *
	 * @return {@code true} if numbered.
	 */
	private final boolean isNumbered() {
		return this.getName().matches("level_\\d+\\.dat");
	}
}