package Pacman.Controllers;

import java.util.*;

public class MazGeneratorController {

    public static int[][] visited;
    public static char[][][] gameTable;
    public static int width = 25;
    public static int length = 25;
    public static int currRepeat;
    public static char WALL = '1';
    public static char SPACE = '0';

    public static void generate() {
        int repeat;
        repeat = 1;
        gameTable = new char[repeat][width][length];
        calculateMaze();

    }

    private static void calculateMaze() {
        visited = new int[width][length];
        currRepeat = 0;
        createTable(width, length);
        createMaze(1, 1);
        randomDeleteWall();
        printMaze();
        //clear corners
        clearCorners();
    }

    private static void clearCorners() {
        gameTable[currRepeat][1][1] = '0';
        gameTable[currRepeat][2][2] = '0';
        gameTable[currRepeat][1][2] = '0';
        gameTable[currRepeat][2][1] = '0';
        gameTable[currRepeat][width-2][width-2] = '0';
        gameTable[currRepeat][width-3][width-3] = '0';
        gameTable[currRepeat][width-2][width-3] = '0';
        gameTable[currRepeat][width-3][width-2] = '0';
        gameTable[currRepeat][1][width-2] = '0';
        gameTable[currRepeat][1][width-3] = '0';
        gameTable[currRepeat][2][width-2] = '0';
        gameTable[currRepeat][2][width-3] = '0';
        gameTable[currRepeat][width-2][1] = '0';
        gameTable[currRepeat][width-3][1] = '0';
        gameTable[currRepeat][width-2][2] = '0';
        gameTable[currRepeat][width-3][2] = '0';
        //for pacman
        gameTable[currRepeat][(width-1)/2][(width-1)/2] = '0';
        gameTable[currRepeat][(width-1)/2+1][(width-1)/2] = '0';
        gameTable[currRepeat][(width-1)/2][(width-1)/2+1] = '0';
        gameTable[currRepeat][(width-1)/2+1][(width-1)/2+1] = '0';

    }

    private static void randomDeleteWall() {
        int counter = 0;
        Random random = new Random();
        while (counter<10){
            int ranWidth = random.nextInt(width+2);
            int randHeight = random.nextInt(width+2);
            ranWidth = getRandom(ranWidth);
            randHeight = getRandom(randHeight);
            if(randHeight == 30 || randHeight == 0 || ranWidth == 0 || ranWidth==30 ||
                    randHeight+1 == 30 || randHeight-1 == 0 || ranWidth-1 == 0 || ranWidth+1 ==30)
                continue;
            if(gameTable[0][ranWidth][randHeight] == '1'){
                if(gameTable[0][ranWidth+1][randHeight] == '1')
                    gameTable[0][ranWidth+1][randHeight] = '0';
                if(gameTable[0][ranWidth][randHeight+1] == '1')
                    gameTable[0][ranWidth][randHeight+1] = '0';
                if(gameTable[0][ranWidth-1][randHeight] == '1')
                    gameTable[0][ranWidth-1][randHeight] = '0';
                if(gameTable[0][ranWidth][randHeight-1] == '1')
                    gameTable[0][ranWidth][randHeight-1] = '0';

                    gameTable[0][ranWidth][randHeight] = '0';

                counter++;
            }

        }
    }

    private static int getRandom(int rand) {
        if(rand >=20)
            rand -=4;
        else if(rand ==0)
            rand +=4;
        return rand;
    }

    private static void printMaze() {
        System.out.println(Arrays.deepToString(gameTable[currRepeat]).
                replace("], ", "\n").
                replace("[[", "").
                replace("]]", "").
                replace("[", "").
                replace(", ", ""));
    }

    public static void createMaze(int currX, int currY) {
        final int[] changeX = {1, -1, 0,0};
        final int[] changeY = {0, 0, 1, -1};
        Random random = new Random();
        int randomPosition = random.nextInt(4);
        int counter = 0;

        while (counter < 4) {
            int x1 = currX + changeX[randomPosition];
            int y1 = currY + changeY[randomPosition];
            int newCellPositionX = x1 + changeX[randomPosition];
            int newCellPositionY = y1 + changeY[randomPosition];

            if (isItPossible(x1, y1, newCellPositionX, newCellPositionY)) {
                gameTable[currRepeat][y1][x1] = SPACE;
                visited[newCellPositionY][newCellPositionX] = 1;

                createMaze(newCellPositionX, newCellPositionY);
            } else {
                randomPosition = (randomPosition + 1) % 4;
                counter++;
            }
        }
    }

    public static void createTable(int width, int length) {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < length; j++) {
                if (i == 0 && j == 1)
                    gameTable[currRepeat][i][j] = '1';
                else if (i == width - 1 && j == length - 2)
                    gameTable[currRepeat][i][j] = '1';
                else if (j == 0 ||
                        j == width - 1 ||
                        i == 0 ||
                        i == length - 1)
                    gameTable[currRepeat][i][j] = '1';
                else {
                    if (i % 2 == 1 && j % 2 == 1)
                        gameTable[currRepeat][i][j] = '0';
                    else
                        gameTable[currRepeat][i][j] = '1';
                }
            }
        }
    }

    public static boolean isItPossible(int oldX, int oldY, int newX, int newY) {
        if (oldX <= 0 ||
                oldX >= length - 1 ||
                oldY <= 0 ||
                oldY >= width - 1) return false;

        if (visited[newY][newX] == 1)
            return false;

        return (gameTable[currRepeat][oldY][oldX] == WALL);
    }

    public static boolean checkIfTheMazeRepeated() {
        for (int i = 0; i < currRepeat; i++) {
            if (Arrays.deepEquals(gameTable[i], gameTable[currRepeat]))
                return false;
        }
        return true;
    }
}
