package org.swifttype.swifttype;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
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
    private boolean isWpm = false;
    private TextArea sampleTextArea;


    @Override
    public void start(Stage stage) {
        // 폰트 로드
        Font pretendard = Font.loadFont(getClass().getResourceAsStream("/fonts/PretendardVariable.ttf"), 14);

        Label promptLabel = new Label("아래 텍스트를 입력하세요:");
        promptLabel.setFont(pretendard);

        sampleTextArea = new TextArea(getRandomText(sampleTexts));
        sampleTextArea.setEditable(false);
        sampleTextArea.setFont(pretendard);

        typingField = new TextField();
        typingField.setFont(pretendard);

        speedLabel = new Label("타자 속도: 0 타");
        speedLabel.setFont(pretendard);

        accuracyLabel = new Label("정확도: 100%");
        accuracyLabel.setFont(pretendard);

        Button restartButton = new Button("재시작");
        restartButton.setFont(pretendard);
        restartButton.setOnAction(e -> restartTyping());

        Button closeButton = new Button("종료");
        closeButton.setFont(pretendard);
        closeButton.setOnAction(e -> stage.close());

        Button toggleButton = new Button("한글/영어 전환");
        toggleButton.setFont(pretendard);
        toggleButton.setOnAction(e -> toggleLanguage(sampleTextArea));

        RadioButton cpmButton = new RadioButton("타");
        cpmButton.setFont(pretendard);
        cpmButton.setSelected(true);

        RadioButton wpmButton = new RadioButton("WPM");
        wpmButton.setFont(pretendard);

        ToggleGroup speedTypeGroup = new ToggleGroup();
        cpmButton.setToggleGroup(speedTypeGroup);
        wpmButton.setToggleGroup(speedTypeGroup);

        cpmButton.setOnAction(e -> isWpm = false);
        wpmButton.setOnAction(e -> isWpm = true);

        HBox speedTypeSelection = new HBox(10, cpmButton, wpmButton);
        speedTypeSelection.setAlignment(Pos.CENTER);

        typingField.setOnKeyPressed(e -> {
            if (startTime == 0) {
                startTime = System.currentTimeMillis();
            }
        });

        typingField.textProperty().addListener((observable, oldValue, newValue) -> calculateSpeedAndAccuracy(newValue));

        HBox buttonBox = new HBox(10, restartButton, toggleButton, closeButton);
        buttonBox.setAlignment(Pos.CENTER);

        VBox layout = new VBox(10, promptLabel, sampleTextArea, typingField, speedLabel, accuracyLabel, speedTypeSelection, buttonBox);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 600, 400);
        stage.setTitle("Swift Type");
        stage.setScene(scene);
        stage.show();
    }

    private void calculateSpeedAndAccuracy(String newValue) {
        if (!newValue.isEmpty() && startTime > 0) {
            long elapsedTime = System.currentTimeMillis() - startTime;
            double minutes = elapsedTime / 60000.0;
            if (isWpm) {
                int words = newValue.trim().split("\\s+").length;
                double wpm = minutes > 0 ? words / minutes : 0;
                speedLabel.setText(String.format("타자 속도: %.2f WPM", wpm));
            } else {
                int characters = newValue.length();
                double cpm = minutes > 0 ? characters / minutes : 0;
                speedLabel.setText(String.format("타자 속도: %.0f 타", cpm));
            }
        }
    }

    private void restartTyping() {
        sampleTextArea.setText(getRandomText(isWpm ? englishSampleTexts : sampleTexts));
        typingField.clear();
        speedLabel.setText(isWpm ? "타자 속도: 0 타" : "타자 속도: 0 WPM");
        accuracyLabel.setText("정확도: 100%");
        startTime = 0;
    }

    private String getRandomText(String[] texts) {
        Random random = new Random();
        return texts[random.nextInt(texts.length)];
    }

    private void toggleLanguage(TextArea sampleTextArea) {
        if (Arrays.asList(sampleTexts).contains(sampleTextArea.getText())) {
            sampleTextArea.setText(getRandomText(englishSampleTexts));
        } else {
            sampleTextArea.setText(getRandomText(sampleTexts));
        }
        restartTyping();
    }

    public static void main(String[] args) {
        launch(args);
    }
}