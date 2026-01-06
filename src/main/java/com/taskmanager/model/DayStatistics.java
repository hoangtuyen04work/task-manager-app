package com.taskmanager.model;

import java.time.LocalDate;

/**
 * Model class để lưu thống kê task cho một ngày cụ thể
 * Dùng để hiển thị trên calendar với màu sắc khác nhau
 */
public class DayStatistics {
    
    private LocalDate date;
    private int totalTasks;
    private int completedTasks;
    private String notes;
    
    public DayStatistics() {
    }
    
    public DayStatistics(LocalDate date, int totalTasks, int completedTasks) {
        this.date = date;
        this.totalTasks = totalTasks;
        this.completedTasks = completedTasks;
    }
    
    public LocalDate getDate() {
        return date;
    }
    
    public void setDate(LocalDate date) {
        this.date = date;
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
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    /**
     * Tính toán tỷ lệ hoàn thành
     */
    public double getCompletionRate() {
        return totalTasks > 0 ? (completedTasks * 100.0 / totalTasks) : 0.0;
    }
    
    /**
     * Kiểm tra có tasks không
     */
    public boolean hasTasks() {
        return totalTasks > 0;
    }
    
    @Override
    public String toString() {
        return "DayStatistics{" +
                "date=" + date +
                ", totalTasks=" + totalTasks +
                ", completedTasks=" + completedTasks +
                ", completionRate=" + getCompletionRate() +
                '}';
    }
}
