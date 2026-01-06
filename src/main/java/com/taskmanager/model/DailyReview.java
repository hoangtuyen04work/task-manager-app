package com.taskmanager.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Model class đại diện cho đánh giá cuối ngày
 * Lưu trữ thống kê và ghi chú về hiệu suất làm việc trong ngày
 */
public class DailyReview {
    
    private Long id;
    private LocalDate reviewDate;
    private int totalTasks;
    private int completedTasks;
    private double completionRate;
    private int rating; // 1-5 stars
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Constructors
    public DailyReview() {
        this.reviewDate = LocalDate.now();
        this.rating = 3;
    }
    
    public DailyReview(LocalDate reviewDate, int totalTasks, int completedTasks, 
                      double completionRate, int rating, String notes) {
        this.reviewDate = reviewDate;
        this.totalTasks = totalTasks;
        this.completedTasks = completedTasks;
        this.completionRate = completionRate;
        this.rating = rating;
        this.notes = notes;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public LocalDate getReviewDate() {
        return reviewDate;
    }
    
    public void setReviewDate(LocalDate reviewDate) {
        this.reviewDate = reviewDate;
    }
    
    public int getTotalTasks() {
        return totalTasks;
    }
    
    public void setTotalTasks(int totalTasks) {
        this.totalTasks = totalTasks;
    }
    
    public int getCompletedTasks() {
        return completedTasks;
    }
    
    public void setCompletedTasks(int completedTasks) {
        this.completedTasks = completedTasks;
    }
    
    public double getCompletionRate() {
        return completionRate;
    }
    
    public void setCompletionRate(double completionRate) {
        this.completionRate = completionRate;
    }
    
    public int getRating() {
        return rating;
    }
    
    public void setRating(int rating) {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating phải từ 1 đến 5");
        }
        this.rating = rating;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    @Override
    public String toString() {
        return "DailyReview{" +
                "id=" + id +
                ", reviewDate=" + reviewDate +
                ", totalTasks=" + totalTasks +
                ", completedTasks=" + completedTasks +
                ", completionRate=" + completionRate +
                ", rating=" + rating +
                '}';
    }
}
