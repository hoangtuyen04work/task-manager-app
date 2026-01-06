# 📦 HƯỚNG DẪN CÀI ĐẶT TASK MANAGER

## 🎯 YÊU CẦU HỆ THỐNG

- **Hệ điều hành:** Windows 10/11 (64-bit)
- **RAM:** Tối thiểu 4GB
- **Ổ đĩa:** 200MB dung lượng trống
- **MySQL Server:** Phiên bản 8.0 trở lên (hoặc XAMPP/WAMP)

---

## 📥 BƯỚC 1: CÀI ĐẶT ỨNG DỤNG

1. **Tải file cài đặt:**
   - File: `TaskManager-1.0.0.exe`
   - Vị trí: `D:\PTPMUD\task-manager-app\target\dist\TaskManager-1.0.0.exe`

2. **Chạy file installer:**
   - Double-click vào `TaskManager-1.0.0.exe`
   - Nếu Windows hiện cảnh báo bảo mật:
     - Click **"More info"**
     - Click **"Run anyway"**

3. **Làm theo hướng dẫn cài đặt:**
   - ✅ Chọn thư mục cài đặt (mặc định: `C:\Program Files\TaskManager`)
   - ✅ Tích chọn **"Create shortcuts in Start Menu"**
   - ✅ Tích chọn **"Create shortcut on Desktop"**
   - Click **"Install"**

---

## 🗄️ BƯỚC 2: CÀI ĐẶT DATABASE

### **Cách 1: Tự động (Khuyến nghị)**

1. **Khởi động MySQL Server:**
   - Nếu dùng XAMPP: Mở XAMPP Control Panel → Start "MySQL"
   - Nếu dùng MySQL độc lập: Đảm bảo MySQL Service đang chạy

2. **Chỉnh sửa thông tin kết nối:**
   - Mở file: `setup-database.bat` bằng Notepad
   - Sửa các dòng sau theo cấu hình MySQL của bạn:
     ```batch
     set MYSQL_USER=root
     set MYSQL_PASSWORD=your_password_here
     ```
   - Lưu file

3. **Chạy script setup:**
   - Double-click vào `setup-database.bat`
   - Đợi cho đến khi thấy thông báo **"✓ SETUP DATABASE THÀNH CÔNG!"**

### **Cách 2: Thủ công**

1. Mở MySQL Workbench hoặc phpMyAdmin

2. Chạy các lệnh SQL sau:
   ```sql
   CREATE DATABASE task_manager_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```

3. Import file `database/schema.sql` vào database `task_manager_db`

---

## ⚙️ BƯỚC 3: CẤU HÌNH KẾT NỐI DATABASE

1. **Tìm file cấu hình:**
   - Sau khi cài đặt, file nằm trong thư mục cài đặt
   - Mặc định: `C:\Program Files\TaskManager\app\application.properties`

2. **Chỉnh sửa thông tin kết nối:**
   
   Mở file `application.properties` bằng Notepad và sửa:
   ```properties
   db.url=jdbc:mysql://localhost:3306/task_manager_db
   db.username=root
   db.password=your_password_here
   ```

3. **Lưu file**

---

## 🚀 BƯỚC 4: CHẠY ỨNG DỤNG

### **Cách 1: Từ Desktop**
- Double-click vào shortcut **"Task Manager"** trên Desktop

### **Cách 2: Từ Start Menu**
- Nhấn **Windows Key** → Gõ "Task Manager" → Enter

### **Cách 3: Từ thư mục cài đặt**
- Vào `C:\Program Files\TaskManager`
- Chạy file `TaskManager.exe`

---

## ✅ KIỂM TRA CÀI ĐẶT

Nếu mọi thứ hoạt động đúng:
- ✅ Ứng dụng mở và hiển thị giao diện chính
- ✅ Bạn có thể thêm, sửa, xóa task
- ✅ Dữ liệu được lưu vào database MySQL

---

## ❌ XỬ LÝ LỖI THƯỜNG GẶP

### **Lỗi 1: "Cannot connect to database"**
**Nguyên nhân:** MySQL server chưa khởi động hoặc thông tin kết nối sai

**Giải pháp:**
1. Kiểm tra MySQL Server đang chạy
2. Kiểm tra lại username/password trong `application.properties`
3. Kiểm tra database `task_manager_db` đã được tạo chưa

### **Lỗi 2: "Main class not found"**
**Nguyên nhân:** Java runtime bị lỗi trong installer

**Giải pháp:**
1. Gỡ cài đặt ứng dụng
2. Build lại file EXE với lệnh: `mvn clean package jpackage:jpackage`
3. Cài đặt lại

### **Lỗi 3: "Access denied"**
**Nguyên nhân:** MySQL user không có quyền truy cập

**Giải pháp:**
Chạy lệnh SQL sau trong MySQL:
```sql
GRANT ALL PRIVILEGES ON task_manager_db.* TO 'root'@'localhost';
FLUSH PRIVILEGES;
```

---

## 📞 HỖ TRỢ

Nếu gặp vấn đề, vui lòng kiểm tra:
1. MySQL Server đã khởi động
2. Database `task_manager_db` đã được tạo
3. Thông tin kết nối trong `application.properties` đúng
4. Port 3306 không bị chặn bởi Firewall

---

## 🎉 CHÚC MỪNG!

Bạn đã cài đặt thành công Task Manager!
Bắt đầu quản lý công việc của bạn ngay bây giờ! 🚀
