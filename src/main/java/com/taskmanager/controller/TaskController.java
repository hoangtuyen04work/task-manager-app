package com.taskmanager.controller;

import com.taskmanager.dao.TaskDAO;
import com.taskmanager.model.Task;
import com.taskmanager.model.Task.Priority;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.SQLException;
import java.time.LocalDate;

/**
 * Controller cho form thêm/sửa Task
 * Xử lý validation và lưu task vào database
 */
public class TaskController {
    
    @FXML private TextField txtTitle;
    @FXML private TextArea txtDescription;
    @FXML private DatePicker datePicker;
    @FXML private ComboBox<Priority> cbPriority;
    @FXML private Label lblValidation;
    
    private TaskDAO taskDAO;
    private Task task;
    private boolean isEditMode;
    
    /**
     * Initialize controller
     */
    @FXML
    public void initialize() {
        taskDAO = new TaskDAO();
        
        // Populate priority combo box
        cbPriority.getItems().addAll(Priority.values());
        cbPriority.setValue(Priority.MEDIUM);
        
        // Set default date to today
        datePicker.setValue(LocalDate.now());
        
        // Setup validation
        setupValidation();
    }
    
    /**
     * Cấu hình validation cho form
     */
    private void setupValidation() {
        // Limit description length
        txtDescription.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getControlNewText().length() <= 1000) {
                return change;
            }
            return null;
        }));
        
        // Limit title length
        txtTitle.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getControlNewText().length() <= 200) {
                return change;
            }
            return null;
        }));
    }
    
    /**
     * Set task để edit (null nếu thêm mới)
     */
    public void setTask(Task task) {
        this.task = task;
        this.isEditMode = (task != null && task.getId() != null);
        
        if (isEditMode) {
            // Điền dữ liệu vào form
            txtTitle.setText(task.getTitle());
            txtDescription.setText(task.getDescription());
            datePicker.setValue(task.getTaskDate());
            cbPriority.setValue(task.getPriority());
        }
    }
    
    /**
     * Set ngày mặc định cho DatePicker
     */
    public void setDatePicker(LocalDate date) {
        if (date != null && !isEditMode) {
            datePicker.setValue(date);
        }
    }
    
    /**
     * Validate form
     */
    private boolean validateForm() {
        // Reset validation message
        lblValidation.setVisible(false);
        lblValidation.setManaged(false);
        
        // Check title
        if (txtTitle.getText() == null || txtTitle.getText().trim().isEmpty()) {
            showValidationError("Vui lòng nhập tên công việc");
            return false;
        }
        
        // Check date
        if (datePicker.getValue() == null) {
            showValidationError("Vui lòng chọn ngày");
            return false;
        }
        
        // Check priority
        if (cbPriority.getValue() == null) {
            showValidationError("Vui lòng chọn độ ưu tiên");
            return false;
        }
        
        return true;
    }
    
    /**
     * Hiển thị thông báo validation lỗi
     */
    private void showValidationError(String message) {
        lblValidation.setText(message);
        lblValidation.setVisible(true);
        lblValidation.setManaged(true);
    }
    
    /**
     * Lưu task vào database
     */
    public Task getTask() {
        if (!validateForm()) {
            return null;
        }
        
        try {
            if (task == null) {
                task = new Task();
            }
            
            // Cập nhật thông tin từ form
            task.setTitle(txtTitle.getText().trim());
            task.setDescription(txtDescription.getText().trim());
            task.setTaskDate(datePicker.getValue());
            task.setPriority(cbPriority.getValue());
            
            // Lưu vào database
            if (isEditMode) {
                taskDAO.update(task);
            } else {
                task = taskDAO.save(task);
            }
            
            return task;
        } catch (SQLException e) {
            showValidationError("Lỗi khi lưu: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
