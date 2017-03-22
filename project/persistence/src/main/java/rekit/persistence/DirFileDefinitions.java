package rekit.persistence;

import java.io.File;

import rekit.persistence.level.LevelManager;
import rekit.util.LambdaTools;

public class DirFileDefinitions {

	public static final File SYS_CONF = DirFileDefinitions.getConfDir();
	public static final File LEVEL_DIR = new File(DirFileDefinitions.SYS_CONF.getAbsolutePath() + "/levels");
	public static final File CONFIG_DIR = new File(DirFileDefinitions.SYS_CONF.getAbsolutePath() + "/config");
	public static final File MODS_DIR = new File(DirFileDefinitions.SYS_CONF.getAbsolutePath() + "/mods");

	/**
	 * The global data file for the {@link LevelManager}.
	 */
	public static final File USER_DATA = new File(DirFileDefinitions.CONFIG_DIR.getAbsolutePath() + "/user-data.dat");

	static {
		DirFileDefinitions.LEVEL_DIR.mkdirs();
		DirFileDefinitions.CONFIG_DIR.mkdirs();
		DirFileDefinitions.MODS_DIR.mkdirs();
		if (!DirFileDefinitions.USER_DATA.exists()) {
			LambdaTools.tryCatch(DirFileDefinitions.USER_DATA::createNewFile).run();
		}
	}

	private static synchronized File getConfDir() {
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
