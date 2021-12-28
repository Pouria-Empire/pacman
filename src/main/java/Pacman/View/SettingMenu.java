package Pacman.View;

import Pacman.Controllers.MazGeneratorController;
import Pacman.Controllers.StartGameController;
import Pacman.Database.DataBase;
import Pacman.Login;
import Pacman.Model.Maze;
import Pacman.Model.User;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import javax.swing.*;
import java.util.HashMap;
import java.util.Objects;

public class SettingMenu {
    private static SettingMenu singleToneClass = null;
    private int lastNewRow;

    public static SettingMenu getInstance() {
        if (singleToneClass == null) singleToneClass = new SettingMenu();
        return singleToneClass;
    }

    public void runSettingMenu(Stage window) {
        JOptionPane.showMessageDialog(null, "Notice: for selecting new maze please click on it " +
                "and please do not forget to set pacman`s heart!");
        JOptionPane.showMessageDialog(null, "after all click save!");
        // Create the Label for the Heart
        Label pacmanHeart = new Label("Pacman Heart:");
        // Create a ChoiceBox for Hearts
        ChoiceBox<String> hearts = new ChoiceBox<>();
        // Add the items to the ChoiceBox
        hearts.getItems().addAll("2", "3", "4", "5");
        // Create the Selection Message Label
        Label result = new Label("Your selection is:");
        // Create the Selection Value Label
        Label selectedValueLbl = new Label();
        // Bind the value property to the text property of the Label
        selectedValueLbl.textProperty().bind(hearts.valueProperty());
        // Display controls in a GridPane
        GridPane root = new GridPane();
        ScrollPane scrollPane = new ScrollPane();
        root.setVgap(3);
        root.setHgap(3);
        //get groups
        root.addRow(0, pacmanHeart, hearts);
        root.addRow(1, result, selectedValueLbl);

        loadDefaultMazes(root);

        addNewMapButton(root);

        showUsersMazes(root);

        addBackAndSaveButton(hearts, root);

        initPane(window, root, scrollPane);
    }

    private void addNewMapButton(GridPane root) {
        Button newMap = new Button("New Map");
        root.addRow(3, newMap);
        newMap.setOnMouseClicked(e -> {
            //generate default mazes
            newMaze(root);

            System.out.println("newMapButton clicked");
        });
    }

    private void loadDefaultMazes(GridPane root) {
        //add default maze
        Group group1 = MazeGenerator.graphMaze(Maze.allMazes.get(0).getMaze());
        Group group2 = MazeGenerator.graphMaze(Maze.allMazes.get(1).getMaze());
        group1.setOnMouseClicked(e -> {
            StartGameMenu.getInstance().setTable(Maze.allMazes.get(0).getMaze());
            System.out.println("maze1 clicked");
            // present result maze selected
            showSelectedMaze(root);

        });
        group2.setOnMouseClicked(e -> {
            StartGameMenu.getInstance().setTable(Maze.allMazes.get(1).getMaze());
            System.out.println("maze2 clicked");
            // present result maze selected
            showSelectedMaze(root);
        });
        root.addRow(2, group1, group2);
    }

    private void addBackAndSaveButton(ChoiceBox<String> hearts, GridPane root) {
        //add buttons
        Button save = new Button("Save");
        Button back = new Button("Back");
        root.addRow(root.getRowCount() + 1, back, save);
        back.setOnMouseClicked(e -> {
            Login.getStageInClass().setScene(MainMenu.mainMenuScen);
            DataBase.saveTheUserList(User.getUsers());
            System.out.println("back clicked");
        });
        save.setOnMouseClicked(e -> {
            if (Objects.isNull(hearts.getValue()) || hearts.getValue().isEmpty() ||
                    hearts.getValue().isBlank()) {
               JOptionPane.showMessageDialog(null,"please select pacman`s heart!","pacman heart!",1);
            } else {
                StartGameController.setPackManHeart(Integer.parseInt(hearts.getValue()));
                Login.getStageInClass().setScene(MainMenu.mainMenuScen);
            }
            DataBase.saveTheUserList(User.getUsers());
            System.out.println("save clicked");
        });
    }

    private void initPane(Stage window, GridPane root, ScrollPane scrollPane) {
        // Set the Size of the GridPane
        scrollPane.setMinSize(600, 600);
        scrollPane.setContent(root);
        Scene scene = new Scene(scrollPane);
        String style = Objects.requireNonNull(this.getClass().getResource("MainMenu/MainMenu.css")).toExternalForm();
        scene.getStylesheets().add(style);
        window.setScene(scene);
        window.setTitle("setting");
        window.show();
    }

    private void newMaze(GridPane root) {
        if(!Objects.isNull(MainMenu.currUser)) {
            User currUser = MainMenu.currUser;
            MazGeneratorController.generate();
            Group newGroup = MazeGenerator.graphMaze(MazGeneratorController.gameTable[0]);
            root.addRow(root.getRowCount() + 1, new Label("Your new maze is :"), newGroup);
            newGroup.setOnMouseClicked(e2 -> {
                System.out.println("new maze clicked");
                StartGameMenu.getInstance().setTable(MazGeneratorController.gameTable[0]);
                int reply = JOptionPane.showConfirmDialog(null, "Do you want to Like this map?",
                        "Like new map", JOptionPane.YES_NO_OPTION);
                if (reply == JOptionPane.YES_OPTION) {
                    DataBase.saveTheUserList(User.getUsers());
                    JOptionPane.showMessageDialog(null, "your new map added to your favorites,Good Look!");
                    currUser.setMazes(new Maze(MazGeneratorController.gameTable[0]));
                }
                // present result maze selected
                showSelectedMaze(root);
            });
        }
        else
            JOptionPane.showMessageDialog(null,"you should Login to generate maze!");
    }

    private void showSelectedMaze(GridPane root) {
        root.addRow(root.getRowCount() + 1, new Label("Your selected maze is :"),
                MazeGenerator.graphMaze(StartGameMenu.getInstance().getTable()));
    }

    private void showUsersMazes(GridPane root) {
        Label message = new Label("Here is your liked maps : ");
        root.addRow(4, message);
        if (!Objects.isNull(MainMenu.currUser)) {
            User currUser = MainMenu.currUser;
            for (int i = 0; i < currUser.getMazes().size(); i++) {
                Group group = MazeGenerator.graphMaze(currUser.getMazes().get(i).getMaze());
                root.addRow(i + 4, group);
                int finalI = i;
                group.setOnMouseClicked(e -> {
                    System.out.println("maze" + finalI + " clicked");
                    StartGameMenu.getInstance().setTable(currUser.getMazes().get(finalI).getMaze());
                    // present result maze selected
                    showSelectedMaze(root);
                });
            }
        }
    }
}