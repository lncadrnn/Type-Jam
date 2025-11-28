# Typing Texts Configuration Guide

## Overview
Type-Jam now loads typing texts from a JSON file, making it easy to add, edit, or remove practice sentences and paragraphs without modifying the Java code.

## Location
The typing texts are stored in:
```
src/main/resources/data/typing-texts.json
```

## File Format
The JSON file contains three difficulty levels: `easy`, `medium`, and `hard`. Each level is an array of text strings.

```json
{
  "easy": [
    "Text 1",
    "Text 2",
    "Text 3"
  ],
  "medium": [
    "Text 1",
    "Text 2"
  ],
  "hard": [
    "Text 1",
    "Text 2"
  ]
}
```

## How to Add New Texts

### Step 1: Open the JSON file
Navigate to `src/main/resources/data/typing-texts.json` and open it in any text editor.

### Step 2: Add your text
Add a new text string to the appropriate difficulty level array. Make sure to:
- Put the text in double quotes `"`
- Add a comma `,` after the previous text (except for the last one)
- Keep proper JSON formatting

**Example:**
```json
{
  "easy": [
    "The quick brown fox jumps.",
    "Hello world, this is new!",
    "My new custom sentence here"  ← New text added
  ],
  ...
}
```

### Step 3: Save the file
Save the JSON file. The changes will be loaded the next time you run the game.

## Tips for Creating Good Typing Texts

### Easy Level
- Short sentences (3-15 words)
- Simple, common words
- Minimal punctuation
- No special characters
- Good for beginners

**Examples:**
- "The cat sat on the mat."
- "Hello world from Type-Jam."
- "Practice typing every day."

### Medium Level
- Medium sentences (15-30 words)
- Mix of common and less common words
- Some punctuation (periods, commas, apostrophes)
- Occasional capitalization
- Good for intermediate learners

**Examples:**
- "In the midst of chaos, there is also opportunity."
- "Success is not final, failure is not fatal: it is the courage to continue that counts."

### Hard Level
- Long sentences or paragraphs (30+ words)
- Complex vocabulary
- Frequent punctuation (periods, commas, semicolons, colons, quotes)
- Numbers and symbols
- Multiple sentences
- Good for advanced practice

**Examples:**
- "Programming is the art of telling another human what one wants the computer to do. It requires precision, logic, and creativity in equal measure."
- "The difference between theory and practice is that in theory there is no difference, but in practice there is. This paradox applies to many areas of life."

## JSON Formatting Rules

### Valid JSON ✅
```json
{
  "easy": [
    "First text",
    "Second text",
    "Third text"
  ]
}
```

### Common Mistakes ❌

**Missing comma:**
```json
{
  "easy": [
    "First text"      ← Missing comma
    "Second text"
  ]
}
```

**Extra comma at the end:**
```json
{
  "easy": [
    "First text",
    "Second text",    ← Remove this comma
  ]
}
```

**Unescaped quotes inside text:**
```json
{
  "easy": [
    "He said "hello""  ← Wrong! Use \"hello\" or 'hello'
  ]
}
```

**Correct way to include quotes:**
```json
{
  "easy": [
    "He said \"hello\" to me.",
    "It's a beautiful day."
  ]
}
```

## Testing Your Changes

1. Save the `typing-texts.json` file
2. Run the application: `.\mvnw.cmd javafx:run`
3. Start a game with the difficulty level you edited
4. Verify your new texts appear during gameplay

## Troubleshooting

### Problem: My texts don't appear
- Check that the JSON file is saved in the correct location
- Verify the JSON syntax is correct (use a JSON validator online)
- Check the console output for error messages

### Problem: Game crashes on startup
- JSON syntax error - check for missing commas, brackets, or quotes
- Use a JSON validator tool to find errors: https://jsonlint.com/

### Problem: Text appears as "Type this text."
- The fallback text is being used
- Check the difficulty level name matches exactly: "easy", "medium", or "hard" (lowercase)
- Ensure the array for that difficulty is not empty

## Advanced: Adding Custom Difficulty Levels

While the current implementation supports three levels (easy, medium, hard), you can add more by:

1. Adding a new key to the JSON file:
```json
{
  "easy": [...],
  "medium": [...],
  "hard": [...],
  "expert": [
    "Your expert level texts here..."
  ]
}
```

2. Then modify the game's difficulty selection screen to include the new level (requires code changes)

## Need Help?

If you encounter issues or need assistance:
1. Check the console output for error messages
2. Validate your JSON syntax
3. Ensure file encoding is UTF-8
4. Make sure quotes and special characters are properly escaped

Happy typing! 🎉

