@echo off
rem ----------------------------------------------------------------------------
rem Maven Wrapper (versao enxuta) para Windows
rem Na primeira execucao baixa o Maven indicado em .mvn\wrapper\maven-wrapper.properties
rem e depois so repassa os argumentos:   mvnw spring-boot:run
rem ----------------------------------------------------------------------------
setlocal

set "MVNW_PS1=%~dp0.mvn\wrapper\download-maven.ps1"
set "MAVEN_HOME="

for /f "usebackq delims=" %%H in (`powershell -NoProfile -ExecutionPolicy Bypass -File "%MVNW_PS1%"`) do set "MAVEN_HOME=%%H"

if not exist "%MAVEN_HOME%\bin\mvn.cmd" (
  echo [mvnw] Nao foi possivel preparar o Maven. Verifique a conexao com a internet.
  exit /b 1
)

call "%MAVEN_HOME%\bin\mvn.cmd" %*
exit /b %ERRORLEVEL%
