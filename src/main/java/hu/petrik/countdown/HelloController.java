package hu.petrik.countdown;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HelloController {
    @FXML
    private TextField dateInput;
    @FXML
    private Label countdownLabel;
    @FXML
    private Button startButton;
    @FXML
    private VBox root;

    public void initialize() {
        countdownLabel.setText("Kérlek, adj meg egy dátumot!");
    }

    @FXML
    public void onStartButtonClicked() {
        String inputDate = dateInput.getText().trim();
        if (validateDate(inputDate)) {
            LocalDateTime targetDate = LocalDateTime.parse(inputDate, DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss"));
            LocalDateTime currentDate = LocalDateTime.now();

            if (targetDate.isBefore(currentDate)) {
                showAlert("Hiba", "A megadott időpont már elmúlt!");
            } else {
                startCountdown(targetDate);
            }
        } else {
            showAlert("Hiba", "Érvénytelen dátum formátum! Használja az 'yyyy.MM.dd HH:mm:ss' formátumot.");
        }
    }

    private boolean validateDate(String inputDate) {
        String regex = "\\d{4}\\.\\d{2}\\.\\d{2} \\d{2}:\\d{2}:\\d{2}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(inputDate);
        return matcher.matches();
    }

    private void startCountdown(LocalDateTime targetDate) {
        new Thread(() -> {
            while (true) {
                LocalDateTime currentDate = LocalDateTime.now();
                if (targetDate.isBefore(currentDate)) {
                    break;
                }

                Duration duration = Duration.between(currentDate, targetDate);
                Period period = Period.between(currentDate.toLocalDate(), targetDate.toLocalDate());

                String countdownText = String.format("%d év %d hó %d nap %02d:%02d:%02d",
                        period.getYears(),
                        period.getMonths(),
                        period.getDays(),
                        duration.toHoursPart(),
                        duration.toMinutesPart(),
                        duration.toSecondsPart());

                updateLabel(countdownText);

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            showAlert("Idő lejárt!", "A visszaszámlálás lejárt!");
        }).start();
    }

    private void updateLabel(String countdownText) {
        javafx.application.Platform.runLater(() -> countdownLabel.setText(countdownText));
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

