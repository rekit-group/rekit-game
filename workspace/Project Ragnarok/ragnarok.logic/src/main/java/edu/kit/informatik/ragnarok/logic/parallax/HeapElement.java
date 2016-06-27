package edu.kit.informatik.ragnarok.logic.parallax;

import edu.kit.informatik.ragnarok.logic.gameelements.Field;
import edu.kit.informatik.ragnarok.primitives.Vec;
import edu.kit.informatik.ragnarok.util.RGBAColor;

public abstract class HeapElement extends BackgroundElement {

	protected float heapDistanceMu() {
		return 9;
	}

	protected float heapDistanceSigma() {
		return 2;
	}

	protected float elemNumMu() {
		return 7;
	}

	protected float elemNumSigma() {
		return 3;
	}

	protected int elemColRMu() {
		return 240;
	}

	protected int elemColRSigma() {
		return 15;
	}

	protected int elemColGMu() {
		return 240;
	}

	protected int elemColGSigma() {
		return 15;
	}

	protected int elemColBMu() {
		return 240;
	}

	protected int elemColBSigma() {
		return 15;
	}

	protected int elemColAMu() {
		return 220;
	}

	protected int elemColASigma() {
		return 30;
	}

	protected float elemXMu() {
		return 0;
	}

	protected float elemXSigma() {
		return 2;
	}

	protected float elemYMu() {
		return 2;
	}

	protected float elemYSigma() {
		return 1;
	}

	protected float elemWidthMu() {
		return 2.5f;
	}

	protected float elemWidthSigma() {
		return 1;
	}

	protected float elemHeightMu() {
		return 1.5f;
	}

	protected float elemHeightSigma() {
		return 0.8f;
	}

	protected RGBAColor col;

	public HeapElement(HeapLayer parent, Vec pos, Vec size, RGBAColor col) {
		super(parent, pos);
		this.size = size;
		this.col = col;
	}

	@Override
	public abstract void render(Field f);

	public abstract HeapElement clone(HeapLayer parent, Vec pos, Vec size, RGBAColor col);
}
