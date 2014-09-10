package org.bollore.edi;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

public class Edifact {

	// Attributs liés à la génération de fichier
	public String filepath;
	public PrintWriter printwriter;
	public Boolean isTest;

	// Attributs utilisés pour le segment UNA
	public Character component_separator;
	public Character element_separator;
	public Character decimal_separator;
	public Character escape_character;
	public Character space_character = ' ';
	public Character segment_separator;
	// Fin

	// Attributs utilisés pour le segment UNB
	public String syntax_id;
	public String syntax_version_number;
	public String interchange_sender_id; // GRIMALDI
	public String id_code_qualifier;
	public String interchange_recipient_id; // exemple SNCUSTOMS
	public String date; // Date du jour
	public String time; // Heure du jour
	public String interchange_control_reference; // Auto-incrément du nombre de
													// fichiers échangés avec
													// interchange_sender_id
													// durant l'année courante
	// Fin

	// Attributs utilisés pour le segment UNH
	public String message_reference_number;
	public String edi_version_number;
	public String edi_type;
	public String edi_year_version;
	public String edi_letter_version;
	public String controlling_agency;
	// Fin

	public ArrayList<org.bollore.edi.Segment> structure;
	public ArrayList<org.bollore.edi.Segment> segments;

	// public HashMap<String, Integer> segments_rank;

	/*********************************************
	 * 
	 * Constructeurs de la classe Edifact
	 * 
	 * *******************************************/

	public Edifact(String filepath, Boolean isTest,
			Character element_separator, Character component_separator,
			Character space_character, Character decimal_separator,
			Character escape_character, Character segment_separator,

			String message_reference_number, String edi_version_number,
			String edi_type, String edi_year_version,
			String edi_letter_version, String controlling_agency,

			String syntax_id, String syntax_version_number,
			String interchange_sender_id, String id_code_qualifier,
			String interchange_recipient_id, Date date,
			String interchange_control_reference)

	{

		super();
		try {
			// Instantiation des attributs de fichier
			this.filepath = filepath;
			this.isTest = isTest;
			this.printwriter = new PrintWriter(new File(filepath));

			// Instantiation des attributs liés au segment UNA
			if (element_separator == null) {
				this.element_separator = '+';
			} else {
				this.element_separator = element_separator;
			}
			if (decimal_separator == null) {
				this.decimal_separator = '.';
			} else {
				this.decimal_separator = decimal_separator;
			}
			if (component_separator == null) {
				this.component_separator = ':';
			} else {
				this.component_separator = component_separator;
			}
			if (escape_character == null) {
				this.escape_character = '?';
			} else {
				this.escape_character = decimal_separator;
			}
			if (segment_separator == null) {
				this.segment_separator = '\'';
			} else {
				this.segment_separator = segment_separator;
			}
			if (space_character == null) {
				this.space_character = ' ';
			} else {
				this.space_character = space_character;
			}

			// Instantiation des attributs du segment UNB
			this.syntax_id = syntax_id;
			this.syntax_version_number = syntax_version_number;
			this.interchange_sender_id = interchange_sender_id;
			this.id_code_qualifier = (id_code_qualifier == null) ? ""
					: id_code_qualifier;
			this.interchange_recipient_id = interchange_recipient_id;
			this.date = (date == null) ? Utils.getCurrentDate("yyMMdd") : Utils
					.formatDate("yyMMdd", date);
			this.time = (date == null) ? Utils.getCurrentDate("HHmmss") : Utils
					.formatDate("HHmmss", date);
			this.interchange_control_reference = interchange_control_reference;

			// Instantiation des attributs du segment UNH
			this.message_reference_number = message_reference_number;
			this.edi_version_number = edi_version_number;
			this.edi_type = edi_type;
			this.edi_year_version = edi_year_version;
			this.edi_year_version = edi_year_version;
			this.controlling_agency = controlling_agency;

			// Création des listes vides des segments et des éléments
			this.structure = new ArrayList<org.bollore.edi.Segment>();
			this.segments = new ArrayList<org.bollore.edi.Segment>();

			this.BuildStructureSegment();
			// this.segments_rank=this.buildHashSegment();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public Edifact(String filepath, Boolean isTest,

	String message_reference_number, String edi_version_number,
			String edi_type, String edi_year_version,
			String edi_letter_version, String controlling_agency,

			String syntax_id, String syntax_version_number,
			String interchange_sender_id, String id_code_qualifier,
			String interchange_recipient_id, Date date,

			String interchange_control_reference)

	{

		super();
		try {
			// Instantiation des attributs de fichier
			this.filepath = filepath;
			this.isTest = isTest;
			this.printwriter = new PrintWriter(new File(filepath));

			// Instantiation des attributs liés au segment UNA
			this.element_separator = '+';
			this.decimal_separator = '.';
			this.component_separator = ':';
			this.escape_character = '?';
			this.segment_separator = '\'';
			this.space_character = ' ';

			// Instantiation des attributs du segment UNB
			this.syntax_id = syntax_id;
			this.syntax_version_number = syntax_version_number;
			this.interchange_sender_id = interchange_sender_id;
			this.id_code_qualifier = (id_code_qualifier == null) ? ""
					: id_code_qualifier;
			this.interchange_recipient_id = interchange_recipient_id;
			this.date = (date == null) ? Utils.getCurrentDate("yyMMdd") : Utils
					.formatDate("yyMMdd", date);
			this.time = (date == null) ? Utils.getCurrentDate("HHmm") : Utils
					.formatDate("HHmm", date);
			this.interchange_control_reference = interchange_control_reference;

			// Instantiation des attributs du segment UNH
			this.message_reference_number = message_reference_number;
			this.edi_version_number = edi_version_number;
			this.edi_type = edi_type;
			this.edi_year_version = edi_year_version;
			this.edi_letter_version = edi_letter_version;
			this.controlling_agency = controlling_agency;

			// Création des listes vides des segments et des éléments
			this.structure = new ArrayList<org.bollore.edi.Segment>();
			this.segments = new ArrayList<org.bollore.edi.Segment>();

			this.BuildStructureSegment();
			// this.segments_rank=this.buildHashSegment();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws JDOMException, IOException,
			EDIException {

		Edifact edi_cuscar = new Edifact("C:/Temp/Cuscar_Test.edi", true, "1",
				"D", "CUSCAR", "95", "B", "UN", "UNOC", "2", "GRIMALDI", "",
				"SNCUSTOMS", Utils.getCurrentDate(), "334518001");

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

		rng.add("RNG1");
		rng.add("RNG2");
		rng.add("RNG3");

		ArrayList<String> rng2 = new ArrayList<String>();

		rng2.add("RNG4");
		rng2.add("RNG5");
		rng2.add("RNG6");

		ArrayList<String> loc_3227 = new ArrayList<String>();

		loc_3227.add("5");

		ArrayList<String> loc_c517 = new ArrayList<String>();

		loc_c517.add("tata");
		loc_c517.add("162");
		loc_c517.add("");
		loc_c517.add("");

		ArrayList<String> loc2_3227 = new ArrayList<String>();

		loc2_3227.add("5");

		ArrayList<String> loc2_c517 = new ArrayList<String>();

		loc2_c517.add("toto");
		loc2_c517.add("139");
		loc2_c517.add("");
		loc2_c517.add("");

		// On peut utiliser l'un ou l'autre
		edi_cuscar.setValueElement("DTM/C507", "I1,J1", ",", true);

//		 edi_cuscar.setValueElement("DTM/C507", dtm, true);
		// /////////////////////////////////////////////////

		edi_cuscar.setValueElement("RFF/C506", rff1, true);
		edi_cuscar.setValueElement("RFF/C506", rff2, true);
		edi_cuscar.setValueElement("NAD/3035", nad1, true);
		edi_cuscar.setValueElement("NAD/C082", nad2, false);

		edi_cuscar.setValueElement("GRP1/LOC/3227", loc_3227, true);
		edi_cuscar.setValueElement("GRP1/LOC/C517", loc_c517, false);
		edi_cuscar.setValueElement("GRP1/LOC/3227", loc2_3227, true);
		edi_cuscar.setValueElement("GRP1/LOC/C517", loc2_c517, false);

		edi_cuscar.setValueElement("GRP2/GRP3/RNG/C280", rng, true);
		edi_cuscar.setValueElement("GRP2/GRP3/RNG/C280", rng2, true);
		edi_cuscar.setValueElement("GRP4/GRP5/GRP10/PCI/C210","1,2,3,4,5,6,7,8,9,10",",",true);
		// edi_cuscar.printEDI();

		 edi_cuscar.print();

		// String A="1,2,3,4,5,6,7,8,9,10";
		String A = "1,2,3,4,5";
		//System.out.println(edi_cuscar.getElementNbValues("GRP2/EQD/8077"));
		

	}

	public Integer getElementNbValues(String element_path) throws EDIException {
		Integer result = -1;

		// On récupère le segment à traiter
		org.bollore.edi.Segment segment = null;
		// On extrait le segment du chemin de l'élément
		String segment_path = element_path.substring(0,
				element_path.lastIndexOf("/"));
		// On récupère le segment depuis la structure
		segment = this.getSegmentStructure(segment_path);

		ArrayList<org.bollore.edi.Element> elements = segment.elements;

		org.bollore.edi.Element element = null;

		String element_name = element_path.substring(
				element_path.lastIndexOf("/") + 1, element_path.length());
		for (int i = 0; i < elements.size(); i++) {
			element = elements.get(i);

			// A.substring(A.lastIndexOf("/")+1,A.length())

			if (element.code.equals(element_name)) {

				break;
			}

		}
		// Si l'élément n'a pas été trouvé on sort
		if(!element.code.equals(element_name)){
			throw new EDIException("L'élément "+element_path+" n'a pas été trouvé.");
		}

		// L'élément n'a pas de composants
		if ((element.components == null || element.components.size() <= 0)) {
			result = 1;
		} else {
			result = element.components.size();
		}

		return result;
	}


	public static String formatInput(String input, Integer length_expected,
			String separator) throws EDIException {
		String result = "";

		Integer length_input = input.split(separator).length;

		if (length_input == length_expected) {
			result = input;
		} else if (length_input < length_expected) {
			result = input;

			for (int i = 0; i < length_expected - length_input; i++) {
				result = result.concat(separator).concat(" ");
			}
		} else if (length_input > length_expected) {
			throw new EDIException("Le nombre d'élément (" + length_input
					+ ") est supérieur au nombre d'éléments attendus ("
					+ length_expected + ")");
		}

		return result;
	}

	public org.bollore.edi.Segment getSegmentStructure(String segment_path)
			throws EDIException {

		org.bollore.edi.Segment result = new org.bollore.edi.Segment();

		if (segment_path == null) {
			throw new EDIException(
					"Le segment a récupérer doit avoir un chemin renseigné");
		} else {

			String[] split = segment_path.split("/");
			ArrayList<org.bollore.edi.Segment> segments = this.structure;

			for (int i = 0; i < split.length; i++) {

				for (int j = 0; j < segments.size(); j++) {
					if (split[i].equals(segments.get(j).code)) {

						result = segments.get(j).clone();
						segments = segments.get(j).segments;
						break;
					}
				}
			}

		}

		return result;

	}

	/**
	 * Cette méthode permet d'ajouter le segment UNB non présent dans la
	 * définition du Cuscar depuis Smooks
	 */

	public org.bollore.edi.Segment buildUNBSegment() {
		// Création du segment UNB
		org.bollore.edi.Segment seg_unb = new Segment(
				"MESSAGE HEADER ENVELOPE", "UNB", 1, 1,
				"To identify the interchange",
				new ArrayList<org.bollore.edi.Element>(),
				new ArrayList<org.bollore.edi.Segment>());

		// Création de l'élément S001 avec les composants
		ArrayList<org.bollore.edi.Component> S001_components = new ArrayList<org.bollore.edi.Component>();

		org.bollore.edi.Component c0001 = new Component(
				"String",
				"1",
				"1",
				true,
				false,
				"Syntax identifier",
				"Must report the following fixed values : UNOA: United Nations Controlling Agency.",
				this.syntax_id);

		org.bollore.edi.Component c0002 = new Component("String", "1", "1",
				true, false, "Syntax version identifier",
				"Version of the syntax", this.syntax_version_number);

		S001_components.add(c0001);
		S001_components.add(c0002);

		org.bollore.edi.Element S001_element = new org.bollore.edi.Element(
				"S001", true, false, "SYNTAX IDENTIFIER", "SYNTAX IDENTIFIER",
				S001_components);

		seg_unb.elements.add(S001_element);

		// Création de l'élément S002 avec les composants
		ArrayList<org.bollore.edi.Component> S002_components = new ArrayList<org.bollore.edi.Component>();

		org.bollore.edi.Component c0004 = new Component("String", "1", "1",
				true, false, "Sender identification",
				"GLN: Global Location Number of the Shipping Company",
				this.interchange_sender_id);
		org.bollore.edi.Component c0007 = new Component("String", "1", "1",
				true, false, "Identification code qualifier", "",
				this.id_code_qualifier);

		S002_components.add(c0004);
		S002_components.add(c0007);
		org.bollore.edi.Element S002_element = new org.bollore.edi.Element(
				"S002", true, false, "INTERCHANGE SENDER",
				"Identification of the sender of the message.", S002_components);

		seg_unb.elements.add(S002_element);

		// Création de l'élément S003 avec les composants

		ArrayList<org.bollore.edi.Component> S003_components = new ArrayList<org.bollore.edi.Component>();

		org.bollore.edi.Component c0010 = new Component(
				"String",
				"1",
				"1",
				true,
				false,
				"Recipient Identification",
				"GLN: Global Location Number regarding the Customs where the ship will arrive. ",
				this.interchange_recipient_id);

		S003_components.add(c0010);
		S003_components.add(c0007);
		org.bollore.edi.Element S003_element = new org.bollore.edi.Element(
				"S003", true, false, "INTERCHANGE RECIPIENT",
				"Identification of the recipient of the message. ",
				S003_components);

		seg_unb.elements.add(S003_element);

		// Création de l'élément S004 avec les composants

		ArrayList<org.bollore.edi.Component> S004_components = new ArrayList<org.bollore.edi.Component>();

		org.bollore.edi.Component c0017 = new Component("String", "1", "1",
				true, false, "Date of Transmission",
				"Date format CCYYMMDD (century, year,month, day)", this.date);
		org.bollore.edi.Component c0019 = new Component("String", "1", "1",
				true, false, "Time of transmission",
				"Time of transmission format HHMM (Hour,minute) ", this.time);

		S004_components.add(c0017);
		S004_components.add(c0019);

		org.bollore.edi.Element S004_element = new org.bollore.edi.Element(
				"S004", true, false, "DATE/TIME OF PREPARATION",
				"Date/time of the message transmission.", S004_components);

		seg_unb.elements.add(S004_element);

		// Création de l'élément 0020
		org.bollore.edi.Element E0020_element = new org.bollore.edi.Element(
				"S0020",
				true,
				false,
				"INTERCHANGE CONTROL REFERENCE",
				"Sequential number of the message (the same of the file) that is being transmitted. This number starts with 0001 and includes all the files sent by the Shipper or its representative during a year. If during the year the company achieves the number 9999, the next one will have 0001 starting again the numbering.",
				this.interchange_control_reference);

		seg_unb.elements.add(E0020_element);

		// Création de l'élément 0035

		org.bollore.edi.Element S0035_element = new org.bollore.edi.Element(
				"S0035",
				true,
				false,
				"TEST INDICATOR",
				"1: If the message is for a test. 6: The message is not for a test.",
				(this.isTest) ? "1" : "6");

		seg_unb.elements.add(S0035_element);

		return seg_unb;
	}

	/**
	 * Cette méthode permet d'ajouter le segment UNH non présent dans la
	 * définition du Cuscar depuis Smooks
	 */
	public org.bollore.edi.Segment buildUNHSegment() {
		// Création du segment UNH
		org.bollore.edi.Segment seg_unh = new Segment("MESSAGE HEADER ", "UNH",
				1, 1, "To head, identify and specify a message.",
				new ArrayList<org.bollore.edi.Element>(),
				new ArrayList<org.bollore.edi.Segment>());

		// Ajout de l'élément S0062

		org.bollore.edi.Element S0062_element = new org.bollore.edi.Element(
				"E0062", true, false, "MESSAGE REFERENCE NUMBER",
				"Unique message reference assigned by the sender.",
				this.message_reference_number);

		seg_unh.elements.add(S0062_element);

		// Création de l'élément S009 avec les composants

		ArrayList<org.bollore.edi.Component> S009_components = new ArrayList<org.bollore.edi.Component>();

		org.bollore.edi.Component c0065 = new Component(
				"String",
				"1",
				"1",
				true,
				false,
				"Message type identifier",
				"D: Code identifying a type of message and assigned by its controlling agency.",
				this.edi_type);
		org.bollore.edi.Component c0052 = new Component("String", "1", "1",
				true, false, "Message type version number ",
				"Version number of a message type", this.edi_version_number);
		org.bollore.edi.Component c0054 = new Component(
				"String",
				"1",
				"1",
				true,
				false,
				"Message type release number",
				"96B: Released number within the current message type version number.",
				this.edi_year_version + this.edi_letter_version);
		org.bollore.edi.Component c0051 = new Component(
				"String",
				"1",
				"1",
				true,
				false,
				"Controlling agency",
				"UN Code identifying the agency controlling the specification, maintenance and publication of the message type.",
				this.controlling_agency);

		S009_components.add(c0065);
		S009_components.add(c0052);
		S009_components.add(c0054);
		S009_components.add(c0051);

		org.bollore.edi.Element S009_element = new org.bollore.edi.Element(
				"S009", true, false, "MESSAGE IDENTIFIER",
				"Message identifier", S009_components);

		seg_unh.elements.add(S009_element);

		return seg_unh;
	}

	/**
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
	public void setValueElement(String element_path, String values,
			String values_separator, Boolean create_new_segment)
			throws EDIException {
		ArrayList<String> a_values = new ArrayList<String>();

		if (values == null) {
			throw new EDIException(
					"La donnée des valeurs à affecter à l'élément "
							+ element_path + " est null");
		} else {
			
			// Si il y a plus de valeurs que de composants 
			// values.split(values_separator) renvoie le nombre de valeurs en entrée
			if(getElementNbValues(element_path)<values.split(values_separator).length){
				throw new EDIException("L'élément "+element_path+" possède "+getElementNbValues(element_path)+" valeurs à renseigner alors que l'on en a "+values.split(values_separator).length+" à affecter "+values);
			} else {
				
				values=formatInput(values, getElementNbValues(element_path), values_separator);
				}
			
			
			
			for (int i = 0; i < values.split(values_separator).length; i++) {
				a_values.add(values.split(values_separator)[i]);
			}
			

			setValueElement(element_path, a_values, create_new_segment);
		}

	}

	public void setValueElement(String element_path, ArrayList<String> values,
			Boolean create_new_segment) throws EDIException {
		// On récupère le segment à traiter
		org.bollore.edi.Segment segment = null;

		// Si l'on doit créer un nouveau segment
		if (create_new_segment) {
			// Si l'on doit créé le segment, on l'importe depuis la structure et
			// on le met dans segments
			String segment_path = element_path.substring(0,
					element_path.lastIndexOf("/"));
			this.segments.add(this.getSegmentStructure(segment_path));
			// this.segments.add(this.getSegmentStructure(split[0]));
		}

		// On récupère le dernier segment en cours de traitement de l'EDI
		segment = this.segments.get(segments.size() - 1);

		// On récupère l'élément que l'on souhaite instancier

		// org.bollore.edi.Element element=null;
		ArrayList<org.bollore.edi.Element> elements = segment.elements;

		org.bollore.edi.Element element = null;

		for (int i = 0; i < elements.size(); i++) {
			element = elements.get(i);

			// A.substring(A.lastIndexOf("/")+1,A.length())
			String element_name = element_path.substring(
					element_path.lastIndexOf("/") + 1, element_path.length());
			if (element.code.equals(element_name)) {

				break;
			}

		}

		// L'élément n'a pas de composants
		if ((element.components == null || element.components.size() <= 0)) {
			if (values.size() == 1) {
				element.value = values.get(0);
			} else {
				throw new EDIException("L'élément " + element.code
						+ " n'a pas de composants et possède " + values.size()
						+ " valeurs à affecter");
			}

		} else {
			// Si le nombre de valeur à affecter est différent du nombre de
			// composants de l'élément
			if (element.components.size() != values.size()) {

				throw new EDIException("Le nombre de valeurs (" + values.size()
						+ ") ne correspond pas au nombre de composants de "
						+ element_path + " (" + element.components.size() + ")");

			} else {

				for (int i = 0; i < element.components.size(); i++) {

					element.components.get(i).value = values.get(i);
				}
			}

		}
	}

	public org.bollore.edi.Segment getSegment(String segment_path,
			ArrayList<org.bollore.edi.Segment> segments) throws EDIException {
		org.bollore.edi.Segment result = null;

		// On récupère les segments et l'élément en découpant la chaîne
		String[] split = segment_path.split("/");

		// Si les segments ne sont pas renseignés, il s'agit de la première
		// exécution
		if (segments == null) {
			segments = this.segments;
		}

		// Si le chemin possède au moins 2 segments
		if (split.length > 1) {
			// On cherche le segment correspondant
			for (int i = 0; i < segments.size(); i++) {
				// On a trouvé le bon segment on itere sur l'arraylist du
				// segment correspondant en rappelant la méthode en tronquant le
				// chemin du composant GRP2/GRP3/RNG/C280 devient GRP3/RNG/C280
				// et en passant en paramètre l'arraylist de segments du segment
				// trouvé
				if (segments.get(i).code.equals(split[0])) {

					return this.getSegment(segment_path.substring(segment_path
							.indexOf("/") + 1), segments.get(i).segments);
				}
			}
			// Le chemin possède 2 segments séparés par un /: On récupère le
			// segment suivant
		} else {
			// On boucle une dernière fois pour trouver le segment
			// La boucle se fait à l'envers pour récupérer le dernier segment
			// avec le code recherché
			for (int j = segments.size() - 1; j >= 0; j--) {
				// Dès qu'on le trouve, on sort de la boucle
				if (segments.get(j).code.equals(split[0])) {

					result = segments.get(j);
					break;
				}
			}
		}
		return result;
	}

	public org.bollore.edi.Element getElement(String element_path)
			throws EDIException {
		return getElement(element_path, null);
	}

	public org.bollore.edi.Element getElement(String element_path,
			ArrayList<org.bollore.edi.Segment> segments) throws EDIException {
		// Instantiation du retour de la méthode
		org.bollore.edi.Element result = null;

		// On récupère les segments et l'élément en découpant la chaîne
		String[] split = element_path.split("/");

		// Si les segments ne sont pas renseignés, il s'agit de la première
		// exécution
		if (segments == null) {

			segments = this.segments;

		}

		// Le chemin ne comporte pas a minima SEGMENT/ELEMENT
		if (split.length < 2) {
			throw new EDIException(
					"Pour récupérer un élément la structure minimale est SEGMENT/ELEMENT. Au moins un '/' doit apparaître dans le chemin.");
		}
		// Le chemin est de la forme SEGMENT/ELEMENT
		else if (split.length == 2) {

			// On cherche le segment correspondant
			for (int i = 0; i < segments.size(); i++) {
				if (segments.get(i).code.equals(split[0])) {
					org.bollore.edi.Segment segment = segments.get(i);

					ArrayList<org.bollore.edi.Element> elements = segment.elements;

					// Si la partie droite correspond à un élément:
					for (int j = elements.size() - 1; j >= 0; j--) {

						if (elements.get(j).code.equals(split[1])) {

							result = elements.get(j);
						}
					}
				}
			}
		} else {

			throw new EDIException(
					" La méthode getElement() dans la classe Edifact attend en entrée une valeur de la forme SEGMENT/ELEMENT");
		}

		if (result == null) {
			throw new EDIException("L'élément '" + element_path
					+ "' pour l'EDI " + this.edi_type + this.edi_year_version
					+ this.edi_letter_version + " n'a pas été trouvé");
		}
		return result;
	}

	public void BuildStructureSegment() {
		SAXBuilder sxb = new SAXBuilder();
		Document document;
		org.jdom2.Element racine;

		try {
			document = sxb.build("EDI_Definitions/D" + this.edi_year_version
					+ this.edi_letter_version + "/" + this.edi_type + ".xml");

			racine = document.getRootElement();
			List<org.jdom2.Element> nodes = racine.getChildren("segments");

			for (int i = 0; i < nodes.size(); i++) {
				BuildStructureSegmentRecursive(
						new ArrayList<org.bollore.edi.Segment>(),
						(nodes.get(i)));

			}

		} catch (JDOMException jdome) {
			jdome.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	public void BuildStructureSegmentRecursive(
			ArrayList<org.bollore.edi.Segment> _Segments,
			org.jdom2.Element _Node) {
		HashMap<String, org.bollore.edi.Segment> segments_definition = this
				.buildStructureSegmentDefinition();

		List<org.jdom2.Element> nodes = _Node.getChildren();

		for (int i = 0; i < nodes.size(); i++) {
			if ("segment".equals(nodes.get(i).getName())
					|| "segmentGroup".equals(nodes.get(i).getName())) {
				if ("segment".equals(nodes.get(i).getName()))
					// _Segments.add(segments_definition.get(nodes.get(i).getAttributeValue("segcode")));
					_Segments.add(segments_definition.get(
							nodes.get(i).getAttributeValue("segcode")).clone());

				if ("segmentGroup".equals(nodes.get(i).getName())) {
					org.bollore.edi.Segment segment = new org.bollore.edi.Segment(

					nodes.get(i).getAttributeValue("xmltag"), "GRP"
							+ (String) nodes.get(i).getAttributeValue("xmltag")
									.split("_")[2], Integer.parseInt(nodes.get(
							i).getAttributeValue("minOccurs")),
							Integer.parseInt(nodes.get(i).getAttributeValue(
									"maxOccurs")), "");
					BuildStructureSegmentRecursive(segment.segments,
							nodes.get(i));
					_Segments.add(segment);
				}
			}
		}

		this.structure = _Segments;
	}

	public void print() {
		try {
			this.printHeader();
			this.printSegments();
			this.printfooter();
			this.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		} finally {

			try {
				if (this.printwriter != null) {
					this.printwriter.close();
				}

			} catch (Exception e2) {
				System.err.println(e2.getMessage());
				e2.printStackTrace();
			}
		}

	}

	public void printHeader() {
		this.printwriter.append("UNA" + this.element_separator
				+ this.component_separator + this.decimal_separator
				+ this.escape_character + this.space_character
				+ this.segment_separator + "\n");

		this.printSegment(this.buildUNBSegment());
		this.printSegment(this.buildUNHSegment());
	}

	public void printSegments() {
		printSegments(this.segments);
	}

	public void printSegment(org.bollore.edi.Segment segment) {
		ArrayList<org.bollore.edi.Segment> segments = new ArrayList<org.bollore.edi.Segment>();
		segments.add(segment);
		this.printSegments(segments);

	}

	public void printSegments(ArrayList<org.bollore.edi.Segment> segments) {

		// On boucle sur tous les segments à imprimer dans le fichier
		for (int i = 0; i < segments.size(); i++) {

			org.bollore.edi.Segment segment = segments.get(i);

			if (!segment.isEmpty(segment)) {

				// Si le segment est un segment de groupe
				if (segment.code.substring(0, 3).equals("GRP")) {
					printSegments(segment.segments);
				}
				// Il s'agit d'un segment simple
				else {

					this.printwriter.append(segment.code);

					ArrayList<org.bollore.edi.Element> elements = segment.elements;

					for (int j = 0; j < elements.size(); j++)

					{
						org.bollore.edi.Element element = elements.get(j);
						ArrayList<org.bollore.edi.Component> components = element.components;

						// S'il s'agit d'un élément simple on l'écrit dans le
						// fichier
						if (components == null || components.size() <= 0) {
							String value = (element.value == null) ? ""
									: element.value;

							this.printwriter.append(element_separator + value);
						}
						// L'élément possède des composants
						else {
							for (int k = 0; k < components.size(); k++) {
								org.bollore.edi.Component component = components
										.get(k);

								String value2 = (component.value == null) ? ""
										: component.value;

								if (k == 0) {

									this.printwriter
											.append(this.element_separator
													+ value2);

								} else {

									this.printwriter
											.append(this.component_separator
													+ value2);

								}
							}
						}
					}
					this.printwriter.append(this.segment_separator + "\n");

				}

			} // if
		}
	}

	public void printfooter() {
		this.printwriter
				.append("UNT" + this.element_separator + this.segments.size()
						+ this.element_separator
						+ this.message_reference_number
						+ this.segment_separator + "\n");
		this.printwriter.append("UNZ" + this.element_separator + "1"
				+ this.element_separator + this.segment_separator + "\n");
	}

	public void printEDIStructure() {
		if (this.structure == null) {
			System.out.println(" Y a pas de structure");
		} else {
			for (int i = 0; i < this.structure.size(); i++) {
				((org.bollore.edi.Segment) this.structure.get(i))
						.printSegment();
			}
		}
	}

	public void printEDI() {
		if (this.structure == null) {
			System.out.println(" Y a pas de structure");
		} else {
			for (int i = 0; i < this.segments.size(); i++) {
				((org.bollore.edi.Segment) this.segments.get(i)).printSegment();
			}
		}
	}

	public void close() {
		try {
			if (this.printwriter != null) {
				this.printwriter.close();
			}

		} catch (Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
	}

	public HashMap<String, org.bollore.edi.Segment> buildStructureSegmentDefinition() {
		SAXBuilder sxb = new SAXBuilder();
		Document document;
		org.jdom2.Element racine;

		HashMap<String, org.bollore.edi.Segment> segments = new HashMap<String, org.bollore.edi.Segment>();

		try {
			document = sxb.build("EDI_Definitions/D" + this.edi_year_version
					+ this.edi_letter_version + "/__modelset_definitions.xml");

			racine = document.getRootElement();
			List<org.jdom2.Element> nodes = racine.getChildren("segments");

			for (int i = 0; i < nodes.size(); i++) {

				// On récupère la liste de tous les segments
				List<org.jdom2.Element> segts = ((org.jdom2.Element) nodes
						.get(i)).getChildren("segment");

				for (int j = 0; j < segts.size(); j++) {

					// Pour chaque segments on récupère la liste des éléments
					List<org.jdom2.Element> field = ((org.jdom2.Element) segts
							.get(j)).getChildren("field");

					ArrayList<org.bollore.edi.Element> elements = new ArrayList<org.bollore.edi.Element>();

					for (int l = 0; l < field.size(); l++) {

						List<org.jdom2.Element> documentation = ((org.jdom2.Element) field
								.get(l)).getChildren("documentation");

						String doc = (documentation.size() > 0) ? documentation
								.get(0).getText() : null;

						// Je récupère la doc de l'élément
						List<org.jdom2.Element> component = ((org.jdom2.Element) field
								.get(l)).getChildren("component");
						ArrayList<Component> components = new ArrayList<Component>();
						// On travaille avec un élément qui possède des
						// composants
						if (component.size() > 0) {

							for (int m = 0; m < component.size(); m++) {

								components
										.add(new Component(
												((org.jdom2.Element) component
														.get(m))
														.getAttributeValue("dataType"),
												((org.jdom2.Element) component
														.get(m))
														.getAttributeValue("maxLength"),
												((org.jdom2.Element) component
														.get(m))
														.getAttributeValue("minLength"),
												"true".equals(((org.jdom2.Element) component
														.get(m))
														.getAttributeValue("required")),
												"true".equals(((org.jdom2.Element) component
														.get(m))
														.getAttributeValue("truncatable")),
												((org.jdom2.Element) component
														.get(m))
														.getAttributeValue("xmltag"),
												(String) ((org.jdom2.Element) component
														.get(m))
														.getChildren(
																"documentation")
														.get(0).getText()));
							}

							// On travaille avec un élément qui ne possède pas
							// de composants
						} else {

						}

						elements.add(new org.bollore.edi.Element(
								((org.jdom2.Element) field.get(l))
										.getAttributeValue("nodeTypeRef"),
								"true".equals(((org.jdom2.Element) field.get(l))
										.getAttributeValue("required")),
								"true".equals(((org.jdom2.Element) field.get(l))
										.getAttributeValue("truncatable")),
								((org.jdom2.Element) field.get(l))
										.getAttributeValue("xmltag"), doc,
								components));
					}

					segments.put(
							((org.jdom2.Element) segts.get(j))
									.getAttributeValue("segcode"),
							new org.bollore.edi.Segment(
									((org.jdom2.Element) segts.get(j))
											.getAttributeValue("xmltag"),
									((org.jdom2.Element) segts.get(j))
											.getAttributeValue("segcode"),
									Integer.parseInt(((org.jdom2.Element) segts
											.get(j))
											.getAttributeValue("minOccurs")),
									Integer.parseInt(((org.jdom2.Element) segts
											.get(j))
											.getAttributeValue("maxOccurs")),
									((org.jdom2.Element) segts.get(j))
											.getAttributeValue("description"),
									elements, new ArrayList<Segment>()));
				}
			}
		} catch (JDOMException jdome) {
			jdome.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}

		return segments;
	}

}
