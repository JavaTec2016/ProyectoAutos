@echo off
setlocal enabledelayedexpansion

set "instancia=%1"
set "puerto=%2"
set "autoRun=%3"
set "user=%4"
set "pass=%5"
echo K %instancia%
if "%instancia%"=="" (
    echo Error: No se especificó el nombre de instancia.
    pause
    exit /b 1
)
if "%puerto%"=="" (
    echo Error: No se especificó el puerto
    pause
    exit /b 2
)
echo %puerto% server %instancia%
set DB2="C:\Program Files\IBM\SQLLIB\BIN\db2cmd.exe"
net session >nul 2>&1
if %errorlevel% NEQ 0 (
    echo Se requieren permisos administrativos
    pause
        powershell -Command "Start-Process '%~f0' -ArgumentList '%*' -Verb RunAs -Wait"
) else (

        db2icrt %instancia%
        if %errorlevel% NEQ 0 (
            echo Error al crear la instancia
            pause
            exit /b 1
        )
        set db2instance=%instancia%
        echo Instancia %instancia% creada, configurando puerto...
        call %DB2% /c /w /i db2 update dbm cfg using svcename %instancia%
        echo Estableciendo puerto: %instancia% %puerto%/tcp en el archivo de puertos

        sc query %instancia%K > nul
        if !errorlevel! NEQ 0 (
            echo ADVERTENCIA: Ya existe una instancia o servicio de windows con el mismo nombre, saltando..
            pause
            exit /b 1
        )

        findstr /R "^db2c_%instancia%" %SystemRoot%\System32\drivers\etc\services

        if %errorlevel% == 0 (
            echo Agregando puerto al archivo de puertos....
            echo %instancia% %puerto%/tcp >> %SystemRoot%\System32\drivers\etc\services
        )

        echo Configurando protocolo TCPIP...
        db2start
        db2set DB2COMM=TCPIP
        db2stop
        db2start
        if "%autoRun%"=="true" (
            echo Configurando auto arranque del servicio...
            sc config "%instancia%" start= auto
        )
        echo Instancia %instancia% arrancada en puerto %puerto%
        pause
)



