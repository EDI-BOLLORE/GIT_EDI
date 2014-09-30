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

				// Calcul du nombre d'�l�ments contenus dans la chaine de
				// caract�re

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
		// On traite le premier �l�ment � part
		result.add(input.substring(0, input.indexOf(separator)));

		String[] split = input.split(separator);
		for (int i = 1; i < split.length; i++) {
			result.add(split[i].replaceFirst(" ", ""));

		}
		}
		return result;
	}
	
	public static ArrayList<String> StringToArray(String input, String separator,Integer nb_items_expected) {
		ArrayList<String> result = StringToArray(input,separator);
		System.out.println("size "+result.size());
		if(nb_items_expected>result.size()) {
			
			System.out.println("loop  "+(nb_items_expected-result.size()));
			
			for (int i = 0; i < (nb_items_expected-result.size()); i++) {
				System.out.println(i+"add");
				result.add("");
			}
		}
		System.out.println("size "+result.size());
	
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

		}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public static void main(String[] args) {
//		String unb="UNOC:2+GRII?+MALDI+SNCUSTOMS+140916:1038+GFR0713+'";
//		String unb2="UNOC%2�GRII?MALDI�SNCUSTOMS�140916%1038�GFR0713�#";
//		String unh="UNH+123456?+789+CUSCAR:D:95B:UN'";
//
//		String[] regex_special= {"\\","+","?","[","]","(",")",".","*"};
//		
//		String segment_separator="#";
//		String element_separator="�";
//		String component_separator="%";
//		
//		
//		
//		String[] split=unb.split("(?<!\\?)\\+");
//		
//		String[] split2=unb2.split("(?<!\\?)"+element_separator);
//		
//		for (int i = 0; i < split.length; i++) {
//			System.out.println(split[i]);
//		}


			String input="A:B::C:";
			
			ArrayList<String> result=StringToArray(input, ":",10);
			
			for (int i = 0; i < result.size(); i++) {
				System.out.println(i+" "+result.get(i));
			}
	
		
		
		
	}
	


}
