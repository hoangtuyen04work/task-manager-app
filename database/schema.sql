-- ===================================================================
-- TASK MANAGER DATABASE SCHEMA
-- ===================================================================
-- Tạo database cho ứng dụng quản lý task
-- ===================================================================

CREATE DATABASE IF NOT EXISTS task_manager_db 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

USE task_manager_db;

-- ===================================================================
-- TABLE: tasks
-- Lưu trữ các task hàng ngày
-- ===================================================================
CREATE TABLE tasks (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    task_date DATE NOT NULL,
    priority ENUM('HIGH', 'MEDIUM', 'LOW') DEFAULT 'MEDIUM',
    completed BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_task_date (task_date),
    INDEX idx_completed (completed),
    INDEX idx_priority (priority)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ===================================================================
-- TABLE: daily_reviews
-- Lưu trữ đánh giá cuối ngày
-- ===================================================================
CREATE TABLE daily_reviews (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    review_date DATE NOT NULL UNIQUE,
    total_tasks INT NOT NULL,
    completed_tasks INT NOT NULL,
    completion_rate DECIMAL(5,2) NOT NULL,
    rating INT CHECK (rating BETWEEN 1 AND 5),
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_review_date (review_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ===================================================================
-- SAMPLE DATA
-- Dữ liệu mẫu để demo ứng dụng
-- ===================================================================

-- Tasks mẫu cho ngày hôm nay
INSERT INTO tasks (title, description, task_date, priority, completed) VALUES
('Học JavaFX cơ bản', 'Hoàn thành tutorial JavaFX controls và layouts', CURDATE(), 'HIGH', true),
('Setup MySQL Database', 'Cài đặt và cấu hình MySQL server với Workbench', CURDATE(), 'HIGH', true),
('Đọc tài liệu Scene Builder', 'Tìm hiểu cách sử dụng Scene Builder để thiết kế UI', CURDATE(), 'MEDIUM', false),
('Viết unit tests', 'Tạo unit tests cho các DAO classes', CURDATE(), 'LOW', false),
('Meeting với team', 'Họp review sprint và plan cho sprint tiếp theo', DATE_ADD(CURDATE(), INTERVAL 1 DAY), 'HIGH', false),
('Code review pull requests', 'Review các PR từ team members', DATE_ADD(CURDATE(), INTERVAL 1 DAY), 'MEDIUM', false),
('Refactor TaskDAO', 'Cải thiện performance và clean code cho TaskDAO', DATE_ADD(CURDATE(), INTERVAL 2 DAY), 'MEDIUM', false),
('Tạo biểu đồ thống kê', 'Implement charts cho statistics view', DATE_ADD(CURDATE(), INTERVAL 2 DAY), 'HIGH', false),
('Deploy ứng dụng lên server', 'Deploy và test trên production environment', DATE_ADD(CURDATE(), INTERVAL 3 DAY), 'HIGH', false),
('Viết documentation', 'Hoàn thiện README và user guide', DATE_ADD(CURDATE(), INTERVAL 3 DAY), 'LOW', false);

-- Tasks mẫu cho ngày hôm qua
INSERT INTO tasks (title, description, task_date, priority, completed) VALUES
('Thiết kế database schema', 'Tạo ERD và SQL script cho database', DATE_SUB(CURDATE(), INTERVAL 1 DAY), 'HIGH', true),
('Setup project structure', 'Tạo cấu trúc thư mục và package cho project', DATE_SUB(CURDATE(), INTERVAL 1 DAY), 'HIGH', true),
('Nghiên cứu JavaFX best practices', 'Đọc tài liệu và examples về JavaFX', DATE_SUB(CURDATE(), INTERVAL 1 DAY), 'MEDIUM', true),
('Cấu hình Maven dependencies', 'Thêm các dependencies cần thiết vào pom.xml', DATE_SUB(CURDATE(), INTERVAL 1 DAY), 'MEDIUM', true),
('Tạo mockup UI', 'Thiết kế giao diện bằng Figma', DATE_SUB(CURDATE(), INTERVAL 1 DAY), 'LOW', false);

-- Daily review mẫu cho ngày hôm qua
INSERT INTO daily_reviews (review_date, total_tasks, completed_tasks, completion_rate, rating, notes) VALUES
(DATE_SUB(CURDATE(), INTERVAL 1 DAY), 5, 4, 80.00, 4, 
'Ngày làm việc khá hiệu quả. Hoàn thành được 4/5 tasks. Task còn lại (Tạo mockup UI) sẽ tiếp tục vào ngày mai. Cần cải thiện time management để tránh bị chậm deadline.');

-- ===================================================================
-- VERIFY DATA
-- ===================================================================
-- Uncomment để kiểm tra dữ liệu đã được insert

-- SELECT COUNT(*) as total_tasks FROM tasks;
-- SELECT * FROM tasks ORDER BY task_date DESC, priority;
-- SELECT * FROM daily_reviews ORDER BY review_date DESC;
