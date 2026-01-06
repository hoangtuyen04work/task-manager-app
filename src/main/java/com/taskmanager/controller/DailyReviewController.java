package com.taskmanager.controller;

import com.taskmanager.dao.DailyReviewDAO;
import com.taskmanager.dao.TaskDAO;
import com.taskmanager.model.DailyReview;
import com.taskmanager.model.Task;
import com.taskmanager.util.AlertUtil;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/**
 * Controller cho Daily Review Dialog
 * Quản lý việc đánh giá cuối ngày
 */
public class DailyReviewController {
    
    @FXML private DatePicker datePicker;
    @FXML private Label lblTotalTasks;
    @FXML private Label lblCompletedTasks;
    @FXML private Label lblCompletionRate;
    @FXML private HBox ratingBox;
    @FXML private TextArea txtNotes;
    @FXML private ListView<String> taskListView;
    
    private DailyReviewDAO dailyReviewDAO;
    private TaskDAO taskDAO;
    private DailyReview currentReview;
    private int selectedRating = 3;
    private Label[] starLabels;
    
    /**
     * Initialize controller
     */
    @FXML
    public void initialize() {
        dailyReviewDAO = new DailyReviewDAO();
        taskDAO = new TaskDAO();
        
        // Set default date to today
        datePicker.setValue(LocalDate.now());
        
        // Setup rating stars
        setupRatingStars();
        
        // Limit notes to 2000 characters
        txtNotes.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getControlNewText().length() <= 2000) {
                return change;
            }
            return null;
        }));
        
        // Load initial data
        loadReviewData();
    }
    
    /**
     * Cấu hình các ngôi sao đánh giá
     */
    private void setupRatingStars() {
        starLabels = new Label[5];
        
        for (int i = 0; i < 5; i++) {
            final int rating = i + 1;
            Label star = new Label("★");
            star.setStyle("-fx-font-size: 24px; -fx-cursor: hand;");
            star.getStyleClass().add("rating-star");
            
            // Click to set rating
            star.setOnMouseClicked(e -> {
                setRating(rating);
            });
            
            // Hover effect
            star.setOnMouseEntered(e -> {
                updateStarDisplay(rating);
            });
            
            star.setOnMouseExited(e -> {
                updateStarDisplay(selectedRating);
            });
            
            starLabels[i] = star;
            ratingBox.getChildren().add(star);
        }
        
        updateStarDisplay(selectedRating);
    }
    
    /**
     * Đặt rating
     */
    private void setRating(int rating) {
        this.selectedRating = rating;
        updateStarDisplay(rating);
    }
    
    /**
     * Cập nhật hiển thị ngôi sao
     */
    private void updateStarDisplay(int rating) {
        for (int i = 0; i < 5; i++) {
            if (i < rating) {
                starLabels[i].setStyle("-fx-font-size: 24px; -fx-cursor: hand; -fx-text-fill: #f39c12;");
            } else {
                starLabels[i].setStyle("-fx-font-size: 24px; -fx-cursor: hand; -fx-text-fill: #bdc3c7;");
            }
        }
    }
    
    /**
     * Set ngày để đánh giá
     */
    public void setDate(LocalDate date) {
        if (date != null) {
            datePicker.setValue(date);
            loadReviewData();
        }
    }
    
    /**
     * Load dữ liệu review cho ngày được chọn
     */
    @FXML
    private void handleDateChange() {
        loadReviewData();
    }
    
    /**
     * Load review data
     */
    private void loadReviewData() {
        LocalDate selectedDate = datePicker.getValue();
        if (selectedDate == null) {
            return;
        }
        
        try {
            // Tính toán stats tự động
            DailyReview stats = dailyReviewDAO.calculateStatsForDate(selectedDate);
            
            // Hiển thị stats
            lblTotalTasks.setText(String.valueOf(stats.getTotalTasks()));
            lblCompletedTasks.setText(String.valueOf(stats.getCompletedTasks()));
            lblCompletionRate.setText(String.format("%.0f%%", stats.getCompletionRate()));
            
            // Load existing review nếu có
            DailyReview existingReview = dailyReviewDAO.findByDate(selectedDate);
            
            if (existingReview != null) {
                currentReview = existingReview;
                selectedRating = existingReview.getRating();
                updateStarDisplay(selectedRating);
                txtNotes.setText(existingReview.getNotes());
            } else {
                // Tạo review mới
                currentReview = stats;
                selectedRating = 3;
                updateStarDisplay(selectedRating);
                txtNotes.clear();
            }
            
            // Load task list
            loadTaskList(selectedDate);
            
        } catch (SQLException e) {
            AlertUtil.showError("Lỗi", "Không thể tải dữ liệu: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Load danh sách tasks cho ngày
     */
    private void loadTaskList(LocalDate date) {
        try {
            List<Task> tasks = taskDAO.findByDate(date);
            
            taskListView.getItems().clear();
            for (Task task : tasks) {
                String status = task.isCompleted() ? "✓" : "✗";
                String priorityIcon = switch (task.getPriority()) {
                    case HIGH -> "🔴";
                    case MEDIUM -> "🟡";
                    case LOW -> "🟢";
                };
                
                String item = String.format("%s %s %s", status, priorityIcon, task.getTitle());
                taskListView.getItems().add(item);
            }
        } catch (SQLException e) {
            AlertUtil.showError("Lỗi", "Không thể tải danh sách tasks: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Lấy review đã được cập nhật
     */
    public DailyReview getReview() {
        if (currentReview == null) {
            return null;
        }
        
        try {
            // Cập nhật rating và notes
            currentReview.setRating(selectedRating);
            currentReview.setNotes(txtNotes.getText().trim());
            
            // Lưu vào database
            if (currentReview.getId() == null) {
                currentReview = dailyReviewDAO.save(currentReview);
                AlertUtil.showInfo("Thành công", "Đã lưu đánh giá cuối ngày");
            } else {
                dailyReviewDAO.update(currentReview);
                AlertUtil.showInfo("Thành công", "Đã cập nhật đánh giá cuối ngày");
            }
            
            return currentReview;
        } catch (SQLException e) {
            AlertUtil.showError("Lỗi", "Không thể lưu đánh giá: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
