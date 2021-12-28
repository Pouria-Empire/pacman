package Pacman.Controllers;

import Pacman.View.Ghost;
import Pacman.View.StartGameMenu;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.io.File;
import java.util.Date;
import java.util.Objects;
import java.util.TimerTask;

public class StartGameController extends Thread {
    public static char[][] gameTable = new char[25][25];
    private static int packManHeart;
    private static StartGameController singleton = null;
    private boolean stop = false;
    private boolean muteSounds = false;
    public Clip clip;

    public static StartGameController getInstance() {
        if (Objects.isNull(singleton)) return singleton = new StartGameController();
        else return singleton;
    }
    public static void refreshInstance() {
        singleton = new StartGameController();
    }

    public static void setPackManHeart(int heart) {
        packManHeart = heart;
    }

    public static int getPackManHeart() {
        return packManHeart;
    }

    public void run() {
        playBackGameSound();
        while (!StartGameMenu.isGameEnded) {
            if (!stop) {

                if (muteSounds) clip.stop();
                else clip.start();

                runGhosts();
            } else {
                keepAlive();
            }
        }
    }

    private void keepAlive() {
        System.out.println("thread stopped!");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void runGhosts() {
        Ghost.ghosts.get(0).run();
        Ghost.ghosts.get(1).run();
        Ghost.ghosts.get(2).run();
        Ghost.ghosts.get(3).run();
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            System.out.println("error Interrupted exception");
        }
    }

    private void playBackGameSound() {
        try {
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(Objects.requireNonNull(getClass().
                    getResource("music/base2.wav")));
            clip = AudioSystem.getClip();
            clip.open(inputStream);
            clip.start();
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }

    public void setStop(boolean stop) {
        this.stop = stop;
    }

    public boolean isStop() {
        return stop;
    }

    public boolean isMuteSounds() {
        return muteSounds;
    }

    public void setMuteSounds(boolean muteSounds) {
        this.muteSounds = muteSounds;
    }

    public static void setSingleton(StartGameController singleton) {
        StartGameController.singleton = singleton;
    }
}
