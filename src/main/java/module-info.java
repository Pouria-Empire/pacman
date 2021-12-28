module graphic {
    requires javafx.graphics;
    requires javafx.fxml;
    requires javafx.controls;
    requires java.desktop;
    requires org.testng;
    requires junit;
    requires org.junit.jupiter.api;
    requires javafx.media;

    opens Pacman to javafx.media, javafx.fxml;
    exports Pacman;
    exports Pacman.Controllers;
    exports Pacman.Model to javafx.fxml;
    exports Pacman.View to javafx.fxml;
    opens Pacman.View  to javafx.fxml;
}