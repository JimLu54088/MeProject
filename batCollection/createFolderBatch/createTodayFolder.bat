@echo off
setlocal

REM 获取当前日期
for /f "tokens=2 delims==" %%i in ('"wmic os get localdatetime /value"') do set datetime=%%i
set year=%datetime:~0,4%
set month=%datetime:~4,2%
set day=%datetime:~6,2%
set currentDate=%year%%month%%day%

REM 目标文件夹路径
set targetFolder=D:\test_files\20240615_createTodayFolder

REM 创建以当天日期命名的文件夹
set dateFolder=%targetFolder%\%currentDate%
mkdir "%dateFolder%"


REM 在日期文件夹下创建子文件夹 kkk 和 ttt
mkdir "%dateFolder%\kkk"

mkdir "%dateFolder%\ttt"

echo Create Folders successfully！
pause
exit /b 0

