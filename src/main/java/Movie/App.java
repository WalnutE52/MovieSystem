package Movie;
import java.io.FileReader;
import java.io.IOException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

class App {
    public static void main(String[] args) {
        try {
            JSONParser jsonParser = new JSONParser();
            JSONObject config = null;
            try {
                config = (JSONObject) jsonParser.parse(new FileReader("cinemaSystem.json"));
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
                    if (mode.equals("staff")) {
                        CinemaSystem cs = new CinemaSystem("cinema.db", false, normal_price,
                                percent_child, percent_student,
                                percent_adult, percent_senior, percent_gold,percent_silver,percent_bronze);
                        cs.stuffRun();
                    } else if (mode.equals("manager")) {
                        CinemaSystem cs = new CinemaSystem("cinema.db", true, normal_price,
                                percent_child, percent_student,
                                percent_adult, percent_senior, percent_gold,percent_silver,percent_bronze);
                        cs.stuffRun();
                    } else {
                        CinemaSystem cs = new CinemaSystem("cinema.db", false, normal_price,
                                percent_child, percent_student,
                                percent_adult, percent_senior, percent_gold,percent_silver,percent_bronze);
                        cs.customerRun();
                    }

                } catch (Exception e) {
                    System.out.println("Please enter correct price");
                }
            }
        } catch (Exception e) {
            System.out.println("System wrong");
        }
    }
    
}