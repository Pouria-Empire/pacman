package Pacman.Database;

import Pacman.Controllers.MazGeneratorController;
import Pacman.Model.Maze;
import Pacman.Model.User;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Objects;

public class DataBase {
    public static final String savedArrayListName = "savedList.list";
    public static final String savedDeckName = "Decks.list";

    public static void saveTheUserList(ArrayList<User> users) {
        try {
            FileOutputStream fileOut = new FileOutputStream(savedArrayListName, false);
            ObjectOutputStream oos = new ObjectOutputStream(fileOut);
            oos.writeObject(users);
            oos.close();
            fileOut.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<User> loadTheList() {
        try {
            FileInputStream fin = new FileInputStream(savedArrayListName);
            ObjectInputStream ois = new ObjectInputStream(fin);
            //.InvalidClassException
            ArrayList<User> myClassObj = (ArrayList<User>) ois.readObject();
            ois.close();
            System.out.println("Loaded Data from file");
            return myClassObj;
        } catch (InvalidClassException e) {
            JOptionPane.showMessageDialog(null,"database had been changed! please delete \"savedList.list\"" +
                    "and try again");
            System.exit(0);
        }
        catch (IOException | ClassNotFoundException e){
            JOptionPane.showMessageDialog(null,"file not found");
            System.exit(0);
        }
        return null;
    }

    public static void addSomeMaze(User user, String name) throws IOException {
        char[][] loaded = new char[25][25];
        FileReader fr = new FileReader(name);
        BufferedReader br = new BufferedReader(fr);
        int counter = 0;
        String str;
        while((str = br.readLine())!=null){
            loaded[counter] = str.toCharArray();
            counter++;
        }
        if(!Objects.isNull(user))
            user.setMazes(new Maze(loaded));
        new Maze(loaded);
    }

}
