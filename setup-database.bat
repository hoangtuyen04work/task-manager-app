@echo off
chcp 65001 >nul
echo ========================================
echo   TASK MANAGER - SETUP DATABASE
echo ========================================
echo.

REM Thay đổi các thông tin sau theo cấu hình MySQL của bạn
set MYSQL_HOST=localhost
set MYSQL_PORT=3306
set MYSQL_USER=root
set MYSQL_PASSWORD=

echo Đang kết nối đến MySQL...
echo Host: %MYSQL_HOST%:%MYSQL_PORT%
echo User: %MYSQL_USER%
echo.

REM Tìm mysql.exe
set MYSQL_PATH=
for %%i in (
    "C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe"
    "C:\Program Files\MySQL\MySQL Server 8.4\bin\mysql.exe"
    "C:\xampp\mysql\bin\mysql.exe"
    "C:\wamp\bin\mysql\mysql8.0.31\bin\mysql.exe"
) do (
    if exist %%i (
        set MYSQL_PATH=%%i
        goto :found
    )
)

echo [LỖI] Không tìm thấy MySQL!
echo Vui lòng cài đặt MySQL hoặc XAMPP/WAMP
pause
exit /b 1

:found
echo Tìm thấy MySQL tại: %MYSQL_PATH%
echo.

echo Đang tạo database và import schema...
%MYSQL_PATH% -h %MYSQL_HOST% -P %MYSQL_PORT% -u %MYSQL_USER% -p%MYSQL_PASSWORD% < database\schema.sql

if %errorlevel% equ 0 (
    echo.
    echo ========================================
    echo   ✓ SETUP DATABASE THÀNH CÔNG!
    echo ========================================
    echo.
    echo Database 'task_manager_db' đã được tạo.
    echo Bạn có thể chạy ứng dụng Task Manager.
    echo.
) else (
    echo.
    echo ========================================
    echo   ✗ LỖI KHI SETUP DATABASE
    echo ========================================
    echo.
    echo Vui lòng kiểm tra:
    echo 1. MySQL Server đã khởi động chưa
    echo 2. Username/password có đúng không
    echo 3. File schema.sql có tồn tại không
    echo.
)

pause
