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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import rekit.config.GameConf;
import rekit.persistence.DirFileDefinitions;
import rekit.persistence.level.token.UnexpectedTokenException;
import rekit.util.LambdaUtil;

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
	public static final String GROUP_UNKNOWN = "Unknown";

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
		LambdaUtil.invoke(LevelManager::loadAllLevels);
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
		Resource[] unassigned = resolv.getResources("/levels/level*");
		Resource[] assigned = resolv.getResources("/levels/*/level*");
		Comparator<Resource> cmp = (c1, c2) -> c1.getFilename().compareToIgnoreCase(c2.getFilename());
		Arrays.sort(unassigned, cmp);
		Arrays.sort(assigned, cmp);
		LevelManager.loadInfiniteLevels();

		for (Resource un : unassigned) {
			LevelManager.addArcadeLevel(un);
		}

		for (Resource as : assigned) {
			String[] path = as.getFile().getAbsolutePath().split(File.separatorChar == '\\' ? "\\\\" : "/");
			String group = path[path.length - 2];
			LevelManager.addArcadeLevel(as, group);
		}

		LevelManager.loadCustomLevels();

	}

	private static final void loadCustomLevels() {
		LevelManager.loadCustomLevels(DirFileDefinitions.LEVEL_DIR.listFiles(), LevelManager.GROUP_UNKNOWN);
	}

	private static final void loadCustomLevels(File[] dir, String group) {
		if (dir == null) {
			return;
		}
		Arrays.sort(dir, (f1, f2) -> f1.getName().toLowerCase().compareTo(f2.getName().toLowerCase()));
		for (File lv : dir) {
			if (lv.exists() && lv.isDirectory()) {
				LevelManager.loadCustomLevels(lv.listFiles(), lv.getName());
			} else if (lv.getName().startsWith("level")) {
				LambdaUtil.invoke(() -> LevelManager.addArcadeLevel(new FileInputStream(lv), group));
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
		LevelManager.addLevel(LevelManager.INFINITE = new LevelDefinition(level.getInputStream(), LevelType.Infinite_Fun));
		// LOTD
		DateFormat levelOfTheDayFormat = new SimpleDateFormat("ddMMyyyy");
		int seed = Integer.parseInt(levelOfTheDayFormat.format(Calendar.getInstance().getTime()));
		LevelManager.addLevel(LevelManager.LOTD = new LevelDefinition(level.getInputStream(), LevelType.Level_of_the_Day, seed));
	}

	/**
	 * Add a level by resource.
	 *
	 * @param level
	 *            the resource
	 */
	private static void addArcadeLevel(Resource level) {
		LambdaUtil.invoke(() -> LevelManager.addArcadeLevel(level, LevelManager.GROUP_UNKNOWN));
	}

	private static void addArcadeLevel(Resource as, String group) {
		LevelDefinition def = null;
		try {
			def = new LevelDefinition(as.getInputStream(), ++LevelManager.ARCADE_NUM);
		} catch (Exception e) {
			GameConf.GAME_LOGGER.error(e.getMessage());
			return;
		}
		if (!def.isSettingSet(SettingKey.GROUP)) {
			def.setSetting(SettingKey.GROUP, group);
		}
		LevelManager.addLevel(def);
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
		LevelManager.addArcadeLevel(levelStructure, LevelManager.GROUP_UNKNOWN);
	}

	private static void addArcadeLevel(InputStream is, String group) {
		LevelDefinition def = null;
		try {
			def = new LevelDefinition(is, ++LevelManager.ARCADE_NUM);
		} catch (Exception e) {
			GameConf.GAME_LOGGER.error(e.getMessage());
			return;
		}
		if (!def.isSettingSet(SettingKey.GROUP)) {
			def.setSetting(SettingKey.GROUP, group);
		}
		LevelManager.addLevel(def);
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

	public static synchronized Map<String, List<String>> getArcadeLevelGroups() {
		Map<String, List<String>> groups = new TreeMap<>();
		for (Entry<String, LevelDefinition> lv : LevelManager.LEVEL_MAP.entrySet()) {
			if (lv.getValue().getType() != LevelType.Arcade) {
				continue;
			}
			if (!groups.containsKey(lv.getValue().getSetting(SettingKey.GROUP))) {
				groups.put(lv.getValue().getSetting(SettingKey.GROUP), new ArrayList<>());
			}
			groups.get(lv.getValue().getSetting(SettingKey.GROUP)).add(lv.getKey());

		}
		for (List<String> lvs : groups.values()) {
			lvs.sort((l1, l2) -> LevelManager.LEVEL_MAP.get(l1).compareTo(LevelManager.LEVEL_MAP.get(l2)));
		}
		return groups;
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
