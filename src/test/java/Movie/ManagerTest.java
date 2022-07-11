package Movie;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
public class ManagerTest {
    Connection c;
    Statement stmt;

    @BeforeEach
    public void before(){
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:src/main/resources/test.db");
            stmt = c.createStatement();
            String manager1 = String.format("INSERT INTO Manager VALUES ('222333444','1234');PRAGMA foreign_keys = ON;");
            stmt.executeUpdate(manager1);
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
            String delete_movie = "DELETE FROM Screening;VACUUM;";
            stmt.executeUpdate(delete_movie);
            String delete_session = "DELETE FROM Session;VACUUM;";
            stmt.executeUpdate(delete_session);
            String delete_staff = "DELETE FROM Stuff;VACUUM";
            stmt.executeUpdate(delete_staff);
            String delete_giftcard = "DELETE FROM Giftcard;VACUUM";
            stmt.executeUpdate(delete_giftcard);
            String delete_manager = "DELETE FROM Manager;VACUUM;";
            stmt.executeUpdate(delete_manager);
            stmt.close();
            c.close();

        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
        
    }

    @Test
    public void testAddScreeningRoomNull() {
        Manager m = new Manager("test.db");
        assertFalse(m.addScreeningRoom(1, "dsfsaf", 1,1,1,null));
        assertFalse(m.addScreeningRoom(1, null, 1,1,1,null));
    }

    @Test
    public void testAddScreeningRoomWithDiffirentScreen() {
        Manager m = new Manager("test.db");
        assertFalse(m.addScreeningRoom(1, "dsfsaf", 1,1,1,"dfsds"));
        assertFalse(m.addScreeningRoom(1, "Gold", -1, -1, -1, "afijos"));
    }

    @Test
    public void testAddScreeningRoom(){
        Manager m = new Manager("test.db");
        assertTrue(m.addScreeningRoom(3, "Gold", 1, 1, 1, "Chicken")); 
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:src/main/resources/test.db");
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT * FROM Screening WHERE id = 3 AND cinema = 'Chicken';" );
            assertTrue(rs.next()) ;
            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
    }

    @Test
    public void testAddScreeningRoom2() {
        Manager m = new Manager("test.db");
        assertTrue(m.addScreeningRoom(3, "Gold", 1, 1, 1, "Chicken")); 
        assertFalse(m.addScreeningRoom(3, "Gold", 1, 1, 1, "Chicken")); 
    }

    @Test
    public void testSetScreeningRoom() {
        Manager m = new Manager("test.db");
        assertTrue(m.addScreeningRoom(3, "Gold", 1, 1, 1, "Chicken"));
        assertTrue(m.setScreeningRoom(3, "Gold", 1, 2, 2, "Chicken"));
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:src/main/resources/test.db");
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT * FROM Screening WHERE id = 3 AND cinema = 'Chicken' AND middle_seat = 2;" );
            assertTrue(rs.next()) ;
            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
    }

    @Test
    public void testRemoveScreeningRoom() {
        Manager m = new Manager("test.db");
        assertTrue(m.addScreeningRoom(3, "Gold", 1, 1, 1, "Chicken"));
        assertTrue(m.removeScreeningRoom(3, "Chicken"));
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:src/main/resources/test.db");
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT * FROM Screening WHERE id = 3 AND cinema = 'Chicken';" );
            assertFalse(rs.next()) ;
            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }

    }

    @Test
    public void testRemoveScreeningRoom2() {
        Manager m = new Manager("test.db");
        assertFalse(m.removeScreeningRoom(1, "Chicken"));
        assertFalse(m.removeScreeningRoom(1, null));
    }

    @Test
    public void testSetScreeningRoom2() {
        Manager m = new Manager("test.db");
        assertFalse(m.setScreeningRoom(3, "Gold", -1, 2, 2, "Chicken"));
        assertFalse(m.setScreeningRoom(3, "Gold2", 1, 2, 2, "Chicken"));
        assertFalse(m.setScreeningRoom(3, "Gold2", 1, 2, 2, null));
        assertFalse(m.setScreeningRoom(2, "Gold", 1, 2, 2, "Chicken"));
    }

    @Test
    public void testaddStuff1() {
        Manager m =  new Manager("test.db");
        assertTrue(m.addStuff("123456789", "1234"));
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:src/main/resources/test.db");
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT * FROM Stuff WHERE account = '123456789';" );
            assertTrue(rs.next()) ;
            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
        assertFalse(m.addStuff(null, null));
        assertFalse(m.addStuff("123456789122112", "1234"));
        assertFalse(m.addStuff("123456789", "1234"));
        assertTrue(m.removeStuff("123456789"));
    }

    @Test
    public void testaddStuff2() {
        Manager m =  new Manager("test.db");
        String a  = "a";
        assertFalse(m.addStuff("123456789", a.repeat(60)));
    }

    @Test
    public void testRemoveStuffInvail() {
        Manager m = new Manager("test.db");
        assertFalse(m.removeStuff(null));
        assertFalse(m.removeStuff(""));
        assertFalse(m.removeStuff("23245364786912"));
        assertFalse(m.removeStuff("1234"));
        assertFalse(m.removeStuff("123456789"));
    }

    @Test
    public void testRemoveStuffPositive() {
        Manager m = new Manager("test.db");
        assertTrue(m.addStuff("123456789", "1234"));
        assertTrue(m.removeStuff("123456789"));
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:src/main/resources/test.db");
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT * FROM Stuff WHERE account = '123456789';" );
            assertFalse(rs.next()) ;
            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
    }
    @Test
    public void checkManager00() {
        Manager m = new Manager("test.db");
        assertTrue(m.checkManager("222333444", "1234"));
    }
    @Test
    public void checkManager01() {
        Manager m = new Manager("test.db");
        assertFalse(m.checkManager(null, null));
    }
    @Test
    public void checkManager02() {
        Manager m = new Manager("test.db");
        assertFalse(m.checkManager("", "1234"));
    }
    @Test
    public void checkManager03() {
        Manager m = new Manager("test.db");
        assertFalse(m.checkManager("12345", "1234"));
    }
    @Test
    public void checkManager04() {
        Manager m = new Manager("test.db");
        assertFalse(m.checkManager("123456789", ""));
    }
    @Test
    public void checkManager05() {
        Manager m = new Manager("test.db");
        assertFalse(m.checkManager("123456789", "012345678901234567890123456789012345678901234567890"));
    }
    @Test
    public void checkManager06() {
        Manager m = new Manager("test.db");
        assertFalse(m.checkManager("123456789", "1256"));
    }

    


}