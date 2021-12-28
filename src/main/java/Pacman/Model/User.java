package Pacman.Model;

import Pacman.Controllers.MazGeneratorController;
import Pacman.Database.DataBase;
import Pacman.View.Ghost;
import Pacman.View.StartGameMenu;

import java.io.IOException;
import java.io.Serializable;
import java.security.KeyStore;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class User implements Serializable {
    private static ArrayList<User> users = new ArrayList<>();
    public static String DayFormat = "yyyy-MM-dd HH:mm:ss";
    public static DateTimeFormatter myFormat = DateTimeFormatter.ofPattern(DayFormat);
    private final String username;
    public HashMap<Character, Integer[]> ghostInMap = new HashMap<>();
    private String password;
    private int score;
    private int maxScore;
    private LocalDateTime lastChangedMaxScoreTime;
    private boolean isLoggedIn;
    private ArrayList<Maze> mazes = new ArrayList<>();
    private boolean isLastGameSaved = false;
    private char[][] lastGame = new char[MazGeneratorController.length * 2 + 1][MazGeneratorController.length * 2 + 1];
    private int pacmanHeart;
    public User(String username , String password){
        this.username = username;
        this.password = password;
        this.isLoggedIn = true; //When a User created by default be logged in
        users.add(this);
        DataBase.saveTheUserList(users);
    }

    public static User getUserByUsername(String username) {
        for(User user : users)
            if(user.getUsername().equals(username)) return user;

        return null;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public static ArrayList<User> getUsers() {
        return users;
    }

    public static void setUsers(ArrayList<User> users) {
        if(!Objects.isNull(users))User.users = users;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void setIsLoggedIn(boolean isLoggedIn) {
        this.isLoggedIn= isLoggedIn;
    }

    public static void deleteUser(User user){
        users.remove(user);
    }

    public void setLastChangedMaxScoreTime(LocalDateTime lastChangedMaxScoreTime) {
        this.lastChangedMaxScoreTime = lastChangedMaxScoreTime;
    }

    public LocalDateTime getLastChangedMaxScoreTime() {
        return lastChangedMaxScoreTime;
    }

    public void setMaxScore(int maxScore) {
        this.maxScore = maxScore;
    }

    public int getMaxScore() {
        return maxScore;
    }

    public void setMazes(Maze maze) {
        this.mazes.add(maze);
    }

    public ArrayList<Maze> getMazes() {
        return mazes;
    }

    public void setLastGameMap(char[][] lastGame) {
        this.lastGame = lastGame;
    }

    public char[][] getLastGameTable() {
        return lastGame;
    }

    public void setLastGameSaved(boolean saved) {
        isLastGameSaved = saved;
    }

    public boolean isLastGameSaved() {
        return isLastGameSaved;
    }

    public void setPacmanHeart(int pacmanHeart) {
        this.pacmanHeart = pacmanHeart;
    }

    public int getPacmanHeart() {
        return pacmanHeart;
    }

}
