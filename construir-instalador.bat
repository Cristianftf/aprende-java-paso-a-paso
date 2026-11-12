@echo off
setlocal
cd /d "%~dp0"

if not defined JAVA_HOME (
    for /d %%D in ("%ProgramFiles%\Java\jdk-*") do set "JAVA_HOME=%%~fD"
)

if not defined JAVA_HOME (
    echo No se encontro el JDK. Define JAVA_HOME apuntando a un JDK 17 o posterior.
    pause
    exit /b 1
)

if not exist "%JAVA_HOME%\bin\javac.exe" (
    echo JAVA_HOME no apunta a un JDK valido: %JAVA_HOME%
    pause
    exit /b 1
)

if not exist "%JAVA_HOME%\bin\jpackage.exe" (
    echo Este JDK no incluye jpackage. Instala un JDK 17 o posterior.
    pause
    exit /b 1
)

set "ISCC="
for /f "delims=" %%I in ('where ISCC.exe 2^>nul') do set "ISCC=%%I"
if not defined ISCC if exist "%LOCALAPPDATA%\Programs\Inno Setup 6\ISCC.exe" set "ISCC=%LOCALAPPDATA%\Programs\Inno Setup 6\ISCC.exe"
if not defined ISCC if exist "%ProgramFiles(x86)%\Inno Setup 6\ISCC.exe" set "ISCC=%ProgramFiles(x86)%\Inno Setup 6\ISCC.exe"

if not defined ISCC (
    echo Falta Inno Setup 6 para generar el instalador EXE.
    echo Comando: winget install --id JRSoftware.InnoSetup --exact --scope user
    pause
    exit /b 1
)

if exist build\classes rmdir /s /q build\classes
if exist build\package-input rmdir /s /q build\package-input
if exist build\image rmdir /s /q build\image
if not exist build\classes mkdir build\classes
if not exist build\package-input mkdir build\package-input
if not exist dist mkdir dist

"%JAVA_HOME%\bin\javac.exe" --release 17 -encoding UTF-8 -d build\classes src\App.java
if errorlevel 1 (
    echo No se pudo compilar la aplicacion.
    pause
    exit /b 1
)

"%JAVA_HOME%\bin\jar.exe" --create --file build\package-input\AprendeJava.jar --main-class App -C build\classes .
if errorlevel 1 (
    echo No se pudo crear el archivo JAR.
    pause
    exit /b 1
)

"%JAVA_HOME%\bin\jpackage.exe" ^
    --type app-image ^
    --name AprendeJava ^
    --app-version 1.0.0 ^
    --vendor "Aprende Java" ^
    --description "Curso interactivo de Java en espanol" ^
    --input build\package-input ^
    --main-jar AprendeJava.jar ^
    --main-class App ^
    --add-modules java.desktop,jdk.compiler ^
    --dest build\image
if errorlevel 1 (
    echo No se pudo preparar la aplicacion con su runtime de Java.
    pause
    exit /b 1
)

copy /y "%JAVA_HOME%\bin\java.exe" "build\image\AprendeJava\runtime\bin\java.exe" >nul
if errorlevel 1 (
    echo No se pudo incluir el lanzador de Java para ejecutar los proyectos.
    pause
    exit /b 1
)

"%ISCC%" /Qp construir-instalador.iss
if errorlevel 1 (
    echo No se pudo crear el instalador.
    pause
    exit /b 1
)

echo.
echo Instalador creado en dist\AprendeJava-Setup-1.0.0.exe
echo Incluye su propio runtime de Java; el estudiante no necesita instalar Java aparte.
pause
