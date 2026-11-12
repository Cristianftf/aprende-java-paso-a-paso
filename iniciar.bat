@echo off
chcp 65001 > nul
cd /d "%~dp0"

if not exist bin mkdir bin

javac --release 17 -encoding UTF-8 -d bin src\App.java
if errorlevel 1 (
    echo No se pudo compilar. Comprueba que el JDK de Java este instalado.
    pause
    exit /b 1
)

java -Dfile.encoding=UTF-8 -cp bin App
pause
