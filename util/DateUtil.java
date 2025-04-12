package util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    
    public static Date parseDate(String dateStr) {
        try {
            return dateStr != null ? dateFormat.parse(dateStr) : null;
        } catch (ParseException e) {
            return null;
        }
    }
    
    public static String formatDate(Date date) {
        return date != null ? dateFormat.format(date) : "";
    }
}
