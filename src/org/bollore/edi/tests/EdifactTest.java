package org.bollore.edi.tests;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
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

import junit.*;
import junit.framework.*;

public class EdifactTest extends TestCase {

	public void testReplaceGrammarChar() throws EDIException, UnsupportedEncodingException {

		String path = UtilsTest.tempdir.concat("Cuscar_Test1.edi");

		File temp = new File(path);

		if (temp.exists()) {
			temp.delete();
		}

		Edifact edi_cuscar = new Edifact("\n",path,"Cp1252", 1, '+', ':', ' ', '.', '?',
				'\'',"CUSCAR", "D95B", "UN", "UNOC", "2", "GRIMALDI",
				"recipient_code_qualifier","sender_code_qualifier", "SNCUSTOMS", UtilsTest.date, "identifiant de mon voyage","CUSCAR");

		String input = "A?B+C'D.E:F";
		String result = "A??B?+C?'D.E?:F";
		assertEquals(result, edi_cuscar.replaceGrammarChar(input));
		assertEquals("", edi_cuscar.replaceGrammarChar(null));
	}

	public void testisGrammarCharValid() throws EDIException, UnsupportedEncodingException {
		Edifact edi_cuscar = new Edifact("\n",
				UtilsTest.tempdir.concat("Cuscar_Test.edi"),"Cp1252",6, '+', ':', ' ',
				'.', '?', '\'',"CUSCAR", "D95B", "UN", "UNOC", "2",
				"GRIMALDI","recipient_code_qualifier","sender_code_qualifier", "SNCUSTOMS", UtilsTest.date,
				"identifiant de mon voyage","CUSCAR");

		assertTrue(edi_cuscar.isGrammarCharValid());

	}
	/**
	 * Ce test permet de valider que l'arborescence de l'EDI que l'on va générer sera créer si elle n'existe pas
	 * @throws EDIException
	 * @throws UnsupportedEncodingException 
	 */
	public void testCreateDir() throws EDIException, UnsupportedEncodingException {
		String fs=System.getProperty("file.separator");
		
		
		String random_dir_name=Utils.RandomString();
		
		String dir=UtilsTest.tempdir.concat(random_dir_name).concat(fs).concat("mon_fichier.edi");

		String message_reference_number = "1234";
		
		
		Edifact edi_cuscar = new Edifact("\n",dir,"Cp1252",0, '+', ':', ' ', '.', '?',
				'\'',"CUSCAR", "D95B", "UN", "UNOC", "2", "GRIMALDI",
				"recipient_code_qualifier","sender_code_qualifier", "SNCUSTOMS", UtilsTest.date, "identifiant de mon voyage","CUSCAR");
		
		edi_cuscar.setValueElement(message_reference_number, "DTM/C507","I1,  ,L1", ",", true);
		
		//edi_cuscar.print();
		
		File file=new File(dir);
		
		assertTrue(file.exists());
		file.delete();
		file.getParentFile().delete();
	}
	
	public void testEDIDefinitionDir() {
		
		String path = UtilsTest.tempdir.concat("Cuscar_Test2.edi");
		System.out.println(path);
		Edifact edi=new Edifact(path, "CUSCAR","D95B");
		
	}

	public void testEdifactPrint() throws EDIException,
			NoSuchAlgorithmException, IOException {

		String path = UtilsTest.tempdir.concat("Cuscar_Test2.edi");

		String message_reference_number = "1234";

		Edifact edi_cuscar = new Edifact("\n",path,"Cp1252",1, '+', ':', ' ', '.', '?',
				'\'',"CUSCAR", "195B", "UN", "UNOC", "2", "sender_id"
				,"sender_code_qualifier","recipient_id","recipient_code_qualifier", UtilsTest.date, "identifiant de mon voyage","CUSCAR");

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

		//nad1.add("NAD1");
		nad1.add(null);

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
		
		ArrayList<String> ftx_c107 = new ArrayList<String>();
		
		ftx_c107.add("");
		ftx_c107.add("");
		ftx_c107.add("C107");
		
		ArrayList<String> gid_1496 = new ArrayList<String>();

		gid_1496.add("gid_1");
		
		ArrayList<String> gid_c213_1 = new ArrayList<String>();
		ArrayList<String> gid_c213_2 = new ArrayList<String>();
		ArrayList<String> gid_c213_3 = new ArrayList<String>();
		
		gid_c213_1.add("gid_11");
		gid_c213_1.add("gid_12");
		gid_c213_1.add("gid_13");
		gid_c213_1.add("gid_14");
		gid_c213_1.add("gid_15");
		
		gid_c213_2.add(" ");
		gid_c213_2.add(" ");
		gid_c213_2.add(" ");
		gid_c213_2.add(" ");
		gid_c213_2.add(" ");
		
		gid_c213_3.add("gid_31");
		gid_c213_3.add("gid_32");
		gid_c213_3.add("gid_33");
		gid_c213_3.add("gid_34");
		gid_c213_3.add("gid_35");
		


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
		
		// On peut utiliser l'un ou l'autre
		
		edi_cuscar.setValueElement(message_reference_number, "GRP4/GRP5/GRP10/GID/1496", gid_1496,
				true);
		
		gid_1496.clear();
		
		edi_cuscar.setValueElement(message_reference_number, "GRP4/GRP5/GRP10/GID/C213", gid_c213_1,
				false);
		
		gid_c213_1.clear();
		
		edi_cuscar.setValueElement(message_reference_number, "GRP4/GRP5/GRP10/GID/C213", gid_c213_2,
				false);
		
		edi_cuscar.setValueElement(message_reference_number, "GRP4/GRP5/GRP10/GID/C213", gid_c213_3,
				false);

		edi_cuscar.setValueElement(message_reference_number,
				"GRP2/GRP3/RNG/C280", rng, true);
		edi_cuscar.setValueElement(message_reference_number,
				"GRP2/GRP3/RNG/C280", rng2, true);
		edi_cuscar.setValueElement(message_reference_number,
				"GRP4/GRP5/GRP10/PCI/C210", "1,2,3,4,5,6,7,8,9,10", ",", true);
		
		edi_cuscar.setValueElement(message_reference_number,
				"GRP4/GRP5/GRP10/CST/1496", cst, true);
		
		edi_cuscar.setValueElement(message_reference_number,
				"FTX/C107", ftx_c107, true);

		
		//J'ajoute 2 segments vides qui ne seront pas imprimés et donc le nb de segments ne doit pas être incrémenté.
 		edi_cuscar.messages.get(0).segments.add(new Segment());
		edi_cuscar.messages.get(0).segments.add(new Segment());
		edi_cuscar.print();
		System.out.println(edi_cuscar.filepath);
		System.out.println(Utils.checkSum(edi_cuscar.filepath));
		
		//edi_cuscar.printEDI();
		
		assertEquals("11c6e929c46b0a4df53a8cb14bc6c975", Utils.checkSum(edi_cuscar.filepath));
		//assertEquals("efef32ac595a667560cf0947df08dc62", Utils.checkSum(edi_cuscar.filepath));
		
		
		

	}

	public void testgetMessage() throws EDIException, UnsupportedEncodingException {

		String path = UtilsTest.tempdir.concat("Tata.edi");
		//System.out.println(path);
		Edifact edi_cuscar = new Edifact("\n",path,"Cp1252", 6, '+', ':', ' ', '.', '?',
				'\'', "CUSCAR", "D95B", "UN", "UNOC", "2", "sender_id"
				,"sender_code_qualifier","recipient_id","recipient_code_qualifier", UtilsTest.date, "identifiant de mon voyage","CUSCAR");

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
	
	public void testMapSegmentsStructureInfo() throws EDIException, UnsupportedEncodingException {
		
		String path = UtilsTest.tempdir.concat("Tata.edi");
		
		Edifact edi_cuscar = new Edifact("\n",path,"Cp1252", 6, '+', ':', ' ', '.', '?',
				'\'',"CUSCAR", "D95B", "UN", "UNOC", "2", "GRIMALDI",
				"recipient_code_qualifier","sender_code_qualifier", "SNCUSTOMS", UtilsTest.date, "identifiant de mon voyage","CUSCAR");
		
		HashMap<String,Segment> seg_def=edi_cuscar.buildStructureSegmentDefinition();
		
			Set<String> keyset=seg_def.keySet();
			
			Iterator<String> iterator=keyset.iterator();
		
		while (iterator.hasNext()) {
			String cur_seg=iterator.next();
			
		}
		
	}

	public static void testparseGrammar() throws EDIException, UnsupportedEncodingException {
		String path = UtilsTest.tempdir.concat("testparseGrammar.edi");
		Edifact edi=new Edifact(path);
		assertNull(edi.component_separator);
		assertNull(edi.segment_separator);
		assertNull(edi.decimal_separator);
		assertNull(edi.element_separator);
		assertNull(edi.escape_character);
		//assertNull(edi.space_character);
		
		Edifact edi_cuscar = new Edifact("\n",path,"Cp1252", 0, '+', ':',' ', '.', '?',
				'\'',"CUSCAR", "D95B", "UN", "UNOC", "2", "GRIMALDI",
				"recipient_code_qualifier","sender_code_qualifier", "SNCUSTOMS", UtilsTest.date, "identifiant de mon voyage","CUSCAR");
		
		edi_cuscar.setValueElement("message_reference_number", "DTM/C507","I1,  ,L1", ",", true);
		
		edi_cuscar.print();
		assertEquals(String.valueOf(edi_cuscar.segment_separator),"'");
		assertEquals(String.valueOf(edi_cuscar.component_separator),":");
		assertEquals(String.valueOf(edi_cuscar.element_separator),"+");
		assertEquals(String.valueOf(edi_cuscar.space_character)," ");
		assertEquals(String.valueOf(edi_cuscar.decimal_separator),".");
		assertEquals(String.valueOf(edi_cuscar.escape_character),"?");
	}
	
	public static void testMessages() throws EDIException, UnsupportedEncodingException {
		
		// On crée la structure vide d'un cuscar
		String path = UtilsTest.tempdir.concat("testparseGrammar.edi");
		
		Edifact edi_cuscar = new Edifact("\n",path,"Cp1252", 0, '+', ':',' ', '.', '?',
				'\'',"CUSCAR", "D95B", "UN", "UNOC", "2", "GRIMALDI",
				"recipient_code_qualifier","sender_code_qualifier", "SNCUSTOMS", UtilsTest.date, "identifiant de mon voyage","CUSCAR");
		
		
		// On crée un message "message 1" avec un unique segment CST
		ArrayList<String> cst = new ArrayList<String>();

		cst.add("1");
		
		edi_cuscar.setValueElement("message 1",
				"GRP4/GRP5/GRP10/CST/1496", cst, true);

		// On crée un message "message 2" avec 2 segments RFF et DTM
		ArrayList<String> rff1 = new ArrayList<String>();

		rff1.add("A");
		rff1.add("B");
		rff1.add("C");
		rff1.add("D");
		
		edi_cuscar.setValueElement("message 2", "RFF/C506", rff1,
				true);
		
		ArrayList<String> dtm = new ArrayList<String>();

		dtm.add("I");
		dtm.add("J");
		dtm.add("K");
		
		edi_cuscar.setValueElement("message 2", "DTM/C507", dtm,
				true);

		// On valide que notre cuscar ne possède que 2 segments
		assertEquals(edi_cuscar.messages.size(), 2);
		
		org.bollore.edi.Message message1=edi_cuscar.messages.get(0);
		org.bollore.edi.Message message2=edi_cuscar.messages.get(1);
		
		// On valide que les références des messages
		assertEquals(message1.reference_number,"message 1");
		assertEquals(message2.reference_number,"message 2");
		
		// On récupère les segments de chaque message
		ArrayList<org.bollore.edi.Segment> segments_message1=message1.segments;
		ArrayList<org.bollore.edi.Segment> segments_message2=message2.segments;
		
		// On valide le nombre de segments de chaque
		assertEquals(segments_message1.size(),1);
		assertEquals(segments_message2.size(),2);
		
		assertEquals("CST", segments_message1.get(0).code);
		assertEquals("RFF", segments_message2.get(0).code);
		assertEquals("DTM", segments_message2.get(1).code);
		

		
	}
	
	public static void testBuildEDI()  {
		
		try {	
			String path=System.getProperty("user.dir")+"/TestFiles/Cuscar_Douanes_GHTKD_TEST.edi";
			
		Edifact edi = new Edifact(path);

		edi.buildEDI();
		
		System.out.println("before");
		edi.printEDI();
		System.out.println("after");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		
	}
	
	

}
