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
import java.util.stream.Stream;

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
	private static final Map<String, Level> levelMap = new HashMap<>();
	/**
	 * The global data file for the {@link LevelManager}.
	 */
	private static final File USER_DATA = new File(GameConf.LVL_MGMT_FILE);

	/**
	 * Prevent instantiation.
	 */
	private LevelManager() {
	}

	/**
	 * Indicates whether the {@link LevelManager} is initialized.
	 */
	private static boolean initialized = false;

	/**
	 * Load levels.
	 */
	public static final synchronized void init() {
		if (LevelManager.initialized) {
			return;
		}
		LevelManager.initialized = true;
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
		Stream<Resource> numbered = Arrays.stream(res).filter(r -> r.getFilename().matches("level_\\d+\\.dat"));
		Stream<Resource> notNumbered = Arrays.stream(res).filter(r -> !r.getFilename().matches("level_\\d+\\.dat"));

		LevelManager.loadInfiniteLevels();
		LevelManager.loadBossRushLevel();

		numbered.sorted((r1, r2) -> {
			String n1 = r1.getFilename(), n2 = r2.getFilename();
			n1 = n1.substring("level_".length()).split("\\.")[0];
			n2 = n2.substring("level_".length()).split("\\.")[0];
			return Integer.compare(Integer.parseInt(n1), Integer.parseInt(n2));
		}).forEach(LevelManager::addLevel);

		notNumbered.sorted((r1, r2) -> r1.toString().compareToIgnoreCase(r2.toString())).forEach(LevelManager::addLevel);

	}

	/**
	 * Load {@link Type#INFINITE} and {@link Type#LOTD} levels.
	 *
	 * @throws IOException
	 *             will thrown if Resources are not accessible
	 */
	private static void loadInfiniteLevels() throws IOException {
		PathMatchingResourcePatternResolver resolv = new PathMatchingResourcePatternResolver();
		Resource level = resolv.getResource("/levels/infinite.dat");
		// Infinite
		LevelManager.addLevel(new Level(level.getFilename(), level.getInputStream(), Type.INFINITE));
		// LOTD
		Level lotd = new Level(level.getFilename(), level.getInputStream(), Type.LOTD);
		DateFormat levelOfTheDayFormat = new SimpleDateFormat("ddMMyyyy");
		int seed = Integer.parseInt(levelOfTheDayFormat.format(Calendar.getInstance().getTime()));
		lotd.setSeed(seed);
		LevelManager.addLevel(lotd);

	}

	/**
	 * Load {@link Type#BOSS_RUSH} level.
	 *
	 * @throws IOException
	 *             will thrown if Resources are not accessible
	 */
	private static void loadBossRushLevel() {
		LevelManager.addLevel(new Level(null, null, Type.BOSS_RUSH));
	}

	/**
	 * Add a level by resource.
	 *
	 * @param level
	 *            the resource
	 */
	private static void addLevel(Resource level) {
		try {
			LevelManager.addArcadeLevel(level.getFilename(), level.getInputStream());
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
	public static synchronized void addArcadeLevel(String name, InputStream levelStructure) {
		if (!LevelManager.initialized) {
			return;
		}
		LevelManager.addLevel(new Level(name, levelStructure, Type.ARCADE));
	}

	/**
	 * Get the infinite level.
	 *
	 * @return the infinite level
	 */
	public static synchronized Level getInfiniteLevel() {
		if (!LevelManager.initialized) {
			return null;
		}
		return LevelManager.getLevelById("" + Level.Type.INFINITE);
	}

	/**
	 * Get the level-of-the-day level.
	 *
	 * @return the level-of-the-day level
	 */
	public static synchronized Level getLOTDLevel() {
		if (!LevelManager.initialized) {
			return null;
		}
		return LevelManager.getLevelById("" + Level.Type.LOTD);
	}

	/**
	 * Get the boss rush level.
	 *
	 * @return the level-of-the-day level
	 */
	public static synchronized Level getBossRushLevel() {
		if (!LevelManager.initialized) {
			return null;
		}
		return LevelManager.getLevelById("" + Level.Type.BOSS_RUSH);
	}

	/**
	 * Get an arcade level by id.
	 *
	 * @param arcadeId
	 *            the id
	 * @return the arcade level
	 */
	public static synchronized Level getArcadeLevel(int arcadeId) {
		if (!LevelManager.initialized) {
			return null;
		}
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
	private static void addLevel(Level level) {
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
	public static synchronized int getNumberOfArcadeLevels() {
		if (!LevelManager.initialized) {
			return 0;
		}
		return (int) LevelManager.levelMap.values().stream().filter(Level.Type.ARCADE::hasType).count();
	}

	/**
	 * This method shall be invoked to signalize a content change in a level.
	 */
	public static synchronized void contentChanged() {
		if (!LevelManager.initialized) {
			return;
		}
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
				if (levelinfo.length != 3) {
					continue;
				}
				String name = levelinfo[0];
				Type type = Type.byString(levelinfo[1]);
				Level level = LevelManager.findByNameAndType(name, type);
				if (level != null) {
					level.setHighScore(Integer.parseInt(levelinfo[2]));
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
		Iterator<Level> it = LevelManager.levelMap.values().stream().sorted().iterator();
		while (it.hasNext()) {
			Level next = it.next();
			result.append(next.getName() + ":" + next.getType() + ":" + next.getHighScore());
			result.append("\n");
		}
		return result.toString();
	}

	/**
	 * Find level by name and type.
	 *
	 * @param name
	 *            the name
	 * @param type
	 *            the type
	 * @return the level or {@code null} if none found
	 */
	private static Level findByNameAndType(String name, Type type) {
		List<Level> levels = LevelManager.levelMap.values().stream().filter(type::hasType).filter(level -> level.getName().equals(name))
				.collect(Collectors.toList());
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
