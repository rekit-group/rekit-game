package rekit.persistence.level;

public final class DataKey {

	public static final DataKey HIGH_SCORE = new DataKey("highscore");

	private final String key;

	private DataKey(String key) {
		this.key = key;
	}

	public String getKey() {
		return this.key;
	}

}
