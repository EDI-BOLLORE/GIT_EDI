package org.bollore.edi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.text.SimpleDateFormat;

public class Utils {

	public static Date getCurrentDate() {
		return Calendar.getInstance().getTime();
	}

	public static String getCurrentDate(String pattern) {
		return formatDate(pattern, Calendar.getInstance().getTime());
	}
	
	public static Date parseDate(String pattern,String input) {
		Date result=null;
		
		try {
			DateFormat sdf=new SimpleDateFormat(pattern);
			result=sdf.parse(input);
		} catch (ParseException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
				
		return result;
		
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

	public static String formatInput(String input,
			Integer nb_elements_expected, String separator) throws EDIException {
		String result = null;

		try {
			if (input == null || nb_elements_expected == null
					|| separator == null) {
				return result;
			}

			input = input.trim();

			if (!input.contains(separator) && nb_elements_expected == 1) {
				result = input.trim();
			} else {

				// Calcul du nombre d'éléments contenus dans la chaine de
				// caractère

				Integer nb_elements = 1;

				java.util.regex.Pattern pattern = Pattern.compile(separator);
				java.util.regex.Matcher matcher = pattern.matcher(input);

				while (matcher.find()) {
					nb_elements++;
				}

				if (nb_elements == nb_elements_expected) {
					result = input;
				} else if (nb_elements < nb_elements_expected) {
					result = input;

					for (int i = 0; i < nb_elements_expected - nb_elements; i++) {
						result = result.concat(separator);
					}

				} else if (nb_elements > nb_elements_expected) {
					throw new EDIException("Le nombre d'element ("
							+ nb_elements
							+ ") est superieur au nombre d'elements attendus ("
							+ nb_elements_expected + ")");
				}
			}

		} catch (Exception e) {
			result = "";
		}
		return result;
	}

	public static String checkSum(String path) throws NoSuchAlgorithmException,
			IOException {

		MessageDigest md = MessageDigest.getInstance("md5");
		FileInputStream fis = new FileInputStream(path);
		byte[] dataBytes = new byte[1024];

		int nread = 0;

		while ((nread = fis.read(dataBytes)) != -1) {
			md.update(dataBytes, 0, nread);
		}
		;

		byte[] mdbytes = md.digest();

		// convert the byte to hex format
		StringBuffer sb = new StringBuffer("");
		for (int i = 0; i < mdbytes.length; i++) {
			sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16)
					.substring(1));
		}

		// System.out.println("Digest(in hex format):: " + sb.toString());

		return sb.toString();
	}

	public static ArrayList<String> StringToArray(String input, String separator) {
		ArrayList<String> result = new ArrayList<String>();
		
		if(input==null||separator==null) {
			result.add("");
			return result;
		} else if(input.trim().equals("")) {
			result.add("");
			return result;
		} else if(!input.contains(separator)) {

			result.add(input);
			return result;
		} else {

		input = input.replaceAll(separator, separator.concat(" "));
		// On traite le premier élément à part
		result.add(input.substring(0, input.indexOf(separator)));

		String[] split = input.split(separator);
		for (int i = 1; i < split.length; i++) {
			result.add(split[i].replaceFirst(" ", ""));

		}
		}
		return result;
	}
	
	public static Boolean PatternCheck(String input,String regex){
		Boolean result=null;
		
		try {
		Pattern pattern=Pattern.compile(regex);
		Matcher matcher=pattern.matcher(input);
		
		result=matcher.matches();
		} catch(Exception e) {
			result=false;
		}
		
		return result;
	}
	
	public static ArrayList<String> PatternExtract(String input,String regex,Integer group_rank){
		ArrayList<String> result=new ArrayList<String>();
		
		try {
		Pattern pattern=Pattern.compile(regex);
		Matcher matcher=pattern.matcher(input);
		
		while(matcher.find()) {
			result.add(matcher.group());
			//System.out.println("AA    "+texte);
//		if(group_rank==null) {
//			result=matcher.group();
//		} else {
//			result=matcher.group(group_rank);
//		}
		}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public static void main(String[] args) {
		String unb="UNB+UNOC:2+GRIMALDI+SNCUSTOMS+140916:1038+GFR0713'";
		String unh="UNH+123456789+CUSCAR:D:95B:UN'";
		String regex="([A-Z0-9\\:]+)";
		ArrayList<String> A=Utils.PatternExtract(unb,regex,null);
		ArrayList<String> B=Utils.PatternExtract(unh,regex,null);
		
//		for (int i = 0; i < A.size(); i++) {
//			System.out.println(A.get(i));
//		}
		
		System.out.println(Utils.parseDate("dd/MM/yyyy HH:mm:ss","22/07/1977 03:45:56"));
		
	}

}
