package Pacman.View;
import Pacman.Controllers.MazGeneratorController;
import Pacman.Login;
import javafx.scene.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.*;
import javafx.scene.canvas.*;
import javafx.scene.paint.Color;

import java.awt.*;

public class MazeGenerator {
    public static char[][] table;

    public static Group graphMaze(char[][] game){
        table = game;
        Group root = new Group();

        final Canvas canvas = new Canvas(300,300);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        printGraphic(gc);

        root.getChildren().add(canvas);
        return root;
    }
    private static void printGraphic(GraphicsContext gc) {
        for(int row=0;row<table.length;row++){
            for(int col=0;col<table[0].length;col++){
                Color color;
                switch (table[row][col]){
                    case '1' : color = Color.BLACK;
                    break;
                    case '0' : color = Color.WHITE;
                    break;
                    default:
                        color = Color.BLUE;
                }
                gc.setFill(color);
                gc.fillRect(10*col,10*row,10,10);
            }
        }
    }
}
