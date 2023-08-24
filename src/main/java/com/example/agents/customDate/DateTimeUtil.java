package com.example.agents.customDate;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateTimeUtil {

    public long getCurrentTimestap() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        return timestamp.getTime();

    }

    public long get1hrBeforeTimestamp() {
        Calendar calc = Calendar.getInstance();
        calc.add(Calendar.MINUTE, -15);

        return calc.getTimeInMillis();

    }

    public String convertIST2UTC(String l_DtStr) {
        String formattedDateString ="";
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
            formatter.setTimeZone(TimeZone.getTimeZone("IST"));
            SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
            formatter1.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = formatter.parse(l_DtStr);
            //			System.out.println("Date: "+date);
            formattedDateString = formatter1.format(date);
            //			System.out.println(formattedDateString);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return formattedDateString;
    }

    public String convertUTC2IST(String l_DtStr) {
        String formattedDateString ="";
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH); //2023-02-06 11:20:00
            formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
            SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
            formatter1.setTimeZone(TimeZone.getTimeZone("IST"));
            Date date = formatter.parse(l_DtStr);
            //			System.out.println("Date: "+date);
            formattedDateString = formatter1.format(date);
            //			System.out.println(formattedDateString);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return formattedDateString;
    }


    public static Date localToGMT(String start_Date) {
        Date date = new Date();
        System.out.println(date);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date gmt = new Date(sdf.format(start_Date));
        return gmt;
    }

    public long convertLongDate(String strDate) {
        long unixTime = 0;
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = dateFormat.parse(strDate );
            unixTime = (long) date.getTime();
            System.out.println(unixTime );
        } catch (Exception e) {
            e.printStackTrace();
        }
        return unixTime;

    }

    public String dateFormat(String l_date) {
        String dt1 ="";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");//2023-02-09 00:00:00
            Date dt = sdf.parse(l_date);
            System.out.println("dt: "+dt);
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//2023-01-19 11:15:00
            dt1 = sdf1.format(dt);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return dt1;
    }


    public static void main(String[] args) {
        DateTimeUtil dateTime = new DateTimeUtil();
//		System.out.println(new DateTimeUtil().getCurrentTimestap());
//		System.out.println(new DateTimeUtil().get1hrBeforeTimestamp());
//		//System.out.println(new DateTimeUtil().convertIST2UTC("2022-09-26 09:45:00"));
//		System.out.println(new DateTimeUtil().convertIST2UTC("2022-09-29 10:00:00"));
//		dateTime.convertLongDate("2022-12-08 11:45:00");
        System.out.println(dateTime.dateFormat("2023-02-08 00:00:00"));

    }
}
