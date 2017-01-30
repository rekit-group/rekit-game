package ragnarok.logic.level;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import ragnarok.config.GameConf;
import ragnarok.logic.level.Level.Type;

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
	private final static File USER_DATA = new File(GameConf.LVL_MGMT_FILE);

	/**
	 * Prevent instantiation.
	 */
	private LevelManager() {
	}

	// Load LevelManager
	static {
		try {
			LevelManager.loadAllLevels();
		} catch (IOException e) {
			GameConf.GAME_LOGGER.error("Could not load levels " + e.getMessage());
		}
		LevelManager.loadInfoFromFile();
	}

	/**
	 * Load all levels.
	 *
	 * @throws IOException
	 *             iff wrong path.
	 */
	private static void loadAllLevels() throws IOException {
		PathMatchingResourcePatternResolver resolv = new PathMatchingResourcePatternResolver();
		Resource[] res = resolv.getResources("/levels/*");
		Arrays.stream(res).sorted((r1, r2) -> r1.toString().compareToIgnoreCase(r2.toString())).forEach(LevelManager::addLevel);

	}

	/**
	 * Add a level by resource.
	 *
	 * @param level
	 *            the resource
	 */
	private static void addLevel(Resource level) {
		try {
			LevelManager.addLevel(level.getFilename(), level.getInputStream());
		} catch (IOException e) {
			GameConf.GAME_LOGGER.error("Loading of " + level + " failed");
		}
	}

	/**
	 * Add a level by structure-file.
	 *
	 * @param name
	 *            the name of the level
	 * @param levelStructure
	 *            the level structure
	 */
	public static synchronized void addLevel(String name, InputStream levelStructure) {
		if (name.contains("infinite")) {
			// Infinite
			LevelManager.addLevel(new Level(name, levelStructure, Type.INFINITE));

			// LOTD
			Level lotd = new Level(name, levelStructure, Type.LOTD);
			DateFormat levelOfTheDayFormat = new SimpleDateFormat("ddMMyyyy");
			int seed = Integer.parseInt(levelOfTheDayFormat.format(Calendar.getInstance().getTime()));
			lotd.setSeed(seed);
			LevelManager.addLevel(lotd);
			return;
		}
		LevelManager.addLevel(new Level(name, levelStructure, Type.ARCADE));
	}

	/**
	 * Get the infinite level.
	 *
	 * @return the infinite level
	 */
	public static Level getInfiniteLevel() {
		return LevelManager.getLevelById("" + Level.Type.INFINITE);
	}

	/**
	 * Get the level-of-the-day level.
	 *
	 * @return the level-of-the-day level
	 */
	public static Level getLOTDLevel() {
		return LevelManager.getLevelById("" + Level.Type.LOTD);
	}

	/**
	 * Get an arcade level by id.
	 *
	 * @param arcadeId
	 *            the id
	 * @return the arcade level
	 */
	public static Level getArcadeLevel(int arcadeId) {
		return LevelManager.getLevelById(Level.Type.ARCADE + "-" + arcadeId);
	}

	/**
	 * Load a level by id.
	 *
	 * @param id
	 *            the level
	 * @return the level
	 */
	private static Level getLevelById(String id) {
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
		LevelManager.levelMap.put(level.getID(), level);
	}

	/**
	 * Get the number of arcade levels.
	 *
	 * @return the number of arcade levels
	 */
	public static int getNumberOfArcadeLevels() {
		return (int) LevelManager.levelMap.values().stream().filter(level -> level.getType() == Level.Type.ARCADE).count();
	}

	/**
	 * This method shall be invoked to signalize a content change in a level.
	 */
	public static void contentChanged() {
		LevelManager.saveToFile();
	}

	/**
	 * Load Highscores / Info from file.
	 */
	private static void loadInfoFromFile() {
		try {
			// create Scanner from InputStream
			Scanner scanner = new Scanner(LevelManager.USER_DATA, Charset.defaultCharset().name());
			while (scanner.hasNextLine()) {
				String[] levelinfo = scanner.nextLine().split(":");
				String name = levelinfo[0];
				Level level = LevelManager.findByName(name);
				if (level != null) {
					level.setHighScore(Integer.parseInt(levelinfo[1]));
				}
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			GameConf.GAME_LOGGER.error("Error while opening " + LevelManager.USER_DATA.getAbsolutePath() + " for scores and saves: FileNotFound");
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
			result.append(next.getName() + ":" + next.getHighScore());
			result.append("\n");
		}
		return result.toString();
	}

	/**
	 * Find level by name.
	 *
	 * @param name
	 *            the name
	 * @return the level or {@code null} if none found
	 */
	private static Level findByName(String name) {
		List<Level> levels = LevelManager.levelMap.values().stream().filter(level -> level.getName().equals(name)).collect(Collectors.toList());
		if (levels.isEmpty()) {
			return null;
		}
		return levels.get(0);
	}

	/**
	 * Save state to {@link #USER_DATA}.
	 */
	private static void saveToFile() {
		// create OutputStream
		OutputStream levelStream = null;
		try {
			levelStream = new FileOutputStream(LevelManager.USER_DATA);
		} catch (IOException e) {
			GameConf.GAME_LOGGER
					.error("Error while opening " + LevelManager.USER_DATA.getAbsolutePath() + " for saving scores and saves: FileNotFound");
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
			GameConf.GAME_LOGGER.error("Error while saving " + LevelManager.USER_DATA.getAbsolutePath() + " for scores and saves: IOException");
			try {
				levelStream.close();
			} catch (IOException e1) {
			}
		}
	}

}
