package com.taskmanager;

/**
 * Launcher class để khởi động JavaFX application
 * Class này cần thiết để tránh lỗi "JavaFX runtime components are missing"
 * khi chạy từ JAR file hoặc EXE
 */
public class Launcher {
    public static void main(String[] args) {
        Main.main(args);
    }
}
