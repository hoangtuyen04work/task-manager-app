package com.taskmanager.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Model class đại diện cho một Task trong hệ thống
 * Lưu trữ thông tin về công việc cần làm hàng ngày
 */
public class Task {
    
    private Long id;
    private String title;
    private String description;
    private LocalDate taskDate;
    private Priority priority;
    private boolean completed;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    /**
     * Enum định nghĩa các mức độ ưu tiên của task
     */
    public enum Priority {
        HIGH("Cao"),
        MEDIUM("Trung bình"),
        LOW("Thấp");
        
        private final String displayName;
        
        Priority(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
        
        @Override
        public String toString() {
            return displayName;
        }
    }
    
    // Constructors
    public Task() {
        this.completed = false;
        this.priority = Priority.MEDIUM;
        this.taskDate = LocalDate.now();
    }
    
    public Task(String title, String description, LocalDate taskDate, Priority priority) {
        this();
        this.title = title;
        this.description = description;
        this.taskDate = taskDate;
        this.priority = priority;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public LocalDate getTaskDate() {
        return taskDate;
    }
    
    public void setTaskDate(LocalDate taskDate) {
        this.taskDate = taskDate;
    }
    
    public Priority getPriority() {
        return priority;
    }
    
    public void setPriority(Priority priority) {
        this.priority = priority;
    }
    
    public boolean isCompleted() {
        return completed;
    }
    
    public void setCompleted(boolean completed) {
        this.completed = completed;
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
        return "Task{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", taskDate=" + taskDate +
                ", priority=" + priority +
                ", completed=" + completed +
                '}';
    }
}
