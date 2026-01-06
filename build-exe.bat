@echo off
chcp 65001 >nul
echo ========================================
echo   TASK MANAGER - BUILD EXE
echo ========================================
echo.

REM Dừng các process đang chạy
echo Đang dừng các process đang chạy...
taskkill /F /IM TaskManager.exe >nul 2>&1
taskkill /F /IM java.exe >nul 2>&1
timeout /t 2 /nobreak >nul

REM Xóa thư mục installer
echo Đang xóa build cũ...
if exist "target\installer" (
    rmdir /s /q "target\installer" 2>nul
    if exist "target\installer" (
        echo ⚠ Không thể xóa target\installer - vui lòng đóng TaskManager.exe
        pause
        exit /b 1
    )
)

echo [1/2] Building JAR with dependencies...
call mvn compile package -DskipTests
if %errorlevel% neq 0 (
    echo ❌ Build JAR failed!
    pause
    exit /b 1
)
echo ✓ JAR build completed
echo.

echo [2/2] Creating EXE installer...
call mvn jpackage:jpackage
if %errorlevel% neq 0 (
    echo ❌ Packaging failed!
    pause
    exit /b 1
)
echo ✓ EXE installer created
echo.

echo ========================================
echo   ✓ BUILD THÀNH CÔNG!
echo ========================================
echo.
echo File installer đã được tạo tại:
echo target\installer\TaskManager-1.0.0.exe
echo.

REM Hiển thị thông tin file
if exist "target\installer\TaskManager-1.0.0.exe" (
    for %%A in ("target\installer\TaskManager-1.0.0.exe") do (
        set size=%%~zA
    )
    call :formatBytes %size%
    echo Kích thước: %formattedSize%
    echo.
    
    REM Hỏi có muốn mở thư mục không
    set /p open="Bạn có muốn mở thư mục chứa file? (Y/N): "
    if /i "%open%"=="Y" (
        explorer "target\installer"
    )
)

pause
exit /b 0

:formatBytes
set bytes=%1
set /a mb=%bytes% / 1048576
set /a kb=%bytes% / 1024
if %mb% gtr 0 (
    set formattedSize=%mb% MB
) else (
    set formattedSize=%kb% KB
)
goto :eof
