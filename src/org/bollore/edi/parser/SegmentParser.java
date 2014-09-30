package org.bollore.edi.parser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import org.bollore.edi.*;
import org.bollore.edi.tests.UtilsTest;

public class SegmentParser {

	public static String getSegmentCode(String segment)
			throws EDIParseException {
		String result = null;

		if (segment != null && segment.length() >= 3) {
			String temp = segment.substring(0, 3);
			if (Utils.PatternCheck(temp, "[A-Z]{3}")) {
				result = temp;
			} else {
				throw new EDIParseException(
						"Le segment '"
								+ segment
								+ "' à parser ne comporte pas de code à 3 caractères alphabétiques");
			}

		} else {
			throw new EDIParseException(
					"Le segment à parser est vide ou de longueur < 3");
		}
		return result;
	}

	public static Segment parse(
			HashMap<String, org.bollore.edi.Segment> seg_def, String segment,
			String element_separator, String component_separator,
			String escape_separator) throws EDIParseException {
		Segment result = new Segment();

		/**
		 * Prévoir cas où element_separator et component_separator sont des
		 * caractères spéciaux pour les regex
		 */
		
		ArrayList<String> segments_values = Utils.PatternExtract(segment,
				"([A-Z0-9" + component_separator + "]" + element_separator
						+ ")", 1);

		if (seg_def != null && seg_def.containsKey(getSegmentCode(segment))) {
			result = seg_def.get(getSegmentCode(segment));
			
			System.out.println("Le segment "+result.code+"possède "+result.elements.size()+" éléments pour "+segments_values.size()+" valeurs.");

			for (int i = 0; i < segments_values.size(); i++) {
				
			}
			
			for (int i = 0; i < segments_values.size(); i++) {
				
				org.bollore.edi.Element cur_element=result.elements.get(i);
				String value=segments_values.get(i);
				
				if(cur_element.components.size()==0){
					cur_element.value=value;
				} else {
					
				}
				
				
			}
			
			// Dans le cas où l'on crée un segment sans renseigner ses
			// caractéristiques seg_def==null
		} else {



			result.code = getSegmentCode(segment);

			for (int i = 1; i < segments_values.size(); i++) {
				result.elements.addAll(ElementParser.parseElement(
						segments_values.get(i), component_separator));

			}
		}
		// String
		// pattern="([A-Z0-9"+component_separator+"]"+element_separator+")";
		// String
		// pattern="([A-Z0-9\\s"+component_separator+"]"+element_separator+")";
		String pattern = "([A-Za-z0-9\\s(?:)]+)" + component_separator + "?";

		ArrayList<String> elements_values = Utils.PatternExtract(segment,
				pattern, 1);

		ArrayList<org.bollore.edi.Element> ref_elements = result.elements;

		// System.out.println(ref_elements.get(index));

		return result;
	}

	public static void main(String[] args) throws EDIParseException,
			EDIException {
		String path = UtilsTest.tempdir.concat("Cuscar_Test2.edi");
		String seg_string = "TDT+20+0214+1+GBN:172:166+GBN:146::vesselname not found:countryname not mapped'";
		String message_reference_number = "1234";

		Calendar cal = Calendar.getInstance();
		cal.set(2014, 1, 1, 1, 1, 0);
		Date date = cal.getTime();

		Edifact edi_cuscar = new Edifact(path, 0, '+', ':', ' ', '.', '?',
				'\'',"CUSCAR", "D95B", "UN", "UNOC", "2", "GRIMALDI",
				"", "SNCUSTOMS", date, "identifiant de mon voyage");

		Message message1 = new Message(message_reference_number);

		HashMap<String, org.bollore.edi.Segment> seg_def = edi_cuscar
				.buildStructureSegmentDefinition();

		Segment seg = parse(seg_def, seg_string, "+", ":", "?");

		// System.out.println(seg.code);
		// //System.out.println(seg.);
		// System.out.println(seg.min_occurence);
		// System.out.println(seg.max_occurence);
		// System.out.println(seg.description);

		ArrayList<Element> elements = seg.elements;

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
