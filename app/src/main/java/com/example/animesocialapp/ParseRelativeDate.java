package com.example.animesocialapp;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

import timber.log.Timber;

public class ParseRelativeDate {

    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;

    public static String getRelativeTimeAgo(String rawDate) {
        String format = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(format, Locale.ENGLISH);
        sf.setLenient(true);

        try {
            long time = sf.parse(rawDate).getTime();
            long now = System.currentTimeMillis();

            final long diff = now - time;
            if (diff < MINUTE_MILLIS) {
                return "just now";
            } else if (diff < 2 * MINUTE_MILLIS) {
                return "1 minute ago";
            } else if (diff < 50 * MINUTE_MILLIS) {
                return diff / MINUTE_MILLIS + " minutes ago";
            } else if (diff < 90 * MINUTE_MILLIS) {
                return "1 hour ago";
            } else if (diff < 24 * HOUR_MILLIS) {
                return diff / HOUR_MILLIS + " hours ago";
            } else if (diff < 48 * HOUR_MILLIS) {
                return "1 day ago";
            } else {
                return diff / DAY_MILLIS + " days ago";
            }
        } catch (ParseException e) {
            Timber.i("getRelativeTimeAgo failed");
            e.printStackTrace();
        }

        return "";
    }

    public static String getRelativeSeasonYear(String rawDate) {
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss", Locale.ENGLISH);
        SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM", Locale.ENGLISH);
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.ENGLISH);

        Date date = null;


        try {
            date = sdf1.parse(rawDate);

            String month = monthFormat.format(date);
            String year = yearFormat.format(date);


            if (Arrays.asList("January", "February", "March").contains(month)){
                return "Winter " + year;
            } else if (Arrays.asList("April", "May", "June").contains(month)){
                return "Spring " + year;
            } else if (Arrays.asList("July", "August", "September").contains(month)){
                return "Summer " + year;
            } else {
                return "Fall " + year;
            }



        } catch (ParseException e) {
            e.printStackTrace();
        }

        return "";
    }

    public static String getYearRange(String rawDate) {
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss", Locale.ENGLISH);
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.ENGLISH);

        Date date = null;
        int year;

        try {
            date = sdf1.parse(rawDate);
            year = Integer.parseInt(yearFormat.format(date));

            if (year < 1960) {
                return "Before 1960";
            } else if (year < 1970){
                return "1960 - 1970";
            } else if (year < 1980){
                return "1970 - 1980";
            } else if (year < 1990){
                return "1980 - 1990";
            } else if (year < 1995){
                return "1990 - 1995";
            } else if (year < 2000){
                return "1995 - 2000";
            } else if (year < 2005){
                return "2000 - 2005";
            } else if (year < 2010){
                return "2005 - 2010";
            } else if (year < 2015){
                return "2010 - 2015";
            } else if (year < 2020){
                return "2015 - 2020";
            } else if (year < 3000){
                return "Current";
            }

        } catch (ParseException e) {

        }


        return "";
    }
    
}
