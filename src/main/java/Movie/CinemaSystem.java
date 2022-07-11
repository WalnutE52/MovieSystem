package Movie;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.*;

class RealTimer extends TimerTask{
    boolean timeout = false;
    @Override
    public void run() {
        System.out.println("The transaction has been closed. Press any key to return");
        timeout = true;
    }
}

public class CinemaSystem{
    public boolean login = false;
    private boolean manager_mode = false;
    private String database;
    private Customer customer;
    private Stuff stuff;
    private Manager manager;
    private double normal_price;
    private double child_per;
    private double student_per;
    private double adult_per;
    private double senior_per;
    private double bronze_per;
    private double silver_per;
    private double gold_per;
    public boolean test_mode = false;
    public String user_id = null;
    public CinemaSystem(String database, boolean manager_mode, double normal_price, double child_per, double student_per, double adult_per, double senior_per, double gold_per, double silver_per, double bronze_per) {
        this.normal_price = normal_price;
        this.child_per = child_per;
        this.student_per = student_per;
        this.adult_per = adult_per;
        this.senior_per = senior_per;
        this.bronze_per = bronze_per;
        this.silver_per =silver_per;
        this.gold_per =gold_per;
        this.database = database;
        customer = new Customer(database);
        stuff = new Stuff(database);
        manager = new Manager(database);
        this.manager_mode = manager_mode;
    }



    public void customerRun() {
//        String user_id = null;
        Scanner reader = new Scanner(System.in);
        while (true) {
            if (!login) {
                System.out.println("\nPlease select the service you need (1.book Movie 2.register 3.login b.exit) ");
            } else {
                System.out.println("\nPlease select the service you need (1.book Movie 2.logout b.exit)");
            }
            String temp = reader.nextLine();
            if (temp == null) {
                System.out.println("\nPlease enter correct number null");
                continue;
            }
            if (!temp.equals("1") && !temp.equals("2") && !temp.equals("3") && !temp.equals("b")) {
                System.out.println("\nPlease enter correct number");
                continue;
            }
            if (login == true && temp.equals("3")) {
                System.out.println("\nPlease enter correct number");
                continue;
            }
            if (temp.equals("b")) {
                reader.close();
                break;
            }
            if (temp.equals("2") || temp.equals("3")) {
                while (true) {
                    String id = null;
                    String password = null;
                    System.out.println("\nPlease enter your user id (b.back)");
                    id = reader.nextLine();
                    if (id == null) {
                        System.out.println("\nPlease enter correct id");
                        continue;
                    }
                    if (id.equals("b")) {
                        break;
                    }
                    password = DBUtils.readPasswordAndAccount("\nPlease enter your password (b.back)");
                    if (password == null || password.length() == 0) {
                        System.out.println("\nPlease enter correct password");

                        continue;
                    }
                    if (password.equals("b")) {
                        break;
                    }
                    if (temp.equals("2")) {
                        if (customer.register(id, password)) {
                            user_id = id;
                            login = true;
                            break;
                        } else {
                            continue;
                        }
                    } else {
                        if (customer.login(id, password)) {
                            user_id = id;
                            login = true;
                            break;
                        } else {
                            continue;
                        }
                    }
                }
            } else if (temp.equals("2")) {
                user_id = null;
                login = false;
            } else if (temp.equals("1")) {
                customer.checkMovie();
                while (true) {
                    boolean cancel_transaction = false;
                    boolean successful = false;

                    System.out.println("\nPlease enter movie name you want to book (b.back)");
                    String movie_name = reader.nextLine();
                    if(movie_name == null) {
                        System.out.println("\nPlease enter correct number");
                        continue;
                    }
                    if (movie_name.equals("b")) {
                        break;
                    }
                    if (!customer.checkSession(movie_name)) {
                        System.out.println("We do not have such movie in this week");
                    } else {
                        while (true) {

                            System.out.println("\n1.filter by screen 2.filter by cinema (b.stop filter)");

                            String cho = reader.nextLine();
                            if(cho == null) {
                                System.out.println("\nPlease enter correct number");
                                continue;
                            }
                            else if(cho.equals("1")) {
                                while(true) {
                                    System.out.println("Gold, Silver, Bronze (b.back)");
                                    String screen = reader.nextLine();
                                    if(screen == null) {
                                        System.out.println("\nPlease enter correct number");
                                        continue;
                                    }
                                    if(screen.equals("b")) {
                                        break;
                                    }
                                    if(!customer.checkSpecificScreen(movie_name, screen)) {
                                        continue;
                                    }

                                    break;
                                }
                            }
                            else if(cho.equals("2")) {
                                while(true) {
                                    printCinema();
                                    String cinema = reader.nextLine();
                                    if(cinema == null) {
                                        System.out.println("\nPlease enter correct number");
                                        continue;
                                    }
                                    if(cinema.equals("b")) {
                                        break;
                                    }
                                    if(!customer.checkSpecificCinema(movie_name, cinema)) {
                                        continue;
                                    }
                                    break;
                                }

                            }
                            else if(cho.equals("b")) {
                                break;
                            } else {
                                System.out.println("\nPlease enter correct number");
                                continue;
                            }
                        }
                        while (true) {
                            if(successful == true) {
                                break;
                            }
                            if(cancel_transaction == true) {
                                break;
                            }
                            if (login == false) {
                                System.out.println("\nPlease login or register(1.login, 2.register, b.back)");
                                String choose = reader.nextLine();
                                if (choose == null) {
                                    System.out.println("\nPlease enter correct number");
                                    continue;
                                } else if (choose.equals("b")) {
                                    break;
                                } else if (choose.equals("1") || choose.equals("2")) {
                                    while (true) {
                                        System.out.println("\nPlease enter your user id (b for go back)");
                                        String account = reader.nextLine();
                                        if (account == null) {
                                            System.out.println("Please enter correct id");
                                            continue;
                                        }
                                        if (account.equals("b")) {
                                            break;
                                        }
                                        String password = DBUtils.readPasswordAndAccount("\nPlease enter your password (b.back)");
                                        if (password == null || password.length() == 0) {
                                            System.out.println("\nPlease enter correct password");
                                            continue;
                                        }
                                        if (password.equals("b")) {
                                            break;
                                        }
                                        if (choose.equals("2")) {
                                            if (customer.register(account, password)) {
                                                user_id = account;
                                                login = true;
                                                break;
                                            } else {
                                                continue;
                                            }
                                        } else {
                                            if (customer.login(account, password)) {
                                                user_id = account;
                                                login = true;
                                                break;
                                            } else {
                                                continue;
                                            }
                                        }
                                    }
                                } else {
                                    System.out.println("\nPlease enter correct number");
                                    continue;
                                }
                            }
                            if (login == false) {
                                continue;
                            }
                            String cinema = null;
                            int screen_id_num = 0;
                            Calendar updating_time_c = null;

                            while(true) {
                                if(successful == true) {
                                    break;
                                }
                                if(cancel_transaction == true) {
                                    break;
                                }

                                System.out.println("\nPlease enter cinema");
                                cinema = reader.nextLine();
                                if(cinema == null) {
                                    System.out.println("\nPlease enter correct number");
                                    continue;
                                }
                                if (cinema.equals("b")) {
                                    break;
                                }
                                System.out.println("\nPlease enter screening id");
                                String screen_id = reader.nextLine();
                                if(screen_id == null) {
                                    System.out.println("\nPlease enter correct number");
                                    continue;
                                }
                                if (screen_id.equals("b")) {
                                    break;
                                }

                                try {
                                    screen_id_num = Integer.parseInt(screen_id);
                                } catch (NumberFormatException e) {
                                    System.out.println("\nPlease enter correct number");
                                    continue;
                                }

                                System.out.println("\nPlease enter updating time (b.back)\nyyyy-MM-dd HH:mm:ss");
                                String updating_time = reader.nextLine();
                                if(updating_time == null) {
                                    System.out.println("Please enter correct number");
                                    continue;
                                }
                                if (updating_time.equals("b")) {
                                    break;
                                }

                                try {
                                    updating_time_c = toCalender(updating_time);
                                } catch (Exception e) {
                                    System.out.println("\nPlease enter correct number");
                                    continue;
                                }
                                if(!customer.checkTicket(movie_name, screen_id_num, cinema, updating_time_c)) {
                                    continue;
                                }
                                break;
                            }


                            while(true) {
                                boolean continuebooking = false;
                                if(successful == true) {
                                    break;
                                }
                                if(cancel_transaction == true) {
                                    break;
                                }
                                System.out.println("\nDo you have gift card ?(1.yes 2.no b.back)");
                                String cos = reader.nextLine();
                                if(cos == null) {
                                    System.out.println("\nPlease enter correct number");
                                    continue;
                                } else if(cos.equals("1")) {
                                    while(true) {
                                        if(continuebooking == true) {
                                            break;
                                        }
                                        if(successful == true) {
                                            break;
                                        }
                                        if(cancel_transaction == true) {
                                            break;
                                        }
                                        System.out.println("\nPlease enter type (b.back)\ntype: Child, Student, Adult, Senior");
                                        String type = reader.nextLine();
                                        if(type == null) {
                                            System.out.println("\nPlease enter correct number");
                                            continue;
                                        }
                                        if(type.equals("b")) {
                                            break;
                                        }
                                        System.out.println("\nPlease enter seat (b.back)\nseat: Front, Middle, Back");
                                        String seat = reader.nextLine();
                                        if(seat == null) {
                                            System.out.println("\nPlease enter correct string");
                                            continue;
                                        }
                                        if(seat.equals("b")) {
                                            break;
                                        }
                                        if(!seat.equals("Front") && !seat.equals("Middle") &&!seat.equals("Back") ) {
                                            System.out.println("\nPlease enter correct string");
                                            continue;
                                        }
                                        System.out.println("\nPlease enter your gift card number (b.back)");
                                        String giftcard = reader.nextLine();
                                        if(giftcard == null) {
                                            System.out.println("\nPlease enter correct number");
                                            continue;
                                        }
                                        if(giftcard.equals("b")) {
                                            break;
                                        }
                                        String[] num = giftcard.split("");
                                        if (giftcard.equals("") || (giftcard.length() != 18) || (!num[16].equals("G")) || (!num[17].equals("C"))) {
                                            System.out.println("\ncard number limit 18 char");
                                            continue;
                                        }
                                        if(customer.checkTicketLeft(updating_time_c, movie_name, screen_id_num, cinema, seat) > 0) {
                                            while(true) {
                                                if(continuebooking == true) {
                                                    break;
                                                }
                                                if(successful == true) {
                                                    break;
                                                }
                                                if(cancel_transaction == true) {
                                                    break;
                                                }
                                                System.out.println("\nAre you confirm this transaction(1.yes 2.no)");
                                                System.out.println("\nyou only have two minutes to confirm");
                                                RealTimer realTimer = new RealTimer();
                                                Timer timer = new Timer();
                                                timer.schedule(realTimer, 5000);
                                                String confirm = reader.nextLine();
                                                if(realTimer.timeout){
                                                    cancel_transaction = true;
                                                    Calendar now = Calendar.getInstance();
                                                    transaction(now, user_id, "Time out", 0);
                                                    timer.cancel();
                                                    break;
                                                }
                                                timer.cancel();
                                                if(confirm == null) {
                                                    System.out.println("\nPlease enter correct number");
                                                    continue;
                                                }
                                                if(confirm.equals("1")) {
                                                    if(!giftcardPay(giftcard, user_id)) {
                                                        break;
                                                    } else {
                                                        customer.addTicket(movie_name, screen_id_num, cinema, updating_time_c, type, user_id, seat);
                                                        Calendar now = Calendar.getInstance();
                                                        transaction(now, user_id, "Gift card", 1);
                                                        while(true) {
                                                            System.out.println("\nDo you want to continue buy ticket with same session or choose another one ?(1.continue 2.another movie)");
                                                            String chh = reader.nextLine();
                                                            if(chh == null) {
                                                                System.out.println("\nPlease enter correct number");
                                                                continue;
                                                            } else if(chh.equals("1")) {
                                                                continuebooking = true;
                                                                break;

                                                            } else if(chh.equals("2")) {
                                                                successful = true;

                                                                break;
                                                            } else {
                                                                System.out.println("\nPlease enter correct number");
                                                                continue;
                                                            }
                                                        }
                                                    }
                                                } else if(confirm.equals("2")) {
                                                    cancel_transaction = true;
                                                    Calendar now = Calendar.getInstance();
                                                    transaction(now, user_id, "Cancel by user", 0);
                                                    break;
                                                } else{
                                                    System.out.println("\nPlease enter correct number");
                                                    continue;
                                                }
                                            }
                                        } else{
                                            System.out.println("\nThis session do not have enough ticket");
                                            break;
                                        }
                                    }
                                } else if(cos.equals("2")) {
                                    while(true) {
                                        if(continuebooking == true) {
                                            break;
                                        }
                                        if(successful == true) {
                                            break;
                                        }
                                        if(cancel_transaction == true) {
                                            break;
                                        }

                                        System.out.println("\nPlease enter ticket type (b.back)\ntype: Child, Student, Adult, Senior");
                                        String type = reader.nextLine();
                                        if(type == null) {
                                            System.out.println("\nPlease enter correct number");
                                            continue;
                                        }
                                        if(type.equals("b")) {
                                            break;
                                        }
                                        System.out.println("\nPlease enter seat (b.back)\nseat: Front, Middle, Back");
                                        String seat = reader.nextLine();
                                        if(seat == null || (!seat.equals("Front") && !seat.equals("Middle") && !seat.equals("Back"))) {
                                            System.out.println("\nPlease enter correct number");
                                            continue;
                                        }
                                        if(seat.equals("b")) {
                                            break;
                                        }

                                        System.out.println("\nHow many tickets do you want to buy? (b.back)");
                                        String number = reader.nextLine();
                                        if(number == null) {
                                            System.out.println("\nPlease enter correct number");
                                            continue;
                                        }
                                        if(number.equals("b")) {
                                            break;
                                        }
                                        int number_num;
                                        try {
                                            number_num = Integer.parseInt(number);
                                        } catch (NumberFormatException e) {
                                            System.out.println("\nPlease enter correct number");
                                            continue;
                                        }
                                        int checkseat = customer.checkTicketLeft(updating_time_c, movie_name, screen_id_num, cinema, seat);
                                        if(checkseat < number_num) {
                                            System.out.println(String.format("This session only has %d %s seats", checkseat, seat));
                                            continue;
                                        } else {
                                            while(true) {
                                                if(continuebooking == true) {
                                                    break;
                                                }
                                                if(successful == true) {
                                                    break;
                                                }
                                                if(cancel_transaction == true) {
                                                    break;
                                                }
                                                System.out.println("\nAre you confirm this transaction(1.yes 2.no)");
                                                System.out.println("\nyou only have two minutes to confirm");
                                                RealTimer realTimer = new RealTimer();
                                                Timer timer = new Timer();
                                                timer.schedule(realTimer, 5000);
                                                String confirm = reader.nextLine();
                                                if(realTimer.timeout){
                                                    cancel_transaction = true;
                                                    Calendar now = Calendar.getInstance();
                                                    transaction(now, user_id, "Time out", 0);
                                                    timer.cancel();
                                                    break;
                                                }
                                                timer.cancel();
                                                if(confirm == null) {
                                                    System.out.println("\nPlease enter correct number");
                                                    continue;
                                                }
                                                if(confirm.equals("1")) {
                                                    double total_price = 0;
                                                    String screen_size = getScreenSize(screen_id_num,cinema);
                                                    total_price = calculator(type, screen_size);
                                                    String pay = String.format("You should pay %s dollar", total_price*number_num);
                                                    System.out.println(pay);
                                                    String card = checkCard(user_id);
                                                    if(card!= null) {
                                                        while(true) {
                                                            System.out.println("\nDo you want to use the card you used last time(1.yes 2.no)");
                                                            String r = reader.nextLine();
                                                            if(r == null) {
                                                                System.out.println("\nPlease enter correct number");
                                                                continue;
                                                            } else if(r.equals("1")) {
                                                                break;
                                                            } else if(r.equals("2")) {
                                                                card = null;
                                                                break;
                                                            } else {
                                                                System.out.println("\nPlease enter correct number");
                                                                continue;
                                                            }
                                                        }
                                                    }
                                                    if(card == null) {
                                                        while(true) {
                                                            if(continuebooking == true) {
                                                                break;
                                                            }
                                                            if(successful == true) {
                                                                break;
                                                            }
                                                            if(cancel_transaction == true) {
                                                                break;
                                                            }
                                                            System.out.println("\nPlease enter card holder(b.cancel transaction)");
                                                            String holder = reader.nextLine();
                                                            if(holder == null) {
                                                                System.out.println("\nPlease enter correct number");
                                                                continue;
                                                            }
                                                            if(holder.equals("b")) {
                                                                cancel_transaction = true;
                                                                Calendar now = Calendar.getInstance();
                                                                transaction(now, user_id, "Cancel by user", 0);
                                                                break;
                                                            }
                                                            String cardnumber;
                                                            cardnumber= DBUtils.readPasswordAndAccount("Please enter your Bank account (b.back)");
                                                            if(cardnumber == null || cardnumber.length() != 16 || cardnumber.equals("b")) {
                                                                System.out.println("\nPlease enter correct bank account");
                                                                continue;
                                                            } else {
                                                                payment(holder, cardnumber, user_id);
                                                                for(int i = 0; i < number_num; i++) {
                                                                    customer.addTicket(movie_name, screen_id_num, cinema, updating_time_c, type, user_id, seat);
                                                                }
                                                                Calendar now = Calendar.getInstance();
                                                                transaction(now, user_id, "pay by card", 1);
                                                                while(true) {
                                                                    System.out.println("\nDo you want to continue buy ticket with same session or choose another one ?(1.continue 2.another movie)");
                                                                    String chh = reader.nextLine();
                                                                    if(chh == null) {
                                                                        System.out.println("\nPlease enter correct number");
                                                                        continue;
                                                                    } else if(chh.equals("1")) {
                                                                        continuebooking = true;
                                                                        break;

                                                                    } else if(chh.equals("2")) {
                                                                        successful = true;
                                                                        break;
                                                                    } else {
                                                                        System.out.println("\nPlease enter correct number");
                                                                        continue;
                                                                    }
                                                                }
                                                            }
                                                        }

                                                    } else if(card != null) {
                                                        payment("holder", card, user_id);
                                                        for(int i = 0; i < number_num; i++) {
                                                            customer.addTicket(movie_name, screen_id_num, cinema, updating_time_c, type, user_id, seat);
                                                        }
                                                        Calendar now = Calendar.getInstance();
                                                        transaction(now, user_id, "pay by card", 1);
                                                        while(true) {
                                                            System.out.println("\nDo you want to continue buy ticket with same session or choose another one ?(1.continue 2.another movie)");
                                                            String chh = reader.nextLine();
                                                            if(chh == null) {
                                                                System.out.println("\nPlease enter correct number");
                                                                continue;
                                                            } else if(chh.equals("1")) {
                                                                continuebooking = true;
                                                                break;

                                                            } else if(chh.equals("2")) {
                                                                successful = true;
                                                                break;
                                                            } else {
                                                                System.out.println("\nPlease enter correct number");
                                                                continue;
                                                            }
                                                        }


                                                    }


                                                } else if(confirm.equals("2")) {
                                                    cancel_transaction = true;
                                                    Calendar now = Calendar.getInstance();
                                                    transaction(now, user_id, "Cancel by user", 0);
                                                    break;
                                                } else{
                                                    System.out.println("\nPlease enter correct number");
                                                    continue;
                                                }
                                            }
                                        }
                                    }
                                } else if(cos.equals("b")) {
                                    cancel_transaction = true;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    public void stuffRun() {
        Scanner reader = new Scanner(System.in);

        while (true) {
            boolean status = true;
            if(!test_mode) {
                System.out.println("\nPlease enter your  id (b.exist)");
                String id = reader.nextLine();
                if (id == null ) {
                    System.out.println("\nPlease enter correct id\n");
                    continue;
                }
                if (id.equals("b")) {
                    reader.close();
                    return;
                }
                if ( id.length() < 9) {
                    System.out.println("\nPlease enter correct id\n");
                    continue;
                }


                String password = DBUtils.readPasswordAndAccount("\nPlease enter your  password");


                if(manager_mode) {
                    if(!manager.checkManager(id, password)) {
                        status = false;
                        System.out.println("Account or password is invalid");
                        continue;
                    }
                }
                else if(!manager_mode) {
                    if(!stuff.checkStuff(id, password)) {
                        status = false;
                        System.out.println("Account or password is invalid");
                        continue;
                    }
                }
            }
            if(status) {
                while (true) {
                    if (manager_mode) {
                        System.out.println(
                                "\nSelect the data you want to edit(1.Movie 2.Session 3.Gift Card 4.Staff 5.Cancel Transaction 6.Screening b.exist)");
                    } else {
                        System.out.println("\nSelect the data you want to edit(1.Movie 2.Session 3.Gift Card b.exist)");
                    }
                    String input = reader.nextLine();
                    if (input == null) {
                        System.out.println("\nPlease enter correct number");
                        continue;
                    }
                    if (input.equals("b")) {
                        reader.close();
                        return;
                    }
                    if (input.equals("1")) {

                        while (true) {
                            System.out.println(
                                    "\n1.add new movie 2.delete moive 3.edit moive 4.get upcoming movies (b.back)");
                            String movieChooses = reader.nextLine();
                            if (movieChooses == null) {
                                System.out.println("\nPlease enter correct number");
                                continue;
                            }
                            if (movieChooses.equals("1")) {
                                while (true) {
                                    System.out.println("\nPlease enter movie name (b.back)");
                                    String movie_name = reader.nextLine();
                                    if(movie_name == null) {
                                        System.out.println("\nPlease enter correct id\n");
                                        continue;
                                    }
                                    if (movie_name.equals("b")) {
                                        break;
                                    }
                                    System.out.println(//1\1\movie
                                            "\nPlease enter classification (b.back)\nGeneral (G), Parental Guidance (PG), Mature (M), Mature Accompanies (MA15+), Restricted (R18+)");
                                    String classification = reader.nextLine();
                                    if (classification.equals("b")) {
                                        break;
                                    }
                                    System.out.println("\nPlease enter synopsis (b.back)");
                                    String synopsis = reader.nextLine();
                                    if(synopsis == null) {
                                        System.out.println("\nPlease enter correct id\n");
                                        continue;
                                    }
                                    if (synopsis.equals("b")) {
                                        break;
                                    }
                                    System.out.println("\nPlease enter release date (b.back)\nyyyy-MM-dd HH:mm:ss");
                                    String release_date = reader.nextLine();
                                    if(release_date == null) {
                                        System.out.println("\nPlease enter correct id\n");
                                        continue;
                                    }
                                    if (release_date.equals("b")) {
                                        break;
                                    }
                                    Calendar release_date_c;
                                    try {
                                        release_date_c = toCalender(release_date);

                                    } catch(Exception e) {
                                        System.out.println("\nPlease enter correct calendar format\n");
                                        continue;
                                    }

                                    System.out.println("\nPlease enter director (b.back)");
                                    String director = reader.nextLine();
                                    if(director == null) {
                                        System.out.println("\nPlease enter correct id\n");
                                        continue;
                                    }
                                    if (director.equals("b")) {
                                        break;
                                    }
                                    System.out.println("\nPlease enter cast (b.back)\nactor1 actor2");
                                    String cast = reader.nextLine();
                                    if(cast == null) {
                                        System.out.println("\nPlease enter correct id\n");
                                        continue;
                                    }
                                    if (cast.equals("b")) {
                                        break;
                                    }
                                    List<String> ls = Arrays.asList(cast.split(" "));
                                    if (!stuff.addMovie(movie_name, classification, synopsis, release_date_c, director,
                                            ls)) {
                                        continue;
                                    } else {
                                        System.out.println("Add movie successfully");
                                        break;
                                    }
                                }

                            } else if (movieChooses.equals("2")) {
                                while (true) {
                                    System.out.println("\nPlease enter movie name (b.back)");
                                    String movie_name = reader.nextLine();
                                    if (movie_name == null) {
                                        System.out.println("\nPlease enter correct number");
                                        continue;
                                    } else if (movie_name.equals("b")) {
                                        break;
                                    } else {
                                        if (!stuff.deleteMovie(movie_name)) {
                                            continue;
                                        } else {
                                            System.out.println("Delete Movie successfully");
                                            break;
                                        }
                                    }
                                }
                            } else if (movieChooses.equals("3")) {
                                while (true) {
                                    System.out.println("\nPlease enter movie name (b.back)");
                                    String movie_name = reader.nextLine();
                                    if(movie_name == null) {
                                        System.out.println("\nPlease enter correct id\n");
                                        continue;
                                    }
                                    if (movie_name.equals("b")) {
                                        break;
                                    }
                                    System.out.println(
                                            "\nPlease enter classification (b.back)\nGeneral (G), Parental Guidance (PG), Mature (M), Mature Accompanies (MA15+), Restricted (R18+)");
                                    String classification = reader.nextLine();
                                    if(classification == null) {
                                        System.out.println("\nPlease enter correct id\n");
                                        continue;
                                    }
                                    if (classification.equals("b")) {
                                        break;
                                    }
                                    System.out.println("\nPlease enter synopsis (b.back)");

                                    String synopsis = reader.nextLine();
                                    if(synopsis == null) {
                                        System.out.println("\nPlease enter correct id\n");
                                        continue;
                                    }
                                    if (synopsis.equals("b")) {
                                        break;
                                    }
                                    System.out.println("\nPlease enter release date (b.back)\nyyyy-MM-dd HH:mm:ss");
                                    String release_date = reader.nextLine();
                                    if(release_date == null) {
                                        System.out.println("\nPlease enter correct id\n");
                                        continue;
                                    }
                                    if (release_date.equals("b")) {
                                        break;
                                    }
                                    Calendar release_date_c;
                                    try {
                                        release_date_c = toCalender(release_date);
                                    } catch (Exception e) {
                                        System.out.println("\nPlease enter correct number");
                                        continue;
                                    }
                                    System.out.println("\nPlease enter director (b.back)");
                                    String director = reader.nextLine();
                                    if (director.equals("b")) {
                                        break;
                                    }
                                    System.out.println("\nPlease enter cast (b.back)\nactor1 actor2");
                                    String cast = reader.nextLine();
                                    if(cast == null) {
                                        System.out.println("\nPlease enter correct id\n");
                                        continue;
                                    }
                                    if (cast.equals("b")) {
                                        break;
                                    }
                                    List<String> ls = Arrays.asList(cast.split(" "));
                                    if (!stuff.editMovie(movie_name, classification, synopsis, release_date_c, director,
                                            ls)) {
                                        continue;
                                    } else {
                                        System.out.println("Edit movie successfully");
                                        break;
                                    }

                                }

                            } else if (movieChooses.equals("b")) {
                                break;

                            } else if (movieChooses.equals("4")) {
                                stuff.notReleasedMovie();
                                System.out.println("Output upcoming movie successfully!");
                                break;
                            } else {
                                System.out.println("\nPlease enter correct number");
                                continue;
                            }
                        }

                    } else if (input.equals("2")) {
                        while (true) {

                            System.out.println("\n1.add new session 2.delete session 3.get session report (b.back)");
                            String choose = reader.nextLine();
                            if (choose == null) {
                                System.out.println("\nPlease enter correct number");
                                continue;
                            } else if (choose.equals("1")) {
                                while (true) {
                                    System.out.println("\nPlease enter movie name (b.back)");
                                    String movie_name = reader.nextLine();
                                    if(movie_name == null) {
                                        System.out.println("\nPlease enter correct id\n");
                                        continue;
                                    }
                                    if (movie_name.equals("b")) {
                                        break;
                                    }
                                    System.out.println("\nPlease enter screening id (b.back)");
                                    String screen_id = reader.nextLine();
                                    if(screen_id == null) {
                                        System.out.println("\nPlease enter correct id\n");
                                        continue;
                                    }
                                    if (screen_id.equals("b")) {
                                        break;
                                    }
                                    int screen_id_num;
                                    try {
                                        screen_id_num = Integer.parseInt(screen_id);
                                    } catch (NumberFormatException e) {
                                        System.out.println("\nPlease enter correct number");
                                        continue;
                                    }
                                    System.out.println("\nPlease enter updating time (b.back)\nyyyy-MM-dd HH:mm:ss");
                                    String updating_time = reader.nextLine();
                                    if(updating_time == null) {
                                        System.out.println("\nPlease enter correct id\n");
                                        continue;
                                    }
                                    if (updating_time.equals("b")) {
                                        break;
                                    }
                                    Calendar updating_time_c;
                                    try {
                                        updating_time_c = toCalender(updating_time);
                                    } catch (Exception e) {
                                        System.out.println("\nPlease enter correct number");
                                        continue;
                                    }
                                    System.out.println("\nPlease enter cinema (b.back)");
                                    String cinema = reader.nextLine();
                                    if(cinema == null) {
                                        System.out.println("\nPlease enter correct id\n");
                                        continue;
                                    }
                                    if (cinema.equals("b")) {
                                        break;
                                    }
                                    if (!stuff.addSession(movie_name, screen_id_num, updating_time_c, cinema)) {
                                        continue;
                                    } else {
                                        System.out.println("Add session successfully");
                                        break;
                                    }
                                }

                            } else if (choose.equals("2")) {
                                while (true) {
                                    System.out.println("\nPlease enter movie name (b.back)");
                                    String movie_name = reader.nextLine();
                                    if(movie_name == null) {
                                        System.out.println("\nPlease enter correct id\n");
                                        continue;
                                    }
                                    if (movie_name.equals("b")) {
                                        break;
                                    }
                                    System.out.println("\nPlease enter screen id (b.back)");
                                    String screen_id = reader.nextLine();
                                    if(screen_id == null) {
                                        System.out.println("\nPlease enter correct id\n");
                                        continue;
                                    }
                                    if (screen_id.equals("b")) {
                                        break;
                                    }
                                    int screen_id_num;
                                    try {
                                        screen_id_num = Integer.parseInt(screen_id);
                                    } catch (NumberFormatException e) {
                                        System.out.println("\nPlease enter correct number");
                                        continue;
                                    }
                                    System.out.println("\nPlease enter updating time (b.back)\nyyyy-MM-dd HH:mm:ss");
                                    String updating_time = reader.nextLine();
                                    if (updating_time.equals("b")) {
                                        break;
                                    }
                                    Calendar updating_time_c;
                                    try {
                                        updating_time_c = toCalender(updating_time);
                                    } catch (Exception e) {
                                        System.out.println("\nPlease enter correct number");
                                        continue;
                                    }
                                    System.out.println("\nPlease enter cinema (b.back)");
                                    String cinema = reader.nextLine();
                                    if(cinema == null) {
                                        System.out.println("\nPlease enter correct id\n");
                                        continue;
                                    }
                                    if (cinema.equals("b")) {
                                        break;
                                    }
                                    if (!stuff.deleteSession(movie_name, screen_id_num, updating_time_c, cinema)) {
                                        continue;
                                    } else {
                                        System.out.println("Delete session successfully");
                                        break;
                                    }
                                }
                            } else if (choose.equals("3")) {
                                stuff.getSession();
                                System.out.println("Output session report successfully!");
                                break;
                            } else if (choose.equals("b")) {
                                break;
                            }else {
                                System.out.println("\nPlease enter correct number");
                                continue;
                            }
                        }

                    } else if (input.equals("3")) {
                        while (true) {
                            System.out.println("\nPlease enter Gift card number. (b.back)");
                            String giftcard = reader.nextLine();
                            if (giftcard == null) {
                                System.out.println("\nPlease enter correct number");
                                continue;
                            }
                            if (giftcard.equals("b")) {
                                break;
                            }
                            if (!stuff.addGiftCard(giftcard)) {
                                continue;
                            } else {
                                System.out.println("Add gift card successfully");
                                break;
                            }
                        }
                    } else if (input.equals("4")) {
                        if (!manager_mode) {
                            System.out.println("\nPlease enter correct number");
                            continue;
                        }
                        while (true) {
                            System.out.println("\n1. add staff 2. delete staff b.back");
                            String stuff1 = reader.nextLine();
                            if (stuff1 == null) {
                                System.out.println("\nPlease enter correct number");
                                continue;
                            }
                            if (stuff1.equals("b")) {
                                break;
                            }
                            if (stuff1.equals("1")) {
                                while (true) {
                                    System.out.println("\nPlease enter staff id (b.back)");
                                    String stuff_account = reader.nextLine();
                                    if (stuff_account == null) {
                                        System.out.println("\nPlease enter correct number");
                                        continue;
                                    }
                                    if (stuff_account.equals("b")) {
                                        break;
                                    }
                                    System.out.println("\nPlease enter staff password (b.back)");
                                    String stuff_password = reader.nextLine();
                                    if (stuff_password == null) {
                                        System.out.println("\nPlease enter correct number");
                                        continue;
                                    }
                                    if (stuff_password.equals("b")) {
                                        break;
                                    }
                                    if (!manager.addStuff(stuff_account, stuff_password)) {
                                        continue;
                                    } else {
                                        break;
                                    }
                                }
                            } else if (stuff1.equals("2")) {
                                while (true) {
                                    System.out.println("\nPlease enter staff id (b.back)");
                                    String stuff_account = reader.nextLine();
                                    if (stuff_account == null) {
                                        System.out.println("\nPlease enter correct number");
                                        continue;
                                    }
                                    if (stuff_account.equals("b")) {
                                        break;
                                    }
                                    if (!manager.removeStuff(stuff_account)) {
                                        continue;
                                    } else {
                                        break;
                                    }
                                }
                            }
                        }
                    } else if (input.equals("5")) {
                        if (!manager_mode) {
                            System.out.println("\nPlease enter correct number");
                            continue;
                        }
                        System.out.println("Output cancel transaction report successfully!");
                        manager.getCancelTransaction();

                    } else if (input.equals("6")) {
                        if (!manager_mode) {
                            System.out.println("\nPlease enter correct number");
                            continue;
                        }

                        while (true) {
                            System.out.println("\n1. add screening 2. delete screening 3. edit screening (b.back)");
                            String cho = reader.nextLine();
                            if (cho == null) {
                                System.out.println("\nPlease enter correct number");
                                continue;
                            }
                            if (cho.equals("1")) {
                                while (true) {
                                    System.out.println("\nPlease enter cinema (b.back)");
                                    String cinema = reader.nextLine();
                                    if (cinema == null) {
                                        System.out.println("\nPlease enter correct number");
                                        continue;
                                    }
                                    if (cinema.equals("b")) {
                                        break;
                                    }
                                    System.out.println("\nPlease enter screening id (b.back)");
                                    String screen_id = reader.nextLine();
                                    if (screen_id == null) {
                                        System.out.println("\nPlease enter correct number");
                                        continue;
                                    }
                                    if (screen_id.equals("b")) {
                                        break;
                                    }
                                    int screen_id_num;
                                    try {
                                        screen_id_num = Integer.parseInt(screen_id);
                                    } catch (NumberFormatException e) {
                                        System.out.println("\nPlease enter correct number");
                                        continue;
                                    }

                                    System.out.println("\nPlease enter screen size (b.back)");
                                    String screen_size = reader.nextLine();
                                    if (screen_size == null) {
                                        System.out.println("\nPlease enter correct number");
                                        continue;
                                    }
                                    if (screen_size.equals("b")) {
                                        break;
                                    }

                                    System.out.println("\nPlease enter front seat number (b.back)");
                                    String front = reader.nextLine();
                                    if (front == null) {
                                        System.out.println("\nPlease enter correct number");
                                        continue;
                                    }
                                    if (front.equals("b")) {
                                        break;
                                    }
                                    int front_num;
                                    try {
                                        front_num = Integer.parseInt(front);
                                    } catch (NumberFormatException e) {
                                        System.out.println("\nPlease enter correct number");
                                        continue;
                                    }

                                    System.out.println("\nPlease enter middle seat number (b.back)");
                                    String middle = reader.nextLine();
                                    if (middle == null) {
                                        System.out.println("\nPlease enter correct number");
                                        continue;
                                    }
                                    if (middle.equals("b")) {
                                        break;
                                    }
                                    int middle_num;
                                    try {
                                        middle_num = Integer.parseInt(middle);
                                    } catch (NumberFormatException e) {
                                        System.out.println("\nPlease enter correct number");
                                        continue;
                                    }


                                    System.out.println("\nPlease enter back seat number (b.back)");
                                    String back = reader.nextLine();
                                    if (back == null) {
                                        System.out.println("\nPlease enter correct number");
                                        continue;
                                    }
                                    if (back.equals("b")) {
                                        break;
                                    }
                                    int back_num;
                                    try {
                                        back_num = Integer.parseInt(back);
                                    } catch (NumberFormatException e) {
                                        System.out.println("\nPlease enter correct number");
                                        continue;
                                    }

                                    if (!manager.addScreeningRoom(screen_id_num, screen_size, front_num, middle_num,
                                            back_num, cinema)) {
                                        continue;
                                    } else {
                                        System.out.println("Screening is added successfully");
                                        break;
                                    }

                                }
                            } else if (cho.equals("2")) {
                                while (true) {
                                    System.out.println("\nPlease enter cinema (b.back)");
                                    String cinema = reader.nextLine();
                                    if (cinema == null) {
                                        System.out.println("\nPlease enter correct number");
                                        continue;
                                    }
                                    if (cinema.equals("b")) {
                                        break;
                                    }
                                    System.out.println("\nPlease enter screening id (b.back)");
                                    String screen_id = reader.nextLine();
                                    if (screen_id == null) {
                                        System.out.println("\nPlease enter correct number");
                                        continue;
                                    }
                                    if (screen_id.equals("b")) {
                                        break;
                                    }
                                    int screen_id_num;
                                    try {
                                        screen_id_num = Integer.parseInt(screen_id);
                                    } catch (NumberFormatException e) {
                                        System.out.println("\nPlease enter correct number");
                                        continue;
                                    }
                                    if (!manager.removeScreeningRoom(screen_id_num, cinema)) {
                                        continue;
                                    } else {
                                        System.out.println("Screening is deleted successfully");
                                        break;
                                    }
                                }

                            } else if (cho.equals("3")) {
                                while (true) {
                                    System.out.println("\nPlease enter cinema (b.back)");
                                    String cinema = reader.nextLine();
                                    if (cinema == null) {
                                        System.out.println("\nPlease enter correct number");
                                        continue;
                                    }
                                    if (cinema.equals("b")) {
                                        break;
                                    }
                                    System.out.println("\nPlease enter screening id (b.back)");
                                    String screen_id = reader.nextLine();
                                    if (screen_id == null) {
                                        System.out.println("\nPlease enter correct number");
                                        continue;
                                    }
                                    if (screen_id.equals("b")) {
                                        break;
                                    }
                                    int screen_id_num;
                                    try {
                                        screen_id_num = Integer.parseInt(screen_id);
                                    } catch (NumberFormatException e) {
                                        System.out.println("\nPlease enter correct number");
                                        continue;
                                    }

                                    System.out.println("\nPlease enter screen size (b.back)");
                                    String screen_size = reader.nextLine();
                                    if (screen_size == null) {
                                        System.out.println("\nPlease enter correct number");
                                        continue;
                                    }
                                    if (screen_size.equals("b")) {
                                        break;
                                    }

                                    System.out.println("\nPlease enter front seat number (b.back)");
                                    String front = reader.nextLine();
                                    if (front == null) {
                                        System.out.println("\nPlease enter correct number");
                                        continue;
                                    }
                                    if (front.equals("b")) {
                                        break;
                                    }
                                    int front_num;
                                    try {
                                        front_num = Integer.parseInt(front);
                                    } catch (NumberFormatException e) {
                                        System.out.println("\nPlease enter correct number");
                                        continue;
                                    }

                                    System.out.println("\nPlease enter middle seat number (b.back)");
                                    String middle = reader.nextLine();
                                    if (middle == null) {
                                        System.out.println("\nPlease enter correct number");
                                        continue;
                                    }
                                    if (middle.equals("b")) {
                                        break;
                                    }
                                    int middle_num;
                                    try {
                                        middle_num = Integer.parseInt(middle);
                                    } catch (NumberFormatException e) {
                                        System.out.println("\nPlease enter correct number");
                                        continue;
                                    }


                                    System.out.println("\nPlease enter back seat number (b.back)");
                                    String back = reader.nextLine();
                                    if (back == null) {
                                        System.out.println("\nPlease enter correct number");
                                        continue;
                                    }
                                    if (back.equals("b")) {
                                        break;
                                    }
                                    int back_num;
                                    try {
                                        back_num = Integer.parseInt(back);
                                    } catch (NumberFormatException e) {
                                        System.out.println("\nPlease enter correct number");
                                        continue;
                                    }
                                    if (!manager.setScreeningRoom(screen_id_num, screen_size, front_num, middle_num,
                                            back_num, cinema)) {
                                        continue;
                                    } else {
                                        System.out.println("Screening is edited successfully");
                                        break;
                                    }
                                }
                            } else if (cho.equals("b")) {
                                break;
                            }else {
                                System.out.println("\nPlease enter correct number");
                                continue;
                            }
                        }
                    } else {
                        System.out.println("\nPlease enter correct number");
                        continue;
                    }
                }
            }
        }

    }

    public boolean transaction(Calendar transaction_time, String user_id, String reason, int successful) {
        if (transaction_time == null || user_id == null || reason == null) {
            System.out.println("null false");
            return false;
        }
        if (successful != 0 && successful != 1) {
            System.out.println("01 false");
            return false;
        }
        if (reason.length() > 100) {
            return false;
        }
        if (user_id.equals("") || (user_id.length() != 9)) {
            System.out.println("account limit 9 char");
            return false;
        }
        try {
            Connection con = DBUtils.getCon(database);
            Statement stmt = con.createStatement();

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            String sql = String.format(
                    "PRAGMA foreign_keys = ON; INSERT INTO Transactions (transaction_time,user_id,reason,successful) VALUES ('%s','%s', '%s', %d);",
                    df.format(transaction_time.getTime()), user_id, reason, successful);
            stmt.executeUpdate(sql);
            stmt.close();
            con.close();
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
        return true;
    }

    public Calendar toCalender(String dateString) {
        String[] date = dateString.split(" ");
        String[] ymd = date[0].split("-");
        String[] hms = date[1].split(":");

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, Integer.valueOf(ymd[0]));
        cal.set(Calendar.MONTH, Integer.valueOf(ymd[1]) - 1);
        cal.set(Calendar.DATE, Integer.valueOf(ymd[2]));
        cal.set(Calendar.HOUR_OF_DAY, Integer.valueOf(hms[0]));
        cal.set(Calendar.MINUTE, Integer.valueOf(hms[1]));
        cal.set(Calendar.SECOND, Integer.valueOf(hms[2]));
        return cal;

    }

    public boolean giftcardPay(String cardnum, String user_id) {
        if (cardnum == null || user_id == null) {
            System.out.println("payment null false");
            return false;
        }
        String[] num = cardnum.split("");
        if (cardnum.equals("") || (cardnum.length() != 18) || (!num[16].equals("G")) || (!num[17].equals("C"))) {
            System.out.println("card number limit 18 char");
            return false;
        }
        if (user_id.equals("") || (user_id.length() != 9)) {
            System.out.println("account limit 9 char");
            return false;
        }
        try {
            Connection con = DBUtils.getCon(database);
            Statement stmt = con.createStatement();

            String sql = String.format("SELECT used FROM Giftcard WHERE giftcard_id = '%s'", cardnum);
            ResultSet rs = stmt.executeQuery(sql);
            if(rs.next()==false){
                System.out.println("This gift card does not exist");
                return false;
            }
            int used = rs.getInt("used");
            if (used == 0) {
                System.out.println("card is used");
                return false;
            } else {
                sql = String.format("PRAGMA foreign_keys = ON; UPDATE Giftcard SET used = 0 WHERE giftcard_id = '%s';",
                        cardnum);
                stmt.executeUpdate(sql);
            }
            rs.close();
            stmt.close();
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        System.out.println("Giftcard pay successfully");
        return true;
    }

    public boolean payment(String cardHolder, String cardnum, String user_id) {
        if (cardHolder == null || cardnum == null || user_id == null) {
            System.out.println("payment null false");
            return false;
        }
        if (cardnum.equals("") || (cardnum.length() != 16)) {
            System.out.println("card number limit 16 char");
            return false;
        }
        if (user_id.equals("") || (user_id.length() != 9)) {
            System.out.println("account limit 9 char");
            return false;
        }
        try {
            Connection con = DBUtils.getCon(database);
            Statement stmt = con.createStatement();
            String sql = "select * from Users where account=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, user_id);
            ResultSet rs = ps.executeQuery();
            boolean s = rs.next();
            if (!s) {
                System.out.println("account does not exist");

                con.close();
                return false;
            }
            sql = String.format("UPDATE Users SET credit_number = '%s' WHERE account = '%s';PRAGMA foreign_keys = ON;",
                    cardnum, user_id);
            stmt.executeUpdate(sql);

            stmt.close();
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        System.out.println("Card pay successfully");
        return true;
    }

    public String checkCard(String user_id){
        if (user_id == null){
            return null;
        }
        if (user_id.equals("") || (user_id.length() != 9)) {
            System.out.println("Account must be limit 9 char，register failed");
            return null;
        }
        String s;
        try {
            Connection con = DBUtils.getCon(database);
            Statement stmt = con.createStatement();


            String sql = String.format(
                    "SELECT credit_number FROM Users WHERE account = '%s';",user_id);

            ResultSet rs = stmt.executeQuery(sql);
            s = rs.getString("credit_number");
            if(s == null) {
                rs.close();
                stmt.close();
                con.close();
                return null;

            }
            rs.close();
            stmt.close();
            con.close();
        } catch (Exception e) {

            System.out.println("Exception false"+ e);
            return null;
        }
        return s;

    }
    double calculator(String type, String screen_size){
        double percent = 0;

        if (type.equals("Child")){
            percent = this.child_per;
        } else if (type.equals("Student")){
            percent = this.student_per;
        } else if (type.equals("Adult")){
            percent = this.adult_per;
        } else if (type.equals("Senior")){
            percent = this.senior_per;
        }

        if (screen_size.equals("Gold")){
            percent *= this.gold_per;
        } else if (screen_size.equals("Silver")) {
            percent *= this.silver_per;
        } else if (screen_size.equals("Bronze")) {
            percent *= this.bronze_per;
        }

        return this.normal_price * percent;
    }

    public String getScreenSize(int screen_id, String cinema){
        if(cinema == null || cinema.equals("")){
            return null;
        }
        try {
            Connection con = DBUtils.getCon(database);
            Statement stmt = con.createStatement();

            String sql = String.format(
                    "SELECT screen FROM Screening WHERE id = %d AND cinema = '%s';", screen_id, cinema);
            ResultSet rs = stmt.executeQuery(sql);
            String size = rs.getString("screen");
            rs.close();
            stmt.close();
            con.close();
            return size;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public void printCinema() {
        try {
            Connection con = DBUtils.getCon(database);
            Statement stmt = con.createStatement();

            String sql = "SELECT DISTINCT cinema FROM Screening;";
            ResultSet rs = stmt.executeQuery(sql);
            String total = "";
            while ( rs.next() ) {
                String cinema = rs.getString("cinema");
                cinema += ", ";
                total += cinema;
            }
            total = total.substring(0, total.length() - 2);
            total += " (b.back)";
            System.out.println(total);
            rs.close();
            stmt.close();
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

}