# Usado pelo mvnw.cmd: garante que o Maven do maven-wrapper.properties esteja baixado
# e imprime (na ultima linha) a pasta onde ele esta.
$ErrorActionPreference = 'Stop'

$props = Join-Path $PSScriptRoot 'maven-wrapper.properties'
$line = Get-Content $props | Where-Object { $_ -match '^distributionUrl=' } | Select-Object -First 1
$url = ($line -replace '^distributionUrl=', '').Trim()

$zipName = [System.IO.Path]::GetFileName($url)      # apache-maven-3.9.9-bin.zip
$name = $zipName -replace '-bin\.zip$', ''          # apache-maven-3.9.9

$userHome = if ($env:MAVEN_USER_HOME) { $env:MAVEN_USER_HOME } else { Join-Path $HOME '.m2' }
$distDir = Join-Path $userHome "wrapper\dists\$name"
$mavenHome = Join-Path $distDir $name

if (-not (Test-Path (Join-Path $mavenHome 'bin\mvn.cmd'))) {
    [Console]::Error.WriteLine("[mvnw] Baixando $zipName (so na primeira vez)...")
    New-Item -ItemType Directory -Force -Path $distDir | Out-Null
    $zipPath = Join-Path $distDir $zipName
    [Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12
    (New-Object System.Net.WebClient).DownloadFile($url, $zipPath)
    Expand-Archive -Path $zipPath -DestinationPath $distDir -Force
    Remove-Item $zipPath -Force
}

Write-Output $mavenHome
