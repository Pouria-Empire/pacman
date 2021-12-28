package Pacman.Controllers;

import Pacman.Database.DataBase;
import Pacman.Login;
import Pacman.Model.User;
import Pacman.View.MainMenu;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.time.LocalDateTime;
import java.util.Objects;

public class LoginController {

    public TextField usernameField;
    public PasswordField passwordField;
    public Text actionTarget;

    public void handleSignIn(ActionEvent actionEvent) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        if(isExistUsername(username)) {
            new Alert(Alert.AlertType.WARNING, "This Username already Exist!").showAndWait();
            actionTarget.setText("This Username already Exist!");
        }
        else {
            new Alert(Alert.AlertType.INFORMATION,"SignIn successfully").showAndWait();
            actionTarget.setText("SignIn successfully..");
            User user = new User(username,password);
            user.setLastChangedMaxScoreTime(LocalDateTime.now());
            user.setMaxScore(0);
            DataBase.saveTheUserList(User.getUsers());
        }
        usernameField.clear();
        passwordField.clear();
    }

    public void handleLogin(ActionEvent actionEvent) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        User user = User.getUserByUsername(username);
        //checking correct password
        if(Objects.isNull(user)){
            new Alert(Alert.AlertType.WARNING, "Username notFound!").showAndWait();
            actionTarget.setText("Username notFound!");
        }
        else if(!user.getPassword().equals(password)){
            new Alert(Alert.AlertType.WARNING, "Password Not Matched!").showAndWait();
            actionTarget.setText("Password Not Matched!!");
        }
        else {
            new Alert(Alert.AlertType.INFORMATION,"Login successfully").showAndWait();
            actionTarget.setText("");
            user.setIsLoggedIn(true);
//            user.setStartGameMenu(null);
            //run main menu
            new MainMenu(User.getUserByUsername(username)).startMainMenu(Login.getStageInClass());
        }
        usernameField.clear();
        passwordField.clear();
    }

    private static boolean isExistUsername(String username){
        return !Objects.isNull(User.getUserByUsername(username));
    }

    public void loginWithOutUser(ActionEvent actionEvent) {
        new MainMenu(null).startMainMenu(Login.getStageInClass());
    }
}
