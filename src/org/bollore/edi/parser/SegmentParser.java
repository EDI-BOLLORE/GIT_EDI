package org.bollore.edi.parser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import org.bollore.edi.*;
import org.bollore.edi.tests.UtilsTest;

public class SegmentParser {





	public static void main(String[] args) throws EDIParseException,
			EDIException {
		String path = UtilsTest.tempdir.concat("Cuscar_Test2.edi");
		String seg_string = "TDT+20+0214+1+GBN:172:166+GBN:146::vesselname not found:countryname not mapped'";
		String message_reference_number = "1234";

		Calendar cal = Calendar.getInstance();
		cal.set(2014, 1, 1, 1, 1, 0);
		Date date = cal.getTime();

		Edifact edi_cuscar = new Edifact("\n",path, 0, '+', ':', ' ', '.', '?',
				'\'',"CUSCAR", "D95B", "UN", "UNOC", "2", "GRIMALDI",
				"","", "SNCUSTOMS", date, "identifiant de mon voyage","");

		Message message1 = new Message(message_reference_number);

		HashMap<String, org.bollore.edi.Segment> seg_def = edi_cuscar
				.buildStructureSegmentDefinition();

		//Segment seg = parse(seg_def, seg_string, "+", ":", "?");

		// System.out.println(seg.code);
		// //System.out.println(seg.);
		// System.out.println(seg.min_occurence);
		// System.out.println(seg.max_occurence);
		// System.out.println(seg.description);

		//ArrayList<Element> elements = seg.elements;

		// for (int i = 0; i < elements.size(); i++) {
		// elements.get(i).printElement();
		// }

		// ArrayList<String>
		// test=Utils.StringToArray(":GBN:146::vesselname not found:countryname not mapped",
		// ":");
		//
		// for (int i = 0; i < test.size(); i++) {
		// System.out.println(i+test.get(i));
		// }
	}

}
