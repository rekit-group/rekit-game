package edu.kit.informatik.ragnarok.logic.save;

import java.util.Random;

import edu.kit.informatik.ragnarok.logic.levelcreator.LevelAssembler;

public class Level {

	/**
	 * Name of the level that is used as extrinsic state and filename for level
	 * structure template.
	 */
	public String stringIdentifier;
	private int levelSeed;
	private LevelManager mediator;
	private int highScore = 0;
	private static Random RNG = new Random();

	public Level(String stringIdentifier, int highScore) {
		this.stringIdentifier = stringIdentifier;
		this.levelSeed = Level.RNG.nextInt();
		this.highScore = highScore;
	}

	public void register(LevelManager mediator) {
		this.mediator = mediator;
	}

	/**
	 * Optional invokable method to set the seed for all randomized actions of
	 * the level generation.
	 *
	 * @param seed
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
		}
	}

	/**
	 * Notification method that informs the LevelManager mediator that some
	 * content changed.
	 */
	public void notifyChange() {
		if (this.mediator != null) {
			this.mediator.contentChanged();
		}
	}

	/**
	 * Creates and returns a LevelAssember for this level.
	 *
	 * @return
	 */
	public LevelAssembler getLevelAssember() {
		return new LevelAssembler(this.stringIdentifier, this.levelSeed);
	}

	@Override
	public String toString() {
		return this.stringIdentifier + ":{score:" + this.highScore + "}";
	}

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