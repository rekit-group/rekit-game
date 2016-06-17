package edu.kit.informatik.ragnarok.config;

import java.util.ResourceBundle;

public final class BundleHelper {
	private final ResourceBundle bundle;

	public BundleHelper(ResourceBundle bundle) {
		if (bundle == null) {
			throw new IllegalArgumentException("Bundle cannot be null");
		}
		this.bundle = bundle;
	}

	public Integer getInt(String key) {
		String res = null;
		if (!this.bundle.containsKey(key) || !((res = this.bundle.getString(key)).matches("(-[0-9]+|[0-9]+)"))) {
			return null;
		}
		return Integer.parseInt(res);
	}

	public Float getFloat(String key) {
		String res = null;
		if (!this.bundle.containsKey(key) || !((res = this.bundle.getString(key)).matches("-?[0-9]+\\.[0-9]+(f|F)"))) {
			return null;
		}
		return Float.parseFloat(res);
	}

	public String getString(String key) {
		if (!this.bundle.containsKey(key)) {
			return null;
		}
		return this.bundle.getString(key);
	}
}