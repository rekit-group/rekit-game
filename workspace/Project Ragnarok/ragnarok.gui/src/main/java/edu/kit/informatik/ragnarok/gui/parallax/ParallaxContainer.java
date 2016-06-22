package edu.kit.informatik.ragnarok.gui.parallax;

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
	
	public void render(Field f, float currentOffset) {
		for (ParallaxLayer layer : this.layers) {
			layer.render(f, currentOffset);
		}
	}
	
	
	
}
