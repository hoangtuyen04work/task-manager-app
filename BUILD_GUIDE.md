# 🚀 HƯỚNG DẪN BUILD FILE EXE - TASK MANAGER

## 📋 TÓM TẮT

Dự án này có thể build thành **3 dạng** để chạy:

1. **File JAR** - Chạy trực tiếp bằng Java (nhanh, đơn giản)
2. **File EXE Installer** - Cài đặt như app Windows chuyên nghiệp (khuyến nghị)
3. **Portable EXE** - File exe độc lập không cần cài đặt

---

## ⚡ CÁCH 1: BUILD FILE JAR (NHANH NHẤT)

### Bước 1: Build JAR
```bash
mvn clean package
```

### Bước 2: Chạy ứng dụng
**Cách A - Dùng script:**
```bash
run-app.bat
```

**Cách B - Dùng lệnh:**
```bash
java -jar target/task-manager-app-1.0.0-jar-with-dependencies.jar
```

### ✅ Ưu điểm:
- Nhanh, đơn giản
- Không cần cài đặt
- Dễ debug

### ❌ Nhược điểm:
- Cần cài Java trên máy
- Không có icon, shortcut
- Không chuyên nghiệp

---

## 🎯 CÁCH 2: BUILD FILE EXE INSTALLER (KHUYẾN NGHỊ)

### Bước 1: Build EXE bằng script tự động
```bash
build-exe.bat
```

Hoặc chạy thủ công:
```bash
mvn compile package -DskipTests
mvn jpackage:jpackage
```
⚠️ **Lưu ý:** Không dùng `mvn clean` vì có thể gây lỗi nếu file cũ đang bị lock!

### Bước 2: Lấy file installer
File được tạo tại:
```
target/installer/TaskManager-1.0.0.exe
```

### Bước 3: Cài đặt
- Double-click vào `TaskManager-1.0.0.exe`
- Làm theo hướng dẫn cài đặt
- App sẽ được cài vào `C:\Program Files\TaskManager`

### ✅ Ưu điểm:
- Chuyên nghiệp, có icon và shortcut
- Tích hợp Start Menu
- Tự động update
- Không cần Java (đã tích hợp JRE)

### ❌ Nhược điểm:
- Build lâu hơn (~1 phút)
- File lớn (~70MB)

---

## 📦 CÁC FILE SCRIPT HỖ TRỢ

### 1. `build-exe.bat`
**Công dụng:** Build toàn bộ project thành file EXE installer
```bash
build-exe.bat
```

### 2. `run-app.bat`
**Công dụng:** Chạy nhanh app từ file JAR (không cần cài đặt)
```bash
run-app.bat
```

### 3. `setup-database.bat`
**Công dụng:** Tự động tạo database và import schema
```bash
setup-database.bat
```
⚠️ **Lưu ý:** Cần sửa username/password MySQL trong file trước khi chạy

---

## 🗄️ SETUP DATABASE

### Cách 1: Tự động
1. Sửa thông tin MySQL trong `setup-database.bat`
2. Chạy script:
   ```bash
   setup-database.bat
   ```

### Cách 2: Thủ công
1. Mở MySQL Workbench/phpMyAdmin
2. Chạy file `database/schema.sql`

### Cách 3: Command line
```bash
mysql -u root -p < database/schema.sql
```

---

## ⚙️ CẤU HÌNH KẾT NỐI DATABASE

Sửa file: `src/main/resources/application.properties`

```properties
db.url=jdbc:mysql://localhost:3306/task_manager_db
db.username=root
db.password=your_password
```

---

## 🔧 YÊU CẦU HỆ THỐNG

### Để Build:
- ✅ Java JDK 21
- ✅ Maven 3.6+
- ✅ WiX Toolset 3.x (để build EXE trên Windows)

### Để Chạy (sau khi build EXE):
- ✅ Windows 10/11 (64-bit)
- ✅ MySQL Server 8.0+
- ❌ Không cần cài Java (đã tích hợp)

---

## 📂 CẤU TRÚC THƯ MỤC SAU KHI BUILD

```
task-manager-app/
├── target/
│   ├── task-manager-app-1.0.0.jar                    # JAR file cơ bản
│   ├── task-manager-app-1.0.0-jar-with-dependencies.jar  # JAR đầy đủ
│   └── installer/
│       └── TaskManager-1.0.0.exe                     # File installer
├── build-exe.bat                                      # Script build EXE
├── run-app.bat                                        # Script chạy JAR
├── setup-database.bat                                 # Script setup DB
└── INSTALLATION_GUIDE.md                              # Hướng dẫn cài đặt
```

---

## 🎓 HƯỚNG DẪN CHI TIẾT TỪNG BƯỚC

### Lần đầu tiên build:

```bash
# Bước 1: Setup database
setup-database.bat

# Bước 2: Build EXE
build-exe.bat

# Bước 3: Cài đặt
# - Vào target/installer/
# - Chạy TaskManager-1.0.0.exe
# - Làm theo hướng dẫn
```

### Lần sau muốn build lại:

```bash
# Nếu có thay đổi code:
build-exe.bat

# Nếu chỉ muốn test nhanh:
run-app.bat
```

---

## ❓ XỬ LÝ LỖI THƯỜNG GẶP

### Lỗi 1: "Cannot connect to database"
**Giải pháp:**
1. Kiểm tra MySQL đã chạy chưa
2. Chạy `setup-database.bat`
3. Kiểm tra `application.properties`

### Lỗi 2: "Failed to delete target\dist" hoặc "target\installer"
**Giải pháp:**
```bash
# Không dùng mvn clean, thay vào đó:
mvn compile package -DskipTests
mvn jpackage:jpackage

# Hoặc dùng script:
build-exe.bat
```

### Lỗi 3: "BUILD FAILURE - jpackage not found"
**Giải pháp:**
- Đảm bảo đang dùng JDK (không phải JRE)
- Kiểm tra: `java -version` phải hiện "Java(TM) SE Runtime Environment"

### Lỗi 4: "WiX Toolset required"
**Giải pháp:**
- Download WiX Toolset: https://wixtoolset.org/
- Cài đặt và thêm vào PATH

### Lỗi 5: "App đóng ngay lập tức"
**Giải pháp:**
- File EXE mới đã được fix vấn đề này
- Đảm bảo dùng file từ `target/installer/` (không phải `target/dist/`)
- Xem chi tiết: [FIX_EXE_CRASH.md](FIX_EXE_CRASH.md)

### Lỗi 6: File JAR không chạy
**Giải pháp:**
```bash
# Xem log lỗi chi tiết:
java -jar target/task-manager-app-1.0.0-jar-with-dependencies.jar
```

---

## 📊 SO SÁNH CÁC PHƯƠNG ÁN BUILD

| Tiêu chí | JAR | EXE Installer | Portable EXE |
|----------|-----|---------------|--------------|
| Tốc độ build | ⚡⚡⚡ Nhanh | ⚡ Chậm | ⚡⚡ Trung bình |
| Kích thước | 📦 ~20MB | 📦 ~70MB | 📦 ~70MB |
| Cài đặt | ❌ Không cần | ✅ Cần cài | ❌ Không cần |
| Cần Java | ✅ Cần JDK 21 | ❌ Tích hợp sẵn | ❌ Tích hợp sẵn |
| Icon/Shortcut | ❌ Không | ✅ Có | ❌ Không |
| Chuyên nghiệp | ⭐⭐ | ⭐⭐⭐⭐⭐ | ⭐⭐⭐ |

---

## 🎯 KHUYẾN NGHỊ

### Cho Developer (đang phát triển):
→ Dùng **JAR + run-app.bat** để test nhanh

### Cho End-user (người dùng cuối):
→ Dùng **EXE Installer** để cài đặt chuyên nghiệp

### Cho Demo/Portable:
→ Dùng **EXE Installer** rồi copy thư mục cài đặt

---

## 📞 LIÊN HỆ HỖ TRỢ

Nếu gặp vấn đề, vui lòng cung cấp:
1. Thông báo lỗi chi tiết
2. Phiên bản Java: `java -version`
3. Phiên bản Maven: `mvn -version`
4. Hệ điều hành Windows version

---

## ✅ CHECKLIST BUILD THÀNH CÔNG

- [ ] Java JDK 21 đã cài đặt
- [ ] Maven đã cài đặt
- [ ] WiX Toolset đã cài đặt (cho Windows EXE)
- [ ] MySQL Server đã khởi động
- [ ] Database đã được tạo (chạy setup-database.bat)
- [ ] Build thành công: `mvn clean package`
- [ ] File JAR chạy được: `run-app.bat`
- [ ] File EXE được tạo: `build-exe.bat`
- [ ] Cài đặt thành công từ EXE
- [ ] App kết nối database OK

---

**🎉 Chúc bạn build thành công!**
