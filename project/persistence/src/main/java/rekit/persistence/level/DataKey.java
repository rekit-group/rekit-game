package rekit.persistence.level;

import java.io.Serializable;

public enum DataKey {

	HIGH_SCORE("highscore", 0), SUCCESS("success", false);
	private final String key;
	private final Serializable defaultVal;

	private DataKey(String key, Serializable defaultVal) {
		this.key = key;
		this.defaultVal = defaultVal;
	}

	public String getKey() {
		return this.key;
	}

	public Serializable getDefaultVal() {
		return this.defaultVal;
	}

}
