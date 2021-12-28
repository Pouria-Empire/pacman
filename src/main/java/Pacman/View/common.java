package Pacman.View;

import Pacman.Login;
import javafx.event.ActionEvent;

public interface common {
    default void back(ActionEvent actionEvent) {
        Login.getStageInClass().setScene(Login.loginScene);
    }
}
