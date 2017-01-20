package com.flickr.imgallery;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;


public class Utils  {
    /**
     * Globalized list for access
     **/
    public static List<FlickrModel> mFeeds = new ArrayList<>();
    /**
     * Convert String into Calendar
     **/
    public static Calendar toCalendar(final String iso8601string)
            throws ParseException {
        Calendar calendar = GregorianCalendar.getInstance();
        String s = iso8601string.replace("Z", "+00:00");
        try {
            s = s.substring(0, 22) + s.substring(23);  // to get rid of the ":"
        } catch (IndexOutOfBoundsException e) {
            throw new ParseException("Invalid length", 0);
        }
        Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").parse(s);
        calendar.setTime(date);
        return calendar;
    }
    /**
     * Convert Calendar into String
     **/
    public static String formattedDated(Calendar calendar){
        Date date = calendar.getTime();
        String formatted = new SimpleDateFormat("dd-MM-yyyy hh:mm a")
                .format(date);
        return formatted;
//        return formatted.substring(0, 22) + ":" + formatted.substring(22);
    }
    /**
     * Convert String into Date
     **/
    public static Date intoDate(String str){
        Date date = null;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        try {
            date = format.parse(str);
            System.out.println(date);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return date;
    }
}
