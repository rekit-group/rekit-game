package rekit.logic.level;

import java.util.Random;

import org.fuchss.configuration.Configurable;

import rekit.config.GameConf;
import rekit.logic.gameelements.GameElement;
import rekit.logic.gameelements.GameElementFactory;
import rekit.logic.gameelements.entities.Player;
import rekit.logic.gameelements.inanimate.EndTrigger;
import rekit.logic.gameelements.inanimate.Inanimate;
import rekit.logic.gameelements.inanimate.InanimateTrigger;
import rekit.logic.gameelements.type.Boss;
import rekit.logic.gameelements.type.Enemy;
import rekit.persistence.level.LevelDefinition;
import rekit.persistence.level.SettingKey;
import rekit.primitives.geometry.Vec;

public class StructurePart {
	private int generatedUntil;

	protected int unitsBuilt;
	private int lastUnitsBuilt;

	private int currentStructureId;
	protected final Random random;

	private final LevelDefinition definition;

	StructurePart(LevelMtx mtx) {
		this.definition = mtx.getDefinition();
		this.random = mtx.getRandom();

	}

	/**
	 * Build {@link Structure Structures} if it must. This decision depends on
	 * the {@link Structure Structures} build so far and the parameter
	 * <i>max</i> that specifies which x position is the smallest that needs to
	 * be generated at.
	 *
	 * @param max
	 *            the lowest x position that must still be generated at.
	 */
	public final void generate(int max) {
		while (this.generatedUntil < max && this.hasNextStructure()) {
			// build structure
			this.generatedUntil += this.next().build(this.generatedUntil + 1, this.definition.isSettingSet(SettingKey.AUTO_COIN_SPAWN));
		}
	}

	/**
	 * Reset the level.
	 */
	final void reset() {
		this.currentStructureId = -1;
		this.unitsBuilt = 0;
		this.generatedUntil = 0;
	}

	/**
	 * Get next structure.
	 *
	 * @return the next structure
	 */
	protected Structure next() {
		// if this is level start: build initial Structure
		if (this.unitsBuilt == 0) {
			return this.getInitialStructure();
		}

		// increment structureId / count
		this.currentStructureId++;
		// if end of sequence reached: Determine if to repeat or end level
		if (this.currentStructureId >= this.definition.amountOfStructures()) {
			if (this.definition.isSettingSet(SettingKey.INFINITE)) {
				this.currentStructureId = 0;
			} else {
				return this.getEndStructure();
			}
		}

		Structure boss = this.getNextBossOrNull(this.lastUnitsBuilt, this.unitsBuilt);

		// keep track of last <i>unitsBuilt</i> before it is incremented.
		this.lastUnitsBuilt = this.unitsBuilt;

		if (boss != null) {
			return boss;
		}

		if (this.definition.isSettingSet(SettingKey.SHUFFLE)) {
			return this.nextShuffeled();
		} else {
			return this.nextInOrder();
		}

	}

	/**
	 * <p>
	 * Used to hold and get the first {@link Structure} that will <b>always</b>
	 * be the first {@link Structure} to be returned by <i>next()</i>.
	 * </p>
	 * <p>
	 * Is thought to give the {@link Player} an easier start and to prevent him
	 * from spawning in an {@link Enemy} or even an {@link Inanimate}.
	 * </p>
	 *
	 * @return the first {@link Structure} to be returned by <i>next()</i>.
	 */
	protected Structure getInitialStructure() {
		// Flat floor
		String floor = Inanimate.class.getSimpleName();
		String[][] lines = new String[1][];
		lines[0] = new String[] { floor, floor, floor, floor, floor, floor, floor, floor, floor, floor, floor, floor };
		Structure structure = new Structure(this.definition, lines);

		// keep track of how far we built (only works because we know structure
		// has no gap!)
		this.unitsBuilt += structure.getWidth();

		return structure;
	}

	/**
	 * <p>
	 * Used to hold and get the last {@link Structure} that will be the last
	 * {@link Structure} to be returned by <i>next()</i> before <i>hasNext()</i>
	 * indicates that there are no more {@link Structure Structures} to be
	 * returned. <br>
	 * Since this never happens on levels that have the setting <i>infinite</i>
	 * set (to anything other then 0), this method will never be called in these
	 * levels.
	 * </p>
	 * <p>
	 * Is thought to give the {@link Player} a characteristic ending and to add
	 * a {@link InanimateTrigger} that effectively ends the level upon
	 * collision.
	 * </p>
	 *
	 * @return the (optional) last {@link Structure} to be returned by
	 *         <i>next()</i>.
	 */
	protected Structure getEndStructure() {

		String verticalTrigger = EndTrigger.class.getSimpleName();
		// Vertical wall

		String[][] lines = new String[9][];
		for (int i = 0; i < lines.length; i++) {
			lines[i] = new String[1];
		}
		lines[4][0] = verticalTrigger;

		Structure structure = new Structure(this.definition, lines);

		// keep track of how far we built
		this.unitsBuilt += structure.getWidth();

		return structure;
	}

	/**
	 * Method used by <i>next</i> if the setting <i>shuffle</i> is set (to
	 * anything other then 0). Internally, it randomly gets a {@link Structure}
	 * from the {@link StructureManager}s <i>structures</i> and returns it.
	 * However, there will be added a gap with the width that
	 * <i>nextGapWidth()</i> returns and the internal value <i>unitsBuilt</i>
	 * will be incremented accordingly to the width of the {@link Structure} and
	 * its gap.
	 *
	 * @return the selected {@link Structure} that has just been randomly
	 *         determined.
	 */
	private Structure nextShuffeled() {
		// get random next Structure
		int randId = this.random.nextInt(this.definition.amountOfStructures());
		Structure selected = new Structure(this.definition, this.definition.getStructure(randId));

		// determine and set gap width
		int gap = this.nextGapWidth();
		selected.setGap(gap);

		// keep track of how far we built
		this.unitsBuilt += selected.getWidth() + gap;

		return selected;
	}

	/**
	 * Method used by <i>next</i> if the setting <i>shuffle</i> is not set (or
	 * 0). Internally, it uses the value <i>currentStructureId</i> to get the
	 * next {@link Structure} from the {@link StructureManager}s
	 * <i>structures</i> and returns it. However, there will be added a gap with
	 * the width that <i>nextGapWidth()</i> returns and the internal value
	 * <i>unitsBuilt</i> will be incremented accordingly to the width of the
	 * {@link Structure} and its gap.
	 *
	 * @return the selected {@link Structure} that is next in order.
	 */
	private Structure nextInOrder() {
		// get next Structure in order
		Structure selected = new Structure(this.definition, this.definition.getStructure(this.currentStructureId));

		// determine and set gap width
		int gap = this.nextGapWidth();
		selected.setGap(gap);

		// keep track of how far we built
		this.unitsBuilt += selected.getWidth() + gap;

		return selected;
	}

	/**
	 * Returns a random gapWidth between 1 and 2 if setting <i>doGaps</i> is set
	 * (to anything other then 0).
	 *
	 * @return the random gapWidth between 1 and 2.
	 */
	private int nextGapWidth() {
		return this.definition.isSettingSet(SettingKey.DO_GAPS) ? this.random.nextInt(2) + 1 : 0;
	}

	/**
	 * Indicates whether the level has more structures to build.
	 *
	 * @return {@code true} if more structures can be build.
	 */
	private boolean hasNextStructure() {
		return this.definition.isSettingSet(SettingKey.INFINITE) || this.currentStructureId < this.definition.amountOfStructures();
	}

	/**
	 * Add amount of units which were build outside.
	 *
	 * @param width
	 *            the width of the built structures
	 * @see BossSetting
	 */
	private void addUnitsBuild(int width) {
		this.unitsBuilt += width;
	}

	/**
	 * Uses information stored in the upper instance of {@link LevelDefinition}
	 * and the internal settings supplied by extending {@link Configurable} to
	 * do one of the following things:
	 * <ul>
	 * <li>Either the settings specify a {@link Boss} (=<i>settingValue</i>) to
	 * be spawned because the level generation reached the threshold
	 * (=<i>settingName</i>) and therefore returns a fully prepared
	 * {@link BossStructure} containing a {@link Boss} already.</li>
	 * <li>Or there is currently no Boss to be spawned at this time (or at all)
	 * and therefore return <i>null</i>.</li>
	 * </ul>
	 *
	 * @param fromX
	 *            the x-position in the level to start checking if a
	 *            {@link Boss} must be spawned.
	 * @param toX
	 *            the x-position in the level to end checking if a {@link Boss}
	 *            must be spawned.
	 * @return a {@link BossStructure} with a Boss attached if there must be a
	 *         {@link Boss} somewhere between the given values <i>fromX</i> and
	 *         <i>toX</i>, <i>null</i> otherwise.
	 */
	private Structure getNextBossOrNull(int fromX, int toX) {
		for (int i = fromX; i <= toX; i++) {
			String setting = "AT" + i;
			if (this.definition.getBossSetting(setting) != null) {
				GameElement bossGameElement = GameElementFactory.getPrototype(this.definition.getBossSetting(setting)).create(new Vec(), new String[] {});
				if (bossGameElement instanceof Boss) {
					Boss boss = (Boss) bossGameElement;
					Structure bossStructure = boss.getBossStructure();
					this.addUnitsBuild(bossStructure.getWidth());
					return bossStructure;
				} else {
					GameConf.GAME_LOGGER.error("Error while spawning Boss: " + this.definition.getBossSetting(setting) + " is not a BossID");
				}
			}
		}
		return null;
	}
}
