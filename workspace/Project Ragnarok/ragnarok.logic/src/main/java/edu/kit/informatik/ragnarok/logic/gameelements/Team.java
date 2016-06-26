package edu.kit.informatik.ragnarok.logic.gameelements;

public enum Team {
	PLAYER, ENEMY, INANIMATE, PICKUP, NEUTRAL;

	public boolean isHostile(Team t) {
		if (this == PLAYER) {
			return t == ENEMY || t == PICKUP;
		}
		if (this == ENEMY || this == PICKUP) {
			return t == PLAYER;
		}
		return false;
	}

}
