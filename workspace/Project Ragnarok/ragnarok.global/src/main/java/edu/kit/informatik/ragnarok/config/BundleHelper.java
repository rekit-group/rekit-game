package edu.kit.informatik.ragnarok.config;

import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.kit.informatik.ragnarok.util.RGBColor;

/**
 * This class contains several getters for a {@link ResourceBundle}
 *
 * @author Dominik Fuchß
 *
 */
public final class BundleHelper {
	/**
	 * The {@link ResourceBundle} of the {@link BundleHelper}
	 */
	private final ResourceBundle bundle;

	/**
	 * Instantiate the BundleHelper
	 *
	 * @param bundle
	 *            the {@link ResourceBundle} the {@link BundleHelper} shall work
	 *            on
	 */
	public BundleHelper(ResourceBundle bundle) {
		if (bundle == null) {
			throw new IllegalArgumentException("Bundle cannot be null");
		}
		this.bundle = bundle;
	}

	/**
	 * Get the value as {@link Integer}
	 *
	 * @param key
	 *            the key
	 * @return {@code null} if no value was found or the found value cannot be
	 *         converted to {@link Integer}, value otherwise
	 */
	public Integer getInt(String key) {
		String res = null;
		if (!this.bundle.containsKey(key) || !((res = this.bundle.getString(key)).matches("(-|\\+)?[0-9]+"))) {
			return null;
		}
		return Integer.parseInt(res);
	}

	/**
	 * Get the value as {@link Float}
	 *
	 * @param key
	 *            the key
	 * @return {@code null} if no value was found or the found value cannot be
	 *         converted to {@link Float}, value otherwise
	 */
	public Float getFloat(String key) {
		String res = null;
		if (!this.bundle.containsKey(key) || !((res = this.bundle.getString(key)).matches("(-|\\+)?[0-9]+\\.[0-9]+(f|F)"))) {
			return null;
		}
		return Float.parseFloat(res);
	}

	/**
	 * Get the value as {@link String}
	 *
	 * @param key
	 *            the key
	 * @return {@code null} if no value was found, value otherwise
	 */
	public String getString(String key) {
		if (!this.bundle.containsKey(key)) {
			return null;
		}
		return this.bundle.getString(key);
	}
	
	public RGBColor getRGBColor(String key) {
		Pattern pattern = Pattern.compile("([0-9]+);([0-9]+);([0-9]+)");
		if (!this.bundle.containsKey(key)) {
			return null;
		}
		String res = this.bundle.getString(key);
		Matcher matcher = pattern.matcher(res);
		if (!matcher.find()) {
			System.err.println("BundleHelper: " + res + " is no RBG");
			return null;
		}
		
		int r = Integer.parseInt(matcher.group(1));
		int g = Integer.parseInt(matcher.group(2));
		int b = Integer.parseInt(matcher.group(3));
		return new RGBColor(r,g,b);
	}

}