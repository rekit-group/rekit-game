package rekit.logic.level;

import rekit.logic.ILevelScene;
import rekit.logic.gameelements.entities.Player;
import rekit.persistence.level.DataKey;
import rekit.persistence.level.DataKeySetter;
import rekit.persistence.level.LevelDefinition;
import rekit.persistence.level.SettingKey;

/**
 * This class represents a Level for a {@link ILevelScene} with its
 * {@link StructurePart} and {@link LogicalPart}.
 *
 * @author Dominik Fuchss
 *
 */
public final class Level implements Comparable<Level>, DataKeySetter {
	private final StructurePart sp;
	private final LogicalPart lp;
	private final LevelDefinition definition;
	private boolean success;
	private boolean won;
	Level(LevelMtx mtx, StructurePart sp, LogicalPart lp) {
		this.sp = sp;
		this.lp = lp;
		this.definition = mtx.getDefinition();
	}

	/**
	 * This method shall be invoked when {@link ILevelScene} ends. This will
	 * save all data like highscore.
	 *
	 * @param won
	 *            indicates whether the level has been completed successfully
	 */
	public void end(boolean won) {
		this.success = won || this.isInfinite();
		this.won = won;
		DataKey.atEnd(this);
	}

	/**
	 * Reset the level.
	 */
	public void reset() {
		this.lp.reset();
		this.sp.reset();
	}

	/**
	 * Get the {@link LogicalPart} of the {@link Level}.
	 *
	 * @return the {@link LogicalPart}
	 */
	public LogicalPart getLp() {
		return this.lp;
	}

	/**
	 * Get the {@link StructurePart} of the {@link Level}.
	 *
	 * @return the {@link StructurePart}
	 */
	public StructurePart getSp() {
		return this.sp;
	}

	/**
	 * Get the name of the level.
	 *
	 * @return the name of the level
	 */
	public String getName() {
		return this.definition.getName();
	}

	/**
	 * Get the highscore of this level.
	 *
	 * @return the highscore
	 * @see DataKey#HIGH_SCORE
	 */
	public int getHighScore() {
		return (Integer) this.definition.getData(DataKey.HIGH_SCORE);
	}

	/**
	 * Indicates whether the level is infinite.
	 *
	 * @return {@code true} iff infinite, {@code false} otherwise
	 * @see SettingKey#INFINITE
	 */
	public boolean isInfinite() {
		return this.definition.isSettingSet(SettingKey.INFINITE);
	}

	@Override
	public int getScore() {
		Player player = this.lp.getPlayer();
		return (int) (player.getCameraOffset() + player.getPoints());
	}

	@Override
	public boolean getSuccess() {
		return this.success;
	}

	@Override
	public boolean getWon() {
		return this.won;
	}

	@Override
	public LevelDefinition getDefinition() {
		return this.definition;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.definition == null) ? 0 : this.definition.getID().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || !(obj instanceof Level)) {
			return false;
		}
		Level other = (Level) obj;
		return this.definition.getID().equals(other.definition.getID());
	}

	@Override
	public int compareTo(Level o) {
		return this.definition.compareTo(o.definition);
	}

}
