@echo off
setlocal

set "instancia=%1"
set "puerto=%2"
set "autoRun=%3"
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
        call %DB2% /c /w /i db2 update dbm cfg using svcename %puerto%
        echo Estableciendo puerto: %instancia% %puerto%/tcp en el archivo de puertos

        sc query "%instancia%" >nul
        if %errorlevel%== 0 (
            echo ADVERTENCIA: Ya existe una instancia con el mismo nombre, se volverá a configurar..
        )

        findstr /R "^db2c_%instancia%" %SystemRoot%\System32\drivers\etc\services >nul

        if %errorlevel% NEQ 0 (
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



