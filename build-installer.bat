@echo off
echo ================================================
echo   Type-Jam Windows Installer Builder
echo ================================================
echo.

echo Checking for installer directory...
if not exist "installer-output" mkdir installer-output

echo.
echo Building Windows installer with jpackage...
echo.

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
  --win-console ^
  --win-menu ^
  --win-shortcut

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ================================================
    echo   Build Complete!
    echo ================================================
    echo.
    echo Your installer is ready at: installer-output\Type-Jam-1.0.exe
    echo.
) else (
    echo.
    echo ERROR: jpackage failed!
    echo.
    echo NOTE: If jpackage is not found, you may need to:
    echo 1. Ensure you have JDK 14 or higher installed
    echo 2. Add the JDK bin folder to your PATH
    echo.
    echo You can still run the JAR directly with:
    echo   java -jar target\Type-Jam-1.0-SNAPSHOT.jar
    echo.
)

pause

