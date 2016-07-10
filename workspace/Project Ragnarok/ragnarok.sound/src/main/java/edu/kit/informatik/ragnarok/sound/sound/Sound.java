package edu.kit.informatik.ragnarok.sound.sound;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public abstract class Sound {

	private final static Map<String, Clip> clips = new HashMap<>();

	private final static Random RNG = new Random();

	public static void play(SoundType type) {
		int variations = type.variations;

		int randomVariation = Sound.RNG.nextInt(variations);

		Clip clip = Sound.getClip(type.fileName + "_" + randomVariation);

		clip.start();
	}

	private static Clip getClip(String path) {
		if (!Sound.clips.containsKey(path)) {
			try {
				Clip clip = AudioSystem.getClip();
				AudioInputStream inputStream = AudioSystem.getAudioInputStream(Sound.class.getResourceAsStream(path + ".wav"));
				clip.open(inputStream);
				Sound.clips.put(path, clip);
			} catch (LineUnavailableException e) {

			} catch (IOException e) {

			} catch (UnsupportedAudioFileException e) {

			}
		}
		return Sound.clips.get(path);
	}
}
