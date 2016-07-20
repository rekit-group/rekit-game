package edu.kit.informatik.ragnarok.logic.gameelements.entities.pickups;

import edu.kit.informatik.ragnarok.logic.Field;
import edu.kit.informatik.ragnarok.logic.gameelements.GameElement;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.Entity;
import edu.kit.informatik.ragnarok.logic.gameelements.type.Pickup;
import edu.kit.informatik.ragnarok.primitives.geometry.Vec;
import edu.kit.informatik.ragnarok.util.ReflectUtils.LoadMe;

/**
 * This class defines a simple {@link Pickup}; a <b>Life</b> which will give the
 * player lives.
 */
@LoadMe
public class Life extends Pickup {
	/**
	 * Prototype Constructor
	 */
	public Life() {
		super();
	}

	/**
	 * Instantiate a Life by start position
	 *
	 * @param startPos
	 *            the start position
	 */
	public Life(Vec startPos) {
		super(startPos, new Vec(), new Vec(1));
	}

	@Override
	public void perform(GameElement collector) {
		collector.addDamage(-1);
		this.addDamage(1);
	}

	@Override
	public void internalRender(Field f) {
		f.drawImage(this.getPos(), this.getSize(), "mrRekt_glasses_left.png");
	}

	@Override
	public Entity create(Vec startPos, String[] options) {
		return new Life(startPos);
	}

}
