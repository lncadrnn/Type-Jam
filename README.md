# Type-Jam

A JavaFX typing practice game to help students build speed and accuracy. Includes two modes, difficulty levels, leaderboards, and a polished sound system.

## Features
- Endless (Practice) and Time Challenge modes
- Easy / Medium / Hard difficulties
- Live WPM and Accuracy calculations
- Leaderboards saved locally
- Background music and sound effects with volume controls

## Sound System
- Background music: loops on all scenes except typing-game, result-scene, and practice-result-scene
- Sound effects:
  - Typing (correct): `assets/sounds/single-kb-type.wav`
  - Error (incorrect): `assets/sounds/error-buzz.mp3`
  - Countdown (4s remaining in Challenge Mode): `assets/sounds/countdown-timer.wav`
  - Result celebration (with confetti): `assets/sounds/congratulatory.mp3`
- Settings:
  - Toggle Sound Effects and Background Music on/off
  - Independent volume sliders for SFX and Music (0–100%)
- Persistence: preferences saved to `%USERPROFILE%/typejam-settings.json`

## How Scoring Works
- WPM = (Total Characters Typed / 5) / Time in Minutes
- Accuracy = (Total Characters Typed / (Total Characters Typed + Total Incorrect Keystrokes)) × 100
- Stars (0–5) require meeting both WPM and Accuracy thresholds (examples: 5★ = 60+ WPM and 95%+ accuracy; 3★ = 30+ WPM and 80%+ accuracy)

## Quick Start (Windows PowerShell)
```powershell
# Build the project
.\mvnw.cmd clean package

# Run the JavaFX application
.\mvnw.cmd javafx:run
```
If you use a system-wide Maven installation, replace `mvnw.cmd` with `mvn`.

## Customize Practice Texts
Edit `src/main/resources/data/typing-texts.json` to add your own sentences under `easy`, `medium`, and `hard`.

## Project Layout
- `src/main/java` — controllers and app code
- `src/main/resources` — FXML, images, fonts, CSS, sounds
- `data/typing-texts.json` — customizable practice texts
- `pom.xml` — Maven build config

## Notes
- JavaFX dependencies (controls, FXML, media) are managed via Maven; use the wrapper `.\mvnw.cmd` for consistent builds.

## License / Acknowledgements
- Built for Bacoor National High School (senior high) to support typing practice.
- Educational use encouraged.
