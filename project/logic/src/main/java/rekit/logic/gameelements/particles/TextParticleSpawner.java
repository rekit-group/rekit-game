package rekit.logic.gameelements.particles;

/**
 * A custom implementation of the {@link ParticleSpawner} that sets the settings
 * for an internal {@link TextParticle} that it can
 * {@link ParticleSpawner#spawn}.
 *
 * @author Angelo Aracri
 */
public final class TextParticleSpawner extends ParticleSpawner {

	/**
	 * The constructor that sets the required settings
	 */
	public TextParticleSpawner() {
		this.amountMin = this.amountMax = 1;
		this.angle = new ParticleSpawnerOption(0);
		this.colorR = new ParticleSpawnerOption(150);
		this.colorG = new ParticleSpawnerOption(30);
		this.colorB = new ParticleSpawnerOption(30);
		this.colorA = new ParticleSpawnerOption(255, -255);
		this.speed = new ParticleSpawnerOption(1, 0.5f);
		this.timeMin = this.timeMax = 1;
		this.particlePrototype = new TextParticle();
	}

	/**
	 * Sets the text of the {@link TextParticle}.
	 *
	 * @param text
	 *            the text
	 */
	public void setText(String text) {
		((TextParticle) this.particlePrototype).setText(text);
	}

	/**
	 * Sets the text size of the {@link TextParticle}.
	 *
	 * @param size
	 *            the size
	 */
	public void setSize(int size) {
		((TextParticle) this.particlePrototype).setSize(size);
	}
}
