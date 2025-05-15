@echo off
@setlocal

shift
set "instancia=%1"
set "rutaSQL=%2"

if "%instancia" == "" (
    echo No se encontro la instancia, abortando...
    pause
    exit /b 0
)

set db2instance=%instancia%
echo Cargando en instancia:
set db2instance

if exist "%rutaSQL%" (
    db2 -vtf %rutaSQL%
    echo Carga completa
    pause
    exit /b 0
) else (
    echo No se encontro el script a cargar, abortando...
    pause
    exit /b 0
)



