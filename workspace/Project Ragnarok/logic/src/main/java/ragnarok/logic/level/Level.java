package ragnarok.logic.level;

import ragnarok.config.GameConf;

/**
 *
 * This class holds all necessary information about a level.
 *
 */
public class Level {

	/**
	 * Name of the level that is used as extrinsic state and filename for level
	 * structure template.
	 */
	public String stringIdentifier;
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
	 * Create a new Level.
	 *
	 * @param stringIdentifier
	 *            the id
	 * @param highScore
	 *            the highscore
	 */
	public Level(String stringIdentifier, int highScore) {
		this.stringIdentifier = stringIdentifier;
		this.levelSeed = GameConf.PRNG.nextInt();
		this.highScore = highScore;
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
			// TODO Not rly cool
			String actualStringIdentifier = this.stringIdentifier.equals("lotd") ? "infinite" : this.stringIdentifier;
			this.levelAssembler = new LevelAssembler(actualStringIdentifier, this.levelSeed);
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
		return this.stringIdentifier + ":{score:" + this.highScore + "}";
	}

	/**
	 * Load level by string (definition).
	 *
	 * @param in
	 *            the input string
	 * @return the level or {@code null} if fails.
	 */
	public static Level fromString(String in) {
		String stringIdentifier = "";
		int highScore = 0;

		// split "name:{.:.,.:.}" --> "name", "{.:.,.:.}"
		String[] mainKeyData = in.split(":", 2);
		stringIdentifier = mainKeyData[0];

		// split "{.:.,.:.}" --> "{.:.}, "{.:.}"
		String[] entries = mainKeyData[1].substring(1, mainKeyData[1].length() - 1).split(",");

		// iterate all entries
		for (String entry : entries) {
			String[] keyVal = entry.split(":");
			switch (keyVal[0]) {
			case "score":
				highScore = Integer.parseInt(keyVal[1]);
				break;
			}
		}
		return new Level(stringIdentifier, highScore);
	}

}