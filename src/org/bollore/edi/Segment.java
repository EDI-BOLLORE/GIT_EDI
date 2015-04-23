package org.bollore.edi;

import org.bollore.edi.parser.EDIParseException;
import org.jdom2.JDOMException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.xpath.XPathExpressionException;

public class Segment implements Cloneable {

	public String name;
	public String code;
	public Integer min_occurence;
	public Integer max_occurence;
	public String description;
	public String segment_path;
	public ArrayList<org.bollore.edi.Element> elements;
	public ArrayList<org.bollore.edi.Segment> segments;

	public Segment() {
		super();
		this.name = "";
		this.code = "";
		this.min_occurence = 0;
		this.max_occurence = 0;
		this.description = "";
		this.elements = new ArrayList<org.bollore.edi.Element>();
		this.segments = new ArrayList<org.bollore.edi.Segment>();
	}

	public Segment(String name, String code, Integer min_occurence,
			Integer max_occurence, String description) {
		super();
		this.name = name;
		this.code = code;
		this.min_occurence = min_occurence;
		this.max_occurence = max_occurence;
		this.description = description;
		this.elements = new ArrayList<org.bollore.edi.Element>();
		this.segments = new ArrayList<org.bollore.edi.Segment>();
	}
	
	public Segment(String name, String code, Integer min_occurence,
			Integer max_occurence, String description,String segment_path) {
		super();
		this.name = name;
		this.code = code;
		this.min_occurence = min_occurence;
		this.max_occurence = max_occurence;
		this.description = description;
		this.elements = new ArrayList<org.bollore.edi.Element>();
		this.segments = new ArrayList<org.bollore.edi.Segment>();
		this.segment_path=segment_path;
	}

	public Segment(String name, String code, Integer min_occurence,
			Integer max_occurence, String description,
			ArrayList<org.bollore.edi.Element> elements,
			ArrayList<Segment> segments) {
		super();
		this.name = name;
		this.code = code;
		this.min_occurence = min_occurence;
		this.max_occurence = max_occurence;
		this.description = description;
		this.elements = elements;
		this.segments = segments;
	}

	public Boolean isEmpty(org.bollore.edi.Segment segment) {
		Boolean result = true;

		ArrayList<org.bollore.edi.Element> elements = segment.elements;
		// On boucle sur les éléments des segments
		for (int i = 0; i < elements.size(); i++) {

			org.bollore.edi.Element element = elements.get(i);
			// Si le segment courant a au moins 1 élément valué on sort à false
			if (element.value != null) {
				if (!element.value.trim().equals("")) {
					result = false;
					return result;
				}

			}
			// On regarde si les composants de l'élément ont une valeur
			ArrayList<org.bollore.edi.Component> components = element.components;
			for (int j = 0; j < components.size(); j++) {
				org.bollore.edi.Component component = components.get(j);
				if (component.value != null) {

					if (!component.value.trim().equals("")) {
						result = false;
						return result;
					}
				}
			}
		}

		// On boucle sur les segments des segments
		for (int i = 0; i < segment.segments.size(); i++) {
			return isEmpty(segment.segments.get(i));
		}

		return result;

	}

	public org.bollore.edi.Segment clone() {
		org.bollore.edi.Segment result = null;

		try {
			ArrayList<org.bollore.edi.Element> elements = new ArrayList<org.bollore.edi.Element>();
			ArrayList<org.bollore.edi.Segment> segments = new ArrayList<org.bollore.edi.Segment>();

			result = (org.bollore.edi.Segment) super.clone();
			result.code = this.code;
			result.min_occurence = this.min_occurence;
			result.max_occurence = this.max_occurence;
			result.description = this.description;

			// On clone les éléments
			for (int i = 0; i < this.elements.size(); i++) {

				elements.add(this.elements.get(i).clone());
			}

			// On clone les segments
			for (int j = 0; j < this.segments.size(); j++) {
				segments.add(this.segments.get(j).clone());
			}
			result.elements = elements;
			result.segments = segments;

		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return result;
	}

	public org.bollore.edi.Element getElement(String _CodeElement) {
		org.bollore.edi.Element element = new org.bollore.edi.Element();
		for (int i = 0; i < this.elements.size(); i++) {
			if (this.elements.get(i).label.equals(_CodeElement))
				System.out.println("OK");
			element = (org.bollore.edi.Element) this.elements.get(i).clone();
		}

		return element;
	}

	public void printSegment() {
		System.out.println(this.code
				+ " - Le segment "+this.segment_path+" possède les éléments suivants : ");

		if (this.segments.size() > 0) {
			for (int i = 0; i < this.segments.size(); i++)
				this.segments.get(i).printSegment();
		}

		for (int j = 0; j < this.elements.size(); j++) {
			((org.bollore.edi.Element) this.elements.get(j)).printElement();

		}
	}

	// Cette méthode permet de renvoyer le rang de la dernière valeur affectée à
	// un élément
	// Elle permet de ne pas afficher les '+' inutiles dans l'EDI quand les
	// éléments sont vides
	public Integer MaxRankElementNonNull() {
		Integer result = null;

		if (this.elements == null) {

			result = 0;
		} else if (this.elements.size() <= 0) {

			result = 0;
		} else if (this.elements.size() > 0) {

			result = this.elements.size();

			// On détermine le rang à partir duquel tous les
			// composants n'ont pas de valeur renseignée
			for (int l = elements.size() - 1; l > 0; l--) {

				org.bollore.edi.Element element = elements.get(l);
				if (element == null) {
					
					result--;
				} else {

					if (element.value == null) {

						if (element.HasEmptyComponents()) {

							result--;
						} else {

							break;
						}
					} else {

						if (element.value.trim().equals("")) {

							if (element.HasEmptyComponents()) {

								result--;
							}
						} else {
							
							break;
						}
					}
				}
			}
		}
		return result;

	}
	
	public static String parseSegmentCode(String segment)
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
	
//	public static Segment parse(
//			HashMap<String, org.bollore.edi.Segment> seg_def, String segment,
//			String element_separator, String component_separator,
//			String escape_separator) throws EDIParseException {
//		
//		Segment result = new Segment();
//
//		/**
//		 * Prévoir cas où element_separator et component_separator sont des
//		 * caractères spéciaux pour les regex
//		 */
//		
//		ArrayList<String> segments_values = Utils.PatternExtract(segment,
//				"([A-Z0-9" + component_separator + "]" + element_separator
//						+ ")");
//
//		if (seg_def != null && seg_def.containsKey(parseSegmentCode(segment))) {
//			result = seg_def.get(parseSegmentCode(segment));
//			
//			System.out.println("Le segment "+result.code+"possède "+result.elements.size()+" éléments pour "+segments_values.size()+" valeurs.");
//
//			for (int i = 0; i < segments_values.size(); i++) {
//				
//			}
//			
//			for (int i = 0; i < segments_values.size(); i++) {
//				
//				org.bollore.edi.Element cur_element=result.elements.get(i);
//				String value=segments_values.get(i);
//				
//				if(cur_element.components.size()==0){
//					cur_element.value=value;
//				} else {
//					
//				}
//				
//				
//			}
//			
//			// Dans le cas où l'on crée un segment sans renseigner ses
//			// caractéristiques seg_def==null
//		} else {
//
//
//
//			result.code = parseSegmentCode(segment);
//
//			for (int i = 1; i < segments_values.size(); i++) {
//				result.elements.add(Element.parseElement(
//						segments_values.get(i), component_separator));
//
//			}
//		}
//		// String
//		// pattern="([A-Z0-9"+component_separator+"]"+element_separator+")";
//		// String
//		// pattern="([A-Z0-9\\s"+component_separator+"]"+element_separator+")";
//		String pattern = "([A-Za-z0-9\\s(?:)]+)" + component_separator + "?";
//
//		ArrayList<String> elements_values = Utils.PatternExtract(segment,
//				pattern);
//
//		ArrayList<org.bollore.edi.Element> ref_elements = result.elements;
//
//		// System.out.println(ref_elements.get(index));
//
//		return result;
//	}

		
	}


