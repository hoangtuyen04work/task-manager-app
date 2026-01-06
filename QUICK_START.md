# ⚡ QUICK START - BUILD FILE EXE

## 🚀 LỆNH BUILD ĐÚNG (Đã Fix Lỗi)

### ❌ **KHÔNG DÙNG** (gây lỗi file lock):
```bash
mvn clean package jpackage:jpackage
```

### ✅ **DÙNG LỆNH NÀY:**

**Cách 1 - Script tự động (Khuyến nghị):**
```bash
build-exe.bat
```

**Cách 2 - Lệnh thủ công:**
```bash
mvn compile package -DskipTests
mvn jpackage:jpackage
```

---

## 📍 VỊ TRÍ FILE EXE

```
target/installer/TaskManager-1.0.0.exe
```

**Lưu ý:** Không phải `target/dist/` nữa!

---

## 🔧 TẠI SAO KHÔNG DÙNG `mvn clean`?

Lệnh `mvn clean` sẽ cố xóa toàn bộ thư mục `target/`, bao gồm:
- `target/dist/` (nếu có file cũ)
- `target/installer/` (nếu có file cũ)

Nếu file `.exe` đang được Windows Explorer mở hoặc đang chạy, nó sẽ bị **lock** và không thể xóa → **BUILD FAILURE**

**Giải pháp:** 
- Dùng `mvn compile package` (không xóa target)
- Script `build-exe.bat` đã được fix để tự động xử lý vấn đề này

---

## 📝 CÁC LỆNH THƯỜNG DÙNG

### Build lần đầu hoặc sau khi sửa code:
```bash
build-exe.bat
```

### Test nhanh không cần build EXE:
```bash
run-app.bat
```

### Build thủ công từng bước:
```bash
# Bước 1: Build JAR
mvn compile package -DskipTests

# Bước 2: Tạo EXE
mvn jpackage:jpackage

# File tạo tại: target/installer/TaskManager-1.0.0.exe
```

---

## ✅ CHECKLIST TRƯỚC KHI BUILD

- [ ] Đóng tất cả TaskManager.exe đang chạy
- [ ] Đóng Windows Explorer nếu đang mở thư mục `target/`
- [ ] Có Internet (Maven tải dependencies)
- [ ] MySQL đang chạy (để test sau khi build)

---

## 🎯 SAU KHI BUILD XONG

1. **File EXE ở:** `target/installer/TaskManager-1.0.0.exe`

2. **Cài đặt:**
   - Double-click file EXE
   - Nếu Windows cảnh báo → "More info" → "Run anyway"
   - Làm theo hướng dẫn

3. **Setup Database:**
   ```bash
   setup-database.bat
   ```
   (Nhớ sửa password MySQL trong file trước khi chạy)

4. **Chạy app:**
   - Từ Desktop shortcut
   - Hoặc Start Menu

---

## 🆘 NẾU GẶP LỖI

### Lỗi: "Failed to delete target\dist"
**Giải pháp:**
```bash
# Đừng dùng mvn clean, dùng:
mvn compile package -DskipTests
mvn jpackage:jpackage
```

### Lỗi: "App đóng ngay lập tức"
**Nguyên nhân:** File EXE cũ từ `target/dist/` (trước khi fix)

**Giải pháp:** 
- Dùng file MỚI từ `target/installer/`
- File mới đã fix JavaFX modules

### Lỗi khác:
Xem [BUILD_GUIDE.md](BUILD_GUIDE.md) hoặc [FIX_EXE_CRASH.md](FIX_EXE_CRASH.md)

---

**🎉 Chúc bạn build thành công!**
