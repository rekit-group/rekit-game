package edu.kit.informatik.ragnarok.logic.levelcreator;

import java.util.HashMap;
import java.util.Map;

public abstract class Configurable {
	protected Map<String, Boolean> settings = new HashMap<String, Boolean>();

	public void setSetting(String settingName, boolean settingValue) {
		this.settings.put(settingName, settingValue);
	}

	protected boolean isSettingSet(String settingName) {
		if (!this.settings.containsKey(settingName)) {
			return false;
		}
		return this.settings.get(settingName);
	}
}
