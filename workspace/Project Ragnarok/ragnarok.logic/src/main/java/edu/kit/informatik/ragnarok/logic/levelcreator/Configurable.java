package edu.kit.informatik.ragnarok.logic.levelcreator;

import java.util.HashMap;
import java.util.Map;

public abstract class Configurable {
	protected Map<String, Integer> settings = new HashMap<String, Integer>();

	public void setSetting(String settingName, int settingValue) {
		this.settings.put(settingName, settingValue);
	}

	protected boolean isSettingSet(String settingName) {
		if (!this.settings.containsKey(settingName)) {
			return false;
		}
		return this.settings.get(settingName) != 0;
	}

	protected int getSettingValue(String settingName) {
		if (!this.isSettingSet(settingName)) {
			return 0;
		}
		return this.settings.get(settingName);
	}
}
