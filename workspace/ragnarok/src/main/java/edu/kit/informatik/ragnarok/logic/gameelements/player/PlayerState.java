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

	/**
	 * return true
	 */
	/**
	 * return true
	 */
	/**
	 * return true
	 */
	/**
	 * return true
	 */
	public boolean canJump() {
		// TODO implement this operation
		throw new UnsupportedOperationException("not implemented");
	}

	public abstract void render();

}
