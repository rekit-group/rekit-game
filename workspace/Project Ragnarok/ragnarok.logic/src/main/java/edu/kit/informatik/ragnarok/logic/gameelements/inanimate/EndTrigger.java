package edu.kit.informatik.ragnarok.logic.gameelements.inanimate;

import edu.kit.informatik.ragnarok.config.GameConf;
import edu.kit.informatik.ragnarok.logic.gameelements.Field;
import edu.kit.informatik.ragnarok.primitives.Vec;
import edu.kit.informatik.ragnarok.util.RGBAColor;

public class EndTrigger extends InanimateTrigger {

	private static EndTrigger instance;

	public EndTrigger(Vec pos, Vec size) {
		super(pos, size);
	}

	@Override
	public void internalRender(Field f) {
		f.drawRectangle(this.getPos(), this.getSize(), new RGBAColor(50, 200, 50, 100));
	}

	@Override
	public void perform() {
		this.scene.end(true);
	}

	@Override
	public EndTrigger create(Vec pos) {
		return new EndTrigger(pos, this.getSize());
	}

	public static Inanimate getPrototype() {
		if (EndTrigger.instance == null) {
			EndTrigger.instance = new EndTrigger(new Vec(), new Vec(1, GameConf.GRID_H));
		}
		return EndTrigger.instance;
	}
}
