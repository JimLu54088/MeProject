@echo off
setlocal

:: 指定要删除的目标目录
set target_dir=D:\test_files\20240616-deletefolderfilds\test

:: 检查目录是否存在
if exist "%target_dir%" (
    :: 删除目标目录下的所有文件和文件夹
    echo deleting folders and filds under %target_dir% ...
    rd /s /q "%target_dir%"
    :: 重新创建目标目录（如果需要,沒有這一行 連test本身都會被刪掉）
     mkdir "%target_dir%"
    echo Delete files and folders under %target_dir% completedly...
) else (
    echo target folder %target_dir% doesn't exist.
)

endlocal
pause
