package rekit.logic.gameelements.particles;

import java.awt.Font;

import rekit.config.GameConf;
import rekit.core.GameGrid;
import rekit.core.Team;
import rekit.primitives.TextOptions;
import rekit.primitives.geometry.Vec;

/**
 * {@link Particle} that shows a text, that can be specified via {@link TextParticle#setText(String)}
 *
 * @author Angelo Aracri
 */
public class TextParticle extends Particle {
	
	/**
	 * The internal text of the {@link Particle} that will be rendered
	 */
	private String text;
	
	/**
	 * The options of the {@link TextParticle#text}.
	 */
	private TextOptions options;
	
	/**
	 * Constructor that sets the {@link TextParticle DamageParticles}
	 * {@link Team} to {@link Team#ENEMY}.
	 */
	public TextParticle() {
		super();
		this.options = new TextOptions(new Vec(-0.6f, 0), 19, null, GameConf.GAME_TEXT_FONT, Font.BOLD);
	}
	
	/**
	 * Setter for the internally used {@link TextParticle#text} that will be rendered.
	 * @param text the text.
	 */
	public void setText(String text) {
		this.text = text;
	}
	
	@Override
	public void internalRender(GameGrid f) {
		this.options.setColor(this.currentCol);
		f.drawText(this.getPos(), this.text, this.options, true);
	}

	@Override
	public TextParticle create() {
		TextParticle newInstance = new TextParticle();
		newInstance.setText(this.text);
		return newInstance;
	}
}
