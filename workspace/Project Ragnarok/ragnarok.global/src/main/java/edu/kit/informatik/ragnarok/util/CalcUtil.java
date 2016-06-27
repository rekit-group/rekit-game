package edu.kit.informatik.ragnarok.util;

import edu.kit.informatik.ragnarok.config.GameConf;
import edu.kit.informatik.ragnarok.primitives.Vec;

public class CalcUtil {
	public static int units2pixel(float units) {
		return (int) (units * GameConf.PX_PER_UNIT);
	}

	public static Vec units2vec(Vec pos) {
		return new Vec(pos.getX() * GameConf.PX_PER_UNIT, pos.getY() * GameConf.PX_PER_UNIT);
	}
}
