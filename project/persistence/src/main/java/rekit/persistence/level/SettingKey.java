package rekit.persistence.level;

public enum SettingKey {
	NAME("name"), GROUP("group"), AUTO_COIN_SPAWN("autoCoinSpawn"), INFINITE("infinite"), SHUFFLE("shuffle"), DO_GAPS("doGaps");

	private final String id;

	private SettingKey(String id) {
		this.id = id;
	}

	String getID() {
		return this.id;
	}

	public static final SettingKey getByString(String id) {
		for (SettingKey key : SettingKey.values()) {
			if (key.id.matches(id)) {
				return key;
			}
		}
		return null;
	}
}
