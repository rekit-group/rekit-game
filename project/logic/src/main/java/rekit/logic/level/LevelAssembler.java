package rekit.logic.level;

import rekit.logic.gameelements.GameElement;

/**
 * <p>
 * Manages assembling a level piece by piece for not having to build a level
 * whole at once. The method <i>generate(int max)</i> must be called at least
 * every time <i>max</i> changes to give the LevelAssembler the change to check
 * if it must generate more {@link GameElement GameElements}.
 * </p>
 * <p>
 * Internally, it uses a {@link StructureManager} to get the the next pieces (=
 * {@link Structure}) to be build.
 * </p>
 *
 * @author Angelo Aracri
 * @version 1.0
 */
public final class LevelAssembler {
	/**
	 * Position of the current end of the level (x pos).
	 */
	private int generatedUntil;
	/**
	 * The structure manager.
	 */
	private final StructureManager manager;

	/**
	 * Constructor that initializes attributes and instructs a new
	 * {@link StructureManager} to load a level file specified by the
	 * <i>levelName</i>. The <i>seed</i> is used for pseudo-randomizing.
	 *
	 * @param manager
	 *            the structure of the level to be assembled.
	 */
	public LevelAssembler(StructureManager manager) {
		if (manager == null) {
			throw new IllegalArgumentException("StructureManager cannot be null");
		}
		this.manager = manager;
	}

	/**
	 * Initializes all attributes. Must also be called upon re-initialization
	 * (dying, loading new level, ...).
	 */
	public void reset() {
		this.generatedUntil = 0;
		this.manager.reset();
	}

	/**
	 * Orders the StructureManager to build {@link Structure}s if it must. This
	 * decision depends on the {@link Structure}s build so far and the parameter
	 * <i>max</i> that specifies which x position is the smallest that needs to
	 * be generated at.
	 *
	 * @param max
	 *            the lowest x position that must still be generated at.
	 */
	public void generate(int max) {
		while (this.generatedUntil < max && this.manager.hasNext()) {
			// build structure
			this.generatedUntil += this.manager.next().build(this.generatedUntil + 1, this.manager.isSettingSet("autoCoinSpawn"));
		}
	}

}
