package rekit.logic.gui.parallax;

import rekit.core.GameGrid;
import rekit.primitives.geometry.Vec;
import rekit.primitives.image.RGBAColor;

public class HeapElementCloud extends HeapElement {

	public HeapElementCloud(HeapLayer parent, Vec pos, Vec size, RGBAColor col) {
		super(parent, pos, size, col);
		// R, G and B channel must be same for clouds
		if (col != null) {
			this.col = new RGBAColor(col.red, col.red, col.red, col.alpha);
		}
	}

	@Override
	public void internalRender(GameGrid f) {
		f.drawCircle(this.getPos(), this.getSize(), this.col);
	}

	@Override
	public HeapElement create(HeapLayer parent, Vec pos, Vec size, RGBAColor col) {
		return new HeapElementCloud(parent, pos, size, col);
	}

}
