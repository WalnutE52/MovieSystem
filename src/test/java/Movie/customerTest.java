package Movie;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Calendar;
import java.sql.*;
import static org.junit.jupiter.api.Assertions.*;
public class customerTest {
    Customer customer;
    Calendar cal;
    ByteArrayOutputStream bytes = null;
    PrintStream console = null;
    Connection c;
    Statement stmt;
    @BeforeEach
    public void setUp() throws Exception {
        bytes = new ByteArrayOutputStream();
        console = System.out;
        System.setOut(new PrintStream(bytes));
        Class.forName("org.sqlite.JDBC");
        c = DriverManager.getConnection("jdbc:sqlite:src/main/resources/test.db");
        stmt = c.createStatement();
        customer = new Customer("test.db");

        String screen1 = "INSERT INTO Screening (id,screen,front_seat," +
                "middle_seat,back_seat,cinema) " +
                "VALUES (1, 'Gold', 10, 10, 10,'disney');PRAGMA foreign_keys" +
                " = ON;";
        stmt.executeUpdate(screen1);
        String screen2 = "INSERT INTO Screening (id,screen,front_seat," +
                "middle_seat,back_seat,cinema) " +
                "VALUES (2, 'Gold', 20, 1, 20,'disney');PRAGMA foreign_keys " +
                "= ON;";
        stmt.executeUpdate(screen2);
        String screen3 = "INSERT INTO Screening (id,screen,front_seat," +
                "middle_seat,back_seat,cinema) " +
                "VALUES (3, 'Silver', 30, 30, 30,'SBS');PRAGMA foreign_keys " +
                "= " +
                "ON;";
        stmt.executeUpdate(screen3);
        String screen4 = "INSERT INTO Screening (id,screen,front_seat," +
                "middle_seat,back_seat,cinema) " +
                "VALUES (4, 'Bronze', 40, 40, 40,'SBS');PRAGMA foreign_keys " +
                "= " +
                "ON;";
        stmt.executeUpdate(screen4);

        String movie1 = "INSERT INTO Movie (name,classification," +
                "synopsis,release_date,director,casts) " +
                "VALUES ('Holiday','General','Family Comedy','2021-10-23 " +
                "20:00:00','Alice' , 'Suzana, Peter');PRAGMA foreign_keys = ON;";
        stmt.executeUpdate(movie1);
        String movie2 = "INSERT INTO Movie (name,classification," +
                "synopsis,release_date,director,casts) " +
                "VALUES ('Breaking News','Mature','Commercial War Movie'," +
                "'2021-10-23 20:10:00','Isabella' , 'Logan, Eva');PRAGMA " +
                "foreign_keys = ON;";
        stmt.executeUpdate(movie2);
        String movie3 = "INSERT INTO Movie (name,classification," +
                "synopsis,release_date,director,casts) " +
                "VALUES ('The Penthouse: War in Life - Season 4','Mature " +
                "Accompanies','Dramatically " +
                "Unbelievable'," +
                "'2021-10-23 22:30:00','Kim Soon-ok' , 'Cheon seo-jin, Oh" +
                " Yoon-hee');PRAGMA " +
                "foreign_keys = ON;";
        stmt.executeUpdate(movie3);

        cal = Calendar.getInstance();
        cal.add(Calendar.SECOND,30);
        Date date = cal.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strDate = dateFormat.format(date);
        String session1_holiday = String.format("PRAGMA foreign_keys = ON;INSERT " +
                "INTO Session (updating_time,movie_name,screen_id," +
                "screen_cinema,cur_front_seat,cur_middle_seat,cur_back_seat)" +
                " " +
                "VALUES ('%s','Holiday',2,'disney',20, 1, " +
                "20);" +
                "PRAGMA" +
                " foreign_keys = ON;",strDate);
        stmt.executeUpdate(session1_holiday);
        String session2_holiday = String.format("PRAGMA foreign_keys = ON;INSERT " +
                "INTO Session (updating_time,movie_name,screen_id,screen_cinema,cur_front_seat,cur_middle_seat,cur_back_seat) " +
                "VALUES ('%s','Holiday',3, 'SBS', 30, 30, " +
                "30);" +
                "PRAGMA foreign_keys = ON;",strDate);
        stmt.executeUpdate(session2_holiday);
        String session3_holiday = String.format("PRAGMA foreign_keys = ON;INSERT " +
                "INTO Session (updating_time,movie_name,screen_id,screen_cinema,cur_front_seat,cur_middle_seat,cur_back_seat) " +
                "VALUES ('%s','Holiday',4, 'SBS', 40, 40, " +
                "40);" +
                "PRAGMA foreign_keys = ON;",strDate);
        stmt.executeUpdate(session3_holiday);
//        Calendar currentTime = DBUtils.getCurrentTime();
        String session1_penthouse = String.format("PRAGMA foreign_keys = ON;INSERT " +
                "INTO Session (updating_time,movie_name,screen_id,screen_cinema,cur_front_seat,cur_middle_seat,cur_back_seat) " +
                "VALUES ('%s','The Penthouse: War in Life -" +
                " " +
                "Season 4',1,'disney', 10, 10, 10);PRAGMA foreign_keys = ON;",strDate);
        stmt.executeUpdate(session1_penthouse);

        String user1 = "INSERT INTO Users (account,password," +
                "credit_number) " +
                "VALUES ('123456789','1234',null);PRAGMA foreign_keys = ON;";
        stmt.executeUpdate(user1);
        String user2 = "INSERT INTO Users (account,password," +
                "credit_number) " +
                "VALUES ('222222222','5678',null);PRAGMA foreign_keys = ON;";
        stmt.executeUpdate(user2);
        String giftcard1 = "PRAGMA foreign_keys = ON;INSERT " +
                "INTO Giftcard (giftcard_id,used) " +
                "VALUES ('1234567890123456GC','0');PRAGMA foreign_keys = ON;";
        stmt.executeUpdate(giftcard1);

    }
    @AfterEach
    public void after() throws Exception{
        String delete_screening = "DELETE FROM Screening;VACUUM;";
        stmt.executeUpdate(delete_screening);
        String delete_movie = "DELETE FROM Movie;VACUUM;";
        stmt.executeUpdate(delete_movie);
        String delete_users = "DELETE FROM Users;VACUUM;";
        stmt.executeUpdate(delete_users);
        String delete_session = "DELETE FROM SESSION;VACUUM;";
        stmt.executeUpdate(delete_session);
        String delete_ticket = "DELETE FROM Ticket;VACUUM;";
        stmt.executeUpdate(delete_ticket);
        String delete_giftcard = "DELETE FROM Giftcard;VACUUM;";
        stmt.executeUpdate(delete_giftcard);
        System.setOut(console);
        stmt.close();
        c.close();

    }
    @Test
    public void checkTicketLeftTest() throws SQLException {
        Calendar updating_time = cal;

        assertEquals(1, customer.checkTicketLeft(updating_time, "Holiday", 2, "disney", "Middle"));
        customer.addTicket("Holiday", 2, "disney", updating_time, "Child", "123456789", "Middle");
        assertEquals(0, customer.checkTicketLeft(updating_time, "Holiday", 2, "disney", "Middle"));
        assertEquals(0, customer.checkTicketLeft(updating_time, "Holiday", 10, "disney", "Middle"));


    }
    @Test
    public void addTicketTest() throws SQLException {
        Calendar updating_time = new GregorianCalendar(2021, 9, 27,10,20,0);
        assertTrue(customer.addTicket("Holiday",2,"disney",updating_time,
                "Child","222222222","Front"));
        ResultSet rs = stmt.executeQuery( "SELECT * FROM Ticket where " +
                "updating_time = '2021-10-27 10:20:00';");
        while ( rs.next() ) {
            assertEquals(rs.getString("user_id"),"222222222");
            assertEquals(rs.getString("movie_name"),"Holiday");
            assertEquals(rs.getInt("screen_id"),2);
            assertEquals(rs.getString("type"),"Child");
            assertEquals(rs.getString("seat"),"Front");
        }
        rs.close();
    }
    @Test
    public void checkTicketTest() throws SQLException{


        Calendar updating_time = cal;
        assertTrue(customer.checkTicket("Holiday",2,"disney",updating_time));
        assertTrue(customer.addTicket("Holiday",2,"disney",updating_time,
                "Child","222222222","Middle"));

        ResultSet rs = stmt.executeQuery( "SELECT * FROM Ticket where " +
                "user_id = '222222222' and movie_name = 'Holiday';");
        while ( rs.next() ) {
//            assertEquals(rs.getString("user_id"),"222222222");
//            assertEquals(rs.getString("movie_name"),"Holiday");
            assertEquals(rs.getString("screen_cinema"),"disney");
            assertEquals(rs.getInt("screen_id"),2);
            assertEquals(rs.getString("type"),"Child");
            assertEquals(rs.getString("seat"),"Middle");
        }
        rs.close();

        assertFalse(customer.checkTicket(null,2,"disney",updating_time));
        assertFalse(customer.checkTicket("Holiday",2,null,updating_time));
        assertFalse(customer.checkTicket("Holiday",2,"disney",null));
        assertFalse(customer.checkTicket("Breaking news",2,"disney",
                updating_time));


    }
        @Test
    public void LoginTest(){
        assertTrue(customer.login("123456789","1234"));
        assertTrue(customer.login("222222222","5678"));
        assertFalse(customer.login("123456789","5678"));
        assertFalse(customer.login(null,"5678"));
        assertFalse(customer.login("123456789",null));
        assertFalse(customer.login(null,null));
        String string="2";
        int count1 = 51;
        assertFalse(customer.login("123456789",string.repeat(count1)));
        assertFalse(customer.login(string.repeat(count1),"5679"));

    }


    @Test
    public void registerTest() throws SQLException {
        assertFalse(customer.register("",""));
        assertFalse(customer.register("012341234",""));
        assertTrue(customer.register("012345678","5678"));
        ResultSet rst = stmt.executeQuery( "SELECT * FROM Users where " +
                "account = '012345678';");
        while ( rst.next() ) {
            assertEquals(rst.getString("password"),"5678");
            //            assertEquals(rst.getString("credit_number"),"1234567891234567");
        }
        rst.close();
        assertFalse(customer.register("012345678","5678"));
        assertFalse(customer.register("012345678","57778"));
        String string="2";
        int count1 = 50;
        int count2 = 51;
        assertTrue(customer.register("112345678",string.repeat(count1)));
        ResultSet rsg = stmt.executeQuery( "SELECT * FROM Users where " +
                "account = '112345678';");
        while ( rsg.next() ) {
            assertEquals(rsg.getString("password"),string.repeat(count1));
            //            assertEquals(rsg.getString("credit_number"),"1234567891234569");
        }
        rsg.close();
        assertFalse(customer.register("212345678",string.repeat(count2)));
        assertFalse(customer.register("212","8888"));
        assertTrue(customer.register("312345678","93838"));
        ResultSet rsgt = stmt.executeQuery( "SELECT * FROM Users where " +
                "account = '312345678';");
        while ( rsgt.next() ) {
            assertEquals(rsgt.getString("password"),"93838");
            //            assertEquals(rsgt.getString("credit_number"),string.repeat(count3));
        }
        rsgt.close();
        assertTrue(customer.register("412345678","0000"));
        assertFalse(customer.register(null,"0000"));
        assertTrue(customer.register("412345888","0000"));
        assertFalse(customer.register("412345884",null));
        assertFalse(customer.register(null,null));

    }
    @Test
    public void checkSessionTest() throws SQLException {
        cal = Calendar.getInstance();
        cal.add(Calendar.SECOND,1);
        assertTrue(customer.checkSession("Holiday"));
        assertTrue(customer.checkSession("Holiday"));
        assertFalse(customer.checkSession("Breaking News"));
        assertFalse(customer.checkSession(null));
        assertTrue(customer.checkSession("The Penthouse: War in Life - Season 4"));
        assertFalse(customer.checkSession("whatever"));
        ResultSet rsfr = stmt.executeQuery( "SELECT * FROM Session where " +
                "movie_name = 'Holiday' and screen_id = 2;");
        while ( rsfr.next() ) {
            assertEquals(rsfr.getString("screen_cinema"),"disney");
            assertEquals(rsfr.getInt("cur_middle_seat"),1);
            assertEquals(rsfr.getInt("cur_front_seat"),20);
            assertEquals(rsfr.getInt("cur_back_seat"),20);
        }
        rsfr.close();
        ResultSet rsfrt = stmt.executeQuery( "SELECT * FROM Session where " +
                "movie_name = 'whatever';");
        assertFalse(rsfrt.next());
        assertFalse(customer.checkSession("The Avengers"));
        assertFalse(customer.checkSession("Titanic"));

    }

    @Test
    public void checkGoldScreenTest(){
        assertTrue(customer.checkSpecificScreen("Holiday","Gold"));
        assertFalse(customer.checkSpecificScreen("Breaking News","Gold"));
        String value = bytes.toString().trim();
        value = value.replaceAll("\r|\n", "");
    }
    @Test
    public void checkSilverScreenTest(){
        assertTrue(customer.checkSpecificScreen("Holiday","Silver"));
        String val = bytes.toString().trim();
        val = val.replaceAll("\r|\n", "");
    }

    @Test
    public void checkNullScreenTest(){
        assertFalse(customer.checkSpecificScreen("Holiday",null));
        assertFalse(customer.checkSpecificScreen(null,"Gold"));
        assertFalse(customer.checkSpecificScreen("Holiday","BigScreen"));
    }
    @Test
    public void checkBronzeTest(){
        assertTrue(customer.checkSpecificScreen("Holiday","Bronze"));
        String val = bytes.toString().trim();
        val = val.replaceAll("\r|\n", "");
    }
    @Test
    public void checkMovieTest(){
        String result ="Movie_name: HolidayClassification: GeneralSynopsis: " +
                "Family ComedyRelease_date: 2021-10-23 20:00:00Director: " +
                "AliceCasts: Suzana, PeterMovie_name: The Penthouse: War in Life - Season 4Classification: Mature AccompaniesSynopsis: Dramatically UnbelievableRelease_date: 2021-10-23 22:30:00Director: Kim Soon-okCasts: Cheon seo-jin, Oh Yoon-hee";
        assertTrue(customer.checkMovie());

        String value = bytes.toString().trim();

        value = value.replaceAll("\r|\n", "");

        assertEquals(result, value);
    }
    @Test
    public void checkNoMovieTest() throws SQLException {

        String delete_movie = "DELETE FROM Movie;VACUUM;";
        stmt.executeUpdate(delete_movie);
        String delete_session = "DELETE FROM SESSION;VACUUM;";
        stmt.executeUpdate(delete_session);
        String result = "Do not have movie yet";
        assertFalse(customer.checkMovie());

        String value = bytes.toString().trim();

        value = value.replaceAll("\r|\n", "");
        assertEquals(result, value);
    }

    @Test
    public void checkSpecificCinemaTest00() throws SQLException {

        String result = "screen type false";
        assertFalse(customer.checkSpecificCinema(null,null));
        String value = bytes.toString().trim();
        value = value.replaceAll("\r|\n", "");
        assertEquals(result, value);
    }

    @Test
    public void checkSpecificCinemaTest01() throws SQLException {
        assertTrue(customer.checkSpecificCinema("Holiday","SBS"));
    }

    @Test
    public void checkSpecificCinemaTest02() throws SQLException {

        String result = "This movie does not have such session with this " +
                "cinema";
        assertFalse(customer.checkSpecificCinema("Holida","SBS"));
        String value = bytes.toString().trim();
        value = value.replaceAll("\r|\n", "");
        assertEquals(result, value);
    }
}
