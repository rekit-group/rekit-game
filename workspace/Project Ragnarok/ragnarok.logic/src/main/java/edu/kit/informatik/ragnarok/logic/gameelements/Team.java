package edu.kit.informatik.ragnarok.logic.gameelements;

public enum Team {
	PLAYER, ENEMY, INANIMATE;

	public boolean isHostile(Team t) {
		if (this == PLAYER) {
			return t == ENEMY;
		}
		if (this == ENEMY) {
			return t == PLAYER;
		}
		return false;
	}

}
