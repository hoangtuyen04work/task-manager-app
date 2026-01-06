package com.taskmanager.dao;

import com.taskmanager.model.DailyReview;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object cho DailyReview
 * Xử lý các thao tác CRUD với database cho Daily Review
 */
public class DailyReviewDAO {
    
    private final DatabaseConnection dbConnection;
    
    public DailyReviewDAO() {
        this.dbConnection = DatabaseConnection.getInstance();
    }
    
    /**
     * Lưu daily review mới vào database
     */
    public DailyReview save(DailyReview review) throws SQLException {
        String sql = "INSERT INTO daily_reviews (review_date, total_tasks, completed_tasks, " +
                    "completion_rate, rating, notes) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setDate(1, Date.valueOf(review.getReviewDate()));
            stmt.setInt(2, review.getTotalTasks());
            stmt.setInt(3, review.getCompletedTasks());
            stmt.setDouble(4, review.getCompletionRate());
            stmt.setInt(5, review.getRating());
            stmt.setString(6, review.getNotes());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Tạo review thất bại, không có row nào bị ảnh hưởng.");
            }
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    review.setId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("Tạo review thất bại, không lấy được ID.");
                }
            }
            
            return review;
        }
    }
    
    /**
     * Cập nhật daily review
     */
    public void update(DailyReview review) throws SQLException {
        String sql = "UPDATE daily_reviews SET total_tasks = ?, completed_tasks = ?, " +
                    "completion_rate = ?, rating = ?, notes = ? WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, review.getTotalTasks());
            stmt.setInt(2, review.getCompletedTasks());
            stmt.setDouble(3, review.getCompletionRate());
            stmt.setInt(4, review.getRating());
            stmt.setString(5, review.getNotes());
            stmt.setLong(6, review.getId());
            
            stmt.executeUpdate();
        }
    }
    
    /**
     * Xóa daily review
     */
    public void delete(Long id) throws SQLException {
        String sql = "DELETE FROM daily_reviews WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }
    
    /**
     * Tìm review theo ngày
     */
    public DailyReview findByDate(LocalDate date) throws SQLException {
        String sql = "SELECT * FROM daily_reviews WHERE review_date = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDate(1, Date.valueOf(date));
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToReview(rs);
                }
            }
        }
        
        return null;
    }
    
    /**
     * Lấy tất cả reviews
     */
    public List<DailyReview> findAll() throws SQLException {
        String sql = "SELECT * FROM daily_reviews ORDER BY review_date DESC";
        List<DailyReview> reviews = new ArrayList<>();
        
        try (Connection conn = dbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                reviews.add(mapResultSetToReview(rs));
            }
        }
        
        return reviews;
    }
    
    /**
     * Tìm reviews trong khoảng thời gian
     */
    public List<DailyReview> findByDateRange(LocalDate startDate, LocalDate endDate) throws SQLException {
        String sql = "SELECT * FROM daily_reviews WHERE review_date BETWEEN ? AND ? " +
                    "ORDER BY review_date DESC";
        List<DailyReview> reviews = new ArrayList<>();
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDate(1, Date.valueOf(startDate));
            stmt.setDate(2, Date.valueOf(endDate));
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    reviews.add(mapResultSetToReview(rs));
                }
            }
        }
        
        return reviews;
    }
    
    /**
     * Tính toán stats cho một ngày cụ thể
     * Lấy thông tin từ bảng tasks và tạo DailyReview object với stats
     */
    public DailyReview calculateStatsForDate(LocalDate date) throws SQLException {
        String sql = "SELECT " +
                    "COUNT(*) as total_tasks, " +
                    "SUM(CASE WHEN completed = true THEN 1 ELSE 0 END) as completed_tasks " +
                    "FROM tasks WHERE task_date = ?";
        
        DailyReview review = new DailyReview();
        review.setReviewDate(date);
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDate(1, Date.valueOf(date));
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int totalTasks = rs.getInt("total_tasks");
                    int completedTasks = rs.getInt("completed_tasks");
                    
                    review.setTotalTasks(totalTasks);
                    review.setCompletedTasks(completedTasks);
                    
                    // Tính completion rate
                    double completionRate = totalTasks > 0 
                        ? (completedTasks * 100.0 / totalTasks) 
                        : 0.0;
                    review.setCompletionRate(Math.round(completionRate * 100.0) / 100.0);
                }
            }
        }
        
        return review;
    }
    
    /**
     * Map ResultSet thành DailyReview object
     */
    private DailyReview mapResultSetToReview(ResultSet rs) throws SQLException {
        DailyReview review = new DailyReview();
        review.setId(rs.getLong("id"));
        review.setReviewDate(rs.getDate("review_date").toLocalDate());
        review.setTotalTasks(rs.getInt("total_tasks"));
        review.setCompletedTasks(rs.getInt("completed_tasks"));
        review.setCompletionRate(rs.getDouble("completion_rate"));
        review.setRating(rs.getInt("rating"));
        review.setNotes(rs.getString("notes"));
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            review.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            review.setUpdatedAt(updatedAt.toLocalDateTime());
        }
        
        return review;
    }
}
