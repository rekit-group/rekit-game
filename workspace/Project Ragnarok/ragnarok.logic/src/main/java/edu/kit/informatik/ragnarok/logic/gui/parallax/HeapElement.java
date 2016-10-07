package edu.kit.informatik.ragnarok.logic.gui.parallax;

import edu.kit.informatik.ragnarok.core.Field;
import edu.kit.informatik.ragnarok.primitives.geometry.Vec;
import edu.kit.informatik.ragnarok.primitives.image.RGBAColor;

/**
 *
 * This class shall be extended by each heap element.
 *
 */
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
		this.setSize(size);
		this.col = col;
	}

	@Override
	public abstract void internalRender(Field f);

	public abstract HeapElement create(HeapLayer parent, Vec pos, Vec size, RGBAColor col);
}
