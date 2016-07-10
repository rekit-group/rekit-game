package edu.kit.informatik.ragnarok.sound.sound;

public enum SoundType {
	Coin("coin", 4);

	protected final String fileName;
	protected final int variations;

	SoundType(String fileName, int variations) {
		this.fileName = fileName;
		this.variations = variations;
	}
}
