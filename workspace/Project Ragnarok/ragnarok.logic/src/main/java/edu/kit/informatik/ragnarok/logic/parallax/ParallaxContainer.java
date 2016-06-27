package edu.kit.informatik.ragnarok.logic.parallax;

import java.util.LinkedList;
import java.util.List;

import edu.kit.informatik.ragnarok.logic.scene.Scene;

public class ParallaxContainer {

	private List<ParallaxLayer> layers;
	private Scene scene;

	public ParallaxContainer(Scene scene) {
		this.layers = new LinkedList<ParallaxLayer>();
		this.scene = scene;
	}

	public void addLayer(ParallaxLayer layer) {
		layer.setScene(this.scene);
		this.layers.add(layer);
	}

	public void logicLoop(float currentOffset) {
		for (ParallaxLayer layer : this.layers) {
			layer.logicLoop(currentOffset);
		}
	}
}
