package rekit.persistence.level;

import java.util.function.Function;

public enum DataKey {

	HIGH_SCORE("highscore", 0, Integer::parseInt);

	private final String key;
	private final Object defaultVal;
	private final Function<String, Object> parser;

	private DataKey(String key, Object defaultVal, Function<String, Object> parser) {
		this.key = key;
		this.defaultVal = defaultVal;
		this.parser = parser;
	}

	public String getKey() {
		return this.key;
	}

	public Object getDefaultVal() {
		return this.defaultVal;
	}

	public Object parse(String definition) {
		return this.parser.apply(definition);
	}

}
