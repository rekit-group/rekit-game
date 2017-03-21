package rekit.persistence.level;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import java.util.SortedMap;
import java.util.TreeMap;

import rekit.config.GameConf;

/**
 *
 * This class holds all necessary information about a level.
 *
 */
public final class LevelDefinition implements Comparable<LevelDefinition> {

	/**
	 * The type of a level.
	 *
	 * @author Dominik Fuchss
	 *
	 */
	public enum Type {
		/**
		 * Infinite level.
		 */
		Infinite_Fun,
		/**
		 * Level of the day.
		 */
		Level_of_the_Day,
		/**
		 * Arcade level.
		 */
		Arcade,
		/**
		 * Boss Rush Mode.
		 */
		Boss_Rush;
		/**
		 * Same as {@link #valueOf(String)}, but no exception.
		 *
		 * @param string
		 *            the representing String
		 * @return the type or {@code null} iff none found
		 */
		public static Type byString(String string) {
			try {
				return Type.valueOf(string);
			} catch (IllegalArgumentException e) {
				return null;
			}
		}
	}

	private final Type type;
	private final String name;
	private final int seed;
	private int arcadeNum;

	public LevelDefinition(InputStream inputStream, Type type) {
		this(inputStream, type, GameConf.PRNG.nextInt());
	}

	public LevelDefinition(InputStream inputStream, Type type, int seed) {
		this.type = type;
		this.seed = seed;
		Scanner scanner = new Scanner(inputStream, Charset.defaultCharset().name());
		scanner.useDelimiter("\\A");
		String input = scanner.hasNext() ? scanner.next() : "";
		scanner.close();

		LevelParser.parseLevel(input, this);
		this.name = this.calcName();
		this.arcadeNum = -1;
	}

	LevelDefinition(InputStream inputStream, int arcadeNumber) {
		this(inputStream, Type.Arcade);
		this.arcadeNum = arcadeNumber;
	}

	private String calcName() {
		String name = null;
		if (this.type != Type.Arcade) {
			name = this.type.toString().replace('_', ' ');
		}
		if (this.isSettingSet("name")) {
			name = this.getSetting("name").replace('_', ' ');
		}
		return (name == null ? this.getID() : name);
	}

	private List<String[][]> structures = new LinkedList<>();

	public void addStructure(List<String[]> lines) {
		String[][] structure = new String[lines.size()][];
		int i = 0;
		for (String[] line : lines) {
			structure[i++] = line.clone();
		}
		this.structures.add(structure);
	}

	private SortedMap<String, String> aliases = new TreeMap<>();

	public void setAlias(String toReplace, String replaceWith) {
		if (this.aliases.put(toReplace, replaceWith) != null) {
			GameConf.GAME_LOGGER.warn("Multiple definitions (alias) for " + toReplace);
		}

	}

	public String getAlias(String key) {
		return this.aliases.get(key);
	}

	private SortedMap<String, String> settings = new TreeMap<>();

	public void setSetting(String key, String value) {
		if (this.settings.put(key, value) != null) {
			GameConf.GAME_LOGGER.warn("Multiple definitions (settings) for " + key);
		}

	}

	public String getSetting(String key) {
		return this.settings.get(key);
	}

	public boolean isSettingSet(String key) {
		if (!this.settings.containsKey(key)) {
			return false;
		}
		String setting = this.settings.get(key);
		if (setting.toLowerCase(Locale.ENGLISH).matches("(true|false)")) {
			return setting.equalsIgnoreCase("true");
		}
		return this.settings.get(key) != null;
	}

	private SortedMap<String, String> bossSettings = new TreeMap<>();

	public void setBossSetting(String setting, String value) {
		if (this.bossSettings.put(setting, value) != null) {
			GameConf.GAME_LOGGER.warn("Multiple definitions (bosssettings) for " + setting);
		}

	}

	public String getBossSetting(String key) {
		return this.bossSettings.get(key);
	}

	public Type getType() {
		return this.type;
	}

	public String getName() {
		return this.name;
	}

	private String id = null;

	public synchronized String getID() {
		if (this.id != null) {
			return this.id;
		}
		this.id = this.calcID();
		return this.id;

	}

	private String calcID() {
		MessageDigest cs = null;
		try {
			cs = MessageDigest.getInstance("SHA-512");
		} catch (NoSuchAlgorithmException e) {
			GameConf.GAME_LOGGER.fatal(e.getMessage());
			return null;
		}
		StringBuilder content = new StringBuilder();
		content.append(this.type);
		if (this.type == Type.Arcade) {
			this.structures.forEach(struct -> Arrays.stream(struct).forEach(row -> Arrays.stream(row).forEach(elem -> content.append(elem.trim()))));
			this.aliases.forEach((k, v) -> content.append(k).append(v));
			this.settings.forEach((k, v) -> content.append(k).append(v));
			this.bossSettings.forEach((k, v) -> content.append(k).append(v));
		}
		cs.update(content.toString().getBytes());
		StringBuffer res = new StringBuffer();
		for (byte bytes : cs.digest()) {
			res.append(String.format("%02x", bytes & 0xff));
		}
		return res.toString();
	}

	private Map<String, Object> data = new HashMap<>();

	public void setData(DataKey key, Object value) {
		this.setData(key, value, true);
	}

	public void setData(DataKey key, Object value, boolean notify) {
		if (this.data.put(key.getKey(), value) != null) {
			GameConf.GAME_LOGGER.warn("Multiple definitions (data) for " + key);
		}
		if (notify) {
			LevelManager.contentChanged();
		}

	}

	public Object getData(DataKey key) {
		Object data = this.data.get(key.getKey());
		if (data == null) {
			return key.getDefaultVal();
		}
		return data;
	}

	public int getSeed() {
		return this.seed;
	}

	public int size() {
		return this.structures.size();
	}

	public String[][] get(int idx) {
		return this.structures.get(idx);
	}

	@Override
	public int compareTo(LevelDefinition o) {
		return 2 * this.type.compareTo(o.type) + Integer.compare(this.arcadeNum, o.arcadeNum);
	}

}