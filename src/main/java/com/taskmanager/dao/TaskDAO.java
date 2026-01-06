package com.taskmanager.dao;

import com.taskmanager.model.DayStatistics;
import com.taskmanager.model.Task;
import com.taskmanager.model.Task.Priority;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Data Access Object cho Task
 * Xử lý tất cả các thao tác CRUD với database cho Task
 */
public class TaskDAO {
    
    private final DatabaseConnection dbConnection;
    
    public TaskDAO() {
        this.dbConnection = DatabaseConnection.getInstance();
    }
    
    /**
     * Lưu task mới vào database
     */
    public Task save(Task task) throws SQLException {
        String sql = "INSERT INTO tasks (title, description, task_date, priority, completed) " +
                    "VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, task.getTitle());
            stmt.setString(2, task.getDescription());
            stmt.setDate(3, Date.valueOf(task.getTaskDate()));
            stmt.setString(4, task.getPriority().name());
            stmt.setBoolean(5, task.isCompleted());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Tạo task thất bại, không có row nào bị ảnh hưởng.");
            }
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    task.setId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("Tạo task thất bại, không lấy được ID.");
                }
            }
            
            return task;
        }
    }
    
    /**
     * Cập nhật task trong database
     */
    public void update(Task task) throws SQLException {
        String sql = "UPDATE tasks SET title = ?, description = ?, task_date = ?, " +
                    "priority = ?, completed = ? WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, task.getTitle());
            stmt.setString(2, task.getDescription());
            stmt.setDate(3, Date.valueOf(task.getTaskDate()));
            stmt.setString(4, task.getPriority().name());
            stmt.setBoolean(5, task.isCompleted());
            stmt.setLong(6, task.getId());
            
            stmt.executeUpdate();
        }
    }
    
    /**
     * Xóa task khỏi database
     */
    public void delete(Long id) throws SQLException {
        String sql = "DELETE FROM tasks WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }
    
    /**
     * Tìm task theo ID
     */
    public Task findById(Long id) throws SQLException {
        String sql = "SELECT * FROM tasks WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToTask(rs);
                }
            }
        }
        
        return null;
    }
    
    /**
     * Lấy tất cả tasks
     */
    public List<Task> findAll() throws SQLException {
        String sql = "SELECT * FROM tasks ORDER BY task_date DESC, created_at DESC";
        List<Task> tasks = new ArrayList<>();
        
        try (Connection conn = dbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                tasks.add(mapResultSetToTask(rs));
            }
        }
        
        return tasks;
    }
    
    /**
     * Tìm tasks theo ngày
     */
    public List<Task> findByDate(LocalDate date) throws SQLException {
        String sql = "SELECT * FROM tasks WHERE task_date = ? ORDER BY priority DESC, created_at";
        List<Task> tasks = new ArrayList<>();
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDate(1, Date.valueOf(date));
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    tasks.add(mapResultSetToTask(rs));
                }
            }
        }
        
        return tasks;
    }
    
    /**
     * Tìm tasks trong khoảng thời gian
     */
    public List<Task> findByDateRange(LocalDate startDate, LocalDate endDate) throws SQLException {
        String sql = "SELECT * FROM tasks WHERE task_date BETWEEN ? AND ? " +
                    "ORDER BY task_date DESC, priority DESC";
        List<Task> tasks = new ArrayList<>();
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDate(1, Date.valueOf(startDate));
            stmt.setDate(2, Date.valueOf(endDate));
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    tasks.add(mapResultSetToTask(rs));
                }
            }
        }
        
        return tasks;
    }
    
    /**
     * Tìm kiếm tasks theo từ khóa (title hoặc description)
     */
    public List<Task> searchByKeyword(String keyword) throws SQLException {
        String sql = "SELECT * FROM tasks WHERE title LIKE ? OR description LIKE ? " +
                    "ORDER BY task_date DESC";
        List<Task> tasks = new ArrayList<>();
        String searchPattern = "%" + keyword + "%";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    tasks.add(mapResultSetToTask(rs));
                }
            }
        }
        
        return tasks;
    }
    
    /**
     * Đánh dấu task hoàn thành/chưa hoàn thành
     */
    public void toggleComplete(Long id) throws SQLException {
        String sql = "UPDATE tasks SET completed = NOT completed WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }
    
    /**
     * Copy tasks sang ngày mới
     */
    public List<Task> copyTasksToDate(List<Long> taskIds, LocalDate targetDate) throws SQLException {
        List<Task> copiedTasks = new ArrayList<>();
        
        for (Long taskId : taskIds) {
            Task originalTask = findById(taskId);
            if (originalTask != null) {
                Task newTask = new Task();
                newTask.setTitle(originalTask.getTitle());
                newTask.setDescription(originalTask.getDescription());
                newTask.setTaskDate(targetDate);
                newTask.setPriority(originalTask.getPriority());
                newTask.setCompleted(false); // Tasks được copy luôn có trạng thái chưa hoàn thành
                
                Task savedTask = save(newTask);
                copiedTasks.add(savedTask);
            }
        }
        
        return copiedTasks;
    }
    
    /**
     * Lấy tasks chưa hoàn thành theo ngày
     */
    public List<Task> findPendingByDate(LocalDate date) throws SQLException {
        String sql = "SELECT * FROM tasks WHERE task_date = ? AND completed = false " +
                    "ORDER BY priority DESC, created_at";
        List<Task> tasks = new ArrayList<>();
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDate(1, Date.valueOf(date));
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    tasks.add(mapResultSetToTask(rs));
                }
            }
        }
        
        return tasks;
    }
    
    /**
     * Lấy thống kê tasks theo từng ngày trong khoảng thời gian
     * Trả về Map với key là LocalDate và value là DayStatistics
     */
    public Map<LocalDate, DayStatistics> getStatisticsByDateRange(LocalDate startDate, LocalDate endDate) throws SQLException {
        String sql = "SELECT task_date, " +
                    "COUNT(*) as total_tasks, " +
                    "SUM(CASE WHEN completed = true THEN 1 ELSE 0 END) as completed_tasks " +
                    "FROM tasks WHERE task_date BETWEEN ? AND ? " +
                    "GROUP BY task_date";
        
        Map<LocalDate, DayStatistics> statsMap = new HashMap<>();
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDate(1, Date.valueOf(startDate));
            stmt.setDate(2, Date.valueOf(endDate));
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    LocalDate date = rs.getDate("task_date").toLocalDate();
                    int totalTasks = rs.getInt("total_tasks");
                    int completedTasks = rs.getInt("completed_tasks");
                    
                    DayStatistics stats = new DayStatistics(date, totalTasks, completedTasks);
                    statsMap.put(date, stats);
                }
            }
        }
        
        return statsMap;
    }
    
    /**
     * Map ResultSet thành Task object
     */
    private Task mapResultSetToTask(ResultSet rs) throws SQLException {
        Task task = new Task();
        task.setId(rs.getLong("id"));
        task.setTitle(rs.getString("title"));
        task.setDescription(rs.getString("description"));
        task.setTaskDate(rs.getDate("task_date").toLocalDate());
        task.setPriority(Priority.valueOf(rs.getString("priority")));
        task.setCompleted(rs.getBoolean("completed"));
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            task.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            task.setUpdatedAt(updatedAt.toLocalDateTime());
        }
        
        return task;
    }
}
