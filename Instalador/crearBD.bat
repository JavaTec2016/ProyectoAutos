@echo off
setlocal


set DB2="C:\Program Files\IBM\SQLLIB\BIN\db2cmd.exe"

if "%1" NEQ "DB2iniciado" (
    echo Relanzando dentro de db2cmd...
    %DB2% /i /c "\"%~f0\" DB2iniciado %*"
)
shift
set "bd=%1"
set "instancia=%2"
set "user=%3"
set "pass=%4"

    echo Probando base de datos para %user% ..

    echo %bd% %instancia%
    set db2instance=%instancia%
    db2 connect to %bd%

    if %errorlevel%== 0 (
        echo la base de datos ya existe
        db2 connect reset
        exit /b 1
    )

    echo Creando la base de datos...
    db2 CREATE DATABASE %bd%
    if %errorlevel% NEQ 0 (
        echo Error al crear la base de datos
        db2 connect reset
        exit /b 2
    )
)