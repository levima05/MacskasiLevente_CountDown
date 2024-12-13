module hu.petrik.countdown {
    requires javafx.controls;
    requires javafx.fxml;


    opens hu.petrik.countdown to javafx.fxml;
    exports hu.petrik.countdown;
}