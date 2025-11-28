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

## Calculations

Type-Jam uses specific formulas to measure your typing performance. Here are the detailed calculations:

### Words Per Minute (WPM)
WPM measures your typing speed. The formula is:

```
WPM = (Total Characters Typed / 5) / Time in Minutes
```

- **Total Characters Typed**: The number of characters you successfully typed (visible in the text field)
- **5**: Standard divisor (represents average word length in English)
- **Time in Minutes**: The time you spent typing, converted to minutes

**Example:**
- If you type 250 characters in 2 minutes:
  - WPM = (250 / 5) / 2 = 50 / 2 = **25 WPM**

**Note:** WPM is rounded to the nearest whole number. For example, 46.66 WPM displays as **47 WPM**.

### Accuracy
Accuracy measures your typing precision, accounting for all incorrect keystrokes (even if later corrected).

```
Accuracy = (Total Characters Typed / (Total Characters Typed + Total Incorrect Keystrokes)) × 100
```

- **Total Characters Typed**: Final number of characters in your completed text
- **Total Incorrect Keystrokes**: Cumulative count of every incorrect character typed (red characters)
- Even if you backspace and correct an error, the initial mistake still counts against accuracy

**Example:**
- You type "hello" but first type "heklo", then backspace and fix it:
  - Total Characters Typed: 5 (h-e-l-l-o)
  - Incorrect Keystrokes: 1 (the "k" was wrong)
  - Accuracy = (5 / (5 + 1)) × 100 = (5 / 6) × 100 = **83.33%**

### Time Consumed

Time consumed is calculated differently based on the game mode:

#### Time Challenge Mode
Shows the actual time you spent typing:
```
Time Consumed = Time Spent (in seconds)
```

**Example:**
- Time Limit: 1 minute (60 seconds)
- You finish in 50 seconds
- Time Consumed = **50 seconds**

#### Endless Mode
Shows how long it took you to complete the passage:
```
Time Consumed = Elapsed Time (in seconds)
```

**Example:**
- You complete an Easy passage in 12 seconds
- Time Consumed = **12 seconds**

### Characters Typed
Simply counts the total number of characters you typed in the text field:
```
Characters Typed = Length of Typed Text
```

This represents the final character count, not including deleted/backspaced characters.

## Scoring and stars
Results are summarized with a star rating from 0 to 5 stars. The star rating is based on clear thresholds for **both WPM and Accuracy** - you must meet both criteria to achieve each star level.

### Star Rating Thresholds

| Stars | Level | WPM Required | Accuracy Required |
|-------|-------|--------------|-------------------|
| ⭐⭐⭐⭐⭐ | **5 Stars - Expert** | 60+ WPM | 95%+ |
| ⭐⭐⭐⭐ | **4 Stars - Advanced** | 45+ WPM | 90%+ |
| ⭐⭐⭐ | **3 Stars - Intermediate** | 30+ WPM | 80%+ |
| ⭐⭐ | **2 Stars - Beginner** | 15+ WPM | 70%+ |
| ⭐ | **1 Star - Needs Practice** | 5+ WPM | 50%+ |
| ☆ | **0 Stars - Try Again** | Below thresholds | Below thresholds |

### How Star Ratings Work

Both WPM **AND** Accuracy thresholds must be met to earn each star level. For example:
- If you type at 65 WPM but only have 85% accuracy, you get **3 stars** (not 5)
- If you have 98% accuracy but only 40 WPM, you get **4 stars** (not 5)
- To get 5 stars, you need **both** 60+ WPM **and** 95%+ accuracy

This system encourages balanced improvement in both speed and precision.

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
