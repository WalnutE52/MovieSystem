package Movie;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class Manager extends Stuff{
    private Connection c;
    private Statement stmt;
    public Manager(String database) {
        super(database);
    }
    public boolean checkManager(String account, String password) {
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
            PreparedStatement ps = con.prepareStatement("select * from Manager where account=? and password=?");
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
    public boolean addScreeningRoom(int screen_id, String screen, int front, int middle, int back, String cinema) {
        if (cinema == null || screen == null) {
            System.out.println("null false");
            return false;
        }
        if (!screen.equals("Gold") && !screen.equals("Silver") && !screen.equals("Bronze")) {
            System.out.println("screen type false");
            return false;
        }
        if (front <=0 || middle <= 0 || back <= 0) {
            System.out.println("seat less than 0");
            return false;
        }

        try {
            c = DBUtils.getCon(database);
            Statement stmt = c.createStatement();

            String sql = String.format(
                    "PRAGMA foreign_keys = ON; INSERT INTO Screening (id,screen,front_seat, middle_seat, back_seat, cinema) VALUES (%d, '%s',%d, %d, %d, '%s');",
                    screen_id, screen, front, middle, back, cinema);
            stmt.executeUpdate(sql);
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.out.println("screen id is already exit");
            return false;
        }
        return true;
    }

    public boolean removeScreeningRoom(int screen_id, String cinema) {
        if (cinema == null) {
            System.out.println("null false");
            return false;
        }

        try {
            Connection c = DBUtils.getCon(database);
            Statement stmt = c.createStatement();

            String sql = String.format(
                    "SELECT * FROM Screening WHERE cinema = '%s' AND id = %d", cinema, screen_id);
            ResultSet rs = stmt.executeQuery(sql);
            if(!rs.next()) {
                System.out.println("sorry no that screening now");
                rs.close();
                stmt.close();
                c.close();
                return false;
            }
            sql = String.format(
                    "PRAGMA foreign_keys = ON; DELETE FROM Screening  WHERE id = %d AND cinema = '%s';",
                    screen_id, cinema);
            stmt.executeUpdate(sql);
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
        return true;
    }

    public boolean setScreeningRoom(int screen_id, String screen, int front, int middle, int back, String cinema) {
        if (cinema == null || screen == null) {
            System.out.println("null false");
            return false;
        }
        if (!screen.equals("Gold") && !screen.equals("Silver") && !screen.equals("Bronze")) {
            System.out.println("screen type false");
            return false;
        }
        if (front <=0 || middle <= 0 || back <= 0) {
            System.out.println("seat less than 0");
            return false;
        }

        try {
            Connection c = DBUtils.getCon(database);
            Statement stmt = c.createStatement();

            String sql = String.format(
                    "SELECT * FROM Screening WHERE cinema = '%s' AND id = %d", cinema, screen_id);
            ResultSet rs = stmt.executeQuery(sql);
            if(!rs.next()) {
                System.out.println("sorry no that screening now");
                rs.close();
                stmt.close();
                c.close();
                return false;
            }
            sql = String.format(
                    "PRAGMA foreign_keys = ON; UPDATE Screening SET screen = '%s', front_seat = %d, middle_seat = %d, back_seat = %d  WHERE id = %d AND cinema = '%s';",
                    screen, front, middle, back, screen_id, cinema);
            stmt.executeUpdate(sql);
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
        return true;
    }

    public boolean addStuff(String account, String password){
        if (account == null || password == null) {
            System.out.println("Invalid input");
            return false;
        }

        if (account.equals("") || (account.length() != 9)) {
            System.out.println("Account must be limit 9 char.");
            return false;
        }

        if (password.equals("") || (password.length() > 50)) {
            System.out.println("Password less than 50.");
            return false;
        }
        try{
            c = DBUtils.getCon(database);
            stmt = c.createStatement();
            PreparedStatement pss = c.prepareStatement("SELECT * FROM Stuff where" +
                    " " +
                    "account = ?;");
            pss.setString(1, account);
            ResultSet rs = pss.executeQuery();
            if (rs.next() == true) {
                System.out.println("The account is already existed.");
                pss.close();
                return false;
            }

            PreparedStatement ps = c.prepareStatement("INSERT INTO Stuff " +
                    "(account," +
                    "password" +
                    ") " +
                    "VALUES (?,?);PRAGMA " +
                    "foreign_keys = ON;");
            ps.setString(1, account);
            ps.setString(2, password);
            ps.executeUpdate();
            System.out.println("Add the  successfully!");
            ps.close();
            stmt.close();
            c.close();
        } catch(Exception e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            return false;
        }
        return true;

    }
    public boolean removeStuff(String account){
        if (account == null ) {
            System.out.println("Invalid input");
            return false;
        }
        if (account.equals("") || (account.length() != 9)) {
            System.out.println("Account must be limit 9 char.");
            return false;
        }
        try {
            c = DBUtils.getCon(database);
            stmt = c.createStatement();
            PreparedStatement pss = c.prepareStatement("SELECT * FROM Stuff where" +
                    " " +
                    "account = ?;");
            pss.setString(1, account);
            ResultSet rs = pss.executeQuery();
            if (rs.next() == true) {
                PreparedStatement ps = c.prepareStatement("DELETE FROM Stuff where account = ? ;PRAGMA foreign_keys = ON;");
                ps.setString(1, account);
                ps.executeUpdate();
                System.out.println("Delete the  successfully!");
                ps.close();
                stmt.close();
                c.close();
                return true;
            }else{
                System.out.println("The account does not exist.");
                pss.close();
                c.close();
                return false;
            }
        } catch (Exception e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            return false;
        }
    }

    public void getCancelTransaction() {
        try {
            List<List<String>> exportDataList = new ArrayList<>();
            List<String> exportDataRowListFirst = new ArrayList<>();
            exportDataRowListFirst.add("TransactionID");
            exportDataRowListFirst.add("TransactionTime");
            exportDataRowListFirst.add("UserID");
            exportDataRowListFirst.add("Reason");
            exportDataRowListFirst.add("Status");
            exportDataList.add(exportDataRowListFirst);
            Connection con = DBUtils.getCon(database);
            Statement stmt = con.createStatement();
            String sql = String.format("SELECT transaction_id, datetime(transaction_time), user_id, reason, successful FROM Transactions WHERE successful = 0");
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                List<String> exportDataRowList = new ArrayList<>();
                String TransactionID = String.valueOf(rs.getInt(1));
                String TransactionTime = rs.getString(2);
                String UserID = rs.getString(3);
                String Reason = rs.getString(4);
                String Status = "Cancel";
                exportDataRowList.add(TransactionID);
                exportDataRowList.add(TransactionTime);
                exportDataRowList.add(UserID);
                exportDataRowList.add(Reason);
                exportDataRowList.add(Status);
                exportDataList.add(exportDataRowList);
            }
            rs.close();
            stmt.close();
            con.close();
            DBUtils.deleteFile("src/main/resources/","Cancel");
            DBUtils.createCSVFile(exportDataList, "src/main/resources/", "CancelTransactionMessage");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

}
