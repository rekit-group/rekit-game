package edu.kit.informatik.ragnarok.sound.sound;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public abstract class Sound {

	private final static Map<String, Clip> clips = new HashMap<>();

	private final static Random RNG = new Random();

	public static void play(SoundType type) {
		int variations = type.variations;

		int randomVariation = Sound.RNG.nextInt(variations);
		
		
		
		Clip clip = Sound.getClip(type.fileName + "_" + randomVariation);

	}

	private static Clip getClip(String path) {
		if (!Sound.clips.containsKey(path)) {

			InputStream inputStream = Sound.class.getResourceAsStream("/" + path + ".wav");

			try {
				AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(inputStream);

				AudioFormat format = audioInputStream.getFormat();

				audioInputStream = AudioSystem.getAudioInputStream(
						new AudioFormat(AudioFormat.Encoding.PCM_FLOAT, format.getSampleRate(), 32, format.getChannels(), 8, 4, false),
						audioInputStream);

				DataLine.Info info = new DataLine.Info(Clip.class, format);
				
				Clip clip = (Clip) AudioSystem.getLine(info);
				clip.open(audioInputStream);
				clip.start();

				// Sound.clips.put(path, clip);
			} catch (LineUnavailableException e) {

			} catch (IOException e) {

			} catch (UnsupportedAudioFileException e) {

			}
		}
		return Sound.clips.get(path);
	}
}
