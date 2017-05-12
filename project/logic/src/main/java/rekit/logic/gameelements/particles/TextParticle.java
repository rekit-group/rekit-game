package rekit.logic.gameelements.particles;

import java.awt.Font;

import rekit.config.GameConf;
import rekit.core.GameGrid;
import rekit.core.Team;
import rekit.primitives.TextOptions;
import rekit.primitives.geometry.Vec;

/**
 * {@link Particle} that shows a text, that can be specified via
 * {@link TextParticle#setText(String)}
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

	public TextParticle() {
		this(19);
	}

	/**
	 * Constructor that sets the {@link TextParticle DamageParticles}
	 * {@link Team} to {@link Team#ENEMY}.
	 */
	public TextParticle(int fontSize) {
		super();
		this.options = new TextOptions(new Vec(-0.6f, 0), fontSize, null, GameConf.GAME_TEXT_FONT, Font.BOLD);
	}

	/**
	 * Setter for the internally used {@link TextParticle#text} that will be
	 * rendered.
	 * 
	 * @param text
	 *            the text.
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * Setter for the internally used font size.
	 * 
	 * @param text
	 *            the font size.
	 */
	public void setSize(int size) {
		this.options = this.options.setHeight(size);
	}

	@Override
	public void internalRender(GameGrid f) {
		this.options.setColor(this.currentCol);
		f.drawText(this.getPos(), this.text, this.options, true);
	}

	@Override
	public TextParticle create() {
		TextParticle newInstance = new TextParticle(this.options.getHeight());
		newInstance.setText(this.text);
		return newInstance;
	}
}
