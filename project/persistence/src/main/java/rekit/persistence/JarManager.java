package rekit.persistence;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import rekit.config.GameConf;

/**
 * This class handles the loading of additional Jars.
 *
 * @author Dominik Fuchss
 *
 */
public final class JarManager {
	/**
	 * Prevent instantiation.
	 */
	private JarManager() {
	}

	/**
	 * The system class loader.
	 */
	public static final URLClassLoader SYSLOADER = new URLClassLoader(JarManager.loadMods(), ClassLoader.getSystemClassLoader());

	/**
	 * Load all mods from {@link DirFileDefinitions#MODS_DIR}.
	 */
	private static synchronized URL[] loadMods() {
		List<URL> urls = new ArrayList<>();
		for (File f : DirFileDefinitions.MODS_DIR.listFiles()) {
			if (f.isFile() && f.getName().endsWith("jar")) {
				JarManager.addJar(f, urls);
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
			// JarManager.ADD_METHOD.invoke(JarManager.SYSLOADER,
			// jar.toURI().toURL());
			GameConf.GAME_LOGGER.info("Loaded Mod: " + jar.getName());
		} catch (Exception e) {
			GameConf.GAME_LOGGER.fatal(e.getMessage());
		}
	}

}
