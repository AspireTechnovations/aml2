package in.aspiretechnovations.aml.utils;

import android.util.Log;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Samsung on 8/19/2015.
 */
public class TimeUtil {
    private static final int SECOND = 1000;
    private static final int MINUTE = 60 * SECOND;
    private static final int HOUR = 60 * MINUTE;
    private static final int DAY = 24 * HOUR;


    public static String getTimeAgo(long time, String dt) {
        if (time < 1000000000000L) {
            time *= 1000;
        }

        long now = System.currentTimeMillis();
        if (time > now || time <= 0) {
            return null;
        }

        final long diff = now - time;
        if (diff <= SECOND) {

            return "just now";
        } if (diff < MINUTE) {

            return "just now";
        } else if (diff < 2 * MINUTE) {
            return "a minute ago";
        } else if (diff < 50 * MINUTE) {
            return diff / MINUTE + " minutes ago";
        } else if (diff < 90 * MINUTE) {
            return "an hour ago";
        } else if (diff < 24 * HOUR) {
            return diff / HOUR + " hours ago";
        } else if (diff < 48 * HOUR) {
            return "yesterday";
        } else {
            if (diff / DAY <= 6) {
                return diff / DAY + " days ago";
            } else {
                return dateConversion(dt);
            }

        }
    }

    public static long findDifference(String inDate) {
        long diff = -1;
        if (inDate != null) {
            //2017-10-24 18:26:36
            SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                Date date1 = myFormat.parse(inDate);
                diff = date1.getTime();
            } catch (ParseException e) {
                Log.e("Date Exception", e.toString());
            }
        }
        return diff;
    }

    public static String dateConversion(String inDate) {
        String outDate = null;
        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat output = new SimpleDateFormat("d MMM yyyy");
        try {
            Date date = input.parse(inDate);   // parse input
            outDate = output.format(date);    // format output
        } catch (ParseException e) {
            Log.e("Date Exception", e.toString());
        }
        return outDate;
    }

    public static String dateConversionByFormat(String inDate) {
        String outDate = null;
        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat output = new SimpleDateFormat("d-MMMM ");
        try {
            Date date = input.parse(inDate);   // parse input
            outDate = output.format(date);    // format output
        } catch (ParseException e) {
            Log.e("Date Exception", e.toString());
        }
        return outDate;
    }

    public static String changeDateFormat(String inDate) {
        String outDate = null;
        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat output = new SimpleDateFormat("dd-MM-yyyy h:mm a");
        try {
            Date date = input.parse(inDate);   // parse input
            outDate = output.format(date);    // format output
        } catch (ParseException e) {
            Log.e("Date Exception", e.toString());
        }
        return outDate;
    }
    public static String dateConversionToTime(String inDate) {
        String outDate = null;
        SimpleDateFormat input = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        SimpleDateFormat output = new SimpleDateFormat("h:mm a");
        try {
            Date date = input.parse(inDate);   // parse input
            outDate = output.format(date);    // format output
        } catch (ParseException e) {
            Log.e("Date Exception", e.toString());
        }
        return outDate;
    }
    public static String dateConversionForReverse(String inDate) {
        String outDate = null;
        SimpleDateFormat input = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat output = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = input.parse(inDate);   // parse input
            outDate = output.format(date);    // format output
        } catch (ParseException e) {
            Log.e("Date Exception", e.toString());
        }
        return outDate;
    }

    public static String getCurrentDateTime() {

       // new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss").format(new Date());
        String date =  new SimpleDateFormat("dd-MMM-yyyy").format(new Date());
        String time = new SimpleDateFormat("HH:mm:ss").format(new Date());

        return date+" | "+time;
    }


    public static String getPreviousDate(int i) {


        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(getFormated(i));

    }


    private static Date getFormated(int i) {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, i);
        return cal.getTime();
    }



    public static String getCurrentDate() {

        // new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss").format(new Date());
        String date =  new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        return date;
    }

    public static String getCurrentTime() {

        // new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss").format(new Date());
        String time = new SimpleDateFormat("HH:mm:ss").format(new Date());

        return time;
    }


    public static int dateChecker(String inDate){
        int ans = 0;
        if(inDate != null){
            String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date1 = null, date2 = null;
            try {
                date1 = sdf.parse(currentDate);
                date2 = sdf.parse(inDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            ans = date2.compareTo(date1);
        }
        return ans;
    }

    public static Timestamp getTimestampFromString(String time_string){
        Timestamp timestamp = null;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date parsedDate = dateFormat.parse(time_string);
            timestamp = new Timestamp(parsedDate.getTime());
        } catch(Exception e) { //this generic but you can control another types of exception
            // look the origin of excption
        }

        return timestamp;

    }


    public static long convertMinutesToMilliSeconds(int min){

        int milli_sec = min*60*1000;

        return Long.valueOf(milli_sec);


    }


    public static String getCurrentDateWesternFormat() {

        // new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss").format(new Date());
        String date =  new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        return date;
    }





}
