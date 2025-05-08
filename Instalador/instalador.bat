@echo off
echo iniciando la creacion de base de datos...
db2cmd /c db2 list db directory | findstr /C:"Autos" > nul
if %errorlevel%==1 (
    echo Ya existe una base de datos con el mismo nombre, saltando...
    exit /b 3
) else (
    echo Creando base de datos 'Autos'...
    db2cmd /c db2 "CREATE DATABASE Autos"
)
echo Cargando tablas...
if exist "../sql/bd.sql" (
    db2cmd /c db2 "CONNECT TO Autos"
    db2cmd /c db2 "db2 -vtf ../sql/bd.sql"
    echo Carga completa, presione cualquier tecla para salir
) else (
    echo El archivo no fue encontrado, abortando...
    exit /b 3
)