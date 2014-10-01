package org.bollore.edi.tests;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.bollore.edi.EDIException;
import org.bollore.edi.Edifact;
import org.bollore.edi.Message;
import org.bollore.edi.Segment;
import org.bollore.edi.Utils;
//import org.junit.Rule;








import junit.*;
import junit.framework.*;

public class EdifactTest extends TestCase {

	public void testReplaceGrammarChar() throws EDIException {

		String path = UtilsTest.tempdir.concat("Cuscar_Test1.edi");

		File temp = new File(path);

		if (temp.exists()) {
			temp.delete();
		}

		Edifact edi_cuscar = new Edifact(path, 6, '+', ':', ' ', '.', '?',
				'\'',"CUSCAR", "D95B", "UN", "UNOC", "2", "GRIMALDI",
				"", "SNCUSTOMS", UtilsTest.date, "identifiant de mon voyage");

		String input = "A?B+C'D.E:F";
		String result = "A??B?+C?'D?.E?:F";
		assertEquals(result, edi_cuscar.replaceGrammarChar(input));
		assertEquals("", edi_cuscar.replaceGrammarChar(null));
	}

	public void testisGrammarCharValid() throws EDIException {
		Edifact edi_cuscar = new Edifact(
				UtilsTest.tempdir.concat("Cuscar_Test.edi"), 6, '+', ':', ' ',
				'.', '?', '\'',"CUSCAR", "D95B", "UN", "UNOC", "2",
				"GRIMALDI", "", "SNCUSTOMS", UtilsTest.date,
				"identifiant de mon voyage");

		assertTrue(edi_cuscar.isGrammarCharValid());

	}
	/**
	 * Ce test permet de valider que l'arborescence de l'EDI que l'on va générer sera créer si elle n'existe pas
	 * @throws EDIException
	 */
	public void testCreateDir() throws EDIException {
		String fs=System.getProperty("file.separator");
		
		
		String random_dir_name=Utils.RandomString();
		
		String dir=UtilsTest.tempdir.concat(random_dir_name).concat(fs).concat("mon_fichier.edi");

		String message_reference_number = "1234";
		
		
		Edifact edi_cuscar = new Edifact(dir, 0, '+', ':', ' ', '.', '?',
				'\'',"CUSCAR", "D95B", "UN", "UNOC", "2", "GRIMALDI",
				"", "SNCUSTOMS", UtilsTest.date, "identifiant de mon voyage");
		
		edi_cuscar.setValueElement(message_reference_number, "DTM/C507","I1,  ,L1", ",", true);
		
		edi_cuscar.print();
		
		File file=new File(dir);
		
		assertTrue(file.exists());
		System.out.println(file.getParentFile().getAbsolutePath());
		file.delete();
		file.getParentFile().delete();
	}
	
	public void testEDIDefinitionDir() {
		
		String path = UtilsTest.tempdir.concat("Cuscar_Test2.edi");
		System.out.println(path);
		Edifact edi=new Edifact(path, "CUSCAR","D95B");
		
		System.out.println(edi.edi_type);
		
	}

	public void testEdifactPrint() throws EDIException,
			NoSuchAlgorithmException, IOException {

		String path = UtilsTest.tempdir.concat("Cuscar_Test2.edi");

		String message_reference_number = "1234";

		Edifact edi_cuscar = new Edifact(path, 0, '+', ':', ' ', '.', '?',
				'\'',"CUSCAR", "D95B", "UN", "UNOC", "2", "GRIMALDI",
				"", "SNCUSTOMS", UtilsTest.date, "identifiant de mon voyage");

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
		edi_cuscar.setValueElement(message_reference_number, "DTM/C507",
				"I1,  ,L1", ",", true);
		edi_cuscar.setValueElement(message_reference_number, "DTM/C507", dtm,
				true);
		edi_cuscar.setValueElement(message_reference_number, "RFF/C506", rff1,
				true);
		edi_cuscar.setValueElement(message_reference_number, "RFF/C506", rff2,
				true);

		edi_cuscar.setValueElement(message_reference_number, "RFF/C506",
				",J,K,L", ",", true);

		edi_cuscar.setValueElement(message_reference_number, "RFF/C506",
				"M,N,O, ", ",", true);
		edi_cuscar.setValueElement(message_reference_number, "RFF/C506",
				"Q,R,S,", ",", true);
		edi_cuscar.setValueElement(message_reference_number, "RFF/C506",
				" ,T,U,V", ",", true);
		edi_cuscar.setValueElement(message_reference_number, "NAD/3035", nad1,
				true);
		edi_cuscar.setValueElement(message_reference_number, "NAD/C082", nad2,
				false);

		edi_cuscar.setValueElement(message_reference_number, "GRP1/LOC/3227",
				loc_3227, true);
		edi_cuscar.setValueElement(message_reference_number, "GRP1/LOC/C517",
				loc_c517, false);
		edi_cuscar.setValueElement(message_reference_number, "GRP1/LOC/3227",
				loc2_3227, true);
		edi_cuscar.setValueElement(message_reference_number, "GRP1/LOC/C517",
				loc2_c517, false);

		edi_cuscar.setValueElement(message_reference_number,
				"GRP2/GRP3/RNG/C280", rng, true);
		edi_cuscar.setValueElement(message_reference_number,
				"GRP2/GRP3/RNG/C280", rng2, true);
		edi_cuscar.setValueElement(message_reference_number,
				"GRP4/GRP5/GRP10/PCI/C210", "1,2,3,4,5,6,7,8,9,10", ",", true);
		
		edi_cuscar.setValueElement(message_reference_number,
				"GRP4/GRP5/GRP10/CST/1496", cst, true);
		// edi_cuscar.printEDI();
		//System.out.println("MD5 "+Utils.checkSum(edi_cuscar.filepath));
		
		//J'ajoute 2 segments vides qui ne seront pas imprimés et donc le nb de segments ne doit pas être incrémenté.
 		edi_cuscar.messages.get(0).segments.add(new Segment());
		edi_cuscar.messages.get(0).segments.add(new Segment());
		
		edi_cuscar.print();
		
		assertEquals("5e19fd997dee39e97ffe224f68b24819", Utils.checkSum(edi_cuscar.filepath));

	}

	public void testgetMessage() throws EDIException {

		String path = UtilsTest.tempdir.concat("Tata.edi");

		Edifact edi_cuscar = new Edifact(path, 6, '+', ':', ' ', '.', '?',
				'\'', "CUSCAR", "D95B", "UN", "UNOC", "2", "GRIMALDI",
				"", "SNCUSTOMS", UtilsTest.date, "identifiant de mon voyage");

		assertTrue(edi_cuscar.messages.isEmpty());
		edi_cuscar.getMessage("Blabla");
		assertTrue(edi_cuscar.messages.size() == 1);
		// Le message a été créé. On vérifie que l'on parvient à le récupérer
		// sans ajout de message supplémentaire
		org.bollore.edi.Message message = edi_cuscar.getMessage("Blabla");
		assertTrue(edi_cuscar.messages.size() == 1);
		assertTrue("Blabla".equals(message.reference_number));
		edi_cuscar.getMessage("Blabla2");
		assertTrue(edi_cuscar.messages.size() == 2);

	}
	
	public void testMapSegmentsStructureInfo() throws EDIException {
		
		String path = UtilsTest.tempdir.concat("Tata.edi");
		
		Edifact edi_cuscar = new Edifact(path, 6, '+', ':', ' ', '.', '?',
				'\'',"CUSCAR", "D95B", "UN", "UNOC", "2", "GRIMALDI",
				"", "SNCUSTOMS", UtilsTest.date, "identifiant de mon voyage");
		
		HashMap<String,Segment> seg_def=edi_cuscar.buildStructureSegmentDefinition();
		
			Set<String> keyset=seg_def.keySet();
			
			Iterator<String> iterator=keyset.iterator();
		
		while (iterator.hasNext()) {
			String cur_seg=iterator.next();
			//System.out.println(cur_seg);
			
		}
		
	}

	public static void main(String[] args) throws EDIException {
		String path = UtilsTest.tempdir.concat("Cuscar_Test2.edi");

		Edifact edi=new Edifact(path, "CUSCAR","D95B");
		
		System.out.println(edi.edi_type);
		
		String message_reference_number = "1234";

		File temp = new File(path);

		if (temp.exists()) {
			temp.delete();
		}

		Calendar cal = Calendar.getInstance();
		cal.set(2014, 1, 1, 1, 1, 0);
		Date date = cal.getTime();
		
//		String filepath = UtilsTest.tempdir.concat("Cuscar_Test2.edi");
//		System.out.println(filepath);
//		Edifact edi_cuscar=new Edifact(filepath,"D", "CUSCAR",
//				"95","B");
//		System.out.println(edi_cuscar.EDIDefinitions_dir);
		

		Edifact edi_cuscar = new Edifact(path, 0, '+', ':', ' ', '.', '?',
				'\'',"CUSCAR", "D95B", "UN", "UNOC", "2", "GRIMALDI",
				"", "SNCUSTOMS", date, "identifiant de mon voyage");

		 Message message1=new Message(message_reference_number);
		 
		 HashMap<String, org.bollore.edi.Segment> seg_def=edi_cuscar.buildStructureSegmentDefinition();
		 
		 System.out.println(seg_def.get("DTM").name);

//		ArrayList<String> cst = new ArrayList<String>();
//
//		cst.add("1");
//
//		ArrayList<String> rff1 = new ArrayList<String>();
//
//		rff1.add("A");
//		rff1.add("B");
//		rff1.add("C");
//		rff1.add("D");
//
//		ArrayList<String> rff2 = new ArrayList<String>();
//
//		rff2.add("E");
//		rff2.add("F");
//		rff2.add("G");
//		rff2.add("H");
//
//		ArrayList<String> dtm = new ArrayList<String>();
//
//		dtm.add("I");
//		dtm.add("J");
//		dtm.add("K");
//
//		ArrayList<String> nad1 = new ArrayList<String>();
//
//		nad1.add("NAD1");
//
//		ArrayList<String> nad2 = new ArrayList<String>();
//
//		nad2.add("NAD21");
//		nad2.add("NAD22");
//		nad2.add("NAD23");
//
//		ArrayList<String> rng = new ArrayList<String>();
//
//		rng.add("RNG?1");
//		rng.add("RNG2");
//		rng.add("RNG3");
//
//		ArrayList<String> rng2 = new ArrayList<String>();
//
//		rng2.add("RNG4");
//		rng2.add("RNG5");
//		rng2.add("RNG6");
//
//		ArrayList<String> loc_3227 = new ArrayList<String>();
//
//		loc_3227.add("5");
//
//		ArrayList<String> loc_c517 = new ArrayList<String>();
//
//		loc_c517.add("tata:");
//		loc_c517.add(" 162");
//		loc_c517.add("");
//		loc_c517.add("");
//
//		ArrayList<String> loc2_3227 = new ArrayList<String>();
//
//		loc2_3227.add("5?");
//
//		ArrayList<String> loc2_c517 = new ArrayList<String>();
//
//		loc2_c517.add("toto");
//		loc2_c517.add("139");
//		loc2_c517.add("   ");
//		loc2_c517.add("       ");
//
//		// On peut utiliser l'un ou l'autre
//		edi_cuscar.setValueElement(message_reference_number, "DTM/C507",
//				"I1,  ,L1", ",", true);
//		edi_cuscar.setValueElement(message_reference_number, "DTM/C507", dtm,
//				true);
//		edi_cuscar.setValueElement(message_reference_number, "RFF/C506", rff1,
//				true);
//		edi_cuscar.setValueElement(message_reference_number, "RFF/C506", rff2,
//				true);
//
//		edi_cuscar.setValueElement(message_reference_number, "RFF/C506",
//				",J,K,L", ",", true);
//
//		edi_cuscar.setValueElement(message_reference_number, "RFF/C506",
//				"M,N,O, ", ",", true);
//		edi_cuscar.setValueElement(message_reference_number, "RFF/C506",
//				"Q,R,S,", ",", true);
//		edi_cuscar.setValueElement(message_reference_number, "RFF/C506",
//				" ,T,U,V", ",", true);
//		edi_cuscar.setValueElement(message_reference_number, "NAD/3035", nad1,
//				true);
//		edi_cuscar.setValueElement(message_reference_number, "NAD/C082", nad2,
//				false);
//
//		edi_cuscar.setValueElement(message_reference_number, "GRP1/LOC/3227",
//				loc_3227, true);
//		edi_cuscar.setValueElement(message_reference_number, "GRP1/LOC/C517",
//				loc_c517, false);
//		edi_cuscar.setValueElement(message_reference_number, "GRP1/LOC/3227",
//				loc2_3227, true);
//		edi_cuscar.setValueElement(message_reference_number, "GRP1/LOC/C517",
//				loc2_c517, false);
//
//		edi_cuscar.setValueElement(message_reference_number,
//				"GRP2/GRP3/RNG/C280", rng, true);
//		edi_cuscar.setValueElement(message_reference_number,
//				"GRP2/GRP3/RNG/C280", rng2, true);
//		edi_cuscar.setValueElement(message_reference_number,
//				"GRP4/GRP5/GRP10/PCI/C210", "1,2,3,4,5,6,7,8,9,10", ",", true);
//
//		edi_cuscar.setValueElement(message_reference_number,
//				"GRP4/GRP5/GRP10/CST/1496", cst, true);
//
//		edi_cuscar.print();
		
		
	}

}
