# Simple JAR Build Script
# This script builds a runnable JAR file

Write-Host "================================================" -ForegroundColor Cyan
Write-Host "  Type-Jam JAR Builder" -ForegroundColor Cyan
Write-Host "================================================" -ForegroundColor Cyan
Write-Host ""

# Build the project
Write-Host "Building project..." -ForegroundColor Yellow
.\mvnw.cmd clean package -DskipTests

if ($LASTEXITCODE -eq 0) {
    Write-Host ""
    Write-Host "================================================" -ForegroundColor Cyan
    Write-Host "  Build Successful!" -ForegroundColor Green
    Write-Host "================================================" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "Your runnable JAR is at: target\Type-Jam-1.0-SNAPSHOT.jar" -ForegroundColor Green
    Write-Host ""
    Write-Host "To run the application:" -ForegroundColor Cyan
    Write-Host "  java -jar target\Type-Jam-1.0-SNAPSHOT.jar" -ForegroundColor White
    Write-Host ""
} else {
    Write-Host ""
    Write-Host "✗ Build failed!" -ForegroundColor Red
    exit 1
}

