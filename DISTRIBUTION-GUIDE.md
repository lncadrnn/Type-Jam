# Type-Jam Distribution Package

## ✅ What You Have Now

Your application has been successfully built! You have a **runnable JAR file** at:
```
target\Type-Jam-1.0-SNAPSHOT.jar
```

This JAR file contains:
- ✓ Your complete application
- ✓ All JavaFX libraries
- ✓ All resources (images, sounds, fonts, data files)
- ✓ All dependencies

## 📦 How to Distribute Your Application

### Option 1: JAR File + Launcher (Recommended - Easiest)

This is the simplest way to share your application!

**What to distribute:**
1. Copy these files from the `target` folder:
   - `Type-Jam-1.0-SNAPSHOT.jar`
   - `run-Type-Jam.bat`

2. Put them in a folder and ZIP it up

3. Send the ZIP to anyone

**How others will run it:**
1. Extract the ZIP
2. Double-click `run-Type-Jam.bat`
3. That's it!

**Requirements:** The user needs Java 17 or higher installed
- Download from: https://adoptium.net/

---

### Option 2: Windows Installer (Advanced)

To create a native Windows installer that includes Java (no Java installation required):

**Requirements:**
- JDK 14 or higher with jpackage support

**Steps:**

1. Open Command Prompt or PowerShell in the project directory

2. Run this command:
```batch
jpackage --input target ^
  --name "Type-Jam" ^
  --main-jar Type-Jam-1.0-SNAPSHOT.jar ^
  --main-class com.example.typejam.Launcher ^
  --type exe ^
  --dest installer-output ^
  --icon "src\main\resources\assets\Type-Jam-Logo.ico" ^
  --app-version 1.0 ^
  --vendor "Type-Jam Development Team" ^
  --description "Type Jam: Master the Keys - A typing practice game" ^
  --win-menu ^
  --win-shortcut
```

3. Find your installer at: `installer-output\Type-Jam-1.0.exe`

**Note:** If jpackage is not found:
- Verify you have JDK 14+ (not just JRE)
- Add JDK's bin folder to your PATH environment variable
- Example path: `C:\Program Files\Java\jdk-25\bin`

---

### Option 3: Portable Package

Create a portable package that users can run without installation:

1. Create a folder named `Type-Jam-Portable`

2. Copy these files into it:
   - `target\Type-Jam-1.0-SNAPSHOT.jar`
   - `target\run-Type-Jam.bat`
   - `README.md` (with instructions)

3. ZIP the folder

4. Users can extract and run from anywhere!

---

## 🎯 Quick Distribution Checklist

### For Users WITH Java Installed:
- [ ] Copy `Type-Jam-1.0-SNAPSHOT.jar`
- [ ] Copy `run-Type-Jam.bat` (optional, for convenience)
- [ ] ZIP and share!

### For Users WITHOUT Java:
- [ ] Use Option 2 to create a Windows installer
- [ ] The installer will bundle a JRE
- [ ] Users won't need to install Java separately

---

## 🚀 Testing Your Distribution

Before sharing, test your distribution package:

1. Copy the JAR to a different folder
2. Double-click the BAT file
3. Verify the application runs correctly
4. Test on a different computer if possible

---

## 📋 System Requirements

**Minimum Requirements:**
- Windows 10 or higher
- 4 GB RAM
- 100 MB disk space

**If using JAR file:**
- Java 17 or higher required
- Download from: https://adoptium.net/

**If using installer:**
- No additional requirements
- Java is bundled with the installer

---

## 🐛 Troubleshooting

### "Module not found" error
- Ensure Java 17 or higher is installed
- Try: `java -version` to check

### Application won't start
- Run from Command Prompt to see error messages:
  ```
  java -jar Type-Jam-1.0-SNAPSHOT.jar
  ```

### Icon not showing
- The icon is embedded in the JAR for the window title bar
- For desktop shortcuts, use the installer (Option 2)

---

## 📁 File Sizes

- **JAR file**: ~12-15 MB
- **With installer**: ~50-80 MB (includes Java Runtime)

---

## ✨ Next Steps

1. Test your JAR file on your computer
2. Share with friends/testers
3. Get feedback
4. Create installer for wider distribution

**Your application is ready to share!** 🎉

