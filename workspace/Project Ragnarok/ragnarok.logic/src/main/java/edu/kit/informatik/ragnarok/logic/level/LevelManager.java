package edu.kit.informatik.ragnarok.logic.level;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

import edu.kit.informatik.ragnarok.config.GameConf;

/**
 *
 * This class manages all Level depended stuff as highscores etc.
 *
 */
public final class LevelManager {
	/**
	 * All known levels (ID -> Level).
	 */
	private final static Map<String, Level> levelMap = new HashMap<>();
	/**
	 * The global data file for the {@link LevelManager}.
	 */
	private final static File FILE = new File(GameConf.LVL_MGMT_FILE);

	/**
	 * Prevent instantiation.
	 */
	private LevelManager() {
	}

	// Load LevelManager
	static {
		LevelManager.loadFromFile();
	}

	/**
	 * Get the infinite level.
	 *
	 * @return the infinite level
	 */
	public static Level getInfiniteLevel() {
		return LevelManager.getLevelById("infinite");
	}

	/**
	 * Get the level of the day.
	 *
	 * @return the level of the day
	 */
	public static Level getLOTDLevel() {
		Level level = LevelManager.getLevelById("lotd");
		DateFormat levelOfTheDayFormat = new SimpleDateFormat("ddMMyyyy");
		int seed = Integer.parseInt(levelOfTheDayFormat.format(Calendar.getInstance().getTime()));
		level.setSeed(seed);
		return level;
	}

	/**
	 * Get an arcade level by id.
	 *
	 * @param arcadeId
	 *            the id
	 * @return the arcade level
	 */
	public static Level getArcadeLevel(int arcadeId) {
		return LevelManager.getLevelById(arcadeId + "");
	}

	/**
	 * Load a level by id.
	 *
	 * @param id
	 *            the level
	 * @return the level or a newly created level with that id
	 */
	private static Level getLevelById(String id) {
		// If no entry for this specific level: create level instance
		if (!LevelManager.levelMap.containsKey(id)) {
			LevelManager.addLevel(new Level(id, 0));
		}
		return LevelManager.levelMap.get(id);
	}

	/**
	 * Add a level to the manager.
	 *
	 * @param level
	 *            the level
	 */
	public static void addLevel(Level level) {
		if (level == null) {
			return;
		}
		LevelManager.levelMap.put(level.stringIdentifier, level);
	}

	/**
	 * Get the last unlocked arcade level.
	 *
	 * @return the number of the last unlocked arcade level or {@code -1} if
	 *         none unlocked
	 */
	public static int getLastUnlockedArcadeLevelId() {
		// check largest id where score is still above 0
		for (int id = 0;; id++) {
			if (!(LevelManager.levelMap.containsKey(id + "") && LevelManager.levelMap.get(id + "").getHighScore() > 0)) {
				return id - 1;
			}
		}
	}

	/**
	 * Get the number of arcade levels.
	 *
	 * @return the number of arcade levels
	 */
	public static int getNumberOfArcadeLevels() {
		for (int num = 0;; num++) {
			if (LevelManager.class.getResourceAsStream("/level_" + num + ".dat") == null) {
				return num;
			}
		}
	}

	/**
	 * This method shall be invoked to signalize a content change in a level.
	 */
	public static void contentChanged() {
		LevelManager.saveToFile();
	}

	/**
	 * Load Highscores / Infos from file.
	 */
	private static void loadFromFile() {
		try {
			// create Scanner from InputStream
			Scanner scanner = new Scanner(LevelManager.FILE, Charset.defaultCharset().name());
			// iterate lines
			while (scanner.hasNext()) {
				String line = scanner.next();
				// input parse level from this lines content.
				Level level = Level.fromString(line);
				// Save this level in local data structure
				LevelManager.addLevel(level);
			}
			// close scanner after use to prevent resource-wasting
			scanner.close();
		} catch (FileNotFoundException e) {
			System.err.println("Error while opening " + LevelManager.FILE.getAbsolutePath() + " for scores and saves: FileNotFound");
		}
	}

	/**
	 * Convert current state of LevelManager to a representing string.
	 *
	 * @return the representing string
	 */
	private static String convertToString() {
		StringBuilder result = new StringBuilder();
		Iterator<Level> it = LevelManager.levelMap.values().iterator();
		while (it.hasNext()) {
			Level next = it.next();
			result.append(next.toString());
			result.append("\n");
		}
		return result.toString();
	}

	/**
	 * Save state to {@link #FILE}.
	 */
	private static void saveToFile() {
		// create OutputStream
		OutputStream levelStream = null;
		try {
			levelStream = new FileOutputStream(LevelManager.FILE);
		} catch (IOException e) {
			System.err.println("Error while opening " + LevelManager.FILE.getAbsolutePath() + " for saving scores and saves: FileNotFound");
			return;
		}
		// get byte-array from String
		byte[] bytes = LevelManager.convertToString().getBytes(Charset.defaultCharset());
		try {
			// write out contents to Buffer
			levelStream.write(bytes);
			// write buffer to actual File
			levelStream.flush();
			// close FileInputStream after use to prevent resource-wasting
			levelStream.close();
		} catch (IOException e) {
			System.err.println("Error while saving " + LevelManager.FILE.getAbsolutePath() + " for scores and saves: IOException");
			try {
				levelStream.close();
			} catch (IOException e1) {
			}
		}
	}

}
