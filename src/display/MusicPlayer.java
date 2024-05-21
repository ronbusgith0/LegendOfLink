package display;

import java.io.IOException;
import java.net.URISyntaxException;

import javax.sound.sampled.*;

public class MusicPlayer {
	private static AudioInputStream audioIn;
	private static Clip clip;
	private static String currentMusic = null;

	public static void playSoundEffect(String file) {
		try {
			AudioInputStream tempAudioIn = AudioSystem
					.getAudioInputStream(MusicPlayer.class.getResource(file));
			Clip tempClip = AudioSystem.getClip();
			tempClip.open(tempAudioIn);
			tempClip.setFramePosition(0);
			tempClip.loop(0);
			tempClip.start();
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Unable to find music file " + file);
		}
	}

	private static void loopSound(String file)
			throws UnsupportedAudioFileException, IOException,
			LineUnavailableException, URISyntaxException {
		audioIn = AudioSystem.getAudioInputStream(MusicPlayer.class
				.getResource(file));
		clip = AudioSystem.getClip();
		clip.open(audioIn);
		clip.loop(Clip.LOOP_CONTINUOUSLY);
	}

	public static void changePlayingMusic(String newMusic) {
		try {
			if (newMusic == null) {
				currentMusic = newMusic;
				if (clip != null)
					clip.stop();
				return;
			}
			if (newMusic.equals(currentMusic))
				return;
			if (clip != null)
				clip.stop();
			loopSound(newMusic);
			currentMusic = newMusic;
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Error playing music " + newMusic);
		}
	}

	public static boolean isMusicPlaying() {
		if (clip != null) {
			return true;
		}
		return false;
	}
	
	public String getPlayingMusic() {
		return currentMusic;
	}
	
	private MusicPlayer() {}
}