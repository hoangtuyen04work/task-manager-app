package com.taskmanager.controller;

import com.taskmanager.dao.TaskDAO;
import com.taskmanager.model.Task;
import com.taskmanager.model.Task.Priority;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.SQLException;
import java.time.LocalDate;

public class TaskController {
    
    @FXML private TextField txtTitle;
    @FXML private TextArea txtDescription;
    @FXML private DatePicker datePicker;
    @FXML private ComboBox<Priority> cbPriority;
    @FXML private Label lblValidation;
    
    private TaskDAO taskDAO;
    private Task task;
    private boolean isEditMode;

    @FXML
    public void initialize() {
        taskDAO = new TaskDAO();
        
        cbPriority.getItems().addAll(Priority.values());
        cbPriority.setValue(Priority.MEDIUM);
        
        datePicker.setValue(LocalDate.now());
        
        setupValidation();
    }

    private void setupValidation() {
        txtDescription.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getControlNewText().length() <= 1000) {
                return change;
            }
            return null;
        }));
        
        txtTitle.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getControlNewText().length() <= 200) {
                return change;
            }
            return null;
        }));
    }

    public void setTask(Task task) {
        this.task = task;
        this.isEditMode = (task != null && task.getId() != null);
        
        if (isEditMode) {
            txtTitle.setText(task.getTitle());
            txtDescription.setText(task.getDescription());
            datePicker.setValue(task.getTaskDate());
            cbPriority.setValue(task.getPriority());
        }
    }

    public void setDatePicker(LocalDate date) {
        if (date != null && !isEditMode) {
            datePicker.setValue(date);
        }
    }

    private boolean validateForm() {
        lblValidation.setVisible(false);
        lblValidation.setManaged(false);
        
        if (txtTitle.getText() == null || txtTitle.getText().trim().isEmpty()) {
            showValidationError("Vui lòng nhập tên công việc");
            return false;
        }
        
        if (datePicker.getValue() == null) {
            showValidationError("Vui lòng chọn ngày");
            return false;
        }
        
        if (cbPriority.getValue() == null) {
            showValidationError("Vui lòng chọn độ ưu tiên");
            return false;
        }
        
        return true;
    }

    private void showValidationError(String message) {
        lblValidation.setText(message);
        lblValidation.setVisible(true);
        lblValidation.setManaged(true);
    }

    public Task getTask() {
        if (!validateForm()) {
            return null;
        }
        
        try {
            if (task == null) {
                task = new Task();
            }
            
            task.setTitle(txtTitle.getText().trim());
            task.setDescription(txtDescription.getText().trim());
            task.setTaskDate(datePicker.getValue());
            task.setPriority(cbPriority.getValue());
            
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
