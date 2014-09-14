package org.bollore.edi.tests;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.bollore.edi.EDIException;
import org.bollore.edi.Edifact;
import org.bollore.edi.Utils;
//import org.junit.Rule;

import junit.*;
import junit.framework.*;


public class EdifactTest extends TestCase {	

	public void testReplaceGrammarChar() throws EDIException {
		
		String path=UtilsTest.tempdir.concat("Cuscar_Test1.edi");
		
		File temp=new File(path);
		
		if(temp.exists()){
			temp.delete();
		}
		
		//java.util.Date date=
		Calendar cal=Calendar.getInstance();
		cal.set(2014,1,1,1,1);
		Date date=cal.getTime();
		
		
		Edifact edi_cuscar =new Edifact(path,6,
				'+',':',' ','.','?','\'',"1234567", "D", "CUSCAR", "95", "B", "UN", "UNOC", "2",
				"GRIMALDI", "", "SNCUSTOMS", date,
				"identifiant de mon voyage");
		
		String input="A?B+C'D.E:F";
		String result="A??B?+C?'D?.E?:F";
		assertEquals(result, edi_cuscar.replaceGrammarChar(input));
		assertEquals("", edi_cuscar.replaceGrammarChar(null));		
	}
	
	public void testisGrammarCharValid() throws EDIException{
		Edifact edi_cuscar =new Edifact(UtilsTest.tempdir.concat("Cuscar_Test.edi"),6,
				'+',':',' ','.','?','\'',"1234567", "D", "CUSCAR", "95", "B", "UN", "UNOC", "2",
				"GRIMALDI", "", "SNCUSTOMS", Utils.getCurrentDate(),
				"identifiant de mon voyage");
		
		assertTrue(edi_cuscar.isGrammarCharValid());
		
		
	}
	
	public void testEdifactPrint() throws EDIException, NoSuchAlgorithmException, IOException{
		
		String path=UtilsTest.tempdir.concat("Cuscar_Test2.edi");
		
		File temp=new File(path);
		
		if(temp.exists()){
			temp.delete();
		}
		
		Calendar cal=Calendar.getInstance();
		cal.set(2014,1,1,1,1,0);
		Date date=cal.getTime();
		
		Edifact edi_cuscar =new Edifact(path,0,
				'+',':',' ','.','?','\'',"1234567", "D", "CUSCAR", "95", "B", "UN", "UNOC", "2",
				"GRIMALDI", "", "SNCUSTOMS", date,
				"identifiant de mon voyage");

		ArrayList<String> cst = new ArrayList<String>();

		cst.add("1");

		ArrayList<String> rff1 = new ArrayList<String>();

		rff1.add("A");
		rff1.add("B");
		rff1.add("C");
		rff1.add("D");

		ArrayList<String> rff2 = new ArrayList<String>();

		rff2.add("E");
		rff2.add("F");
		rff2.add("G");
		rff2.add("H");

		ArrayList<String> dtm = new ArrayList<String>();

		dtm.add("I");
		dtm.add("J");
		dtm.add("K");

		ArrayList<String> nad1 = new ArrayList<String>();

		nad1.add("NAD1");

		ArrayList<String> nad2 = new ArrayList<String>();

		nad2.add("NAD21");
		nad2.add("NAD22");
		nad2.add("NAD23");

		ArrayList<String> rng = new ArrayList<String>();

		rng.add("RNG?1");
		rng.add("RNG2");
		rng.add("RNG3");

		ArrayList<String> rng2 = new ArrayList<String>();

		rng2.add("RNG4");
		rng2.add("RNG5");
		rng2.add("RNG6");

		ArrayList<String> loc_3227 = new ArrayList<String>();

		loc_3227.add("5");

		ArrayList<String> loc_c517 = new ArrayList<String>();

		loc_c517.add("tata:");
		loc_c517.add(" 162");
		loc_c517.add("");
		loc_c517.add("");

		ArrayList<String> loc2_3227 = new ArrayList<String>();

		loc2_3227.add("5?");

		ArrayList<String> loc2_c517 = new ArrayList<String>();

		loc2_c517.add("toto");
		loc2_c517.add("139");
		loc2_c517.add("   ");
		loc2_c517.add("       ");

		// On peut utiliser l'un ou l'autre
		edi_cuscar.setValueElement("DTM/C507", "I1,  ,L1", ",", true);
		edi_cuscar.setValueElement("DTM/C507", dtm, true);
		edi_cuscar.setValueElement("RFF/C506", rff1, true);
		edi_cuscar.setValueElement("RFF/C506", rff2, true);

		edi_cuscar.setValueElement("RFF/C506", ",J,K,L", ",", true);

		edi_cuscar.setValueElement("RFF/C506", "M,N,O, ", ",", true);
		edi_cuscar.setValueElement("RFF/C506", "Q,R,S,", ",", true);
		edi_cuscar.setValueElement("RFF/C506", " ,T,U,V", ",", true);
		edi_cuscar.setValueElement("NAD/3035", nad1, true);
		edi_cuscar.setValueElement("NAD/C082", nad2, false);

		edi_cuscar.setValueElement("GRP1/LOC/3227", loc_3227, true);
		edi_cuscar.setValueElement("GRP1/LOC/C517", loc_c517, false);
		edi_cuscar.setValueElement("GRP1/LOC/3227", loc2_3227, true);
		edi_cuscar.setValueElement("GRP1/LOC/C517", loc2_c517, false);

		edi_cuscar.setValueElement("GRP2/GRP3/RNG/C280", rng, true);
		edi_cuscar.setValueElement("GRP2/GRP3/RNG/C280", rng2, true);
		edi_cuscar.setValueElement("GRP4/GRP5/GRP10/PCI/C210",
				"1,2,3,4,5,6,7,8,9,10", ",", true);

		edi_cuscar.setValueElement("GRP4/GRP5/GRP10/CST/1496", cst, true);
		// edi_cuscar.printEDI();

		 edi_cuscar.print();
		 //System.out.println(Utils.checkSum(path));
		 assertEquals("513607a92df5c18dbf3a1a0ce1f7f9e9",Utils.checkSum(path));
		
	}

}
