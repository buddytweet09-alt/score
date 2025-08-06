package com.footballscore.liveapp.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtils {
    
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private static final SimpleDateFormat DISPLAY_DATE_FORMAT = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm", Locale.getDefault());
    private static final SimpleDateFormat FULL_DATE_FORMAT = new SimpleDateFormat("EEEE, MMM dd, yyyy", Locale.getDefault());
    private static final SimpleDateFormat MATCH_TIME_FORMAT = new SimpleDateFormat("HH:mm", Locale.getDefault());

    public static String getCurrentDate() {
        return DATE_FORMAT.format(new Date());
    }

    public static String formatDateForDisplay(String dateString) {
        try {
            Date date = DATE_FORMAT.parse(dateString);
            return DISPLAY_DATE_FORMAT.format(date);
        } catch (Exception e) {
            return dateString;
        }
    }

    public static String formatMatchTime(long timestamp) {
        try {
            Date date = new Date(timestamp);
            return MATCH_TIME_FORMAT.format(date);
        } catch (Exception e) {
            return "";
        }
    }

    public static String formatTime(long timestamp) {
        try {
            Date date = new Date(timestamp);
            return TIME_FORMAT.format(date);
        } catch (Exception e) {
            return "";
        }
    }

    public static String formatFullDate(long timestamp) {
        try {
            Date date = new Date(timestamp);
            return FULL_DATE_FORMAT.format(date);
        } catch (Exception e) {
            return "";
        }
    }

    public static boolean isToday(String dateString) {
        return getCurrentDate().equals(dateString);
    }

    public static String getDateFromTimestamp(long timestamp) {
        try {
            Date date = new Date(timestamp);
            return DATE_FORMAT.format(date);
        } catch (Exception e) {
            return getCurrentDate();
        }
    }
}