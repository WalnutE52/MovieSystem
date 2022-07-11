package Movie;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class Stuff {
    private Connection c;
    private Statement stmt;
    protected String database;
    public Stuff(String database) {
        this.database = database;
    }
    public boolean addMovie(String name, String classification,
                            String synopsis, Calendar release_date,
                            String director, List<String> casts) {
        if (name == null || name.equals("") || classification == null || synopsis == null || release_date == null || director == null || casts == null) {
            System.out.println("null false");
            return false;
        }
        try{
            c = DBUtils.getCon(database);
            stmt = c.createStatement();
            PreparedStatement pss = c.prepareStatement("SELECT * FROM Movie where " +
                    "name = ?;");
            pss.setString(1, name);
            ResultSet rs = pss.executeQuery();
            if (rs.next() == true) {
                System.out.println("The movie is already existed.");
                pss.close();
                c.close();
                stmt.close();
                return false;
            }
    //        General (G), Parental Guidance (PG), Mature (M), Mature Accompanies (MA15+), Restricted (R18+).
            if (!classification.equals("General") && !classification.equals(
                    "Parental Guidance") && !classification.equals(
                    "Mature") && !classification.equals(
                    "Mature Accompanies") && !classification.equals(
                    "Restricted")) {
                System.out.println("Wrong classification.");
                pss.close();
                c.close();
                stmt.close();
                return false;
            }
            String Casts = "";
            if (casts.size() > 0) {
                for (int i = 0; i < casts.size() - 1; i++) {
                    Casts = Casts + casts.get(i) + ", ";
                }
                Casts = Casts + casts.get(casts.size() - 1);
            }
            System.out.println(Casts);
            Date date = release_date.getTime();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String strDate = dateFormat.format(date);
            PreparedStatement ps = c.prepareStatement("INSERT INTO Movie (name,classification,synopsis,release_date,director,casts) VALUES (?,?,?,?,?,?);PRAGMA foreign_keys = ON;");
            ps.setString(1, name);
            ps.setString(2, classification);
            ps.setString(3, synopsis);
            ps.setString(4, strDate);
            ps.setString(5, director);
            ps.setString(6, Casts);
            ps.executeUpdate();
            pss.close();
            ps.close();
            stmt.close();
            c.close();
            System.out.println("Movie added successfully!");
        } catch (Exception e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            return false;
        }
        return true;
    }

    public boolean deleteMovie(String name){
        if (name == null || name.equals("")) {
            return false;
        }
        try{
            c = DBUtils.getCon(database);
            stmt = c.createStatement();
            PreparedStatement ps = c.prepareStatement("SELECT * FROM Movie where name = ?;");
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if (rs.next() == true) {
                PreparedStatement pss = c.prepareStatement("DELETE FROM Movie where name = ?;PRAGMA foreign_keys = ON;");
                pss.setString(1, name);
                pss.executeUpdate();
                pss.close();
                ps.close();
                c.close();
                stmt.close();
                return true;
            } else {
                System.out.println("The movie does not exist.");
                ps.close();
                c.close();
                stmt.close();
                return false;
            }
        } catch(Exception e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            return false;
        }
    }

    public boolean editMovie(String name, String classification,
                             String synopsis, Calendar release_date,
                             String director, List<String> casts){
        if (name == null || name.equals("") || classification == null || synopsis == null || release_date == null || director == null || casts == null) {
            System.out.println("null false");
            return false;
        }
        if (!classification.equals("General") && !classification.equals(
                    "Parental Guidance") && !classification.equals(
                    "Mature") && !classification.equals(
                    "Mature Accompanies") && !classification.equals(
                    "Restricted")) {
            System.out.println("Wrong classification.");
            return false;
        }
        
        try{
            c = DBUtils.getCon(database);
            stmt = c.createStatement();
            PreparedStatement pss = c.prepareStatement("SELECT * FROM Movie where name = ?;");
            pss.setString(1, name);
            ResultSet rs = pss.executeQuery();
            if (rs.next() == false) {
                System.out.println("The movie does not exist.");
                c.close();
                stmt.close();
                pss.close();
                return false;
            }
    //        General (G), Parental Guidance (PG), Mature (M), Mature Accompanies (MA15+), Restricted (R18+).
            
            String Casts = "";
            if (casts.size() > 0) {
                for (int i = 0; i < casts.size() - 1; i++) {
                    Casts = Casts + casts.get(i) + ", ";
                }
                Casts = Casts + casts.get(casts.size() - 1);
            }
            Date date = release_date.getTime();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String strDate = dateFormat.format(date);
            PreparedStatement ps = c.prepareStatement("UPDATE Movie set classification= ? , synopsis = ?, release_date = ? , director = ? , casts = ? where name = ?;PRAGMA foreign_keys = ON;");
            ps.setString(6, name);
            ps.setString(1, classification);
            ps.setString(2, synopsis);
            ps.setString(3, strDate);
            ps.setString(4, director);
            ps.setString(5, Casts);
            ps.executeUpdate();
            ps.close();
            stmt.close();
            c.close();
            System.out.println("Movie was edited successfully!");
        } catch (Exception e) {
            System.out.println("The movie does not exist.");
            return false;
        }
        return true;
    }

    public boolean addSession(String movie_name, int screen_id,
                              Calendar updating_time, String screen_cinema){
        if (movie_name == null || movie_name.equals("") || screen_cinema == null || updating_time == null) {
            System.out.println("null false");
            return false;
        }
        try {
            c = DBUtils.getCon(database);
            stmt = c.createStatement();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String strDate = dateFormat.format(updating_time.getTime());
            int front;
            int middle;
            int back;
            PreparedStatement pss = c.prepareStatement("SELECT * FROM Session where movie_name = ? AND screen_id = ? And updating_time = ? And screen_cinema = ?;");
            pss.setString(1, movie_name);
            pss.setInt(2, screen_id);
            pss.setString(3, strDate);
            pss.setString(4, screen_cinema);
            ResultSet rs = pss.executeQuery();
            if (rs.next() == true) {
                System.out.println("The session has already existed.");
                pss.close();
                c.close();
                stmt.close();
                return false;
            }
            PreparedStatement checkScreening = c.prepareStatement("SELECT * FROM Screening where id = ? AND cinema = ?;");
            checkScreening.setInt(1, screen_id);
            checkScreening.setString(2, screen_cinema);
            ResultSet Screeningcheck = checkScreening.executeQuery();
            if (Screeningcheck.next() == false) {
                System.out.println("The screen does not exist");
                checkScreening.close();
                return false;
            } else {
                front = Screeningcheck.getInt("front_seat");
                middle = Screeningcheck.getInt("middle_seat");
                back = Screeningcheck.getInt("back_seat");
            }
            if (updating_time.compareTo(Calendar.getInstance()) < 0) {
                System.out.println("Please add future sessions");
                pss.close();
                c.close();
                stmt.close();
                checkScreening.close();
                return false;
            }
            PreparedStatement ps = c.prepareStatement("INSERT INTO Session (updating_time,movie_name,screen_id,screen_cinema,cur_front_seat,cur_middle_seat,cur_back_seat) VALUES (?,?,?,?,? ,?,?);PRAGMA foreign_keys = ON;");
            ps.setString(1, strDate);
            System.out.println(strDate);
            ps.setString(2, movie_name);
            ps.setInt(3, screen_id);
            ps.setString(4, screen_cinema);
            ps.setInt(5, front);
            ps.setInt(6, middle);
            ps.setInt(7, back);
            ps.executeUpdate();
            ps.close();
            pss.close();
            stmt.close();
            c.close();
            System.out.println("Session added successfully!");
        } catch (Exception e) {
            System.out.println("The movie does not exist.");
            return false;
        }
        return true;
    }


    public boolean deleteSession(String movie_name, int screen_id,
                                 Calendar updating_time, String screen_cinema){
        if (movie_name == null || movie_name.equals("") || screen_cinema == null || updating_time == null) {
            System.out.println("null false");
            return false;
        }
        try {
            c = DBUtils.getCon(database);
            stmt = c.createStatement();
            Date date = updating_time.getTime();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String strDate = dateFormat.format(date);
            PreparedStatement pss = c.prepareStatement("SELECT * FROM Session where movie_name = ? AND screen_id = ? And updating_time = ? And screen_cinema = ?;");
            pss.setString(1, movie_name);
            pss.setInt(2, screen_id);
            pss.setString(3, strDate);
            pss.setString(4, screen_cinema);
            ResultSet rs = pss.executeQuery();
            if (rs.next() == true) {
                pss.close();
                PreparedStatement psst = c.prepareStatement("DELETE FROM Session where movie_name = ? AND screen_id = ? And updating_time = ? And screen_cinema = ?;PRAGMA foreign_keys = ON;");
                psst.setString(1, movie_name);
                psst.setInt(2, screen_id);
                psst.setString(3, strDate);
                psst.setString(4, screen_cinema);
                psst.executeUpdate();
                System.out.println("The session was deleted successfully.");
                stmt.close();
                c.close();
                return true;
            } else {
                System.out.println("The session does not exist.");
                pss.close();
                stmt.close();
                c.close();
                return false;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    // public boolean editSession(String movie_name, int screen_id, String cinema, Calendar updating_time) {
    //     return false;
    // }
    public boolean addGiftCard(String giftcard_id){
        try {
            if(giftcard_id.length() != 18 ){
                System.out.println("The gift card id must be a 18 digital " +
                        "string");
                return false;
            }
            String suffix = giftcard_id.substring(16);
            if(!suffix.equals("GC")){
                System.out.println("The gift card id must has GC as a suffix.");
                return false;
            }
            for (int i = 0; i < giftcard_id.length()-2; i++) {
                if (!Character.isDigit(giftcard_id.charAt(i))){
                    System.out.println("The gift card id's first 16 digits " +
                            "must " +
                            "be int");
                    return false;
                }
            }
            c = DBUtils.getCon(database);
            stmt = c.createStatement();
            PreparedStatement ps = c.prepareStatement("SELECT * FROM " +
                    "Giftcard " +
                    "WHERE giftcard_id = ?");
            ps.setString(1,giftcard_id);
            ResultSet rs = ps.executeQuery();
            if(rs.next() == true){
                ps.close();
                System.out.println("The gift card already exists.");
                return false;
            }
            PreparedStatement pss = c.prepareStatement("INSERT INTO Giftcard " +
                    "(giftcard_id,used) " +
                 "VALUES (?,?);PRAGMA foreign_keys = ON;");
            pss.setString(1, giftcard_id);
            pss.setInt(2, 0);
            pss.executeUpdate();
            System.out.println("The gift card was added successfully.");
            stmt.close();
            c.close();
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }


    }

    public void getSession() {
        try {
            List<List<String>> exportDataList = new ArrayList<>();
            List<String> exportDataRowListFirst = new ArrayList<>();
            exportDataRowListFirst.add("updatingTime");
            exportDataRowListFirst.add("ScreenCinema");
            exportDataRowListFirst.add("ScreenId");
            exportDataRowListFirst.add("curFrontSeat");
            exportDataRowListFirst.add("curMiddleSeat");
            exportDataRowListFirst.add("curBackSeat");
            exportDataRowListFirst.add("screenName");
            exportDataRowListFirst.add("frontSeat");
            exportDataRowListFirst.add("middleSeat");
            exportDataRowListFirst.add("backSeat");
            exportDataRowListFirst.add("cinema");
            exportDataRowListFirst.add("nameFromMovie");
            exportDataRowListFirst.add("classification");
            exportDataRowListFirst.add("synopsis");
            exportDataRowListFirst.add("releaseDate");
            exportDataRowListFirst.add("director");
            exportDataRowListFirst.add("casts");
            exportDataList.add(exportDataRowListFirst);
            Connection con = DBUtils.getCon(database);
            Statement stmt = con.createStatement();
            String sql = String.format("select datetime(updating_time), movie_name, screen_id, screen_cinema, cur_front_seat, cur_middle_seat, cur_back_seat from Session;");
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                List<String> exportDataRowList = new ArrayList<>();
                String updatingTime = rs.getString("datetime(updating_time)");
                String movieName = rs.getString("movie_name");
                int screenId = rs.getInt("screen_id");
                String ScreenCinema = rs.getString("screen_cinema");
                int curFrontSeat = rs.getInt("cur_front_seat");
                int curMiddleSeat = rs.getInt("cur_middle_seat");
                int curBackSeat = rs.getInt("cur_back_seat");
                sql = String.format("select screen, front_seat, middle_seat, back_seat from Screening where id = %d AND cinema = '%s'", screenId, ScreenCinema);
                Statement stmt1 = con.createStatement();
                ResultSet rsScreening = stmt1.executeQuery(sql);
               int screenID = screenId;
                String screenName = rsScreening.getString("screen");
                int frontSeat = rsScreening.getInt("front_seat");
                int middleSeat = rsScreening.getInt("middle_seat");
                int backSeat = rsScreening.getInt("back_seat");
               String cinema = ScreenCinema;
                sql = String.format("SELECT classification, synopsis, datetime(release_date), director, casts FROM Movie WHERE name = '%s'", movieName);
                Statement stmt2 = con.createStatement();
               ResultSet rsMovie = stmt2.executeQuery(sql);
               String nameFromMovie = movieName;
               String classification = rsMovie.getString(1);
               String synopsis = rsMovie.getString(2);
               String releaseDate = rsMovie.getString(3);
               String director = rsMovie.getString(4);
               String casts = rsMovie.getString(5);
               exportDataRowList.add(updatingTime);
               exportDataRowList.add(ScreenCinema);
               exportDataRowList.add(screenID + "");
               exportDataRowList.add(curFrontSeat + "");
               exportDataRowList.add(curMiddleSeat + "");
               exportDataRowList.add(curBackSeat + "");
               exportDataRowList.add(screenName);
               exportDataRowList.add(frontSeat + "");
               exportDataRowList.add(middleSeat + "");
               exportDataRowList.add(backSeat + "");
               exportDataRowList.add(cinema);
               exportDataRowList.add(nameFromMovie);
               exportDataRowList.add(classification);
               exportDataRowList.add(synopsis);
               exportDataRowList.add(releaseDate);
               exportDataRowList.add(director);
               exportDataRowList.add(casts);
               exportDataList.add(exportDataRowList);
               rsScreening.close();
               rsMovie.close();
                stmt1.close();
            }
            rs.close();
            stmt.close();
            con.close();
            DBUtils.deleteFile("src/main/resources/", "Movie");
            DBUtils.createCSVFile(exportDataList, "src/main/resources/", "MovieSessionMessage");
        } catch (Exception e) {
            System.out.println("false");
            System.out.println(e);
        }
    }


    public void notReleasedMovie() {
        try {
            List<List<String>> exportDataList = new ArrayList<>();
            List<String> exportDataRowListFirst = new ArrayList<>();
            exportDataRowListFirst.add("name");
            exportDataRowListFirst.add("classification");
            exportDataRowListFirst.add("synopsis");
            exportDataRowListFirst.add("releaseDate");
            exportDataRowListFirst.add("director");
            exportDataRowListFirst.add("casts");
            exportDataList.add(exportDataRowListFirst);
            Connection con = DBUtils.getCon(database);
            Statement stmt = con.createStatement();
            String sql = String.format("SELECT name, classification, synopsis, datetime(release_date), director, casts FROM Movie WHERE release_date > '%s'", DBUtils.getCurrentTime());
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                List<String> exportDataRowList = new ArrayList<>();
                String name = rs.getString(1);
                String classification = rs.getString(2);
                String synopsis = rs.getString(3);
                String releaseDate = rs.getString(4);
                String director = rs.getString(5);
                String casts = rs.getString(6);
                exportDataRowList.add(name);
                exportDataRowList.add(classification);
                exportDataRowList.add(synopsis);
                exportDataRowList.add(releaseDate);
                exportDataRowList.add(director);
                exportDataRowList.add(casts);
                exportDataList.add(exportDataRowList);
            }
            rs.close();
            stmt.close();
            con.close();
            DBUtils.deleteFile("src/main/resources/", "NotReleased");
            DBUtils.createCSVFile(exportDataList, "src/main/resources/", "NotReleasedMovie");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public boolean checkStuff(String account, String password) {
        if (account == null || password == null) {
            return false;
        }
        if (account.equals("") || (account.length() != 9)) {
            return false;
        }
        if (password.equals("") || (password.length() > 50)) {
            return false;
        }
        boolean status = false;
        try {
            Connection con = DBUtils.getCon(database);
            PreparedStatement ps = con.prepareStatement("select * from Stuff where account=? and password=?");
            ps.setString(1, account);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            status = rs.next();
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return status;
    }
}
    








