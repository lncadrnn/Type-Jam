# Type-Jam Build Script - Simple Version
Write-Host "================================================" -ForegroundColor Cyan
Write-Host "  Type-Jam Build System" -ForegroundColor Cyan
Write-Host "================================================" -ForegroundColor Cyan
Write-Host ""

# Build with Maven
Write-Host "Building project..." -ForegroundColor Yellow
& ".\mvnw.cmd" clean package -DskipTests

if ($LASTEXITCODE -ne 0) {
    Write-Host "Build failed!" -ForegroundColor Red
    exit 1
}

Write-Host ""
Write-Host "Build successful!" -ForegroundColor Green
Write-Host ""

# Create portable package
Write-Host "Creating portable package..." -ForegroundColor Yellow
$portableDir = "Type-Jam-Portable"
if (Test-Path $portableDir) {
    Remove-Item -Recurse -Force $portableDir
}
New-Item -ItemType Directory -Path $portableDir | Out-Null

# Copy files
Copy-Item "target\Type-Jam-1.0-SNAPSHOT.jar" -Destination $portableDir
Copy-Item "target\run-Type-Jam.bat" -Destination $portableDir -ErrorAction SilentlyContinue

# Create README
$readme = @"
Type Jam: Master the Keys - Portable Edition

HOW TO RUN:
1. Install Java 17 or higher from https://adoptium.net/
2. Double-click 'run-Type-Jam.bat' to start

OR run from command line:
  java -jar Type-Jam-1.0-SNAPSHOT.jar

SYSTEM REQUIREMENTS:
- Windows 10 or higher
- Java 17 or higher
- 4 GB RAM

Enjoy typing!
"@

$readme | Out-File -FilePath "$portableDir\README.txt" -Encoding UTF8

Write-Host "Portable package created: $portableDir\" -ForegroundColor Green
Write-Host ""
Write-Host "================================================" -ForegroundColor Cyan
Write-Host "  Build Complete!" -ForegroundColor Green
Write-Host "================================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "Your application is in: target\Type-Jam-1.0-SNAPSHOT.jar" -ForegroundColor White
Write-Host "Portable package is in: $portableDir\" -ForegroundColor White
Write-Host ""
Write-Host "To distribute: ZIP the '$portableDir' folder and share!" -ForegroundColor Cyan
Write-Host ""

