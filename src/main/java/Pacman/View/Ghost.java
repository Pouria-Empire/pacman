package Pacman.View;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class Ghost extends Thread implements Runnable{
    public static ArrayList<Ghost> ghosts = new ArrayList<>();
    static StartGameMenu startGameMenu;
    private char name;
    int currX,currY;
    int oldX,oldY;
    int firstX,firstY;
    Image image;
    Image oldImage;
    char oldChar = 'E';//as checking empty
    private boolean isDead = false;

    public Ghost(int x,int y, Image image,StartGameMenu startGame){
        startGameMenu = startGame;
        this.currX = x;
        this.currY = y;
        this.firstX = x;
        this.firstY = y;
        this.image = image;
        this.oldImage = image;
        ghosts.add(this);
    }

    public void run() {
        if(!isDead){
            if(oldY==firstY && firstY==currY){
                clearGhost(firstY, firstX);
            }
            final int[] changeX = {1, -1, 0, 0};
            final int[] changeY = {0, 0, 1, -1};
            Random random = new Random();
            int randomPosition = random.nextInt(4);
            int x1 = currX + changeX[randomPosition];
            int y1 = currY + changeY[randomPosition];


            if (startGameMenu.isPossible(x1, y1)) {
                if (startGameMenu.getTable()[x1][y1] != '1') {
                    update(x1, y1);
                }
            }
        }
        else{
            currX = firstX; currY = firstY;
            clearGhost();
            startGameMenu.graphic.drawImage(this.image, startGameMenu.size * this.firstY,
                    startGameMenu.size * this.firstX, startGameMenu.size+2, startGameMenu.size+1);
        }
    }

    private void clearGhost() {
        clearGhost(currY, currX);
    }

    private void clearGhost(int y, int x) {
        startGameMenu.graphic.drawImage(startGameMenu.imageBackGround,
                startGameMenu.size * y, startGameMenu.size * x, startGameMenu.size, startGameMenu.size);
        startGameMenu.graphic.setFill(Color.GREEN);
        startGameMenu.graphic.fillRoundRect(startGameMenu.size * y, startGameMenu.size * x,
                startGameMenu.size-13, startGameMenu.size-13,startGameMenu.size,startGameMenu.size);
    }

    private void update(int x1, int y1) {
        oldChar = startGameMenu.getTable()[currX][currY];
        oldX = currX;
        oldY = currY;
        if(startGameMenu.getTable()[x1][y1]=='S'){
            //put ghost to nearest place
            if(startGameMenu.isPossible(x1-1, y1)){
                currX = x1-1;
                currY = y1;
            }
            else if(startGameMenu.isPossible(x1+1, y1)){
                currX = x1+1;
                currY = y1;
            }
            else if(startGameMenu.isPossible(x1, y1+1)){
                currX = x1;
                currY = y1+1;
            }
            else if(startGameMenu.isPossible(x1, y1-1)){
                currX = x1;
                currY = y1-1;
            }

        }
        else {
            currX = x1;
            currY = y1;
        }
        repaint(oldChar);
        repaint(startGameMenu.getTable()[currX][currY]);
        startGameMenu.graphic.drawImage(image, startGameMenu.size * currY,
                startGameMenu.size * currX, startGameMenu.size+2, startGameMenu.size+1);

        startGameMenu.getTable()[currX][currY] = 'G';
    }

    private void repaint(char charInPlace) {
        switch (charInPlace) {
            case '0':
                startGameMenu.graphic.setFill(Color.GREEN);
                startGameMenu.graphic.fillRoundRect(startGameMenu.size * oldY, startGameMenu.size * oldX,
                        startGameMenu.size-13, startGameMenu.size-13,startGameMenu.size,startGameMenu.size);
                break;
            case 'S':
                startGameMenu.graphic.drawImage(startGameMenu.imageGift, startGameMenu.size * currY,
                        startGameMenu.size * currX, startGameMenu.size-3, startGameMenu.size-3);
                break;
            case 'V':
                startGameMenu.graphic.drawImage(startGameMenu.imageBackGround,
                        startGameMenu.size * oldY, startGameMenu.size * oldX, startGameMenu.size, startGameMenu.size);
                break;
            case 'G':
                Ghost currGhost = getGhostByXAndY(oldX,oldY);
                if(!Objects.isNull(currGhost)){
                    if(currGhost.getImage().equals(this.getImage())){
                        startGameMenu.graphic.drawImage(startGameMenu.imageBackGround,
                                startGameMenu.size * oldY, startGameMenu.size * oldX, startGameMenu.size, startGameMenu.size);
                        startGameMenu.graphic.setFill(Color.GREEN);
                        startGameMenu.graphic.fillRoundRect(startGameMenu.size * oldY, startGameMenu.size * oldX,
                                startGameMenu.size-13, startGameMenu.size-13,startGameMenu.size,startGameMenu.size);
                        startGameMenu.getTable()[oldX][oldY] = '0';
                    }
                }
                break;
            case 'P':
                StartGameMenu.ghostHitPacman = this;
                StartGameMenu.getInstance().pacmanHitByGhost();
                break;
            default:
                break;
        }
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public Image getOldImage() {
        return oldImage;
    }

    public int getCurrX() {
        return currX;
    }

    public int getCurrY() {
        return currY;
    }

    public static Ghost getGhostByXAndY(int x,int y){
        for(Ghost ghost : ghosts){
            if(ghost.getOldX()==x && ghost.getOldY()==y)
                return ghost;
        }
        return null;
    }
    public static Ghost getGhostByCurrXAndY(int x,int y){
        for(Ghost ghost : ghosts){
            if(ghost.getCurrX()==x && ghost.getCurrY()==y)
                return ghost;
        }
        return null;
    }

    public Image getImage() {
        return image;
    }

    public int getOldX() {
        return oldX;
    }

    public void setDead(boolean dead) {
        isDead = dead;
    }

    public int getOldY() {
        return oldY;
    }

    public boolean isDead() {
        return isDead;
    }

    public void setName(char name) {
        this.name = name;
    }

    public char getGhostName() {
        return this.name;
    }
}
