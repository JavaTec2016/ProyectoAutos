@echo off
setlocal

set db2="C:\Program Files\IBM\SQLLIB\BIN\db2cmd.exe"

if %errorlevel% NEQ 0 (
    echo Se requieren permisos administrativos
    pause
        powershell -Command "Start-Process '%~f0' -ArgumentList '%*' -Verb RunAs -Wait"
) else (
    echo iniciando la creacion de base de datos...
    db2 list db directory | findstr /C:"Autos" > nul
    if %errorlevel%==1 (
        echo Ya existe una base de datos con el mismo nombre, saltando...
        exit /b 3
    ) else (
        echo Creando base de datos 'Autos'...
        db2 CREATE DATABASE Autos
    )
    echo Cargando tablas...
    if exist "../sql/bd.sql" (
        db2 CONNECT TO Autos
        db2 -vtf ../sql/bd.sql
        echo Carga completa, presione cualquier tecla para salir
        db2 connect reset
        pause
    ) else (
        echo El archivo no fue encontrado, abortando...
        exit /b 3
    )
)

