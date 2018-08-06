package rekit.persistence;

import java.io.File;

import rekit.persistence.level.LevelManager;
import rekit.util.LambdaUtil;

/**
 * This class contains all Directory and File constants.
 *
 * @author Dominik Fuchss
 *
 */
public final class DirFileDefinitions {
	/**
	 * Prevent instantiation.
	 */
	private DirFileDefinitions() {
	}

	/**
	 * The base directory for all stored files of the program.
	 */
	public static final File BASE = DirFileDefinitions.getBaseDir();
	/**
	 * The directory which contains all custom levels.
	 */
	public static final File LEVEL_DIR = new File(DirFileDefinitions.BASE.getAbsolutePath() + "/levels");
	/**
	 * The directory which contains all config files.
	 */
	public static final File CONFIG_DIR = new File(DirFileDefinitions.BASE.getAbsolutePath() + "/config");
	/**
	 * The directory which contains all mods / addons.
	 */
	public static final File MODS_DIR = new File(DirFileDefinitions.BASE.getAbsolutePath() + "/mods");

	/**
	 * The global data file for the {@link LevelManager}.
	 */
	public static final File USER_DATA_DB = new File(DirFileDefinitions.CONFIG_DIR.getAbsolutePath() + "/user-data.db");

	static {
		DirFileDefinitions.LEVEL_DIR.mkdirs();
		DirFileDefinitions.CONFIG_DIR.mkdirs();
		DirFileDefinitions.MODS_DIR.mkdirs();
	}

	private static synchronized File getBaseDir() {
		File res = null;
		if (System.getProperty("os.name").toLowerCase().indexOf("win") >= 0) {
			res = new File(System.getenv("APPDATA") + "/rekit");
		} else {
			res = new File(System.getProperty("user.home") + "/.config/rekit");
		}
		res.mkdirs();
		return res;
	}
}
