package edu.kit.informatik.ragnarok.logic.gameelements.inanimate;

import org.eclipse.swt.graphics.RGB;

import edu.kit.informatik.ragnarok.gui.Field;
import edu.kit.informatik.ragnarok.logic.gameelements.GameElement;
import edu.kit.informatik.ragnarok.primitives.Direction;
import edu.kit.informatik.ragnarok.primitives.Vec2D;

public class InanimateTrigger extends Inanimate {

	public InanimateTrigger(Vec2D pos, Vec2D size) {
		super(pos, size, new RGB(0, 0, 0));
	}
	
	public void render(Field f) {
		
	}

	public void reactToCollision(GameElement element, Direction dir) {
		if (this.isHostile(element)) {
			perform();	
		}
	}
	
	public void perform() {
		
	}

}
