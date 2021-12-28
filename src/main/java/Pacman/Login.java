package Pacman;

import Pacman.Constant.Initialize;
import Pacman.Controllers.LoginController;
import Pacman.Controllers.MazGeneratorController;
import Pacman.Database.DataBase;
import Pacman.View.MazeGenerator;
import Pacman.View.StartGameMenu;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class Login extends Application {
    private static Stage stageInClass;
    public static Scene loginScene;

    @Override
    public void start(Stage stage) throws Exception {
        stageInClass = stage;
        URL fxmlAddress = getClass().getResource("View/Login/login.fxml");
        assert fxmlAddress != null;
        Parent pane = FXMLLoader.load(fxmlAddress);
        Scene scene = new Scene(pane,600,300);
        loginScene = scene;
        stage.setTitle("Welcome to Pacman");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        try {
            DataBase.addSomeMaze(null,"maz1.txt");
            DataBase.addSomeMaze(null,"maz2.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Initialize.initUserList();
        launch(args);
    }

    public static void setStageInClass(Stage stageInClass) {
        Login.stageInClass = stageInClass;
    }

    public static Stage getStageInClass() {
        return stageInClass;
    }
}
