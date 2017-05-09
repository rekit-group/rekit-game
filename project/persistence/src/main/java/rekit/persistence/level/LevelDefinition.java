package rekit.persistence.level;

import java.io.InputStream;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.security.MessageDigest;
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
import rekit.util.LambdaUtil;

/**
 *
 * This class holds all necessary information about a level.
 *
 */
public final class LevelDefinition implements Comparable<LevelDefinition> {

	private final LevelType type;
	private final String name;
	private final long seed;
	private int arcadeNum;

	/**
	 * Create a new LevelDefinition by data and type.
	 *
	 * @param in
	 *            the input data (e.g. from file) of the level
	 * @param type
	 *            the type of the level
	 */
	public LevelDefinition(InputStream in, LevelType type) {
		this(in, type, GameConf.PRNG.nextLong());
	}

	/**
	 * Create a new LevelDefinition by data and type and rnd seed.
	 *
	 * @param in
	 *            the input data (e.g. from file) of the level
	 * @param type
	 *            the type of the level
	 * @param seed
	 *            the random seed for the level
	 */
	public LevelDefinition(InputStream in, LevelType type, long seed) {
		this.type = type;
		this.seed = seed;
		Scanner scanner = new Scanner(in, Charset.defaultCharset().name());
		scanner.useDelimiter("\\A");
		String input = scanner.hasNext() ? scanner.next() : "";
		scanner.close();

		LevelParser.parseLevel(input, this);
		this.name = this.calcName();
		this.arcadeNum = -1;
	}

	/**
	 * Create an arcade level by definition and number
	 *
	 * @param in
	 *            the input data of the level
	 * @param number
	 *            the arcade number
	 */
	LevelDefinition(InputStream in, int number) {
		this(in, LevelType.Arcade);
		this.arcadeNum = number;
	}

	private String calcName() {
		String name = null;
		if (this.type != LevelType.Arcade) {
			name = this.type.toString().replace('_', ' ');
		}
		if (this.isSettingSet(SettingKey.NAME)) {
			name = this.getSetting(SettingKey.NAME);
		}
		return (name == null ? this.getID() : name);
	}

	private List<String[][]> structures = new LinkedList<>();

	/**
	 * Add a new structure to the level.
	 *
	 * @param rows
	 *            a list of rows (== the structure)
	 */
	void addStructure(List<String[]> rows) {
		String[][] structure = new String[rows.size()][];
		int i = 0;
		for (String[] line : rows) {
			structure[i++] = line.clone();
		}
		this.structures.add(structure);
	}

	private SortedMap<String, String> aliases = new TreeMap<>();

	/**
	 * Set a new alias.
	 *
	 * @param toReplace
	 *            the string to be replaced
	 * @param replaceWith
	 *            the replacement string
	 */
	void setAlias(String toReplace, String replaceWith) {
		if (this.aliases.put(toReplace, replaceWith) != null) {
			GameConf.GAME_LOGGER.warn("Multiple definitions (alias) for " + toReplace);
		}

	}

	/**
	 * Get alias by key.
	 *
	 * @param key
	 *            the key
	 * @return the alias or {@code null} if not set
	 */
	public String getAlias(String key) {
		return this.aliases.get(key);
	}

	private SortedMap<String, String> settings = new TreeMap<>();

	/**
	 * Set a new setting.
	 *
	 * @param key
	 *            the setting key
	 * @param value
	 *            the value to set
	 */
	void setSetting(SettingKey key, String value) {
		if (this.settings.put(key.getID(), value) != null) {
			GameConf.GAME_LOGGER.warn("Multiple definitions (settings) for " + key);
		}

	}

	/**
	 * Get setting by key.
	 *
	 * @param key
	 *            the key
	 * @return the setting or {@code null} if not set
	 */
	public String getSetting(SettingKey key) {
		String val = this.settings.get(key.getID());
		return val == null ? null : val.replace('_', ' ');
	}

	/**
	 * Check whether a setting is set (== if setting is "true" or "false",
	 * return whether it is "true", otherwise return true if setting is set to a
	 * value)
	 *
	 * @param key
	 *            the key
	 * @return {@code true} if set, {@code false} otherwise
	 */
	public boolean isSettingSet(SettingKey key) {
		if (!this.settings.containsKey(key.getID())) {
			return false;
		}
		String setting = this.settings.get(key.getID());
		if (setting.toLowerCase(Locale.ENGLISH).matches("(true|false)")) {
			return setting.equalsIgnoreCase("true");
		}
		return this.settings.get(key.getID()) != null;
	}

	private SortedMap<String, String> bossSettings = new TreeMap<>();

	/**
	 * Set a new boss-setting.
	 *
	 * @param key
	 *            the boss-setting key
	 * @param value
	 *            the value to set
	 */
	void setBossSetting(String key, String value) {
		if (this.bossSettings.put(key, value) != null) {
			GameConf.GAME_LOGGER.warn("Multiple definitions (bosssettings) for " + key);
		}

	}

	/**
	 * Get boss-setting by key.
	 *
	 * @param key
	 *            the key
	 * @return the boss-setting or {@code null} if not set
	 */
	public String getBossSetting(String key) {
		return this.bossSettings.get(key);
	}

	/**
	 * Get the type of the level.
	 *
	 * @return the type
	 */
	public LevelType getType() {
		return this.type;
	}

	/**
	 * Get the name of the level. (If no name is set, this will return the ID of
	 * the level.)
	 *
	 * @return the name (or id)
	 */
	public String getName() {
		return this.name;
	}

	private String id = null;

	/**
	 * Get the id of the level.
	 *
	 * @return the id
	 */
	public synchronized String getID() {
		if (this.id != null) {
			return this.id;
		}
		this.id = this.calcID();
		return this.id;

	}

	private String calcID() {
		MessageDigest cs = LambdaUtil.invoke(() -> MessageDigest.getInstance("SHA-512"));
		if (cs == null) {
			return null;
		}

		StringBuilder content = new StringBuilder();
		content.append(this.type);
		if (this.type == LevelType.Arcade) {
			this.structures.forEach(struct -> Arrays.stream(struct).forEach(row -> Arrays.stream(row).forEach(elem -> content.append(elem.trim()))));
			this.aliases.forEach((k, v) -> content.append(k).append(v));
			this.settings.forEach((k, v) -> content.append(k).append(v));
			this.bossSettings.forEach((k, v) -> content.append(k).append(v));
		}
		cs.update(content.toString().getBytes(Charset.forName("UTF-8")));
		StringBuffer res = new StringBuffer();
		for (byte bytes : cs.digest()) {
			res.append(String.format("%02x", bytes & 0xff));
		}
		return res.toString();
	}

	private Map<DataKey, Serializable> data = new HashMap<>();

	/**
	 * Set data of the level and notify the {@link LevelManager} about the
	 * changes.
	 *
	 * @param key
	 *            the key
	 * @param value
	 *            the new value
	 * @see LevelManager#contentChanged()
	 */
	void setData(DataKey key, Serializable value) {
		this.setData(key, value, true);
	}

	/**
	 * Set data of the level.
	 *
	 * @param key
	 *            the key
	 * @param value
	 *            the new value
	 * @param notify
	 *            indicated whether the {@link LevelManager} shall be informed
	 */
	void setData(DataKey key, Serializable value, boolean notify) {
		this.data.put(key, value);
		if (notify) {
			LevelManager.contentChanged();
		}
	}

	/**
	 * Get the data of the level.
	 *
	 * @param key
	 *            the key
	 * @return the data of the level or the default value of the {@link DataKey}
	 *         if not set
	 */
	public Serializable getData(DataKey key) {
		Serializable data = this.data.get(key);
		if (data == null) {
			return key.getDefaultVal();
		}
		return data;
	}

	/**
	 * Get the seed of the level.
	 *
	 * @return the seed
	 */
	public long getSeed() {
		return this.seed;
	}

	/**
	 * Get the amount of structures.
	 *
	 * @return the amount of structures
	 */
	public int amountOfStructures() {
		return this.structures.size();
	}

	/**
	 * Get structure by index.
	 *
	 * @param idx
	 *            the index
	 * @return the structure
	 */
	public String[][] getStructure(int idx) {
		return this.structures.get(idx);
	}

	@Override
	public int compareTo(LevelDefinition o) {
		return 2 * this.type.compareTo(o.getType()) + Integer.compare(this.arcadeNum, o.arcadeNum);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || this.getClass() != obj.getClass()) {
			return false;
		}
		LevelDefinition other = (LevelDefinition) obj;
		return this.id == null ? other.id == null : this.id.equals(other.id);
	}

}
