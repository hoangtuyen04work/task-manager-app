# 🔍 HƯỚNG DẪN DEBUG APP - TÌM LỖI CHI TIẾT

## 📊 KẾT QUẢ KIỂM TRA

Theo log, **App ĐANG CHẠY THÀNH CÔNG**:
```
✓ APPLICATION START
✓ FXML URL loaded
✓ UI SHOWN SUCCESSFULLY  
✓ DATABASE CONNECTED SUCCESSFULLY
```

## ❓ VẬY TẠI SAO KHÔNG THẤY WINDOW?

Có thể do:
1. ⬜ Window bị ẩn sau các window khác
2. ⬜ Window mở ngoài màn hình (nếu dùng nhiều màn hình)
3. ⬜ App crash ngay sau khi mở (nhưng log không thấy lỗi)
4. ⬜ Window quá nhỏ hoặc trong suốt

---

## 🔧 CÁCH 1: CHẠY VÀ XEM LOG TRỰC TIẾP

### Bước 1: Chạy app từ JAR
```bash
java -jar target\task-manager-app-1.0.0-jar-with-dependencies.jar
```

### Bước 2: Quan sát
- Có window "Task Manager - Debug Mode" xuất hiện không?
- Nếu có lỗi, console sẽ hiển thị ngay

### Bước 3: Xem log
```bash
# Trong PowerShell:
Get-Content startup.log
Get-Content startup-error.log
```

---

## 🔧 CÁCH 2: CHẠY VỚI FULL LOGGING

### Tạo file `run-with-log.bat`:

```batch
@echo off
echo Starting Task Manager with full logging...
java -Djavafx.verbose=true -Dprism.verbose=true -jar target\task-manager-app-1.0.0-jar-with-dependencies.jar 2>&1 | tee app-debug.log
pause
```

Sau đó chạy:
```bash
run-with-log.bat
```

---

## 🔧 CÁCH 3: TÌM WINDOW ẨN

### Dùng PowerShell:

```powershell
# Tìm tất cả Java windows
Get-Process | Where-Object {$_.ProcessName -eq "java"} | 
    Select-Object ProcessName, Id, MainWindowTitle, 
    @{N='Visible';E={$_.MainWindowHandle -ne 0}}

# Nếu thấy process Java nhưng MainWindowTitle rỗng
# → Window bị ẩn hoặc minimized
```

### Giải pháp:
1. Nhấn **Alt+Tab** để xem tất cả windows
2. Nhấn **Windows Key + Tab** (Task View)
3. Kiểm tra taskbar xem có icon Java không

---

## 🔧 CÁCH 4: KIỂM TRA VỊ TRÍ FILE LOG

App có thể tạo log ở nhiều nơi:

```powershell
# Thư mục dự án
Get-ChildItem "D:\PTPMUD\task-manager-app" -Filter "startup*.log"

# Thư mục user
Get-ChildItem "$env:USERPROFILE" -Filter "startup*.log"

# Thư mục app đã cài (nếu cài từ EXE)
Get-ChildItem "C:\Program Files\TaskManager" -Filter "startup*.log" -ErrorAction SilentlyContinue
```

---

## 🔧 CÁCH 5: CHẠY TỪ VISUAL STUDIO CODE

### Tạo file `.vscode/launch.json`:

```json
{
    "version": "0.2.0",
    "configurations": [
        {
            "type": "java",
            "name": "Debug TaskManager",
            "request": "launch",
            "mainClass": "com.taskmanager.Launcher",
            "projectName": "task-manager-app",
            "vmArgs": "-Dfile.encoding=UTF-8"
        }
    ]
}
```

Sau đó:
1. Mở VS Code
2. Nhấn F5 (Start Debugging)
3. Xem console output trong VS Code

---

## 🔧 CÁCH 6: KIỂM TRA DATABASE

Có thể app crash do không kết nối được DB:

```bash
# Test kết nối MySQL
mysql -u root -p123 -e "SHOW DATABASES;"

# Kiểm tra database task_manager_db
mysql -u root -p123 -e "USE task_manager_db; SHOW TABLES;"
```

Nếu lỗi → Chạy:
```bash
setup-database.bat
```

---

## 🔧 CÁCH 7: KIỂM TRA FILE FXML

Có thể FXML bị lỗi:

```powershell
# Kiểm tra file tồn tại
Test-Path "src\main\resources\fxml\main.fxml"

# Xem nội dung
Get-Content "src\main\resources\fxml\main.fxml" | Select-Object -First 20
```

---

## 🔧 CÁCH 8: CHẠY VỚI JVM DEBUG

```bash
java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005 ^
     -jar target\task-manager-app-1.0.0-jar-with-dependencies.jar
```

Sau đó attach debugger từ IDE.

---

## 📸 CÁCH 9: CHỤP SCREENSHOT TỰ ĐỘNG

Sửa Main.java để chụp màn hình khi UI được tạo:

```java
primaryStage.show();
log("UI SHOWN SUCCESSFULLY");

// Chụp screenshot để debug
Platform.runLater(() -> {
    try {
        WritableImage image = scene.snapshot(null);
        File file = new File("ui-screenshot.png");
        ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
        log("Screenshot saved: " + file.getAbsolutePath());
    } catch (Exception e) {
        log("Screenshot failed: " + e.getMessage());
    }
});
```

---

## 🆘 CHECKLIST DEBUG

Hãy làm theo thứ tự:

- [ ] **Bước 1:** Chạy `java -jar target\task-manager-app-1.0.0-jar-with-dependencies.jar`
- [ ] **Bước 2:** Nhấn Alt+Tab xem có window Task Manager không
- [ ] **Bước 3:** Xem file `startup.log` - có dòng "UI SHOWN SUCCESSFULLY" không?
- [ ] **Bước 4:** Xem file `startup-error.log` - có lỗi gì không?
- [ ] **Bước 5:** Kiểm tra MySQL đang chạy: `mysql -u root -p123 -e "SHOW DATABASES;"`
- [ ] **Bước 6:** Kiểm tra database: `mysql -u root -p123 -e "USE task_manager_db; SHOW TABLES;"`
- [ ] **Bước 7:** Nếu vẫn không thấy window, thử restart máy

---

## 🎯 THÔNG TIN QUAN TRỌNG

**Log hiện tại cho thấy:**
```
2026-01-05T23:28:41.778135100 | === APPLICATION START ===
2026-01-05T23:28:41.779223800 | FXML URL = jar:file:/D:/PTPMUD/task-manager-app/...
2026-01-05T23:28:43.413163200 | UI SHOWN SUCCESSFULLY  ← ✓ UI ĐÃ MỞ!
2026-01-05T23:28:43.415696200 | INIT DATABASE...
2026-01-05T23:28:43.450498700 | DATABASE CONNECTED SUCCESSFULLY  ← ✓ DB OK!
```

**Kết luận:** App **ĐANG CHẠY ĐÚNG**! 

Vấn đề có thể là:
1. Window bị ẩn → Nhấn **Alt+Tab**
2. Window ở màn hình khác → Di chuyển chuột sang màn hình khác
3. Window đã đóng → Chạy lại và để ý taskbar

---

## 💡 DEBUG NHANH NHẤT

```bash
# 1. Xóa log cũ
del startup*.log

# 2. Chạy app
java -jar target\task-manager-app-1.0.0-jar-with-dependencies.jar

# 3. Trong khi app chạy, mở terminal khác:
type startup.log
type startup-error.log

# 4. Tìm window
tasklist /FI "IMAGENAME eq java.exe" /V
```

---

**Bạn có thấy window Task Manager không? Hãy cho tôi biết kết quả!**
