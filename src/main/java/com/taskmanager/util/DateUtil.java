package com.taskmanager.util;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;


public class DateUtil {
    
    private static final DateTimeFormatter DISPLAY_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;
    

    public static String formatDate(LocalDate date) {
        if (date == null) {
            return "";
        }
        return date.format(DISPLAY_FORMATTER);
    }
    

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
    
    public static LocalDate getStartOfWeek(LocalDate date) {
        if (date == null) {
            date = LocalDate.now();
        }
        return date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
    }
    
    public static LocalDate getEndOfWeek(LocalDate date) {
        if (date == null) {
            date = LocalDate.now();
        }
        return date.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
    }
    
    public static LocalDate getStartOfMonth(LocalDate date) {
        if (date == null) {
            date = LocalDate.now();
        }
        return date.with(TemporalAdjusters.firstDayOfMonth());
    }
    
    public static LocalDate getEndOfMonth(LocalDate date) {
        if (date == null) {
            date = LocalDate.now();
        }
        return date.with(TemporalAdjusters.lastDayOfMonth());
    }
    
    public static long getDaysBetween(LocalDate start, LocalDate end) {
        if (start == null || end == null) {
            return 0;
        }
        return ChronoUnit.DAYS.between(start, end);
    }
    
    public static boolean isToday(LocalDate date) {
        return date != null && date.equals(LocalDate.now());
    }
    
    public static boolean isYesterday(LocalDate date) {
        return date != null && date.equals(LocalDate.now().minusDays(1));
    }
    
    public static boolean isTomorrow(LocalDate date) {
        return date != null && date.equals(LocalDate.now().plusDays(1));
    }
    
    public static LocalDate getDaysAgo(int days) {
        return LocalDate.now().minusDays(days);
    }
    
    public static LocalDate getDaysFromNow(int days) {
        return LocalDate.now().plusDays(days);
    }
}
