package com.example.typejam;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

/**
 * Singleton class to manage all sound effects and background music in the application.
 * Integrates with SettingsManager to respect user preferences for sound effects and music.
 */
public class SoundManager {

    private static SoundManager instance;

    // MediaPlayer instances for sound effects
    private MediaPlayer typingSoundPlayer;
    private MediaPlayer errorSoundPlayer;
    private MediaPlayer countdownTimerPlayer;
    private MediaPlayer congratulatorySoundPlayer;
    private MediaPlayer buttonClickPlayer; // Added MediaPlayer for button click sound

    // MediaPlayer for background music
    private MediaPlayer backgroundMusicPlayer;

    // Track if countdown has been played (to avoid repeating)
    private boolean countdownPlayed = false;

    private SoundManager() {
        initializeSounds();
    }

    public static SoundManager getInstance() {
        if (instance == null) {
            instance = new SoundManager();
        }
        return instance;
    }

    private void initializeSounds() {
        try {
            // Load typing sound
            Media typingMedia = new Media(getClass().getResource("/assets/sounds/single-kb-type.wav").toExternalForm());
            typingSoundPlayer = new MediaPlayer(typingMedia);

            // Load error sound
            Media errorMedia = new Media(getClass().getResource("/assets/sounds/error-buzz.mp3").toExternalForm());
            errorSoundPlayer = new MediaPlayer(errorMedia);

            // Load countdown timer sound
            Media countdownMedia = new Media(getClass().getResource("/assets/sounds/countdown-timer.wav").toExternalForm());
            countdownTimerPlayer = new MediaPlayer(countdownMedia);

            // Load congratulatory sound
            Media congratulatoryMedia = new Media(getClass().getResource("/assets/sounds/congratulatory.mp3").toExternalForm());
            congratulatorySoundPlayer = new MediaPlayer(congratulatoryMedia);

            // Load background music
            Media musicMedia = new Media(getClass().getResource("/assets/sounds/friendly-melody.mp3").toExternalForm());
            backgroundMusicPlayer = new MediaPlayer(musicMedia);
            backgroundMusicPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Loop indefinitely

            // Load button click sound
            Media buttonClickMedia = new Media(getClass().getResource("/assets/sounds/button-click.mp3").toExternalForm());
            buttonClickPlayer = new MediaPlayer(buttonClickMedia);

            // Apply initial volumes from settings
            updateVolumes();

            System.out.println("SoundManager: All sounds initialized successfully");
        } catch (Exception e) {
            System.err.println("Error initializing sounds: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Update volume levels from SettingsManager
     */
    private void updateVolumes() {
        SettingsManager settings = SettingsManager.getInstance();
        double sfxVolume = settings.getSfxVolume();
        double musicVolume = settings.getMusicVolume();

        if (typingSoundPlayer != null) typingSoundPlayer.setVolume(sfxVolume);
        if (errorSoundPlayer != null) errorSoundPlayer.setVolume(sfxVolume * 0.8); // Slightly lower for error
        if (countdownTimerPlayer != null) countdownTimerPlayer.setVolume(sfxVolume);
        if (congratulatorySoundPlayer != null) congratulatorySoundPlayer.setVolume(musicVolume);
        if (backgroundMusicPlayer != null) backgroundMusicPlayer.setVolume(musicVolume);
        if (buttonClickPlayer != null) buttonClickPlayer.setVolume(sfxVolume); // Set volume for button click sound
    }

    /**
     * Play typing sound effect
     */
    public void playTypingSound() {
        if (!SettingsManager.getInstance().isSoundEffectsEnabled()) return;

        if (typingSoundPlayer != null) {
            typingSoundPlayer.stop();
            typingSoundPlayer.seek(Duration.ZERO);
            typingSoundPlayer.play();
        }
    }

    /**
     * Play error/buzz sound when incorrect character is typed
     */
    public void playErrorSound() {
        if (!SettingsManager.getInstance().isSoundEffectsEnabled()) return;

        if (errorSoundPlayer != null) {
            errorSoundPlayer.stop();
            errorSoundPlayer.seek(Duration.ZERO);
            errorSoundPlayer.play();
        }
    }

    /**
     * Play countdown timer sound (when 4 seconds remain)
     */
    public void playCountdownTimer() {
        if (!SettingsManager.getInstance().isSoundEffectsEnabled()) return;

        if (countdownTimerPlayer != null && !countdownPlayed) {
            countdownTimerPlayer.stop();
            countdownTimerPlayer.seek(Duration.ZERO);
            countdownTimerPlayer.play();
            countdownPlayed = true;
        }
    }

    /**
     * Reset countdown played flag (call this when starting a new game)
     */
    public void resetCountdown() {
        countdownPlayed = false;
    }

    /**
     * Play congratulatory sound (with confetti on result scene)
     */
    public void playCongratulatorySound() {
        if (!SettingsManager.getInstance().isMusicEnabled()) return;

        if (congratulatorySoundPlayer != null) {
            congratulatorySoundPlayer.stop();
            congratulatorySoundPlayer.seek(Duration.ZERO);
            congratulatorySoundPlayer.play();
        }
    }

    /**
     * Start background music (loops indefinitely)
     */
    public void startBackgroundMusic() {
        if (!SettingsManager.getInstance().isMusicEnabled()) return;

        if (backgroundMusicPlayer != null) {
            if (backgroundMusicPlayer.getStatus() != MediaPlayer.Status.PLAYING) {
                backgroundMusicPlayer.play();
            }
        }
    }

    /**
     * Stop background music
     */
    public void stopBackgroundMusic() {
        if (backgroundMusicPlayer != null) {
            backgroundMusicPlayer.stop();
        }
    }

    /**
     * Play button click sound
     */
    public void playButtonClick() {
        if (!SettingsManager.getInstance().isSoundEffectsEnabled()) return;
        if (buttonClickPlayer != null) {
            buttonClickPlayer.stop();
            buttonClickPlayer.seek(Duration.ZERO);
            buttonClickPlayer.play();
        }
    }

    /**
     * Set sound effects volume (0.0 to 1.0)
     */
    public void setSfxVolume(double volume) {
        if (typingSoundPlayer != null) typingSoundPlayer.setVolume(volume);
        if (errorSoundPlayer != null) errorSoundPlayer.setVolume(volume * 0.8);
        if (countdownTimerPlayer != null) countdownTimerPlayer.setVolume(volume);
        if (buttonClickPlayer != null) buttonClickPlayer.setVolume(volume); // Set volume for button click sound
    }

    /**
     * Set music volume (0.0 to 1.0)
     */
    public void setMusicVolume(double volume) {
        if (backgroundMusicPlayer != null) backgroundMusicPlayer.setVolume(volume);
        if (congratulatorySoundPlayer != null) congratulatorySoundPlayer.setVolume(volume);
    }

    /**
     * Stop all sounds (cleanup)
     */
    public void stopAll() {
        if (typingSoundPlayer != null) typingSoundPlayer.stop();
        if (errorSoundPlayer != null) errorSoundPlayer.stop();
        if (countdownTimerPlayer != null) countdownTimerPlayer.stop();
        if (congratulatorySoundPlayer != null) congratulatorySoundPlayer.stop();
        if (buttonClickPlayer != null) buttonClickPlayer.stop(); // Stop button click sound if playing
        stopBackgroundMusic();
    }
}
