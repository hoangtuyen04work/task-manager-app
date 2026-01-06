@echo off
chcp 65001 >nul
echo ========================================
echo   TASK MANAGER - RUN FROM JAR
echo ========================================
echo.

REM Kiểm tra file JAR có tồn tại không
if not exist "target\task-manager-app-1.0.0-jar-with-dependencies.jar" (
    echo ❌ File JAR không tồn tại!
    echo.
    echo Bạn cần build project trước:
    echo   Chạy: mvn clean package
    echo   Hoặc: build-exe.bat
    echo.
    pause
    exit /b 1
)

echo Đang khởi động Task Manager...
echo.

REM Chạy JAR file
java -jar target\task-manager-app-1.0.0-jar-with-dependencies.jar

if %errorlevel% neq 0 (
    echo.
    echo ========================================
    echo   ❌ LỖI KHI CHẠY ỨNG DỤNG
    echo ========================================
    echo.
    echo Vui lòng kiểm tra:
    echo 1. Java JDK 21 đã được cài đặt
    echo 2. MySQL Server đã khởi động
    echo 3. Database task_manager_db đã được tạo
    echo 4. Thông tin kết nối trong application.properties đúng
    echo.
    pause
)

exit /b %errorlevel%
