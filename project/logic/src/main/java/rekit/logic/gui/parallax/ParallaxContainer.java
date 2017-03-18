package rekit.logic.gui.parallax;

import java.util.LinkedList;
import java.util.List;

import rekit.logic.IScene;

public class ParallaxContainer {

	private List<ParallaxLayer> layers;
	private IScene scene;

	public ParallaxContainer(IScene scene) {
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
