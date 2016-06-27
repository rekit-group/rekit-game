package edu.kit.informatik.ragnarok.logic.gameelements.inanimate;

import edu.kit.informatik.ragnarok.logic.gameelements.Field;
import edu.kit.informatik.ragnarok.logic.gameelements.GameElement;
import edu.kit.informatik.ragnarok.primitives.Direction;
import edu.kit.informatik.ragnarok.primitives.Vec;
import edu.kit.informatik.ragnarok.util.RGBColor;

public class InanimateTrigger extends Inanimate {

	public InanimateTrigger(Vec pos, Vec size) {
		super(pos, size, new RGBColor(0, 0, 0));
	}
	
	public void render(Field f) {
		// Do nothing
	}

	public void reactToCollision(GameElement element, Direction dir) {
		if (this.isHostile(element)) {
			perform();	
		}
	}
	
	public void perform() {
		
	}

}
