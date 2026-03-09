$target = "app-0.0.1-SNAPSHOT.jar"
Get-CimInstance Win32_Process -Filter "name='java.exe'" |
    Where-Object { $_.CommandLine -like "*$target*" } |
    ForEach-Object { Stop-Process -Id $_.ProcessId -Force }
Write-Host "Stopped Auction House java process(es)."
