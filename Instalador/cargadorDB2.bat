@echo off
setlocal

set DB2="db2cmd.exe"
set "script=%1"

powershell -Command "Start-Process '%DB2%' -ArgumentList '%script% %*' -Verb RunAs -Wait"
exit /b 0
