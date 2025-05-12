@echo off
setlocal

set instancia=db2inst4
set user=SANTIAGO
set pass=santiago
set puerto=54000
set DB2="C:\Program Files\IBM\SQLLIB\BIN\db2cmd.exe"
net session >nul 2>&1
if %errorlevel% NEQ 0 (
    echo Se requieren permisos administrativos
    pause
    powershell -Command "Start-Process '%~f0' -Verb RunAs"
) else (
    echo AAAAAAaa
        db2icrt %instancia%
        if %errorlevel% NEQ 0 (
            echo Error al crear la instancia
            pause
            exit /b 1
        )
        set db2instance=%instancia%
        echo Instancia %instancia% creada, configurando puerto...
        call %DB2% /c /w /i db2 update dbm cfg using svcename %puerto%
        pause
        echo Estableciendo puerto: %instancia% %puerto%/tcp en el archivo de puertos

        findstr /R "^db2c_%instancia%" %SystemRoot%\System32\drivers\etc\services >nul

        if %errorlevel% NEQ 0 (
            echo %instancia% %puerto%/tcp >> %SystemRoot%\System32\drivers\etc\services
        )

        echo Configurando protocolo TCPIP...
        db2start
        db2set DB2COMM=TCPIP
        db2stop
        db2start

        echo Instancia %instancia% arrancada en puerto %puerto%
        pause
)



