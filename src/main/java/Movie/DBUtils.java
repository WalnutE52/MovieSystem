package Movie;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;


public class DBUtils {
    public static Connection getCon(String dbfile) {
        Connection con = null;
        try {
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:src/main/resources/" + dbfile);
        } catch (Exception e) {
            System.out.println(e);
        }
        return con;
    }
    /**
     * create csv File1
     *
     * @param exportData
     * @param outPutPath
     * @param fileName
     * @return
     */
    public static File createCSVFile(List<List<String>> exportData, String outPutPath, String fileName) {
        File csvFile = null;
        BufferedWriter csvFileOutputStream = null;
        try {
            File file = new File(outPutPath);
            if (!file.exists()) {
                file.mkdirs();
            }
            csvFile = File.createTempFile(fileName, ".csv", new File(outPutPath));
            csvFileOutputStream = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(csvFile), StandardCharsets.UTF_8), 1024);
            for (List<String> exportDatum : exportData) {
                writeRow(exportDatum, csvFileOutputStream);
                csvFileOutputStream.newLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (csvFileOutputStream != null) {
                    csvFileOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return csvFile;
    }

    /**
     * write row data
     *
     * @param row
     * @param csvWriter
     * @throws IOException
     */
    public static void writeRow(List<String> row, BufferedWriter csvWriter) throws IOException {
        int i = 0;
        for (String data : row) {
            csvWriter.write(data);
            if (i != row.size() - 1) {
                csvWriter.write(",");
            }
            i++;
        }
    }

    /**
     * delete single file
     *
     * @param filePath
     * @param startFileName
     */
    public static void deleteFile(String filePath, String startFileName) {
        File file = new File(filePath);
        if (file.exists()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isFile()) {
                    if (files[i].getName().startsWith(startFileName)) {
                        files[i].delete();
                        return;
                    }
                }
            }
        }
    }

    /**
     * return single file name from prefix
     *
     * @param filePath
     * @param startFileName
     */
    public static String returnFileNameWithPrefix(String filePath, String startFileName) {
        File file = new File(filePath);
        if (file.exists()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isFile()) {
                    if (files[i].getName().startsWith(startFileName)) {
                        return files[i].getName();
                    }
                }
            }
        }
        return "";
    }

    public static String getWeekTime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
        Calendar cld = Calendar.getInstance();
        cld.setFirstDayOfWeek(Calendar.MONDAY);
        cld.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        return df.format(cld.getTime());
    }

    public static String getCurrentTime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = df.format(Calendar.getInstance().getTime());
        return time;
    }

//    public static void main(String[] args) {
//        System.out.println(returnFileNameWithPrefix("src/main/resources/","Movie"));
//    }
    /**
     * Input encryption method 2
     * <p>
     * U can use eg:
     * readPassword("Please enter your user id (b for go back)");
     *
     * @param prompt
     * @return password or user id
     */
    public static String readPasswordAndAccount(String prompt) {
        EraserThread et = new EraserThread(prompt);
        Thread mask = new Thread(et);
        mask.start();

        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String password = "";

        try {
            password = in.readLine();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        et.stopMasking();
        return password;
    }
    
}


class EraserThread implements Runnable {
    private boolean stop;

    public EraserThread(String prompt) {
        System.out.println(prompt);
    }

    @Override
    public void run() {
        while (!stop) {
            System.out.print("\010*");
            try {
                Thread.sleep(1);
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        }
    }

    public void stopMasking() {
        this.stop = true;
    }

}
