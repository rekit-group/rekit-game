package edu.kit.informatik.ragnarok.logic.gui.parallax;

import java.util.LinkedList;
import java.util.List;

import edu.kit.informatik.ragnarok.core.Scene;

public class ParallaxContainer {

	private List<ParallaxLayer> layers;
	private Scene scene;

	public ParallaxContainer(Scene scene) {
		this.layers = new LinkedList<>();
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
