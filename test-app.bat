@echo off
chcp 65001 >nul
cls
echo ╔════════════════════════════════════════╗
echo ║   DEBUG TASK MANAGER - XEM LỖI CHI TIẾT   ║
echo ╚════════════════════════════════════════╝
echo.

REM Xóa log cũ
if exist startup.log del startup.log
if exist startup-error.log del startup-error.log

echo [1] Khởi động app...
echo.
echo ⚠ QUAN TRỌNG: Để ý xem có cửa sổ "Task Manager" xuất hiện không!
echo.
echo Đang chạy...
echo ────────────────────────────────────────
echo.

REM Chạy app trong foreground để thấy lỗi
java -jar target\task-manager-app-1.0.0-jar-with-dependencies.jar

echo.
echo ────────────────────────────────────────
echo.

if %errorlevel% neq 0 (
    echo ❌ App đã dừng với lỗi: %errorlevel%
) else (
    echo ✓ App đã đóng bình thường
)

echo.
echo [2] Kiểm tra log files...
echo.

if exist startup.log (
    echo ✓ startup.log:
    echo ────────────────────────────────────────
    type startup.log
    echo ────────────────────────────────────────
    echo.
) else (
    echo ⚠ Không có startup.log
)

if exist startup-error.log (
    echo ❌ startup-error.log (CÓ LỖI):
    echo ────────────────────────────────────────
    type startup-error.log
    echo ────────────────────────────────────────
    echo.
) else (
    echo ✓ Không có startup-error.log (Không có lỗi fatal)
)

echo.
echo [3] Phân tích...
echo.

if exist startup.log (
    findstr /C:"UI SHOWN SUCCESSFULLY" startup.log >nul
    if %errorlevel% equ 0 (
        echo ✓ UI đã được hiển thị thành công
    ) else (
        echo ❌ UI không được hiển thị
    )
    
    findstr /C:"DATABASE CONNECTED SUCCESSFULLY" startup.log >nul
    if %errorlevel% equ 0 (
        echo ✓ Database kết nối thành công
    ) else (
        echo ⚠ Database không kết nối được
    )
)

echo.
echo ════════════════════════════════════════
echo.
echo BẠN CÓ THẤY CỬA SỔ "TASK MANAGER" KHÔNG?
echo.
echo • Nếu THẤY → App chạy OK!
echo • Nếu KHÔNG THẤY nhưng log OK → Window bị ẩn (nhấn Alt+Tab)
echo • Nếu có lỗi → Xem thông báo lỗi ở trên
echo.
pause
