package edu.kit.informatik.ragnarok.logic.parallax;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import edu.kit.informatik.ragnarok.logic.gameelements.Field;
import edu.kit.informatik.ragnarok.primitives.Vec2D;
import edu.kit.informatik.ragnarok.util.RGBAColor;

public class CloudLayer extends ParallaxLayer {

	private final float cloudHeapDistanceMu = 9;
	private final float cloudHeapDistanceSigma = 2;
	
	private final float cloudHeapCloudNumMu = 7;
	private final float cloudHeapCloudNumSigma = 3;
	
	private final int cloudHeapRGBMu = 240;
	private final int cloudHeapRGBSigma = 15;
	
	private final int cloudHeapAMu = 220;
	private final int cloudHeapASigma = 30;
	
	private final float cloudHeapXMu = 0;
	private final float cloudHeapXSigma = 2;
	
	private final float cloudHeapYMu = 2;
	private final float cloudHeapYSigma = 1;
	
	private final float cloudHeapWidthMu = 2.5f;
	private final float cloudHeapWidthSigma = 1;
	
	private final float cloudHeapHeightMu = 1.5f;
	private final float cloudHeapHeightSigma = 0.8f;
	
	
	private final Random RNG = new Random();
	
	private float currentlyGeneratedUntil = 0;
	
	/**
	 * Holds a list of all cloudHeaps to render
	 */
	private List<Ellipse> cloudHeaps;
	
	private final Object sync = new Object();
	
	private class Ellipse {
		private Vec2D pos;
		private Vec2D size;
		private RGBAColor col;
		public Ellipse(Vec2D pos, Vec2D size, RGBAColor col) {
			this.pos = pos;
			this.size = size;
			this.col = col;
		}
		public Vec2D getFieldPos() {
			return pos.addX(CloudLayer.this.fieldXtoLayerX(CloudLayer.this.x));
		}
		public void render(Field f) {
			f.drawCircle(getFieldPos(), size, col);
		}
	}
	
	public CloudLayer(float distanceFromFront) {
		super(distanceFromFront);
		cloudHeaps = new LinkedList<Ellipse>();
	}
	
	
	public void logicLoop(float currentOffset) {
		// calculates this.x and this.generateUntil
		super.logicLoop(currentOffset);
		
		// while we need to generate
		while (this.currentlyGeneratedUntil + CloudLayer.this.fieldXtoLayerX(CloudLayer.this.x) < this.generateUntil) {
			float randDist = calcWithVariance(cloudHeapDistanceMu, cloudHeapDistanceSigma);
			float randAmount = calcWithVariance(cloudHeapCloudNumMu, cloudHeapCloudNumSigma);
			
			this.currentlyGeneratedUntil += randDist;
			
			
			for (int i = 0; i < randAmount; i++) {
				// Calc Position (relative to screen!)
				float randX = this.currentlyGeneratedUntil + calcWithVariance(cloudHeapXMu, cloudHeapXSigma);
				float randY = calcWithVariance(cloudHeapYMu, cloudHeapYSigma);
				Vec2D pos = new Vec2D(randX, randY);
				
				// Calc Size
				float randW = calcWithVariance(cloudHeapWidthMu, cloudHeapWidthSigma);
				float randH = calcWithVariance(cloudHeapHeightMu, cloudHeapHeightSigma);
				Vec2D size = new Vec2D(randW, randH);
				
				// Calc Color
				int randRGB = (int) calcWithVariance(cloudHeapRGBMu, cloudHeapRGBSigma);
				int randA = (int) calcWithVariance(cloudHeapAMu, cloudHeapASigma);
				RGBAColor col = new RGBAColor(randRGB, randRGB, randRGB, randA);
				
				// Create actual Ellipse object and (blockingly) add it
				Ellipse cloud = new Ellipse(pos, size, col);
				synchronized (this.synchronize()) {
					this.cloudHeaps.add(cloud);
				}
			}
			
		}
	}

	public void render(Field f) {
		
		// call super method before this (drawing white rectangle)
		super.render(f);
		synchronized (this.synchronize()) {
			Iterator<Ellipse> it = this.cloudHeaps.iterator();
			while (it.hasNext()) {
				Ellipse e = it.next();
				if (e.getFieldPos().getX() + e.size.getX() < this.x) {
					it.remove();
				} else {
					e.render(f);
				}
			}
		}
	}
	
	public float calcWithVariance(float mu, float sigma) {
		return mu + (this.RNG.nextFloat() * 2 * sigma) - sigma;
	}
	
	public Object synchronize() {
		return this.sync;
	}

}
