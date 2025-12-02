# Installation & Build Guide 🔧

This guide provides detailed instructions for installing and running Type Jam, both for end users and developers.

---

## 📦 For End Users (Windows)

### Option 1: Pre-built Executable (Recommended)

The easiest way to run Type Jam is using the pre-built Windows executable.

#### Requirements
- Windows 10 or higher
- No additional software required

#### Installation Steps

1. **Download** the distribution package:
   - Locate `Type-Jam-Windows-Setup.zip` (58 MB)

2. **Extract** the ZIP file:
   ```
   Right-click → Extract All → Choose destination
   ```

3. **Run** the application:
   - Navigate to the extracted `Type-Jam` folder
   - Double-click `Type-Jam.exe`
   - ✅ The application will launch immediately!

#### Troubleshooting
- If Windows Defender flags the executable, click "More info" → "Run anyway"
- Ensure the entire folder structure is extracted together (don't move just the .exe file)

---

## 🛠️ For Developers

### Prerequisites

Before building Type Jam from source, ensure you have:

1. **Java Development Kit (JDK) 17 or higher**
   - Download from: [Oracle JDK](https://www.oracle.com/java/technologies/downloads/) or [OpenJDK](https://adoptium.net/)
   - Verify installation:
     ```bash
     java -version
     ```

2. **Apache Maven 3.6+**
   - Download from: [Maven Official Site](https://maven.apache.org/download.cgi)
   - Or use the included Maven Wrapper (`mvnw`)
   - Verify installation:
     ```bash
     mvn -version
     ```

3. **IDE (Optional but Recommended)**
   - IntelliJ IDEA (Ultimate or Community)
   - Eclipse with JavaFX plugin
   - VS Code with Java extensions

### Setup Instructions

#### 1. Clone or Download the Project

```bash
# If using Git
git clone <repository-url>
cd Type-Jam

# Or download and extract the ZIP file
```

#### 2. Verify Project Structure

Ensure the following structure exists:
```
Type-Jam/
├── src/
│   └── main/
│       ├── java/com/example/typejam/
│       └── resources/
├── pom.xml
├── mvnw (Maven Wrapper for Unix)
└── mvnw.cmd (Maven Wrapper for Windows)
```

#### 3. Install Dependencies

Maven will automatically download all required dependencies:

```bash
# Windows
mvnw.cmd clean install

# macOS/Linux
./mvnw clean install
```

This downloads:
- JavaFX 21.0.2
- Jackson (JSON processing)
- Other required libraries

### Running the Application

#### Option 1: Using Maven (Recommended)

```bash
# Windows
mvnw.cmd javafx:run

# macOS/Linux
./mvnw javafx:run
```

#### Option 2: Using IDE

**IntelliJ IDEA:**
1. Open the project folder in IntelliJ
2. Wait for Maven to import dependencies
3. Locate `Launcher.java` or `TypeJam.java` in `src/main/java/com/example/typejam/`
4. Right-click → Run

**Eclipse:**
1. File → Import → Maven → Existing Maven Projects
2. Select the Type-Jam folder
3. Wait for dependencies to download
4. Right-click the project → Run As → Java Application
5. Select `Launcher` as the main class

#### Option 3: Command Line with Compiled Classes

```bash
# Compile first
mvnw.cmd clean package

# Run the generated JAR
java -jar target/Type-Jam-1.0.jar
```

### Building for Distribution

#### Create JAR with Dependencies

```bash
# Windows
mvnw.cmd clean package

# macOS/Linux
./mvnw clean package
```

The output JAR will be in: `target/Type-Jam-1.0.jar`

#### Creating Native Executable (Advanced)

To create a native Windows executable like `Type-Jam.exe`, you can use tools like:

1. **jpackage** (included with JDK 17+):
   ```bash
   jpackage --input target/ \
            --name Type-Jam \
            --main-jar Type-Jam-1.0.jar \
            --main-class com.example.typejam.Launcher \
            --type exe \
            --icon src/main/resources/assets/Type-Jam-Logo.ico
   ```

2. **Launch4j** - Wraps JAR into Windows .exe
   - Download from: http://launch4j.sourceforge.net/
   - Configure with your JAR and icon
   - Generate .exe file

3. **jlink** - Create custom JRE with your application
   ```bash
   jlink --module-path "path/to/javafx-jmods:target/Type-Jam-1.0.jar" \
         --add-modules javafx.controls,javafx.fxml \
         --output Type-Jam-Runtime
   ```

---

## 🔧 Configuration

### Resource Files

The application uses several JSON configuration files:

- **Settings**: `src/main/resources/data/typejam-settings.json`
  - Sound volume preferences
  - First launch flag

- **Leaderboard**: Generated at runtime in `data/typejam-leaderboard.json`
  - Stores player scores locally

- **Typing Texts**: `src/main/resources/data/typing-texts.json`
  - Contains practice passages for each difficulty level

### Customizing Content

To add or modify typing texts:
1. Open `src/main/resources/data/typing-texts.json`
2. Edit the arrays under `easy`, `medium`, or `hard`
3. Rebuild and run

---

## 🐛 Troubleshooting

### Common Issues

#### "JavaFX runtime components are missing"
- **Solution**: Use Maven to run (`mvnw javafx:run`) instead of direct `java` command
- Or add JavaFX to module path manually

#### "Module not found: javafx.controls"
- **Solution**: Ensure `module-info.java` is present and declares required modules
- Check that JavaFX dependencies are in `pom.xml`

#### Maven build fails
- **Solution**: Check internet connection (Maven downloads dependencies)
- Try clearing Maven cache:
  ```bash
  mvnw.cmd clean -U
  ```

#### Application won't start after building
- **Solution**: Ensure all resources are copied to `target/classes/`
- Verify `pom.xml` includes the `maven-resources-plugin`

#### Missing assets or sounds
- **Solution**: Check that `src/main/resources/` folder structure is intact
- Rebuild project: `mvnw clean package`

#### OutOfMemoryError
- **Solution**: Increase Java heap size:
  ```bash
  set MAVEN_OPTS=-Xmx1024m
  mvnw javafx:run
  ```

---

## 📚 Additional Resources

### JavaFX Documentation
- [Official JavaFX Docs](https://openjfx.io/)
- [JavaFX Tutorial](https://docs.oracle.com/javafx/2/)

### Maven Resources
- [Maven Getting Started](https://maven.apache.org/guides/getting-started/)
- [Maven POM Reference](https://maven.apache.org/pom.html)

### Development Tools
- [IntelliJ IDEA Download](https://www.jetbrains.com/idea/download/)
- [Scene Builder](https://gluonhq.com/products/scene-builder/) - Visual FXML editor

---

## 🔄 Version History

### Version 1.0
- Initial release
- Challenge Mode and Practice Mode
- Star-based rating system
- Leaderboard functionality
- Sound effects and background music
- Three difficulty levels

---

## 📞 Support

If you encounter issues not covered in this guide:

1. Check existing GitHub Issues
2. Review the main [README.md](README.md)
3. Contact the development team

---

<div align="center">
  <strong>Happy Coding! 💻</strong>
  <br>
  For questions about the application itself, see <a href="README.md">README.md</a>
</div>

