@echo off
setlocal

set bd="Usuario"
set nodo="nodoUsr"
set alias="Usuario"
set host="127.0.0.1"
set puerto="60000"
set user="SANTIAGO"
set pass="santiago"

db2 catalog tcpip node %nodo% remote %host% server %puerto%

db2 uncatalog db %alias% >nul 2>&1
db2 catalog db %bd% as %alias% at node %nodo%

db2 connect to %alias% user %user% using %pass%

if %errorlevel%==0 (
    echo La base de datos ya existe, saltando...
    db2 connect reset
    exit /b 1
)
pause
db2 CREATE DATABASE %bd%

if %errorlevel% NEQ 0 (
    echo "Error al crear la base de datos"
    db2 connect reset
    exit /b 2
)