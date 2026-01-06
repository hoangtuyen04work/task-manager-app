# Lịch Sử Công Việc - Calendar History Feature

## Tổng quan

Tính năng lịch sử công việc cho phép người dùng xem toàn bộ công việc đã làm trong các ngày trước đó thông qua giao diện lịch trực quan với màu sắc biểu thị mức độ hoàn thành.

## Giao diện

### Màn hình chính

```
┌─────────────────────────────────────────────────────────────────┐
│ Lịch Sử Công Việc                                               │
│ ◀ Tháng trước    [Tháng 12 2024]    Tháng sau ▶    [Hôm nay]   │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  ┌────────────────────────────┬──────────────────────────────┐  │
│  │ Lịch                       │ Ngày: 26/12/2024             │  │
│  │                            │ Tổng: 5 | Hoàn thành: 4 | 80%│  │
│  │  T2  T3  T4  T5  T6  T7 CN │                              │  │
│  │  ──  ──  ──  ──  ──   1  2 │ Danh sách công việc:         │  │
│  │   3   4   5   6   7   8  9 │ ✓ 🔴 Học JavaFX              │  │
│  │  10  11  12  13  14  15 16 │ ✓ 🔴 Setup MySQL             │  │
│  │  17  18  19  20  21  22 23 │ ○ 🟡 Đọc tài liệu           │  │
│  │  24  25 [26] 27  28  29 30 │ ○ 🟢 Viết unit tests        │  │
│  │  31                        │ ✓ 🔴 Meeting với team       │  │
│  │                            │                              │  │
│  │ Chú thích:                 │ Ghi chú:                     │  │
│  │ □ Không có task            │ Ngày làm việc khá hiệu quả.  │  │
│  │ □ Có task chưa làm         │ Hoàn thành được 4/5 tasks.   │  │
│  │ □ 1-2 task hoàn thành      │ Task còn lại sẽ tiếp tục...  │  │
│  │ □ 3-5 task hoàn thành      │                              │  │
│  │ □ 6+ task hoàn thành       │                              │  │
│  └────────────────────────────┴──────────────────────────────┘  │
└─────────────────────────────────────────────────────────────────┘
```

## Màu sắc

### Bảng màu theo mức độ hoàn thành

| Trạng thái | Màu | Hex Code | Mô tả |
|------------|-----|----------|-------|
| Không có task | Xám nhạt | #ecf0f1 | Ngày không có công việc nào |
| Có task chưa làm | Đỏ nhạt | #fadbd8 | Có task nhưng chưa hoàn thành |
| 1-2 task hoàn thành | Xanh nhạt | #d5f4e6 | Ít task hoàn thành |
| 3-5 task hoàn thành | Xanh vừa | #82e0aa | Trung bình task hoàn thành |
| 6+ task hoàn thành | Xanh đậm | #27ae60 | Nhiều task hoàn thành |
| Ngày hôm nay | Viền đỏ | #e74c3c | Đánh dấu ngày hiện tại |
| Ngày được chọn | Viền xanh | #3498db | Đánh dấu ngày đang xem |

## Chức năng

### 1. Xem lịch theo tháng
- Hiển thị toàn bộ ngày trong tháng
- Mỗi ô ngày hiển thị:
  - Số ngày
  - Số lượng task (hoàn thành/tổng)
  - Màu nền theo mức độ hoàn thành

### 2. Điều hướng
- **◀ Tháng trước**: Xem tháng trước
- **Tháng sau ▶**: Xem tháng sau
- **Hôm nay**: Quay về tháng hiện tại

### 3. Xem chi tiết ngày
Click vào bất kỳ ngày nào để xem:
- Danh sách đầy đủ các task trong ngày
- Trạng thái hoàn thành của từng task
- Độ ưu tiên (Cao 🔴, Trung bình 🟡, Thấp 🟢)
- Ghi chú đánh giá cuối ngày (nếu có)
- Thống kê: Tổng số task, đã hoàn thành, tỷ lệ %

### 4. Chú thích màu sắc
Panel chú thích ở dưới lịch giải thích ý nghĩa của từng màu sắc

## Cách sử dụng

### Mở lịch sử
1. Click nút "Lịch sử" trên toolbar
2. Hoặc chọn menu: Xem → Lịch sử công việc
3. Hoặc nhấn phím tắt: **Ctrl+H**

### Xem chi tiết
1. Click vào ngày muốn xem
2. Panel bên phải sẽ hiển thị:
   - Danh sách task
   - Ghi chú
   - Thống kê

### Di chuyển giữa các tháng
1. Dùng nút "◀ Tháng trước" và "Tháng sau ▶"
2. Hoặc click "Hôm nay" để về tháng hiện tại

## Use Cases

### 1. Review hiệu suất tuần
- Mở lịch sử
- Xem các ngày trong tuần
- Các ngày xanh đậm = làm việc hiệu quả
- Các ngày đỏ nhạt = cần cải thiện

### 2. Tìm kiếm thông tin cũ
- Di chuyển về tháng cần tìm
- Click vào ngày cụ thể
- Xem lại tasks và ghi chú của ngày đó

### 3. Học hỏi từ ngày thành công
- Tìm các ngày có nhiều task hoàn thành (xanh đậm)
- Xem ghi chú đánh giá
- Áp dụng cách làm việc tương tự

### 4. Theo dõi tiến độ dài hạn
- Xem xu hướng màu sắc qua các tháng
- Kiểm tra xem có cải thiện theo thời gian không
- Đánh giá hiệu quả của các phương pháp làm việc

## Kỹ thuật

### Components chính
- **CalendarHistoryController**: Xử lý logic và tương tác
- **calendar-history.fxml**: Định nghĩa giao diện
- **DayStatistics**: Model lưu thống kê ngày
- **TaskDAO.getStatisticsByDateRange()**: Lấy dữ liệu từ database

### Luồng dữ liệu
1. Controller load dữ liệu tháng hiện tại
2. TaskDAO query database lấy thống kê từng ngày
3. Render lịch với màu sắc tương ứng
4. Khi click ngày → Load chi tiết tasks và notes
5. Display trong panel bên phải

## Tips

1. **Sử dụng màu sắc làm động lực**: Nhìn thấy nhiều ô xanh đậm sẽ tạo động lực làm việc
2. **Review định kỳ**: Mỗi tuần xem lại lịch sử để đánh giá tiến độ
3. **Học từ thất bại**: Các ngày đỏ nhạt cho biết cần cải thiện điều gì
4. **Đặt mục tiêu**: Cố gắng có nhiều ngày xanh đậm hơn mỗi tháng
5. **Kết hợp với Daily Review**: Ghi chú chi tiết để sau này review dễ dàng
