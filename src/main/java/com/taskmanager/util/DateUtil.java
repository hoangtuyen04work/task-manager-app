package com.taskmanager.util;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;

/**
 * Utility class để xử lý các thao tác liên quan đến ngày tháng
 */
public class DateUtil {
    
    private static final DateTimeFormatter DISPLAY_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;
    
    /**
     * Format LocalDate thành chuỗi theo định dạng dd/MM/yyyy
     */
    public static String formatDate(LocalDate date) {
        if (date == null) {
            return "";
        }
        return date.format(DISPLAY_FORMATTER);
    }
    
    /**
     * Parse chuỗi ngày từ định dạng dd/MM/yyyy thành LocalDate
     */
    public static LocalDate parseDate(String dateString) {
        if (dateString == null || dateString.trim().isEmpty()) {
            return null;
        }
        try {
            return LocalDate.parse(dateString, DISPLAY_FORMATTER);
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * Lấy ngày đầu tuần (Thứ 2)
     */
    public static LocalDate getStartOfWeek(LocalDate date) {
        if (date == null) {
            date = LocalDate.now();
        }
        return date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
    }
    
    /**
     * Lấy ngày cuối tuần (Chủ nhật)
     */
    public static LocalDate getEndOfWeek(LocalDate date) {
        if (date == null) {
            date = LocalDate.now();
        }
        return date.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
    }
    
    /**
     * Lấy ngày đầu tháng
     */
    public static LocalDate getStartOfMonth(LocalDate date) {
        if (date == null) {
            date = LocalDate.now();
        }
        return date.with(TemporalAdjusters.firstDayOfMonth());
    }
    
    /**
     * Lấy ngày cuối tháng
     */
    public static LocalDate getEndOfMonth(LocalDate date) {
        if (date == null) {
            date = LocalDate.now();
        }
        return date.with(TemporalAdjusters.lastDayOfMonth());
    }
    
    /**
     * Tính số ngày giữa 2 ngày
     */
    public static long getDaysBetween(LocalDate start, LocalDate end) {
        if (start == null || end == null) {
            return 0;
        }
        return ChronoUnit.DAYS.between(start, end);
    }
    
    /**
     * Kiểm tra có phải ngày hôm nay không
     */
    public static boolean isToday(LocalDate date) {
        return date != null && date.equals(LocalDate.now());
    }
    
    /**
     * Kiểm tra có phải ngày hôm qua không
     */
    public static boolean isYesterday(LocalDate date) {
        return date != null && date.equals(LocalDate.now().minusDays(1));
    }
    
    /**
     * Kiểm tra có phải ngày mai không
     */
    public static boolean isTomorrow(LocalDate date) {
        return date != null && date.equals(LocalDate.now().plusDays(1));
    }
    
    /**
     * Lấy ngày cách đây n ngày
     */
    public static LocalDate getDaysAgo(int days) {
        return LocalDate.now().minusDays(days);
    }
    
    /**
     * Lấy ngày sau n ngày
     */
    public static LocalDate getDaysFromNow(int days) {
        return LocalDate.now().plusDays(days);
    }
}
