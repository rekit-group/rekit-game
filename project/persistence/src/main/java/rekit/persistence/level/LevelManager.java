package rekit.persistence.level;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Base64;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import rekit.config.GameConf;
import rekit.persistence.DirFileDefinitions;
import rekit.persistence.level.LevelDefinition.Type;
import rekit.persistence.level.token.UnexpectedTokenException;

/**
 *
 * This class manages all Level depended stuff as highscores etc.
 *
 */
public final class LevelManager {
	/**
	 * All known levels (ID -> Level).
	 */
	private static final Map<String, LevelDefinition> LEVEL_MAP = new HashMap<>();

	private static final Set<String> BANNED_IDS = new HashSet<>();

	private static LevelDefinition INFINITE = null;
	private static LevelDefinition LOTD = null;

	/**
	 * Prevent instantiation.
	 */
	private LevelManager() {
	}

	/**
	 * Indicates whether the {@link LevelManager} is initialized.
	 */
	private static boolean initialized = false;

	private static int ARCADE_NUM = 0;

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
		LevelManager.loadDataFromFile();

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

		numbered.sorted((r1, r2) -> {
			String n1 = r1.getFilename(), n2 = r2.getFilename();
			n1 = n1.substring("level_".length()).split("\\.")[0];
			n2 = n2.substring("level_".length()).split("\\.")[0];
			return Integer.compare(Integer.parseInt(n1), Integer.parseInt(n2));
		}).forEach(LevelManager::addArcadeLevel);

		notNumbered.sorted((r1, r2) -> r1.toString().compareToIgnoreCase(r2.toString())).forEach(LevelManager::addArcadeLevel);

		LevelManager.loadCustomLevels();

	}

	private static final void loadCustomLevels() {
		File[] levels = DirFileDefinitions.LEVEL_DIR.listFiles();
		if (levels == null) {
			return;
		}
		for (File lv : levels) {
			if (lv.getName().startsWith("level")) {
				try {
					LevelManager.addArcadeLevel(new FileInputStream(lv));
				} catch (IOException | UnexpectedTokenException e) {
					GameConf.GAME_LOGGER.error("Loading of " + lv.getName() + " failed: " + e.getMessage());
				}
			}
		}

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
		LevelManager.addLevel(LevelManager.INFINITE = new LevelDefinition(level.getInputStream(), Type.Infinite_Fun));
		// LOTD
		DateFormat levelOfTheDayFormat = new SimpleDateFormat("ddMMyyyy");
		int seed = Integer.parseInt(levelOfTheDayFormat.format(Calendar.getInstance().getTime()));
		LevelManager.addLevel(LevelManager.LOTD = new LevelDefinition(level.getInputStream(), Type.Level_of_the_Day, seed));
		LevelManager.BANNED_IDS.add(LevelManager.INFINITE.getID());
		LevelManager.BANNED_IDS.add(LevelManager.LOTD.getID());
	}

	/**
	 * Add a level by resource.
	 *
	 * @param level
	 *            the resource
	 */
	private static void addArcadeLevel(Resource level) {
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
		LevelManager.addLevel(new LevelDefinition(levelStructure, ++LevelManager.ARCADE_NUM));
	}

	/**
	 * Get the infinite level.
	 *
	 * @return the infinite level
	 */
	public static synchronized LevelDefinition getInfiniteLevel() {
		if (!LevelManager.initialized) {
			return null;
		}
		return LevelManager.INFINITE;
	}

	/**
	 * Get the level-of-the-day level.
	 *
	 * @return the level-of-the-day level
	 */
	public static synchronized LevelDefinition getLOTDLevel() {
		if (!LevelManager.initialized) {
			return null;
		}
		return LevelManager.LOTD;
	}

	/**
	 * Load a level by id.
	 *
	 * @param id
	 *            the level
	 * @return the level
	 */
	public static synchronized LevelDefinition getLevelById(String id) {
		return LevelManager.LEVEL_MAP.get(id);
	}

	public static synchronized List<String> getArcadeLevelIDs() {
		return LevelManager.LEVEL_MAP.entrySet().stream().filter(e -> !LevelManager.BANNED_IDS.contains(e.getKey()))
				.sorted((e1, e2) -> e1.getValue().compareTo(e2.getValue())).map(e -> e.getKey()).collect(Collectors.toList());
	}

	/**
	 * Add a level to the manager.
	 *
	 * @param level
	 *            the level
	 * @return the id of the level
	 */
	public static synchronized String addLevel(LevelDefinition level) {
		if (level == null) {
			return null;
		}
		LevelManager.LEVEL_MAP.put(level.getID(), level);
		LevelManager.loadDataFromFile();
		return level.getID();
	}

	/**
	 * This method shall be invoked to signalize a content change in a level.
	 */
	static synchronized void contentChanged() {
		if (!LevelManager.initialized) {
			return;
		}
		LevelManager.saveToFile();
	}

	/**
	 * Load Highscores / Info from file.
	 */
	private static void loadDataFromFile() {
		try {
			Scanner scanner = new Scanner(DirFileDefinitions.USER_DATA, Charset.defaultCharset().name());
			while (scanner.hasNextLine()) {
				String[] levelinfo = scanner.nextLine().split(":");
				if (levelinfo.length != DataKey.values().length + 1) {
					continue;
				}
				String id = levelinfo[0];
				LevelDefinition level = LevelManager.findByID(id);
				if (level == null) {
					continue;
				}
				DataKey[] keys = DataKey.values();
				for (int idx = 1; idx < levelinfo.length; idx++) {
					level.setData(keys[idx - 1], LevelManager.fromBase64(levelinfo[idx]), false);
				}

			}
			scanner.close();
		} catch (FileNotFoundException e) {
			GameConf.GAME_LOGGER.error("Error while opening " + DirFileDefinitions.USER_DATA.getAbsolutePath() + " for scores and saves: FileNotFound");
		}
	}

	/**
	 * Convert current state of LevelManager to a representing string.
	 *
	 * @return the representing string
	 */
	private static String convertToString() {
		StringBuilder result = new StringBuilder();
		for (LevelDefinition lvd : LevelManager.LEVEL_MAP.values()) {
			result.append(lvd.getID());
			for (DataKey dk : DataKey.values()) {
				Serializable data = lvd.getData(dk);
				result.append(":").append(LevelManager.toBase64(data));
			}
			result.append("\n");
		}
		return result.toString();
	}

	private static Serializable fromBase64(String s) {
		try {
			byte[] data = Base64.getDecoder().decode(s);
			ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
			Object o = ois.readObject();
			ois.close();
			return (Serializable) o;
		} catch (IOException | IllegalArgumentException | ClassNotFoundException e) {
			GameConf.GAME_LOGGER.error(e.getMessage());
			return null;
		}
	}

	private static String toBase64(Serializable o) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(o);
			oos.close();
			return Base64.getEncoder().encodeToString(baos.toByteArray());
		} catch (IOException e) {
			GameConf.GAME_LOGGER.error(e.getMessage());
			return null;
		}

	}

	/**
	 * Find level by name and type.
	 *
	 * @param id
	 *            the id
	 *
	 * @return the level or {@code null} if none found
	 */
	private static LevelDefinition findByID(String id) {
		if (id == null) {
			return null;
		}
		List<LevelDefinition> levels = LevelManager.LEVEL_MAP.values().stream().filter(level -> id.equals(level.getID())).collect(Collectors.toList());
		if (levels.isEmpty()) {
			return null;
		}
		return levels.get(0);
	}

	/**
	 * Save state to {@link #USER_DATA}.
	 */
	private static void saveToFile() {
		OutputStream levelStream = null;
		try {
			levelStream = new FileOutputStream(DirFileDefinitions.USER_DATA);
		} catch (IOException e) {
			GameConf.GAME_LOGGER.error("Error while opening " + DirFileDefinitions.USER_DATA.getAbsolutePath() + " for saving scores and saves: FileNotFound");
			return;
		}
		byte[] bytes = LevelManager.convertToString().getBytes(Charset.defaultCharset());
		try {
			levelStream.write(bytes);
			levelStream.flush();
			levelStream.close();
		} catch (IOException e) {
			GameConf.GAME_LOGGER.error("Error while saving " + DirFileDefinitions.USER_DATA.getAbsolutePath() + " for scores and saves: IOException");
		}
	}

}
