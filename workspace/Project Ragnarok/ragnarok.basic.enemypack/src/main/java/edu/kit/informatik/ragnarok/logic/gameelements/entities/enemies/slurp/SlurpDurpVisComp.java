package edu.kit.informatik.ragnarok.logic.gameelements.entities.enemies.slurp;

import edu.kit.informatik.ragnarok.core.Field;
import edu.kit.informatik.ragnarok.primitives.geometry.Vec;
import edu.kit.informatik.ragnarok.primitives.image.RGBColor;

/**
 * Simple data-holding class that represents a component of the visualization
 * of a SlurpDurp (Thus, the name)
 * 
 * @author Angelo Aracri
 */
public class SlurpDurpVisComp {
	private Vec relativePos;
	private Vec relativeSize;
	private RGBColor col;
	
	public SlurpDurpVisComp(Vec relativePos, Vec relativeSize, RGBColor col) {
		this.relativePos = relativePos;
		this.relativeSize = relativeSize;
		this.col = col;
	}
	public void render(Field f, Vec pos, Vec size) {
		Vec absSize = size.multiply(relativeSize);
		f.drawCircle(pos.add(this.relativePos.multiply(absSize)), absSize, col);
	}
}
