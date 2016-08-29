package edu.kit.informatik.ragnarok.core;

public enum Team {
	PLAYER, ENEMY, INANIMATE, PICKUP, NEUTRAL, TRIGGER;

	public boolean isHostile(Team t) {
		if (this == PLAYER) {
			return t == ENEMY || t == PICKUP;
		}
		if (this == ENEMY || this == PICKUP || this == TRIGGER) {
			return t == PLAYER;
		}
		return false;
	}

	public boolean isNeutral() {
		return this == Team.NEUTRAL;
	}
}
