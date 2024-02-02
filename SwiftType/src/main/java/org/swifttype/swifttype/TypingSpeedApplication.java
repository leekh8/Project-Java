package org.swifttype.swifttype;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.Random;

public class TypingSpeedApplication extends Application {
    private final String[] sampleTexts = {
            "파란 하늘을 나는 새가 보고 싶다",
            "빠르게 달리는 강물처럼 시간은 흐른다",
            "행복은 가까이에 있을지도 모른다",
            "조용한 아침의 첫 번째 커피처럼",
            "바쁜 일상 속에서도 미소를 잃지 않기",
            "작은 것에서 행복을 찾는 것이 중요하다",
            "매일 매일이 새로운 시작이 될 수 있다",
            "꿈을 향해 한 걸음씩 나아가기",
            "좋은 생각은 좋은 결과를 가져온다",
            "힘든 시간도 결국 지나가기 마련이다"
    };

    private final String[] englishSampleTexts = {
            "The quick brown fox jumps over the lazy dog",
            "A journey of a thousand miles begins with a single step",
            "To be or not to be, that is the question",
            "Every moment is a fresh beginning",
            "Believe you can and you're halfway there",
            "Dream big and dare to fail",
            "Keep your face always toward the sunshine and shadows will fall behind you",
            "The only way to do great work is to love what you do",
            "Success is not the key to happiness. Happiness is the key to success",
            "Life is 10% what happens to us and 90% how we react to it"
    };
    private long startTime;
    private TextField typingField;
    private Label speedLabel;
    private Label accuracyLabel;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        // 폰트 로드
        Font pretendard =Font.loadFont(getClass().getResourceAsStream("/fonts/PretendardVariable.ttf"), 14);

        // UI 컴포넌트
        Label promptLabel = new Label("아래 텍스트를 입력하세요:");
        TextArea sampleTextArea = new TextArea(getRandomText(sampleTexts));
        sampleTextArea.setEditable(false);
        typingField = new TextField();
        speedLabel = new Label("타자 속도: 0 WPM");
        accuracyLabel = new Label("정확도: 100%");
        Button restartButton = new Button("재시작");
        Button closeButton = new Button("종료");

        // 폰트 적용
        promptLabel.setFont(pretendard);
        sampleTextArea.setFont(pretendard);
        typingField.setFont(pretendard);
        speedLabel.setFont(pretendard);
        accuracyLabel.setFont(pretendard);
        restartButton.setFont(pretendard);
        closeButton.setFont(pretendard);

        closeButton.setOnAction(event -> stage.close());

        Button toggleButton = new Button("한글/영어 전환");
        toggleButton.setOnAction(event -> {
            if (Arrays.asList(sampleTexts).contains(sampleTextArea.getText())) {
                sampleTextArea.setText(getRandomText(englishSampleTexts));
            } else {
                sampleTextArea.setText(getRandomText(sampleTexts));
            }
            restartTyping();
        });

        typingField.setOnKeyPressed(event -> {
            if (startTime == 0) {
                startTime = System.currentTimeMillis();
            }
        });

        typingField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                long elapsedTime = System.currentTimeMillis() - startTime;
                double minutes = elapsedTime / 60000.0;
                int words = newValue.split("\\s+").length;
                double speed = minutes > 0 ? words / minutes : 0;
                speedLabel.setText(String.format("타자 속도: %.2f WPM", speed));

                double accuracy = calculateAccuracy(sampleTextArea.getText(), newValue);
                accuracyLabel.setText(String.format("정확도: %.2f%%", accuracy));
            }
        });

        restartButton.setOnAction(event -> {
            sampleTextArea.setText(getRandomText(sampleTexts));
            restartTyping();
        });

        HBox buttonBox = new HBox(10, restartButton, toggleButton, closeButton);
        buttonBox.setAlignment(Pos.CENTER);

        VBox layout = new VBox(10, promptLabel, sampleTextArea, typingField, speedLabel, accuracyLabel, buttonBox);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout, 600, 400);

        stage.setTitle("타자 속도 테스트");
        stage.setScene(scene);
        stage.show();
    }

    private void restartTyping() {
        typingField.clear();
        speedLabel.setText("타자 속도: 0 WPM");
        accuracyLabel.setText("정확도: 100%");
        startTime = 0;
    }

    private String getRandomText(String[] texts) {
        Random random = new Random();
        return texts[random.nextInt(texts.length)];
    }

    private double calculateAccuracy(String original, String typed) {
        int errors = 0;
        for (int i = 0; i < Math.min(original.length(), typed.length()); i++) {
            if (original.charAt(i) != typed.charAt(i)) {
                errors++;
            }
        }
        errors += Math.abs(original.length() - typed.length());
        return original.length() > 0 ? 100.0 * (original.length() - errors) / original.length() : 100.0;
    }
}
