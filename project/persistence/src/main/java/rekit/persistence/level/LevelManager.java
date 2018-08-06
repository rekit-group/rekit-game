package rekit.persistence.level;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.fuchss.obox.OBoxFactory;
import org.fuchss.obox.port.Configuration;
import org.fuchss.obox.port.Configuration.Flag;
import org.fuchss.obox.port.OBoxException;
import org.fuchss.obox.port.OBoxPort;
import org.fuchss.obox.port.Session;
import org.fuchss.obox.port.SessionManager;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import rekit.config.GameConf;
import rekit.core.ShutdownManager;
import rekit.persistence.DirFileDefinitions;
import rekit.persistence.ModManager;
import rekit.persistence.level.parser.UnexpectedTokenException;
import rekit.util.LambdaUtil;
import rekit.util.container.RWContainer;

/**
 *
 * This class manages all Level depended stuff as defined in {@link DataKey}.
 *
 * @author Dominik Fuchss
 *
 */
public final class LevelManager {
	/**
	 * All known levels (ID -> Level).
	 */
	private static final Map<String, LevelDefinition> LEVEL_MAP = new HashMap<>();
	/**
	 * The default group name of unknown arcade levels.
	 */
	public static final String GROUP_UNKNOWN = "Unknown";
	/**
	 * The one and only infinite level.
	 */
	private static LevelDefinition INFINITE = null;
	/**
	 * The one and only level of the day.
	 */
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

	private static int arcadeNum = 0;

	private static Session SESSION;

	/**
	 * Load levels.
	 */
	public static synchronized void init() {
		if (LevelManager.initialized) {
			return;
		}
		LevelManager.initDB();
		LevelManager.initialized = true;
		LambdaUtil.invoke(LevelManager::loadAllLevels);
		LevelManager.loadDataFromFile();

	}

	private static void initDB() {
		OBoxPort obox = OBoxFactory.FACTORY.OBoxPort();
		Configuration config = obox.configurationBuilder().createConfiguration();
		try {
			config.setDriver(org.sqlite.JDBC.class, "jdbc:sqlite:");
			config.setUri(DirFileDefinitions.USER_DATA_DB.toURI().getPath());
			config.setUser("");
			config.setPasswd("");
			config.setFlag(Flag.CREATE, Flag.MODIFY);
		} catch (OBoxException e) {
			e.printStackTrace();
			return;
		}

		SessionManager sm = obox.sessionManager();
		try {
			LevelManager.SESSION = sm.session(config);
			LevelManager.SESSION.declareClass(LevelData.class);
			LevelManager.SESSION.open();
		} catch (OBoxException e) {
			return;
		}

		ShutdownManager.registerObserver(() -> sm.terminate(LevelManager.SESSION));
	}

	/**
	 * Load all levels.
	 *
	 * @throws IOException
	 *             iff wrong path.
	 */
	private static void loadAllLevels() throws IOException {
		PathMatchingResourcePatternResolver resolv = new PathMatchingResourcePatternResolver(ModManager.SYSLOADER);
		Resource[] unknown = resolv.getResources("classpath*:/levels/level*.dat");
		Resource[] subdirs = resolv.getResources("classpath*:/levels/*/level*.dat");
		Resource[] res = new Resource[unknown.length + subdirs.length];
		System.arraycopy(unknown, 0, res, 0, unknown.length);
		System.arraycopy(subdirs, 0, res, unknown.length, subdirs.length);

		Stream<Resource> numbered = Arrays.stream(res).filter(r -> r.getFilename().matches("level_\\d+\\.dat"));
		Stream<Resource> notNumbered = Arrays.stream(res).filter(r -> !r.getFilename().matches("level_\\d+\\.dat"));

		numbered.sorted((r1, r2) -> {
			String n1 = r1.getFilename().substring("level_".length()).split("\\.")[0];
			String n2 = r2.getFilename().substring("level_".length()).split("\\.")[0];
			return Integer.compare(Integer.parseInt(n1), Integer.parseInt(n2));
		}).forEach(LevelManager::addArcadeLevel);
		notNumbered.sorted((r1, r2) -> r1.toString().compareToIgnoreCase(r2.toString())).forEach(LevelManager::addArcadeLevel);

		LevelManager.loadInfiniteLevels();
		LevelManager.loadCustomLevels();
	}

	private static void loadCustomLevels() {
		LevelManager.loadCustomLevels(DirFileDefinitions.LEVEL_DIR.listFiles(), LevelManager.GROUP_UNKNOWN);
	}

	private static void loadCustomLevels(File[] dir, String group) {
		if (dir == null) {
			return;
		}
		Arrays.sort(dir, (f1, f2) -> f1.getName().compareToIgnoreCase(f2.getName()));
		for (File lv : dir) {
			if (lv.exists() && lv.isDirectory()) {
				LevelManager.loadCustomLevels(lv.listFiles(), lv.getName());
			} else if (lv.getName().startsWith("level") && lv.getName().endsWith(".dat")) {
				LambdaUtil.invoke(() -> LevelManager.addArcadeLevel(new FileInputStream(lv), group, false));
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
		Resource res = resolv.getResource("/levels/infinite.dat");
		// Infinite
		LevelManager.INFINITE = new LevelDefinition(res.getInputStream(), LevelType.Infinite_Fun);
		LevelManager.addLevel(LevelManager.INFINITE, false);

		// LOTD
		DateFormat levelOfTheDayFormat = new SimpleDateFormat("ddMMyyyy");
		int seed = Integer.parseInt(levelOfTheDayFormat.format(Calendar.getInstance().getTime()));
		LevelManager.LOTD = new LevelDefinition(res.getInputStream(), LevelType.Level_of_the_Day, seed);
		LevelManager.addLevel(LevelManager.LOTD, false);
	}

	/**
	 * Add a level by resource.
	 *
	 * @param level
	 *            the resource
	 */
	private static void addArcadeLevel(Resource level) {
		RWContainer<String> path = new RWContainer<>();
		LambdaUtil.invoke(() -> path.set(level.getURL().getPath()));
		String[] split = null;
		if (path.get() == null || (split = path.get().split("/")) == null || split[split.length - 2].equals("levels")) {
			LambdaUtil.invoke(() -> LevelManager.addArcadeLevel(level, LevelManager.GROUP_UNKNOWN));
		} else {
			final String group = split[split.length - 2];
			LambdaUtil.invoke(() -> LevelManager.addArcadeLevel(level, group));
		}
	}

	private static void addArcadeLevel(Resource as, String group) {
		LevelDefinition def = null;
		try {
			def = new LevelDefinition(as.getInputStream(), ++LevelManager.arcadeNum);
		} catch (Exception e) {
			GameConf.GAME_LOGGER.error(e.getMessage());
			return;
		}
		if (!def.isSettingSet(SettingKey.GROUP)) {
			def.setSetting(SettingKey.GROUP, group);
		}
		LevelManager.addLevel(def, false);
	}

	/**
	 * Add a level by structure-file.
	 *
	 * @param levelStructure
	 *            the level structure
	 * @throws IOException
	 *             will thrown if Resources are not accessible
	 * @param reloadUserData
	 *            indicates whether userdata file shall be reloaded for
	 *            highscore etc.
	 * @throws UnexpectedTokenException
	 *             will thrown if syntax of Resources are wrong
	 * @return the id of the level or {@code null} if error occurred
	 */
	public static synchronized String addArcadeLevel(InputStream levelStructure, boolean reloadUserData) throws UnexpectedTokenException, IOException {
		if (!LevelManager.initialized) {
			return null;
		}
		return LevelManager.addArcadeLevel(levelStructure, LevelManager.GROUP_UNKNOWN, reloadUserData);
	}

	private static String addArcadeLevel(InputStream is, String group, boolean reloadUserData) {
		LevelDefinition def = null;
		try {
			def = new LevelDefinition(is, ++LevelManager.arcadeNum);
		} catch (Exception e) {
			GameConf.GAME_LOGGER.error(e.getMessage());
			return null;
		}
		if (!def.isSettingSet(SettingKey.GROUP)) {
			def.setSetting(SettingKey.GROUP, group);
		}
		return LevelManager.addLevel(def, reloadUserData);
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

	/**
	 * Get a map of arcade levels (grouped).
	 *
	 * @return a grouped (map) of arcade levels
	 */
	public static synchronized Map<String, List<String>> getArcadeLevelGroups() {
		Map<String, List<String>> groups = new TreeMap<>();
		for (Entry<String, LevelDefinition> lv : LevelManager.LEVEL_MAP.entrySet()) {
			String group = lv.getValue().getSetting(SettingKey.GROUP);
			if (lv.getValue().getType() != LevelType.Arcade) {
				continue;
			}
			if (!groups.containsKey(group)) {
				groups.put(group, new ArrayList<>());
			}
			groups.get(group).add(lv.getKey());

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
	 * @param reloadUserData
	 *            indicates whether userdata file shall be reloaded for
	 *            highscore etc.
	 * @return the id of the level
	 */
	public static synchronized String addLevel(LevelDefinition level, boolean reloadUserData) {
		if (level == null) {
			return null;
		}
		LevelManager.LEVEL_MAP.put(level.getRawData(), level);
		if (reloadUserData) {
			LevelManager.loadDataFromFile();
		}
		return level.getRawData();
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
			Set<LevelData> data = LevelManager.SESSION.getAllObjects(LevelData.class);
			for (LevelData datum : data) {
				LevelDefinition level = LevelManager.findByID(datum.data);
				if (level == null) {
					continue;
				}
				level.setData(DataKey.HIGH_SCORE, datum.highscore, false);
				level.setData(DataKey.WON, datum.won, false);
				level.setData(DataKey.SUCCESS, datum.success, false);
			}
		} catch (OBoxException e) {
			GameConf.GAME_LOGGER.error("Error while opening " + DirFileDefinitions.USER_DATA_DB.getAbsolutePath() + " for scores and saves");
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
		List<LevelDefinition> levels = LevelManager.LEVEL_MAP.values().stream().filter(level -> id.equals(level.getRawData())).collect(Collectors.toList());
		if (levels.isEmpty()) {
			return null;
		}
		return levels.get(0);
	}

	/**
	 * Save state to {@link #USER_DATA}.
	 */
	private static void saveToFile() {
		if (!LevelManager.initialized) {
			return;
		}
		for (LevelDefinition ld : LevelManager.LEVEL_MAP.values()) {
			try {
				LevelData lvd = new LevelData(ld.getName(), ld.getType().ordinal(), ld.getRawData());
				Set<LevelData> lvData = LevelManager.SESSION.getObjectsByPrototype(lvd, LevelData.getCompare());
				if (lvData != null && lvData.size() == 1) {
					lvd = lvData.iterator().next();
				}
				lvd.highscore = (int) ld.getData(DataKey.HIGH_SCORE);
				lvd.success = (boolean) ld.getData(DataKey.SUCCESS);
				lvd.won = (boolean) ld.getData(DataKey.WON);
				LevelManager.SESSION.persist(lvd);

			} catch (OBoxException e) {
				e.printStackTrace();
				GameConf.GAME_LOGGER.error("Error while saving " + DirFileDefinitions.USER_DATA_DB.getAbsolutePath() + " for scores and saves");
			}
		}
	}

	@Override
	protected void finalize() throws Throwable {
		OBoxFactory.FACTORY.OBoxPort().sessionManager().terminate(LevelManager.SESSION);
	}

}
