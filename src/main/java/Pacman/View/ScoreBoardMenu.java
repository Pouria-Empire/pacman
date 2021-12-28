package Pacman.View;

import Pacman.Controllers.ScoreBoardController;
import Pacman.Login;
import Pacman.Model.User;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;

public class ScoreBoardMenu implements common{
    private static ScoreBoardMenu singleToneClass = null;

    public static ScoreBoardMenu getInstance() {
        if(singleToneClass == null) singleToneClass = new ScoreBoardMenu();
        return singleToneClass;
    }

    public void runScoreBoardMenu(){
        //initialize
        BorderPane root = new BorderPane();
        //build Buttons
        Button back = new Button("back!");
        //set handler
        back.setOnMouseClicked(e->{
            Login.getStageInClass().setScene(MainMenu.mainMenuScen);
        });
        //adding users sorted!
        TextArea textArea = new TextArea();
        for(String detail : ScoreBoardController.sortUsers()){
            textArea.appendText(detail + "\n");
        }
        //build scene
        root.setCenter(textArea);
        root.setBottom(back);
        Scene scene = new Scene(root,400,300);
        //set style
        String style= Objects.requireNonNull(this.getClass().getResource("MainMenu/MainMenu.css")).toExternalForm();
        scene.getStylesheets().add(style);
        //show stage
        Login.getStageInClass().setScene(scene);
        Login.getStageInClass().show();
    }
}
