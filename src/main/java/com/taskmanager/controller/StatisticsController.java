package com.taskmanager.controller;

import com.taskmanager.dao.TaskDAO;
import com.taskmanager.model.Task;
import com.taskmanager.model.Task.Priority;
import com.taskmanager.model.TaskStatistics;
import com.taskmanager.util.AlertUtil;
import com.taskmanager.util.DateUtil;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

/**
 * Controller cho Statistics Window
 * Hiển thị các thống kê và biểu đồ
 */
public class StatisticsController {
    
    @FXML private DatePicker dateFrom;
    @FXML private DatePicker dateTo;
    
    // Overview tab
    @FXML private Label lblOverallTotal;
    @FXML private Label lblOverallCompleted;
    @FXML private Label lblOverallPending;
    @FXML private Label lblAverageRate;
    
    @FXML private Label lblHighTotal;
    @FXML private Label lblHighCompleted;
    @FXML private Label lblMediumTotal;
    @FXML private Label lblMediumCompleted;
    @FXML private Label lblLowTotal;
    @FXML private Label lblLowCompleted;
    
    // Charts tab
    @FXML private BarChart<String, Number> barChart;
    @FXML private LineChart<String, Number> lineChart;
    @FXML private PieChart pieChart;
    
    private TaskDAO taskDAO;
    private TaskStatistics statistics;
    
    /**
     * Initialize controller
     */
    @FXML
    public void initialize() {
        taskDAO = new TaskDAO();
        statistics = new TaskStatistics();
        
        // Set default date range to last 7 days
        dateTo.setValue(LocalDate.now());
        dateFrom.setValue(LocalDate.now().minusDays(6));
        
        // Load initial data
        loadStatistics();
    }
    
    /**
     * Load statistics data
     */
    private void loadStatistics() {
        LocalDate start = dateFrom.getValue();
        LocalDate end = dateTo.getValue();
        
        if (start == null || end == null) {
            AlertUtil.showWarning("Cảnh báo", "Vui lòng chọn khoảng thời gian");
            return;
        }
        
        if (start.isAfter(end)) {
            AlertUtil.showWarning("Cảnh báo", "Ngày bắt đầu phải trước ngày kết thúc");
            return;
        }
        
        try {
            List<Task> tasks = taskDAO.findByDateRange(start, end);
            calculateStatistics(tasks);
            updateOverviewTab();
            updateChartsTab();
        } catch (SQLException e) {
            AlertUtil.showError("Lỗi", "Không thể tải thống kê: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Tính toán thống kê từ danh sách tasks
     */
    private void calculateStatistics(List<Task> tasks) {
        statistics = new TaskStatistics();
        
        // Overall stats
        int total = tasks.size();
        long completed = tasks.stream().filter(Task::isCompleted).count();
        int pending = total - (int) completed;
        
        statistics.setTotalTasks(total);
        statistics.setCompletedTasks((int) completed);
        statistics.setPendingTasks(pending);
        
        // Average completion rate
        double avgRate = total > 0 ? (completed * 100.0 / total) : 0.0;
        statistics.setAverageCompletionRate(avgRate);
        
        // Stats by priority
        Map<Priority, Integer> tasksByPriority = new HashMap<>();
        Map<Priority, Integer> completedByPriority = new HashMap<>();
        
        for (Priority priority : Priority.values()) {
            tasksByPriority.put(priority, 0);
            completedByPriority.put(priority, 0);
        }
        
        for (Task task : tasks) {
            Priority p = task.getPriority();
            tasksByPriority.put(p, tasksByPriority.get(p) + 1);
            if (task.isCompleted()) {
                completedByPriority.put(p, completedByPriority.get(p) + 1);
            }
        }
        
        statistics.setTasksByPriority(tasksByPriority);
        statistics.setCompletedByPriority(completedByPriority);
        
        // Stats by date
        Map<LocalDate, Integer> tasksByDate = new HashMap<>();
        Map<LocalDate, Integer> completedByDate = new HashMap<>();
        Map<LocalDate, Double> completionRateByDate = new HashMap<>();
        
        for (Task task : tasks) {
            LocalDate date = task.getTaskDate();
            tasksByDate.put(date, tasksByDate.getOrDefault(date, 0) + 1);
            if (task.isCompleted()) {
                completedByDate.put(date, completedByDate.getOrDefault(date, 0) + 1);
            }
        }
        
        // Calculate completion rate by date
        for (LocalDate date : tasksByDate.keySet()) {
            int totalForDate = tasksByDate.get(date);
            int completedForDate = completedByDate.getOrDefault(date, 0);
            double rate = totalForDate > 0 ? (completedForDate * 100.0 / totalForDate) : 0.0;
            completionRateByDate.put(date, rate);
        }
        
        statistics.setTasksByDate(tasksByDate);
        statistics.setCompletedByDate(completedByDate);
        statistics.setCompletionRateByDate(completionRateByDate);
        
        // Top performance days
        Map<LocalDate, Double> topDays = new LinkedHashMap<>();
        completionRateByDate.entrySet().stream()
            .sorted(Map.Entry.<LocalDate, Double>comparingByValue().reversed())
            .limit(5)
            .forEach(entry -> topDays.put(entry.getKey(), entry.getValue()));
        
        statistics.setTopPerformanceDays(topDays);
    }
    
    /**
     * Cập nhật tab tổng quan
     */
    private void updateOverviewTab() {
        // Overall stats
        lblOverallTotal.setText(String.valueOf(statistics.getTotalTasks()));
        lblOverallCompleted.setText(String.valueOf(statistics.getCompletedTasks()));
        lblOverallPending.setText(String.valueOf(statistics.getPendingTasks()));
        lblAverageRate.setText(String.format("%.0f%%", statistics.getAverageCompletionRate()));
        
        // Priority stats
        Map<Priority, Integer> byPriority = statistics.getTasksByPriority();
        Map<Priority, Integer> completedByPriority = statistics.getCompletedByPriority();
        
        lblHighTotal.setText(byPriority.get(Priority.HIGH) + " tasks");
        lblHighCompleted.setText("(" + completedByPriority.get(Priority.HIGH) + " hoàn thành)");
        
        lblMediumTotal.setText(byPriority.get(Priority.MEDIUM) + " tasks");
        lblMediumCompleted.setText("(" + completedByPriority.get(Priority.MEDIUM) + " hoàn thành)");
        
        lblLowTotal.setText(byPriority.get(Priority.LOW) + " tasks");
        lblLowCompleted.setText("(" + completedByPriority.get(Priority.LOW) + " hoàn thành)");
    }
    
    /**
     * Cập nhật tab biểu đồ
     */
    private void updateChartsTab() {
        updateBarChart();
        updateLineChart();
        updatePieChart();
    }
    
    /**
     * Cập nhật biểu đồ cột (tasks by date)
     */
    private void updateBarChart() {
        barChart.getData().clear();
        
        XYChart.Series<String, Number> totalSeries = new XYChart.Series<>();
        totalSeries.setName("Tổng số");
        
        XYChart.Series<String, Number> completedSeries = new XYChart.Series<>();
        completedSeries.setName("Hoàn thành");
        
        // Sort dates
        List<LocalDate> sortedDates = new ArrayList<>(statistics.getTasksByDate().keySet());
        Collections.sort(sortedDates);
        
        for (LocalDate date : sortedDates) {
            String dateStr = DateUtil.formatDate(date);
            int total = statistics.getTasksByDate().get(date);
            int completed = statistics.getCompletedByDate().getOrDefault(date, 0);
            
            totalSeries.getData().add(new XYChart.Data<>(dateStr, total));
            completedSeries.getData().add(new XYChart.Data<>(dateStr, completed));
        }
        
        barChart.getData().addAll(totalSeries, completedSeries);
    }
    
    /**
     * Cập nhật biểu đồ đường (completion rate by date)
     */
    private void updateLineChart() {
        lineChart.getData().clear();
        
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Tỷ lệ hoàn thành");
        
        // Sort dates
        List<LocalDate> sortedDates = new ArrayList<>(statistics.getCompletionRateByDate().keySet());
        Collections.sort(sortedDates);
        
        for (LocalDate date : sortedDates) {
            String dateStr = DateUtil.formatDate(date);
            double rate = statistics.getCompletionRateByDate().get(date);
            series.getData().add(new XYChart.Data<>(dateStr, rate));
        }
        
        lineChart.getData().add(series);
    }
    
    /**
     * Cập nhật biểu đồ tròn (priority distribution)
     */
    private void updatePieChart() {
        pieChart.getData().clear();
        
        Map<Priority, Integer> byPriority = statistics.getTasksByPriority();
        
        for (Priority priority : Priority.values()) {
            int count = byPriority.get(priority);
            if (count > 0) {
                PieChart.Data data = new PieChart.Data(
                    priority.getDisplayName() + " (" + count + ")", 
                    count
                );
                pieChart.getData().add(data);
            }
        }
    }
    
    // Event Handlers
    
    @FXML
    private void handleLast7Days() {
        dateTo.setValue(LocalDate.now());
        dateFrom.setValue(LocalDate.now().minusDays(6));
        loadStatistics();
    }
    
    @FXML
    private void handleLast30Days() {
        dateTo.setValue(LocalDate.now());
        dateFrom.setValue(LocalDate.now().minusDays(29));
        loadStatistics();
    }
    
    @FXML
    private void handleThisMonth() {
        dateTo.setValue(DateUtil.getEndOfMonth(LocalDate.now()));
        dateFrom.setValue(DateUtil.getStartOfMonth(LocalDate.now()));
        loadStatistics();
    }
    
    @FXML
    private void handleDateRangeChange() {
        loadStatistics();
    }
    
    @FXML
    private void handleRefresh() {
        loadStatistics();
    }
}
