package Movie;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Customer {
    private String database;
    public Customer(String database) {
        this.database = database;
    }

    public boolean checkTicket(String movie_name, int screen_id, String cinema, Calendar updating_time) {
        if (movie_name == null || updating_time == null || cinema == null) {
            System.out.println("null false");
            return false;
        }
        try {
            Connection con = DBUtils.getCon(database);
            Statement stmt = con.createStatement();

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String sql = String.format(
                    "SELECT * FROM Session WHERE movie_name = '%s' AND updating_time = '%s' AND screen_id = %d AND screen_cinema = '%s';",
                     movie_name, df.format(updating_time.getTime()), screen_id, cinema);
            ResultSet rs = stmt.executeQuery(sql);
            if(rs.next()) {
                rs.close();
                stmt.close();
                con.close();
                return true;
            }
            rs.close();
            stmt.close();
            con.close();
            System.out.println("This session is not exist");
            return false;
        } catch (Exception e) {
            System.out.println(e);
        }
        return true;
    }

    public int checkTicketLeft(Calendar updating_time, String movie_name, int screen_id, String screen_cinema, String seat){
        try {
            Connection con = DBUtils.getCon(database);
            Statement stmt = con.createStatement();

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String full_seat = String.format("cur_%s_seat", seat.toLowerCase());
            String sql = String.format(
                    "SELECT %s FROM Session WHERE movie_name = '%s' AND updating_time = '%s' AND screen_id = %d AND screen_cinema = '%s';",
                    full_seat, movie_name, df.format(updating_time.getTime()), screen_id, screen_cinema);
            ResultSet rs = stmt.executeQuery(sql);
            int cur_seat = rs.getInt(full_seat);
            rs.close();
            stmt.close();
            con.close();
            return cur_seat;
        } catch (Exception e) {
            System.out.println(e);
            return 0;
        }
    }

    public boolean addTicket(String movie_name, int screen_id, String cinema, Calendar updating_time, String type,
                             String user_id, String seat) {
        try {
            Connection con = DBUtils.getCon(database);
            Statement stmt = con.createStatement();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String full_seat = String.format("cur_%s_seat", seat.toLowerCase());
            String sql = String.format(
                    "SELECT %s FROM Session WHERE movie_name = '%s' AND updating_time = '%s' AND screen_id = %d AND screen_cinema = '%s'",
                    full_seat, movie_name, df.format(updating_time.getTime()), screen_id, cinema);
            ResultSet rs = stmt.executeQuery(sql);
            int cur_seat = 0;
            if (rs.next()) {
                cur_seat = rs.getInt(full_seat);
            }
            cur_seat -= 1;
            sql = String.format(
                    "PRAGMA foreign_keys = ON; UPDATE Session SET %s = %d WHERE movie_name = '%s' AND updating_time = '%s' AND screen_id = %d;",
                    full_seat, cur_seat, movie_name, df.format(updating_time.getTime()), screen_id);
            stmt.executeUpdate(sql);
            sql = String.format(
                    "PRAGMA foreign_keys = ON; INSERT INTO Ticket (user_id,updating_time,movie_name,screen_id,screen_cinema,type,seat) VALUES ('%s','%s', '%s', %d,'%s','%s','%s');",
                    user_id, df.format(updating_time.getTime()), movie_name, screen_id,cinema, type, seat);
            stmt.executeUpdate(sql);
            rs.close();
            stmt.close();
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return true;
    }

    public boolean register(String account, String password) {
        if (account == null || password == null) {
            System.out.println("Invalid input");
            return false;
        }

        if (account.equals("") || (account.length() != 9)) {
            System.out.println("Account must be limit 9 char，register failed");
            return false;
        }

        if (password.equals("") || (password.length() > 50)) {
            System.out.println("Password less than 50，register failed");
            return false;
        }
        int status = 0;
        try {
            Connection con = DBUtils.getCon(database);
            String sql = "select * from Users where account=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, account);
            ResultSet rs = ps.executeQuery();
            boolean s = rs.next();
            if (s) {
                System.out.println("account has registered");
                con.close();
                return false;
            }
            PreparedStatement preparedStatement = con
                    .prepareStatement("insert into Users(account ,password) values(?,?)");
            preparedStatement.setString(1, account);
            preparedStatement.setString(2, password);

            status = preparedStatement.executeUpdate();
            if (status > 0) {
                System.out.println("register success");
            }
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        if (status > 0) {
            return true;
        }
        return false;
    }

    public boolean login(String account, String password) {
        if (account == null || password == null) {
            System.out.println("Invalid Input");
            return false;
        }
        if (account.equals("") || (account.length() != 9)) {
            System.out.println("Account limit 9 char, register failed");
            return false;
        }

        if (password.equals("") || (password.length() > 50)) {
            System.out.println("Password less than 50, register failed");
            return false;
        }
        boolean status = false;
        try {
            Connection con = DBUtils.getCon(database);
            PreparedStatement ps = con.prepareStatement("select * from Users where account=? and password=?");
            ps.setString(1, account);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            status = rs.next();
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        if(!status){
            System.out.println("Account or password is invalid.");
        }
        return status;
    }

    public boolean checkSession(String movie_name) {
        boolean hasMovie = false;
        if (movie_name == null) {
            return false;
        }
        try {
            Connection con = DBUtils.getCon(database);
            PreparedStatement ps = con.prepareStatement(
                    "select * from Session where movie_name=? and updating_time >= ? and updating_time<= ?");
            ps.setString(1, movie_name);
            ps.setString(2, DBUtils.getCurrentTime());
            ps.setString(3, DBUtils.getWeekTime());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                
                hasMovie = true;
                printCheckSessionRs(rs);
            }
            con.close();
            if (hasMovie == false) {
                return false;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return true;
    }

    public boolean checkSpecificScreen(String movie_name, String screen) {
        if (movie_name == null || screen == null) {
            System.out.println("screen type false");
            return false;
        }
        if (!screen.equals("Gold") && !screen.equals("Silver") && !screen.equals("Bronze")) {
            System.out.println("screen type false");
            return false;
        }
        try {
            Connection con = DBUtils.getCon(database);
            PreparedStatement ps = con.prepareStatement("select * from Screening where screen= ? ");
            ps.setString(1, screen);
            ResultSet rs = ps.executeQuery();
            boolean exist = false;
            while (rs.next()) {
                int id = rs.getInt("id");
                String cinema = rs.getString("cinema");
                PreparedStatement pss = con
                        .prepareStatement("select * from Session where movie_name= ? and screen_id = ? AND updating_time > ? and updating_time < ? AND screen_cinema = ?;");
                pss.setString(1, movie_name);
                pss.setInt(2, id);
                pss.setString(3, DBUtils.getCurrentTime());
                pss.setString(4, DBUtils.getWeekTime());
                pss.setString(5, cinema);
                ResultSet rss = pss.executeQuery();

                while (rss.next()) {
                    
                    exist = true;
                    printCheckSessionRs(rss);
                }
            }
         
            con.close();
            if(!exist) {
                System.out.println("This movie does not have such screen");
                return false;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return true;
    }

    public boolean checkSpecificCinema(String movie_name, String cinema) {
        if (movie_name == null || cinema == null) {
            System.out.println("screen type false");
            return false;
        }
        
        try {
            Connection con = DBUtils.getCon(database);
            PreparedStatement ps = con.prepareStatement("select * from Screening where cinema= ?;");
            ps.setString(1, cinema);
            ResultSet rs = ps.executeQuery();
            boolean exist = false;
            while (rs.next()) {
                int id = rs.getInt("id");
                PreparedStatement pss = con
                        .prepareStatement("select * from Session where movie_name= ? and screen_id = ? AND updating_time > ? and updating_time < ? AND screen_cinema = ?;");
                pss.setString(1, movie_name);
                pss.setInt(2, id);
                pss.setString(3, DBUtils.getCurrentTime());
                pss.setString(4, DBUtils.getWeekTime());
                pss.setString(5, cinema);
                ResultSet rss = pss.executeQuery();

                while (rss.next()) {
                    
                    exist = true;
                    printCheckSessionRs(rss);
                }
            }
            con.close();
            if(!exist) {
                System.out.println("This movie does not have such session " +
                        "with this cinema");
                return false;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return true;
    }

    public boolean checkMovie() {
        try {
            Connection con = DBUtils.getCon(database);
            String spl = "select Movie.name,Movie.classification,Movie.synopsis,movie.release_date,movie.director,movie.casts, COUNT(*) from Movie INNER JOIN Session ON Movie.name=Session.movie_name WHERE updating_time > ? and updating_time < ? GROUP BY Movie.name";
            PreparedStatement ps = con.prepareStatement(spl);
            ps.setString(1, DBUtils.getCurrentTime());
            ps.setString(2, DBUtils.getWeekTime());
            ResultSet rs = ps.executeQuery();

            boolean exist = false;
            while (rs.next()) {
                
                exist = true;
                printCheckMovieRs(rs);
            }
            ps.close();
            rs.close();
            con.close();
            if(!exist){
                System.out.println("Do not have movie yet");
                return false;
            }
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
        return true;
    }
    private static void printCheckSessionRs(ResultSet rs) throws SQLException {
        if (rs.getString(1) == null){
            System.out.println("Updating_time: " );
            System.out.println("Movie_name: ");
            System.out.println("Projection_hall: ");
            System.out.println("Cinema: ");
            System.out.println("Front_seat_left: ");
            System.out.println("Middle_seat_left: ");
            System.out.println("Back_seat_left: ");
            System.out.println("Updating_time: ");
            System.out.println();
        } else {
            System.out.println("Updating_time: "+ rs.getString(1));
            System.out.println("Movie_name: "+ rs.getString(2));
            System.out.println("Projection_hall: "+ rs.getString(3));
            System.out.println("Cinema: "+ rs.getString(4));
            System.out.println("Front_seat_left: "+ rs.getString(5));
            System.out.println("Middle_seat_left: "+ rs.getString(6));
            System.out.println("Back_seat_left: "+ rs.getString(7));
            System.out.println();

        }
    }
        // Moive_name Classification Synopsis Release_date Director Casts
    private static void printCheckMovieRs(ResultSet rs) throws SQLException {
        if (rs.getString(1) == null){
            System.out.println("Movie_name: " );
            System.out.println("Classification: ");
            System.out.println("Synopsis: ");
            System.out.println("Release_date: ");
            System.out.println("Director: ");
            System.out.println("Casts: ");
            System.out.println();
        } else {
            System.out.println("Movie_name: "+ rs.getString(1));
            System.out.println("Classification: "+ rs.getString(2));
            System.out.println("Synopsis: "+ rs.getString(3));
            System.out.println("Release_date: "+ rs.getString(4));
            System.out.println("Director: "+ rs.getString(5));
            System.out.println("Casts: "+ rs.getString(6));
            System.out.println();

        }
    }
    
}
        
        
