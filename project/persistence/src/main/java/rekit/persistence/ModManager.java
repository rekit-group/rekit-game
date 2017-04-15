package rekit.persistence;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.List;

import rekit.config.GameConf;

/**
 * This class handles the loading of additional Jars / Mods.
 *
 * @author Dominik Fuchss
 *
 */
public final class ModManager {
	/**
	 * Prevent instantiation.
	 */
	private ModManager() {
	}

	/**
	 * The system class loader.
	 */

	public static final URLClassLoader SYSLOADER = ModManager.loadIt();

	private static synchronized URLClassLoader loadIt() {
		if (ModManager.SYSLOADER != null) {
			return ModManager.SYSLOADER;
		}
		return AccessController
				.doPrivileged((PrivilegedAction<URLClassLoader>) () -> new URLClassLoader(ModManager.loadMods(), ClassLoader.getSystemClassLoader()));
	}

	/**
	 * Load all mods from {@link DirFileDefinitions#MODS_DIR}.
	 */
	private static synchronized URL[] loadMods() {
		List<URL> urls = new ArrayList<>();
		File[] fs = DirFileDefinitions.MODS_DIR.listFiles();
		if (fs != null) {
			for (File f : fs) {
				if (f.isFile() && f.getName().endsWith("jar")) {
					ModManager.addJar(f, urls);
				}
			}
		}
		URL[] url = new URL[urls.size()];
		int i = 0;
		for (URL u : urls) {
			url[i++] = u;
		}
		return url;
	}

	private static void addJar(File jar, List<URL> urls) {
		try {
			urls.add(jar.toURI().toURL());
			GameConf.GAME_LOGGER.info("Loaded Mod: " + jar.getName());
		} catch (Exception e) {
			GameConf.GAME_LOGGER.fatal(e.getMessage());
		}
	}

}
