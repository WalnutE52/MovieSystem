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
import java.util.Calendar;
import java.util.Date;
public class systemTest {

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
            c = DriverManager.getConnection("jdbc:sqlite:src/main/resources/" +
                    "test.db");
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
                    "VALUES ('123456789','1234','1234567890123456');PRAGMA " +
                    "foreign_keys = ON;";
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
                    "VALUES ('0987654321098765GC','2');PRAGMA foreign_keys = " +
                    "ON;";
            stmt.executeUpdate(giftcard2);
            String ticket1 = String.format("INSERT INTO Ticket (user_id," +
                            "updating_time," +
                            "movie_name,screen_id,screen_cinema,type,seat) " +
                            "VALUES ('123456789','%s', 'Holiday', " +
                            "2, " +
                            "'disney','Child','Middle');PRAGMA foreign_keys = ON;",
                    strDate);
            stmt.executeUpdate(ticket1);
//            cs = new CinemaSystem("test.db", false, 100.0, 0.5, 0.8,1.0, 0.9, 2.0,1.5, 1.0);

        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            JSONParser jsonParser = new JSONParser();
            JSONObject config = null;
            try {
                config = (JSONObject) jsonParser.parse(new FileReader(
                        "cinemaSystem.json"));
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
                            percent_adult, percent_senior, percent_gold,percent_silver,percent_bronze);
//                    cs.test_mode = true;
//                    cs.stuffRun();
//                    cs.login = true;
                    cs.user_id = "123456789";

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
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Test
    public void runTest(){
        String s = "b";
        ByteArrayInputStream strIn = new ByteArrayInputStream(s.getBytes());
        System.setIn(strIn);
        cs.customerRun();
        String result = "Please select the service you need (1.book Movie 2.register 3.login b.exit)";
        String value = bytes.toString().trim();
        value = value.replaceAll("\r|\n", "");
        assertEquals(result, value);

    }
    @Test
    public void checkMovieFilterTheScreenTest(){
//        cs.login = true;
        cal = Calendar.getInstance();
        cal.add(Calendar.SECOND,1);
        Date date = cal.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strDate = dateFormat.format(date);
        String s = "1\r\nHoliday\r\n1\r\nGold\r\nb\r\nb\r\nb\r\nb";
        ByteArrayInputStream strIn = new ByteArrayInputStream(s.getBytes());
        System.setIn(strIn);
        cs.customerRun();
        String result = String.format("Please select the service you need (1.book " +
                        "Movie 2.register 3.login b.exit) Movie_name: " +
                        "HolidayClassification: GeneralSynopsis: Family " +
                        "ComedyRelease_date: 2021-10-23 20:00:00Director: " +
                        "AliceCasts: Suzana, PeterMovie_name: The Penthouse: War in Life - Season 4Classification: Mature AccompaniesSynopsis: Dramatically UnbelievableRelease_date: 2021-10-23 22:30:00Director: Kim Soon-okCasts: Cheon seo-jin, Oh Yoon-heePlease enter movie name " +
                        "you want to book (b.back)Updating_time: %sMovie_name: " +
                        "HolidayProjection_hall: 2Cinema: " +
                        "disneyFront_seat_left: 20Middle_seat_left: 20Back_seat_left: 20Updating_time: %sMovie_name: HolidayProjection_hall: 3Cinema: SBSFront_seat_left: 30Middle_seat_left: 30Back_seat_left: 30Updating_time: %sMovie_name: HolidayProjection_hall: 4Cinema: SBSFront_seat_left: 40Middle_seat_left: 40Back_seat_left: 401.filter by screen 2.filter by cinema (b.stop filter)Gold, Silver, " +
                        "Bronze (b.back)Updating_time: %sM" +
                        "ovie_name: HolidayProjection_hall: 2Cinema: disneyFront_seat_left: 20Middle_seat_left: 20Back_seat_left: 201.filter by screen 2.filter by cinema (b.stop filter)Please login or register(1.login, 2.register, b.back)Please enter movie name you want to book (b.back)Please select the s" +
                        "ervice you need (1.book Movie 2.register 3.login b.exit)",
                strDate,strDate,strDate,strDate);
        String value = bytes.toString().trim();
        value = value.replaceAll("\r|\n", "");
        assertEquals(result, value);
    }
    @Test
    public void checkMovieFilterTheCinemaTest(){
//        cs.login = true;
        cal = Calendar.getInstance();
        cal.add(Calendar.SECOND,1);
        Date date = cal.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strDate = dateFormat.format(date);
        String s = "1\r\nHoliday\r\n2\r\nSBS\r\nb\r\nb\r\nb\r\nb";
        ByteArrayInputStream strIn = new ByteArrayInputStream(s.getBytes());
        System.setIn(strIn);
        cs.customerRun();
        String result = String.format("Please select the service you need (1" +
                        ".book Movie 2.register 3.login b.exit) Movie_name: " +
                        "HolidayClassification: GeneralSynopsis: Family " +
                        "ComedyRelease_date: 2021-10-23 20:00:00Director: " +
                        "AliceCasts: Suzana, PeterMovie_name: The Penthouse: " +
                        "War in Life - Season 4Classification: Mature " +
                        "AccompaniesSynopsis: Dramatically " +
                        "UnbelievableRelease_date: 2021-10-23 " +
                        "22:30:00Director: Kim Soon-okCasts: Cheon seo-jin, " +
                        "Oh Yoon-heePlease enter movie name you want to book " +
                        "(b.back)Updating_time: %sMovie_name: HolidayProjection_hall: 2Cinema: disneyFront_seat_left: 20Middle_seat_left: 20Back_seat_left: 20Updating_time: %sMovie_name: HolidayProjection_hall: 3Cinema: SBSFront_seat_left: 30Middle_seat_left: 30Back_seat_left: 30Updating_time: %sMovie_name: HolidayProjection_hall: 4Cinema: SBSFront_seat_left: 40Middle_seat_left: 40Back_seat_left: 401.filter by screen 2.filter by cinema (b.stop filter)disney, SBS (b.back)Updating_time: %sMovie_name: HolidayProjection_hall: 3Cinema: SBSFront_seat_left: 30Middle_seat_left: 30Back_seat_left: 30Updating_time: %sMovie_name: HolidayProjection_hall: 4Cinema: SBSFront_seat_left: 40Middle_seat_left: 40Back_seat_left: 401.filter by screen 2.filter by cinema (b.stop filter)Please login or register(1.login, 2.register, b.back)Please enter movie name you want to book (b.back)Please select the service you need (1.book Movie 2.register 3.login b.exit)",
                strDate,strDate,strDate,strDate,strDate);
        String value = bytes.toString().trim();
        value = value.replaceAll("\r|\n", "");
        assertEquals(result, value);
    }
    @Test
    public void getTicketByGiftcardTest(){
        cs.login = true;
        cal = Calendar.getInstance();
        cal.add(Calendar.SECOND,1);
        Date date = cal.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strDate = dateFormat.format(date);
        String s = String.format("1\r\nHoliday\r\nb\r\nSBS\r\n3\r\n%s\r\n1\r" +
                        "\nChild\r" +
                        "\nFront\r\n0987654321098765GC\r\n1\r\n1\r\nb\r\nb\r\nb",
                strDate);
        ByteArrayInputStream strIn = new ByteArrayInputStream(s.getBytes());
        System.setIn(strIn);
        cs.customerRun();
        String result = String.format("Please select the service you need (1" +
                        ".book Movie 2.logout b.exit)Movie_name: " +
                        "HolidayClassification: GeneralSynopsis: Family " +
                        "ComedyRelease_date: 2021-10-23 20:00:00Director: " +
                        "AliceCasts: Suzana, PeterMovie_name: The Penthouse: " +
                        "War in Life - Season 4Classification: Mature " +
                        "AccompaniesSynopsis: Dramatically UnbelievableRelease_date: 2021-10-23 22:30:00Director: Kim Soon-okCasts: Cheon seo-jin, Oh Yoon-heePlease enter movie name you want to book (b.back)Updating_time: %sMovie_name: HolidayProjection_hall: 2Cinema: disneyFront_seat_left: 20Middle_seat_left: 20Back_seat_left: 20Updating_time: %sMovie_name: HolidayProjection_hall: 3Cinema: SBSFront_seat_left: 30Middle_seat_left: 30Back_seat_left: 30Updating_time: %sMovie_name: HolidayProjection_hall: 4Cinema: SBSFront_seat_left: 40Middle_seat_left: 40Back_seat_left: 401.filter by screen 2.filter by cinema (b.stop filter)Please enter cinemaPlease enter screening idPlease enter updating time (b.back)yyyy-MM-dd HH:mm:ssDo you have gift card ?(1.yes 2.no b.back)Please enter type (b.back)type: Child, Student, Adult, SeniorPlease enter seat (b.back)seat: Front, Middle, BackPlease enter your gift card number (b.back)Are you confirm this transaction(1.yes 2.no)you only have two minutes to confirmGiftcard pay successfullyDo you want to continue buy ticket with same session or choose another one ?(1.continue 2.another movie)Do you have gift card ?(1.yes 2.no b.back)Please enter movie name you want to book (b.back)Please select the service you need (1.book Movie 2.logout b.exit)",
                strDate,strDate,strDate);
        String value = bytes.toString().trim();
        value = value.replaceAll("\r|\n", "");
        assertEquals(result, value);
    }
    @Test
    public void getTicketByCardSuccessTest(){
        cs.login = true;
//        cs.user_id = "222222222";
        cal = Calendar.getInstance();
        cal.add(Calendar.SECOND,1);
        Date date = cal.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strDate = dateFormat.format(date);
        String s = String.format("1\r\nHoliday\r\nb\r\nSBS\r\n3\r\n%s\r\n2\r" +
                        "\nSenior\r\nFront\r\n50\r\nSenior\r\nBack\r\n2\r\n1" +
                        "\r\n1" +
                        "\r\n1\r\nb\r\nb" +
                        "\r\nb",
                strDate);
        ByteArrayInputStream strIn = new ByteArrayInputStream(s.getBytes());
        System.setIn(strIn);
        cs.customerRun();
        String result = String.format("Please select the service you need (1.book Movie 2.logout b.exit)" +
                        "Movie_name: HolidayClassification: GeneralSynopsis: Family ComedyRelease_date: 2021-10-23 20:00:00Director: AliceCasts: Suzana, Peter" +
                        "Movie_name: The Penthouse: War in Life - Season 4Classification: Mature AccompaniesSynopsis: Dramatically UnbelievableRelease_date: 2021-10-23 22:30:00Director: Kim Soon-okCasts: Cheon seo-jin, Oh Yoon-hee" +
                        "Please enter movie name you want to book (b.back)" +
                        "Updating_time: %sMovie_name: HolidayProjection_hall:" +
                        " 2Cinema: disneyFront_seat_left: 20Middle_seat_left: 20Back_seat_left: 20" +
                        "Updating_time: %sMovie_name: HolidayProjection_hall:" +
                        " 3Cinema: SBSFront_seat_left: 30Middle_seat_left: 30Back_seat_left: 30" +
                        "Updating_time: %sMovie_name: " +
                        "HolidayProjection_hall: 4Cinema: SBSFront_seat_left: 40Middle_seat_left: 40Back_seat_left: 401.filter by screen 2.filter by cinema (b.stop filter)Please enter cinema" +
                        "Please enter screening idPlease enter updating time (b.back)yyyy-MM-dd HH:mm:ssDo you have gift card ?(1.yes 2.no b.back)Please enter ticket type (b.back)type: Child, Student, Adult, SeniorPlease enter seat (b.back)seat: Front, Middle, BackHow many tickets do you want to buy? (b.back)" +
                        "This session only has 30 Front seatsPlease enter " +
                        "ticket type (b.back)type: Child, Student, Adult, SeniorPlease enter seat (b.back)seat: Front, Middle, BackHow many tickets do you want to buy? (b.back)Are you confirm this transaction(1.yes 2.no)you only have two minutes to confirmYou should pay 270.0 dollarDo you want to use the card you used last time(1.yes 2.no)Card pay successfullyDo you want to continue buy ticket with same session or choose another one ?(1.continue 2.another movie)Do you have gift card ?(1.yes 2.no b.back)Please enter movie name you want to book (b.back)Please select the service you need (1.book Movie 2.logout b.exit)",
                strDate,strDate,strDate,strDate);
        String value = bytes.toString().trim();
        value = value.replaceAll("\r|\n", "");
        assertEquals(result, value);

    }
    @Test
    public void getTicketByCardCanceledTest(){
        cs.login = true;
        cs.user_id = "222222222";
        cal = Calendar.getInstance();
        cal.add(Calendar.SECOND,1);
        Date date = cal.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strDate = dateFormat.format(date);
        String s = String.format("1\r\nHoliday\r\nSBS\r\nb\r\nSBS\r\n2\r\n%s" +
                        "\r" +
                        "\nSBS\r" +
                        "\n3\r\n%s\r\n2\r\nChild\r\nFront\r\n1\r\n1\r\nb\r\nb" +
                        "\r\nb",
                strDate,strDate);
        ByteArrayInputStream strIn = new ByteArrayInputStream(s.getBytes());
        System.setIn(strIn);
        cs.customerRun();
        String result = String.format("Please select the service you need (1" +
                        ".book Movie 2.logout b.exit)Movie_name: " +
                        "HolidayClassification: GeneralSynopsis: Family " +
                        "ComedyRelease_date: 2021-10-23 20:00:00Director: " +
                        "AliceCasts: Suzana, PeterMovie_name: The Penthouse: " +
                        "War in Life - Season 4Classification: Mature " +
                        "AccompaniesSynopsis: Dramatically UnbelievableRelease_date: 2021-10-23 22:30:00Director: Kim Soon-okCasts: Cheon seo-jin, Oh Yoon-heePlease enter movie name you want to book (b.back)Updating_time: %sMovie_name: HolidayProjection_hall: 2Cinema: disneyFront_seat_left: 20Middle_seat_left: 20Back_seat_left: 20Updating_time: %sMovie_name: HolidayProjection_hall: 3Cinema: SBSFront_seat_left: 30Middle_seat_left: 30Back_seat_left: 30Updating_time: %sMovie_name: HolidayProjection_hall: 4Cinema: SBSFront_seat_left: 40Middle_seat_left: 40Back_seat_left: 401.filter by screen 2.filter by cinema (b.stop filter)Please enter correct number1.filter by screen 2.filter by cinema (b.stop filter)Please enter cinemaPlease enter screening idPlease enter updating time (b.back)yyyy-MM-dd HH:mm:ssThis session is not existPlease enter cinemaPlease enter screening idPlease enter updating time (b.back)yyyy-MM-dd HH:mm:ssDo you have gift card ?(1.yes 2.no b.back)Please enter ticket type (b.back)type: Child, Student, Adult, SeniorPlease enter seat (b.back)seat: Front, Middle, BackHow many tickets do you want to buy? (b.back)Are you confirm this transaction(1.yes 2.no)you only have two minutes to confirmYou should pay 75.0 dollarPlease enter card holder(b.cancel transaction)Please enter movie name you want to book (b.back)Please select the service you need (1.book Movie 2.logout b.exit)",
                strDate,strDate,strDate);
        String value = bytes.toString().trim();
        value = value.replaceAll("\r|\n", "");
        assertEquals(result, value);

    }
    @Test
    public void wrongInputFilterTest(){
        cal = Calendar.getInstance();
        cal.add(Calendar.SECOND,1);
        Date date = cal.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strDate = dateFormat.format(date);
        String s = "5\r\n1\r\nPenthouse\r\nThe Penthouse: War in Life - " +
                "Season 4\r\n4\r\n1\r\nBlueScreen\r\nb\r\n2\r\nHBO\r\nb\r\nb" +
                "\r\nb\r\nb\r\nb\r\nb";
        ByteArrayInputStream strIn = new ByteArrayInputStream(s.getBytes());
        System.setIn(strIn);
        cs.customerRun();
        String result = String.format(
                "Please select the service you need (1.book " +
                        "Movie 2.register 3.login b.exit) " +
                        "Please enter correct numberPlease select the service you need (1.book Movie 2.register 3.login b.exit) " +
                        "Movie_name: HolidayClassification: GeneralSynopsis: Family ComedyRelease_date: 2021-10-23 20:00:00Director: AliceCasts: Suzana, PeterMovie_name: The Penthouse: War in Life - Season 4Classification: Mature AccompaniesSynopsis: Dramatically UnbelievableRelease_date: 2021-10-23 22:30:00Director: Kim Soon-okCasts: Cheon seo-jin, Oh Yoon-hee" +
                        "Please enter movie name you want to book (b.back)We do not " +
                        "have such movie in this weekPlease enter movie name " +
                        "you want to book (b.back)Updating_time: %sMovie_name: The Penthouse: War in Life - Season 4Projection_hall: 1Cinema: disneyFront_seat_left: 10Middle_seat_left: 10Back_seat_left: 101.filter by screen 2.filter by cinema (b.stop filter)" +
                        "Please enter correct number1.filter by screen 2.filter by cinema (b.stop filter)Gold, Silver, Bronze (b.back)screen type falseGold, Silver, Bronze (b.back)1.filter by screen 2.filter by cinema (b.stop filter)disney, SBS (b.back)This movie does not have such session with this cinemadisney, SBS (b.back)1.filter by screen 2.filter by cinema (b.stop filter)" +
                        "Please login or register(1.login, 2.register, b.back)Please " +
                        "enter movie name you want to book (b.back)Please " +
                        "select the service you need (1.book Movie 2.register" +
                        " 3.login b.exit)",strDate);
        String value = bytes.toString().trim();
        value = value.replaceAll("\r|\n", "");
        assertEquals(result, value);

    }
//     @Test
//     public void wrongInputLogoutTest(){
//         cs.login = true;
//         String s = "2\r\n123456789\r\n\r\nb\r\nb";
//         ByteArrayInputStream strIn = new ByteArrayInputStream(s.getBytes());
//         System.setIn(strIn);
//         cs.customerRun();
//         String result ="Please select the service you need (1.book Movie 2.logout b.exit)Please enter your user id (b.back)Please enter your password (b.back)Please enter correct passwordPlease enter your user id (b.back)*Please enter your password (b.back)Please enter correct passwordPlease enter your user id (b.back)Please select the service you need (1.book Movie 2.logout b.exit)";
//         String value = bytes.toString().trim();
//         value = value.replaceAll("\r|\n", "");
//         assertEquals(result, value);

//     }
    @Test
    public void wrongInputGetTicketByGiftCardTest(){
        cal = Calendar.getInstance();
        cal.add(Calendar.SECOND,1);
        Date date = cal.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strDate = dateFormat.format(date);
        cs.login = true;
        String s = String.format("1\r\nThe Penthouse: War in Life - Season " +
                        "4\r\n4\r\nb\r\ndisney\r\n1\r\n%s\r" +
                        "\n1\r" +
                        "\nAdult\r\nMiddle\r\n9987654321098765GC\r\n1\r\nb\r" +
                        "\nb\r" +
                        "\nb\r\nb",
                strDate);
        ByteArrayInputStream strIn = new ByteArrayInputStream(s.getBytes());
        System.setIn(strIn);
        cs.customerRun();
        String result =String.format("Please select the service you need (1" +
                        ".book Movie 2.logout b.exit)Movie_name: " +
                        "HolidayClassification: GeneralSynopsis: Family " +
                        "ComedyRelease_date: 2021-10-23 20:00:00Director: AliceCasts: Suzana, PeterMovie_name: The Penthouse: War in Life - Season 4Classification: Mature AccompaniesSynopsis: Dramatically UnbelievableRelease_date: 2021-10-23 22:30:00Director: Kim Soon-okCasts: Cheon seo-jin, Oh Yoon-heePlease enter movie name you want to book (b.back)Updating_time: %sMovie_name: The Penthouse: War in Life - Season 4Projection_hall: 1Cinema: disneyFront_seat_left: 10Middle_seat_left: 10Back_seat_left: 101.filter by screen 2.filter by cinema (b.stop filter)Please enter correct number1.filter by screen 2.filter by cinema (b.stop filter)Please enter cinemaPlease enter screening idPlease enter updating time (b.back)yyyy-MM-dd HH:mm:ssDo you have gift card ?(1.yes 2.no b.back)Please enter type (b.back)type: Child, Student, Adult, SeniorPlease enter seat (b.back)seat: Front, Middle, BackPlease enter your gift card number (b.back)Are you confirm this transaction(1.yes 2.no)you only have two minutes to confirmThis gift card does not existPlease enter type (b.back)type: Child, Student, Adult, SeniorDo you have gift card ?(1.yes 2.no b.back)Please enter movie name you want to book (b.back)Please select the service you need (1.book Movie 2.logout b.exit)",
                strDate);
        String value = bytes.toString().trim();
        value = value.replaceAll("\r|\n", "");
        assertEquals(result, value);

    }
    @Test
    public void seatTest10(){
        cs.login = true;
        cs.user_id = "222222222";
        cal = Calendar.getInstance();
        cal.add(Calendar.SECOND,1);
        Date date = cal.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strDate = dateFormat.format(date);
        String s = String.format("1\r\nHoliday\r\nSBS\r\nb\r\nSBS\r\n2\r\n%s" +
                        "\r" +
                        "\nSBS\r" +
                        "\n3\r\n%s\r\n2\r\nChild\r\nabc\r\nb\r\nb\r\nb\r\nb" +
                        "\r\nb",
                strDate,strDate);
        ByteArrayInputStream strIn = new ByteArrayInputStream(s.getBytes());
        System.setIn(strIn);
        cs.customerRun();
        String result = String.format("Please select the service you need (1.book Movie 2.logout b.exit)Movie_name: HolidayClassification: GeneralSynopsis: Family ComedyRelease_date: 2021-10-23 20:00:00Director: AliceCasts: Suzana, PeterMovie_name: The Penthouse: War in Life - Season 4Classification: Mature AccompaniesSynopsis: Dramatically UnbelievableRelease_date: 2021-10-23 22:30:00Director: Kim Soon-okCasts: Cheon seo-jin, Oh Yoon-heePlease enter movie name you want to book (b.back)Updating_time: %sMovie_name: HolidayProjection_hall: 2Cinema: disneyFront_seat_left: 20Middle_seat_left: 20Back_seat_left: 20Updating_time: %sMovie_name: HolidayProjection_hall: 3Cinema: SBSFront_seat_left: 30Middle_seat_left: 30Back_seat_left: 30Updating_time: %sMovie_name: HolidayProjection_hall: 4Cinema: SBSFront_seat_left: 40Middle_seat_left: 40Back_seat_left: 401.filter by screen 2.filter by cinema (b.stop filter)Please enter correct number1.filter by screen 2.filter by cinema (b.stop filter)Please enter cinemaPlease enter screening idPlease enter updating time (b.back)yyyy-MM-dd HH:mm:ssThis session is not existPlease enter cinemaPlease enter screening idPlease enter updating time (b.back)yyyy-MM-dd HH:mm:ssDo you have gift card ?(1.yes 2.no b.back)Please enter ticket type (b.back)type: Child, Student, Adult, SeniorPlease enter seat (b.back)seat: Front, Middle, BackPlease enter correct numberPlease enter ticket type (b.back)type: Child, Student, Adult, SeniorDo you have gift card ?(1.yes 2.no b.back)Please enter movie name you want to book (b.back)Please select the service you need (1.book Movie 2.logout b.exit)",strDate,strDate,strDate);
        String value = bytes.toString().trim();
        value = value.replaceAll("\r|\n", "");
        assertEquals(result, value);

    }
    @Test
    public void seatTest11(){
        cs.login = true;
        cs.user_id = "222222222";
        cal = Calendar.getInstance();
        cal.add(Calendar.SECOND,1);
        Date date = cal.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strDate = dateFormat.format(date);
        String s = String.format("1\r\nHoliday\r\nSBS\r\nb\r\nSBS\r\n2\r\n%s" +
                        "\r" +
                        "\nSBS\r" +
                        "\n3\r\n%s\r\n2\r\nChild\r\nb\r\nb\r\nb\r\nb\r\nb\r\nb",
                strDate,strDate);
        ByteArrayInputStream strIn = new ByteArrayInputStream(s.getBytes());
        System.setIn(strIn);
        cs.customerRun();
        String result = String.format("Please select the service you need (1.book Movie 2.logout b.exit)Movie_name: HolidayClassification: GeneralSynopsis: Family ComedyRelease_date: 2021-10-23 20:00:00Director: AliceCasts: Suzana, PeterMovie_name: The Penthouse: War in Life - Season 4Classification: Mature AccompaniesSynopsis: Dramatically UnbelievableRelease_date: 2021-10-23 22:30:00Director: Kim Soon-okCasts: Cheon seo-jin, Oh Yoon-heePlease enter movie name you want to book (b.back)Updating_time: %sMovie_name: HolidayProjection_hall: 2Cinema: disneyFront_seat_left: 20Middle_seat_left: 20Back_seat_left: 20Updating_time: %sMovie_name: HolidayProjection_hall: 3Cinema: SBSFront_seat_left: 30Middle_seat_left: 30Back_seat_left: 30Updating_time: %sMovie_name: HolidayProjection_hall: 4Cinema: SBSFront_seat_left: 40Middle_seat_left: 40Back_seat_left: 401.filter by screen 2.filter by cinema (b.stop filter)Please enter correct number1.filter by screen 2.filter by cinema (b.stop filter)Please enter cinemaPlease enter screening idPlease enter updating time (b.back)yyyy-MM-dd HH:mm:ssThis session is not existPlease enter cinemaPlease enter screening idPlease enter updating time (b.back)yyyy-MM-dd HH:mm:ssDo you have gift card ?(1.yes 2.no b.back)Please enter ticket type (b.back)type: Child, Student, Adult, SeniorPlease enter seat (b.back)seat: Front, Middle, BackPlease enter correct numberPlease enter ticket type (b.back)type: Child, Student, Adult, SeniorDo you have gift card ?(1.yes 2.no b.back)Please enter movie name you want to book (b.back)Please select the service you need (1.book Movie 2.logout b.exit)",strDate,strDate,strDate,strDate);
        String value = bytes.toString().trim();
        value = value.replaceAll("\r|\n", "");
        assertEquals(result, value);

    }
    @Test
    public void seatTest12(){
        cs.login = true;
        cs.user_id = "222222222";
        cal = Calendar.getInstance();
        cal.add(Calendar.SECOND,1);
        Date date = cal.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strDate = dateFormat.format(date);
        String s = String.format("1\r\nHoliday\r\nSBS\r\nb\r\nSBS\r\n2\r\n%s" +
                        "\r" +
                        "\nSBS\r" +
                        "\n3\r\n%s\r\n2\r\nChild\r\nFront\r\nb\r\nb\r\nb\r\nb\r\nb",
                strDate,strDate);
        ByteArrayInputStream strIn = new ByteArrayInputStream(s.getBytes());
        System.setIn(strIn);
        cs.customerRun();
        String result = String.format("Please select the service you need (1.book Movie 2.logout b.exit)Movie_name: HolidayClassification: GeneralSynopsis: Family ComedyRelease_date: 2021-10-23 20:00:00Director: AliceCasts: Suzana, PeterMovie_name: The Penthouse: War in Life - Season 4Classification: Mature AccompaniesSynopsis: Dramatically UnbelievableRelease_date: 2021-10-23 22:30:00Director: Kim Soon-okCasts: Cheon seo-jin, Oh Yoon-heePlease enter movie name you want to book (b.back)Updating_time: %sMovie_name: HolidayProjection_hall: 2Cinema: disneyFront_seat_left: 20Middle_seat_left: 20Back_seat_left: 20Updating_time: %sMovie_name: HolidayProjection_hall: 3Cinema: SBSFront_seat_left: 30Middle_seat_left: 30Back_seat_left: 30Updating_time: %sMovie_name: HolidayProjection_hall: 4Cinema: SBSFront_seat_left: 40Middle_seat_left: 40Back_seat_left: 401.filter by screen 2.filter by cinema (b.stop filter)Please enter correct number1.filter by screen 2.filter by cinema (b.stop filter)Please enter cinemaPlease enter screening idPlease enter updating time (b.back)yyyy-MM-dd HH:mm:ssThis session is not existPlease enter cinemaPlease enter screening idPlease enter updating time (b.back)yyyy-MM-dd HH:mm:ssDo you have gift card ?(1.yes 2.no b.back)Please enter ticket type (b.back)type: Child, Student, Adult, SeniorPlease enter seat (b.back)seat: Front, Middle, BackHow many tickets do you want to buy? (b.back)Do you have gift card ?(1.yes 2.no b.back)Please enter movie name you want to book (b.back)Please select the service you need (1.book Movie 2.logout b.exit)",strDate,strDate,strDate,strDate);
        String value = bytes.toString().trim();
        value = value.replaceAll("\r|\n", "");
        assertEquals(result, value);

    }
    @Test
    public void seatTest20(){
        cs.login = true;
        cs.user_id = "222222222";
        cal = Calendar.getInstance();
        cal.add(Calendar.SECOND,1);
        Date date = cal.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strDate = dateFormat.format(date);
        String s = String.format("1\r\nHoliday\r\nSBS\r\nb\r\nSBS\r\n2\r\n%s" +
                        "\r" +
                        "\nSBS\r" +
                        "\n3\r\n%s\r\n1\r\nChild\r\nabc\r\nb\r\nb\r\nb\r\nb" +
                        "\r\nb",
                strDate,strDate);
        ByteArrayInputStream strIn = new ByteArrayInputStream(s.getBytes());
        System.setIn(strIn);
        cs.customerRun();
        String result = String.format("Please select the service you need (1.book Movie 2.logout b.exit)Movie_name: HolidayClassification: GeneralSynopsis: Family ComedyRelease_date: 2021-10-23 20:00:00Director: AliceCasts: Suzana, PeterMovie_name: The Penthouse: War in Life - Season 4Classification: Mature AccompaniesSynopsis: Dramatically UnbelievableRelease_date: 2021-10-23 22:30:00Director: Kim Soon-okCasts: Cheon seo-jin, Oh Yoon-heePlease enter movie name you want to book (b.back)Updating_time: %sMovie_name: HolidayProjection_hall: 2Cinema: disneyFront_seat_left: 20Middle_seat_left: 20Back_seat_left: 20Updating_time: %sMovie_name: HolidayProjection_hall: 3Cinema: SBSFront_seat_left: 30Middle_seat_left: 30Back_seat_left: 30Updating_time: %sMovie_name: HolidayProjection_hall: 4Cinema: SBSFront_seat_left: 40Middle_seat_left: 40Back_seat_left: 401.filter by screen 2.filter by cinema (b.stop filter)Please enter correct number1.filter by screen 2.filter by cinema (b.stop filter)Please enter cinemaPlease enter screening idPlease enter updating time (b.back)yyyy-MM-dd HH:mm:ssThis session is not existPlease enter cinemaPlease enter screening idPlease enter updating time (b.back)yyyy-MM-dd HH:mm:ssDo you have gift card ?(1.yes 2.no b.back)Please enter type (b.back)type: Child, Student, Adult, SeniorPlease enter seat (b.back)seat: Front, Middle, BackPlease enter correct stringPlease enter type (b.back)type: Child, Student, Adult, SeniorDo you have gift card ?(1.yes 2.no b.back)Please enter movie name you want to book (b.back)Please select the service you need (1.book Movie 2.logout b.exit)",strDate,strDate,strDate);
        String value = bytes.toString().trim();
        value = value.replaceAll("\r|\n", "");
        assertEquals(result, value);

    }
    @Test
    public void seatTest21(){
        cs.login = true;
        cs.user_id = "222222222";
        cal = Calendar.getInstance();
        cal.add(Calendar.SECOND,1);
        Date date = cal.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strDate = dateFormat.format(date);
        String s = String.format("1\r\nHoliday\r\nSBS\r\nb\r\nSBS\r\n2\r\n%s" +
                        "\r" +
                        "\nSBS\r" +
                        "\n3\r\n%s\r\n1\r\nChild\r\nb\r\nb\r\nb\r\nb\r\nb" +
                        "\r\nb",
                strDate,strDate);
        ByteArrayInputStream strIn = new ByteArrayInputStream(s.getBytes());
        System.setIn(strIn);
        cs.customerRun();
        String result = String.format("Please select the service you need (1.book Movie 2.logout b.exit)Movie_name: HolidayClassification: GeneralSynopsis: Family ComedyRelease_date: 2021-10-23 20:00:00Director: AliceCasts: Suzana, PeterMovie_name: The Penthouse: War in Life - Season 4Classification: Mature AccompaniesSynopsis: Dramatically UnbelievableRelease_date: 2021-10-23 22:30:00Director: Kim Soon-okCasts: Cheon seo-jin, Oh Yoon-heePlease enter movie name you want to book (b.back)Updating_time: %sMovie_name: HolidayProjection_hall: 2Cinema: disneyFront_seat_left: 20Middle_seat_left: 20Back_seat_left: 20Updating_time: %sMovie_name: HolidayProjection_hall: 3Cinema: SBSFront_seat_left: 30Middle_seat_left: 30Back_seat_left: 30Updating_time: %sMovie_name: HolidayProjection_hall: 4Cinema: SBSFront_seat_left: 40Middle_seat_left: 40Back_seat_left: 401.filter by screen 2.filter by cinema (b.stop filter)Please enter correct number1.filter by screen 2.filter by cinema (b.stop filter)Please enter cinemaPlease enter screening idPlease enter updating time (b.back)yyyy-MM-dd HH:mm:ssThis session is not existPlease enter cinemaPlease enter screening idPlease enter updating time (b.back)yyyy-MM-dd HH:mm:ssDo you have gift card ?(1.yes 2.no b.back)Please enter type (b.back)type: Child, Student, Adult, SeniorPlease enter seat (b.back)seat: Front, Middle, BackDo you have gift card ?(1.yes 2.no b.back)Please enter movie name you want to book (b.back)Please select the service you need (1.book Movie 2.logout b.exit)",strDate,strDate,strDate);
        String value = bytes.toString().trim();
        value = value.replaceAll("\r|\n", "");
        assertEquals(result, value);

    }
    @Test
    public void seatTest22(){
        cs.login = true;
        cs.user_id = "222222222";
        cal = Calendar.getInstance();
        cal.add(Calendar.SECOND,1);
        Date date = cal.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strDate = dateFormat.format(date);
        String s = String.format("1\r\nHoliday\r\nSBS\r\nb\r\nSBS\r\n2\r\n%s" +
                        "\r" +
                        "\nSBS\r" +
                        "\n3\r\n%s\r\n1\r\nChild\r\nFront\r\nb\r\nb\r\nb\r\nb" +
                        "\r\nb",
                strDate,strDate);
        ByteArrayInputStream strIn = new ByteArrayInputStream(s.getBytes());
        System.setIn(strIn);
        cs.customerRun();
        String result = String.format("Please select the service you need (1.book Movie 2.logout b.exit)Movie_name: HolidayClassification: GeneralSynopsis: Family ComedyRelease_date: 2021-10-23 20:00:00Director: AliceCasts: Suzana, PeterMovie_name: The Penthouse: War in Life - Season 4Classification: Mature AccompaniesSynopsis: Dramatically UnbelievableRelease_date: 2021-10-23 22:30:00Director: Kim Soon-okCasts: Cheon seo-jin, Oh Yoon-heePlease enter movie name you want to book (b.back)Updating_time: %sMovie_name: HolidayProjection_hall: 2Cinema: disneyFront_seat_left: 20Middle_seat_left: 20Back_seat_left: 20Updating_time: %sMovie_name: HolidayProjection_hall: 3Cinema: SBSFront_seat_left: 30Middle_seat_left: 30Back_seat_left: 30Updating_time: %sMovie_name: HolidayProjection_hall: 4Cinema: SBSFront_seat_left: 40Middle_seat_left: 40Back_seat_left: 401.filter by screen 2.filter by cinema (b.stop filter)Please enter correct number1.filter by screen 2.filter by cinema (b.stop filter)Please enter cinemaPlease enter screening idPlease enter updating time (b.back)yyyy-MM-dd HH:mm:ssThis session is not existPlease enter cinemaPlease enter screening idPlease enter updating time (b.back)yyyy-MM-dd HH:mm:ssDo you have gift card ?(1.yes 2.no b.back)Please enter type (b.back)type: Child, Student, Adult, SeniorPlease enter seat (b.back)seat: Front, Middle, BackPlease enter your gift card number (b.back)Do you have gift card ?(1.yes 2.no b.back)Please enter movie name you want to book (b.back)Please select the service you need (1.book Movie 2.logout b.exit)",strDate,strDate,strDate);
        String value = bytes.toString().trim();
        value = value.replaceAll("\r|\n", "");
        assertEquals(result, value);

    }
    @Test
    public void seatTest23(){
        cs.login = true;
        cs.user_id = "222222222";
        cal = Calendar.getInstance();
        cal.add(Calendar.SECOND,1);
        Date date = cal.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strDate = dateFormat.format(date);
        String s = String.format("1\r\nHoliday\r\nSBS\r\nb\r\nSBS\r\n2\r\n%s" +
                        "\r" +
                        "\nSBS\r" +
                        "\n3\r\n%s\r\n1\r\nChild\r\nFront\r\n125\r\nb\r\nb\r\nb\r\nb" +
                        "\r\nb",
                strDate,strDate);
        ByteArrayInputStream strIn = new ByteArrayInputStream(s.getBytes());
        System.setIn(strIn);
        cs.customerRun();
        String result = String.format("Please select the service you need (1.book Movie 2.logout b.exit)Movie_name: HolidayClassification: GeneralSynopsis: Family ComedyRelease_date: 2021-10-23 20:00:00Director: AliceCasts: Suzana, PeterMovie_name: The Penthouse: War in Life - Season 4Classification: Mature AccompaniesSynopsis: Dramatically UnbelievableRelease_date: 2021-10-23 22:30:00Director: Kim Soon-okCasts: Cheon seo-jin, Oh Yoon-heePlease enter movie name you want to book (b.back)Updating_time: %sMovie_name: HolidayProjection_hall: 2Cinema: disneyFront_seat_left: 20Middle_seat_left: 20Back_seat_left: 20Updating_time: %sMovie_name: HolidayProjection_hall: 3Cinema: SBSFront_seat_left: 30Middle_seat_left: 30Back_seat_left: 30Updating_time: %sMovie_name: HolidayProjection_hall: 4Cinema: SBSFront_seat_left: 40Middle_seat_left: 40Back_seat_left: 401.filter by screen 2.filter by cinema (b.stop filter)Please enter correct number1.filter by screen 2.filter by cinema (b.stop filter)Please enter cinemaPlease enter screening idPlease enter updating time (b.back)yyyy-MM-dd HH:mm:ssThis session is not existPlease enter cinemaPlease enter screening idPlease enter updating time (b.back)yyyy-MM-dd HH:mm:ssDo you have gift card ?(1.yes 2.no b.back)Please enter type (b.back)type: Child, Student, Adult, SeniorPlease enter seat (b.back)seat: Front, Middle, BackPlease enter your gift card number (b.back)card number limit 18 charPlease enter type (b.back)type: Child, Student, Adult, SeniorDo you have gift card ?(1.yes 2.no b.back)Please enter movie name you want to book (b.back)Please select the service you need (1.book Movie 2.logout b.exit)",strDate,strDate,strDate);
        String value = bytes.toString().trim();
        value = value.replaceAll("\r|\n", "");
        assertEquals(result, value);

    }
    @Test
    public void cinemaSystemTest00(){
        cs.login = true;
        cs.user_id = "222222222";
        cal = Calendar.getInstance();
        cal.add(Calendar.SECOND,1);
        Date date = cal.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strDate = dateFormat.format(date);
        String s = String.format("1\r\nHoliday\r\nSBS\r\nb\r\nSBS\r\n2\r\n%s" +
                        "\r" +
                        "\nSBS\r" +
                        "\n3\r\n%s\r\n1\r\nChild\r\nFront\r\n0987654321098765GC\r\n1\r\n2\r\nb\r\nb\r\nb" +
                        "\r\nb",
                strDate,strDate);
        ByteArrayInputStream strIn = new ByteArrayInputStream(s.getBytes());
        System.setIn(strIn);
        cs.customerRun();
        String result = String.format("Please select the service you need (1.book Movie 2.logout b.exit)Movie_name: HolidayClassification: GeneralSynopsis: Family ComedyRelease_date: 2021-10-23 20:00:00Director: AliceCasts: Suzana, PeterMovie_name: The Penthouse: War in Life - Season 4Classification: Mature AccompaniesSynopsis: Dramatically UnbelievableRelease_date: 2021-10-23 22:30:00Director: Kim Soon-okCasts: Cheon seo-jin, Oh Yoon-heePlease enter movie name you want to book (b.back)Updating_time: %sMovie_name: HolidayProjection_hall: 2Cinema: disneyFront_seat_left: 20Middle_seat_left: 20Back_seat_left: 20Updating_time: %sMovie_name: HolidayProjection_hall: 3Cinema: SBSFront_seat_left: 30Middle_seat_left: 30Back_seat_left: 30Updating_time: %sMovie_name: HolidayProjection_hall: 4Cinema: SBSFront_seat_left: 40Middle_seat_left: 40Back_seat_left: 401.filter by screen 2.filter by cinema (b.stop filter)Please enter correct number1.filter by screen 2.filter by cinema (b.stop filter)Please enter cinemaPlease enter screening idPlease enter updating time (b.back)yyyy-MM-dd HH:mm:ssThis session is not existPlease enter cinemaPlease enter screening idPlease enter updating time (b.back)yyyy-MM-dd HH:mm:ssDo you have gift card ?(1.yes 2.no b.back)Please enter type (b.back)type: Child, Student, Adult, SeniorPlease enter seat (b.back)seat: Front, Middle, BackPlease enter your gift card number (b.back)Are you confirm this transaction(1.yes 2.no)you only have two minutes to confirmGiftcard pay successfullyDo you want to continue buy ticket with same session or choose another one ?(1.continue 2.another movie)Please enter movie name you want to book (b.back)Please select the service you need (1.book Movie 2.logout b.exit)",strDate,strDate,strDate);
        String value = bytes.toString().trim();
        value = value.replaceAll("\r|\n", "");
        assertEquals(result, value);

    }
    @Test
    public void cinemaSystemTest01(){
        cs.login = true;
        cs.user_id = "222222222";
        cal = Calendar.getInstance();
        cal.add(Calendar.SECOND,1);
        Date date = cal.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strDate = dateFormat.format(date);
        String s = String.format("1\r\nHoliday\r\nSBS\r\nb\r\nSBS\r\n2\r\n%s" +
                        "\r" +
                        "\nSBS\r" +
                        "\n3\r\n%s\r\n1\r\nChild\r\nFront\r\n0987654321098765GC\r\n2\r\nb\r\nb\r\nb\r\nb\r\nb\r\nb" +
                        "\r\nb\r\nb\r\nb",
                strDate,strDate);
        ByteArrayInputStream strIn = new ByteArrayInputStream(s.getBytes());
        System.setIn(strIn);
        cs.customerRun();
        String result = String.format("Please select the service you need (1.book Movie 2.logout b.exit)Movie_name: HolidayClassification: GeneralSynopsis: Family ComedyRelease_date: 2021-10-23 20:00:00Director: AliceCasts: Suzana, PeterMovie_name: The Penthouse: War in Life - Season 4Classification: Mature AccompaniesSynopsis: Dramatically UnbelievableRelease_date: 2021-10-23 22:30:00Director: Kim Soon-okCasts: Cheon seo-jin, Oh Yoon-heePlease enter movie name you want to book (b.back)Updating_time: %sMovie_name: HolidayProjection_hall: 2Cinema: disneyFront_seat_left: 20Middle_seat_left: 20Back_seat_left: 20Updating_time: %sMovie_name: HolidayProjection_hall: 3Cinema: SBSFront_seat_left: 30Middle_seat_left: 30Back_seat_left: 30Updating_time: %sMovie_name: HolidayProjection_hall: 4Cinema: SBSFront_seat_left: 40Middle_seat_left: 40Back_seat_left: 401.filter by screen 2.filter by cinema (b.stop filter)Please enter correct number1.filter by screen 2.filter by cinema (b.stop filter)Please enter cinemaPlease enter screening idPlease enter updating time (b.back)yyyy-MM-dd HH:mm:ssThis session is not existPlease enter cinemaPlease enter screening idPlease enter updating time (b.back)yyyy-MM-dd HH:mm:ssDo you have gift card ?(1.yes 2.no b.back)Please enter type (b.back)type: Child, Student, Adult, SeniorPlease enter seat (b.back)seat: Front, Middle, BackPlease enter your gift card number (b.back)Are you confirm this transaction(1.yes 2.no)you only have two minutes to confirmPlease enter movie name you want to book (b.back)Please select the service you need (1.book Movie 2.logout b.exit)",strDate,strDate,strDate);
        String value = bytes.toString().trim();
        value = value.replaceAll("\r|\n", "");
        assertEquals(result, value);

    }
    @Test
    public void cinemaSystemTest02(){
        cs.login = true;
        cs.user_id = "222222222";
        cal = Calendar.getInstance();
        cal.add(Calendar.SECOND,1);
        Date date = cal.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strDate = dateFormat.format(date);
        String s = String.format("1\r\nHoliday\r\nSBS\r\nb\r\nSBS\r\n2\r\n%s" +
                        "\r" +
                        "\nSBS\r" +
                        "\n3\r\n%s\r\n2\r\nChild\r\nFront\r\nabc\r\nb\r\nb\r\nb\r\nb\r\nb\r\nb" +
                        "\r\nb\r\nb\r\nb",
                strDate,strDate);
        ByteArrayInputStream strIn = new ByteArrayInputStream(s.getBytes());
        System.setIn(strIn);
        cs.customerRun();
        String result = String.format("Please select the service you need (1.book Movie 2.logout b.exit)Movie_name: HolidayClassification: GeneralSynopsis: Family ComedyRelease_date: 2021-10-23 20:00:00Director: AliceCasts: Suzana, PeterMovie_name: The Penthouse: War in Life - Season 4Classification: Mature AccompaniesSynopsis: Dramatically UnbelievableRelease_date: 2021-10-23 22:30:00Director: Kim Soon-okCasts: Cheon seo-jin, Oh Yoon-heePlease enter movie name you want to book (b.back)Updating_time: %sMovie_name: HolidayProjection_hall: 2Cinema: disneyFront_seat_left: 20Middle_seat_left: 20Back_seat_left: 20Updating_time: %sMovie_name: HolidayProjection_hall: 3Cinema: SBSFront_seat_left: 30Middle_seat_left: 30Back_seat_left: 30Updating_time: %sMovie_name: HolidayProjection_hall: 4Cinema: SBSFront_seat_left: 40Middle_seat_left: 40Back_seat_left: 401.filter by screen 2.filter by cinema (b.stop filter)Please enter correct number1.filter by screen 2.filter by cinema (b.stop filter)Please enter cinemaPlease enter screening idPlease enter updating time (b.back)yyyy-MM-dd HH:mm:ssThis session is not existPlease enter cinemaPlease enter screening idPlease enter updating time (b.back)yyyy-MM-dd HH:mm:ssDo you have gift card ?(1.yes 2.no b.back)Please enter ticket type (b.back)type: Child, Student, Adult, SeniorPlease enter seat (b.back)seat: Front, Middle, BackHow many tickets do you want to buy? (b.back)Please enter correct numberPlease enter ticket type (b.back)type: Child, Student, Adult, SeniorDo you have gift card ?(1.yes 2.no b.back)Please enter movie name you want to book (b.back)Please select the service you need (1.book Movie 2.logout b.exit)",strDate,strDate,strDate);
        String value = bytes.toString().trim();
        value = value.replaceAll("\r|\n", "");
        assertEquals(result, value);

    }

    @Test
    public void payment(){
        assertFalse(cs.payment(null,null,null));
        assertFalse(cs.payment("cda","",""));
        assertFalse(cs.payment("abc","1111222233334444","123"));
    }
    @Test
    public void giftCardPay(){
        assertFalse(cs.giftcardPay(null,null));
        assertFalse(cs.giftcardPay("",""));
        assertFalse(cs.giftcardPay("1111222233334444GC",""));
  
        
    }
    @Test
    public void checkCard(){
            assertEquals(cs.checkCard(null), null);
            assertEquals(cs.checkCard(""), null);
    }
    @Test
    public void calculator(){
        assertEquals(cs.calculator("Child", "Gold"), 100.0);
        assertEquals(cs.calculator("Child", "Silver"), 75.0);
        assertEquals(cs.calculator("Child", "Bronze"), 50.0);
        assertEquals(cs.calculator("Student", "Gold"), 160.0);
        assertEquals(cs.calculator("Student", "Silver"), 120.00000000000001);
        assertEquals(cs.calculator("Student", "Bronze"), 80.0);
        assertEquals(cs.calculator("Adult", "Gold"), 200.0);
        assertEquals(cs.calculator("Adult", "Silver"), 150.0);
        assertEquals(cs.calculator("Adult", "Bronze"), 100.0);
        assertEquals(cs.calculator("Senior", "Gold"), 180.0);
        assertEquals(cs.calculator("Senior", "Silver"), 135.0);
        assertEquals(cs.calculator("Senior", "Bronze"), 90.0);
    }
    @Test
    public void transaction(){
        cal = Calendar.getInstance();
        assertFalse(cs.transaction(null, null, null, 1));
        assertFalse(cs.transaction(cal, "1234", "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789", 2));
        assertFalse(cs.transaction(cal, "1234", "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789", 1));
        assertFalse(cs.transaction(cal, "1234", "012", 1));
        assertFalse(cs.transaction(cal, "", "012", 1));
    }
    @Test
    public void getScreenSize(){
        assertEquals(cs.getScreenSize(2,null), null);
        assertEquals(cs.getScreenSize(1,""), null);
    }
}