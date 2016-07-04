package edu.kit.informatik.ragnarok.logic.save;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class LevelManager {

	private Map<String, Level> levelMap;

	public LevelManager() {
		this.levelMap = new HashMap<String, Level>();
		this.loadFromFile();
	}

	public void addLevel(Level level) {
		this.levelMap.put(level.stringIdentifier, level);
		level.register(this);
	}

	public Level getInfiniteLevel() {
		return this.getLevelByIdentifier("infinite");
	}

	public Level getLOTDLevel() {
		Level level = this.getLevelByIdentifier("lotd");
		DateFormat levelOfTheDayFormat = new SimpleDateFormat("ddMMyyyy");
		int seed = Integer.parseInt(levelOfTheDayFormat.format(Calendar.getInstance().getTime()));
		level.setSeed(seed);

		return level;
	}

	public Level getArcadeLevel(int arcadeId) {
		return this.getLevelByIdentifier(arcadeId + "");
	}

	private Level getLevelByIdentifier(String identifier) {
		// If no entry for this specific level: create level instance
		if (!this.levelMap.containsKey(identifier)) {
			this.addLevel(new Level(identifier, 0));
		}
		return this.levelMap.get(identifier);
	}

	public int getLastUnlockedArcadeLevelId() {
		int id = 0;
		// check largest id where score is still above 0
		while (this.levelMap.containsKey(id + "") && this.levelMap.get(id + "").getHighScore() > 0) {
			id++;
		}
		return id;
	}

	public void contentChanged() {
		this.saveToFile();
	}

	private void loadFromFile() {
		// create Scanner from InputStream
		InputStream levelStream;
		try {
			levelStream = new FileInputStream(new File("levelManager.dat"));
		} catch (FileNotFoundException e) {
			System.err.println("Error while loading levelManager.dat for scores and saves: FileNotFound");
			e.printStackTrace();
			return;
		}
		Scanner scanner = new java.util.Scanner(levelStream);

		while (scanner.hasNext()) {
			String line = scanner.next();

			// input parse level from this lines content.
			Level level = Level.fromString(line);

			// Save this level in local data structure
			this.addLevel(level);
		}

		// close scanner after use to prevent java-typical resource-wasting ;)
		scanner.close();
	}

	private void saveToFile() {

	}

}
