@echo off
setlocal


shift
set "DB2User=%1"
set "DB2Pass=%2"

echo Creando usuario: %DB2User%

net user %DB2User% >nul 2>&1
if %errorlevel% EQU 0 (
    echo El usuario ya existe, saltando..
    exit /b 2
)
net user %DB2User% %DB2Pass% /add
if %errorlevel% NEQ 0 (
    echo Error al crear el usuario, abortando...
    pause
    exit /b 1
)
wmic useraccount where "name='%DB2User%'" set PasswordExpires=FALSE

echo Usuario configurado exitosamente
exit /b 0