# Type-Jam

Type-Jam is a simple JavaFX typing practice game and learning tool designed to help users improve their typing speed and accuracy.

## Purpose
- This project was created for Bacoor National High School — especially for senior high school students — to help them develop faster and more accurate typing skills through short practice sessions and fun challenges.

## What you see in this project
- A JavaFX application with multiple screens (main menu, typing game, time challenges, settings, leaderboards).
- FXML layouts and controllers for UI flow.
- Assets such as images, fonts, and CSS styles under `src/main/resources`.

## Game modes
Type-Jam currently supports two main modes designed to help students practice different aspects of typing:

- Endless Mode
  - In Endless Mode you are presented with text passages that you must type until the passage is finished. There is no ticking clock — the goal is accuracy and completion. This mode is good for learning new key patterns and improving accuracy without time pressure.

- Time Challenge
  - Time Challenge gives you a fixed time limit to type as much as you can. Typical time options are 1 minute, 3 minutes, and 5 minutes. This mode trains speed and consistency under pressure.

## Difficulty levels
To make the practice useful at different skill levels, Type-Jam provides three difficulty levels:

- Easy
  - Shorter passages, simple common words, minimal punctuation.
  - More lenient error tolerance and lower WPM expectations.
  - Good for beginners and students just learning finger placement and accuracy.

- Medium
  - Moderate-length passages with a mix of common and less-common words, occasional punctuation.
  - Balanced error tolerance and standard WPM expectations.
  - Suited for students who have basic proficiency and want to improve speed.

- Hard
  - Longer passages, complex words, frequent punctuation and capitalization, occasional numbers/symbols.
  - Stricter error tolerance and higher WPM expectations.
  - Best for advanced practice and accuracy under challenging text conditions.

How difficulty affects gameplay and scoring
- Passage selection: difficulty determines the source text complexity (word choice, punctuation, length).
- Error tolerance: higher difficulties apply stricter checks and may penalize errors more heavily when computing accuracy.
- Scoring normalization: WPM and accuracy expectations are normalized per difficulty so star thresholds can be meaningful across levels (for example, 50 WPM on Easy may map to higher normalized value than 50 WPM on Hard).

## Scoring and stars
- Results are summarized with a star rating from 1 to 5, where 5 stars is the best and 1 star is the lowest.
- The star rating is calculated from a combination of metrics:
  - Words per minute (WPM) — how fast you typed.
  - Accuracy — percentage of correctly typed characters.
  - Remaining time / completion (for time challenge or endless completion) — helps reward finishing a passage or using time efficiently.
- Leaderboards display players ranked by their highest star ratings. When multiple players have the same star rating, the leaderboard can break ties using WPM and accuracy.

Example scoring logic (conceptual)
- A simple weighted score might look like: score = 0.6 * normalized(WPM) + 0.3 * accuracy + 0.1 * timeFactor
- The normalized score is converted into stars:
  - 5 stars: top tier (excellent WPM and accuracy)
  - 4 stars: very good
  - 3 stars: average
  - 2 stars: below average
  - 1 star: needs improvement

Note: The exact weighting and thresholds are implementation details and can be adjusted to better reflect classroom goals and difficulty levels.

## Leaderboards
- Leaderboards show entries sorted by stars (highest to lowest), then by finer metrics like WPM and accuracy to break ties.
- This helps students quickly see top performers and encourages improvement across multiple metrics (speed and accuracy).

## Key features
- Practice typing with themed screens and animated assets.
- Time challenge mode to build speed under pressure.
- Track and view high scores via a simple leaderboard screen.
- Configurable settings, difficulty selection (Easy/Medium/Hard), and difficulty-aware scoring.

## Quick start (Windows PowerShell)
Use the included Maven wrapper to build and run the application.

```powershell
# Build the project
.\mvnw.cmd clean package

# Run the JavaFX application
.\mvnw.cmd javafx:run
```

If you use a system-wide Maven installation, replace `./mvnw.cmd` with `mvn`.

## Notes
- This repository expects JavaFX to be available via the project's Maven configuration (pom.xml). The Maven wrapper (`mvnw.cmd`) included in the repository will bootstrap the correct build environment.
- If you prefer a packaged JAR (the artifact name and path may vary), you can run it with:

```powershell
# Example (artifact name may differ)
java -jar target\Type-Jam-1.0.jar
```

## Project structure (high level)
- `src/main/java` — Java source files and controllers
- `src/main/resources` — FXML views, images, fonts, and CSS
- `pom.xml` — Maven build configuration

## Contributing
- Feel free to improve UI, add more practice modes, or enhance the leaderboard persistence.
- Keep changes focused on helping senior high students practice typing efficiently.

## Acknowledgements
- Created for Bacoor National High School (senior high students) to support improvement of typing skills and digital literacy.

## License
- Use or adapt this project for educational purposes. Add a specific license file if you want to set formal terms.

## Contact
- For questions about the project structure or to suggest features, open an issue or contact the repository owner.
