package com.taskmanager.controller;

import com.taskmanager.dao.DailyReviewDAO;
import com.taskmanager.dao.TaskDAO;
import com.taskmanager.model.DailyReview;
import com.taskmanager.model.DayStatistics;
import com.taskmanager.model.Task;
import com.taskmanager.util.AlertUtil;
import com.taskmanager.util.DateUtil;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Controller cho màn hình lịch sử công việc theo lịch
 * Hiển thị calendar với màu sắc biểu thị số lượng task hoàn thành
 */
public class CalendarHistoryController {

    @FXML
    private Label lblMonthYear;
    @FXML
    private GridPane calendarGrid;
    @FXML
    private ListView<Task> taskListView;
    @FXML
    private TextArea notesArea;
    @FXML
    private Label lblSelectedDate;
    @FXML
    private Label lblStats;

    private TaskDAO taskDAO;
    private DailyReviewDAO dailyReviewDAO;
    private YearMonth currentMonth;
    private LocalDate selectedDate;
    private Map<LocalDate, DayStatistics> statsMap;
    private Map<LocalDate, VBox> dayCellsMap = new java.util.HashMap<>();

    @FXML
    public void initialize() {
        taskDAO = new TaskDAO();
        dailyReviewDAO = new DailyReviewDAO();
        currentMonth = YearMonth.now();
        selectedDate = LocalDate.now();

        renderCalendar();
        loadDayDetails(selectedDate);
    }

    /**
     * Render calendar cho tháng hiện tại
     */
    private void renderCalendar() {
        // Clear existing calendar and map
        calendarGrid.getChildren().clear();
        calendarGrid.getColumnConstraints().clear();
        calendarGrid.getRowConstraints().clear();
        dayCellsMap.clear();

        // Set column constraints first for stable layout
        for (int i = 0; i < 7; i++) {
            ColumnConstraints colConstraints = new ColumnConstraints();
            colConstraints.setPercentWidth(100.0 / 7);
            colConstraints.setHgrow(Priority.ALWAYS);
            calendarGrid.getColumnConstraints().add(colConstraints);
        }

        // Update month/year label
        String monthName = currentMonth.getMonth().getDisplayName(TextStyle.FULL, new Locale("vi", "VN"));
        lblMonthYear.setText(monthName + " " + currentMonth.getYear());

        // Load statistics for the month
        LocalDate startDate = currentMonth.atDay(1);
        LocalDate endDate = currentMonth.atEndOfMonth();

        try {
            statsMap = taskDAO.getStatisticsByDateRange(startDate, endDate);
        } catch (SQLException e) {
            AlertUtil.showError("Lỗi", "Không thể tải dữ liệu thống kê: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        // Add day headers (T2 - CN)
        String[] dayHeaders = { "T2", "T3", "T4", "T5", "T6", "T7", "CN" };
        for (int i = 0; i < 7; i++) {
            Label header = new Label(dayHeaders[i]);
            header.setFont(Font.font("System", FontWeight.BOLD, 12));
            header.setMaxWidth(Double.MAX_VALUE);
            header.setAlignment(Pos.CENTER);
            header.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-padding: 5px;");
            GridPane.setHgrow(header, Priority.ALWAYS);
            calendarGrid.add(header, i, 0);
        }

        // Get first day of month and calculate offset
        LocalDate firstDay = currentMonth.atDay(1);
        int dayOfWeek = firstDay.getDayOfWeek().getValue(); // 1 = Monday, 7 = Sunday
        int offset = dayOfWeek - 1; // Convert to 0-based index

        // Fill calendar cells
        int daysInMonth = currentMonth.lengthOfMonth();
        int row = 1;
        int col = offset;

        for (int day = 1; day <= daysInMonth; day++) {
            LocalDate date = currentMonth.atDay(day);
            VBox dayCell = createDayCell(date);

            // Store reference to the cell
            dayCellsMap.put(date, dayCell);

            GridPane.setHgrow(dayCell, Priority.ALWAYS);
            GridPane.setVgrow(dayCell, Priority.ALWAYS);
            calendarGrid.add(dayCell, col, row);

            col++;
            if (col > 6) {
                col = 0;
                row++;
            }
        }
    }

    /**
     * Tạo ô ngày trong calendar
     */
    private VBox createDayCell(LocalDate date) {
        VBox cell = new VBox();
        cell.setAlignment(Pos.TOP_CENTER);
        cell.setPadding(new Insets(5));
        cell.setMinHeight(60);
        cell.setPrefHeight(80);
        cell.setMaxWidth(Double.MAX_VALUE);
        cell.setSpacing(3);

        // Day number label
        Label dayLabel = new Label(String.valueOf(date.getDayOfMonth()));
        dayLabel.setFont(Font.font("System", FontWeight.BOLD, 14));

        // Get statistics for this day
        DayStatistics stats = statsMap.get(date);

        // Set background color based on completed tasks
        String bgColor = getCellBackgroundColor(stats);
        String textColor = getTextColor(stats);

        // Add task count label if has tasks
        if (stats != null && stats.hasTasks()) {
            int completedTasks = stats.getCompletedTasks();
            Label countLabel = new Label(completedTasks + "/" + stats.getTotalTasks());
            countLabel.setFont(Font.font("System", 10));
            countLabel.setStyle("-fx-text-fill: " + textColor + ";");
            cell.getChildren().add(countLabel);
        }

        // Apply styling
        updateCellStyle(cell, date, bgColor);

        dayLabel.setStyle("-fx-text-fill: " + textColor + ";");
        cell.getChildren().add(0, dayLabel);

        // Store background color and text color in user data for later updates
        cell.setUserData(new CellStyleData(bgColor, textColor));

        // Add click handler
        cell.setOnMouseClicked(event -> handleDayClick(date));

        return cell;
    }

    /**
     * Get background color based on task statistics
     */
    private String getCellBackgroundColor(DayStatistics stats) {
        if (stats == null || !stats.hasTasks()) {
            return "#ecf0f1";
        }

        int completedTasks = stats.getCompletedTasks();
        if (completedTasks == 0) {
            return "#fadbd8"; // Has tasks but none completed - light red
        } else if (completedTasks <= 2) {
            return "#d5f4e6"; // Few completed tasks - light green
        } else if (completedTasks <= 5) {
            return "#82e0aa"; // Medium completed tasks - medium green
        } else {
            return "#27ae60"; // Many completed tasks - dark green
        }
    }

    /**
     * Get text color based on task statistics
     */
    private String getTextColor(DayStatistics stats) {
        if (stats != null && stats.getCompletedTasks() > 5) {
            return "white";
        }
        return "black";
    }

    /**
     * Update cell style based on selection state
     */
    private void updateCellStyle(VBox cell, LocalDate date, String bgColor) {
        String borderColor;
        String borderWidth;

        if (DateUtil.isToday(date)) {
            borderColor = "#e74c3c";
            borderWidth = "2px";
        } else if (date.equals(selectedDate)) {
            borderColor = "#3498db";
            borderWidth = "2px";
        } else {
            borderColor = "#bdc3c7";
            borderWidth = "1px";
        }

        cell.setStyle(String.format(
                "-fx-background-color: %s; -fx-border-color: %s; -fx-border-width: %s; -fx-cursor: hand;",
                bgColor, borderColor, borderWidth));
    }

    /**
     * Handle day cell click
     */
    private void handleDayClick(LocalDate newDate) {
        LocalDate previousDate = selectedDate;
        selectedDate = newDate;

        // Update only the affected cells instead of re-rendering entire calendar
        updateCellSelection(previousDate);
        updateCellSelection(newDate);

        // Load details for the new date
        loadDayDetails(newDate);
    }

    /**
     * Update selection style for a specific cell
     */
    private void updateCellSelection(LocalDate date) {
        VBox cell = dayCellsMap.get(date);
        if (cell != null && cell.getUserData() instanceof CellStyleData) {
            CellStyleData styleData = (CellStyleData) cell.getUserData();
            updateCellStyle(cell, date, styleData.bgColor);
        }
    }

    /**
     * Inner class to store cell style data
     */
    private static class CellStyleData {
        String bgColor;
        String textColor;

        CellStyleData(String bgColor, String textColor) {
            this.bgColor = bgColor;
            this.textColor = textColor;
        }
    }

    /**
     * Load chi tiết công việc và ghi chú cho ngày được chọn
     */
    private void loadDayDetails(LocalDate date) {
        lblSelectedDate.setText("Ngày: " + DateUtil.formatDate(date));

        try {
            // Load tasks for the selected date
            List<Task> tasks = taskDAO.findByDate(date);
            taskListView.getItems().setAll(tasks);

            // Custom cell factory to display task info
            taskListView.setCellFactory(param -> new ListCell<Task>() {
                @Override
                protected void updateItem(Task task, boolean empty) {
                    super.updateItem(task, empty);
                    if (empty || task == null) {
                        setText(null);
                        setStyle("");
                    } else {
                        String priorityIcon = switch (task.getPriority()) {
                            case HIGH -> "🔴";
                            case MEDIUM -> "🟡";
                            case LOW -> "🟢";
                        };
                        String status = task.isCompleted() ? "✓" : "○";
                        setText(status + " " + priorityIcon + " " + task.getTitle());

                        if (task.isCompleted()) {
                            setStyle("-fx-text-fill: #7f8c8d; -fx-strikethrough: true;");
                        } else {
                            setStyle("");
                        }
                    }
                }
            });

            // Load daily review notes
            DailyReview review = dailyReviewDAO.findByDate(date);
            if (review != null && review.getNotes() != null) {
                notesArea.setText(review.getNotes());
            } else {
                notesArea.setText("(Chưa có ghi chú cho ngày này)");
            }

            // Update statistics label
            int total = tasks.size();
            long completed = tasks.stream().filter(Task::isCompleted).count();
            double rate = total > 0 ? (completed * 100.0 / total) : 0.0;
            lblStats.setText(String.format("Tổng: %d | Hoàn thành: %d | Tỷ lệ: %.0f%%",
                    total, completed, rate));

        } catch (SQLException e) {
            AlertUtil.showError("Lỗi", "Không thể tải dữ liệu: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handlePreviousMonth() {
        currentMonth = currentMonth.minusMonths(1);
        renderCalendar();
    }

    @FXML
    private void handleNextMonth() {
        currentMonth = currentMonth.plusMonths(1);
        renderCalendar();
    }

    @FXML
    private void handleToday() {
        currentMonth = YearMonth.now();
        selectedDate = LocalDate.now();
        renderCalendar();
        loadDayDetails(selectedDate);
    }

    @FXML
    private void handleClose() {
        // Close the window
        calendarGrid.getScene().getWindow().hide();
    }
}
