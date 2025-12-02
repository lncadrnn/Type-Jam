@echo off
setlocal EnableDelayedExpansion

echo ================================================
echo   Type-Jam Windows Installer Creator
echo ================================================
echo.

REM Check for Java
echo [1/4] Checking Java installation...
java -version >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Java not found!
    echo Please install JDK 14 or higher from: https://adoptium.net/
    pause
    exit /b 1
)

REM Display Java version
echo Java found. Version:
java -version 2>&1 | findstr "version"
echo.

REM Check for jpackage
echo [2/4] Checking for jpackage...
where jpackage >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo WARNING: jpackage not found in PATH
    echo.
    echo Attempting to locate jpackage...

    REM Try to find jpackage in Java installation
    for /f "tokens=*" %%i in ('where java 2^>nul') do (
        set JAVA_PATH=%%i
        for %%j in ("!JAVA_PATH!") do set JAVA_DIR=%%~dpj
        set JAVA_HOME=!JAVA_DIR:~0,-5!

        if exist "!JAVA_HOME!bin\jpackage.exe" (
            echo Found jpackage at: !JAVA_HOME!bin\jpackage.exe
            set PATH=!JAVA_HOME!bin;!PATH!
            goto :jpackage_found
        )
    )

    echo.
    echo ERROR: jpackage not found!
    echo.
    echo jpackage is required to create a Windows installer.
    echo It is included in JDK 14 and higher.
    echo.
    echo Please ensure you have:
    echo 1. JDK 14 or higher installed (not just JRE)
    echo 2. The JDK bin directory is in your PATH
    echo.
    echo Download JDK from: https://adoptium.net/
    echo.
    echo ALTERNATIVE: You can use the portable package in Type-Jam-Portable\
    echo Just ZIP that folder and share it with users.
    echo.
    pause
    exit /b 1
)

:jpackage_found
echo jpackage found!
jpackage --version
echo.

REM Check for JAR file
echo [3/4] Checking for JAR file...
if not exist "target\Type-Jam-1.0-SNAPSHOT.jar" (
    echo ERROR: JAR file not found!
    echo Please run: .\build-simple.ps1
    pause
    exit /b 1
)
echo JAR file found: target\Type-Jam-1.0-SNAPSHOT.jar
echo.

REM Create installer
echo [4/4] Creating Windows installer...
echo This may take a few minutes...
echo.

REM Create output directory
if exist "installer-output" rmdir /s /q "installer-output"
mkdir "installer-output"

REM Check for icon
set ICON_PARAM=
if exist "src\main\resources\assets\Type-Jam-Logo.ico" (
    echo Using icon: src\main\resources\assets\Type-Jam-Logo.ico
    set ICON_PARAM=--icon "src\main\resources\assets\Type-Jam-Logo.ico"
) else (
    echo WARNING: Icon file not found, creating installer without custom icon
)

echo.
echo Running jpackage...
echo.

jpackage ^
  --input target ^
  --name "Type-Jam" ^
  --main-jar Type-Jam-1.0-SNAPSHOT.jar ^
  --main-class com.example.typejam.Launcher ^
  --type exe ^
  --dest installer-output ^
  %ICON_PARAM% ^
  --app-version 1.0 ^
  --vendor "Type-Jam Development Team" ^
  --description "Type Jam: Master the Keys - A typing practice game" ^
  --win-menu ^
  --win-shortcut

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ================================================
    echo   SUCCESS! Installer Created!
    echo ================================================
    echo.

    REM Find the installer file
    for %%f in (installer-output\*.exe) do (
        echo Installer: %%f
        set INSTALLER_SIZE=%%~zf
        set /a INSTALLER_SIZE_MB=!INSTALLER_SIZE! / 1048576
        echo Size: !INSTALLER_SIZE_MB! MB
    )

    echo.
    echo Your Windows installer includes:
    echo  - The Type-Jam application
    echo  - Java Runtime Environment (bundled)
    echo  - Desktop shortcut
    echo  - Start Menu entry
    echo.
    echo Users can install without having Java pre-installed!
    echo.
) else (
    echo.
    echo ERROR: jpackage failed!
    echo.
    echo Check the error messages above.
    echo.
    echo ALTERNATIVE: Use the portable package in Type-Jam-Portable\
    echo ZIP that folder and share it with users (requires Java on their PC).
    echo.
)

pause

