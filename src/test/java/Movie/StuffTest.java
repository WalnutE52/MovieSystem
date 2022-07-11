package Movie;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.ArrayList;
import java.util.Calendar;
import java.sql.*;
import static org.junit.jupiter.api.Assertions.*;
public class StuffTest {
   Stuff stuff;
   Manager m;
   String database;
   Connection c;
   Statement stmt;
   Calendar date1;
   Calendar date2;
   Calendar updating;
   Calendar post;
   List<String> cast;
   List<String> cast2;
   @BeforeEach
   public void setUp(){
       try{
            stuff = new Stuff("test.db");
            m = new Manager("test.db");
            database = "test.db";
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:src/main/resources/test.db");
            stmt = c.createStatement();
            String staff1 = String.format("INSERT INTO Stuff VALUES ('222333444','1234');PRAGMA foreign_keys = ON;");
            stmt.executeUpdate(staff1);
            post = Calendar.getInstance();
            post.set(Calendar.YEAR, 2021);
            post.set(Calendar.MONTH, 5);
            post.set(Calendar.DATE, 20);
            post.set(Calendar.HOUR_OF_DAY, 10);
            post.set(Calendar.MINUTE, 10);
            post.set(Calendar.SECOND, 00);
            date1 = Calendar.getInstance();
            date1.set(Calendar.YEAR, 2021);
            date1.set(Calendar.MONTH, 9);
            date1.set(Calendar.DATE, 22);
            date1.set(Calendar.HOUR_OF_DAY, 10);
            date1.set(Calendar.MINUTE, 10);
            date1.set(Calendar.SECOND, 00);

            date2 = Calendar.getInstance();
            date2.set(Calendar.YEAR, 2021);
            date2.set(Calendar.MONTH, 9);
            date2.set(Calendar.DATE, 23);
            date2.set(Calendar.HOUR_OF_DAY, 20);
            date2.set(Calendar.MINUTE, 10);
            date2.set(Calendar.SECOND, 00);
            
            updating = Calendar.getInstance();
            updating.set(Calendar.YEAR, 9999);
            updating.set(Calendar.MONTH, 9);
            updating.set(Calendar.DATE, 23);
            updating.set(Calendar.HOUR_OF_DAY, 20);
            updating.set(Calendar.MINUTE, 10);
            updating.set(Calendar.SECOND, 00);

            cast = new ArrayList<>();
            cast.add("Angelababy");
            cast2 = new ArrayList<>();
            cast2.add("liqin");
            cast2.add("cang");
            m.addScreeningRoom(1, "Gold", 10, 10, 10, "sky");
            m.addScreeningRoom(2, "Silver", 20, 20, 20, "sea");
        } catch (Exception e) {
            e.printStackTrace();
        }

    

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
           stmt.close();
           c.close();

       } catch(Exception e) {
           System.out.println(e.getMessage());
       }

   }



   @Test
   public void addMovieTest() {
       assertTrue(stuff.addMovie("ji", "General", "good movie", date1, "eason", cast));
       try {
           Connection con = DBUtils.getCon(database);
           Statement stmt = con.createStatement();
           ResultSet rs = stmt.executeQuery( "SELECT * FROM Movie where name = 'ji' AND classification = 'General'"+
           "AND synopsis = 'good movie' AND release_date = '2021-10-22 10:10:00' AND director = 'eason' AND casts = 'Angelababy';");
           assertTrue(rs.next());
           stmt.close();
           con.close();
       } catch (SQLException e) {
           // TODO Auto-generated catch block
           e.printStackTrace();
       }
       assertTrue(stuff.addMovie("ji1", "Parental Guidance", "good movie", date1, "eason", cast));
       assertTrue(stuff.addMovie("ji2", "Mature", "good movie", date1, "eason", cast));
       assertTrue(stuff.addMovie("ji3", "Mature Accompanies", "good movie", date1, "eason", cast));
       assertTrue(stuff.addMovie("ji4", "Restricted", "good movie", date1, "eason", cast2));
       assertFalse(stuff.addMovie(null, null, null, null, null, null));
       assertFalse(stuff.addMovie("ji5", "18jin", "good movie", date1, "eason", cast));
       assertFalse(stuff.addMovie("", "General", "good movie", date1, "eason", cast));
       assertFalse(stuff.addMovie("ji", "General", "good movie", date1, "eason", cast));


   }
   @Test
   public void editMovieTest() {
       stuff.addMovie("jiji","Mature","Commercial War Movie",date2, "Isabella",cast2);
       assertTrue(stuff.editMovie("jiji","Mature","Commercial War",date2, "Isabella",cast2));
       try {
           Connection con = DBUtils.getCon(database);
           Statement stmt = con.createStatement();
           ResultSet rs = stmt.executeQuery("SELECT * FROM Movie where name = 'jiji' AND classification = 'Mature'"+
           "AND synopsis = 'Commercial War' AND release_date = '2021-10-23 20:10:00' AND director = 'Isabella' AND casts = 'liqin, cang';");
           assertTrue(rs.next());
           stmt.close();
           con.close();
       } catch (SQLException e) {
           // TODO Auto-generated catch block
           e.printStackTrace();
       }
       assertFalse(stuff.editMovie("","Mature","Commercial War Movie",date2, "Isabella",cast));
       assertFalse(stuff.editMovie(null,null,null,null, null,null));
       assertFalse(stuff.editMovie("jigechuanqi","Mature","Commercial War Movie",date2, "Isabella",cast));
       assertFalse(stuff.editMovie("jiji","18jin","Commercial War Movie",date2, "Isabella",cast));

   }

   @Test
   public void deleteMovieTest() {
       assertTrue(stuff.addMovie("guo","General","Commercial War Movie",date2, "guoguo",cast2));
       assertTrue(stuff.deleteMovie("guo"));
       try {
           Connection con = DBUtils.getCon(database);
           Statement stmt = con.createStatement();
           ResultSet rs = stmt.executeQuery( "SELECT * FROM Movie where movie_name = 'guo';");
           assertFalse(rs.next());
           stmt.close();
           con.close();
       } catch (SQLException e) {
           // TODO Auto-generated catch block
           e.printStackTrace();
       }
       assertFalse(stuff.deleteMovie("cheng"));
       assertFalse(stuff.deleteMovie(null));
       assertFalse(stuff.deleteMovie(""));
       assertFalse(stuff.deleteMovie("guo"));


   }

   @Test
   public void addSessionTest() {
       stuff.addMovie("ji","Mature","Commercial War Movie",date2, "Isabella",cast2);
       stuff.addMovie("test","Restricted","Commercial Movie",date2, "Isabella",cast2);
       assertTrue(stuff.addSession("ji", 1, updating, "sky"));
       try {
           Connection con = DBUtils.getCon(database);
           Statement stmt = con.createStatement();
           ResultSet rs = stmt.executeQuery( "SELECT * FROM Session where movie_name = 'ji' AND screen_id = 1"+
           " AND screen_cinema = 'sky' AND updating_time = '9999-10-23 " +
                   "20:10:00';");
           assertTrue(rs.next());
           stmt.close();
           con.close();
       } catch (SQLException e) {
           // TODO Auto-generated catch block
           e.printStackTrace();
       }
       assertFalse(stuff.addSession("test", 1, post, "sky"));
       assertFalse(stuff.addSession(null, 1, null, null));
       assertFalse(stuff.addSession("", 1, updating, "sky"));
       assertFalse(stuff.addSession("ji", 1, updating, ""));
       assertFalse(stuff.addSession("ji", 111, updating, "sky"));
       assertFalse(stuff.addSession("ji", 1, updating, "sky"));

   }



   @Test
   public void deleteSessionTest() {
       stuff.addMovie("jiji","Mature","Commercial War Movie",date2, "Isabella",cast2);
       stuff.addSession("jiji", 2, updating, "sea");
       assertTrue(stuff.deleteSession("jiji",2,updating,"sea"));
       try {
           Connection con = DBUtils.getCon(database);
           Statement stmt = con.createStatement();
           ResultSet rs = stmt.executeQuery( "SELECT * FROM Session where movie_name = 'jiji' AND screen_id = 2 AND updating_time = '9999-10-23 20:10:00' AND screen_cinema = 'sea';");
           assertFalse(rs.next());
           stmt.close();
           con.close();
       } catch (SQLException e) {
           // TODO Auto-generated catch block
           e.printStackTrace();
       }
       assertFalse(stuff.deleteSession("jiji",2,updating,"sea"));
       assertFalse(stuff.deleteSession(null,0,null,null));
       assertFalse(stuff.deleteSession("",2,updating,"sea"));
       assertFalse(stuff.deleteSession("cheng",2,updating,"sea"));
       assertFalse(stuff.deleteSession("jiji",5,updating,"sea"));
       assertFalse(stuff.deleteSession("jiji",2,updating,""));
       assertFalse(stuff.deleteSession("jiji",2,updating,"ssssss"));
       assertFalse(stuff.deleteSession("jigechanqi",2,updating,"sea"));

   }
   @Test
   public void addGiftcardTest() {
       
       assertTrue(stuff.addGiftCard("2222333344445555GC"));
       try {
           Connection con = DBUtils.getCon(database);
           Statement stmt = con.createStatement();
           ResultSet rs = stmt.executeQuery( "SELECT * FROM Giftcard where giftcard_id = '2222333344445555GC' AND used = 0;");
           assertTrue(rs.next());
           stmt.close();
           con.close();
       } catch (SQLException e) {
           // TODO Auto-generated catch block
           e.printStackTrace();
       }
       assertFalse(stuff.addGiftCard("2222333344445555GC"));
       assertFalse(stuff.addGiftCard("2222333344445555GC5555555555"));
       assertFalse(stuff.addGiftCard("2222333344445555XY"));
       assertFalse(stuff.addGiftCard("SSSSAAAABBBBDDDDGC"));
       assertFalse(stuff.addGiftCard("22223"));
       assertFalse(stuff.addGiftCard("2222333344445555GC"));
   }
   @Test
   public void checkStuff00() {
        Stuff m = new Stuff("test.db");
       assertTrue(m.checkStuff("222333444", "1234"));
   }
   @Test
   public void checkStuff01() {
    Stuff m = new Stuff("test.db");
       assertFalse(m.checkStuff(null, null));
   }
   @Test
   public void checkStuff02() {
    Stuff m = new Stuff("test.db");
       assertFalse(m.checkStuff("", "1234"));
   }
   @Test
   public void checkStuff03() {
    Stuff m = new Stuff("test.db");
       assertFalse(m.checkStuff("12345", "1234"));
   }
   @Test
   public void checkStuff04() {
        Stuff m = new Stuff("test.db");
       assertFalse(m.checkStuff("123456789", ""));
   }
   @Test
   public void checkStuff05() {
        Stuff m = new Stuff("test.db");
       assertFalse(m.checkStuff("123456789", "012345678901234567890123456789012345678901234567890"));
   }
   @Test
   public void checkStuff06() {
        Stuff m = new Stuff("test.db");
       assertFalse(m.checkStuff("123456789", "1256"));
   }


}
