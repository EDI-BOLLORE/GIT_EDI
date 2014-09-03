package org.bollore.edi;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Utils {
	
	
	public static Date getCurrentDate(){
		return Calendar.getInstance().getTime();
	}
	
	public static String getCurrentDate(String pattern){
		return formatDate(pattern, Calendar.getInstance().getTime());
	}
	
	public static String formatDate(String pattern, Date date) {
		SimpleDateFormat formater=new SimpleDateFormat(pattern);
		
		return formater.format(date);
	}
	
	
    public static boolean isDate(String stringDate, String pattern) {

        if (stringDate == null) {
            return false;
        }
        if (pattern == null) {
            pattern = "yyyy-MM-dd HH:mm:ss";
        }

        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(pattern);
        java.util.Date testDate = null;

        try {
            testDate = sdf.parse(stringDate);
        } catch (Exception e) {
            return false;
        }

        if (!sdf.format(testDate).equalsIgnoreCase(stringDate)) {
            return false;
        }

        return true;
    }
    
    public static void main(String[] args) {
    	System.out.println(isDate("12:24:13","hh:mm:ss"));
    	
    	System.out.println(getCurrentDate("dd/MM/yyyy HH:mm:ss"));
    	System.out.println(getCurrentDate("yyMMdd"));
    	System.out.println(getCurrentDate("HHmmss"));
    	
    }

}
