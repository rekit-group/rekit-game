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
import java.util.TreeMap;

import rekit.config.GameConf;
import rekit.persistence.level.parser.LevelParser;

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

		/**
		 * Indicate whether level has this type.
		 *
		 * @param lv
		 *            the level
		 * @return {@code true} if lv has this level type
		 */
		public final boolean hasType(LevelDefinition lv) {
			if (lv == null) {
				return false;
			}
			return this == lv.getType();
		}
	}

	private boolean finished;
	private final Type type;
	private String name;
	private final int seed;

	public LevelDefinition(InputStream inputStream, Type type) {
		this(inputStream, type, GameConf.PRNG.nextInt());
	}

	public LevelDefinition(InputStream inputStream, Type type, int seed) {
		this.type = type;
		this.seed = seed;
		// create Scanner from InputStream
		Scanner scanner = new Scanner(inputStream, Charset.defaultCharset().name());
		// don't use line-ending-wise iteration, but get the whole file at once
		// \\A = <EOF>
		scanner.useDelimiter("\\A");
		// get whole file content as String
		String input = scanner.hasNext() ? scanner.next() : "";
		// close scanner after use to prevent java-typical resource-wasting ;)
		scanner.close();
		// create Parser to extract all information of file-String
		LevelParser.parseLevel(input, this);
		if (this.type == Type.Infinite_Fun || this.type == Type.Level_of_the_Day) {
			this.name = this.type.toString().replace('_', ' ');
		}
		if (this.isSettingSet("name")) {
			this.name = this.getSetting("name");
		}
		this.finish();
	}

	@Override
	public int compareTo(LevelDefinition o) {
		boolean thisIsNumbered = this.getName().matches("level_\\d+\\.dat"), otherIsNumbered = o.getName().matches("level_\\d+\\.dat");
		if (thisIsNumbered != otherIsNumbered) {
			// NonNumbered before Numbered
			return otherIsNumbered ? -1 : 1;
		}
		// Numbered will be compared by number
		if (thisIsNumbered) {
			String n1 = this.getName(), n2 = o.getName();
			n1 = n1.substring("level_".length()).split("\\.")[0];
			n2 = n2.substring("level_".length()).split("\\.")[0];
			return Integer.compare(Integer.parseInt(n1), Integer.parseInt(n2));
		}

		// Else order by string order (and secondly by type)
		return 2 * this.getName().compareTo(o.getName()) + (this.getType().compareTo(o.getType()));
	}

	private void finish() {
		if (this.finished) {
			throw new IllegalStateException("LevelDefinition already finished");
		}
		this.finished = true;
		this.getID();
		this.name = (this.name == null ? this.id : this.name);

	}

	private List<String[][]> structures = new LinkedList<>();

	public void addStructure(List<String[]> lines) {
		if (this.finished) {
			throw new IllegalStateException("LevelDefinition already finished");
		}
		String[][] structure = new String[lines.size()][];
		int i = 0;
		for (String[] line : lines) {
			structure[i++] = line.clone();
		}
		this.structures.add(structure);
	}

	private Map<String, String> aliases = new TreeMap<>();

	public void setAlias(String toReplace, String replaceWith) {
		if (this.finished) {
			throw new IllegalStateException("LevelDefinition already finished");
		}
		if (this.aliases.put(toReplace, replaceWith) != null) {
			GameConf.GAME_LOGGER.warn("Multiple definitions (alias) for " + toReplace);
		}

	}

	public String getAlias(String key) {
		return this.aliases.get(key);
	}

	private Map<String, String> settings = new TreeMap<>();

	public void setSetting(String key, String value) {
		if (this.finished) {
			throw new IllegalStateException("LevelDefinition already finished");
		}
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

	private Map<String, String> bossSettings = new TreeMap<>();

	public void setBossSetting(String setting, String value) {
		if (this.finished) {
			throw new IllegalStateException("LevelDefinition already finished");
		}
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
		if (!this.finished) {
			throw new IllegalStateException("LevelDefinition not finished yet");
		}
		return this.name;
	}

	private String id = null;

	public synchronized String getID() {
		if (!this.finished) {
			throw new IllegalStateException("LevelDefinition not finished yet");
		}
		if (this.id != null) {
			return this.id;
		}
		MessageDigest cs = null;
		try {
			cs = MessageDigest.getInstance("SHA-512");
		} catch (NoSuchAlgorithmException e) {
			GameConf.GAME_LOGGER.fatal(e.getMessage());
			return null;
		}
		StringBuilder content = new StringBuilder();
		content.append(this.name);
		content.append(this.type);

		this.structures.forEach(struct -> Arrays.stream(struct).forEach(row -> Arrays.stream(row).forEach(elem -> content.append(elem.trim()))));
		this.aliases.forEach((k, v) -> content.append(k).append(v));
		this.settings.forEach((k, v) -> content.append(k).append(v));
		this.bossSettings.forEach((k, v) -> content.append(k).append(v));

		byte[] data = content.toString().getBytes();
		cs.update(data);
		byte[] digest = cs.digest();
		StringBuffer sb = new StringBuffer();
		for (byte b : digest) {
			sb.append(String.format("%02x", b & 0xff));
		}
		this.id = sb.toString();
		return this.id;

	}

	private Map<String, Object> data = new HashMap<>();

	public void setData(DataKey key, Object value) {
		if (this.data.put(key.getKey(), value) != null) {
			GameConf.GAME_LOGGER.warn("Multiple definitions (data) for " + key);
		}
	}

	public Object getData(DataKey key) {
		return this.data.get(key.getKey());
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

	public Object getData(DataKey key, Object std) {
		if (this.getData(key) == null) {
			return std;
		}
		return this.getData(key);
	}

}