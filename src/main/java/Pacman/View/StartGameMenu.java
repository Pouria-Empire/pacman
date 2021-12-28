package Pacman.View;
/* this symbols are used in map:
 * 'V' as visited
 * '1' as wall
 * '0' as none visited
 * 'P' as pacman
 * 'G' as ghost
 * 'S' as Star
 */

import Pacman.Controllers.MazGeneratorController;
import Pacman.Controllers.StartGameController;
import Pacman.Database.DataBase;
import Pacman.Login;
import Pacman.Model.Maze;
import Pacman.Model.User;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import org.testng.ITestRunnerFactory;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;
import java.util.Timer;

public class StartGameMenu implements Serializable {
    private static int pacManHeart = StartGameController.getPackManHeart();
    private static StartGameMenu singleToneClass = null;
    private static int countPoints = 0;
    private static int countBombs = 4;
    public static Ghost ghostHitPacman;
    public static boolean startSavedGame = false;
    public static boolean isGameEnded = false;
    final int size = 23;
    final int position = 25;
    final Canvas canvas = new Canvas(700, 600);
    private char[][] table = new char[25][25];
    final String pathRed = Objects.requireNonNull(this.getClass().getResource("StartGame/red.gif")).toExternalForm();
    final String pathYellow = Objects.requireNonNull(this.getClass().getResource("StartGame/yellow.gif")).toExternalForm();
    final String pathBlue = Objects.requireNonNull(this.getClass().getResource("StartGame/blue.gif")).toExternalForm();
    final String pathPink = Objects.requireNonNull(this.getClass().getResource("StartGame/pink.gif")).toExternalForm();
    final String pathPacman = Objects.requireNonNull(this.getClass().getResource("StartGame/pacman.gif")).toExternalForm();
    final String pathGift = Objects.requireNonNull(this.getClass().getResource("StartGame/gift.gif")).toExternalForm();
    final String newColor = Objects.requireNonNull(this.getClass().getResource("StartGame/ghostNewColor.gif")).toExternalForm();
    final String backGround = Objects.requireNonNull(this.getClass().getResource("MainMenu/background.jpg")).toExternalForm();
    final Image imageRed = new Image(pathRed);
    final Image imageYellow = new Image(pathYellow);
    final Image imageBlue = new Image(pathBlue);
    final Image imagePink = new Image(pathPink);
    final Image imagePacman = new Image(pathPacman);
    final Image imageGift = new Image(pathGift);
    final Image imageNewColor = new Image(newColor);
    final Image imageBackGround = new Image(backGround);
    GraphicsContext graphic = canvas.getGraphicsContext2D();
    Group root = new Group();
    int pacPositionX = (position - 3) / 2;
    int pacPositionY = (position - 3) / 2;
    int muteSoundCounter = 1;
    int score = 0;
    public int countEatGhosts = 1;
    public boolean canEatGhost = false;
    boolean gamePause = false;
    boolean isMuteSounds = false;
    private boolean isSetTable = false;
    Timer timer = new Timer("Timer");
    Scene scene;
    BorderPane borderPane = new BorderPane();
    Text scoreBox = new Text("0");
    Text heartBox = new Text("" + pacManHeart);
    Text messageBox = new Text("messages here ...");
    Clip clipEatGhost, clipPacmanDie, clipEatBomb, clipEnd;

    public static StartGameMenu getInstance() {
        if (singleToneClass == null) {
            singleToneClass = new StartGameMenu();
        }
        return singleToneClass;
    }


    public void runStartGameMenu() {
        if (!isSetTable)
            table = Maze.allMazes.get(0).getMaze();
        if (startSavedGame)
            runGameFromSavedGame();
        else
            initGame();

    }

    private void runGameFromSavedGame() {
        User currUser = MainMenu.currUser;
        if (!Objects.isNull(currUser)) {
            //restore ghosts
            restoreGhosts();
            //pacman Heart
            pacManHeart = currUser.getPacmanHeart();
            heartBox.setText(""+pacManHeart);
            score = currUser.getScore();
            scoreBox.setText(""+score);

            char[][] gameSavedTable = currUser.getLastGameTable();
            table = gameSavedTable.clone();
            for (int row = 0; row < table.length; row++) {
                for (int col = 0; col < table[0].length; col++) {
                    if (table[row][col] == 'V') {
                        graphic.drawImage(imageBackGround,
                                size * col, size * row, size, size);
                    } else if (table[row][col] == 'G') {
                        Ghost ghost = Ghost.getGhostByCurrXAndY(row, col);
                        if (!Objects.isNull(ghost))
                            graphic.drawImage(ghost.image, size * ghost.firstY,
                                    size * ghost.firstX, size, size);
                        else
                            System.out.println("no ghost found!");
                    } else if (table[row][col] == 'P') {
                        pacPositionY = col;
                        pacPositionX = row;
                        graphic.drawImage(imageBackGround, size * pacPositionY, size * pacPositionX, size, size);
                    } else if (table[row][col] == 'S') {
                        graphic.drawImage(imageGift, size * col, size * row, size - 3, size - 3);
                    } else if (table[row][col] == '0') {
                        graphic.drawImage(imageBackGround,
                                size * col, size * row, size, size);
                        graphic.setFill(Color.GREEN);
                        graphic.fillRoundRect(size * col, size * row,
                                size - 13, size - 13, size, size);
                    } else if (table[row][col] == '1') {
                        graphic.setFill(Color.DARKBLUE);
                        graphic.fillRect(size * col, size * row, size - 5, size - 4);
                    }
                }
            }
        }
        currUser.setLastGameMap(null);
        currUser.setLastGameSaved(false);
        handleGame();
    }

    private void restoreGhosts() {
        HashMap<Character, Integer[]> ghostInMap = MainMenu.currUser.ghostInMap;
        new Ghost(ghostInMap.get('R')[0], ghostInMap.get('R')[1], imageRed, this);
        new Ghost(ghostInMap.get('Y')[0], ghostInMap.get('Y')[1], imageYellow, this);
        new Ghost(ghostInMap.get('P')[0], ghostInMap.get('P')[1], imagePink, this);
        new Ghost(ghostInMap.get('B')[0], ghostInMap.get('B')[1], imageBlue, this);
    }

    private void initGame() {
        generateMap();
        //random put gift
        randomPutGift();

        handleGame();
    }

    private void handleGame() {
        //if user forget to set pacman heart By default set it to 3
        if(pacManHeart==0) {
            pacManHeart = 3;
        }
        heartBox.setText(""+pacManHeart);

        initializeScene();
        //handle pacman
        handlePacmanMovement();

        canvas.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                System.out.println("entered");
            }
        });

        //after 2 seconds ghosts start moving
        TimerTask taskStartMovingGhosts = new TimerTask() {
            public void run() {
                System.out.println("Task performed on: " + new Date() + "n" +
                        "Thread's name: " + Thread.currentThread().getName());
                StartGameController.getInstance().start();
            }
        };
        timer.schedule(taskStartMovingGhosts, 2000L);

        Login.getStageInClass().setScene(scene);
        Login.getStageInClass().show();
    }

    private void randomPutGift() {

        for (int i = 0; i < 4; i++) {
            //random generate number
            Random random = new Random();
            int randX = random.nextInt(position - 1);
            int randY = random.nextInt(position - 1);
            //check numbers are good!
            while (!isPossible(randX, randY) && table[randX][randY] != '0') {
                randX = random.nextInt(position - 1);
                randY = random.nextInt(position - 1);
            }
            //not put gift near another gift!
            if ((isPossible(randX, randY + 1) && table[randX][randY + 1] == 'S') ||
                    isPossible(randX + 1, randY) && table[randX + 1][randY] == 'S' ||
                    isPossible(randX - 1, randY) && table[randX - 1][randY] == 'S' ||
                    isPossible(randX, randY - 1) && table[randX][randY - 1] == 'S') {
                i--;
                continue;
            }
            //not put gift on pacman!
            if (table[randX][randY] == 'P') {
                i--;
                continue;
            }
            //draw gifts
            graphic.drawImage(imageGift, size * randY, size * randX, size - 3, size - 3);
            table[randX][randY] = 'S';
        }
    }

    private void generateMap() {
        for (int row = 0; row < table.length; row++) {
            for (int col = 0; col < table[0].length; col++) {
                Color color;
                switch (table[row][col]) {
                    case '1':
                        color = Color.DARKBLUE;
                        break;
                    case '0':
                        color = Color.GREEN;
                        break;
                    case 'V':
                        color = Color.GREEN;
                        break;
                    default:
                        color = Color.GRAY;
                }
                if (row == (position - 3) / 2 && col == (position - 3) / 2) {
                    graphic.drawImage(imagePacman, size * col, size * row, size + 1, size + 1);
                    table[(position - 3) / 2][(position - 3) / 2] = 'P';
                } else if (row == 1 && col == 1) {
                    graphic.drawImage(imageRed, size * col, size * row, size + 3, size + 3);
                    table[1][1] = 'G';
                    new Ghost(1, 1, imageRed, this);
                } else if (row == 1 && col == position - 2) {
                    graphic.drawImage(imageYellow, size * col, size * row, size, size);
                    table[1][position - 2] = 'G';
                    new Ghost(1, position - 2, imageYellow, this);
                } else if (row == position - 2 && col == 1) {
                    graphic.drawImage(imageBlue, size * col, size * row, size, size);
                    table[position - 2][1] = 'G';
                    new Ghost(position - 2, 1, imageBlue, this);
                } else if (row == position - 2 && col == position - 2) {
                    graphic.drawImage(imagePink, size * col, size * row, size, size);
                    table[position - 2][position - 2] = 'G';
                    new Ghost(position - 2, position - 2, imagePink, this);
                } else {
                    graphic.setFill(color);
                    if (table[row][col] == '1')
                        graphic.fillRect(size * col, size * row, size - 5, size - 4);
                    else {
                        countPoints++;
                        graphic.fillRoundRect(size * col, size * row, size - 13, size - 13, size, size);
                    }
                }
            }
        }
    }


    private void initializeScene() {
        initSounds();

        VBox vBox = new VBox();
        Button startNewGame = new Button("New Game!");
        Button pause = new Button("Pause");
        Button mute = new Button("Mute Sounds!");
        Button exitBtn = new Button("Exit!");
        vBox.getChildren().addAll(new Label("here is your score : "), scoreBox);
        vBox.getChildren().addAll(new Label("here is pacman heart : "), heartBox);
        vBox.getChildren().addAll(new Label("Messages : "), messageBox);
        vBox.getChildren().addAll(startNewGame, pause, mute, exitBtn);
        String styleButton = "    -fx-text-fill: white;\n" +
                "    -fx-font-family: \"Arial Narrow\";\n" +
                "    -fx-font-weight: bold;\n" +
                "    -fx-background-color: linear-gradient(#61a2b1, #2A5058);\n" +
                "    -fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.6) , 5, 0.0 , 0 , 1 );\n";
        pause.setStyle(styleButton);
        startNewGame.setStyle(styleButton);
        mute.setStyle(styleButton);
        exitBtn.setStyle(styleButton);
        exitBtn.setFocusTraversable(false);
        mute.setFocusTraversable(false);
        pause.setFocusTraversable(false);
        startNewGame.setFocusTraversable(false);
        pause.setOnMouseClicked(e -> {
            pauseGame();
        });
        startNewGame.setOnMouseClicked(e -> {
            newGame();
        });
        mute.setOnMouseClicked(e -> {
            muteSounds();
        });
        exitBtn.setOnMouseClicked(e -> {
            exitGame();
        });
        vBox.setStyle("-fx-font-size: 32px;\n" +
                "    -fx-font-family: \"Arial Black\";\n" +
                "    -fx-fill: #818181;\n" +
                "    -fx-effect: innershadow( three-pass-box , rgba(0,0,0,0.7) , 6, 0.0 , 0 , 2 );");

        root.getChildren().add(canvas);
        canvas.setFocusTraversable(true);
        borderPane.setCenter(root);
        borderPane.setRight(vBox);
        scene = new Scene(borderPane);
        String style = Objects.requireNonNull(this.getClass().getResource("MainMenu/MainMenu.css")).toExternalForm();
        scene.getStylesheets().add(style);
    }

    public void exitGame() {
        StartGameController.getInstance().clip.stop();
        StartGameController.getInstance().setMuteSounds(true);
        StartGameController.getInstance().stop();
        StartGameController.setSingleton(null);
        pauseGame();

        updateUserProfile();
        User currUser = MainMenu.currUser;
        if (!Objects.isNull(currUser)) {
            currUser.setLastGameMap(table);
            currUser.setLastGameSaved(true);
            currUser.setScore(score);
            currUser.setPacmanHeart(pacManHeart);
            for (Ghost ghost : Ghost.ghosts) {
                if (ghost.getImage().equals(imageRed))
                    currUser.ghostInMap.put('R', new Integer[]{ghost.currX, ghost.currY});
                else if (ghost.getImage().equals(imageBlue))
                    currUser.ghostInMap.put('B', new Integer[]{ghost.currX, ghost.currY});
                else if (ghost.getImage().equals(imageYellow))
                    currUser.ghostInMap.put('Y', new Integer[]{ghost.currX, ghost.currY});
                else if (ghost.getImage().equals(imagePink))
                    currUser.ghostInMap.put('P', new Integer[]{ghost.currX, ghost.currY});
                ghost.oldY= ghost.firstY;
                ghost.oldX = ghost.firstX;
            }
        }
        deleteGame();
        Login.getStageInClass().setScene(MainMenu.mainMenuScen);
    }

    private void deleteGame() {
        singleToneClass = new StartGameMenu();
        singleToneClass.isSetTable = true;
        isGameEnded = false;
        if(!Objects.isNull(MainMenu.currUser) || isSetTable)
            singleToneClass.table = StartGameController.gameTable;
        else
            singleToneClass.table = Maze.allMazes.get(0).getMaze();
        isSetTable = false;
        StartGameController.refreshInstance();
        DataBase.saveTheUserList(User.getUsers());
    }


    private void muteSounds() {
        if (muteSoundCounter == 2) {
            StartGameController.getInstance().setMuteSounds(false);
            muteSoundCounter = 1;
            isMuteSounds = true;
        } else {
            StartGameController.getInstance().setMuteSounds(true);
            muteSoundCounter++;
            isMuteSounds = false;
        }
    }

    private void initSounds() {
        try {
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(Objects.requireNonNull(getClass().
                    getResource("music/pacman_eatghost.wav")));
            clipEatGhost = AudioSystem.getClip();
            clipEatGhost.open(inputStream);
            AudioInputStream inputStream2 = AudioSystem.getAudioInputStream(Objects.requireNonNull(getClass().
                    getResource("music/pacmanDie.wav")));
            clipPacmanDie = AudioSystem.getClip();
            clipPacmanDie.open(inputStream2);
            AudioInputStream inputStream3 = AudioSystem.getAudioInputStream(Objects.requireNonNull(getClass().
                    getResource("music/pacman_eatBomb.wav")));
            clipEatBomb = AudioSystem.getClip();
            clipEatBomb.open(inputStream3);
            AudioInputStream inputStream4 = AudioSystem.getAudioInputStream(Objects.requireNonNull(getClass().
                    getResource("music/end.wav")));
            clipEnd = AudioSystem.getClip();
            clipEnd.open(inputStream4);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void newGame() {
        //pause the game
        StartGameController.getInstance().setStop(true);
        pauseGame();
        //update user profile
        updateUserProfile();
        //show alert
        new Alert(Alert.AlertType.INFORMATION, "new game will start for you!").showAndWait();
        //reset pacman
        resetGamePacman();

        //reset ghosts
        resetGameGhost();

        resetPointAndBombs();
        //restart the game
        pauseGame();
        StartGameController.getInstance().setStop(false);
    }

    private void updateUserProfile() {
        User currUser = MainMenu.currUser;
        if (!Objects.isNull(currUser)) {
            if (score > currUser.getMaxScore()) {
                currUser.setMaxScore(score);
                currUser.setLastChangedMaxScoreTime(LocalDateTime.now());
                DataBase.saveTheUserList(User.getUsers());
            }
        }
    }

    private void resetGameGhost() {
        for (Ghost ghost : Ghost.ghosts) {
            //clear this ghost place
            clearGhost(ghost);
            ghost.currX = ghost.firstX;
            ghost.currY = ghost.firstY;
            ghost.image = ghost.oldImage;
            ghost.oldX = ghost.firstX;
            ghost.oldY = ghost.firstY;
            graphic.drawImage(ghost.image, size * ghost.firstY, size * ghost.firstX, size, size);
        }
    }

    private void resetGamePacman() {
        pacManHeart = StartGameController.getPackManHeart();
        countPoints = 0;
        countBombs = 4;
        setVisited();
        pacPositionX = (position - 3) / 2;
        pacPositionY = (position - 3) / 2;
        movePacman();
        countEatGhosts = 1;
        score = 0;
        canEatGhost = false;
        gamePause = false;
        gamePause = true;
    }

    private void clearGhost(Ghost ghost) {
        graphic.drawImage(imageBackGround,
                size * ghost.currY, size * ghost.currX, size, size);
        graphic.setFill(Color.GREEN);
        graphic.fillRoundRect(size * ghost.currY, size * ghost.currX,
                size - 13, size - 13, size, size);
    }

    private void pauseGame() {
        if (!messageBox.getText().equals("Game Paused!")) {
            messageBox.setText("Game Paused!");
            gamePause = true;
            StartGameController.getInstance().setStop(true);
        } else {
            messageBox.setText("Game restarted!");
            gamePause = false;
            StartGameController.getInstance().setStop(false);
        }
    }

    private void handlePacmanMovement() {
        canvas.setOnKeyPressed(event -> {
            if (!gamePause && !isGameEnded) {
                if (event.getCode() == KeyCode.UP) {
                    handleUpMovement();
                    System.out.println("entered up key");
                }
                if (event.getCode() == KeyCode.DOWN) {
                    handleDownMovement();
                    System.out.println("entered down key");
                }
                if (event.getCode() == KeyCode.RIGHT) {
                    handleRightMovement();
                    System.out.println("entered right key");
                }
                if (event.getCode() == KeyCode.LEFT) {
                    handleLeftMovement();
                    System.out.println("entered left key");
                }
                scoreBox.setText("" + score);
                root.getChildren().remove(canvas);
                root.getChildren().add(canvas);
                Login.getStageInClass().setScene(scene);
                Login.getStageInClass().show();
            }
            if(isGameEnded)
                messageBox.setText("Exit! you lose");
        });
    }

    private void handleLeftMovement() {
        resetGameIfStopped();
        if (table[pacPositionX][pacPositionY - 1] == 'G') {

            pacmanHitByGhost();
        } else if (table[pacPositionX][pacPositionY - 1] == 'S') {
            setVisited();
            pacPositionY -= 1;
            movePacman();
            gainStar();
        } else if (table[pacPositionX][pacPositionY - 1] == '0' || table[pacPositionX][pacPositionY - 1] == 'V') {
            if (table[pacPositionX][pacPositionY - 1] == '0') {
                countPoints--;
                score += 5;
                checkCanWine();
                if(isGameEnded) messageBox.setText("please exit the game");
            }
            setVisited();
            pacPositionY -= 1;
            movePacman();
        }
    }

    private void handleRightMovement() {
        resetGameIfStopped();
        if (table[pacPositionX][pacPositionY + 1] == 'G') {

            pacmanHitByGhost();
        } else if (table[pacPositionX][pacPositionY + 1] == 'S') {
            setVisited();
            pacPositionY += 1;
            movePacman();
            gainStar();
        } else if (table[pacPositionX][pacPositionY + 1] == '0' || table[pacPositionX][pacPositionY + 1] == 'V') {
            if (table[pacPositionX][pacPositionY + 1] == '0') {
                countPoints--;
                score += 5;
                checkCanWine();
                if(isGameEnded) messageBox.setText("please exit the game");
            }
            setVisited();
            pacPositionY += 1;
            movePacman();
        }
    }

    private void handleDownMovement() {
        resetGameIfStopped();
        if (table[pacPositionX + 1][pacPositionY] == 'G') {
            pacmanHitByGhost();
        } else if (table[pacPositionX + 1][pacPositionY] == 'S') {
            setVisited();
            pacPositionX += 1;
            movePacman();
            gainStar();
        } else if (table[pacPositionX + 1][pacPositionY] == '0' || table[pacPositionX + 1][pacPositionY] == 'V') {
            if (table[pacPositionX + 1][pacPositionY] == '0') {
                countPoints--;
                score += 5;
                checkCanWine();
                if(isGameEnded) messageBox.setText("please exit the game");
            }
            setVisited();
            pacPositionX += 1;
            movePacman();
        }
    }

    private void handleUpMovement() {
        resetGameIfStopped();
        if (isPossible(pacPositionX - 1, pacPositionY)) {
            if (table[pacPositionX - 1][pacPositionY] == 'G') {
                pacmanHitByGhost();
            } else if (table[pacPositionX - 1][pacPositionY] == 'S') {
                setVisited();
                pacPositionX -= 1;
                movePacman();
                gainStar();
            } else if (table[pacPositionX - 1][pacPositionY] == '0' || table[pacPositionX - 1][pacPositionY] == 'V') {
                if (table[pacPositionX - 1][pacPositionY] == '0') {
                    countPoints--;
                    score += 5;
                    checkCanWine();
                    if(isGameEnded) messageBox.setText("please exit the game");
                }
                setVisited();
                pacPositionX -= 1;
                movePacman();
            }
        }
    }

    private void resetGameIfStopped() {
        if (StartGameController.getInstance().isStop()) {
            StartGameController.getInstance().setStop(false);
            messageBox.setText("game start!");
        }
    }

    private void resetGhost(Ghost ghost) {
        StartGameController.getInstance().setStop(true);
        //clear this ghost place
        clearGhost(ghost);
        ghost.setDead(true);
        ghost.currY = ghost.firstY;
        ghost.currX = ghost.firstX;
        //reset ghost after 5 second
        TimerTask taskResetPlaceGhost = new TimerTask() {
            public void run() {
                System.out.println("ghost place reset");
                ghost.setDead(false);
                graphic.drawImage(ghost.image, size * ghost.firstY,
                        size * ghost.firstX, size, size);
            }
        };
        timer.schedule(taskResetPlaceGhost, 5000L);

        StartGameController.getInstance().setStop(false);
    }

    private void gainStar() {
        canEatGhost = true;
        long delay = 10000L;
        TimerTask taskStarEat = new TimerTask() {
            public void run() {
                System.out.println("Task performed on: " + new Date() + "n" +
                        "Thread's name: " + Thread.currentThread().getName());
                StartGameMenu.getInstance().canEatGhost = false;
                StartGameMenu.getInstance().countEatGhosts = 1;
                for (Ghost ghost : Ghost.ghosts)
                    ghost.setImage(ghost.getOldImage());
            }
        };
        timer.schedule(taskStarEat, delay);
        if (!isMuteSounds) clipEatBomb.loop(1);
        for (Ghost ghost : Ghost.ghosts)
            ghost.setImage(imageNewColor);
    }

    void pacmanHitByGhost() {
        Ghost ghost = getGhost();
        if (ghost == null) return;
        if (!ghost.isDead()) {
            if (canEatGhost) {
                //we can`t attack to ghost home !
                eatGhost(ghost);
            } else {
                killPacman();
                if (!checkIfLose()) {
                    messageBox.setText("You lose 1 heart.\n for try again please \nenter" +
                            " one of arrow keys");
                }
                else {
                    clipEnd.loop(1);
                    messageBox.setText("You lose!");
                    JOptionPane.showMessageDialog(null, "you lose the game, try again later!");
                    isGameEnded = true;
                    return;
                }
            }
            heartBox.setText("" + pacManHeart);
        }
    }

    private Ghost getGhost() {
        Ghost ghost = Ghost.getGhostByCurrXAndY(pacPositionX, pacPositionY);
        if (Objects.isNull(ghost))
            ghost = ghostHitPacman;
        if (Objects.isNull(ghost)) {
            System.out.println(
                    "no ghost found !"
            );
            return null;
        }
        return ghost;
    }

    private void eatGhost(Ghost ghost) {
        if (!isMuteSounds) clipEatGhost.loop(1);
        score += countEatGhosts * 200;
        countEatGhosts++;
        setVisited();
        movePacman();
        resetGhost(ghost);
    }

    private void killPacman() {
        if (!isMuteSounds) clipPacmanDie.loop(1);
        pacManHeart--;
        StartGameController.getInstance().setStop(true);
        //move pac man to safe place
        setVisited();
        pacPositionX = (position - 3) / 2;
        pacPositionY = (position - 3) / 2;
        movePacman();
    }


    private void checkCanWine() {
        if (countPoints <= 30 && countBombs <= 0) {
            new Alert(Alert.AlertType.INFORMATION, "you finish the map, we fill it again!").showAndWait();
            pauseGame();
            isMuteSounds = true;
            resetPointAndBombs();
            isMuteSounds = false;
            pauseGame();
            updateUserProfile();
        }
    }

    private void resetPointAndBombs() {
        for (int row = 0; row < table.length; row++) {
            for (int col = 0; col < table[0].length; col++) {
                if (table[row][col] == 'V' || table[row][col] == 'S') {
                    graphic.drawImage(imageBackGround,
                            size * col, size * row, size, size);
                    graphic.setFill(Color.GREEN);
                    graphic.fillRoundRect(size * col, size * row,
                            size - 13, size - 13, size, size);
                    table[row][col] = '0';
                }
            }
        }
        countPoints = 0;
        countBombs = 4;
        randomPutGift();
    }

    private void movePacman() {
        graphic.drawImage(imagePacman, size * pacPositionY, size * pacPositionX, size + 1, size + 1);
        table[pacPositionX][pacPositionY] = 'P';
    }

    private void setVisited() {
        countPoints--;
        graphic.drawImage(imageBackGround, size * pacPositionY, size * pacPositionX, size, size);
        table[pacPositionX][pacPositionY] = 'V';
    }

    private boolean checkIfLose() {
        return pacManHeart <= 0;
    }

    public boolean isPossible(int x, int y) {
        return x > 0 && y > 0 && y < position && x < position;
    }

    public void setTable(char[][] table) {
        isSetTable = true;
        StartGameController.gameTable = table.clone();
        this.table = table;
    }

    public char[][] getTable() {
        return table;
    }

    public boolean isSetTable() {
        return isSetTable;
    }
}
