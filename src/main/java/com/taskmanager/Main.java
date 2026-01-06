package com.taskmanager;

import com.taskmanager.dao.DatabaseConnection;
import com.taskmanager.util.AlertUtil;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.net.URL;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {

        try {
            log("=== APPLICATION START ===");

            // 1. Load FXML (DEBUG path)
            URL fxmlUrl = getClass().getResource("/fxml/main.fxml");
            log("FXML URL = " + fxmlUrl);

            if (fxmlUrl == null) {
                throw new RuntimeException("Không tìm thấy /fxml/main.fxml");
            }

            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent root = loader.load();

            // 2. Show UI trước
            Scene scene = new Scene(root);
            primaryStage.setTitle("Task Manager - Debug Mode");
            primaryStage.setScene(scene);
            primaryStage.setMinWidth(1000);
            primaryStage.setMinHeight(600);
            primaryStage.show();

            log("UI SHOWN SUCCESSFULLY");

            // 3. Init DB sau khi UI đã show
            new Thread(this::initDatabase).start();

        } catch (Exception e) {
            logException("STARTUP ERROR", e);
            Platform.runLater(() ->
                    AlertUtil.showError(
                            "Lỗi khởi động",
                            "Ứng dụng không thể khởi động.\nXem file startup-error.log"
                    )
            );
        }
    }

    private void initDatabase() {
        try {
            log("INIT DATABASE...");

            DatabaseConnection db = DatabaseConnection.getInstance();

            if (!db.testConnection()) {
                log("DATABASE CONNECTION FAILED");

                Platform.runLater(() ->
                        AlertUtil.showError(
                                "Lỗi Database",
                                "Không kết nối được MySQL.\n" +
                                        "Ứng dụng vẫn chạy nhưng chức năng DB bị tắt."
                        )
                );
            } else {
                log("DATABASE CONNECTED SUCCESSFULLY");
            }

        } catch (Exception e) {
            logException("DATABASE ERROR", e);
        }
    }

    @Override
    public void stop() {
        try {
            log("APPLICATION STOP");
            DatabaseConnection.getInstance().closeConnection();
        } catch (Exception e) {
            logException("STOP ERROR", e);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    // ================= LOGGING =================

    private static synchronized void log(String msg) {
        try (PrintWriter pw = new PrintWriter(new FileWriter("startup.log", true))) {
            pw.println(LocalDateTime.now() + " | " + msg);
        } catch (Exception ignored) {}
    }

    private static synchronized void logException(String title, Exception e) {
        try (PrintWriter pw = new PrintWriter(new FileWriter("startup-error.log", true))) {
            pw.println("==== " + title + " ====");
            pw.println(LocalDateTime.now());
            e.printStackTrace(pw);
        } catch (Exception ignored) {}
    }
}
