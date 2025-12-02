@echo off
title Type Jam: Master the Keys

REM Check if Java is installed
where java >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Java is not installed or not in PATH
    echo Please install Java 17 or higher from: https://adoptium.net/
    echo.
    pause
    exit /b 1
)

REM Run the application
echo Starting Type Jam: Master the Keys...
echo.
java -jar Type-Jam-1.0-SNAPSHOT.jar

REM If the application exits with an error
if %ERRORLEVEL% NEQ 0 (
    echo.
    echo ERROR: Application failed to start
    echo.
    pause
)

