package ragnarok.logic.gui.parallax;

import ragnarok.config.GameConf;
import ragnarok.core.GameGrid;
import ragnarok.primitives.geometry.Polygon;
import ragnarok.primitives.geometry.Vec;
import ragnarok.primitives.image.RGBAColor;

public class HeapElementMountain extends HeapElement {

	@Override
	protected float elemNumMu() {
		return 4;
	}

	@Override
	protected float elemNumSigma() {
		return 1;
	}

	@Override
	protected float heapDistanceMu() {
		return 15;
	}

	@Override
	protected float heapDistanceSigma() {
		return 2;
	}

	@Override
	protected final int elemColRMu() {
		return 180;
	}

	@Override
	protected final int elemColRSigma() {
		return 10;
	}

	@Override
	protected final int elemColGMu() {
		return 160;
	}

	@Override
	protected final int elemColGSigma() {
		return 10;
	}

	@Override
	protected final int elemColBMu() {
		return 160;
	}

	@Override
	protected final int elemColBSigma() {
		return 10;
	}

	@Override
	protected final int elemColAMu() {
		return 255;
	}

	@Override
	protected final int elemColASigma() {
		return 0;
	}

	@Override
	protected float elemXMu() {
		return 0;
	}

	@Override
	protected float elemXSigma() {
		return 1.5f;
	}

	@Override
	protected final float elemYMu() {
		return GameConf.GRID_H - 3.2f;
	}

	@Override
	protected final float elemYSigma() {
		return 0;
	}

	@Override
	protected float elemWidthMu() {
		return 5.2f;
	}

	@Override
	protected float elemWidthSigma() {
		return 0.7f;
	}

	@Override
	protected float elemHeightMu() {
		return 2.5f;
	}

	@Override
	protected float elemHeightSigma() {
		return 1.8f;
	}

	private Polygon polygon;

	public HeapElementMountain(HeapLayer parent, Vec pos, Vec size, RGBAColor col) {
		super(parent, pos, size, col);
		if (size != null) {
			this.polygon = new Polygon(new Vec(), new Vec[] { new Vec(-size.getX() / 2, -size.getY()), new Vec(-size.getX(), 0), new Vec() });
		}

	}

	@Override
	public void internalRender(GameGrid f) {
		this.polygon.moveTo(this.getPos());
		f.drawPolygon(this.polygon, this.col, true);
	}

	@Override
	public HeapElement create(HeapLayer parent, Vec pos, Vec size, RGBAColor col) {
		return new HeapElementMountain(parent, pos, size, col);
	}

}
