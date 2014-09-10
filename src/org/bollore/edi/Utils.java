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
    
	public static String formatInput(String input, Integer length_expected,
			String separator) throws EDIException {
		String result = "";
		
		
		//Integer length_input = (input.endsWith(separator)||(input.startsWith(separator)))?input.split(separator).length+1:input.split(separator).length;
		input=(input!=null)?" ".concat(input).concat(" "):input;
				
		Integer length_input =input.split(separator).length;
		
		if (length_input == length_expected) {
			result = input;
		} else if (length_input < length_expected) {
			result = input;

			for (int i = 0; i < length_expected - length_input; i++) {
				result = result.concat(separator).concat(" ");
			}
		} else if (length_input > length_expected) {
			throw new EDIException("Le nombre d'élément (" + length_input
					+ ") est supérieur au nombre d'éléments attendus ("
					+ length_expected + ")");
		}
		
		return result;
		
	}
    
    public static void main(String[] args) {
    	System.out.println(isDate("12:24:13","hh:mm:ss"));
    	
    	System.out.println(getCurrentDate("dd/MM/yyyy HH:mm:ss"));
    	System.out.println(getCurrentDate("yyMMdd"));
    	System.out.println(getCurrentDate("HHmmss"));
    	
    }

}
