@echo off
chcp 65001 >nul
echo ========================================
echo   DEBUG TASK MANAGER APP
echo ========================================
echo.
echo Chọn cách debug:
echo.
echo 1. Chạy từ JAR (xem lỗi console trực tiếp)
echo 2. Chạy EXE đã cài với console log
echo 3. Xem log files (nếu có)
echo 4. Test kết nối database
echo 5. Thoát
echo.
set /p choice="Nhập lựa chọn (1-5): "

if "%choice%"=="1" goto run_jar
if "%choice%"=="2" goto run_exe_console
if "%choice%"=="3" goto view_logs
if "%choice%"=="4" goto test_db
if "%choice%"=="5" goto end

:run_jar
echo.
echo ========================================
echo   CHẠY TỪ JAR - XEM LỖI TRỰC TIẾP
echo ========================================
echo.
if not exist "target\task-manager-app-1.0.0-jar-with-dependencies.jar" (
    echo ❌ File JAR không tồn tại!
    echo Vui lòng build trước: mvn compile package -DskipTests
    pause
    goto end
)

echo Đang khởi động app...
echo Nếu có lỗi, bạn sẽ thấy ngay ở đây ↓
echo ----------------------------------------
echo.

java -jar target\task-manager-app-1.0.0-jar-with-dependencies.jar

echo.
echo ----------------------------------------
if %errorlevel% neq 0 (
    echo.
    echo ❌ App đã dừng với mã lỗi: %errorlevel%
    echo.
)
pause
goto end

:run_exe_console
echo.
echo ========================================
echo   CHẠY EXE VỚI CONSOLE LOG
echo ========================================
echo.

REM Tìm TaskManager.exe đã cài
set EXE_PATH=
if exist "C:\Program Files\TaskManager\TaskManager.exe" (
    set EXE_PATH=C:\Program Files\TaskManager\TaskManager.exe
) else if exist "C:\Program Files (x86)\TaskManager\TaskManager.exe" (
    set EXE_PATH=C:\Program Files (x86)\TaskManager\TaskManager.exe
) else if exist "target\installer\TaskManager-1.0.0.exe" (
    echo ⚠ Tìm thấy installer, không phải app đã cài
    echo Vui lòng cài đặt app trước
    pause
    goto end
)

if "%EXE_PATH%"=="" (
    echo ❌ Không tìm thấy TaskManager.exe đã cài!
    echo.
    echo Vui lòng cài đặt app trước từ:
    echo target\installer\TaskManager-1.0.0.exe
    echo.
    pause
    goto end
)

echo Đã tìm thấy: %EXE_PATH%
echo Đang khởi động với console log...
echo.

start "" "%EXE_PATH%"

echo ✓ App đã được khởi động
echo Kiểm tra xem window có mở không
echo.
pause
goto end

:view_logs
echo.
echo ========================================
echo   XEM LOG FILES
echo ========================================
echo.

REM Kiểm tra log ở thư mục hiện tại
if exist "startup.log" (
    echo ✓ Tìm thấy startup.log:
    echo ----------------------------------------
    type startup.log
    echo ----------------------------------------
    echo.
) else (
    echo ⚠ Không có startup.log ở thư mục hiện tại
)

if exist "startup-error.log" (
    echo ✓ Tìm thấy startup-error.log:
    echo ----------------------------------------
    type startup-error.log
    echo ----------------------------------------
    echo.
) else (
    echo ⚠ Không có startup-error.log
)

REM Kiểm tra log ở thư mục user
set USER_DIR=%USERPROFILE%
if exist "%USER_DIR%\startup.log" (
    echo ✓ Tìm thấy log ở thư mục user:
    echo ----------------------------------------
    type "%USER_DIR%\startup.log"
    echo ----------------------------------------
    echo.
)

REM Kiểm tra log ở thư mục app đã cài
if exist "C:\Program Files\TaskManager\startup.log" (
    echo ✓ Tìm thấy log ở thư mục cài đặt:
    echo ----------------------------------------
    type "C:\Program Files\TaskManager\startup.log"
    echo ----------------------------------------
    echo.
)

if exist "C:\Program Files\TaskManager\startup-error.log" (
    echo ✓ Tìm thấy error log ở thư mục cài đặt:
    echo ----------------------------------------
    type "C:\Program Files\TaskManager\startup-error.log"
    echo ----------------------------------------
    echo.
)

pause
goto end

:test_db
echo.
echo ========================================
echo   TEST KẾT NỐI DATABASE
echo ========================================
echo.

REM Đọc cấu hình database
if not exist "src\main\resources\application.properties" (
    echo ❌ Không tìm thấy application.properties
    pause
    goto end
)

echo Đọc cấu hình database...
findstr /C:"db.url" src\main\resources\application.properties
findstr /C:"db.username" src\main\resources\application.properties
echo.

echo Đang test kết nối MySQL...
mysql --version >nul 2>&1
if %errorlevel% neq 0 (
    echo ⚠ Không tìm thấy MySQL client trong PATH
    echo Vui lòng kiểm tra MySQL đã cài đặt chưa
    pause
    goto end
)

REM Lấy thông tin từ properties
for /f "tokens=2 delims==" %%a in ('findstr /C:"db.username" src\main\resources\application.properties') do set DB_USER=%%a
for /f "tokens=2 delims==" %%a in ('findstr /C:"db.password" src\main\resources\application.properties') do set DB_PASS=%%a

echo Testing connection với user: %DB_USER%
mysql -u %DB_USER% -p%DB_PASS% -e "SHOW DATABASES;" 2>nul
if %errorlevel% equ 0 (
    echo ✓ Kết nối MySQL thành công!
    echo.
    echo Kiểm tra database task_manager_db:
    mysql -u %DB_USER% -p%DB_PASS% -e "USE task_manager_db; SHOW TABLES;"
    if %errorlevel% equ 0 (
        echo ✓ Database task_manager_db tồn tại!
    ) else (
        echo ❌ Database task_manager_db không tồn tại!
        echo Vui lòng chạy: setup-database.bat
    )
) else (
    echo ❌ Không kết nối được MySQL!
    echo.
    echo Vui lòng kiểm tra:
    echo 1. MySQL Server đã khởi động chưa
    echo 2. Username/password trong application.properties có đúng không
)

echo.
pause
goto end

:end
exit /b 0
