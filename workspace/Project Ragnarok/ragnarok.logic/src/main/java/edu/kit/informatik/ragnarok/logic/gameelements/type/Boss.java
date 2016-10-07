package edu.kit.informatik.ragnarok.logic.gameelements.type;

import java.util.Set;

import edu.kit.informatik.ragnarok.config.GameConf;
import edu.kit.informatik.ragnarok.core.GameElement;
import edu.kit.informatik.ragnarok.core.Team;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.Entity;
import edu.kit.informatik.ragnarok.logic.gameelements.inanimate.Inanimate;
import edu.kit.informatik.ragnarok.logic.level.bossstructure.BossStructure;
import edu.kit.informatik.ragnarok.primitives.geometry.Vec;
import edu.kit.informatik.ragnarok.util.ReflectUtils;
import edu.kit.informatik.ragnarok.util.ReflectUtils.LoadMe;

/**
 *
 * This class shall be extended in case of creating Bosses.
 *
 */
public abstract class Boss extends Entity {
	/**
	 * The Bosses Level-Structure (BossRoom).
	 */
	protected BossStructure bossStructure;
	/**
	 * The target of the Boss.
	 */
	protected GameElement target;
	/**
	 * This boolean indicates whether the boss is harmless.
	 */
	protected boolean isHarmless = false;

	/**
	 * Get all instances of BossPrototypes.
	 *
	 * @return a set of BossPrototypes
	 * @see LoadMe
	 */
	public static final Set<Boss> getBossPrototypes() {
		return ReflectUtils.loadInstances(GameConf.SEARCH_PATH, Boss.class);
	}

	/**
	 * Create a Boss.
	 */
	protected Boss() {
		super(Team.ENEMY);
	}

	/**
	 * Create a Boss.
	 *
	 * @param startPos
	 *            the start pos
	 * @param vel
	 *            the initial velocity
	 * @param size
	 *            the initial size
	 */
	protected Boss(Vec startPos, Vec vel, Vec size) {
		super(startPos, vel, size, Team.ENEMY);
	}

	/**
	 * Get the BossName.
	 *
	 * @return the bossName
	 */
	public abstract String getName();

	/**
	 * Set the bossroom-structure.
	 *
	 * @param bossStructure
	 *            the bossroom-structure
	 */
	public final void setBossStructure(BossStructure bossStructure) {
		this.bossStructure = bossStructure;
	}

	/**
	 * Set the target of the boss.
	 *
	 * @param target
	 *            the target
	 */
	public final void setTarget(GameElement target) {
		this.target = target;
	}

	/**
	 * Get and set the boss structure.
	 *
	 * @return the boss-structure
	 */
	public BossStructure getBossStructure() {
		String[][] struct = new String[9][];
		for (int i = 0; i < struct.length; i++) {
			struct[i] = new String[] { Inanimate.class.getName() };
		}

		BossStructure structure = new BossStructure(struct, this);
		System.err.println("Error while spawning Boss: " + this.getClass().getName() + " did not specify getBossStructure()");
		this.setBossStructure(structure);
		return structure;
	}

	/**
	 * Get initial position of the {@link Boss} relative to the
	 * {@link BossStructure BossStructures} top left corner.
	 *
	 * @return the relative start position of the {@link Boss}.
	 */
	public Vec getStartPos() {
		return new Vec(0, GameConf.GRID_H / 2 + 1);
	}

	@Override
	public abstract GameElement create(Vec startPos, String[] options);

	@Override
	public final void destroy() {
		this.bossStructure.endBattle(this.getScene());
		this.isHarmless = true;
	}
}
