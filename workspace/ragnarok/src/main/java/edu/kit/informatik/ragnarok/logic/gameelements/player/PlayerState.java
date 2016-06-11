package edu.kit.informatik.ragnarok.logic.gameelements.player;


public abstract class PlayerState {
	/**
	 * <pre>
	 *           1..1     1..1
	 * PlayerState ------------------------- Player
	 *           playerState        &lt;       player
	 * </pre>
	 */
	private Player player;

	public void setPlayer(Player value) {
		this.player = value;
	}

	public Player getPlayer() {
		return this.player;
	}

	public boolean canJump() {
		return true;
	}

	public abstract void render();

}
