package org.bollore.edi;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Utils {

	public static Date getCurrentDate() {
		return Calendar.getInstance().getTime();
	}

	public static String getCurrentDate(String pattern) {
		return formatDate(pattern, Calendar.getInstance().getTime());
	}

	public static String formatDate(String pattern, Date date) {
		SimpleDateFormat formater = new SimpleDateFormat(pattern);

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
		String result = null;
		
		try {
			if(input==null||length_expected==null||separator==null) {
				return result;
			}
			
			input=input.trim();
			
			if (!input.contains(separator)) {
				result = input.trim();
			} else {

				// J'ai ajouté des espaces car sinon le split ne prend pas en compte
				// la valeur vide
				// Ex: "A,B,C," le split considère 3 valeurs au lieu de 4 pour
				// "A,B,C, "
				input = (input != null) ? " ".concat(input).concat(" ") : input;

				Integer length_input = input.split(separator).length;

				if (length_input == length_expected) {
					result = input;
				} else if (length_input < length_expected) {
					result = input;

					for (int i = 0; i < length_expected - length_input; i++) {
						result = result.concat(separator).concat(" ");
					}
					//result=result.concat(" ");
				} else if (length_input > length_expected) {
					throw new EDIException("Le nombre d'élément (" + length_input
							+ ") est supérieur au nombre d'éléments attendus ("
							+ length_expected + ")");
				}
			}
			
		} catch (Exception e) {
			result="";
		}
		


		return result;

	}

	public static void main(String[] args) throws EDIException {
		System.out.println("A"+Utils.formatInput("  1,2,3",3,",")+"A");
	}

}
