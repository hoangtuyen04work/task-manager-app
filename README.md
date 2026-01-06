# Task Manager - Ứng dụng Quản lý Công việc Hàng ngày

Ứng dụng desktop quản lý task hàng ngày được xây dựng bằng JavaFX 21 và MySQL Database. Ứng dụng giúp bạn tổ chức công việc, theo dõi tiến độ, đánh giá hiệu suất làm việc và xem thống kê trực quan.

## 🌟 Tính năng chính

### 1. Quản lý Tasks (CRUD)
- ✅ Thêm, sửa, xóa tasks
- ✅ Đánh dấu hoàn thành/chưa hoàn thành
- ✅ Phân loại theo độ ưu tiên (Cao, Trung bình, Thấp)
- ✅ Lọc theo trạng thái và độ ưu tiên
- ✅ Tìm kiếm theo tên hoặc mô tả
- ✅ Hiển thị thống kê nhanh (tổng số, hoàn thành, chưa hoàn thành, tỷ lệ %)

### 2. Copy Tasks
- ✅ Copy tasks từ ngày này sang ngày khác
- ✅ Chọn tasks cần copy (chỉ hiển thị tasks chưa hoàn thành)
- ✅ Tasks được copy tự động có trạng thái "chưa hoàn thành"

### 3. Đánh giá cuối ngày (Daily Review)
- ✅ Tự động tính toán thống kê (tổng tasks, hoàn thành, tỷ lệ %)
- ✅ Đánh giá bằng sao (1-5 sao)
- ✅ Ghi chú cảm nhận về ngày làm việc
- ✅ Xem danh sách tasks của ngày đó

### 4. Thống kê (Statistics)
- ✅ Tổng quan: Tổng số tasks, hoàn thành, pending, tỷ lệ trung bình
- ✅ Thống kê theo độ ưu tiên
- ✅ Biểu đồ cột: Số lượng tasks theo ngày
- ✅ Biểu đồ đường: Tỷ lệ hoàn thành theo ngày
- ✅ Biểu đồ tròn: Phân bố theo độ ưu tiên
- ✅ Bộ lọc thời gian: 7 ngày, 30 ngày, tháng này, hoặc tùy chỉnh

### 5. Lịch sử công việc (Calendar History) 🆕
- ✅ Hiển thị lịch theo tháng với màu sắc biểu thị mức độ hoàn thành
- ✅ Màu sắc theo số lượng task hoàn thành:
  - **Xám nhạt**: Không có task
  - **Đỏ nhạt**: Có task nhưng chưa hoàn thành
  - **Xanh nhạt**: 1-2 task hoàn thành
  - **Xanh vừa**: 3-5 task hoàn thành
  - **Xanh đậm**: 6+ task hoàn thành
- ✅ Click vào ngày để xem chi tiết tasks và ghi chú
- ✅ Điều hướng giữa các tháng (Tháng trước / Tháng sau / Hôm nay)
- ✅ Hiển thị tỷ lệ hoàn thành và danh sách task cho ngày được chọn

## 📋 Yêu cầu hệ thống

### Phần mềm cần cài đặt:

1. **JDK 17 hoặc cao hơn (JDK 21 khuyến nghị)**
   - Download JDK 21: https://www.oracle.com/java/technologies/downloads/#java21
   - Hoặc JDK 17: https://www.oracle.com/java/technologies/downloads/#java17
   - Hoặc OpenJDK: https://adoptium.net/
   - Sau khi cài, kiểm tra: `java -version`

2. **MySQL Server 8.0+**
   - Download: https://dev.mysql.com/downloads/mysql/
   - Hoặc XAMPP/WAMP (đã bao gồm MySQL)
   - MySQL Workbench (khuyến nghị): https://dev.mysql.com/downloads/workbench/

3. **Maven 3.6+**
   - Download: https://maven.apache.org/download.cgi
   - Hoặc sử dụng Maven wrapper có sẵn trong project
   - Kiểm tra: `mvn -version`

4. **IntelliJ IDEA** (khuyến nghị)
   - Community Edition (miễn phí): https://www.jetbrains.com/idea/download/
   - Hoặc Ultimate Edition

## 🚀 Hướng dẫn cài đặt

### Bước 1: Clone repository

```bash
git clone https://github.com/ct070261/task-manager-app.git
cd task-manager-app
```

### Bước 2: Cài đặt Database

1. **Khởi động MySQL Server**
   - Windows: Mở MySQL Workbench hoặc từ Services
   - Mac/Linux: `sudo service mysql start`

2. **Tạo Database**
   - Mở MySQL Workbench
   - Kết nối đến MySQL Server (localhost:3306)
   - Mở file `database/schema.sql`
   - Chạy toàn bộ script (Click Execute ⚡ hoặc Ctrl+Shift+Enter)

3. **Kiểm tra**
   - Refresh schema list, bạn sẽ thấy database `task_manager_db`
   - Expand database, kiểm tra 2 bảng: `tasks` và `daily_reviews`
   - Kiểm tra dữ liệu mẫu:
     ```sql
     SELECT * FROM tasks;
     SELECT * FROM daily_reviews;
     ```

### Bước 3: Cấu hình kết nối Database

1. Mở file: `src/main/resources/application.properties`

2. Điều chỉnh thông tin kết nối MySQL của bạn:
   ```properties
   db.url=jdbc:mysql://localhost:3306/task_manager_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
   db.username=root
   db.password=your_password_here
   db.driver=com.mysql.cj.jdbc.Driver
   ```

3. Thay `your_password_here` bằng mật khẩu MySQL của bạn (nếu không có mật khẩu, để trống)

### Bước 4: Build và Run ứng dụng

#### Cách 1: Sử dụng Maven Command Line

```bash
# Build project
mvn clean install

# Run application
mvn javafx:run
```

#### Cách 2: Sử dụng IntelliJ IDEA (Khuyến nghị)

1. **Mở project trong IntelliJ IDEA**
   - File → Open → Chọn thư mục `task-manager-app`
   - IntelliJ sẽ tự động nhận diện Maven project

2. **Đợi Maven download dependencies**
   - Ở góc phải dưới, xem progress bar
   - Có thể mất vài phút lần đầu tiên

3. **Chạy ứng dụng**
   - Mở file `src/main/java/com/taskmanager/Main.java`
   - Click chuột phải → Run 'Main.main()'
   - Hoặc nhấn phím `Shift + F10`

4. **Nếu gặp lỗi JavaFX**
   - File → Project Structure → Project → SDK: Chọn JDK 21
   - File → Settings → Build, Execution, Deployment → Build Tools → Maven
   - Đảm bảo Maven JDK đang dùng là JDK 21

## 📖 Hướng dẫn sử dụng

### 1. Quản lý Tasks

#### Thêm Task mới
1. Click nút **"Thêm Task"** trên toolbar
2. Điền thông tin:
   - **Tên công việc** (bắt buộc, tối đa 200 ký tự)
   - **Mô tả** (tùy chọn, tối đa 1000 ký tự)
   - **Ngày** (mặc định: hôm nay)
   - **Độ ưu tiên**: Cao / Trung bình / Thấp
3. Click **"Lưu"**

#### Sửa Task
- Double-click vào task trong bảng
- Hoặc click nút **"Sửa"** ở cột Thao tác
- Hoặc click chuột phải → **"Sửa"**

#### Xóa Task
- Click nút **"Xóa"** ở cột Thao tác
- Hoặc click chuột phải → **"Xóa"**
- Xác nhận xóa trong dialog

#### Đánh dấu hoàn thành
- Click vào checkbox ở đầu mỗi dòng
- Hoặc click chuột phải → **"Đánh dấu hoàn thành/chưa hoàn thành"**

#### Lọc và Tìm kiếm
- **Chọn ngày**: Sử dụng DatePicker bên trái
- **Lọc theo trạng thái**: Tất cả / Đã hoàn thành / Chưa hoàn thành
- **Lọc theo độ ưu tiên**: Check/uncheck Cao, Trung bình, Thấp
- **Tìm kiếm**: Nhập từ khóa trong ô tìm kiếm (tìm theo tên hoặc mô tả)

### 2. Copy Tasks

1. Click nút **"Copy Tasks"** trên toolbar
2. Chọn **Ngày nguồn** (ngày cần copy tasks)
3. Chọn **Ngày đích** (ngày muốn copy đến)
4. Danh sách sẽ hiển thị các tasks chưa hoàn thành từ ngày nguồn
5. Chọn các tasks cần copy (Ctrl+Click để chọn nhiều)
6. Click **OK**
7. Tasks được copy sẽ có trạng thái "chưa hoàn thành"

### 3. Đánh giá cuối ngày

1. Click nút **"Đánh giá cuối ngày"** trên toolbar
2. Chọn ngày cần đánh giá (mặc định: hôm nay)
3. Xem thống kê tự động:
   - Tổng số tasks
   - Đã hoàn thành
   - Tỷ lệ hoàn thành
4. Đánh giá:
   - Click vào các ngôi sao (1-5 sao)
   - Nhập ghi chú, cảm nhận (tối đa 2000 ký tự)
5. Xem danh sách tasks trong ngày
6. Click **"Lưu"**

**Lưu ý:** Mỗi ngày chỉ có 1 đánh giá. Nếu đã có đánh giá, form sẽ load dữ liệu cũ để bạn chỉnh sửa.

### 4. Xem Lịch sử Công việc

1. Click nút **"Lịch sử"** trên toolbar hoặc chọn menu **Xem → Lịch sử công việc** (Ctrl+H)
2. Xem lịch theo tháng với màu sắc:
   - **Xám nhạt (#ecf0f1)**: Không có task nào
   - **Đỏ nhạt (#fadbd8)**: Có task nhưng chưa hoàn thành task nào
   - **Xanh nhạt (#d5f4e6)**: Hoàn thành 1-2 task
   - **Xanh vừa (#82e0aa)**: Hoàn thành 3-5 task
   - **Xanh đậm (#27ae60)**: Hoàn thành 6+ task
3. Click vào bất kỳ ngày nào để xem:
   - Danh sách tasks trong ngày đó
   - Ghi chú đánh giá cuối ngày (nếu có)
   - Thống kê: Tổng số tasks, hoàn thành, tỷ lệ %
4. Điều hướng:
   - Click **"◀ Tháng trước"** để xem tháng trước
   - Click **"Tháng sau ▶"** để xem tháng sau
   - Click **"Hôm nay"** để quay về tháng hiện tại
5. Ngày hôm nay được đánh dấu viền đỏ, ngày được chọn có viền xanh

**Tip:** Dùng lịch sử để review lại các ngày làm việc hiệu quả và học hỏi từ các ngày làm việc kém hiệu quả.

### 5. Xem Thống kê

1. Click nút **"Thống kê"** trên toolbar
2. Chọn khoảng thời gian:
   - **7 ngày qua** (mặc định)
   - **30 ngày qua**
   - **Tháng này**
   - **Tùy chỉnh**: Chọn "Từ ngày" và "Đến ngày"
3. Xem **Tab Tổng quan**:
   - Tổng số tasks, hoàn thành, pending, tỷ lệ trung bình
   - Phân bổ theo độ ưu tiên
4. Xem **Tab Biểu đồ**:
   - Biểu đồ cột: Số lượng tasks theo ngày
   - Biểu đồ đường: Tỷ lệ hoàn thành theo ngày
   - Biểu đồ tròn: Phân bố theo độ ưu tiên
5. Click **"Làm mới"** để cập nhật dữ liệu

## 🎨 Giao diện

### Màn hình chính
- **Toolbar**: Các nút thao tác nhanh
- **Panel trái**: DatePicker, bộ lọc, tìm kiếm
- **Panel giữa**: Bảng danh sách tasks
- **Panel phải**: Thống kê nhanh cho ngày được chọn
- **Status bar**: Thông tin trạng thái và ngày

### Màu sắc
- **Cao (High)**: 🔴 Đỏ (#e74c3c)
- **Trung bình (Medium)**: 🟡 Cam (#f39c12)
- **Thấp (Low)**: 🟢 Xanh (#2ecc71)
- **Đã hoàn thành**: Màu xám, có gạch ngang

## 🛠️ Troubleshooting

### Lỗi: "Không thể kết nối đến MySQL database"

**Nguyên nhân:**
- MySQL server chưa khởi động
- Database chưa được tạo
- Thông tin kết nối trong `application.properties` sai

**Giải pháp:**
1. Kiểm tra MySQL server đã chạy chưa
2. Kiểm tra database `task_manager_db` đã tồn tại
3. Kiểm tra username/password trong `application.properties`
4. Thử test connection bằng MySQL Workbench với cùng thông tin

### Lỗi: "Error: JavaFX runtime components are missing"

**Nguyên nhân:**
- JDK không bao gồm JavaFX
- JavaFX dependencies chưa được download

**Giải pháp:**
1. Đảm bảo đang dùng JDK 17 hoặc cao hơn (không phải JRE)
2. Chạy `mvn clean install` để download dependencies
3. Nếu dùng IntelliJ, đảm bảo Maven đã import xong
4. Run bằng Maven: `mvn javafx:run`

### Lỗi: Maven không tìm thấy dependencies

**Giải pháp:**
1. Kiểm tra kết nối internet
2. Xóa `.m2` cache: Xóa thư mục `~/.m2/repository`
3. Chạy lại: `mvn clean install -U`
4. Nếu vẫn lỗi, thử dùng mirror Maven khác

### Lỗi: "Table doesn't exist"

**Giải pháp:**
1. Kiểm tra đã chạy `database/schema.sql` chưa
2. Kiểm tra kết nối đúng database: `task_manager_db`
3. Trong MySQL Workbench, chạy:
   ```sql
   USE task_manager_db;
   SHOW TABLES;
   ```
4. Nếu không có tables, chạy lại `schema.sql`

### Ứng dụng chạy chậm

**Giải pháp:**
1. Kiểm tra MySQL server đang chạy local (không remote)
2. Tăng heap size cho JVM: Thêm `-Xmx512m` vào VM options
3. Giảm số lượng tasks trong database nếu quá nhiều (>1000)

## 🏗️ Cấu trúc dự án

```
task-manager-app/
├── pom.xml                          # Maven configuration
├── README.md                        # File này
├── .gitignore                       # Git ignore rules
├── database/
│   └── schema.sql                   # Database schema và sample data
├── src/main/java/com/taskmanager/
│   ├── Main.java                    # Entry point
│   ├── controller/
│   │   ├── MainController.java      # Main window controller
│   │   ├── TaskController.java      # Task form controller
│   │   ├── DailyReviewController.java
│   │   └── StatisticsController.java
│   ├── model/
│   │   ├── Task.java                # Task model với Priority enum
│   │   ├── DailyReview.java         # Daily review model
│   │   └── TaskStatistics.java      # Statistics model
│   ├── dao/
│   │   ├── DatabaseConnection.java  # Singleton DB connection
│   │   ├── TaskDAO.java             # Task data access
│   │   └── DailyReviewDAO.java      # Review data access
│   └── util/
│       ├── DateUtil.java            # Date utilities
│       └── AlertUtil.java           # Alert/Dialog utilities
└── src/main/resources/
    ├── fxml/
    │   ├── main.fxml                # Main window UI
    │   ├── task-form.fxml           # Task form UI
    │   ├── daily-review.fxml        # Review dialog UI
    │   └── statistics.fxml          # Statistics window UI
    ├── css/
    │   └── style.css                # Application stylesheet
    └── application.properties       # Database configuration
```

## 🔧 Công nghệ sử dụng

- **JavaFX 21**: Framework UI cho ứng dụng desktop
- **JDK 17+**: Java Development Kit (tương thích với JDK 17, 18, 19, 20, 21)
- **MySQL 8.0**: Relational database
- **Maven**: Build tool và dependency management
- **JDBC**: Java Database Connectivity

## 📝 Database Schema

### Bảng `tasks`
- `id`: BIGINT (Primary Key, Auto Increment)
- `title`: VARCHAR(200) - Tên công việc
- `description`: TEXT - Mô tả chi tiết
- `task_date`: DATE - Ngày thực hiện
- `priority`: ENUM('HIGH', 'MEDIUM', 'LOW') - Độ ưu tiên
- `completed`: BOOLEAN - Trạng thái hoàn thành
- `created_at`: TIMESTAMP - Ngày tạo
- `updated_at`: TIMESTAMP - Ngày cập nhật

### Bảng `daily_reviews`
- `id`: BIGINT (Primary Key, Auto Increment)
- `review_date`: DATE (Unique) - Ngày đánh giá
- `total_tasks`: INT - Tổng số tasks
- `completed_tasks`: INT - Số tasks hoàn thành
- `completion_rate`: DECIMAL(5,2) - Tỷ lệ hoàn thành (%)
- `rating`: INT (1-5) - Đánh giá sao
- `notes`: TEXT - Ghi chú
- `created_at`: TIMESTAMP - Ngày tạo
- `updated_at`: TIMESTAMP - Ngày cập nhật

## 🎯 Tips sử dụng hiệu quả

1. **Tạo tasks vào đầu ngày**: Lên kế hoạch cho ngày mới
2. **Sử dụng độ ưu tiên**: Làm tasks "Cao" trước
3. **Copy tasks chưa xong**: Đừng quên tasks của hôm qua
4. **Đánh giá cuối ngày**: Review và rút kinh nghiệm
5. **Xem thống kê định kỳ**: Theo dõi hiệu suất hàng tuần/tháng
6. **Dùng lịch sử để học hỏi**: Xem lại các ngày làm việc hiệu quả (màu xanh đậm) và tìm hiểu lý do thành công

## 👨‍💻 Phát triển thêm

Nếu muốn mở rộng ứng dụng, có thể thêm:
- Export/Import tasks (CSV, Excel)
- Notifications/Reminders
- Dark mode
- Task categories/tags
- Multiple users
- Cloud sync
- Mobile app

## 📄 License

MIT License - Tự do sử dụng và chỉnh sửa

## 🤝 Đóng góp

Contributions are welcome! Feel free to:
- Report bugs
- Suggest features
- Submit pull requests

## 📧 Liên hệ

Nếu có thắc mắc hoặc cần hỗ trợ, vui lòng tạo issue trên GitHub.

---

**Chúc bạn sử dụng ứng dụng hiệu quả! 🚀**