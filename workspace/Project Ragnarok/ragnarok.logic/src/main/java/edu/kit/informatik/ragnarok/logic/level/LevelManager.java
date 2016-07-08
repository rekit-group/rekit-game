package edu.kit.informatik.ragnarok.logic.level;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

public class LevelManager {

	private final static Map<String, Level> levelMap;

	private final static File FILE = new File("levelManager.dat");

	private LevelManager() {

	}

	static {
		levelMap = new HashMap<String, Level>();
		LevelManager.loadFromFile();
	}

	public static void addLevel(Level level) {
		LevelManager.levelMap.put(level.stringIdentifier, level);
	}

	public static Level getInfiniteLevel() {
		return LevelManager.getLevelByIdentifier("infinite");
	}

	public static Level getLOTDLevel() {
		Level level = LevelManager.getLevelByIdentifier("lotd");
		DateFormat levelOfTheDayFormat = new SimpleDateFormat("ddMMyyyy");
		int seed = Integer.parseInt(levelOfTheDayFormat.format(Calendar.getInstance().getTime()));
		level.setSeed(seed);

		return level;
	}

	public static Level getArcadeLevel(int arcadeId) {
		return LevelManager.getLevelByIdentifier(arcadeId + "");
	}

	private static Level getLevelByIdentifier(String identifier) {
		// If no entry for this specific level: create level instance
		if (!LevelManager.levelMap.containsKey(identifier)) {
			LevelManager.addLevel(new Level(identifier, 0));
		}
		return LevelManager.levelMap.get(identifier);
	}

	public static int getLastUnlockedArcadeLevelId() {
		int id = 0;
		// check largest id where score is still above 0
		while (LevelManager.levelMap.containsKey(id + "") && LevelManager.levelMap.get(id + "").getHighScore() > 0) {
			id++;
		}
		return id;
	}

	public static int getLastArcadeLevelId() {
		return 2; // TODO could be dynamic?
	}

	public static void contentChanged() {
		LevelManager.saveToFile();
	}

	private static void loadFromFile() {
		try {
			// create InputStream from File
			InputStream levelStream;
			levelStream = new FileInputStream(LevelManager.FILE);

			// create Scanner from InputStream
			Scanner scanner = new Scanner(levelStream);

			// iterate lines
			while (scanner.hasNext()) {
				String line = scanner.next();

				// input parse level from this lines content.
				Level level = Level.fromString(line);

				// Save this level in local data structure
				LevelManager.addLevel(level);
			}

			// close scanner and FileInputStream after use to prevent
			// java-typical
			// resource-wasting ;)
			scanner.close();
			levelStream.close();
		} catch (FileNotFoundException e) {
			System.err.println("Error while opening " + LevelManager.FILE.getAbsolutePath() + " for scores and saves: FileNotFound");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Error while loading " + LevelManager.FILE.getAbsolutePath() + " for scores and saves: IOException");
			e.printStackTrace();
		}
	}

	public static String convertToString() {
		StringBuilder result = new StringBuilder();

		Iterator<Level> it = LevelManager.levelMap.values().iterator();
		while (it.hasNext()) {
			Level next = it.next();
			result.append(next.toString());
			result.append("\n");
		}

		return result.toString();
	}

	private static void saveToFile() {
		// create OutputStream
		OutputStream levelStream;
		try {
			levelStream = new FileOutputStream(LevelManager.FILE);
		} catch (FileNotFoundException e) {
			System.err.println("Error while opening " + LevelManager.FILE.getAbsolutePath() + " for saving scores and saves: FileNotFound");
			e.printStackTrace();
			return;
		}

		// get byte-array from String
		byte[] bytes = LevelManager.convertToString().getBytes();

		try {
			// write out contents to Buffer
			levelStream.write(bytes);

			// write buffer to actual File
			levelStream.flush();

			// close FileInputStream after use to prevent java-typical
			// resource-wasting ;)
			levelStream.close();
		} catch (IOException e) {
			System.err.println("Error while saving " + LevelManager.FILE.getAbsolutePath() + " for scores and saves: IOException");
			e.printStackTrace();
		}
	}

}
