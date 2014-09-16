package org.bollore.edi.tests;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import org.bollore.edi.EDIException;
import org.bollore.edi.Utils;
import junit.framework.TestCase;

public class UtilsTest extends TestCase {
	// Calendar cal=Calendar.getInstance();
	// cal.

	public static String fileseparator = System.getProperty("file.separator");
	public static String tempdir = System.getProperty("java.io.tmpdir")
			.concat(fileseparator).concat("EDI").concat(fileseparator);
	public static Date date;

	static {
		Calendar cal = Calendar.getInstance();
		cal.set(2014, 1, 1, 1, 1, 1);
		date = cal.getTime();

		// File file=new File(tempdir);
		// if(!file.exists()) {
		// file.cr
	}
	
	public void testPatternCheck(){
		
		assertTrue(Utils.PatternCheck("ext.nuno.francisco@bollore.fr","^[a-z\\.]+@[a-z]+\\.[a-z]{2,4}$"));
		assertFalse(Utils.PatternCheck(null,"^\\w+@[a-z]+\\.[a-z]{2,4}$"));
		assertFalse(Utils.PatternCheck("A",null));
		assertFalse(Utils.PatternCheck(null,null));
	}
	
	public void testPatternExtract(){
		
		//assertEquals("ext.nuno.francisco@bollore.com",Utils.PatternExtract("126ext.nuno.francisco@bollore.com167823:/","[A-Za-z\\.]+[@]{1}[A-Za-z\\.]+",null));
	}

	public void testFormatInput() throws EDIException {
		assertNull(Utils.formatInput(null, 1, ","));
		assertNull(Utils.formatInput("1", null, ","));
		assertNull(Utils.formatInput("1", 1, null));
		assertEquals("1", Utils.formatInput("1", 1, ","));
		assertEquals("1", Utils.formatInput("1   ", 1, ","));
		assertEquals("1", Utils.formatInput("    1   ", 1, ","));
		assertEquals("1,,", Utils.formatInput("1", 3, ","));
		assertEquals("1,2,3", Utils.formatInput("1,2,3", 3, ","));
		assertEquals("1,2,3", Utils.formatInput("    1,2,3   ", 3, ","));
		assertEquals("1,2,3,", Utils.formatInput("    1,2,3,   ", 4, ","));
		assertEquals("1,2,3,", Utils.formatInput("    1,2,3,   ", 4, ","));
		assertEquals("1,2,3,,", Utils.formatInput("1,2,3", 5, ","));
	}

	public void testisDate() {

		assertEquals(true, Utils.isDate("12:24:13", "hh:mm:ss"));
		assertFalse(Utils.isDate(null, "hh:mm:ss"));
		assertFalse(Utils.isDate("12:24:13", null));
		assertFalse(Utils.isDate(null, null));
	}

	public void testcheckSum() throws EDIException, NoSuchAlgorithmException,
			IOException {
		File temp = new File(UtilsTest.tempdir.concat("test.md5"));

		if (temp.exists()) {
			temp.delete();
			temp.createNewFile();
		} else {
			temp.createNewFile();
		}

		FileWriter fw = new FileWriter(temp, true);

		BufferedWriter output = new BufferedWriter(fw);
		output.write("Test du MD5");
		output.flush();
		output.close();

		assertEquals("73359b10d4d1761127fefbb0d04c03a7",
				Utils.checkSum(temp.getAbsolutePath()));
		temp.delete();
	}

	public void testStringToArray() {
		ArrayList<String> result = new ArrayList<String>();
		result.add(" ");
		result.add("B");
		result.add("C");
		result.add(" D");
		result.add("");
		result.add("");
		result.add("");

		assertEquals(result, Utils.StringToArray(" ,B,C, D,,,", ","));

		result.clear();

		result.add("");
		result.add("B");
		result.add("C");
		result.add(" D");
		result.add("");
		result.add("");
		result.add("  ");


		assertEquals(result, Utils.StringToArray(",B,C, D,,,  ", ","));
		assertEquals(result, Utils.StringToArray("%B%C% D%%%  ", "%"));
		
		result.clear();		
		result.add("1");

		ArrayList<String> temp=Utils.StringToArray("1", ",");

		assertEquals(result, Utils.StringToArray("1", ","));
		
		result.clear();
		result.add("");
		
		assertEquals(result, Utils.StringToArray(null, ","));
		assertEquals(result, Utils.StringToArray(null, null));
		assertEquals(result, Utils.StringToArray("Blabla",null));
		
		result.clear();
		result.add("ACE");
		
		assertEquals(result, Utils.StringToArray("ACE", ","));

	}

	public static void main(String[] args) throws IOException,
			NoSuchAlgorithmException {

		String A = " ,B,C, D,,,";

		ArrayList<String> result = new ArrayList<String>();

		// Integer nb=1;
		//
		// java.util.regex.Pattern pattern=Pattern.compile(",");
		// java.util.regex.Matcher matcher = pattern.matcher(A);
		//
		// while (matcher.find()) {
		// nb++;
		// }

		A = A.replaceAll(",", ", ");

		result.add(A.substring(0, A.indexOf(",")));

		String[] split = A.split(",");
		for (int i = 1; i < split.length; i++) {
			result.add(split[i].replaceFirst(" ", ""));
			// System.out.println("A"+split[i].replaceFirst(" ", "")+"A");
		}

		for (int i = 0; i < result.size(); i++) {
			System.out.println("A" + result.get(i) + "A");
		}

	}

}
