@echo off
setlocal

set instancia=%1
set DB2="C:\Program Files\IBM\SQLLIB\BIN\db2cmd.exe"
net session >nul 2>&1
if %errorlevel% NEQ 0 (
    echo Se requieren permisos administrativos, de verdad quiere eliminar esta instancia?: %instancia%
    pause
    powershell -Command "Start-Process '%~f0' -ArgumentList '%*' -Verb RunAs -Wait"
) else (
    echo apagando instancia...
    set db2instance=%instancia%
    db2stop
    db2idrop %instancia%
    echo salida: %errorlevel%
)
