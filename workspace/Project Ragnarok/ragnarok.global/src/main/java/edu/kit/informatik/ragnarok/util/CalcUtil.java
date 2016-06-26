package edu.kit.informatik.ragnarok.util;

import edu.kit.informatik.ragnarok.config.GameConf;
import edu.kit.informatik.ragnarok.primitives.Vec2D;

public class CalcUtil {
	public static int units2pixel(float units) {
		return (int) (units * GameConf.PX_PER_UNIT);
	}

	public static Vec2D units2vec(Vec2D pos) {
		return new Vec2D(pos.getX() * GameConf.PX_PER_UNIT, pos.getY() * GameConf.PX_PER_UNIT);
	}
}
