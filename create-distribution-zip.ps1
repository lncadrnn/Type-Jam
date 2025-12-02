# Create Distributable ZIP Package
Write-Host "================================================" -ForegroundColor Cyan
Write-Host "  Type-Jam Distribution Package Creator" -ForegroundColor Cyan
Write-Host "================================================" -ForegroundColor Cyan
Write-Host ""

$appFolder = "installer-output\Type-Jam"
$zipName = "Type-Jam-Windows-Setup.zip"

# Check if application exists
if (-not (Test-Path $appFolder)) {
    Write-Host "ERROR: Application folder not found at: $appFolder" -ForegroundColor Red
    Write-Host "Please run: .\create-windows-app.ps1 first" -ForegroundColor Yellow
    exit 1
}

Write-Host "Found application at: $appFolder" -ForegroundColor Green

# Get folder size
$size = (Get-ChildItem $appFolder -Recurse | Measure-Object -Property Length -Sum).Sum / 1MB
Write-Host "Application size: $([Math]::Round($size, 2)) MB" -ForegroundColor Cyan
Write-Host ""

# Remove old ZIP if exists
if (Test-Path $zipName) {
    Write-Host "Removing old ZIP file..." -ForegroundColor Yellow
    Remove-Item $zipName -Force
}

# Create ZIP
Write-Host "Creating ZIP package..." -ForegroundColor Yellow
Write-Host "This may take a moment..." -ForegroundColor Gray

try {
    Compress-Archive -Path $appFolder -DestinationPath $zipName -CompressionLevel Optimal

    if (Test-Path $zipName) {
        $zipSize = (Get-Item $zipName).Length / 1MB
        Write-Host ""
        Write-Host "================================================" -ForegroundColor Cyan
        Write-Host "  SUCCESS!" -ForegroundColor Green
        Write-Host "================================================" -ForegroundColor Cyan
        Write-Host ""
        Write-Host "ZIP Package Created: $zipName" -ForegroundColor Green
        Write-Host "ZIP Size: $([Math]::Round($zipSize, 2)) MB" -ForegroundColor Cyan
        Write-Host ""
        Write-Host "Distribution Instructions:" -ForegroundColor Yellow
        Write-Host "1. Share $zipName with users" -ForegroundColor White
        Write-Host "2. Users extract the ZIP" -ForegroundColor White
        Write-Host "3. Users run Type-Jam\Type-Jam.exe" -ForegroundColor White
        Write-Host ""
        Write-Host "No Java installation required on user's PC!" -ForegroundColor Green
        Write-Host ""
    }
} catch {
    Write-Host ""
    Write-Host "ERROR: Failed to create ZIP" -ForegroundColor Red
    Write-Host $_.Exception.Message -ForegroundColor Red
    exit 1
}

