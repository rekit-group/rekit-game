package rekit.logic.level;

import java.util.Random;

import rekit.persistence.level.LevelDefinition;

/**
 * This is the MTX-Object (a connector/interlink) of the {@link Level},
 * {@link StructurePart} and {@link LogicalPart} which holds necessary
 * information.
 *
 * @author Dominik Fuchss
 *
 */
class LevelMtx {
	private final LevelDefinition definition;
	private final Random random;

	/**
	 * Create LevelMtx by LevelDefinition.
	 *
	 * @param definition
	 *            the definition
	 */
	LevelMtx(LevelDefinition definition) {
		this.definition = definition;
		this.random = new Random(this.definition.getSeed());
	}

	/**
	 * Get {@link LevelDefinition}.
	 *
	 * @return the level's definition
	 */
	LevelDefinition getDefinition() {
		return this.definition;
	}

	/**
	 * The source of randomness of the level.
	 *
	 * @return the random source
	 */
	Random getRandom() {
		return this.random;
	}

}
