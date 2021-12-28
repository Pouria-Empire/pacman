package Pacman.View;

import Pacman.Login;
import Pacman.Model.User;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javax.swing.*;
import java.io.IOException;
import java.util.Objects;

public class MainMenu {
    public static User currUser;
    public static Scene mainMenuScen;
    public MainMenu(User user){
        currUser = user;
    }
    public void startMainMenu(Stage window){
        //initialize
        BorderPane root = new BorderPane();
        VBox vBox = new VBox();
        //build Buttons
        Button startGame = new Button("Start Game!");
        Button profile = new Button("Profile");
        Button scoreBoard = new Button("Score Board");
        Button setting = new Button("Setting");
        Button logout = new Button("Logout");

        handleButtons(window, startGame, profile, scoreBoard, setting, logout);

        //build vBox
        vBox.getChildren().addAll(startGame,profile,scoreBoard,setting);
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(5);
        vBox.getChildren().addAll(logout);
        //build scene
        root.setCenter(vBox);
        Scene scene = new Scene(root,400,300);
        //set style
        String style= Objects.requireNonNull(this.getClass().getResource("MainMenu/MainMenu.css")).toExternalForm();
        scene.getStylesheets().add(style);
        //store Scene
        mainMenuScen = scene;
        //show stage
        window.setScene(scene);
        window.show();
    }

    private void handleButtons(Stage window, Button startGame, Button profile, Button scoreBoard, Button setting, Button logout) {
        //set handler
        startGame.setOnMouseClicked(e->{
            startSavedGame();

            StartGameMenu.getInstance().runStartGameMenu();
        });
        profile.setOnMouseClicked(e->{
            if(Objects.isNull(currUser)){
                new Alert(Alert.AlertType.ERROR,"You dont have user please Register First").showAndWait();
            }
            else{
                try {
                    ProfileMenu.getInstance().runProfileMenu();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });
        scoreBoard.setOnMouseClicked(e->{
            ScoreBoardMenu.getInstance().runScoreBoardMenu();
        });
        setting.setOnMouseClicked(e->{
            SettingMenu.getInstance().runSettingMenu(window);
        });

        logout.setOnMouseClicked(e->{
           logoutUser();
        });
    }

    private void startSavedGame() {
        if(!Objects.isNull(currUser)) {
            if (!Objects.isNull(currUser.getLastGameTable()) && currUser.isLastGameSaved()) {
                int reply = JOptionPane.showConfirmDialog(null, "Do you want to continue last game?",
                        "continue last try", JOptionPane.YES_NO_OPTION);
                if (reply == JOptionPane.YES_OPTION) {
                    JOptionPane.showMessageDialog(null, "You can continue your game, Good lock!");
                    StartGameMenu.startSavedGame = true;
                } else {
                    JOptionPane.showMessageDialog(null, "Ok, we will start new game!");
                    StartGameMenu.startSavedGame = false;
                }
            }
        }
    }

    private void logoutUser() {
        Object[] options = {"Yes",
                "No"};
        int n = JOptionPane.showOptionDialog(null,
                "Would you like to logout?",
                "logout question",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,     //do not use a custom Icon
                options,  //the titles of buttons
                options[1]); //default button title
        if(n==0){
            if(!Objects.isNull(currUser)) {
                currUser.setIsLoggedIn(false);
                currUser = null;
            }
            Login.getStageInClass().setScene(Login.loginScene);
        }
    }
}
