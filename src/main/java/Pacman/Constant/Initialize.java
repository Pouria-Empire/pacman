package Pacman.Constant;

import Pacman.Database.DataBase;
import Pacman.Model.User;
import java.io.File;
import java.time.LocalDateTime;

public class Initialize {

    public static void initUserList(){
        File tmpDir = new File("savedList.list");
        if(tmpDir.exists()) User.setUsers(DataBase.loadTheList());
    }
}
