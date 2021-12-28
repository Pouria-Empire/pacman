package Pacman.Controllers;

import Pacman.Model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;

public class ScoreBoardController {

    public static ArrayList<String> sortUsers(){
        ArrayList<String> userList = new ArrayList<>();
        ArrayList<User> users = User.getUsers();
        //compare users
        Comparator<User> orderedUsers = Comparator.comparing(User::getMaxScore,Comparator.reverseOrder()).
                thenComparing(User::getLastChangedMaxScoreTime);
        //sorting users
        users.sort(orderedUsers);
        //for ranking users
        int rank=1;
        //iterate users
        for (int i = 0 ; i < users.size() ; i++) {
            userList.add(((rank + "- " + users.get(i).getUsername() + " : " + users.get(i).getMaxScore()) + "\n"));
            if(i != users.size()-1 && users.get(i).getMaxScore() > users.get(i+1).getMaxScore() ){
                if(i+1 == rank)
                    rank++;
                else
                    rank = i+2;
            }
        }
        return userList;
    }
    @Test
    public void scoreBoardTest(){
        LocalDateTime time1 =  LocalDateTime.parse("2021-12-12 12:12:11", User.myFormat);
        LocalDateTime time2 =  LocalDateTime.parse("2021-12-12 12:12:12", User.myFormat);
        LocalDateTime time3 =  LocalDateTime.parse("2021-12-12 12:12:13", User.myFormat);
        LocalDateTime time4 =  LocalDateTime.parse("2021-12-12 12:12:14", User.myFormat);
        LocalDateTime time5 =  LocalDateTime.parse("2021-12-12 12:12:15", User.myFormat);
        User user5 = new User("pouria5","123");
        user5.setMaxScore(100); user5.setLastChangedMaxScoreTime(time5);
        User user1 = new User("pouria1","123");
        user1.setMaxScore(300); user1.setLastChangedMaxScoreTime(time1);
        User user4 = new User("pouria4","123");
        user4.setMaxScore(200); user4.setLastChangedMaxScoreTime(time4);
        User user2 = new User("pouria2","123");
        user2.setMaxScore(200); user2.setLastChangedMaxScoreTime(time2);
        User user3 = new User("pouria3","123");
        user3.setMaxScore(200); user3.setLastChangedMaxScoreTime(time3);

        ArrayList<String> test = new ScoreBoardController().sortUsers();
        ArrayList<String> expected = new ArrayList<>();
        expected.add("1- pouria1 : 300");
        expected.add("2- pouria2 : 200");
        expected.add("2- pouria3 : 200");
        expected.add("2- pouria4 : 200");
        expected.add("5- pouria5 : 100");
        for(int i=0;i< test.size();i++)
        Assertions.assertEquals(expected.get(i).trim(), test.get(i).trim());
    }
}
