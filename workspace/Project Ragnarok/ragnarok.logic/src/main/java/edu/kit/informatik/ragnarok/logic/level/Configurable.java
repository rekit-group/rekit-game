package edu.kit.informatik.ragnarok.logic.level;

import java.util.HashMap;
import java.util.Map;

import edu.kit.informatik.ragnarok.logic.level.parser.SettingParser;

/**
 * <p>
 * Abstract class and interface declaration that supplies defining settings.
 * Each Setting is a mapping from a setting name (of type String) to its
 * corresponding value (of type integer).
 * </p>
 * <p>
 * Initially, no setting is set and therefore <i>isSettingSet(String
 * settingName)</i> (as well as <i>getSettingValue(String settingName)</i>)
 * return their default value <i>false</i> (or <i>0</i>)
 * </p>
 * <p>
 * To define settings, use <i>setSetting(String settingName, int
 * settingValue)</i>
 * </p>
 * <p>
 * This class is mainly - but not only - used by the {@link SettingParser}.
 * </p>
 * <p>
 * The data structure used to hold these saved settings is entirely managed by
 * this abstract class and therefore private.
 * </p>
 *
 * @author Angelo Aracri
 * @version 1.0
 */
public abstract class Configurable {
	/**
	 * Internal data structure used to hold all specified settings.
	 */
	private Map<String, Integer> settings = new HashMap<String, Integer>();

	/**
	 * Sets a setting of name <i>settingName</i> to the given value
	 * <i>settingValue</i>. If this setting was defined beforehand, the old
	 * value will be overwritten (classic Map-behavior).
	 *
	 * @param settingName
	 *            The name of the setting to set/overwrite
	 * @param settingValue
	 *            The new value for the setting
	 */
	public void setSetting(String settingName, int settingValue) {
		this.settings.put(settingName, settingValue);
	}

	/**
	 * Determines if a setting of a given <i>settingName</i> is defined and not
	 * <i>0</i>
	 *
	 * @param settingName
	 *            the name of the setting to check.
	 * @return <b>true</b> if the setting is defined and not <i>0</i>,
	 *         <b>false</b> otherwise.
	 */
	protected boolean isSettingSet(String settingName) {
		if (!this.settings.containsKey(settingName)) {
			return false;
		}
		return this.settings.get(settingName) != 0;
	}

	/**
	 * Returns the value of the setting of a given <i>settingName</i> or 0 if it
	 * is not defined.
	 *
	 * @param settingName
	 *            the name of the setting to get the value from.
	 * @return the value of the setting if defined, <i>0</i> otherwise.
	 */
	protected int getSettingValue(String settingName) {
		if (!this.isSettingSet(settingName)) {
			return 0;
		}
		return this.settings.get(settingName);
	}
}
