package Movie;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.List;
import java.util.ArrayList;
import java.util.Calendar;
import java.sql.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


import java.sql.Connection;
import java.sql.DriverManager;

public class CSVTest {
   Stuff stuff;
   Manager m;
   CinemaSystem cs;
   Customer customer;
   Connection c;
   Statement stmt;
   Calendar date1;
   Calendar date2;
   Calendar notRelease;
   List<String> cast;
   List<String> cast2;
   @BeforeEach
   public void setUp(){
       stuff = new Stuff("test.db");
       m = new Manager("test.db");
       cs = new CinemaSystem("test.db",false,0,0,0,0,0,0,0,0);
       customer = new Customer("test.db");

       date1 = Calendar.getInstance();
       date1.set(Calendar.YEAR, 2021);
       date1.set(Calendar.MONTH, 9);
       date1.set(Calendar.DATE, 22);
       date1.set(Calendar.HOUR_OF_DAY, 10);
       date1.set(Calendar.MINUTE, 10);
       date1.set(Calendar.SECOND, 00);
       date2 = Calendar.getInstance();
       date2.set(Calendar.YEAR, 9999);
       date2.set(Calendar.MONTH, 9);
       date2.set(Calendar.DATE, 30);
       date2.set(Calendar.HOUR_OF_DAY, 23);
       date2.set(Calendar.MINUTE, 10);
       date2.set(Calendar.SECOND, 00);
       notRelease = Calendar.getInstance();
       notRelease.set(Calendar.YEAR, 3000);
       notRelease.set(Calendar.MONTH, 9);
       notRelease.set(Calendar.DATE, 22);
       notRelease.set(Calendar.HOUR_OF_DAY, 10);
       notRelease.set(Calendar.MINUTE, 10);
       notRelease.set(Calendar.SECOND, 00);
       cast = new ArrayList<>();
       cast.add("Angelababy");
       cast2 = new ArrayList<>();
       cast2.add("liqin");
       cast2.add("cang");
       m.addScreeningRoom(1, "Gold", 10, 10, 10, "sky");
       stuff.addMovie("ji", "General", "good movie", date1, "eason", cast);
       stuff.addMovie("jilate", "General", "good movie", notRelease, "eason", cast);
       stuff.addSession("ji", 1, date2, "sky");
       customer.register("123456789", "1111");
       cs.transaction(date2, "123456789", "bad", 0);
   }
   @AfterEach
   public void after(){
       try{
           Class.forName("org.sqlite.JDBC");
           c = DriverManager.getConnection("jdbc:sqlite:src/main/resources/test.db");
           stmt = c.createStatement();
           String delete_users = "DELETE FROM Users;";
           stmt.executeUpdate(delete_users);
           String delete_screen = "DELETE FROM Screening;VACUUM;";
           stmt.executeUpdate(delete_screen);
           String delete_session = "DELETE FROM Session;VACUUM;";
           stmt.executeUpdate(delete_session);
           String delete_staff = "DELETE FROM Stuff;VACUUM";
           stmt.executeUpdate(delete_staff);
           String delete_giftcard = "DELETE FROM Giftcard;VACUUM;";
           stmt.executeUpdate(delete_giftcard);
           String delete_movie = "DELETE FROM Movie;VACUUM;";
           stmt.executeUpdate(delete_movie);
           String delete_transaction = "DELETE FROM Transactions;VACUUM;";
           stmt.executeUpdate(delete_transaction);
           stmt.close();
           c.close();
           DBUtils.deleteFile("src/main/resources/","Cancel");
           DBUtils.deleteFile("src/main/resources/","Movie");
           DBUtils.deleteFile("src/main/resources/","NotReleased");

       } catch(Exception e) {
           System.out.println(e.getMessage());
       }

   }
   public static ArrayList<String[]> readCsv(String csvFile) {
       BufferedReader br = null;
       String line = "";
       String cvsSplitBy = ",";
       ArrayList<String[]> csvList = new ArrayList<String[]>();
       try {

           br = new BufferedReader(new FileReader("src/main/resources/" + csvFile));
           while ((line = br.readLine()) != null) {

               // use comma as separator
               String[] aline = line.split(cvsSplitBy);
               csvList.add(aline);
           }

       } catch (FileNotFoundException e) {
           e.printStackTrace();
       } catch (IOException e) {
           e.printStackTrace();
       } finally {
           if (br != null) {
               try {
                   br.close();
               } catch (IOException e) {
                   e.printStackTrace();
               }
           }
       }

       return csvList;
   }

   @Test
   public void getSessionTest(){
       stuff.getSession();
       ArrayList<String[]> content = readCsv(DBUtils.returnFileNameWithPrefix("src/main/resources/", "Movie"));

       assertEquals(content.get(0)[0], "updatingTime");
       assertEquals(content.get(0)[1], "ScreenCinema");
       assertEquals(content.get(0)[2], "ScreenId");
       assertEquals(content.get(0)[3], "curFrontSeat");
       assertEquals(content.get(0)[4], "curMiddleSeat");
       assertEquals(content.get(0)[5], "curBackSeat");
       assertEquals(content.get(0)[6], "screenName");
       assertEquals(content.get(0)[7], "frontSeat");
       assertEquals(content.get(0)[8], "middleSeat");
       assertEquals(content.get(0)[9], "backSeat");
       assertEquals(content.get(0)[10], "cinema");
       assertEquals(content.get(0)[11], "nameFromMovie");
       assertEquals(content.get(0)[12], "classification");
       assertEquals(content.get(0)[13], "synopsis");
       assertEquals(content.get(0)[14], "releaseDate");
       assertEquals(content.get(0)[15], "director");
       assertEquals(content.get(0)[16], "casts");

       assertEquals(content.get(1)[0], "9999-10-30 23:10:00");
       assertEquals(content.get(1)[1], "sky");
       assertEquals(content.get(1)[2], "1");
       assertEquals(content.get(1)[3], "10");
       assertEquals(content.get(1)[4], "10");
       assertEquals(content.get(1)[5], "10");
       assertEquals(content.get(1)[6], "Gold");
       assertEquals(content.get(1)[7], "10");
       assertEquals(content.get(1)[8], "10");
       assertEquals(content.get(1)[9], "10");
       assertEquals(content.get(1)[10], "sky");
       assertEquals(content.get(1)[11], "ji");
       assertEquals(content.get(1)[12], "General");
       assertEquals(content.get(1)[13], "good movie");
       assertEquals(content.get(1)[14], "2021-10-22 10:10:00");
       assertEquals(content.get(1)[15], "eason");
       assertEquals(content.get(1)[16], "Angelababy");

   }

    @Test
    public void notReleaseMovieTest(){
        stuff.notReleasedMovie();
        ArrayList<String[]> content = readCsv(DBUtils.returnFileNameWithPrefix("src/main/resources/", "NotReleased"));

        assertEquals(content.get(0)[0], "name");
        assertEquals(content.get(0)[1], "classification");
        assertEquals(content.get(0)[2], "synopsis");
        assertEquals(content.get(0)[3], "releaseDate");
        assertEquals(content.get(0)[4], "director");
        assertEquals(content.get(0)[5], "casts");

        assertEquals(content.get(1)[0], "jilate");
        assertEquals(content.get(1)[1], "General");
        assertEquals(content.get(1)[2], "good movie");
        assertEquals(content.get(1)[3], "3000-10-22 10:10:00");
        assertEquals(content.get(1)[4], "eason");
        assertEquals(content.get(1)[5], "Angelababy");

    }

    @Test
    public void getCancelTransactionTest(){
       m.getCancelTransaction();
       ArrayList<String[]> content = readCsv(DBUtils.returnFileNameWithPrefix("src/main/resources/", "Cancel"));

       assertEquals(content.get(0)[0], "TransactionID");
       assertEquals(content.get(0)[1], "TransactionTime");
       assertEquals(content.get(0)[2], "UserID");
       assertEquals(content.get(0)[3], "Reason");
       assertEquals(content.get(0)[4], "Status");



       assertEquals(content.get(1)[1], "9999-10-30 23:10:00");
       assertEquals(content.get(1)[2], "123456789");
       assertEquals(content.get(1)[3], "bad");
       assertEquals(content.get(1)[4], "Cancel");

    }


}

