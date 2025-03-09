pushd "D:\myjava\PGPEncryptDecryptTool\bat"

:: 提取當前日期 (格式: YYYY-MM-DD)
for /f "tokens=1-3 delims=/" %%a in ('date /t') do (
    set year=%%a
    set month=%%b
    set day=%%c
)
set dateFormat=%year%-%month%-%day%

echo dateFormat=%dateFormat%

:: 執行 Jar 並將輸出重定向到日期命名的檔案
D:\JDK\jdk-18.0.2.1\bin\java -jar "D:\myjava\PGPEncryptDecryptTool\out\artifacts\PGPEncryptDecryptTool_jar\PGPEncryptDecryptTool.jar" "EncryptPGP" "D:/myjava/PGPEncryptDecryptTool/encrypt.properties" >> "Tool_Log_%dateFormat%.log" 2>&1

if %ERRORLEVEL% GEQ 1 goto Error
goto end

:Error
popd
echo "ClassifyIphone_picture_Tool Error"
REM pause
exit 1

:end
popd
echo "ClassifyIphone_picture_Tool Success"
REM pause
exit 0
