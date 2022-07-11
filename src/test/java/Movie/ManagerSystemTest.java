package Movie;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ManagerSystemTest {

    ByteArrayOutputStream bytes = null;
    PrintStream console = null;
    Connection c;
    Statement stmt;
    Calendar cal;
    CinemaSystem cs;
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
            cal.add(Calendar.SECOND,1);
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
            String giftcard2 = "PRAGMA foreign_keys = ON;INSERT " +
                    "INTO Giftcard (giftcard_id,used) " +
                    "VALUES ('0987654321098765GC','1');PRAGMA foreign_keys = " +
                    "ON;";
            stmt.executeUpdate(giftcard2);
            String ticket1 = String.format("INSERT INTO Ticket (user_id," +
                    "updating_time," +
                    "movie_name,screen_id,screen_cinema,type,seat) " +
                    "VALUES ('123456789','%s', 'Holiday', " +
                    "2,'disney', " +
                    "'Child','Middle');PRAGMA foreign_keys = ON;",strDate);
            stmt.executeUpdate(ticket1);
            String staff1 = String.format("INSERT INTO Stuff VALUES ('123456789','abc');PRAGMA foreign_keys = ON;");
            stmt.executeUpdate(staff1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            JSONParser jsonParser = new JSONParser();
            JSONObject config = null;
            try {
                config = (JSONObject) jsonParser.parse(new FileReader(
                        "ManagercinemaSystem.json"));
            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }
            if (config != null) {
                String mode = (String) config.get("mode");
                if (!mode.equals("customer") && !mode.equals("stuff") && !mode.equals("manager")) {
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
                    cs = new CinemaSystem("test.db", true, normal_price,
                            percent_child, percent_student,
                            percent_adult, percent_senior, percent_gold,percent_silver,percent_bronze);
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
    public void after() throws Exception{

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

        }catch (Exception e){
            e.printStackTrace();
        }
    }



    //    #addStuff Successful
    @Test
    public void addStaff00(){
        String s = "4\r\n1\r\n222333444\r\n123456\r\nb\r\nb";
        ByteArrayInputStream strIn = new ByteArrayInputStream(s.getBytes());
        System.setIn(strIn);
        cs.stuffRun();
        String result = "Select the data you want to edit(1.Movie 2.Session 3.Gift Card 4.Staff 5.Cancel Transaction 6.Screening b.exist)1. add staff 2. delete staff b.backPlease enter staff id (b.back)Please enter staff password (b.back)Add the  successfully!1. add staff 2. delete staff b.backSelect the data you want to edit(1.Movie 2.Session 3.Gift Card 4.Staff 5.Cancel Transaction 6.Screening b.exist)";
        String value = bytes.toString().trim();
        value = value.replaceAll("\r|\n", "");
        assertEquals(result, value);
        new Manager("test.db").removeStuff("222333444");

    }
   //    #addStuff null Password
   @Test
   public void addStaff01(){
       String s = "4\r\n1\r\n222333444\r\n\r\nb\r\nb\r\nb";
       ByteArrayInputStream strIn = new ByteArrayInputStream(s.getBytes());
       System.setIn(strIn);
       cs.stuffRun();
       String result = "Select the data you want to edit(1.Movie 2.Session 3.Gift Card 4.Staff 5.Cancel Transaction 6.Screening b.exist)1. add staff 2. delete staff b.backPlease enter staff id (b.back)Please enter staff password (b.back)Password less than 50.Please enter staff id (b.back)1. add staff 2. delete staff b.backSelect the data you want to edit(1.Movie 2.Session 3.Gift Card 4.Staff 5.Cancel Transaction 6.Screening b.exist)";
       String value = bytes.toString().trim();
       value = value.replaceAll("\r|\n", "");
       assertEquals(result, value);
   }
   //    #addStuff null staff account number
   @Test
   public void addStaff02(){
       String s = "4\r\n1\r\n\r\n\r\nb\r\nb\r\nb";
       ByteArrayInputStream strIn = new ByteArrayInputStream(s.getBytes());
       System.setIn(strIn);
       cs.stuffRun();
       String result = "Select the data you want to edit(1.Movie 2.Session 3.Gift Card 4.Staff 5.Cancel Transaction 6.Screening b.exist)1. add staff 2. delete staff b.backPlease enter staff id (b.back)Please enter staff password (b.back)Account must be limit 9 char.Please enter staff id (b.back)1. add staff 2. delete staff b.backSelect the data you want to edit(1.Movie 2.Session 3.Gift Card 4.Staff 5.Cancel Transaction 6.Screening b.exist)";
       String value = bytes.toString().trim();
       value = value.replaceAll("\r|\n", "");
       assertEquals(result, value);
   }
   //    back from enter stuff password
   @Test
   public void addStaff03(){
       String s = "4\r\n1\r\n\r\nb\r\nb\r\nb";
       ByteArrayInputStream strIn = new ByteArrayInputStream(s.getBytes());
       System.setIn(strIn);
       cs.stuffRun();
       String result = "Select the data you want to edit(1.Movie 2.Session 3.Gift Card 4.Staff 5.Cancel Transaction 6.Screening b.exist)1. add staff 2. delete staff b.backPlease enter staff id (b.back)Please enter staff password (b.back)1. add staff 2. delete staff b.backSelect the data you want to edit(1.Movie 2.Session 3.Gift Card 4.Staff 5.Cancel Transaction 6.Screening b.exist)";
       String value = bytes.toString().trim();
       value = value.replaceAll("\r|\n", "");
       assertEquals(result, value);
   }
   //    back from enter stuff id
   @Test
   public void addStaff04(){
       String s = "4\r\n1\r\nb\r\nb\r\nb";
       ByteArrayInputStream strIn = new ByteArrayInputStream(s.getBytes());
       System.setIn(strIn);
       cs.stuffRun();
       String result = "Select the data you want to edit(1.Movie 2.Session 3.Gift Card 4.Staff 5.Cancel Transaction 6.Screening b.exist)1. add staff 2. delete staff b.backPlease enter staff id (b.back)1. add staff 2. delete staff b.backSelect the data you want to edit(1.Movie 2.Session 3.Gift Card 4.Staff 5.Cancel Transaction 6.Screening b.exist)";
       String value = bytes.toString().trim();
       value = value.replaceAll("\r|\n", "");
       assertEquals(result, value);
   }
      //    stuff id more than 9 char
    @Test
    public void addStaff05(){
        String s = "4\r\n1\r\n2223334444\r\n123456\r\nb\r\nb\r\nb";
        ByteArrayInputStream strIn = new ByteArrayInputStream(s.getBytes());
        System.setIn(strIn);
        cs.stuffRun();
        String result = "Select the data you want to edit(1.Movie 2.Session 3.Gift Card 4.Staff 5.Cancel Transaction 6.Screening b.exist)1. add staff 2. delete staff b.backPlease enter staff id (b.back)Please enter staff password (b.back)Account must be limit 9 char.Please enter staff id (b.back)1. add staff 2. delete staff b.backSelect the data you want to edit(1.Movie 2.Session 3.Gift Card 4.Staff 5.Cancel Transaction 6.Screening b.exist)";
        String value = bytes.toString().trim();
        value = value.replaceAll("\r|\n", "");
        assertEquals(result, value);
    }
    //   stuff id less than 9 char
    @Test
    public void addStaff06(){
        String s = "4\r\n1\r\n222333\r\n123456\r\nb\r\nb\r\nb";
        ByteArrayInputStream strIn = new ByteArrayInputStream(s.getBytes());
        System.setIn(strIn);
        cs.stuffRun();
        String result = "Select the data you want to edit(1.Movie 2.Session 3.Gift Card 4.Staff 5.Cancel Transaction 6.Screening b.exist)1. add staff 2. delete staff b.backPlease enter staff id (b.back)Please enter staff password (b.back)Account must be limit 9 char.Please enter staff id (b.back)1. add staff 2. delete staff b.backSelect the data you want to edit(1.Movie 2.Session 3.Gift Card 4.Staff 5.Cancel Transaction 6.Screening b.exist)";
        String value = bytes.toString().trim();
        value = value.replaceAll("\r|\n", "");
        assertEquals(result, value);
    }
    //   stuff password more than 50 char
    @Test
    public void addStaff07(){
        String s = "4\r\n1\r\n222333444\r\n012345678901234567890123456789012345678901234567891\r\nb\r\nb\r\nb";
        ByteArrayInputStream strIn = new ByteArrayInputStream(s.getBytes());
        System.setIn(strIn);
        cs.stuffRun();
        String result = "Select the data you want to edit(1.Movie 2.Session 3.Gift Card 4.Staff 5.Cancel Transaction 6.Screening b.exist)1. add staff 2. delete staff b.backPlease enter staff id (b.back)Please enter staff password (b.back)Password less than 50.Please enter staff id (b.back)1. add staff 2. delete staff b.backSelect the data you want to edit(1.Movie 2.Session 3.Gift Card 4.Staff 5.Cancel Transaction 6.Screening b.exist)";
        String value = bytes.toString().trim();
        value = value.replaceAll("\r|\n", "");
        assertEquals(result, value);
    }   
    //   delete stuff Successful
    @Test
    public void deleteStaff00(){
        String s = "4\r\n2\r\n123456789\r\nb\r\nb\r\nb";
        ByteArrayInputStream strIn = new ByteArrayInputStream(s.getBytes());
        System.setIn(strIn);
        cs.stuffRun();
        String result = "Select the data you want to edit(1.Movie 2.Session 3.Gift Card 4.Staff 5.Cancel Transaction 6.Screening b.exist)1. add staff 2. delete staff b.backPlease enter staff id (b.back)Delete the  successfully!1. add staff 2. delete staff b.backSelect the data you want to edit(1.Movie 2.Session 3.Gift Card 4.Staff 5.Cancel Transaction 6.Screening b.exist)";
        String value = bytes.toString().trim();
        value = value.replaceAll("\r|\n", "");
        assertEquals(result, value);
    }  
    // stuff id null
    @Test
    public void deleteStaff01(){
        String s = "4\r\n2\r\n\r\nb\r\nb\r\nb";
        ByteArrayInputStream strIn = new ByteArrayInputStream(s.getBytes());
        System.setIn(strIn);
        cs.stuffRun();
        String result = "Select the data you want to edit(1.Movie 2.Session 3.Gift Card 4.Staff 5.Cancel Transaction 6.Screening b.exist)1. add staff 2. delete staff b.backPlease enter staff id (b.back)Account must be limit 9 char.Please enter staff id (b.back)1. add staff 2. delete staff b.backSelect the data you want to edit(1.Movie 2.Session 3.Gift Card 4.Staff 5.Cancel Transaction 6.Screening b.exist)";
        String value = bytes.toString().trim();
        value = value.replaceAll("\r|\n", "");
        assertEquals(result, value);
    } 
    // stuff id less than 9
    @Test
    public void deleteStaff02(){
        String s = "4\r\n2\r\n123\r\nb\r\nb\r\nb";
        ByteArrayInputStream strIn = new ByteArrayInputStream(s.getBytes());
        System.setIn(strIn);
        cs.stuffRun();
        String result = "Select the data you want to edit(1.Movie 2.Session 3.Gift Card 4.Staff 5.Cancel Transaction 6.Screening b.exist)1. add staff 2. delete staff b.backPlease enter staff id (b.back)Account must be limit 9 char.Please enter staff id (b.back)1. add staff 2. delete staff b.backSelect the data you want to edit(1.Movie 2.Session 3.Gift Card 4.Staff 5.Cancel Transaction 6.Screening b.exist)";
        String value = bytes.toString().trim();
        value = value.replaceAll("\r|\n", "");
        assertEquals(result, value);
    } 
    // wrong stuff id
    @Test
    public void deleteStaff03(){
        String s = "4\r\n2\r\n334455667\r\nb\r\nb\r\nb";
        ByteArrayInputStream strIn = new ByteArrayInputStream(s.getBytes());
        System.setIn(strIn);
        cs.stuffRun();
        String result = "Select the data you want to edit(1.Movie 2.Session 3.Gift Card 4.Staff 5.Cancel Transaction 6.Screening b.exist)1. add staff 2. delete staff b.backPlease enter staff id (b.back)The account does not exist.Please enter staff id (b.back)1. add staff 2. delete staff b.backSelect the data you want to edit(1.Movie 2.Session 3.Gift Card 4.Staff 5.Cancel Transaction 6.Screening b.exist)";
        String value = bytes.toString().trim();
        value = value.replaceAll("\r|\n", "");
        assertEquals(result, value);
    } 
    // back in deleteStaff
    @Test
    public void deleteStaff04(){
        String s = "4\r\n2\r\nb\r\nb\r\nb";
        ByteArrayInputStream strIn = new ByteArrayInputStream(s.getBytes());
        System.setIn(strIn);
        cs.stuffRun();
        String result = "Select the data you want to edit(1.Movie 2.Session 3.Gift Card 4.Staff 5.Cancel Transaction 6.Screening b.exist)1. add staff 2. delete staff b.backPlease enter staff id (b.back)1. add staff 2. delete staff b.backSelect the data you want to edit(1.Movie 2.Session 3.Gift Card 4.Staff 5.Cancel Transaction 6.Screening b.exist)";
        String value = bytes.toString().trim();
        value = value.replaceAll("\r|\n", "");
        assertEquals(result, value);
    } 
    // wrong number
    @Test
    public void Staff00(){
        String s = "4\r\n3\r\nb\r\nb";
        ByteArrayInputStream strIn = new ByteArrayInputStream(s.getBytes());
        System.setIn(strIn);
        cs.stuffRun();
        String result = "Select the data you want to edit(1.Movie 2.Session 3.Gift Card 4.Staff 5.Cancel Transaction 6.Screening b.exist)1. add staff 2. delete staff b.back1. add staff 2. delete staff b.backSelect the data you want to edit(1.Movie 2.Session 3.Gift Card 4.Staff 5.Cancel Transaction 6.Screening b.exist)";
        String value = bytes.toString().trim();
        value = value.replaceAll("\r|\n", "");
        assertEquals(result, value);
    } 
    // null
    @Test
    public void Staff01(){
        String s = "4\r\n\r\nb\r\nb";
        ByteArrayInputStream strIn = new ByteArrayInputStream(s.getBytes());
        System.setIn(strIn);
        cs.stuffRun();
        String result = "Select the data you want to edit(1.Movie 2.Session 3.Gift Card 4.Staff 5.Cancel Transaction 6.Screening b.exist)1. add staff 2. delete staff b.back1. add staff 2. delete staff b.backSelect the data you want to edit(1.Movie 2.Session 3.Gift Card 4.Staff 5.Cancel Transaction 6.Screening b.exist)";
        String value = bytes.toString().trim();
        value = value.replaceAll("\r|\n", "");
        assertEquals(result, value);
    } 
    // back in Staff
    @Test
    public void Staff02(){
        String s = "4\r\nb\r\nb";
        ByteArrayInputStream strIn = new ByteArrayInputStream(s.getBytes());
        System.setIn(strIn);
        cs.stuffRun();
        String result = "Select the data you want to edit(1.Movie 2.Session 3.Gift Card 4.Staff 5.Cancel Transaction 6.Screening b.exist)1. add staff 2. delete staff b.backSelect the data you want to edit(1.Movie 2.Session 3.Gift Card 4.Staff 5.Cancel Transaction 6.Screening b.exist)";
        String value = bytes.toString().trim();
        value = value.replaceAll("\r|\n", "");
        assertEquals(result, value);
    } 
    @Test
    public void CancelTransaction(){
        String s = "5\r\nb";
        ByteArrayInputStream strIn = new ByteArrayInputStream(s.getBytes());
        System.setIn(strIn);
        cs.stuffRun();
        String result = "Select the data you want to edit(1.Movie 2.Session 3.Gift Card 4.Staff 5.Cancel Transaction 6.Screening b.exist)Output cancel transaction report successfully!Select the data you want to edit(1.Movie 2.Session 3.Gift Card 4.Staff 5.Cancel Transaction 6.Screening b.exist)";
        String value = bytes.toString().trim();
        value = value.replaceAll("\r|\n", "");
        assertEquals(result, value);
    } 
    // add screening Successful
    @Test
    public void addScreening00(){
        String s = "6\r\n1\r\nSBS\r\n1\r\nGold\r\n20\r\n20\r\n20\r\nb\r\nb";
        ByteArrayInputStream strIn = new ByteArrayInputStream(s.getBytes());
        System.setIn(strIn);
        cs.stuffRun();
        String result = "Select the data you want to edit(1.Movie 2.Session 3.Gift Card 4.Staff 5.Cancel Transaction 6.Screening b.exist)1. add screening 2. delete screening 3. edit screening (b.back)Please enter cinema (b.back)Please enter screening id (b.back)Please enter screen size (b.back)Please enter front seat number (b.back)Please enter middle seat number (b.back)Please enter back seat number (b.back)Screening is added successfully1. add screening 2. delete screening 3. edit screening (b.back)Select the data you want to edit(1.Movie 2.Session 3.Gift Card 4.Staff 5.Cancel Transaction 6.Screening b.exist)";
        String value = bytes.toString().trim();
        value = value.replaceAll("\r|\n", "");
        assertEquals(result, value);
    }
    // front seat 
    @Test
    public void addScreening01(){
        String s = "6\r\n1\r\nSBS\r\n1\r\nGold\r\nabc\r\nb\r\nb\r\nb";
        ByteArrayInputStream strIn = new ByteArrayInputStream(s.getBytes());
        System.setIn(strIn);
        cs.stuffRun();
        String result = "Select the data you want to edit(1.Movie 2.Session 3.Gift Card 4.Staff 5.Cancel Transaction 6.Screening b.exist)1. add screening 2. delete screening 3. edit screening (b.back)Please enter cinema (b.back)Please enter screening id (b.back)Please enter screen size (b.back)Please enter front seat number (b.back)Please enter correct numberPlease enter cinema (b.back)1. add screening 2. delete screening 3. edit screening (b.back)Select the data you want to edit(1.Movie 2.Session 3.Gift Card 4.Staff 5.Cancel Transaction 6.Screening b.exist)";
        String value = bytes.toString().trim();
        value = value.replaceAll("\r|\n", "");
        assertEquals(result, value);
    } 
    // mid seat
    @Test
    public void addScreening02(){
        String s = "6\r\n1\r\nSBS\r\n1\r\nGold\r\n20\r\nabc\r\nb\r\nb\r\nb";
        ByteArrayInputStream strIn = new ByteArrayInputStream(s.getBytes());
        System.setIn(strIn);
        cs.stuffRun();
        String result = "Select the data you want to edit(1.Movie 2.Session 3.Gift Card 4.Staff 5.Cancel Transaction 6.Screening b.exist)1. add screening 2. delete screening 3. edit screening (b.back)Please enter cinema (b.back)Please enter screening id (b.back)Please enter screen size (b.back)Please enter front seat number (b.back)Please enter middle seat number (b.back)Please enter correct numberPlease enter cinema (b.back)1. add screening 2. delete screening 3. edit screening (b.back)Select the data you want to edit(1.Movie 2.Session 3.Gift Card 4.Staff 5.Cancel Transaction 6.Screening b.exist)";
        String value = bytes.toString().trim();
        value = value.replaceAll("\r|\n", "");
        assertEquals(result, value);
    } 
    // back seat
    @Test
    public void addScreening03(){
        String s = "6\r\n1\r\nSBS\r\n1\r\nGold\r\n20\r\n20\r\nabc\r\nb\r\nb\r\nb";
        ByteArrayInputStream strIn = new ByteArrayInputStream(s.getBytes());
        System.setIn(strIn);
        cs.stuffRun();
        String result = "Select the data you want to edit(1.Movie 2.Session 3.Gift Card 4.Staff 5.Cancel Transaction 6.Screening b.exist)1. add screening 2. delete screening 3. edit screening (b.back)Please enter cinema (b.back)Please enter screening id (b.back)Please enter screen size (b.back)Please enter front seat number (b.back)Please enter middle seat number (b.back)Please enter back seat number (b.back)Please enter correct numberPlease enter cinema (b.back)1. add screening 2. delete screening 3. edit screening (b.back)Select the data you want to edit(1.Movie 2.Session 3.Gift Card 4.Staff 5.Cancel Transaction 6.Screening b.exist)";
        String value = bytes.toString().trim();
        value = value.replaceAll("\r|\n", "");
        assertEquals(result, value);
    }
    // null front seat 
    @Test
    public void addScreening04(){
        String s = "6\r\n1\r\nSBS\r\n1\r\nGold\r\n\r\nb\r\nb\r\nb";
        ByteArrayInputStream strIn = new ByteArrayInputStream(s.getBytes());
        System.setIn(strIn);
        cs.stuffRun();
        String result = "Select the data you want to edit(1.Movie 2.Session 3.Gift Card 4.Staff 5.Cancel Transaction 6.Screening b.exist)1. add screening 2. delete screening 3. edit screening (b.back)Please enter cinema (b.back)Please enter screening id (b.back)Please enter screen size (b.back)Please enter front seat number (b.back)Please enter correct numberPlease enter cinema (b.back)1. add screening 2. delete screening 3. edit screening (b.back)Select the data you want to edit(1.Movie 2.Session 3.Gift Card 4.Staff 5.Cancel Transaction 6.Screening b.exist)";
        String value = bytes.toString().trim();
        value = value.replaceAll("\r|\n", "");
        assertEquals(result, value);
    } 
    // null mid seat
    @Test
    public void addScreening05(){
        String s = "6\r\n1\r\nSBS\r\n1\r\nGold\r\n20\r\n\r\nb\r\nb\r\nb";
        ByteArrayInputStream strIn = new ByteArrayInputStream(s.getBytes());
        System.setIn(strIn);
        cs.stuffRun();
        String result = "Select the data you want to edit(1.Movie 2.Session 3.Gift Card 4.Staff 5.Cancel Transaction 6.Screening b.exist)1. add screening 2. delete screening 3. edit screening (b.back)Please enter cinema (b.back)Please enter screening id (b.back)Please enter screen size (b.back)Please enter front seat number (b.back)Please enter middle seat number (b.back)Please enter correct numberPlease enter cinema (b.back)1. add screening 2. delete screening 3. edit screening (b.back)Select the data you want to edit(1.Movie 2.Session 3.Gift Card 4.Staff 5.Cancel Transaction 6.Screening b.exist)";
        String value = bytes.toString().trim();
        value = value.replaceAll("\r|\n", "");
        assertEquals(result, value);
    } 
    // null back seat
    @Test
    public void addScreening06(){
        String s = "6\r\n1\r\nSBS\r\n1\r\nGold\r\n20\r\n20\r\n\r\nb\r\nb\r\nb";
        ByteArrayInputStream strIn = new ByteArrayInputStream(s.getBytes());
        System.setIn(strIn);
        cs.stuffRun();
        String result = "Select the data you want to edit(1.Movie 2.Session 3.Gift Card 4.Staff 5.Cancel Transaction 6.Screening b.exist)1. add screening 2. delete screening 3. edit screening (b.back)Please enter cinema (b.back)Please enter screening id (b.back)Please enter screen size (b.back)Please enter front seat number (b.back)Please enter middle seat number (b.back)Please enter back seat number (b.back)Please enter correct numberPlease enter cinema (b.back)1. add screening 2. delete screening 3. edit screening (b.back)Select the data you want to edit(1.Movie 2.Session 3.Gift Card 4.Staff 5.Cancel Transaction 6.Screening b.exist)";
        String value = bytes.toString().trim();
        value = value.replaceAll("\r|\n", "");
        assertEquals(result, value);
    }
    // back in back seat
    @Test
    public void addScreening07(){
        String s = "6\r\n1\r\nSBS\r\n1\r\nGold\r\n20\r\n20\r\nb\r\nb\r\nb";
        ByteArrayInputStream strIn = new ByteArrayInputStream(s.getBytes());
        System.setIn(strIn);
        cs.stuffRun();
        String result = "Select the data you want to edit(1.Movie 2.Session 3.Gift Card 4.Staff 5.Cancel Transaction 6.Screening b.exist)1. add screening 2. delete screening 3. edit screening (b.back)Please enter cinema (b.back)Please enter screening id (b.back)Please enter screen size (b.back)Please enter front seat number (b.back)Please enter middle seat number (b.back)Please enter back seat number (b.back)1. add screening 2. delete screening 3. edit screening (b.back)Select the data you want to edit(1.Movie 2.Session 3.Gift Card 4.Staff 5.Cancel Transaction 6.Screening b.exist)";
        String value = bytes.toString().trim();
        value = value.replaceAll("\r|\n", "");
        assertEquals(result, value);
    }
    // Back in mid seat
    @Test
    public void addScreening08(){
        String s = "6\r\n1\r\nSBS\r\n1\r\nGold\r\n20\r\nb\r\nb\r\nb";
        ByteArrayInputStream strIn = new ByteArrayInputStream(s.getBytes());
        System.setIn(strIn);
        cs.stuffRun();
        String result = "Select the data you want to edit(1.Movie 2.Session 3.Gift Card 4.Staff 5.Cancel Transaction 6.Screening b.exist)1. add screening 2. delete screening 3. edit screening (b.back)Please enter cinema (b.back)Please enter screening id (b.back)Please enter screen size (b.back)Please enter front seat number (b.back)Please enter middle seat number (b.back)1. add screening 2. delete screening 3. edit screening (b.back)Select the data you want to edit(1.Movie 2.Session 3.Gift Card 4.Staff 5.Cancel Transaction 6.Screening b.exist)";
        String value = bytes.toString().trim();
        value = value.replaceAll("\r|\n", "");
        assertEquals(result, value);
    }
    // Back in back seat
    @Test
    public void addScreening09(){
        String s = "6\r\n1\r\nSBS\r\n1\r\nGold\r\nb\r\nb\r\nb";
        ByteArrayInputStream strIn = new ByteArrayInputStream(s.getBytes());
        System.setIn(strIn);
        cs.stuffRun();
        String result = "Select the data you want to edit(1.Movie 2.Session 3.Gift Card 4.Staff 5.Cancel Transaction 6.Screening b.exist)1. add screening 2. delete screening 3. edit screening (b.back)Please enter cinema (b.back)Please enter screening id (b.back)Please enter screen size (b.back)Please enter front seat number (b.back)1. add screening 2. delete screening 3. edit screening (b.back)Select the data you want to edit(1.Movie 2.Session 3.Gift Card 4.Staff 5.Cancel Transaction 6.Screening b.exist)";
        String value = bytes.toString().trim();
        value = value.replaceAll("\r|\n", "");
        assertEquals(result, value);
    }
    // already in SQL
    @Test
    public void addScreening10(){
        String s = "6\r\n1\r\nSBS\r\n3\r\nSilver\r\n30\r\n30\r\n30\r\nb\r\nb\r\nb";
        ByteArrayInputStream strIn = new ByteArrayInputStream(s.getBytes());
        System.setIn(strIn);
        cs.stuffRun();
        String result = "Select the data you want to edit(1.Movie 2.Session 3.Gift Card 4.Staff 5.Cancel Transaction 6.Screening b.exist)1. add screening 2. delete screening 3. edit screening (b.back)Please enter cinema (b.back)Please enter screening id (b.back)Please enter screen size (b.back)Please enter front seat number (b.back)Please enter middle seat number (b.back)Please enter back seat number (b.back)screen id is already exitPlease enter cinema (b.back)1. add screening 2. delete screening 3. edit screening (b.back)Select the data you want to edit(1.Movie 2.Session 3.Gift Card 4.Staff 5.Cancel Transaction 6.Screening b.exist)";
        String value = bytes.toString().trim();
        value = value.replaceAll("\r|\n", "");
        assertEquals(result, value);
    }
    // back in cinema
    @Test
    public void addScreening11(){
        String s = "6\r\n1\r\nb\r\nb\r\nb\r\nb";
        ByteArrayInputStream strIn = new ByteArrayInputStream(s.getBytes());
        System.setIn(strIn);
        cs.stuffRun();
        String result = "Select the data you want to edit(1.Movie 2.Session 3.Gift Card 4.Staff 5.Cancel Transaction 6.Screening b.exist)1. add screening 2. delete screening 3. edit screening (b.back)Please enter cinema (b.back)1. add screening 2. delete screening 3. edit screening (b.back)Select the data you want to edit(1.Movie 2.Session 3.Gift Card 4.Staff 5.Cancel Transaction 6.Screening b.exist)";
        String value = bytes.toString().trim();
        value = value.replaceAll("\r|\n", "");
        assertEquals(result, value);
    }
    // wrong screen size
    @Test
    public void addScreening12(){
        String s = "6\r\n1\r\nSBS\r\n1\r\nabc\r\n30\r\n30\r\n30\r\nb\r\nb\r\nb";
        ByteArrayInputStream strIn = new ByteArrayInputStream(s.getBytes());
        System.setIn(strIn);
        cs.stuffRun();
        String result = "Select the data you want to edit(1.Movie 2.Session 3.Gift Card 4.Staff 5.Cancel Transaction 6.Screening b.exist)1. add screening 2. delete screening 3. edit screening (b.back)Please enter cinema (b.back)Please enter screening id (b.back)Please enter screen size (b.back)Please enter front seat number (b.back)Please enter middle seat number (b.back)Please enter back seat number (b.back)screen type falsePlease enter cinema (b.back)1. add screening 2. delete screening 3. edit screening (b.back)Select the data you want to edit(1.Movie 2.Session 3.Gift Card 4.Staff 5.Cancel Transaction 6.Screening b.exist)";
        String value = bytes.toString().trim();
        value = value.replaceAll("\r|\n", "");
        assertEquals(result, value);
    }
    // back in screen id
    @Test
    public void addScreening13(){
        String s = "6\r\n1\r\nSBS\r\nb\r\nb\r\nb\r\nb";
        ByteArrayInputStream strIn = new ByteArrayInputStream(s.getBytes());
        System.setIn(strIn);
        cs.stuffRun();
        String result = "Select the data you want to edit(1.Movie 2.Session 3.Gift Card 4.Staff 5.Cancel Transaction 6.Screening b.exist)1. add screening 2. delete screening 3. edit screening (b.back)Please enter cinema (b.back)Please enter screening id (b.back)1. add screening 2. delete screening 3. edit screening (b.back)Select the data you want to edit(1.Movie 2.Session 3.Gift Card 4.Staff 5.Cancel Transaction 6.Screening b.exist)";
        String value = bytes.toString().trim();
        value = value.replaceAll("\r|\n", "");
        assertEquals(result, value);
    }
    // string in screen id
    @Test
    public void addScreening14(){
        String s = "6\r\n1\r\nSBS\r\nabc\r\nb\r\nb\r\nb";
        ByteArrayInputStream strIn = new ByteArrayInputStream(s.getBytes());
        System.setIn(strIn);
        cs.stuffRun();
        String result = "Select the data you want to edit(1.Movie 2.Session 3.Gift Card 4.Staff 5.Cancel Transaction 6.Screening b.exist)1. add screening 2. delete screening 3. edit screening (b.back)Please enter cinema (b.back)Please enter screening id (b.back)Please enter correct numberPlease enter cinema (b.back)1. add screening 2. delete screening 3. edit screening (b.back)Select the data you want to edit(1.Movie 2.Session 3.Gift Card 4.Staff 5.Cancel Transaction 6.Screening b.exist)";
        String value = bytes.toString().trim();
        value = value.replaceAll("\r|\n", "");
        assertEquals(result, value);
    }
    // back in screen size
    @Test
    public void addScreening15(){
        String s = "6\r\n1\r\nSBS\r\n1\r\nb\r\nb\r\nb\r\nb";
        ByteArrayInputStream strIn = new ByteArrayInputStream(s.getBytes());
        System.setIn(strIn);
        cs.stuffRun();
        String result = "Select the data you want to edit(1.Movie 2.Session 3.Gift Card 4.Staff 5.Cancel Transaction 6.Screening b.exist)1. add screening 2. delete screening 3. edit screening (b.back)Please enter cinema (b.back)Please enter screening id (b.back)Please enter screen size (b.back)1. add screening 2. delete screening 3. edit screening (b.back)Select the data you want to edit(1.Movie 2.Session 3.Gift Card 4.Staff 5.Cancel Transaction 6.Screening b.exist)";
        String value = bytes.toString().trim();
        value = value.replaceAll("\r|\n", "");
        assertEquals(result, value);
    }
    


    // delete Screening Successful
    @Test
    public void deleteScreening00(){
        String s = "6\r\n2\r\nSBS\r\n3\r\nb\r\nb\r\nb";
        ByteArrayInputStream strIn = new ByteArrayInputStream(s.getBytes());
        System.setIn(strIn);
        cs.stuffRun();
        String result = "Select the data you want to edit(1.Movie 2.Session 3.Gift Card 4.Staff 5.Cancel Transaction 6.Screening b.exist)1. add screening 2. delete screening 3. edit screening (b.back)Please enter cinema (b.back)Please enter screening id (b.back)Screening is deleted successfully1. add screening 2. delete screening 3. edit screening (b.back)Select the data you want to edit(1.Movie 2.Session 3.Gift Card 4.Staff 5.Cancel Transaction 6.Screening b.exist)";
        String value = bytes.toString().trim();
        value = value.replaceAll("\r|\n", "");
        assertEquals(result, value);
    }

    // wrong screening
    @Test
    public void deleteScreening01(){
        String s = "6\r\n2\r\nSBS\r\n2\r\nb\r\nb\r\nb";
        ByteArrayInputStream strIn = new ByteArrayInputStream(s.getBytes());
        System.setIn(strIn);
        cs.stuffRun();
        String result = "Select the data you want to edit(1.Movie 2.Session 3.Gift Card 4.Staff 5.Cancel Transaction 6.Screening b.exist)1. add screening 2. delete screening 3. edit screening (b.back)Please enter cinema (b.back)Please enter screening id (b.back)sorry no that screening nowPlease enter cinema (b.back)1. add screening 2. delete screening 3. edit screening (b.back)Select the data you want to edit(1.Movie 2.Session 3.Gift Card 4.Staff 5.Cancel Transaction 6.Screening b.exist)";
        String value = bytes.toString().trim();
        value = value.replaceAll("\r|\n", "");
        assertEquals(result, value);
    }
    // back in screening
    @Test
    public void deleteScreening02(){
        String s = "6\r\n2\r\nSBS\r\nb\r\nb\r\nb";
        ByteArrayInputStream strIn = new ByteArrayInputStream(s.getBytes());
        System.setIn(strIn);
        cs.stuffRun();
        String result = "Select the data you want to edit(1.Movie 2.Session 3.Gift Card 4.Staff 5.Cancel Transaction 6.Screening b.exist)1. add screening 2. delete screening 3. edit screening (b.back)Please enter cinema (b.back)Please enter screening id (b.back)1. add screening 2. delete screening 3. edit screening (b.back)Select the data you want to edit(1.Movie 2.Session 3.Gift Card 4.Staff 5.Cancel Transaction 6.Screening b.exist)";
        String value = bytes.toString().trim();
        value = value.replaceAll("\r|\n", "");
        assertEquals(result, value);
    }
    // back in cinema
    @Test
    public void deleteScreening03(){
        String s = "6\r\n2\r\nb\r\nb\r\nb";
        ByteArrayInputStream strIn = new ByteArrayInputStream(s.getBytes());
        System.setIn(strIn);
        cs.stuffRun();
        String result = "Select the data you want to edit(1.Movie 2.Session 3.Gift Card 4.Staff 5.Cancel Transaction 6.Screening b.exist)1. add screening 2. delete screening 3. edit screening (b.back)Please enter cinema (b.back)1. add screening 2. delete screening 3. edit screening (b.back)Select the data you want to edit(1.Movie 2.Session 3.Gift Card 4.Staff 5.Cancel Transaction 6.Screening b.exist)";
        String value = bytes.toString().trim();
        value = value.replaceAll("\r|\n", "");
        assertEquals(result, value);
    }
    // string in screen id
    @Test
    public void deleteScreening04(){
        String s = "6\r\n2\r\nSBS\r\nabc\r\nb\r\nb\r\nb";
        ByteArrayInputStream strIn = new ByteArrayInputStream(s.getBytes());
        System.setIn(strIn);
        cs.stuffRun();
        String result = "Select the data you want to edit(1.Movie 2.Session 3.Gift Card 4.Staff 5.Cancel Transaction 6.Screening b.exist)1. add screening 2. delete screening 3. edit screening (b.back)Please enter cinema (b.back)Please enter screening id (b.back)Please enter correct numberPlease enter cinema (b.back)1. add screening 2. delete screening 3. edit screening (b.back)Select the data you want to edit(1.Movie 2.Session 3.Gift Card 4.Staff 5.Cancel Transaction 6.Screening b.exist)";
        String value = bytes.toString().trim();
        value = value.replaceAll("\r|\n", "");
        assertEquals(result, value);
    }
    // edit Screening Successful
    @Test
    public void editScreening00(){
        String s = "6\r\n3\r\nSBS\r\n3\r\nGold\r\n20\r\n20\r\n20\r\nb\r\nb";
        ByteArrayInputStream strIn = new ByteArrayInputStream(s.getBytes());
        System.setIn(strIn);
        cs.stuffRun();
        String result = "Select the data you want to edit(1.Movie 2.Session 3.Gift Card 4.Staff 5.Cancel Transaction 6.Screening b.exist)1. add screening 2. delete screening 3. edit screening (b.back)Please enter cinema (b.back)Please enter screening id (b.back)Please enter screen size (b.back)Please enter front seat number (b.back)Please enter middle seat number (b.back)Please enter back seat number (b.back)Screening is edited successfully1. add screening 2. delete screening 3. edit screening (b.back)Select the data you want to edit(1.Movie 2.Session 3.Gift Card 4.Staff 5.Cancel Transaction 6.Screening b.exist)";
        String value = bytes.toString().trim();
        value = value.replaceAll("\r|\n", "");
        assertEquals(result, value);
    }
    // edit wrong screen size
    @Test
    public void editScreening01(){
        String s = "6\r\n3\r\nSBS\r\n3\r\nabc\r\n20\r\n20\r\n20\r\nb\r\nb\r\nb\r\nb";
        ByteArrayInputStream strIn = new ByteArrayInputStream(s.getBytes());
        System.setIn(strIn);
        cs.stuffRun();
        String result = "Select the data you want to edit(1.Movie 2.Session 3.Gift Card 4.Staff 5.Cancel Transaction 6.Screening b.exist)1. add screening 2. delete screening 3. edit screening (b.back)Please enter cinema (b.back)Please enter screening id (b.back)Please enter screen size (b.back)Please enter front seat number (b.back)Please enter middle seat number (b.back)Please enter back seat number (b.back)screen type falsePlease enter cinema (b.back)1. add screening 2. delete screening 3. edit screening (b.back)Select the data you want to edit(1.Movie 2.Session 3.Gift Card 4.Staff 5.Cancel Transaction 6.Screening b.exist)";
        String value = bytes.toString().trim();
        value = value.replaceAll("\r|\n", "");
        assertEquals(result, value);
    }
    // edit wrong screen id
    @Test
    public void editScreening02(){
        String s = "6\r\n3\r\nSBS\r\n1\r\nGold\r\n20\r\n20\r\n20\r\nb\r\nb\r\nb\r\nb";
        ByteArrayInputStream strIn = new ByteArrayInputStream(s.getBytes());
        System.setIn(strIn);
        cs.stuffRun();
        String result = "Select the data you want to edit(1.Movie 2.Session 3.Gift Card 4.Staff 5.Cancel Transaction 6.Screening b.exist)1. add screening 2. delete screening 3. edit screening (b.back)Please enter cinema (b.back)Please enter screening id (b.back)Please enter screen size (b.back)Please enter front seat number (b.back)Please enter middle seat number (b.back)Please enter back seat number (b.back)sorry no that screening nowPlease enter cinema (b.back)1. add screening 2. delete screening 3. edit screening (b.back)Select the data you want to edit(1.Movie 2.Session 3.Gift Card 4.Staff 5.Cancel Transaction 6.Screening b.exist)";
        String value = bytes.toString().trim();
        value = value.replaceAll("\r|\n", "");
        assertEquals(result, value);
    }
    // edit wrong cinima
    @Test
    public void editScreening03(){
        String s = "6\r\n3\r\nSB\r\n3\r\nGold\r\n20\r\n20\r\n20\r\nb\r\nb\r\nb\r\nb";
        ByteArrayInputStream strIn = new ByteArrayInputStream(s.getBytes());
        System.setIn(strIn);
        cs.stuffRun();
        String result = "Select the data you want to edit(1.Movie 2.Session 3.Gift Card 4.Staff 5.Cancel Transaction 6.Screening b.exist)1. add screening 2. delete screening 3. edit screening (b.back)Please enter cinema (b.back)Please enter screening id (b.back)Please enter screen size (b.back)Please enter front seat number (b.back)Please enter middle seat number (b.back)Please enter back seat number (b.back)sorry no that screening nowPlease enter cinema (b.back)1. add screening 2. delete screening 3. edit screening (b.back)Select the data you want to edit(1.Movie 2.Session 3.Gift Card 4.Staff 5.Cancel Transaction 6.Screening b.exist)";
        String value = bytes.toString().trim();
        value = value.replaceAll("\r|\n", "");
        assertEquals(result, value);
    }
    // edit wrong font seat type
    @Test
    public void editScreening04(){
        String s = "6\r\n3\r\nSBS\r\n3\r\nGold\r\nabc\r\nb\r\nb\r\nb";
        ByteArrayInputStream strIn = new ByteArrayInputStream(s.getBytes());
        System.setIn(strIn);
        cs.stuffRun();
        String result = "Select the data you want to edit(1.Movie 2.Session 3.Gift Card 4.Staff 5.Cancel Transaction 6.Screening b.exist)1. add screening 2. delete screening 3. edit screening (b.back)Please enter cinema (b.back)Please enter screening id (b.back)Please enter screen size (b.back)Please enter front seat number (b.back)Please enter correct numberPlease enter cinema (b.back)1. add screening 2. delete screening 3. edit screening (b.back)Select the data you want to edit(1.Movie 2.Session 3.Gift Card 4.Staff 5.Cancel Transaction 6.Screening b.exist)";
        String value = bytes.toString().trim();
        value = value.replaceAll("\r|\n", "");
        assertEquals(result, value);
    }
    // edit wrong mid seat type
    @Test
    public void editScreening05(){
        String s = "6\r\n3\r\nSBS\r\n3\r\nGold\r\n20\r\nabc\r\nb\r\nb\r\nb";
        ByteArrayInputStream strIn = new ByteArrayInputStream(s.getBytes());
        System.setIn(strIn);
        cs.stuffRun();
        String result = "Select the data you want to edit(1.Movie 2.Session 3.Gift Card 4.Staff 5.Cancel Transaction 6.Screening b.exist)1. add screening 2. delete screening 3. edit screening (b.back)Please enter cinema (b.back)Please enter screening id (b.back)Please enter screen size (b.back)Please enter front seat number (b.back)Please enter middle seat number (b.back)Please enter correct numberPlease enter cinema (b.back)1. add screening 2. delete screening 3. edit screening (b.back)Select the data you want to edit(1.Movie 2.Session 3.Gift Card 4.Staff 5.Cancel Transaction 6.Screening b.exist)";
        String value = bytes.toString().trim();
        value = value.replaceAll("\r|\n", "");
        assertEquals(result, value);
    }
    // edit wrong back seat type
    @Test
    public void editScreening06(){
        String s = "6\r\n3\r\nSBS\r\n3\r\nGold\r\n20\r\n20\r\nabc\r\nb\r\nb\r\nb";
        ByteArrayInputStream strIn = new ByteArrayInputStream(s.getBytes());
        System.setIn(strIn);
        cs.stuffRun();
        String result = "Select the data you want to edit(1.Movie 2.Session 3.Gift Card 4.Staff 5.Cancel Transaction 6.Screening b.exist)1. add screening 2. delete screening 3. edit screening (b.back)Please enter cinema (b.back)Please enter screening id (b.back)Please enter screen size (b.back)Please enter front seat number (b.back)Please enter middle seat number (b.back)Please enter back seat number (b.back)Please enter correct numberPlease enter cinema (b.back)1. add screening 2. delete screening 3. edit screening (b.back)Select the data you want to edit(1.Movie 2.Session 3.Gift Card 4.Staff 5.Cancel Transaction 6.Screening b.exist)";
        String value = bytes.toString().trim();
        value = value.replaceAll("\r|\n", "");
        assertEquals(result, value);
    }
    // back from back seat
    @Test
    public void editScreening07(){
        String s = "6\r\n3\r\nSBS\r\n3\r\nGold\r\n20\r\n20\r\nb\r\nb\r\nb";
        ByteArrayInputStream strIn = new ByteArrayInputStream(s.getBytes());
        System.setIn(strIn);
        cs.stuffRun();
        String result = "Select the data you want to edit(1.Movie 2.Session 3.Gift Card 4.Staff 5.Cancel Transaction 6.Screening b.exist)1. add screening 2. delete screening 3. edit screening (b.back)Please enter cinema (b.back)Please enter screening id (b.back)Please enter screen size (b.back)Please enter front seat number (b.back)Please enter middle seat number (b.back)Please enter back seat number (b.back)1. add screening 2. delete screening 3. edit screening (b.back)Select the data you want to edit(1.Movie 2.Session 3.Gift Card 4.Staff 5.Cancel Transaction 6.Screening b.exist)";
        String value = bytes.toString().trim();
        value = value.replaceAll("\r|\n", "");
        assertEquals(result, value);
    }
    // back from mid seat
    @Test
    public void editScreening08(){
        String s = "6\r\n3\r\nSBS\r\n3\r\nGold\r\n20\r\nb\r\nb\r\nb\r\nb";
        ByteArrayInputStream strIn = new ByteArrayInputStream(s.getBytes());
        System.setIn(strIn);
        cs.stuffRun();
        String result = "Select the data you want to edit(1.Movie 2.Session 3.Gift Card 4.Staff 5.Cancel Transaction 6.Screening b.exist)1. add screening 2. delete screening 3. edit screening (b.back)Please enter cinema (b.back)Please enter screening id (b.back)Please enter screen size (b.back)Please enter front seat number (b.back)Please enter middle seat number (b.back)1. add screening 2. delete screening 3. edit screening (b.back)Select the data you want to edit(1.Movie 2.Session 3.Gift Card 4.Staff 5.Cancel Transaction 6.Screening b.exist)";
        String value = bytes.toString().trim();
        value = value.replaceAll("\r|\n", "");
        assertEquals(result, value);
    }
    // back from front seat
    @Test
    public void editScreening09(){
        String s = "6\r\n3\r\nSBS\r\n3\r\nGold\r\nb\r\nb\r\nb\r\nb";
        ByteArrayInputStream strIn = new ByteArrayInputStream(s.getBytes());
        System.setIn(strIn);
        cs.stuffRun();
        String result = "Select the data you want to edit(1.Movie 2.Session 3.Gift Card 4.Staff 5.Cancel Transaction 6.Screening b.exist)1. add screening 2. delete screening 3. edit screening (b.back)Please enter cinema (b.back)Please enter screening id (b.back)Please enter screen size (b.back)Please enter front seat number (b.back)1. add screening 2. delete screening 3. edit screening (b.back)Select the data you want to edit(1.Movie 2.Session 3.Gift Card 4.Staff 5.Cancel Transaction 6.Screening b.exist)";
        String value = bytes.toString().trim();
        value = value.replaceAll("\r|\n", "");
        assertEquals(result, value);
    }
    // back from screen size
    @Test
    public void editScreening10(){
        String s = "6\r\n3\r\nSBS\r\n3\r\nb\r\nb\r\nb\r\nb";
        ByteArrayInputStream strIn = new ByteArrayInputStream(s.getBytes());
        System.setIn(strIn);
        cs.stuffRun();
        String result = "Select the data you want to edit(1.Movie 2.Session 3.Gift Card 4.Staff 5.Cancel Transaction 6.Screening b.exist)1. add screening 2. delete screening 3. edit screening (b.back)Please enter cinema (b.back)Please enter screening id (b.back)Please enter screen size (b.back)1. add screening 2. delete screening 3. edit screening (b.back)Select the data you want to edit(1.Movie 2.Session 3.Gift Card 4.Staff 5.Cancel Transaction 6.Screening b.exist)";
        String value = bytes.toString().trim();
        value = value.replaceAll("\r|\n", "");
        assertEquals(result, value);
    }
    // back from screen id
    @Test
    public void editScreening11(){
        String s = "6\r\n3\r\nSBS\r\nb\r\nb\r\nb\r\nb";
        ByteArrayInputStream strIn = new ByteArrayInputStream(s.getBytes());
        System.setIn(strIn);
        cs.stuffRun();
        String result = "Select the data you want to edit(1.Movie 2.Session 3.Gift Card 4.Staff 5.Cancel Transaction 6.Screening b.exist)1. add screening 2. delete screening 3. edit screening (b.back)Please enter cinema (b.back)Please enter screening id (b.back)1. add screening 2. delete screening 3. edit screening (b.back)Select the data you want to edit(1.Movie 2.Session 3.Gift Card 4.Staff 5.Cancel Transaction 6.Screening b.exist)";
        String value = bytes.toString().trim();
        value = value.replaceAll("\r|\n", "");
        assertEquals(result, value);
    }
    // back from cinema
    @Test
    public void editScreening12(){
        String s = "6\r\n3\r\nb\r\nb\r\nb\r\nb";
        ByteArrayInputStream strIn = new ByteArrayInputStream(s.getBytes());
        System.setIn(strIn);
        cs.stuffRun();
        String result = "Select the data you want to edit(1.Movie 2.Session 3.Gift Card 4.Staff 5.Cancel Transaction 6.Screening b.exist)1. add screening 2. delete screening 3. edit screening (b.back)Please enter cinema (b.back)1. add screening 2. delete screening 3. edit screening (b.back)Select the data you want to edit(1.Movie 2.Session 3.Gift Card 4.Staff 5.Cancel Transaction 6.Screening b.exist)";
        String value = bytes.toString().trim();
        value = value.replaceAll("\r|\n", "");
        assertEquals(result, value);
    }
    // string in screen id
    @Test
    public void editScreening13(){
        String s = "6\r\n3\r\nSBS\r\nabc\r\nb\r\nb\r\nb\r\nb";
        ByteArrayInputStream strIn = new ByteArrayInputStream(s.getBytes());
        System.setIn(strIn);
        cs.stuffRun();
        String result = "Select the data you want to edit(1.Movie 2.Session 3.Gift Card 4.Staff 5.Cancel Transaction 6.Screening b.exist)1. add screening 2. delete screening 3. edit screening (b.back)Please enter cinema (b.back)Please enter screening id (b.back)Please enter correct numberPlease enter cinema (b.back)1. add screening 2. delete screening 3. edit screening (b.back)Select the data you want to edit(1.Movie 2.Session 3.Gift Card 4.Staff 5.Cancel Transaction 6.Screening b.exist)";
        String value = bytes.toString().trim();
        value = value.replaceAll("\r|\n", "");
        assertEquals(result, value);
    }
    @Test
    public void Screening00(){
        String s = "6\r\nb\r\nb\r\nb";
        ByteArrayInputStream strIn = new ByteArrayInputStream(s.getBytes());
        System.setIn(strIn);
        cs.stuffRun();
        String result = "Select the data you want to edit(1.Movie 2.Session 3.Gift Card 4.Staff 5.Cancel Transaction 6.Screening b.exist)1. add screening 2. delete screening 3. edit screening (b.back)Select the data you want to edit(1.Movie 2.Session 3.Gift Card 4.Staff 5.Cancel Transaction 6.Screening b.exist)";
        String value = bytes.toString().trim();
        value = value.replaceAll("\r|\n", "");
        assertEquals(result, value);
    }
    @Test
    public void Screening01(){
        String s = "6\r\n8\r\nb\r\nb";
        ByteArrayInputStream strIn = new ByteArrayInputStream(s.getBytes());
        System.setIn(strIn);
        cs.stuffRun();
        String result = "Select the data you want to edit(1.Movie 2.Session 3.Gift Card 4.Staff 5.Cancel Transaction 6.Screening b.exist)1. add screening 2. delete screening 3. edit screening (b.back)Please enter correct number1. add screening 2. delete screening 3. edit screening (b.back)Select the data you want to edit(1.Movie 2.Session 3.Gift Card 4.Staff 5.Cancel Transaction 6.Screening b.exist)";
        String value = bytes.toString().trim();
        value = value.replaceAll("\r|\n", "");
        assertEquals(result, value);
    }
    @Test
    public void Screening02(){
        String s = "8\r\nb\r\nb";
        ByteArrayInputStream strIn = new ByteArrayInputStream(s.getBytes());
        System.setIn(strIn);
        cs.stuffRun();
        String result = "Select the data you want to edit(1.Movie 2.Session 3.Gift Card 4.Staff 5.Cancel Transaction 6.Screening b.exist)Please enter correct numberSelect the data you want to edit(1.Movie 2.Session 3.Gift Card 4.Staff 5.Cancel Transaction 6.Screening b.exist)";
        String value = bytes.toString().trim();
        value = value.replaceAll("\r|\n", "");
        assertEquals(result, value);
    }

    
}