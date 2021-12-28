package Pacman.Model;

import java.io.Serializable;
import java.util.ArrayList;

public class Maze implements Serializable {
    public static ArrayList<Maze> allMazes = new ArrayList<>();
    private char[][] maze = new char[25][25];

    public Maze(char[][] maze){
        this.maze = maze;
        allMazes.add(this);
    }

    public void setMaze(char[][] maze) {
        this.maze = maze;
    }

    public char[][] getMaze() {
        return maze;
    }
}
