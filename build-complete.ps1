# Type-Jam Complete Build and Package Script
Write-Host "================================================" -ForegroundColor Cyan
Write-Host "  Type-Jam Complete Build System" -ForegroundColor Cyan
Write-Host "================================================" -ForegroundColor Cyan
Write-Host ""

# Step 1: Verify Java
Write-Host "[1/4] Checking Java installation..." -ForegroundColor Yellow
$javaVersionOutput = java -version 2>&1
if ($javaVersionOutput) {
    Write-Host "  Success: Java is installed" -ForegroundColor Green
} else {
    Write-Host "  Error: Java not found!" -ForegroundColor Red
    exit 1
}

# Step 2: Build the project
Write-Host ""
Write-Host "[2/5] Building project with Maven..." -ForegroundColor Yellow
& ".\mvnw.cmd" clean package -DskipTests

if ($LASTEXITCODE -ne 0) {
    Write-Host "  ✗ Build failed!" -ForegroundColor Red
    exit 1
}

$jarFile = "target\Type-Jam-1.0-SNAPSHOT.jar"
if (Test-Path $jarFile) {
    $jarSize = (Get-Item $jarFile).Length / 1MB
    Write-Host "  ✓ JAR file created: $jarFile ($([Math]::Round($jarSize, 2)) MB)" -ForegroundColor Green
} else {
    Write-Host "  ✗ JAR file not found!" -ForegroundColor Red
    exit 1
}

# Step 3: Test the JAR
Write-Host ""
Write-Host "[3/5] Verifying JAR contents..." -ForegroundColor Yellow
try {
    $jarContents = jar -tf $jarFile | Select-String "module-info.class"
    if ($jarContents) {
        Write-Host "  ✓ JAR is a valid modular application" -ForegroundColor Green
    }
} catch {
    Write-Host "  ⚠ Could not verify JAR contents" -ForegroundColor Yellow
}

# Step 4: Check for jpackage
Write-Host ""
Write-Host "[4/5] Checking for jpackage..." -ForegroundColor Yellow
$jpackageAvailable = $false
try {
    $jpackagePath = Get-Command jpackage -ErrorAction Stop
    Write-Host "  ✓ jpackage found at: $($jpackagePath.Source)" -ForegroundColor Green
    $jpackageAvailable = $true
} catch {
    Write-Host "  ⚠ jpackage not found in PATH" -ForegroundColor Yellow
    Write-Host "  Attempting to locate jpackage..." -ForegroundColor Yellow

    # Try to find jpackage in common locations
    $javaHome = $env:JAVA_HOME
    if ($javaHome) {
        $jpackageExe = Join-Path $javaHome "bin\jpackage.exe"
        if (Test-Path $jpackageExe) {
            Write-Host "  ✓ jpackage found at: $jpackageExe" -ForegroundColor Green
            $jpackageAvailable = $true
            $env:PATH = "$javaHome\bin;$env:PATH"
        }
    }
}

# Step 5: Create installer or provide alternative
Write-Host ""
Write-Host "[5/5] Creating distribution package..." -ForegroundColor Yellow

if ($jpackageAvailable) {
    Write-Host "  Creating Windows installer with jpackage..." -ForegroundColor Cyan

    # Create output directory
    if (Test-Path "installer-output") {
        Remove-Item -Recurse -Force "installer-output"
    }
    New-Item -ItemType Directory -Path "installer-output" | Out-Null

    # Check for icon
    $iconFile = "src\main\resources\assets\Type-Jam-Logo.ico"
    $iconParam = @()
    if (Test-Path $iconFile) {
        Write-Host "  ✓ Using icon: $iconFile" -ForegroundColor Green
        $iconParam = @("--icon", $iconFile)
    } else {
        Write-Host "  ⚠ Icon file not found, proceeding without icon" -ForegroundColor Yellow
    }

    # Build jpackage command
    $jpackageArgs = @(
        "--input", "target",
        "--name", "Type-Jam",
        "--main-jar", "Type-Jam-1.0-SNAPSHOT.jar",
        "--main-class", "com.example.typejam.Launcher",
        "--type", "exe",
        "--dest", "installer-output",
        "--app-version", "1.0",
        "--vendor", "Type-Jam Development Team",
        "--description", "Type Jam: Master the Keys - A typing practice game",
        "--win-menu",
        "--win-shortcut"
    )

    if ($iconParam) {
        $jpackageArgs += $iconParam
    }

    try {
        & jpackage @jpackageArgs

        if ($LASTEXITCODE -eq 0) {
            $installerFile = Get-ChildItem "installer-output\*.exe" | Select-Object -First 1
            if ($installerFile) {
                $installerSize = $installerFile.Length / 1MB
                Write-Host ""
                Write-Host "  ✓ Windows installer created successfully!" -ForegroundColor Green
                Write-Host "  Location: $($installerFile.FullName)" -ForegroundColor Cyan
                Write-Host "  Size: $([Math]::Round($installerSize, 2)) MB" -ForegroundColor Cyan
            }
        } else {
            Write-Host "  ✗ jpackage failed with exit code: $LASTEXITCODE" -ForegroundColor Red
            $jpackageAvailable = $false
        }
    } catch {
        Write-Host "  ✗ jpackage error: $_" -ForegroundColor Red
        $jpackageAvailable = $false
    }
}

# Create portable package
Write-Host ""
Write-Host "Creating portable package..." -ForegroundColor Cyan
$portableDir = "Type-Jam-Portable"
if (Test-Path $portableDir) {
    Remove-Item -Recurse -Force $portableDir
}
New-Item -ItemType Directory -Path $portableDir | Out-Null

Copy-Item $jarFile -Destination $portableDir
Copy-Item "target\run-Type-Jam.bat" -Destination $portableDir -ErrorAction SilentlyContinue

# Create README for portable package
$readmeLines = @(
    "# Type Jam: Master the Keys - Portable Edition",
    "",
    "## How to Run",
    "",
    "1. Make sure you have Java 17 or higher installed",
    "   Download from: https://adoptium.net/",
    "   Check your version: java -version",
    "",
    "2. Double-click 'run-Type-Jam.bat' to start the game",
    "",
    "   OR",
    "",
    "   Run from command line: java -jar Type-Jam-1.0-SNAPSHOT.jar",
    "",
    "## System Requirements",
    "",
    "- Windows 10 or higher",
    "- Java 17 or higher",
    "- 4 GB RAM",
    "- 100 MB disk space",
    "",
    "## Troubleshooting",
    "",
    "If the game doesn't start:",
    "1. Open Command Prompt in this folder",
    "2. Type: java -jar Type-Jam-1.0-SNAPSHOT.jar",
    "3. Look for any error messages",
    "",
    "Enjoy typing!"
)

$readmeLines | Out-File -FilePath "$portableDir\README.txt" -Encoding UTF8
Write-Host "  ✓ Portable package created: $portableDir\" -ForegroundColor Green

# Final summary
Write-Host ""
Write-Host "================================================" -ForegroundColor Cyan
Write-Host "  Build Complete!" -ForegroundColor Green
Write-Host "================================================" -ForegroundColor Cyan
Write-Host ""

Write-Host "📦 Distribution Options:" -ForegroundColor Cyan
Write-Host ""

Write-Host "1. Runnable JAR File:" -ForegroundColor Yellow
Write-Host "   Location: $jarFile" -ForegroundColor White
Write-Host "   Run with: java -jar $jarFile" -ForegroundColor Gray
Write-Host ""

Write-Host "2. Portable Package:" -ForegroundColor Yellow
Write-Host "   Location: $portableDir\" -ForegroundColor White
Write-Host "   Contents: JAR + Launcher + README" -ForegroundColor Gray
Write-Host "   ➜ ZIP this folder to share with others!" -ForegroundColor Green
Write-Host ""

if ($jpackageAvailable -and (Test-Path "installer-output\*.exe")) {
    Write-Host "3. Windows Installer:" -ForegroundColor Yellow
    $installerFile = Get-ChildItem "installer-output\*.exe" | Select-Object -First 1
    Write-Host "   Location: $($installerFile.FullName)" -ForegroundColor White
    Write-Host "   ➜ This is a complete installer (no Java required)!" -ForegroundColor Green
    Write-Host ""
} else {
    Write-Host "3. Windows Installer:" -ForegroundColor Yellow
    Write-Host "   ✗ Not created (jpackage not available)" -ForegroundColor Red
    Write-Host "   To create an installer:" -ForegroundColor Gray
    Write-Host "   - Install JDK 14+ with jpackage support" -ForegroundColor Gray
    Write-Host "   - Add JDK bin folder to PATH" -ForegroundColor Gray
    Write-Host "   - Run this script again" -ForegroundColor Gray
    Write-Host ""
}

Write-Host "📖 For detailed instructions, see:" -ForegroundColor Cyan
Write-Host "   - BUILD-GUIDE.md" -ForegroundColor White
Write-Host "   - DISTRIBUTION-GUIDE.md" -ForegroundColor White
Write-Host ""

Write-Host "✨ Your application is ready to distribute! ✨" -ForegroundColor Green
Write-Host ""

