package rekit.logic.level;

import java.util.Random;

import rekit.persistence.level.LevelDefinition;

/**
 * This is the MTX-Object of the {@link Level}, {@link StructurePart} and
 * {@link LogicalPart} which holds necessary information.
 *
 * @author Dominik Fuchss
 *
 */
class LevelMtx {
	private final LevelDefinition definition;
	private final Random random;

	LevelMtx(LevelDefinition definition) {
		this.definition = definition;
		this.random = new Random(this.definition.getSeed());
	}

	LevelDefinition getDefinition() {
		return this.definition;
	}

	Random getRandom() {
		return this.random;
	}

}
