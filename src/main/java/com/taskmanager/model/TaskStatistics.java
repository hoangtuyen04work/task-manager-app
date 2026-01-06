package com.taskmanager.model;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * Model class chứa dữ liệu thống kê cho Statistics view
 * Tính toán và lưu trữ các chỉ số về hiệu suất làm việc
 */
public class TaskStatistics {
    
    private int totalTasks;
    private int completedTasks;
    private int pendingTasks;
    private double averageCompletionRate;
    
    // Thống kê theo priority
    private Map<Task.Priority, Integer> tasksByPriority;
    private Map<Task.Priority, Integer> completedByPriority;
    
    // Thống kê theo ngày
    private Map<LocalDate, Integer> tasksByDate;
    private Map<LocalDate, Integer> completedByDate;
    private Map<LocalDate, Double> completionRateByDate;
    
    // Top performance days
    private Map<LocalDate, Double> topPerformanceDays;
    
    public TaskStatistics() {
        this.tasksByPriority = new HashMap<>();
        this.completedByPriority = new HashMap<>();
        this.tasksByDate = new HashMap<>();
        this.completedByDate = new HashMap<>();
        this.completionRateByDate = new HashMap<>();
        this.topPerformanceDays = new HashMap<>();
        
        // Initialize priority maps
        for (Task.Priority priority : Task.Priority.values()) {
            tasksByPriority.put(priority, 0);
            completedByPriority.put(priority, 0);
        }
    }
    
    // Getters and Setters
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
    
    public int getPendingTasks() {
        return pendingTasks;
    }
    
    public void setPendingTasks(int pendingTasks) {
        this.pendingTasks = pendingTasks;
    }
    
    public double getAverageCompletionRate() {
        return averageCompletionRate;
    }
    
    public void setAverageCompletionRate(double averageCompletionRate) {
        this.averageCompletionRate = averageCompletionRate;
    }
    
    public Map<Task.Priority, Integer> getTasksByPriority() {
        return tasksByPriority;
    }
    
    public void setTasksByPriority(Map<Task.Priority, Integer> tasksByPriority) {
        this.tasksByPriority = tasksByPriority;
    }
    
    public Map<Task.Priority, Integer> getCompletedByPriority() {
        return completedByPriority;
    }
    
    public void setCompletedByPriority(Map<Task.Priority, Integer> completedByPriority) {
        this.completedByPriority = completedByPriority;
    }
    
    public Map<LocalDate, Integer> getTasksByDate() {
        return tasksByDate;
    }
    
    public void setTasksByDate(Map<LocalDate, Integer> tasksByDate) {
        this.tasksByDate = tasksByDate;
    }
    
    public Map<LocalDate, Integer> getCompletedByDate() {
        return completedByDate;
    }
    
    public void setCompletedByDate(Map<LocalDate, Integer> completedByDate) {
        this.completedByDate = completedByDate;
    }
    
    public Map<LocalDate, Double> getCompletionRateByDate() {
        return completionRateByDate;
    }
    
    public void setCompletionRateByDate(Map<LocalDate, Double> completionRateByDate) {
        this.completionRateByDate = completionRateByDate;
    }
    
    public Map<LocalDate, Double> getTopPerformanceDays() {
        return topPerformanceDays;
    }
    
    public void setTopPerformanceDays(Map<LocalDate, Double> topPerformanceDays) {
        this.topPerformanceDays = topPerformanceDays;
    }
}
