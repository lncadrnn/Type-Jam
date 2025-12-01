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
- Persistence: 
  - Settings: `src/main/resources/data/typejam-settings.json`
  - Leaderboard: `src/main/resources/data/typejam-leaderboard.json`

## How Scoring Works
- **WPM** = (Total Characters Typed / 5) / Time in Minutes
- **Accuracy** = (Total Characters Typed / (Total Characters Typed + Total Incorrect Keystrokes)) × 100
- **Star Rating (0–5)** uses a weighted composite score:
  - **40% WPM Score**: Based on 0-80 WPM range (80+ WPM = 100 score)
  - **40% Accuracy Score**: Direct percentage (0-100)
  - **20% Characters Typed Score**: Scaled based on volume (800+ chars = 100 score)
  - **Composite Score** determines stars:
    - 5★ Expert: 85-100
    - 4★ Advanced: 70-84
    - 3★ Intermediate: 55-69
    - 2★ Beginner: 40-54
    - 1★ Needs Practice: 20-39
    - 0★ Try Again: 0-19

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
