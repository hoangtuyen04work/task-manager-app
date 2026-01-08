package com.taskmanager.controller;

import com.taskmanager.dao.TaskDAO;
import com.taskmanager.model.DayStatistics;
import com.taskmanager.model.Task;
import com.taskmanager.model.Task.Priority;
import com.taskmanager.model.TaskStatistics;
import com.taskmanager.util.AlertUtil;
import com.taskmanager.util.DateUtil;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.*;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
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
    
    // Heatmap tab
    @FXML private VBox heatmapContainer;
    @FXML private Label lblHeatmapDays;
    @FXML private Label lblHeatmapActiveDays;
    @FXML private Label lblHeatmapAverage;
    @FXML private Label lblHeatmapStreak;
    
    private TaskDAO taskDAO;
    private TaskStatistics statistics;
    private int heatmapMonths = 6; // Default 6 months
    
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
            updateHeatmap();
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
    
    @FXML
    private void handleHeatmap6Months() {
        heatmapMonths = 6;
        updateHeatmap();
    }
    
    @FXML
    private void handleHeatmap1Year() {
        heatmapMonths = 12;
        updateHeatmap();
    }
    
    /**
     * Cập nhật Heatmap giống Anki
     */
    private void updateHeatmap() {
        if (heatmapContainer == null) return;
        
        heatmapContainer.getChildren().clear();
        
        try {
            // Tính ngày bắt đầu và kết thúc cho heatmap
            LocalDate endDate = LocalDate.now();
            LocalDate startDate = endDate.minusMonths(heatmapMonths);
            
            // Lấy dữ liệu thống kê từ database
            Map<LocalDate, DayStatistics> statsMap = taskDAO.getStatisticsByDateRange(startDate, endDate);
            
            // Tính toán max tasks để normalize màu sắc
            int maxTasks = statsMap.values().stream()
                .mapToInt(DayStatistics::getCompletedTasks)
                .max()
                .orElse(1);
            
            // Tạo heatmap grid
            createHeatmapGrid(startDate, endDate, statsMap, maxTasks);
            
            // Cập nhật stats summary
            updateHeatmapStats(statsMap, startDate, endDate);
            
        } catch (SQLException e) {
            AlertUtil.showError("Lỗi", "Không thể tải dữ liệu heatmap: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Tạo grid cho heatmap (giống Anki)
     */
    private void createHeatmapGrid(LocalDate startDate, LocalDate endDate, 
                                   Map<LocalDate, DayStatistics> statsMap, int maxTasks) {
        // Điều chỉnh startDate về đầu tuần (Monday)
        while (startDate.getDayOfWeek().getValue() != 1) {
            startDate = startDate.minusDays(1);
        }
        
        // Tính số tuần cần hiển thị
        long totalDays = ChronoUnit.DAYS.between(startDate, endDate) + 1;
        int totalWeeks = (int) Math.ceil(totalDays / 7.0);
        
        // Container chính cho tất cả các tháng
        HBox monthsContainer = new HBox(15);
        monthsContainer.setAlignment(Pos.TOP_LEFT);
        
        // Tạo heatmap theo tháng
        LocalDate currentDate = startDate;
        VBox currentMonthBox = null;
        GridPane currentGrid = null;
        String currentMonthName = "";
        int weekInMonth = 0;
        
        for (int week = 0; week < totalWeeks; week++) {
            // Kiểm tra xem có chuyển sang tháng mới không
            String monthName = currentDate.getMonth().getDisplayName(TextStyle.SHORT, new Locale("vi")) + 
                              " '" + String.format("%02d", currentDate.getYear() % 100);
            
            if (!monthName.equals(currentMonthName) || currentMonthBox == null) {
                // Tạo box mới cho tháng mới
                currentMonthBox = new VBox(5);
                currentMonthBox.setAlignment(Pos.TOP_LEFT);
                
                // Label tháng
                Label monthLabel = new Label(monthName);
                monthLabel.setStyle("-fx-font-size: 11px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
                currentMonthBox.getChildren().add(monthLabel);
                
                // Grid mới cho tháng
                currentGrid = new GridPane();
                currentGrid.setHgap(3);
                currentGrid.setVgap(3);
                
                // Thêm labels cho các ngày trong tuần
                String[] dayLabels = {"T2", "T3", "T4", "T5", "T6", "T7", "CN"};
                for (int day = 0; day < 7; day++) {
                    Label dayLabel = new Label(dayLabels[day]);
                    dayLabel.setStyle("-fx-font-size: 9px; -fx-text-fill: #7f8c8d;");
                    dayLabel.setPrefWidth(14);
                    dayLabel.setAlignment(Pos.CENTER);
                    currentGrid.add(dayLabel, 0, day);
                }
                
                currentMonthBox.getChildren().add(currentGrid);
                monthsContainer.getChildren().add(currentMonthBox);
                
                currentMonthName = monthName;
                weekInMonth = 0;
            }
            
            // Tạo cells cho tuần này
            for (int dayOfWeek = 0; dayOfWeek < 7; dayOfWeek++) {
                if (currentDate.isAfter(endDate)) break;
                
                Region cell = createHeatmapCell(currentDate, statsMap, maxTasks);
                currentGrid.add(cell, weekInMonth + 1, dayOfWeek);
                
                currentDate = currentDate.plusDays(1);
            }
            
            weekInMonth++;
        }
        
        heatmapContainer.getChildren().add(monthsContainer);
    }
    
    /**
     * Tạo một cell cho heatmap
     */
    private Region createHeatmapCell(LocalDate date, Map<LocalDate, DayStatistics> statsMap, int maxTasks) {
        Region cell = new Region();
        cell.setPrefSize(14, 14);
        cell.setMinSize(14, 14);
        cell.setMaxSize(14, 14);
        
        DayStatistics stats = statsMap.get(date);
        int completedTasks = (stats != null) ? stats.getCompletedTasks() : 0;
        
        // Xác định level (0-4) dựa trên số tasks hoàn thành
        int level = calculateHeatLevel(completedTasks, maxTasks);
        
        // Set style class theo level
        cell.getStyleClass().addAll("heatmap-cell", "heatmap-level-" + level);
        
        // Thêm tooltip
        String tooltipText = DateUtil.formatDate(date) + "\n" +
                            "Hoàn thành: " + completedTasks + " việc";
        if (stats != null) {
            tooltipText += "\nTổng: " + stats.getTotalTasks() + " việc";
        }
        
        Tooltip tooltip = new Tooltip(tooltipText);
        tooltip.setShowDelay(Duration.millis(300));
        Tooltip.install(cell, tooltip);
        
        return cell;
    }
    
    /**
     * Tính level cho màu (0-4)
     * 0 = không có việc
     * 1 = 1-25% của max
     * 2 = 26-50% của max
     * 3 = 51-75% của max
     * 4 = 76-100% của max
     */
    private int calculateHeatLevel(int tasks, int maxTasks) {
        if (tasks == 0 || maxTasks == 0) return 0;
        
        double percentage = (double) tasks / maxTasks;
        
        if (percentage <= 0.25) return 1;
        if (percentage <= 0.50) return 2;
        if (percentage <= 0.75) return 3;
        return 4;
    }
    
    /**
     * Cập nhật stats summary cho heatmap
     */
    private void updateHeatmapStats(Map<LocalDate, DayStatistics> statsMap, 
                                   LocalDate startDate, LocalDate endDate) {
        long totalDays = ChronoUnit.DAYS.between(startDate, endDate) + 1;
        int activeDays = statsMap.size();
        
        int totalCompleted = statsMap.values().stream()
            .mapToInt(DayStatistics::getCompletedTasks)
            .sum();
        
        double average = activeDays > 0 ? (double) totalCompleted / activeDays : 0;
        
        // Tính longest streak
        int longestStreak = calculateLongestStreak(statsMap, startDate, endDate);
        
        lblHeatmapDays.setText(String.valueOf(totalDays));
        lblHeatmapActiveDays.setText(String.valueOf(activeDays));
        lblHeatmapAverage.setText(String.format("%.1f việc", average));
        lblHeatmapStreak.setText(longestStreak + " ngày");
    }
    
    /**
     * Tính chuỗi ngày dài nhất có hoàn thành task
     */
    private int calculateLongestStreak(Map<LocalDate, DayStatistics> statsMap, 
                                      LocalDate startDate, LocalDate endDate) {
        int longestStreak = 0;
        int currentStreak = 0;
        
        LocalDate date = startDate;
        while (!date.isAfter(endDate)) {
            DayStatistics stats = statsMap.get(date);
            if (stats != null && stats.getCompletedTasks() > 0) {
                currentStreak++;
                longestStreak = Math.max(longestStreak, currentStreak);
            } else {
                currentStreak = 0;
            }
            date = date.plusDays(1);
        }
        
        return longestStreak;
    }
}
