package rekit.persistence;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;

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
	public static final URLClassLoader SYSLOADER = (URLClassLoader) ClassLoader.getSystemClassLoader();
	private static final Method ADD_METHOD = JarManager.getAddJarMethod();

	/**
	 * Load all mods from {@link DirFileDefinitions#MODS_DIR}.
	 */
	public static synchronized void loadMods() {
		if (JarManager.ADD_METHOD == null) {
			return;
		}
		Arrays.stream(DirFileDefinitions.MODS_DIR.listFiles()).filter(f -> f.getName().endsWith("jar") && f.isFile()).forEach(JarManager::addJar);

	}

	private static void addJar(File jar) {
		try {
			JarManager.ADD_METHOD.invoke(JarManager.SYSLOADER, jar.toURI().toURL());
			GameConf.GAME_LOGGER.info("Loaded Mod: " + jar.getName());
		} catch (Exception e) {
			GameConf.GAME_LOGGER.fatal(e.getMessage());
		}
	}

	private static Method getAddJarMethod() {

		try {
			final Class<URLClassLoader> sysclass = URLClassLoader.class;
			Method method = sysclass.getDeclaredMethod("addURL", URL.class);
			method.setAccessible(true);
			return method;
		} catch (Exception e) {
			GameConf.GAME_LOGGER.fatal(e.getMessage());
			return null;
		}

	}

}
