package rekit.logic.level;

import java.io.ByteArrayInputStream;
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

import rekit.config.GameConf;
import rekit.logic.gameelements.GameElement;
import rekit.logic.gameelements.inanimate.Inanimate;
import rekit.logic.gameelements.type.Boss;
import rekit.logic.level.Level.Type;
import rekit.logic.level.parser.token.UnexpectedTokenException;

/**
 *
 * This class manages all Level depended stuff as highscores etc.
 *
 */
public final class LevelManager {
	/**
	 * All known levels (ID -> Level).
	 */
	private static final Map<String, Level> LEVEL_MAP = new HashMap<>();
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
		Resource[] res = resolv.getResources("/levels/level*");
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
	 * Load {@link Type#Infinite_Fun} and {@link Type#Level_of_the_Day} levels.
	 *
	 * @throws IOException
	 *             will thrown if Resources are not accessible
	 */
	private static void loadInfiniteLevels() throws IOException {
		PathMatchingResourcePatternResolver resolv = new PathMatchingResourcePatternResolver();
		Resource level = resolv.getResource("/levels/infinite.dat");
		// Infinite
		LevelManager.addLevel(new Level(level.getInputStream(), Type.Infinite_Fun));
		// LOTD
		DateFormat levelOfTheDayFormat = new SimpleDateFormat("ddMMyyyy");
		int seed = Integer.parseInt(levelOfTheDayFormat.format(Calendar.getInstance().getTime()));
		Level lotd = new Level(level.getInputStream(), Type.Level_of_the_Day, seed);
		LevelManager.addLevel(lotd);

	}

	/**
	 * Load {@link Type#Boss_Rush} level.
	 *
	 * @throws IOException
	 *             will thrown if Resources are not accessible
	 */
	private static void loadBossRushLevel() throws IOException {
		LevelManager.addLevel(new Level(LevelManager.bossRushManager(GameConf.PRNG.nextInt()), Type.Boss_Rush));
	}

	/**
	 * Get the {@link Type#Boss_Rush} structure.
	 *
	 * @param seed
	 *            the rnd seed
	 * @return the resulting {@link StructureManager}
	 * @throws IOException
	 *             will thrown by
	 *             {@link StructureManager#load(InputStream, int)}
	 */
	private static StructureManager bossRushManager(int seed) throws IOException {
		StringBuilder builder = new StringBuilder();
		builder.append("#SETTING::infinite->true").append("\n");
		int idx = 5;
		for (GameElement boss : Boss.getPrototypes()) {
			builder.append("#BOSS_SETTING::AT" + (idx += 50) + "->" + boss.getClass().getSimpleName()).append("\n");
		}
		builder.append("{{").append(Inanimate.class.getSimpleName()).append("}}");
		ByteArrayInputStream is = new ByteArrayInputStream(builder.toString().getBytes());
		return StructureManager.load(is, seed);
	}

	/**
	 * Add a level by resource.
	 *
	 * @param level
	 *            the resource
	 */
	private static void addLevel(Resource level) {
		try {
			LevelManager.addArcadeLevel(level.getInputStream());
		} catch (IOException | UnexpectedTokenException e) {
			GameConf.GAME_LOGGER.error("Loading of " + level + " failed: " + e.getMessage());
		}
	}

	/**
	 * Add a level by structure-file.
	 *
	 * @param levelStructure
	 *            the level structure
	 * @throws IOException
	 *             will thrown if Resources are not accessible
	 * @throws UnexpectedTokenException
	 *             will thrown if syntax of Resources are wrong
	 */
	public static synchronized void addArcadeLevel(InputStream levelStructure) throws UnexpectedTokenException, IOException {
		if (!LevelManager.initialized) {
			return;
		}
		LevelManager.addLevel(new Level(levelStructure, Type.Arcade));
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
		return LevelManager.getLevelById("" + Level.Type.Infinite_Fun);
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
		return LevelManager.getLevelById("" + Level.Type.Level_of_the_Day);
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
		return LevelManager.getLevelById("" + Level.Type.Boss_Rush);
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
		return LevelManager.getLevelById(Level.Type.Arcade + "-" + arcadeId);
	}

	/**
	 * Load a level by id.
	 *
	 * @param id
	 *            the level
	 * @return the level
	 */
	private static Level getLevelById(String id) {
		return LevelManager.LEVEL_MAP.get(id);
	}

	/**
	 * Add a level to the manager.
	 *
	 * @param level
	 *            the level
	 */
	public static synchronized void addLevel(Level level) {
		if (level == null) {
			return;
		}
		LevelManager.LEVEL_MAP.put(level.getID(), level);
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
		return (int) LevelManager.LEVEL_MAP.values().stream().filter(Level.Type.Arcade::hasType).count();
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
				String id = levelinfo[0];
				Type type = Type.byString(levelinfo[1]);
				Level level = LevelManager.findByIDAndType(id, type);
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
		Iterator<Level> it = LevelManager.LEVEL_MAP.values().stream().sorted().iterator();
		while (it.hasNext()) {
			Level next = it.next();
			result.append(next.getID() + ":" + next.getType() + ":" + next.getHighScore());
			result.append("\n");
		}
		return result.toString();
	}

	/**
	 * Find level by name and type.
	 *
	 * @param id
	 *            the id
	 * @param type
	 *            the type
	 * @return the level or {@code null} if none found
	 */
	private static Level findByIDAndType(String id, Type type) {
		if (id == null || type == null) {
			return null;
		}
		List<Level> levels = LevelManager.LEVEL_MAP.values().stream().filter(type::hasType).filter(level -> level.getID().equals(id))
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
