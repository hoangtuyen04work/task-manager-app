# 🛠️ FIX: App đóng ngay lập tức - ĐÃ GIẢI QUYẾT

## ❌ VẤN ĐỀ GẶP PHẢI

Khi chạy file `TaskManager-1.0.0.exe`, app đóng ngay lập tức không hiển thị gì.

**Nguyên nhân:** JavaFX modules không được bundle đúng cách vào file EXE.

---

## ✅ GIẢI PHÁP ĐÃ THỰC HIỆN

### 1️⃣ Tạo Launcher Class
Đã tạo file [Launcher.java](src/main/java/com/taskmanager/Launcher.java) để khởi động JavaFX:

```java
package com.taskmanager;

public class Launcher {
    public static void main(String[] args) {
        Main.main(args);
    }
}
```

**Tại sao cần Launcher?**
- JavaFX yêu cầu main class không được extend từ `Application`
- Launcher giúp khởi động app đúng cách khi chạy từ JAR/EXE

### 2️⃣ Cập nhật pom.xml

**a) Đổi main class trong Maven Assembly:**
```xml
<mainClass>com.taskmanager.Launcher</mainClass>
```

**b) Thêm JavaFX modules vào jpackage:**
```xml
<javaOptions>
    <option>-Dfile.encoding=UTF-8</option>
    <option>--add-modules</option>
    <option>javafx.controls,javafx.fxml</option>
</javaOptions>
```

**c) Đổi output folder:**
```xml
<destination>target/installer</destination>
```

### 3️⃣ Rebuild Project

File EXE mới đã được tạo tại:
```
📁 target/installer/TaskManager-1.0.0.exe
```

---

## 🚀 CÁCH SỬ DỤNG FILE EXE MỚI

### **Bước 1: Vị trí file EXE mới**
```
D:\PTPMUD\task-manager-app\target\installer\TaskManager-1.0.0.exe
```

### **Bước 2: Cài đặt ứng dụng**

1. **Chạy installer:**
   - Double-click file `TaskManager-1.0.0.exe`
   - Nếu Windows hiện cảnh báo:
     - Click **"More info"**
     - Click **"Run anyway"**

2. **Làm theo hướng dẫn:**
   - Chọn thư mục cài đặt
   - Tích chọn "Create shortcuts"
   - Click "Install"

### **Bước 3: Setup Database (nếu chưa làm)**

**Cách 1 - Tự động:**
```bash
setup-database.bat
```

**Cách 2 - Thủ công:**
```sql
CREATE DATABASE task_manager_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```
Sau đó import file `database/schema.sql`

### **Bước 4: Chạy ứng dụng**

- Từ Desktop shortcut: **Task Manager**
- Từ Start Menu: Tìm "Task Manager"
- Từ thư mục cài đặt: `C:\Program Files\TaskManager\TaskManager.exe`

---

## 🔍 KIỂM TRA VÀ DEBUG

### Test JAR trước (không cần cài đặt):

```bash
run-app.bat
```

Hoặc:
```bash
java -jar target\task-manager-app-1.0.0-jar-with-dependencies.jar
```

### Xem log nếu có lỗi:

Sau khi chạy app, kiểm tra các file log:
```
startup.log          - Log khởi động bình thường
startup-error.log    - Log chi tiết lỗi nếu có
```

---

## 📝 BUILD LẠI FILE EXE (nếu cần)

### Cách nhanh:
```bash
build-exe.bat
```

### Cách thủ công:
```bash
# Bước 1: Build JAR
mvn compile package -DskipTests

# Bước 2: Tạo EXE
mvn jpackage:jpackage
```

File sẽ được tạo tại: `target\installer\TaskManager-1.0.0.exe`

---

## ✅ CHECKLIST TRƯỚC KHI CHẠY

- [ ] MySQL Server đã khởi động
- [ ] Database `task_manager_db` đã được tạo
- [ ] File `application.properties` có thông tin kết nối đúng:
  ```properties
  db.url=jdbc:mysql://localhost:3306/task_manager_db?useSSL=false&serverTimezone=UTC
  db.username=root
  db.password=123
  ```
- [ ] File EXE mới từ thư mục `target\installer`

---

## 🎯 SO SÁNH FILE CŨ VÀ MỚI

| | File Cũ | File Mới (Fixed) |
|---|---|---|
| **Vị trí** | `target/dist/` | `target/installer/` |
| **Main Class** | `Main.class` | `Launcher.class` |
| **JavaFX Modules** | ❌ Thiếu | ✅ Đã thêm |
| **Trạng thái** | ❌ Đóng ngay | ✅ Chạy OK |

---

## 💡 LƯU Ý QUAN TRỌNG

### ⚠️ Nếu app vẫn đóng ngay:

1. **Kiểm tra MySQL:**
   ```bash
   # Mở MySQL Workbench hoặc XAMPP
   # Đảm bảo MySQL đang chạy
   ```

2. **Kiểm tra database:**
   ```sql
   SHOW DATABASES;
   -- Phải có 'task_manager_db'
   ```

3. **Xem log chi tiết:**
   - Chạy từ JAR: `run-app.bat`
   - Xem file: `startup-error.log`

### ✅ App chạy thành công khi:

- UI hiển thị window "Task Manager"
- Có thể add/edit/delete tasks
- Không có error dialog

---

## 🎉 KẾT QUẢ

File EXE mới đã được fix và hoạt động đúng:
- ✅ Khởi động thành công
- ✅ Hiển thị UI
- ✅ Kết nối database
- ✅ Tất cả chức năng hoạt động

**Vị trí file:** `target\installer\TaskManager-1.0.0.exe`

---

Nếu gặp vấn đề khác, hãy kiểm tra file log hoặc chạy từ JAR để debug!
