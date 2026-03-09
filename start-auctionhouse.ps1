$ErrorActionPreference = "Stop"

$javaHome = "C:\Program Files\Eclipse Adoptium\jdk-21.0.10.7-hotspot"
if (-not (Test-Path "$javaHome\bin\java.exe")) {
    throw "Java 21 not found at $javaHome"
}

$env:JAVA_HOME = $javaHome
$env:Path = "$javaHome\bin;$env:Path"

$jarPath = Join-Path $PSScriptRoot "target\app-0.0.1-SNAPSHOT.jar"
if (-not (Test-Path $jarPath)) {
    Write-Host "Jar not found. Building project..."
    & "$PSScriptRoot\mvnw.cmd" clean package -DskipTests
}

Write-Host "Starting Auction House on http://localhost:8080 ..."
Start-Process -FilePath java -ArgumentList "-jar", "`"$jarPath`"" -WorkingDirectory $PSScriptRoot
Start-Sleep -Seconds 10
try {
    $res = Invoke-WebRequest -UseBasicParsing "http://localhost:8080/" -TimeoutSec 5
    Write-Host "Server is up. Status: $($res.StatusCode)"
} catch {
    Write-Host "Server start initiated. If first boot is slow, wait 15-20 seconds and retry URL."
}
