# Building Type-Jam Application

This guide explains how to build Type-Jam into a distributable executable.

## Prerequisites

- **JDK 21 or higher** - Download from [Oracle](https://www.oracle.com/java/technologies/downloads/) or [OpenJDK](https://adoptium.net/)
- Maven is included via the Maven Wrapper (mvnw.cmd)

## Build Options

### Option 1: Windows Installer (Recommended)

This creates a native Windows installer (.exe) with everything bundled.

1. Open PowerShell in the project directory
2. Run the build script:
   ```powershell
   .\build-exe.ps1
   ```
3. Find your installer in the `installer-output` folder:
   - `Type-Jam-1.0.exe` - The Windows installer

**Features:**
- Native Windows installer
- Desktop shortcut creation
- Start menu entry
- Application icon embedded
- No Java required on target machine (bundled JRE)

### Option 2: Runnable JAR File

This creates a JAR file that requires Java to be installed on the target machine.

1. Open PowerShell in the project directory
2. Run the build script:
   ```powershell
   .\build-jar.ps1
   ```
3. Find your JAR in the `target` folder:
   - `Type-Jam-1.0-SNAPSHOT.jar`

**To run the JAR:**
```bash
java -jar Type-Jam-1.0-SNAPSHOT.jar
```

### Option 3: Manual Build

If you prefer to build manually:

```powershell
# Clean and build
.\mvnw.cmd clean package -DskipTests

# Create installer (requires JDK 14+)
jpackage --input target `
  --name "Type-Jam" `
  --main-jar Type-Jam-1.0-SNAPSHOT.jar `
  --main-class com.example.typejam.Launcher `
  --type exe `
  --dest installer-output `
  --icon src\main\resources\assets\Type-Jam-Logo.ico `
  --app-version 1.0 `
  --vendor "Type-Jam Development Team" `
  --description "Type Jam: Master the Keys - A typing practice game" `
  --win-menu `
  --win-shortcut
```

## Distributing Your Application

### Windows Installer (.exe)
- Simply copy the `Type-Jam-1.0.exe` file from the `installer-output` folder
- Transfer it to another computer
- Run the installer - it will install the application with all dependencies

### JAR File
- Copy the `Type-Jam-1.0-SNAPSHOT.jar` file from the `target` folder
- Ensure the target computer has Java 21 or higher installed
- Run with: `java -jar Type-Jam-1.0-SNAPSHOT.jar`

## Troubleshooting

### "jpackage not found"
- Make sure you have JDK 14 or higher (jpackage is included)
- Add the JDK bin folder to your PATH
- Or use Option 2 (JAR file) instead

### "Module not found" errors
- Ensure you're using JDK 21 or higher
- Clean the project: `.\mvnw.cmd clean`
- Rebuild: `.\mvnw.cmd package`

### Icon not showing
- Verify the icon file exists at: `src\main\resources\assets\Type-Jam-Logo.ico`
- The icon must be in .ico format
- If using a different icon, update the path in `build-exe.ps1`

## Project Structure

```
Type-Jam/
├── src/
│   └── main/
│       ├── java/          # Java source files
│       └── resources/     # Resources (FXML, CSS, images, sounds)
│           └── assets/
│               └── Type-Jam-Logo.ico  # Application icon
├── target/                # Build output
├── installer-output/      # Installer output (after build)
├── pom.xml               # Maven configuration
├── build-exe.ps1         # Windows installer build script
└── build-jar.ps1         # JAR build script
```

## Additional Information

- **Application Name:** Type Jam: Master the Keys
- **Version:** 1.0-SNAPSHOT
- **Main Class:** com.example.typejam.Launcher
- **Required Java Version:** 21+
- **JavaFX Version:** 24.0.1

