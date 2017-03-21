package rekit.logic.level;

import java.util.Random;

import rekit.logic.gameelements.entities.Player;
import rekit.logic.gameelements.inanimate.EndTrigger;
import rekit.logic.gameelements.inanimate.Inanimate;
import rekit.logic.gameelements.inanimate.InanimateTrigger;
import rekit.logic.gameelements.type.Enemy;
import rekit.logic.level.bossstructure.BossSetting;
import rekit.persistence.level.LevelDefinition;

/**
 *
 * This class holds all necessary information about a level.
 *
 */
public final class Level implements Comparable<Level> {

	private final LevelDefinition definition;
	private int generatedUntil;

	private int unitsBuilt;
	private int lastUnitsBuilt;

	private int currentStructureId;
	private Random rand;

	private final BossSetting bosssetting;

	public Level(LevelDefinition definition) {
		this.definition = definition;
		this.rand = new Random(definition.getSeed());
		this.bosssetting = new BossSetting(this);
	}

	public void reset() {
		this.currentStructureId = -1;
		this.unitsBuilt = 0;
		this.generatedUntil = 0;
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
	public void generate(int max) {
		while (this.generatedUntil < max && this.hasNext()) {
			// build structure
			this.generatedUntil += this.next().build(this.generatedUntil + 1, this.definition.isSettingSet("autoCoinSpawn"));
		}
	}

	public Structure next() {

		// if this is level start: build initial Structure
		if (this.unitsBuilt == 0) {
			return this.getInitialStructure();
		} else {
			// increment structureId / count
			this.currentStructureId++;

			// if end of sequence reached: Determine if to repeat or end level
			if (this.currentStructureId >= this.definition.size()) {
				if (this.definition.isSettingSet("infinite")) {
					this.currentStructureId = 0;
				} else {
					return this.getEndStructure();
				}
			}

			// get a BossStructure or null.
			Structure bossStructureOrNull = this.bosssetting.getNextOrNull(this.lastUnitsBuilt, this.unitsBuilt);

			// keep track of last <i>unitsBuilt</i> before it is incremented.
			this.lastUnitsBuilt = this.unitsBuilt;

			// return BossStructure if we want it (no usual Structure in this
			// case!)
			if (bossStructureOrNull != null) {
				return bossStructureOrNull;
			}

			// get next Structure depending on strategy (shuffled / in order)
			if (this.definition.isSettingSet("shuffle")) {
				return this.nextShuffeled();
			} else {
				return this.nextInOrder();
			}
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
	private Structure getInitialStructure() {
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
	private Structure getEndStructure() {

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
		int randId = this.rand.nextInt(this.definition.size());
		Structure selected = new Structure(this.definition, this.definition.get(randId));

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
		Structure selected = new Structure(this.definition, this.definition.get(this.currentStructureId));

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
		return this.definition.isSettingSet("doGaps") ? this.rand.nextInt(2) + 1 : 0;
	}

	@Override
	public final int compareTo(Level o) {
		return this.definition.compareTo(o.definition);
	}

	public boolean hasNext() {
		return this.definition.isSettingSet("infinite") || this.currentStructureId < this.definition.size();
	}

	public void addUnitsBuild(int width) {
		this.unitsBuilt += width;
	}

	public LevelDefinition getDefinition() {
		return this.definition;
	}

	public String getName() {
		return this.definition.getName();
	}
}