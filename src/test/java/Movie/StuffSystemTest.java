package Movie;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.*;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class StuffSystemTest {
    CinemaSystem cs;
    ByteArrayOutputStream bytes = null;
    PrintStream console = null;
    Connection c;
    Statement stmt;
    Calendar cal;

    @BeforeEach
    public void setUp() throws Exception {
        try {
            bytes = new ByteArrayOutputStream();
            console = System.out;
            System.setOut(new PrintStream(bytes));
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:src/main/resources/test.db");
            stmt = c.createStatement();

            String screen1 = "INSERT INTO Screening (id,screen,front_seat," +
                    "middle_seat,back_seat,cinema) " +
                    "VALUES (1, 'Gold', 10, 10, 10,'disney');PRAGMA foreign_keys" +
                    " = ON;";
            stmt.executeUpdate(screen1);
            String screen2 = "INSERT INTO Screening (id,screen,front_seat," +
                    "middle_seat,back_seat,cinema) " +
                    "VALUES (2, 'Gold', 20, 20, 20,'disney');PRAGMA " +
                    "foreign_keys " +
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
            cal.add(Calendar.SECOND, 1);
            Date date = cal.getTime();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String strDate = dateFormat.format(date);
            String session1_holiday = String.format("PRAGMA foreign_keys = ON;INSERT " +
                    "INTO Session (updating_time,movie_name,screen_id," +
                    "screen_cinema,cur_front_seat,cur_middle_seat,cur_back_seat)" +
                    " " +
                    "VALUES ('%s','Holiday',2,'disney',20, 20, " +
                    "20);" +
                    "PRAGMA" +
                    " foreign_keys = ON;", strDate);
            stmt.executeUpdate(session1_holiday);
            String session2_holiday = String.format("PRAGMA foreign_keys = ON;INSERT " +
                    "INTO Session (updating_time,movie_name,screen_id,screen_cinema,cur_front_seat,cur_middle_seat,cur_back_seat) " +
                    "VALUES ('%s','Holiday',3, 'SBS', 30, 30, " +
                    "30);" +
                    "PRAGMA foreign_keys = ON;", strDate);
            stmt.executeUpdate(session2_holiday);
            String session3_holiday = String.format("PRAGMA foreign_keys = ON;INSERT " +
                    "INTO Session (updating_time,movie_name,screen_id,screen_cinema,cur_front_seat,cur_middle_seat,cur_back_seat) " +
                    "VALUES ('%s','Holiday',4, 'SBS', 40, 40, " +
                    "40);" +
                    "PRAGMA foreign_keys = ON;", strDate);
            stmt.executeUpdate(session3_holiday);
//        Calendar currentTime = DBUtils.getCurrentTime();
            String session1_penthouse = String.format("PRAGMA foreign_keys = ON;INSERT " +
                    "INTO Session (updating_time,movie_name,screen_id,screen_cinema,cur_front_seat,cur_middle_seat,cur_back_seat) " +
                    "VALUES ('%s','The Penthouse: War in Life -" +
                    " " +
                    "Season 4',1,'disney', 10, 10, 10);PRAGMA foreign_keys = ON;", strDate);
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
            String giftcard2 = "PRAGMA foreign_keys = ON;INSERT " +
                    "INTO Giftcard (giftcard_id,used) " +
                    "VALUES ('0987654321098765GC','1');PRAGMA foreign_keys = " +
                    "ON;";
            stmt.executeUpdate(giftcard2);
            String ticket1 = String.format("INSERT INTO Ticket (user_id," +
                    "updating_time," +
                    "movie_name,screen_id,screen_cinema,type,seat) " +
                    "VALUES ('123456789','%s', 'Holiday', " +
                    "2,'disney',  " +
                    "'Child','Middle');PRAGMA foreign_keys = ON;", strDate);
            stmt.executeUpdate(ticket1);

        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            JSONParser jsonParser = new JSONParser();
            JSONObject config = null;
            try {
                config = (JSONObject) jsonParser.parse(new FileReader(
                        "StaffcinemaSystem.json"));
            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }
            if (config != null) {
                String mode = (String) config.get("mode");
                if (!mode.equals("customer") && !mode.equals("staff") && !mode.equals("manager")) {
                    System.out.println("Please enter correct mode");
                    return;
                }
                double normal_price, percent_child, percent_student, percent_adult, percent_senior, percent_gold, percent_silver, percent_bronze;
                try {
                    normal_price = (Double) config.get("normal_price");
                    percent_child = (Double) config.get("child");
                    percent_student = (Double) config.get("student");
                    percent_adult = (Double) config.get("adult");
                    percent_senior = (Double) config.get("senior");
                    percent_gold = (Double) config.get("gold");
                    percent_silver = (Double) config.get("silver");
                    percent_bronze = (Double) config.get("bronze");
                    if (percent_adult < 0 || percent_student < 0 || percent_adult < 0 || percent_student < 0) {
                        System.out.println("Percentage should between 0 - 1");
                        return;
                    }
                    if (percent_adult > 1 || percent_student > 1 || percent_adult > 1 || percent_student > 1) {
                        System.out.println("Percentage should between 0 - 1");
                        return;
                    }
                    cs = new CinemaSystem("test.db", false, normal_price,
                            percent_child, percent_student,
                            percent_adult, percent_senior, percent_gold, percent_silver, percent_bronze);
                    cs.test_mode = true;
//                    cs.stuffRun();


                } catch (Exception e) {
                    System.out.println("Please enter correct price");
                }
            }
        } catch (Exception e) {
            System.out.println("System wrong");
        }
    }

    @AfterEach
    public void after() throws Exception {

        try {
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
            String delete_stuff = "DELETE FROM Stuff;VACUUM;";
            stmt.executeUpdate(delete_stuff);
            String delete_manager = "DELETE FROM Manager;VACUUM;";
            stmt.executeUpdate(delete_manager);
            String delete_transactions = "DELETE FROM Transactions;VACUUM;";
            stmt.executeUpdate(delete_transactions);

            System.setOut(console);
            stmt.close();
            c.close();
            DBUtils.deleteFile("src/main/resources/","Cancel");
            DBUtils.deleteFile("src/main/resources/","Movie");
            DBUtils.deleteFile("src/main/resources/","NotReleased");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void addMovieTest00()  {
        String s = "1\r\n1\r\nmovie\r\nGeneral\r\nsynopsis\r\n2021-11-01 11:11:11\r\ndirector\r\ncast\r\nb\r\nb\r\nb\r\n";
        ByteArrayInputStream strIn = new ByteArrayInputStream(s.getBytes());
        System.setIn(strIn);
        cs.stuffRun();
        String result = "Select the data you want to edit(1.Movie 2.Session 3.Gift Card b.exist)1.add new movie 2.delete moive 3.edit moive 4.get upcoming movies (b.back)Please enter movie name (b.back)Please enter classification (b.back)General (G), Parental Guidance (PG), Mature (M), Mature Accompanies (MA15+), Restricted (R18+)Please enter synopsis (b.back)Please enter release date (b.back)yyyy-MM-dd HH:mm:ssPlease enter director (b.back)Please enter cast (b.back)actor1 actor2castMovie added successfully!Add movie successfully1.add new movie 2.delete moive 3.edit moive 4.get upcoming movies (b.back)Select the data you want to edit(1.Movie 2.Session 3.Gift Card b.exist)";
        String value = bytes.toString().trim();
        value = value.replaceAll("\r|\n", "");
        assertEquals(result, value);
        new Stuff("test.db").deleteMovie("movie");
    }

    @Test
    public void addMovieTest01()  {
        String s = "1\r\n1\r\nmovie\r\nclass\r\nsynopsis\r\n2021-11-01 11:11:11\r\ndirector\r\nb\r\nb\r\nb\r\n";
        ByteArrayInputStream strIn = new ByteArrayInputStream(s.getBytes());
        System.setIn(strIn);
        cs.stuffRun();
        String result = "Select the data you want to edit(1.Movie 2.Session 3.Gift Card b.exist)1.add new movie 2.delete moive 3.edit moive 4.get upcoming movies (b.back)Please enter movie name (b.back)Please enter classification (b.back)General (G), Parental Guidance (PG), Mature (M), Mature Accompanies (MA15+), Restricted (R18+)Please enter synopsis (b.back)Please enter release date (b.back)yyyy-MM-dd HH:mm:ssPlease enter director (b.back)Please enter cast (b.back)actor1 actor21.add new movie 2.delete moive 3.edit moive 4.get upcoming movies (b.back)Select the data you want to edit(1.Movie 2.Session 3.Gift Card b.exist)";
        String value = bytes.toString().trim();
        value = value.replaceAll("\r|\n", "");
        assertEquals(result, value);

    }

    @Test
    public void addMovieTest02()  {
        String s = "1\r\n1\r\nmovie\r\nclass\r\nsynopsis\r\n2021-11-01 11:11:11\r\nb\r\nb\r\nb\r\n";
        ByteArrayInputStream strIn = new ByteArrayInputStream(s.getBytes());
        System.setIn(strIn);
        cs.stuffRun();
        String result = "Select the data you want to edit(1.Movie 2.Session 3.Gift Card b.exist)1.add new movie 2.delete moive 3.edit moive 4.get upcoming movies (b.back)Please enter movie name (b.back)Please enter classification (b.back)General (G), Parental Guidance (PG), Mature (M), Mature Accompanies (MA15+), Restricted (R18+)Please enter synopsis (b.back)Please enter release date (b.back)yyyy-MM-dd HH:mm:ssPlease enter director (b.back)1.add new movie 2.delete moive 3.edit moive 4.get upcoming movies (b.back)Select the data you want to edit(1.Movie 2.Session 3.Gift Card b.exist)";
        String value = bytes.toString().trim();
        value = value.replaceAll("\r|\n", "");
        assertEquals(result, value);

    }

    @Test
    public void addMovieTest03()  {
        String s = "1\r\n1\r\nmovie\r\nclass\r\nsynopsis\r\n2021-11-0a11:11:11\r\nb\r\nb\r\nb\r\n";
        ByteArrayInputStream strIn = new ByteArrayInputStream(s.getBytes());
        System.setIn(strIn);
        cs.stuffRun();
        String result = "Select the data you want to edit(1.Movie 2.Session 3.Gift Card b.exist)1.add new movie 2.delete moive 3.edit moive 4.get upcoming movies (b.back)Please enter movie name (b.back)Please enter classification (b.back)General (G), Parental Guidance (PG), Mature (M), Mature Accompanies (MA15+), Restricted (R18+)Please enter synopsis (b.back)Please enter release date (b.back)yyyy-MM-dd HH:mm:ssPlease enter correct calendar formatPlease enter movie name (b.back)1.add new movie 2.delete moive 3.edit moive 4.get upcoming movies (b.back)Select the data you want to edit(1.Movie 2.Session 3.Gift Card b.exist)";
        String value = bytes.toString().trim();
        value = value.replaceAll("\r|\n", "");
        assertEquals(result, value);

    }

    @Test
    public void addMovieTest04()  {
        String s = "1\r\n1\r\nmovie\r\nclass\r\nsynopsis\r\nb\r\nb\r\nb\r\n";
        ByteArrayInputStream strIn = new ByteArrayInputStream(s.getBytes());
        System.setIn(strIn);
        cs.stuffRun();
        String result = "Select the data you want to edit(1.Movie 2.Session 3.Gift Card b.exist)1.add new movie 2.delete moive 3.edit moive 4.get upcoming movies (b.back)Please enter movie name (b.back)Please enter classification (b.back)General (G), Parental Guidance (PG), Mature (M), Mature Accompanies (MA15+), Restricted (R18+)Please enter synopsis (b.back)Please enter release date (b.back)yyyy-MM-dd HH:mm:ss1.add new movie 2.delete moive 3.edit moive 4.get upcoming movies (b.back)Select the data you want to edit(1.Movie 2.Session 3.Gift Card b.exist)";
        String value = bytes.toString().trim();
        value = value.replaceAll("\r|\n", "");
        assertEquals(result, value);

    }

    @Test
    public void addMovieTest05()  {
        String s = "1\r\n1\r\nmovie\r\nclass\r\nb\r\nb\r\nb\r\n";
        ByteArrayInputStream strIn = new ByteArrayInputStream(s.getBytes());
        System.setIn(strIn);
        cs.stuffRun();
        String result = "Select the data you want to edit(1.Movie 2.Session 3.Gift Card b.exist)1.add new movie 2.delete moive 3.edit moive 4.get upcoming movies (b.back)Please enter movie name (b.back)Please enter classification (b.back)General (G), Parental Guidance (PG), Mature (M), Mature Accompanies (MA15+), Restricted (R18+)Please enter synopsis (b.back)1.add new movie 2.delete moive 3.edit moive 4.get upcoming movies (b.back)Select the data you want to edit(1.Movie 2.Session 3.Gift Card b.exist)";
        String value = bytes.toString().trim();
        value = value.replaceAll("\r|\n", "");
        assertEquals(result, value);

    }

    @Test
    public void addMovieTest06()  {
        String s = "1\r\n1\r\nmovie\r\nb\r\nb\r\nb\r\n";
        ByteArrayInputStream strIn = new ByteArrayInputStream(s.getBytes());
        System.setIn(strIn);
        cs.stuffRun();
        String result = "Select the data you want to edit(1.Movie 2.Session 3.Gift Card b.exist)1.add new movie 2.delete moive 3.edit moive 4.get upcoming movies (b.back)Please enter movie name (b.back)Please enter classification (b.back)General (G), Parental Guidance (PG), Mature (M), Mature Accompanies (MA15+), Restricted (R18+)1.add new movie 2.delete moive 3.edit moive 4.get upcoming movies (b.back)Select the data you want to edit(1.Movie 2.Session 3.Gift Card b.exist)";
        String value = bytes.toString().trim();
        value = value.replaceAll("\r|\n", "");
        assertEquals(result, value);

    }

    @Test
    public void addMovieTest07()  {
        String s = "1\r\n1\r\nb\r\nb\r\nb\r\n";
        ByteArrayInputStream strIn = new ByteArrayInputStream(s.getBytes());
        System.setIn(strIn);
        cs.stuffRun();
        String result = "Select the data you want to edit(1.Movie 2.Session 3.Gift Card b.exist)1.add new movie 2.delete moive 3.edit moive 4.get upcoming movies (b.back)Please enter movie name (b.back)1.add new movie 2.delete moive 3.edit moive 4.get upcoming movies (b.back)Select the data you want to edit(1.Movie 2.Session 3.Gift Card b.exist)";
        String value = bytes.toString().trim();
        value = value.replaceAll("\r|\n", "");
        assertEquals(result, value);

    }

    @Test
    public void addMovieTest08()  {
        String s = "1\r\n1\r\n\r\nclass\r\nsynopsis\r\n2021-11-01 11:11:11\r\ndirector\r\ncast\r\nb\r\nb\r\nb\r\n";
        ByteArrayInputStream strIn = new ByteArrayInputStream(s.getBytes());
        System.setIn(strIn);
        cs.stuffRun();
        String result = "Select the data you want to edit(1.Movie 2.Session 3.Gift Card b.exist)1.add new movie 2.delete moive 3.edit moive 4.get upcoming movies (b.back)Please enter movie name (b.back)Please enter classification (b.back)General (G), Parental Guidance (PG), Mature (M), Mature Accompanies (MA15+), Restricted (R18+)Please enter synopsis (b.back)Please enter release date (b.back)yyyy-MM-dd HH:mm:ssPlease enter director (b.back)Please enter cast (b.back)actor1 actor2null falsePlease enter movie name (b.back)1.add new movie 2.delete moive 3.edit moive 4.get upcoming movies (b.back)Select the data you want to edit(1.Movie 2.Session 3.Gift Card b.exist)";
        String value = bytes.toString().trim();
        value = value.replaceAll("\r|\n", "");
        assertEquals(result, value);
    }

    @Test
    public void deleteMovieTest00()  {
        ArrayList<String> lst=new ArrayList<>();
        lst.add("cast");
        new Stuff("test.db").addMovie("movie","General","synopsis",cs.toCalender("2021-11-01 11:11:11"),"director",lst);
        String s = "1\r\n2\r\nmovie\r\nb\r\nb\r\n";
        ByteArrayInputStream strIn = new ByteArrayInputStream(s.getBytes());
        System.setIn(strIn);
        cs.stuffRun();
        String result = "castMovie added successfully!Select the data you want to edit(1.Movie 2.Session 3.Gift Card b.exist)1.add new movie 2.delete moive 3.edit moive 4.get upcoming movies (b.back)Please enter movie name (b.back)Delete Movie successfully1.add new movie 2.delete moive 3.edit moive 4.get upcoming movies (b.back)Select the data you want to edit(1.Movie 2.Session 3.Gift Card b.exist)";
        String value = bytes.toString().trim();
        value = value.replaceAll("\r|\n", "");
        assertEquals(result, value);
    }

    @Test
    public void deleteMovieTest01()  {
        String s = "1\r\n2\r\nb\r\nb\r\nb\r\n";
        ByteArrayInputStream strIn = new ByteArrayInputStream(s.getBytes());
        System.setIn(strIn);
        cs.stuffRun();
        String result = "Select the data you want to edit(1.Movie 2.Session 3.Gift Card b.exist)1.add new movie 2.delete moive 3.edit moive 4.get upcoming movies (b.back)Please enter movie name (b.back)1.add new movie 2.delete moive 3.edit moive 4.get upcoming movies (b.back)Select the data you want to edit(1.Movie 2.Session 3.Gift Card b.exist)";
        String value = bytes.toString().trim();
        value = value.replaceAll("\r|\n", "");
        assertEquals(result, value);
    }

    @Test
    public void deleteMovieTest02()  {
        String s = "1\r\n2\r\nmovie not exist\r\nb\r\nb\r\nb\r\n";
        ByteArrayInputStream strIn = new ByteArrayInputStream(s.getBytes());
        System.setIn(strIn);
        cs.stuffRun();
        String result = "Select the data you want to edit(1.Movie 2.Session 3.Gift Card b.exist)1.add new movie 2.delete moive 3.edit moive 4.get upcoming movies (b.back)Please enter movie name (b.back)The movie does not exist.Please enter movie name (b.back)1.add new movie 2.delete moive 3.edit moive 4.get upcoming movies (b.back)Select the data you want to edit(1.Movie 2.Session 3.Gift Card b.exist)";
        String value = bytes.toString().trim();
        value = value.replaceAll("\r|\n", "");
        assertEquals(result, value);
    }

    @Test
    public void editMovieTest00()  {
        ArrayList<String> lst=new ArrayList<>();
        lst.add("cast");
        new Stuff("test.db").addMovie("movie","General","synopsis",cs.toCalender("2021-11-01 11:11:11"),"director",lst);
        String s = "1\r\n3\r\nmovie\r\nGeneral\r\nsynopsis\r\n2021-11-01 11:11:11\r\ndirector\r\ncast\r\nb\r\nb\r\nb\r\n";
        ByteArrayInputStream strIn = new ByteArrayInputStream(s.getBytes());
        System.setIn(strIn);
        cs.stuffRun();
        String result = "castMovie added successfully!Select the data you want to edit(1.Movie 2.Session 3.Gift Card b.exist)1.add new movie 2.delete moive 3.edit moive 4.get upcoming movies (b.back)Please enter movie name (b.back)Please enter classification (b.back)General (G), Parental Guidance (PG), Mature (M), Mature Accompanies (MA15+), Restricted (R18+)Please enter synopsis (b.back)Please enter release date (b.back)yyyy-MM-dd HH:mm:ssPlease enter director (b.back)Please enter cast (b.back)actor1 actor2Movie was edited successfully!Edit movie successfully1.add new movie 2.delete moive 3.edit moive 4.get upcoming movies (b.back)Select the data you want to edit(1.Movie 2.Session 3.Gift Card b.exist)";
        String value = bytes.toString().trim();
        value = value.replaceAll("\r|\n", "");
        assertEquals(result, value);
        new Stuff("test.db").deleteMovie("movie");
    }

    @Test
    public void editMovieTest01()  {
        String s = "1\r\n3\r\nmovie\r\nclass\r\nsynopsis\r\n2021-11-01 11:11:11\r\ndirector\r\nb\r\nb\r\nb\r\n";
        ByteArrayInputStream strIn = new ByteArrayInputStream(s.getBytes());
        System.setIn(strIn);
        cs.stuffRun();
        String result = "Select the data you want to edit(1.Movie 2.Session 3.Gift Card b.exist)1.add new movie 2.delete moive 3.edit moive 4.get upcoming movies (b.back)Please enter movie name (b.back)Please enter classification (b.back)General (G), Parental Guidance (PG), Mature (M), Mature Accompanies (MA15+), Restricted (R18+)Please enter synopsis (b.back)Please enter release date (b.back)yyyy-MM-dd HH:mm:ssPlease enter director (b.back)Please enter cast (b.back)actor1 actor21.add new movie 2.delete moive 3.edit moive 4.get upcoming movies (b.back)Select the data you want to edit(1.Movie 2.Session 3.Gift Card b.exist)";
        String value = bytes.toString().trim();
        value = value.replaceAll("\r|\n", "");
        assertEquals(result, value);

    }

    @Test
    public void editMovieTest02()  {
        String s = "1\r\n3\r\nmovie\r\nclass\r\nsynopsis\r\n2021-11-01 11:11:11\r\nb\r\nb\r\nb\r\n";
        ByteArrayInputStream strIn = new ByteArrayInputStream(s.getBytes());
        System.setIn(strIn);
        cs.stuffRun();
        String result = "Select the data you want to edit(1.Movie 2.Session 3.Gift Card b.exist)1.add new movie 2.delete moive 3.edit moive 4.get upcoming movies (b.back)Please enter movie name (b.back)Please enter classification (b.back)General (G), Parental Guidance (PG), Mature (M), Mature Accompanies (MA15+), Restricted (R18+)Please enter synopsis (b.back)Please enter release date (b.back)yyyy-MM-dd HH:mm:ssPlease enter director (b.back)1.add new movie 2.delete moive 3.edit moive 4.get upcoming movies (b.back)Select the data you want to edit(1.Movie 2.Session 3.Gift Card b.exist)";
        String value = bytes.toString().trim();
        value = value.replaceAll("\r|\n", "");
        assertEquals(result, value);

    }

    @Test
    public void editMovieTest03()  {
        String s = "1\r\n3\r\nmovie\r\nclass\r\nsynopsis\r\n2021-11-0a11:11:11\r\nb\r\nb\r\nb\r\n";
        ByteArrayInputStream strIn = new ByteArrayInputStream(s.getBytes());
        System.setIn(strIn);
        cs.stuffRun();
        String result = "Select the data you want to edit(1.Movie 2.Session 3.Gift Card b.exist)1.add new movie 2.delete moive 3.edit moive 4.get upcoming movies (b.back)Please enter movie name (b.back)Please enter classification (b.back)General (G), Parental Guidance (PG), Mature (M), Mature Accompanies (MA15+), Restricted (R18+)Please enter synopsis (b.back)Please enter release date (b.back)yyyy-MM-dd HH:mm:ssPlease enter correct numberPlease enter movie name (b.back)1.add new movie 2.delete moive 3.edit moive 4.get upcoming movies (b.back)Select the data you want to edit(1.Movie 2.Session 3.Gift Card b.exist)";
        String value = bytes.toString().trim();
        value = value.replaceAll("\r|\n", "");
        assertEquals(result, value);

    }

    @Test
    public void editMovieTest04()  {
        String s = "1\r\n3\r\nmovie\r\nclass\r\nsynopsis\r\nb\r\nb\r\nb\r\n";
        ByteArrayInputStream strIn = new ByteArrayInputStream(s.getBytes());
        System.setIn(strIn);
        cs.stuffRun();
        String result = "Select the data you want to edit(1.Movie 2.Session 3.Gift Card b.exist)1.add new movie 2.delete moive 3.edit moive 4.get upcoming movies (b.back)Please enter movie name (b.back)Please enter classification (b.back)General (G), Parental Guidance (PG), Mature (M), Mature Accompanies (MA15+), Restricted (R18+)Please enter synopsis (b.back)Please enter release date (b.back)yyyy-MM-dd HH:mm:ss1.add new movie 2.delete moive 3.edit moive 4.get upcoming movies (b.back)Select the data you want to edit(1.Movie 2.Session 3.Gift Card b.exist)";
        String value = bytes.toString().trim();
        value = value.replaceAll("\r|\n", "");
        assertEquals(result, value);

    }

    @Test
    public void editMovieTest05()  {
        String s = "1\r\n3\r\nmovie\r\nclass\r\nb\r\nb\r\nb\r\n";
        ByteArrayInputStream strIn = new ByteArrayInputStream(s.getBytes());
        System.setIn(strIn);
        cs.stuffRun();
        String result = "Select the data you want to edit(1.Movie 2.Session 3.Gift Card b.exist)1.add new movie 2.delete moive 3.edit moive 4.get upcoming movies (b.back)Please enter movie name (b.back)Please enter classification (b.back)General (G), Parental Guidance (PG), Mature (M), Mature Accompanies (MA15+), Restricted (R18+)Please enter synopsis (b.back)1.add new movie 2.delete moive 3.edit moive 4.get upcoming movies (b.back)Select the data you want to edit(1.Movie 2.Session 3.Gift Card b.exist)";
        String value = bytes.toString().trim();
        value = value.replaceAll("\r|\n", "");
        assertEquals(result, value);

    }

    @Test
    public void editMovieTest06()  {
        String s = "1\r\n3\r\nmovie\r\nb\r\nb\r\nb\r\n";
        ByteArrayInputStream strIn = new ByteArrayInputStream(s.getBytes());
        System.setIn(strIn);
        cs.stuffRun();
        String result = "Select the data you want to edit(1.Movie 2.Session 3.Gift Card b.exist)1.add new movie 2.delete moive 3.edit moive 4.get upcoming movies (b.back)Please enter movie name (b.back)Please enter classification (b.back)General (G), Parental Guidance (PG), Mature (M), Mature Accompanies (MA15+), Restricted (R18+)1.add new movie 2.delete moive 3.edit moive 4.get upcoming movies (b.back)Select the data you want to edit(1.Movie 2.Session 3.Gift Card b.exist)";
        String value = bytes.toString().trim();
        value = value.replaceAll("\r|\n", "");
        assertEquals(result, value);

    }

    @Test
    public void editMovieTest07()  {
        String s = "1\r\n3\r\nb\r\nb\r\nb\r\n";
        ByteArrayInputStream strIn = new ByteArrayInputStream(s.getBytes());
        System.setIn(strIn);
        cs.stuffRun();
        String result = "Select the data you want to edit(1.Movie 2.Session 3.Gift Card b.exist)1.add new movie 2.delete moive 3.edit moive 4.get upcoming movies (b.back)Please enter movie name (b.back)1.add new movie 2.delete moive 3.edit moive 4.get upcoming movies (b.back)Select the data you want to edit(1.Movie 2.Session 3.Gift Card b.exist)";
        String value = bytes.toString().trim();
        value = value.replaceAll("\r|\n", "");
        assertEquals(result, value);

    }

    @Test
    public void editMovieTest08()  {
        String s = "1\r\n3\r\n\r\nclass\r\nsynopsis\r\n2021-11-01 11:11:11\r\ndirector\r\ncast\r\nb\r\nb\r\nb\r\n";
        ByteArrayInputStream strIn = new ByteArrayInputStream(s.getBytes());
        System.setIn(strIn);
        cs.stuffRun();
        String result = "Select the data you want to edit(1.Movie 2.Session 3.Gift Card b.exist)1.add new movie 2.delete moive 3.edit moive 4.get upcoming movies (b.back)Please enter movie name (b.back)Please enter classification (b.back)General (G), Parental Guidance (PG), Mature (M), Mature Accompanies (MA15+), Restricted (R18+)Please enter synopsis (b.back)Please enter release date (b.back)yyyy-MM-dd HH:mm:ssPlease enter director (b.back)Please enter cast (b.back)actor1 actor2null falsePlease enter movie name (b.back)1.add new movie 2.delete moive 3.edit moive 4.get upcoming movies (b.back)Select the data you want to edit(1.Movie 2.Session 3.Gift Card b.exist)";
        String value = bytes.toString().trim();
        value = value.replaceAll("\r|\n", "");
        assertEquals(result, value);
    }

    @Test
    public void notReleaseMovieTest00()  {
        String s = "1\r\n4\r\nb\r\nb\r\n";
        ByteArrayInputStream strIn = new ByteArrayInputStream(s.getBytes());
        System.setIn(strIn);
        cs.stuffRun();
        String result = "Select the data you want to edit(1.Movie 2.Session 3.Gift Card b.exist)1.add new movie 2.delete moive 3.edit moive 4.get upcoming movies (b.back)Output upcoming movie successfully!Select the data you want to edit(1.Movie 2.Session 3.Gift Card b.exist)";
        String value = bytes.toString().trim();
        value = value.replaceAll("\r|\n", "");
        assertEquals(result, value);
    }

    @Test
    public void notTest00()  {
        String s = "1\r\n5\r\nb\r\nb\r\n";
        ByteArrayInputStream strIn = new ByteArrayInputStream(s.getBytes());
        System.setIn(strIn);
        cs.stuffRun();
        String result = "Select the data you want to edit(1.Movie 2.Session 3.Gift Card b.exist)1.add new movie 2.delete moive 3.edit moive 4.get upcoming movies (b.back)Please enter correct number1.add new movie 2.delete moive 3.edit moive 4.get upcoming movies (b.back)Select the data you want to edit(1.Movie 2.Session 3.Gift Card b.exist)";
        String value = bytes.toString().trim();
        value = value.replaceAll("\r|\n", "");
        assertEquals(result, value);
    }

    @Test
    public void addSessionTest00()  {
        String s = "2\r\n1\r\nmovie\r\n1\r\n2022-11-01 11:11:11\r\ncinema\r\nb\r\nb\r\nb\r\n";
        ByteArrayInputStream strIn = new ByteArrayInputStream(s.getBytes());
        System.setIn(strIn);
        cs.stuffRun();
        String result = "Select the data you want to edit(1.Movie 2.Session 3.Gift Card b.exist)1.add new session 2.delete session 3.get session report (b.back)Please enter movie name (b.back)Please enter screening id (b.back)Please enter updating time (b.back)yyyy-MM-dd HH:mm:ssPlease enter cinema (b.back)The screen does not existPlease enter movie name (b.back)1.add new session 2.delete session 3.get session report (b.back)Select the data you want to edit(1.Movie 2.Session 3.Gift Card b.exist)";
        String value = bytes.toString().trim();
        value = value.replaceAll("\r|\n", "");
        assertEquals(result, value);
    }


    @Test
    public void addSessionTest01()  {
        String s = "2\r\n1\r\nmovie\r\n1\r\n2022-11-01 11:11:11\r\nb\r\nb\r\nb\r\n";
        ByteArrayInputStream strIn = new ByteArrayInputStream(s.getBytes());
        System.setIn(strIn);
        cs.stuffRun();
        String result = "Select the data you want to edit(1.Movie 2.Session 3.Gift Card b.exist)1.add new session 2.delete session 3.get session report (b.back)Please enter movie name (b.back)Please enter screening id (b.back)Please enter updating time (b.back)yyyy-MM-dd HH:mm:ssPlease enter cinema (b.back)1.add new session 2.delete session 3.get session report (b.back)Select the data you want to edit(1.Movie 2.Session 3.Gift Card b.exist)";
        String value = bytes.toString().trim();
        value = value.replaceAll("\r|\n", "");
        assertEquals(result, value);
    }


    @Test
    public void addSessionTest02()  {
        String s = "2\r\n1\r\nmovie\r\n1\r\nb\r\nb\r\nb\r\n";
        ByteArrayInputStream strIn = new ByteArrayInputStream(s.getBytes());
        System.setIn(strIn);
        cs.stuffRun();
        String result = "Select the data you want to edit(1.Movie 2.Session 3.Gift Card b.exist)1.add new session 2.delete session 3.get session report (b.back)Please enter movie name (b.back)Please enter screening id (b.back)Please enter updating time (b.back)yyyy-MM-dd HH:mm:ss1.add new session 2.delete session 3.get session report (b.back)Select the data you want to edit(1.Movie 2.Session 3.Gift Card b.exist)";
        String value = bytes.toString().trim();
        value = value.replaceAll("\r|\n", "");
        assertEquals(result, value);
    }


    @Test
    public void addSessionTest03()  {
        String s = "2\r\n1\r\nmovie\r\nb\r\nb\r\nb\r\n";
        ByteArrayInputStream strIn = new ByteArrayInputStream(s.getBytes());
        System.setIn(strIn);
        cs.stuffRun();
        String result = "Select the data you want to edit(1.Movie 2.Session 3.Gift Card b.exist)1.add new session 2.delete session 3.get session report (b.back)Please enter movie name (b.back)Please enter screening id (b.back)1.add new session 2.delete session 3.get session report (b.back)Select the data you want to edit(1.Movie 2.Session 3.Gift Card b.exist)";
        String value = bytes.toString().trim();
        value = value.replaceAll("\r|\n", "");
        assertEquals(result, value);
    }

    @Test
    public void addSessionTest04()  {
        String s = "2\r\n1\r\nb\r\nb\r\nb\r\n";
        ByteArrayInputStream strIn = new ByteArrayInputStream(s.getBytes());
        System.setIn(strIn);
        cs.stuffRun();
        String result = "Select the data you want to edit(1.Movie 2.Session 3.Gift Card b.exist)1.add new session 2.delete session 3.get session report (b.back)Please enter movie name (b.back)1.add new session 2.delete session 3.get session report (b.back)Select the data you want to edit(1.Movie 2.Session 3.Gift Card b.exist)";
        String value = bytes.toString().trim();
        value = value.replaceAll("\r|\n", "");
        assertEquals(result, value);
    }

    @Test
    public void addSessionTest05()  {
        String s = "2\r\n1\r\nmovie\r\na\r\nb\r\nb\r\nb\r\n";
        ByteArrayInputStream strIn = new ByteArrayInputStream(s.getBytes());
        System.setIn(strIn);
        cs.stuffRun();
        String result = "Select the data you want to edit(1.Movie 2.Session 3.Gift Card b.exist)1.add new session 2.delete session 3.get session report (b.back)Please enter movie name (b.back)Please enter screening id (b.back)Please enter correct numberPlease enter movie name (b.back)1.add new session 2.delete session 3.get session report (b.back)Select the data you want to edit(1.Movie 2.Session 3.Gift Card b.exist)";
        String value = bytes.toString().trim();
        value = value.replaceAll("\r|\n", "");
        assertEquals(result, value);
    }

    @Test
    public void addSessionTest06()  {
        String s = "2\r\n1\r\nmovie\r\n1\r\n2022-11-0a11:11:11\r\nb\r\nb\r\nb\r\n";
        ByteArrayInputStream strIn = new ByteArrayInputStream(s.getBytes());
        System.setIn(strIn);
        cs.stuffRun();
        String result = "Select the data you want to edit(1.Movie 2.Session 3.Gift Card b.exist)1.add new session 2.delete session 3.get session report (b.back)Please enter movie name (b.back)Please enter screening id (b.back)Please enter updating time (b.back)yyyy-MM-dd HH:mm:ssPlease enter correct numberPlease enter movie name (b.back)1.add new session 2.delete session 3.get session report (b.back)Select the data you want to edit(1.Movie 2.Session 3.Gift Card b.exist)";
        String value = bytes.toString().trim();
        value = value.replaceAll("\r|\n", "");
        assertEquals(result, value);
    }

    @Test
    public void addSessionTest07()  {
        String s = "2\r\n1\r\nmovie\r\n1\r\n2022-11-01 11:11:11\r\ndisney\r\nb\r\nb\r\nb\r\n";
        ByteArrayInputStream strIn = new ByteArrayInputStream(s.getBytes());
        System.setIn(strIn);
        cs.stuffRun();
        String result = "Select the data you want to edit(1.Movie 2.Session 3.Gift Card b.exist)1.add new session 2.delete session 3.get session report (b.back)Please enter movie name (b.back)Please enter screening id (b.back)Please enter updating time (b.back)yyyy-MM-dd HH:mm:ssPlease enter cinema (b.back)2022-11-01 11:11:11Session added successfully!Add session successfully1.add new session 2.delete session 3.get session report (b.back)Select the data you want to edit(1.Movie 2.Session 3.Gift Card b.exist)";
        String value = bytes.toString().trim();
        value = value.replaceAll("\r|\n", "");
        assertEquals(result, value);
        new Stuff("test.db").deleteSession("movie",1,cs.toCalender("2022-11-01 11:11:11"),"disney");
    }

    @Test
    public void deleteSessionTest00()  {
        String s = "2\r\n2\r\nmovie\r\n1\r\n2022-11-01 11:11:11\r\ncinema\r\nb\r\nb\r\nb\r\n";
        ByteArrayInputStream strIn = new ByteArrayInputStream(s.getBytes());
        System.setIn(strIn);
        cs.stuffRun();
        String result = "Select the data you want to edit(1.Movie 2.Session 3.Gift Card b.exist)1.add new session 2.delete session 3.get session report (b.back)Please enter movie name (b.back)Please enter screen id (b.back)Please enter updating time (b.back)yyyy-MM-dd HH:mm:ssPlease enter cinema (b.back)The session does not exist.Please enter movie name (b.back)1.add new session 2.delete session 3.get session report (b.back)Select the data you want to edit(1.Movie 2.Session 3.Gift Card b.exist)";
        String value = bytes.toString().trim();
        value = value.replaceAll("\r|\n", "");
        assertEquals(result, value);
    }


    @Test
    public void deleteSessionTest01()  {
        String s = "2\r\n2\r\nmovie\r\n1\r\n2022-11-01 11:11:11\r\nb\r\nb\r\nb\r\n";
        ByteArrayInputStream strIn = new ByteArrayInputStream(s.getBytes());
        System.setIn(strIn);
        cs.stuffRun();
        String result = "Select the data you want to edit(1.Movie 2.Session 3.Gift Card b.exist)1.add new session 2.delete session 3.get session report (b.back)Please enter movie name (b.back)Please enter screen id (b.back)Please enter updating time (b.back)yyyy-MM-dd HH:mm:ssPlease enter cinema (b.back)1.add new session 2.delete session 3.get session report (b.back)Select the data you want to edit(1.Movie 2.Session 3.Gift Card b.exist)";
        String value = bytes.toString().trim();
        value = value.replaceAll("\r|\n", "");
        assertEquals(result, value);
    }


    @Test
    public void deleteSessionTest02()  {
        String s = "2\r\n2\r\nmovie\r\n1\r\nb\r\nb\r\nb\r\n";
        ByteArrayInputStream strIn = new ByteArrayInputStream(s.getBytes());
        System.setIn(strIn);
        cs.stuffRun();
        String result = "Select the data you want to edit(1.Movie 2.Session 3.Gift Card b.exist)1.add new session 2.delete session 3.get session report (b.back)Please enter movie name (b.back)Please enter screen id (b.back)Please enter updating time (b.back)yyyy-MM-dd HH:mm:ss1.add new session 2.delete session 3.get session report (b.back)Select the data you want to edit(1.Movie 2.Session 3.Gift Card b.exist)";
        String value = bytes.toString().trim();
        value = value.replaceAll("\r|\n", "");
        assertEquals(result, value);
    }


    @Test
    public void deleteSessionTest03()  {
        String s = "2\r\n2\r\nmovie\r\nb\r\nb\r\nb\r\n";
        ByteArrayInputStream strIn = new ByteArrayInputStream(s.getBytes());
        System.setIn(strIn);
        cs.stuffRun();
        String result = "Select the data you want to edit(1.Movie 2.Session 3.Gift Card b.exist)1.add new session 2.delete session 3.get session report (b.back)Please enter movie name (b.back)Please enter screen id (b.back)1.add new session 2.delete session 3.get session report (b.back)Select the data you want to edit(1.Movie 2.Session 3.Gift Card b.exist)";
        String value = bytes.toString().trim();
        value = value.replaceAll("\r|\n", "");
        assertEquals(result, value);
    }

    @Test
    public void deleteSessionTest04()  {
        String s = "2\r\n2\r\nb\r\nb\r\nb\r\n";
        ByteArrayInputStream strIn = new ByteArrayInputStream(s.getBytes());
        System.setIn(strIn);
        cs.stuffRun();
        String result = "Select the data you want to edit(1.Movie 2.Session 3.Gift Card b.exist)1.add new session 2.delete session 3.get session report (b.back)Please enter movie name (b.back)1.add new session 2.delete session 3.get session report (b.back)Select the data you want to edit(1.Movie 2.Session 3.Gift Card b.exist)";
        String value = bytes.toString().trim();
        value = value.replaceAll("\r|\n", "");
        assertEquals(result, value);
    }

    @Test
    public void deleteSessionTest05()  {
        String s = "2\r\n2\r\nmovie\r\na\r\nb\r\nb\r\nb\r\n";
        ByteArrayInputStream strIn = new ByteArrayInputStream(s.getBytes());
        System.setIn(strIn);
        cs.stuffRun();
        String result = "Select the data you want to edit(1.Movie 2.Session 3.Gift Card b.exist)1.add new session 2.delete session 3.get session report (b.back)Please enter movie name (b.back)Please enter screen id (b.back)Please enter correct numberPlease enter movie name (b.back)1.add new session 2.delete session 3.get session report (b.back)Select the data you want to edit(1.Movie 2.Session 3.Gift Card b.exist)";
        String value = bytes.toString().trim();
        value = value.replaceAll("\r|\n", "");
        assertEquals(result, value);
    }

    @Test
    public void deleteSessionTest06()  {
        String s = "2\r\n2\r\nmovie\r\n1\r\n2022-11-0a11:11:11\r\nb\r\nb\r\nb\r\n";
        ByteArrayInputStream strIn = new ByteArrayInputStream(s.getBytes());
        System.setIn(strIn);
        cs.stuffRun();
        String result = "Select the data you want to edit(1.Movie 2.Session 3.Gift Card b.exist)1.add new session 2.delete session 3.get session report (b.back)Please enter movie name (b.back)Please enter screen id (b.back)Please enter updating time (b.back)yyyy-MM-dd HH:mm:ssPlease enter correct numberPlease enter movie name (b.back)1.add new session 2.delete session 3.get session report (b.back)Select the data you want to edit(1.Movie 2.Session 3.Gift Card b.exist)";
        String value = bytes.toString().trim();
        value = value.replaceAll("\r|\n", "");
        assertEquals(result, value);
    }

    @Test
    public void deleteSessionTest07()  {
        new Stuff("test.db").addSession("movie",1,cs.toCalender("2022-11-01 11:11:11"),"disney");
        String s = "2\r\n2\r\nmovie\r\n1\r\n2022-11-01 11:11:11\r\ndisney\r\nb\r\nb\r\nb\r\n";
        ByteArrayInputStream strIn = new ByteArrayInputStream(s.getBytes());
        System.setIn(strIn);
        cs.stuffRun();
        String result = "2022-11-01 11:11:11Session added successfully!Select the data you want to edit(1.Movie 2.Session 3.Gift Card b.exist)1.add new session 2.delete session 3.get session report (b.back)Please enter movie name (b.back)Please enter screen id (b.back)Please enter updating time (b.back)yyyy-MM-dd HH:mm:ssPlease enter cinema (b.back)The session was deleted successfully.Delete session successfully1.add new session 2.delete session 3.get session report (b.back)Select the data you want to edit(1.Movie 2.Session 3.Gift Card b.exist)";
        String value = bytes.toString().trim();
        value = value.replaceAll("\r|\n", "");
        assertEquals(result, value);
    }

    @Test
    public void outputSessionTest00()  {
        String s = "2\r\n3\r\nb\r\nb\r\nb\r\n";
        ByteArrayInputStream strIn = new ByteArrayInputStream(s.getBytes());
        System.setIn(strIn);
        cs.stuffRun();
        String result = "Select the data you want to edit(1.Movie 2.Session 3.Gift Card b.exist)1.add new session 2.delete session 3.get session report (b.back)Output session report successfully!Select the data you want to edit(1.Movie 2.Session 3.Gift Card b.exist)";
        String value = bytes.toString().trim();
        value = value.replaceAll("\r|\n", "");
        assertEquals(result, value);
    }

    @Test
    public void notTest01()  {
        String s = "2\r\n4\r\nb\r\nb\r\nb\r\n";
        ByteArrayInputStream strIn = new ByteArrayInputStream(s.getBytes());
        System.setIn(strIn);
        cs.stuffRun();
        String result = "Select the data you want to edit(1.Movie 2.Session 3.Gift Card b.exist)1.add new session 2.delete session 3.get session report (b.back)Please enter correct number1.add new session 2.delete session 3.get session report (b.back)Select the data you want to edit(1.Movie 2.Session 3.Gift Card b.exist)";
        String value = bytes.toString().trim();
        value = value.replaceAll("\r|\n", "");
        assertEquals(result, value);
    }

    @Test
    public void addGiftTest00()  {
        String s = "3\r\ngift\r\nb\r\nb\r\nb\r\n";
        ByteArrayInputStream strIn = new ByteArrayInputStream(s.getBytes());
        System.setIn(strIn);
        cs.stuffRun();
        String result = "Select the data you want to edit(1.Movie 2.Session 3.Gift Card b.exist)Please enter Gift card number. (b.back)The gift card id must be a 18 digital stringPlease enter Gift card number. (b.back)Select the data you want to edit(1.Movie 2.Session 3.Gift Card b.exist)";
        String value = bytes.toString().trim();
        value = value.replaceAll("\r|\n", "");
        assertEquals(result, value);
    }

    @Test
    public void addGiftTest01()  {
        String s = "3\r\nb\r\nb\r\nb\r\nb\r\n";
        ByteArrayInputStream strIn = new ByteArrayInputStream(s.getBytes());
        System.setIn(strIn);
        cs.stuffRun();
        String result = "Select the data you want to edit(1.Movie 2.Session 3.Gift Card b.exist)Please enter Gift card number. (b.back)Select the data you want to edit(1.Movie 2.Session 3.Gift Card b.exist)";
        String value = bytes.toString().trim();
        value = value.replaceAll("\r|\n", "");
        assertEquals(result, value);
    }

    @Test
    public void addGiftTest02()  {
        String s = "3\r\n0123456789543210GC\r\nb\r\nb\r\nb\r\n";
        ByteArrayInputStream strIn = new ByteArrayInputStream(s.getBytes());
        System.setIn(strIn);
        cs.stuffRun();
        String result = "Select the data you want to edit(1.Movie 2.Session 3.Gift Card b.exist)Please enter Gift card number. (b.back)The gift card was added successfully.Add gift card successfullySelect the data you want to edit(1.Movie 2.Session 3.Gift Card b.exist)";
        String value = bytes.toString().trim();
        value = value.replaceAll("\r|\n", "");
        assertEquals(result, value);
    }
    @Test
    public void Staff00()  {
        String s = "4\r\nb\r\nb\r\nb";
        ByteArrayInputStream strIn = new ByteArrayInputStream(s.getBytes());
        System.setIn(strIn);
        cs.stuffRun();
        String result = "Select the data you want to edit(1.Movie 2.Session 3.Gift Card b.exist)Please enter correct numberSelect the data you want to edit(1.Movie 2.Session 3.Gift Card b.exist)";
        String value = bytes.toString().trim();
        value = value.replaceAll("\r|\n", "");
        assertEquals(result, value);
    }
    @Test
    public void Staff01()  {
        String s = "5\r\nb\r\nb\r\nb";
        ByteArrayInputStream strIn = new ByteArrayInputStream(s.getBytes());
        System.setIn(strIn);
        cs.stuffRun();
        String result = "Select the data you want to edit(1.Movie 2.Session 3.Gift Card b.exist)Please enter correct numberSelect the data you want to edit(1.Movie 2.Session 3.Gift Card b.exist)";
        String value = bytes.toString().trim();
        value = value.replaceAll("\r|\n", "");
        assertEquals(result, value);
    }
    @Test
    public void Staff02()  {
        String s = "6\r\nb\r\nb\r\nb";
        ByteArrayInputStream strIn = new ByteArrayInputStream(s.getBytes());
        System.setIn(strIn);
        cs.stuffRun();
        String result = "Select the data you want to edit(1.Movie 2.Session 3.Gift Card b.exist)Please enter correct numberSelect the data you want to edit(1.Movie 2.Session 3.Gift Card b.exist)";
        String value = bytes.toString().trim();
        value = value.replaceAll("\r|\n", "");
        assertEquals(result, value);
    }

    
}