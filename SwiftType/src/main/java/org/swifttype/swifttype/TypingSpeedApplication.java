package org.swifttype.swifttype;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TypingSpeedApplication extends Application {
    private long startTime;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        Label promptLabel = new Label("여기에 텍스트를 입력하세요:");
        TextField typingField = new TextField();
        Label speedLabel = new Label("타자 속도: 0 WPM");

        // 타이핑 시작 시간 기록
        typingField.setOnKeyPressed(event -> {
            if (startTime == 0) {
                startTime = System.currentTimeMillis();
            }
        });

        // 실시간 타자 속도 계산
        typingField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                long elapsedTime = System.currentTimeMillis() - startTime;
                double minutes = elapsedTime / 60000.0;
                int words = newValue.split("\\s+").length;
                double speed = minutes > 0 ? words / minutes : 0;
                speedLabel.setText(String.format("타자 속도: %.2f WPM", speed));
            }
        });

        VBox layout = new VBox(10, promptLabel, typingField, speedLabel);
        Scene scene = new Scene(layout, 400, 200);
        stage.setTitle("타자 속도 테스트");
        stage.setScene(scene);
        stage.show();
    }
}