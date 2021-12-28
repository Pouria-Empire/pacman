package Pacman.View;

import Pacman.Database.DataBase;
import Pacman.Login;
import Pacman.Model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class ProfileMenu implements common{
    private static ProfileMenu singleToneClass = null;
    public TextField oldPass;
    public PasswordField newPass;
    public Text actionTarget;

    public static ProfileMenu getInstance() {
        if(singleToneClass == null) singleToneClass = new ProfileMenu();
        return singleToneClass;
    }

    public void runProfileMenu() throws IOException {
        System.out.println(getClass().toString());
        System.out.println(Objects.requireNonNull(getClass().getResource("ProfileMenu/profile.fxml")).toString());
        URL fxmlAddress = getClass().getResource("ProfileMenu/profile.fxml");
        assert fxmlAddress != null;
        GridPane pane = FXMLLoader.load(fxmlAddress);
        Scene scene = new Scene(pane,500,400);
        Login.getStageInClass().setTitle("profile");
        Login.getStageInClass().setScene(scene);
        Login.getStageInClass().show();
    }

    public void changePassword(ActionEvent actionEvent) {
        if(newPass.getText().isEmpty() || oldPass.getText().isEmpty()){
            new Alert(Alert.AlertType.WARNING,"Please fill all fields").showAndWait();
        }
        if(!MainMenu.currUser.getPassword().equals(oldPass.getText())){
            new Alert(Alert.AlertType.WARNING,"old password is not correct").showAndWait();
        }
        else{
            MainMenu.currUser.setPassword(newPass.getText());
            new Alert(Alert.AlertType.WARNING,"password changed successfully").showAndWait();
            DataBase.saveTheUserList(User.getUsers());
        }
    }

    public void deleteAccount(ActionEvent actionEvent) {
        Object[] options = {"Yes,Delete",
            "No"};
        int n = JOptionPane.showOptionDialog(null,
                "Are you sure to Delete account?",
                "delete account question",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,     //do not use a custom Icon
                options,  //the titles of buttons
                options[1]); //default button title
        if(n==0){
            User.deleteUser(MainMenu.currUser);
            MainMenu.currUser = null;
            Login.getStageInClass().setScene(Login.loginScene);
        }
        DataBase.saveTheUserList(User.getUsers());
    }

    public void back(ActionEvent actionEvent) {
        Login.getStageInClass().setScene(MainMenu.mainMenuScen);
    }
}
