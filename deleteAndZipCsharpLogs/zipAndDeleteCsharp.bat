@echo off
setlocal enabledelayedexpansion
 
set "rootDir=D:\test_files\NMLroot"
set dateFormat=%date:~0,4%-%date:~5,2%-%date:~8,2%
set "log_dir=D:\test_files\20241025_zipAndDeleteCsharplog\logs\"
set deleteRetention=20

for /f "tokens=1-3 delims=-" %%k in ('powershell -command "(Get-Date).AddDays(-12).ToString('yyyyMMdd')"') do (
    set "before10days=%%k%%l%%m"
)
 
echo before10days is %before10days% >> %log_dir%zipAndDeleteCsharpLogs_%dateFormat%.txt 2>&1
 
 
for /d %%i in (%rootDir%\*) do (
   for %%j in (ADR_rest,ADR3,APP) do (
   set "logDir=%%i\CP0177WEB_LOG\%%j"
   if exist "!logDir!\" (
        pushd "!logDir!"
        echo Process logDir::!logDir! >> %log_dir%zipAndDeleteCsharpLogs_%dateFormat%.txt 2>&1
        for /f %%f in ('dir /b *.log* ^| findstr /R "\.log[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]$"') do (
            set "fileName=%%~nf"
            set "extension=%%~xf"
            set "fileDate=!fileName:~-8!"
            set "fileDateEx=!extension:~-8!"
 
            REM echo File : %%f, Name: !fileName!, fileDate: !fileDate!, Extension: !extension!, fileDateEx: !fileDateEx! >> %log_dir%zipAndDeleteCsharpLogs_%dateFormat%.txt 2>&1
 
 
             if !fileDateEx! leq %before10days% (
 
             "C:\Program Files\7-Zip\7z.exe" a -tzip "%%~nf.zip" "%%~ff"
             ren "%%~nf.zip" "%%~nxf.zip"
             del "%%f"
              )

        

 
   )
   rem 判斷是否有超過10天的檔案
   set "hasOldFile=false"
   forfiles /M "*.zip" /D -!deleteRetention! /C "cmd /c exit /b 1" && set "hasOldFile=true"
   
   if !hasOldFile! equ true (
      echo Found files older than !deleteRetention! days. >> %log_dir%\zipAndDeleteCsharpLogs_%dateFormat%.txt
      forfiles /M "*.zip" /D -!deleteRetention! /C "cmd /c del @file" >> %log_dir%zipAndDeleteCsharpLogs_%dateFormat%.txt 2>&1
   ) else (
      echo No files older than !deleteRetention! days found. >> %log_dir%\zipAndDeleteCsharpLogs_%dateFormat%.txt
   )
   
   
   popd
   )
  )
)
endlocal