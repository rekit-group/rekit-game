package rekit.logic.level;

import java.util.ArrayList;
import java.util.List;

import rekit.logic.gameelements.type.Boss;
import rekit.persistence.level.LevelType;
import rekit.primitives.geometry.Vec;

/**
 * A special type of structure for a BossRush level.
 *
 * @author Dominik Fuchss
 *
 * @see LevelType#Boss_Rush
 *
 */
final class BossRushStructurePart extends StructurePart {
	private final List<Boss> bosses;
	private int next;

	/**
	 * Create StructurePart by interlink (LevelMtx).
	 *
	 * @param mtx
	 *            the interlink
	 */
	BossRushStructurePart(LevelMtx mtx) {
		super(mtx);
		this.bosses = new ArrayList<>();
		Boss.getPrototypes().forEach(e -> this.bosses.add((Boss) e));
		this.next = this.bosses.size();
	}

	@Override
	public Structure next() {
		if (this.next == this.bosses.size() || this.unitsBuilt == 0) {
			this.next = 0;
			this.shuffle();
			return this.getInitialStructure();
		}
		return this.bosses.get(this.next++).create(new Vec(), new String[] {}).getBossStructure();
	}

	private void shuffle() {
		List<Boss> newList = new ArrayList<>();
		while (!this.bosses.isEmpty()) {
			int idx = this.random.nextInt(this.bosses.size());
			newList.add(this.bosses.get(idx));
			this.bosses.remove(idx);
		}
		newList.forEach(this.bosses::add);
	}

}