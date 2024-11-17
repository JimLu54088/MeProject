# How to use

1. Create one excel file insertQuerytest.xlsx, path you can set.

**2. If you want to test, change test folder in test java, please create a new one, otherwise this folder will be delete after testing automatically!!!!
For example, D:\\myjava\\ttt is not a path in your PC, then you can set it. If D:\\myjava\\ttt is already in your PC, change to another one.**

3. For test jar Find D:\workspace_backup\workspace2\deleteFilesInFolder\FileUnitJar

4. set configuration

parameter will be like

D:\\myjava\\insertQuerytest.xlsx D:\\myjava 5

excelFilePath = args[0];
outputSQLDirRoot = args[1];
lastRow = args[2];

lastRow is the count you want to generate SQL. For example the target count of rows is 4 (actually there are 5 rows in excel file), the lastRow parameter should be 4.

In excel file, the format is like
from most left and most up(A1)

col1	col2	col3	col4
value1	value2	value3	value14


