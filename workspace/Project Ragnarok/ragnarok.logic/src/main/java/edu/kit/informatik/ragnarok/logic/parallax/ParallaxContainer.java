package edu.kit.informatik.ragnarok.logic.parallax;

import java.util.LinkedList;
import java.util.List;

import edu.kit.informatik.ragnarok.logic.gameelements.Field;

public class ParallaxContainer {

	private List<ParallaxLayer> layers;

	public ParallaxContainer() {
		this.layers = new LinkedList<ParallaxLayer>();
	}

	public void addLayer(ParallaxLayer layer) {
		this.layers.add(layer);
	}

	public void logicLoop(float currentOffset) {
		for (ParallaxLayer layer : this.layers) {
			layer.logicLoop(currentOffset);
		}
	}

	public void render(Field f) {
		long before = System.currentTimeMillis();
		for (ParallaxLayer layer : this.layers) {
			layer.render(f);
		}
		long after = System.currentTimeMillis();
		System.out.println(after - before);
	}

	public int getElementCount() {
		int result = 0;
		for (ParallaxLayer layer : this.layers) {
			result += layer.getElementCount();
		}
		return result;
	}

}
