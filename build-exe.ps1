# Type-Jam Build Script
# This script builds the application and creates a Windows installer

Write-Host "================================================" -ForegroundColor Cyan
Write-Host "  Type-Jam Application Builder" -ForegroundColor Cyan
Write-Host "================================================" -ForegroundColor Cyan
Write-Host ""

# Check if Java is installed
Write-Host "Checking Java installation..." -ForegroundColor Yellow
try {
    $javaVersion = java -version 2>&1 | Select-String "version"
    Write-Host "✓ Java found: $javaVersion" -ForegroundColor Green
} catch {
    Write-Host "✗ Java not found! Please install JDK 21 or higher." -ForegroundColor Red
    exit 1
}

# Check if Maven is available
Write-Host "Checking Maven installation..." -ForegroundColor Yellow
$mavenCmd = ".\mvnw.cmd"
if (Test-Path $mavenCmd) {
    Write-Host "✓ Maven wrapper found" -ForegroundColor Green
} else {
    Write-Host "✗ Maven wrapper not found!" -ForegroundColor Red
    exit 1
}

# Clean previous builds
Write-Host ""
Write-Host "Cleaning previous builds..." -ForegroundColor Yellow
& $mavenCmd clean
if ($LASTEXITCODE -ne 0) {
    Write-Host "✗ Clean failed!" -ForegroundColor Red
    exit 1
}
Write-Host "✓ Clean successful" -ForegroundColor Green

# Build the project
Write-Host ""
Write-Host "Building project..." -ForegroundColor Yellow
& $mavenCmd package -DskipTests
if ($LASTEXITCODE -ne 0) {
    Write-Host "✗ Build failed!" -ForegroundColor Red
    exit 1
}
Write-Host "✓ Build successful" -ForegroundColor Green

# Check if JAR was created
$jarFile = "target\Type-Jam-1.0-SNAPSHOT.jar"
if (-not (Test-Path $jarFile)) {
    Write-Host "✗ JAR file not found at $jarFile" -ForegroundColor Red
    exit 1
}
Write-Host "✓ JAR file created: $jarFile" -ForegroundColor Green

# Create output directory for installer
$outputDir = "installer-output"
if (Test-Path $outputDir) {
    Remove-Item -Recurse -Force $outputDir
}
New-Item -ItemType Directory -Path $outputDir | Out-Null

# Check for icon file
$iconFile = "src\main\resources\assets\Type-Jam-Logo.ico"
if (-not (Test-Path $iconFile)) {
    Write-Host "⚠ Warning: Icon file not found at $iconFile" -ForegroundColor Yellow
    $iconFile = $null
} else {
    Write-Host "✓ Icon file found: $iconFile" -ForegroundColor Green
}

# Create Windows installer using jpackage
Write-Host ""
Write-Host "Creating Windows installer..." -ForegroundColor Yellow

$jpackageArgs = @(
    "--input", "target",
    "--name", "Type-Jam",
    "--main-jar", "Type-Jam-1.0-SNAPSHOT.jar",
    "--main-class", "com.example.typejam.Launcher",
    "--type", "exe",
    "--dest", $outputDir,
    "--app-version", "1.0",
    "--vendor", "Type-Jam Development Team",
    "--description", "Type Jam: Master the Keys - A typing practice game",
    "--win-console",
    "--win-menu",
    "--win-shortcut"
)

if ($iconFile) {
    $jpackageArgs += "--icon"
    $jpackageArgs += $iconFile
}

try {
    & jpackage @jpackageArgs
    if ($LASTEXITCODE -ne 0) {
        Write-Host "✗ jpackage failed!" -ForegroundColor Red
        exit 1
    }
} catch {
    Write-Host "✗ jpackage error: $_" -ForegroundColor Red
    Write-Host ""
    Write-Host "NOTE: jpackage requires JDK 14 or higher." -ForegroundColor Yellow
    Write-Host "If you don't have it, the JAR file can still be run with: java -jar $jarFile" -ForegroundColor Yellow
    exit 1
}

Write-Host ""
Write-Host "================================================" -ForegroundColor Cyan
Write-Host "  Build Complete!" -ForegroundColor Green
Write-Host "================================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "Your installer is ready at: $outputDir\Type-Jam-1.0.exe" -ForegroundColor Green
Write-Host ""
Write-Host "You can also run the JAR directly with:" -ForegroundColor Cyan
Write-Host "  java -jar $jarFile" -ForegroundColor White
Write-Host ""

