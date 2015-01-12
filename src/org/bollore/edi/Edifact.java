package org.bollore.edi;

//import java.io.BufferedReader;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
//import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
//import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;







import java.util.StringTokenizer;

import org.bollore.edi.tests.UtilsTest;
//import org.bollore.edi.tests.UtilsTest;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

public class Edifact {

	
	
	// Attributs lies à la generation de fichier
	public String filepath;
	public String lineseparator;
	public PrintWriter printwriter;
	public Integer isTest;

	// Attributs utilis�s pour le segment UNA
	public Character component_separator;
	public Character element_separator;
	public Character decimal_separator;
	public Character escape_character;
	public Character space_character = ' ';
	public Character segment_separator;
	// Fin

	// Attributs utilis�s pour le segment UNB
	public String syntax_id;
	public String syntax_version_number;
	public String interchange_sender_id; // GRIMALDI
	public String sender_code_qualifier;
	public String recipient_code_qualifier;
	public String interchange_recipient_id; // exemple SNCUSTOMS
	public String date; // Date du jour au format
	public String time; // Heure du jour
	public String interchange_control_reference; // Auto-incr�ment du nombre de
													// fichiers �chang�s avec
													// interchange_sender_id
													// durant l'ann�e courante
	// Fin

	// Attributs utilis�s pour le segment UNH
	// public String message_reference_number;
	public String edi_version;
	public String edi_type;
	public String controlling_agency;
	// Fin

	// Attribut utilise pour le segment UNZ
	// Cet attribut donne le nombre de segments (UNH,UNT) contenus dans le
	// fichier
	// Integer nb_message;

	public ArrayList<org.bollore.edi.Segment> structure;
	public ArrayList<org.bollore.edi.Message> messages;

	/*********************************************
	 * 
	 * Constructeurs de la classe Edifact
	 * 
	 * @throws EDIException
	 * 
	 * *******************************************/

	//String EDIDefinitions_dir
	
	public Edifact(String filepath) {
		this.filepath=filepath;
		Utils.CreateDir(this.filepath);
		this.messages=new ArrayList<org.bollore.edi.Message>();
	}
	
	public Edifact(String filepath,String edi_type,String edi_version){
		this.filepath=filepath;
		Utils.CreateDir(this.filepath);
		this.edi_type=edi_type;
		this.edi_version=edi_version;
		this.lineseparator="\n";
		
		this.messages=new ArrayList<Message>();
		this.buildStructureSegmentDefinition();
		
		
		}
	

	
	public Edifact(String lineseparator,String filepath, Integer isTest,
			Character element_separator, Character component_separator,
			Character space_character, Character decimal_separator,
			Character escape_character, Character segment_separator,

			 String edi_type,String edi_version,
			String controlling_agency,

			String syntax_id, String syntax_version_number,
			String interchange_sender_id, String sender_code_qualifier,			
			String interchange_recipient_id,String recipient_code_qualifier, Date date,
			String interchange_control_reference) throws EDIException

	{

		super();
		try {
			// Instantiation des attributs de fichier
			this.filepath = filepath;
			
			Utils.CreateDir(this.filepath);
			
			this.isTest = isTest;
			this.printwriter = new PrintWriter(new File(filepath));

			// Instantiation des attributs li�s au segment UNA

			this.element_separator = element_separator;
			this.decimal_separator = decimal_separator;
			this.escape_character = escape_character;
			this.space_character = space_character;
			this.segment_separator = segment_separator;
			this.component_separator = component_separator;

			this.isGrammarCharValid();

			// Instantiation des attributs du segment UNB
			this.syntax_id = syntax_id;
			this.syntax_version_number = syntax_version_number;
			this.interchange_sender_id = interchange_sender_id;
			this.sender_code_qualifier = (sender_code_qualifier == null) ? ""
					: sender_code_qualifier;
			this.recipient_code_qualifier=(recipient_code_qualifier == null) ? ""
					: recipient_code_qualifier;
			this.interchange_recipient_id = interchange_recipient_id;
			this.date = (date == null) ? Utils.getCurrentDate("yyMMdd") : Utils
					.formatDate("yyMMdd", date);
			this.time = (date == null) ? Utils.getCurrentDate("HHmmss") : Utils
					.formatDate("HHmmss", date);
			this.interchange_control_reference = interchange_control_reference;

			// Instantiation des attributs du segment UNH
			this.edi_version = edi_version;
			this.edi_type = edi_type;
			this.controlling_agency = controlling_agency;

			// Cr�ation des listes vides des segments et des �l�ments
			this.structure = new ArrayList<org.bollore.edi.Segment>();
			this.messages = new ArrayList<org.bollore.edi.Message>();
			
			this.lineseparator=lineseparator;

			this.BuildStructureSegment();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public Edifact(String lineseparator,String filepath, Integer isTest,

	String edi_version, String edi_type, String controlling_agency,

			String syntax_id, String syntax_version_number,
			String interchange_sender_id, String sender_code_qualifier,
			String interchange_recipient_id,String recipient_code_qualifier, Date date,

			String interchange_control_reference) throws EDIException

	{

		super();
		try {
			// Instantiation des attributs de fichier
			this.filepath = filepath;
			Utils.CreateDir(this.filepath);
			this.isTest = isTest;
			this.printwriter = new PrintWriter(new File(filepath));

			// Instantiation des attributs li�s au segment UNA
			this.element_separator = '+';
			this.decimal_separator = '.';
			this.component_separator = ':';
			this.escape_character = '?';
			this.segment_separator = '\'';
			this.space_character = ' ';

			this.isGrammarCharValid();

			// Instantiation des attributs du segment UNB
			this.syntax_id = syntax_id;
			this.syntax_version_number = syntax_version_number;
			this.interchange_sender_id = interchange_sender_id;
			this.sender_code_qualifier = (sender_code_qualifier == null) ? ""
					: sender_code_qualifier;
			this.interchange_recipient_id = interchange_recipient_id;
			this.recipient_code_qualifier=recipient_code_qualifier;
			this.date = (date == null) ? Utils.getCurrentDate("yyMMdd") : Utils
					.formatDate("yyMMdd", date);
			this.time = (date == null) ? Utils.getCurrentDate("HHmm") : Utils
					.formatDate("HHmm", date);
			this.interchange_control_reference = interchange_control_reference;

			// Instantiation des attributs du segment UNH
			this.edi_version = edi_version;
			this.edi_type = edi_type;
			this.controlling_agency = controlling_agency;

			// Creation des listes vides des segments et des elements
			this.structure = new ArrayList<org.bollore.edi.Segment>();
			this.messages = new ArrayList<org.bollore.edi.Message>();

			this.BuildStructureSegment();
			this.lineseparator=lineseparator;
			Utils.CreateDir(this.filepath);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	


	public Boolean isGrammarCharValid() throws EDIException {
		Boolean result = null;

		HashSet<String> dedoub = new HashSet<String>();

		ArrayList<String> edi_char = new ArrayList<String>();

		if (this.component_separator == null) {
			throw new EDIException(
					"Le séparateur de composants doit être renseigné : this.component_separator="
							+ this.component_separator);
		}

		if (this.element_separator == null) {
			throw new EDIException(
					"Le séparateur d'élément doit être renseigné : this.element_separator="
							+ this.element_separator);
		}

		if (this.decimal_separator == null) {
			throw new EDIException(
					"Le séparateur de décimales doit être renseigné : this.decimal_separator="
							+ this.decimal_separator);
		}

		if (this.escape_character == null) {
			throw new EDIException(
					"Le caractère d'échappement doit être renseigné : this.escape_character="
							+ this.escape_character);
		}

		if (this.space_character == null) {
			throw new EDIException(
					"Le caractère d'espace doit être renseigné : this.space_character="
							+ this.space_character);
		}

		if (this.element_separator == null) {
			throw new EDIException(
					"Le séparateur de segment doit être renseigné : this.segment_separator="
							+ this.segment_separator);
		}

		edi_char.add(this.component_separator.toString());
		edi_char.add(this.element_separator.toString());
		edi_char.add(this.decimal_separator.toString());
		edi_char.add(this.escape_character.toString());
		edi_char.add(this.space_character.toString());
		edi_char.add(this.segment_separator.toString());

		for (int i = 0; i < edi_char.size(); i++) {

			// Si un élément est déjà présent dans un hashset la méthode add
			// renvoie false.
			if (dedoub.contains(edi_char.get(i))) {
				throw new EDIException(
						"La grammaire de l'EDI ne doit pas comporter de doublons "
								+ this.component_separator
								+ this.element_separator
								+ this.decimal_separator
								+ this.escape_character + this.space_character
								+ this.segment_separator);
				// return false;
			} else {
				dedoub.add(edi_char.get(i));
				result = true;
			}
		}

		return result;

	}

	public String replaceGrammarChar(String input) {
		String result = "";

		if (input == null) {

			return "";
		} else {
			if (input.trim().equals("")) {
				return result;
			}
		}

		result = input;

		// Construction d'une hashmap avec tous les m�ta-caract�res de
		// JAVA/Regex
		HashMap<String, String> javametacharacter_map = new HashMap<String, String>();

		
		//javametacharacter_map.put(".", "\\.");
		javametacharacter_map.put("*", "\\*");
		javametacharacter_map.put("?", "\\?");
		javametacharacter_map.put("(", "\\(");
		javametacharacter_map.put(")", "\\)");
		javametacharacter_map.put("[", "\\[");
		javametacharacter_map.put("]", "\\]");
		javametacharacter_map.put("{", "\\{");
		javametacharacter_map.put("}", "\\}");
		javametacharacter_map.put("^", "\\^");
		javametacharacter_map.put("$", "\\$");
		javametacharacter_map.put("|", "\\|");
		javametacharacter_map.put("+", "\\+");
		javametacharacter_map.put("\\", "\\\\");

		// A faire en premier: remplacement du caractere d'echappement de l'EDI

		String edi_escape_char;
		String to_replace;
		String replacement_string;

		if (javametacharacter_map.containsKey(String
				.valueOf(this.escape_character))) {

			edi_escape_char = javametacharacter_map.get(String
					.valueOf(this.escape_character));

		} else {
			edi_escape_char = String.valueOf(this.escape_character);
		}

		result = result.replaceAll(edi_escape_char,
				edi_escape_char.concat(edi_escape_char));

		String[] edi_char = { String.valueOf(this.component_separator),
				String.valueOf(this.element_separator),
				//String.valueOf(this.decimal_separator),
				String.valueOf(this.segment_separator) };

		// String.valueOf(this.space_character), doit on catché l'espace???

		for (int i = 0; i < edi_char.length; i++) {
			if (javametacharacter_map.containsKey(edi_char[i])) {
				to_replace = javametacharacter_map.get(edi_char[i]);
				replacement_string = edi_escape_char.concat(to_replace);
			} else {
				to_replace = edi_char[i];
				replacement_string = edi_escape_char.concat(edi_char[i]);
			}

			result = result.replaceAll(to_replace, replacement_string);
		}

		return result;
	}

	public Integer getElementNbValues(String element_path) throws EDIException {
		Integer result = -1;

		// On recupere le segment � traiter
		org.bollore.edi.Segment segment = null;
		// On extrait le segment du chemin de l'�l�ment
		String segment_path = element_path.substring(0,
				element_path.lastIndexOf("/"));
		// On recupere le segment depuis la structure
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
		// Si l'element n'a pas ete trouve on sort
		if (!element.code.equals(element_name)) {
			throw new EDIException("L'element " + element_path
					+ " n'a pas ete trouve pour l'EDI "+this.edi_type+" "+this.edi_version);
		}

		// L'�l�ment n'a pas de composants
		if ((element.components == null || element.components.size() <= 0)) {
			result = 1;
		} else {
			result = element.components.size();
		}

		return result;
	}

	public org.bollore.edi.Segment getSegmentStructure(String segment_path)
			throws EDIException {

		org.bollore.edi.Segment result = new org.bollore.edi.Segment();

		if (segment_path == null) {
			throw new EDIException(
					"Le segment a r�cup�rer doit avoir un chemin renseign�");
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
	 * Cette methode permet d'ajouter le segment UNB non present dans la
	 * definition du Cuscar depuis Smooks
	 */

	public org.bollore.edi.Segment buildUNBSegment() {

		// Creation du segment UNB
		org.bollore.edi.Segment seg_unb = new Segment(
				"MESSAGE HEADER ENVELOPE", "UNB", 1, 1,
				"To identify the interchange",
				new ArrayList<org.bollore.edi.Element>(),
				new ArrayList<org.bollore.edi.Segment>());

		// Cr�ation de l'�l�ment S001 avec les composants
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

		// Cr�ation de l'�l�ment S002 avec les composants
		ArrayList<org.bollore.edi.Component> S002_components = new ArrayList<org.bollore.edi.Component>();

		org.bollore.edi.Component c0004 = new Component("String", "1", "1",
				true, false, "Sender identification",
				"GLN: Global Location Number of the Shipping Company",
				this.interchange_sender_id);
		org.bollore.edi.Component c0007 = new Component("String", "1", "1",
				true, false, "Identification code qualifier", "",
				this.sender_code_qualifier);

		S002_components.add(c0004);
		S002_components.add(c0007);
		org.bollore.edi.Element S002_element = new org.bollore.edi.Element(
				"S002", true, false, "INTERCHANGE SENDER",
				"Identification of the sender of the message.", S002_components);

		seg_unb.elements.add(S002_element);

		// Cr�ation de l'�l�ment S003 avec les composants

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
		
		org.bollore.edi.Component c0007_2 = new Component("String", "1", "1",
				true, false, "Identification code qualifier", "",
				this.recipient_code_qualifier);

		S003_components.add(c0010);
		S003_components.add(c0007_2);
		org.bollore.edi.Element S003_element = new org.bollore.edi.Element(
				"S003", true, false, "INTERCHANGE RECIPIENT",
				"Identification of the recipient of the message. ",
				S003_components);

		seg_unb.elements.add(S003_element);

		// Cr�ation de l'�l�ment S004 avec les composants

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

		// Cr�ation de l'�l�ment 0020
		org.bollore.edi.Element E0020_element = new org.bollore.edi.Element(
				"S0020",
				true,
				false,
				"INTERCHANGE CONTROL REFERENCE",
				"Sequential number of the message (the same of the file) that is being transmitted. This number starts with 0001 and includes all the files sent by the Shipper or its representative during a year. If during the year the company achieves the number 9999, the next one will have 0001 starting again the numbering.");
		E0020_element.setValue(this.interchange_control_reference);

		seg_unb.elements.add(E0020_element);

		// Cr�ation de l'�l�ment 0035

		org.bollore.edi.Element S0035_element = new org.bollore.edi.Element(
				"S0035", true, false, "TEST INDICATOR",
				"1: If the message is for a test. 6: The message is not for a test.");

		// On n'écrit l'element test (1 c'est un test 6, ce n'est pas un test
		// que si la valeur est !=0
		if (!(new Integer(0)).equals(this.isTest)) {
			S0035_element.setValue(String.valueOf(this.isTest));

			seg_unb.elements.add(S0035_element);
		}

		return seg_unb;
	}
	


	public org.bollore.edi.Segment buildUNTSegment(Message message) {
		// Le nombre de segments affiches dans UNT est le nombre de segments
		// entre UNH et UNT inclus d'ou le +2
		String nb_segments = message.countSegments();

		org.bollore.edi.Segment seg_unt = new Segment("MESSAGE TRAILER", "UNT",
				1, 1, "To identify the message",
				new ArrayList<org.bollore.edi.Element>(),
				new ArrayList<org.bollore.edi.Segment>());
		
		org.bollore.edi.Element element1 = new org.bollore.edi.Element("S2",
				true, false, "MESSAGE ID", "Message id", null);
		element1.value = nb_segments;

		org.bollore.edi.Element element2 = new org.bollore.edi.Element("S1",
				true, false, "NUMBER OF MESSAGES", "Number of messages", null);

		element2.value = message.reference_number;



		seg_unt.elements.add(element1);
		seg_unt.elements.add(element2);

		return seg_unt;
	}

	/**
	 * Cette methode permet d'ajouter le segment UNH non present dans la
	 * definition des EDI dans Smooks
	 */
	public org.bollore.edi.Segment buildUNHSegment(Message message) {
		// Cr�ation du segment UNH
		org.bollore.edi.Segment seg_unh = new Segment("MESSAGE HEADER ", "UNH",
				1, 1, "To head, identify and specify a message.",
				new ArrayList<org.bollore.edi.Element>(),
				new ArrayList<org.bollore.edi.Segment>());

		// Ajout de l'�l�ment S0062

		org.bollore.edi.Element S0062_element = new org.bollore.edi.Element(
				"E0062", true, false, "MESSAGE REFERENCE NUMBER",
				"Unique message reference assigned by the sender.");

		S0062_element.setValue(message.reference_number);

		seg_unh.elements.add(S0062_element);

		// Cr�ation de l'�l�ment S009 avec les composants

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
				"Version number of a message type", this.edi_version.substring(0, 1));
		org.bollore.edi.Component c0054 = new Component(
				"String",
				"1",
				"1",
				true,
				false,
				"Message type release number",
				"96B: Released number within the current message type version number.",
				this.edi_version.substring(1,4));
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

	public void setValueElement(String message_reference_number,
			String element_path, String values, String values_separator,
			Boolean create_new_segment) throws EDIException {
		ArrayList<String> a_values = new ArrayList<String>();

		if (values == null) {
			throw new EDIException(
					"La donnee des valeurs a affecter a l'element "
							+ element_path + " est null");
		} else {

			// Si il y a plus de valeurs que de composants
			// values.split(values_separator) renvoie le nombre de valeurs en
			// entr�e
			if (getElementNbValues(element_path) < values
					.split(values_separator).length) {
				throw new EDIException("L'element " + element_path
						+ " possede " + getElementNbValues(element_path)
						+ " valeurs a renseigner alors que l'on en a "
						+ values.split(values_separator).length
						+ " a affecter " + values);
			} else {

				values = Utils.formatInput(values,
						getElementNbValues(element_path), values_separator);
			}
			a_values = Utils.StringToArray(values, values_separator);

			setValueElement(message_reference_number, element_path, a_values,
					create_new_segment);
		}

	}

	public org.bollore.edi.Message getMessage(String message_reference_number)
			throws EDIException {
		ArrayList<org.bollore.edi.Message> messages = this.messages;
		org.bollore.edi.Message result = null;

		// On catche les cas d'erreur
		if (message_reference_number == null) {
			throw new EDIException(
					"Le message à récupérer dans l'EDI doit avoir une référence non null");

		} else {
			if ("".equals(message_reference_number.trim())) {
				throw new EDIException(
						"Le message à récupérer dans l'EDI doit avoir une référence");
			}
		}

		for (int i = 0; i < messages.size(); i++) {
			org.bollore.edi.Message message = messages.get(i);

			if (message_reference_number.equals(message.reference_number)) {
				result = message;
				return result;
			}
		}

		// Si le message n'existe pas on l'ajoute
		if (result == null) {
			result = new Message(message_reference_number);
			this.messages.add(result);
		}

		return result;

	}

	public void setValueElement(String message_ref, String element_path,
			ArrayList<String> values, Boolean create_new_segment)
			throws EDIException {
		// On recupere le segment a traiter
		org.bollore.edi.Segment segment = null;

		org.bollore.edi.Message message = this.getMessage(message_ref);

		// Si l'on doit creer un nouveau segment
		if (create_new_segment) {

			// this.messages.size()-1;
			// Si l'on doit cree le segment, on l'importe depuis la structure et
			// on le met dans segments
			String segment_path = element_path.substring(0,
					element_path.lastIndexOf("/"));
			this.getMessage(message_ref).segments.add(this
					.getSegmentStructure(segment_path));
			// this.segments.add(this.getSegmentStructure(split[0]));
		}

		// On recupere le dernier segment en cours de traitement de l'EDI
		segment = this.getMessage(message_ref).segments.get(this
				.getMessage(message_ref).segments.size() - 1);

		// On recupere l'element que l'on souhaite instancier

		// org.bollore.edi.Element element=null;
		ArrayList<org.bollore.edi.Element> elements = segment.elements;

		org.bollore.edi.Element element = null;
		
		String element_name=null;
		
		for (int i = 0; i < elements.size(); i++) {
			element = elements.get(i);

			element_name = element_path.substring(
					element_path.lastIndexOf("/") + 1, element_path.length());
			if (element.code.equals(element_name)) {

				break;
			}

		}
		
		// Si l'element n'a pas ete trouve on sort
		if (!element.code.equals(element_name)) {
			throw new EDIException("L'element " + element_path
					+ " n'a pas ete trouve pour l'EDI "+this.edi_type+" "+this.edi_version);
		}

		// L'element pas de composants
		if ((element.components == null || element.components.size() <= 0)) {
			if (values.size() == 1) {
				element.value = values.get(0);
			} else {
				throw new EDIException("L'�l�ment " + element.code
						+ " n'a pas de composants et possede " + values.size()
						+ " valeurs a affecter");
			}

		} else {
			// Si le nombre de valeur a affecter est different du nombre de
			// composants de l'element
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

	public org.bollore.edi.Segment getSegment(Integer message_rank,
			String segment_path, ArrayList<org.bollore.edi.Segment> segments)
			throws EDIException {
		org.bollore.edi.Segment result = null;

		// On recupere les segments et l'element en decoupant la chaine
		String[] split = segment_path.split("/");

		// Si les segments ne sont pas renseignes, il s'agit de la premi�re
		// ex�cution
		if (segments == null) {
			segments = this.messages.get(message_rank).segments;
		}

		// Si le chemin poss�de au moins 2 segments
		if (split.length > 1) {
			// On cherche le segment correspondant
			for (int i = 0; i < segments.size(); i++) {
				// On a trouv� le bon segment on itere sur l'arraylist du
				// segment correspondant en rappelant la m�thode en tronquant le
				// chemin du composant GRP2/GRP3/RNG/C280 devient GRP3/RNG/C280
				// et en passant en param�tre l'arraylist de segments du segment
				// trouv�
				if (segments.get(i).code.equals(split[0])) {

					return this.getSegment(message_rank, segment_path
							.substring(segment_path.indexOf("/") + 1), segments
							.get(i).segments);
				}
			}
			// Le chemin poss�de 2 segments s�par�s par un /: On r�cup�re le
			// segment suivant
		} else {
			// On boucle une derni�re fois pour trouver le segment
			// La boucle se fait � l'envers pour r�cup�rer le dernier segment
			// avec le code recherch�
			for (int j = segments.size() - 1; j >= 0; j--) {
				// D�s qu'on le trouve, on sort de la boucle
				if (segments.get(j).code.equals(split[0])) {

					result = segments.get(j);
					break;
				}
			}
		}
		return result;
	}

	public org.bollore.edi.Element getElement(Integer message_rank,
			String element_path) throws EDIException {
		return getElement(message_rank, element_path, null);
	}

	public org.bollore.edi.Element getElement(Integer message_rank,
			String element_path, ArrayList<org.bollore.edi.Segment> segments)
			throws EDIException {
		// Instantiation du retour de la m�thode
		org.bollore.edi.Element result = null;

		// On r�cup�re les segments et l'�l�ment en d�coupant la cha�ne
		String[] split = element_path.split("/");

		// Si les segments ne sont pas renseign�s, il s'agit de la premi�re
		// ex�cution
		if (segments == null) {

			segments = this.messages.get(message_rank).segments;

		}

		// Le chemin ne comporte pas a minima SEGMENT/ELEMENT
		if (split.length < 2) {
			throw new EDIException(
					"Pour recuperer un element la structure minimale est SEGMENT/ELEMENT. Au moins un '/' doit apparaitre dans le chemin.");
		}
		// Le chemin est de la forme SEGMENT/ELEMENT
		else if (split.length == 2) {

			// On cherche le segment correspondant
			for (int i = 0; i < segments.size(); i++) {
				if (segments.get(i).code.equals(split[0])) {
					org.bollore.edi.Segment segment = segments.get(i);

					ArrayList<org.bollore.edi.Element> elements = segment.elements;

					// Si la partie droite correspond � un �l�ment:
					for (int j = elements.size() - 1; j >= 0; j--) {

						if (elements.get(j).code.equals(split[1])) {

							result = elements.get(j);
						}
					}
				}
			}
		} else {

			throw new EDIException(
					" La m�thode getElement() dans la classe Edifact attend en entr�e une valeur de la forme SEGMENT/ELEMENT");
		}

		if (result == null) {
			throw new EDIException("L'�l�ment '" + element_path
					+ "' pour l'EDI " + this.edi_type + this.edi_version.substring(1,4) + " n'a pas ete trouve");
		}
		return result;
	}

	public void BuildStructureSegment() {
		SAXBuilder sxb = new SAXBuilder();
		Document document;
		org.jdom2.Element racine;

		try {
			
			document = sxb.build(this.getClass().getResourceAsStream("/D" + this.edi_version.substring(1,4) + "/" + this.edi_type + ".xml"));

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
				if ("segment".equals(nodes.get(i).getName())) {
					// _Segments.add(segments_definition.get(nodes.get(i).getAttributeValue("segcode")));
					_Segments.add(segments_definition.get(
							nodes.get(i).getAttributeValue("segcode")).clone());
				}
				// if ("segmentGroup".equals(nodes.get(i).getName())) {
				else {
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
			
			String edifilepath=this.filepath;
			
			File edifile=new File(edifilepath);
			
			if(!edifile.getParentFile().exists()) {
				edifile.getParentFile().mkdirs();
			}
			
			this.printHeader();
			this.printMessages();
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
		this.printwriter.append("UNA" + this.component_separator
				+ this.element_separator + this.decimal_separator
				+ this.escape_character + this.space_character
				+ this.segment_separator + this.lineseparator);

		this.printSegment(this.buildUNBSegment());
		// this.printSegment(this.buildUNHSegment());
	}

	public void printMessages() {
		for (int i = 0; i < this.messages.size(); i++) {
			printMessage(this.messages.get(i));
		}
	}

	public void printMessage(Message message) {
		this.printSegment(this.buildUNHSegment(message));
		this.printSegments(message.segments);
		this.printSegment(this.buildUNTSegment(message));
	}

	public void printSegment(org.bollore.edi.Segment segment) {
		ArrayList<org.bollore.edi.Segment> segments = new ArrayList<org.bollore.edi.Segment>();
		segments.add(segment);
		this.printSegments(segments);

	}

	public void printSegments(ArrayList<org.bollore.edi.Segment> segments) {

		// On boucle sur tous les segments � imprimer dans le fichier
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

					
					
					for (int j = 0; j < segment.MaxRankElementNonNull(); j++)

					{
						org.bollore.edi.Element element = elements.get(j);

						ArrayList<org.bollore.edi.Component> components = element.components;
						
						if(!element.isEmpty()) {
						
						
						
						// Il s'agit d'un élément simple: on l'écrit dans le
						// fichier
						if (components == null || components.size() <= 0) {
							String value = this
									.replaceGrammarChar(element.value);
							if (value != null&&!"".equals(value.trim())) {
									this.printwriter.append(this.element_separator
											+ value);
							} else {
								this.printwriter.append(this.element_separator);
							}

						}
						// L'élément possède des composants
						else {
							Integer max_non_null = element
									.MaxRankComponentNonNull();

							for (int k = 0; k < max_non_null; k++) {
								org.bollore.edi.Component component = components
										.get(k);

								String value2 = this
										.replaceGrammarChar(component.value);


								if (k == 0) {
									// Si c'est le premier composant, on
									// n'imprime pas le separateur de composant
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
					} else {
						this.printwriter.append(this.element_separator);
					}
					}

					this.printwriter.append(this.segment_separator + this.lineseparator);
					
					

				}

			}
		}

	}

	public void printfooter() {

		this.printwriter.append("UNZ" + this.element_separator
				+ this.messages.size() + this.element_separator
				+ this.replaceGrammarChar(this.interchange_control_reference)  + this.segment_separator
				+ this.lineseparator);
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
			for (int i = 0; i < this.messages.size(); i++) {

				org.bollore.edi.Message message = this.messages.get(i);
				for (int j = 0; j < message.segments.size(); j++) {
					((org.bollore.edi.Segment) message.segments.get(j))
							.printSegment();
				}
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
			
			document = sxb.build(Edifact.class.getResourceAsStream("/" + this.edi_version + "/__modelset_definitions.xml"));

			racine = document.getRootElement();
			List<org.jdom2.Element> nodes = racine.getChildren("segments");

			for (int i = 0; i < nodes.size(); i++) {

				// On recupere la liste de tous les segments
				List<org.jdom2.Element> segts = ((org.jdom2.Element) nodes
						.get(i)).getChildren("segment");

				for (int j = 0; j < segts.size(); j++) {

					// Pour chaque segments on recupere la liste des elements
					List<org.jdom2.Element> field = ((org.jdom2.Element) segts
							.get(j)).getChildren("field");

					ArrayList<org.bollore.edi.Element> elements = new ArrayList<org.bollore.edi.Element>();

					for (int l = 0; l < field.size(); l++) {

						List<org.jdom2.Element> documentation = ((org.jdom2.Element) field
								.get(l)).getChildren("documentation");

						String doc = (documentation.size() > 0) ? documentation
								.get(0).getText() : null;

						// Je r�cup�re la doc de l'�l�ment
						List<org.jdom2.Element> component = ((org.jdom2.Element) field
								.get(l)).getChildren("component");
						ArrayList<Component> components = new ArrayList<Component>();
						// On travaille avec un �l�ment qui possede des
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

							// On travaille avec un element qui ne poss�de pas
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

	
	public static String replaceSpecialGrammarChar(Character input) {
		Character[] inputs= {'*','+','.'};
		String result="";
		Boolean is_special_char=false;
		for (int i = 0; i < inputs.length; i++) {
			if(inputs[i].equals(input)) {
				is_special_char=true;
				break;
			}
		}
		if(is_special_char) {
			result="\\".concat(input.toString());
		} else {
			result=input.toString();
		}
		
		return result;
	}
	
	public void parseEDI(){
		try {
			
			FileInputStream fstream = new FileInputStream(filepath);
			
			DataInputStream in = new DataInputStream(fstream);
			  BufferedReader br = new BufferedReader(new InputStreamReader(in));
			  String strLine="";
			  
			  this.lineseparator="\n";
			  
			  while((strLine = br.readLine())!= null) {
				  if(strLine.startsWith("UNA")) {
					  this.component_separator=strLine.substring(4,5).charAt(0);
					  this.segment_separator=strLine.substring(8,9).charAt(0);
					  this.element_separator=strLine.substring(3,4).charAt(0);
					  this.escape_character=strLine.substring(6,7).charAt(0);
					  this.space_character=strLine.substring(7,8).charAt(0);
					  this.decimal_separator=strLine.substring(5,6).charAt(0);
					  break;
				  }				  
			  }
			  
			  while((strLine = br.readLine())!= null&&strLine.startsWith("UNB")) {
				
					  
					String[] unb_elements= strLine.split(replaceSpecialGrammarChar(this.component_separator));
					if(unb_elements.length==7&&"1".equals(unb_elements[6].substring(0,1))){
						this.isTest=1;												
					} else {
						this.isTest=0;
						}
					
					String[] syntax=unb_elements[1].split(replaceSpecialGrammarChar(this.element_separator));
					this.syntax_id=syntax[0];
					this.syntax_version_number=syntax[1];
					if(Utils.PatternCheck(unb_elements[4],"[0-9]{6}.{1}[0-9]{4}")) {
						this.date=unb_elements[4].substring(0,6);
						this.time=unb_elements[4].substring(7,11).concat("00");
					} 
					this.interchange_control_reference=unb_elements[5].replaceAll(this.segment_separator.toString(),"");
					break;
			  }
			  
			  while((strLine = br.readLine())!= null) {
				  if(strLine.startsWith("UNH")) {
					  
					String[] unh_elements= strLine.split(replaceSpecialGrammarChar(this.component_separator));
					
					this.messages.add(new Message(unh_elements[1]));
					
					String[] unh_components=(unh_elements[2]).split(replaceSpecialGrammarChar(this.element_separator));
					
					this.edi_type=unh_components[0];
					
					this.edi_version=unh_components[1].concat(unh_components[2]);					
					this.buildStructureSegmentDefinition();
					this.controlling_agency=unh_components[3];
					
					
					for (int i = 0; i < unh_elements.length; i++) {
						//System.out.println(unh_elements[i]);
					}					

				  }				  
			  }	
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) throws IOException, EDIException {
		
		Edifact edi=new Edifact("C:/Bollore/Projets/EDI/Cuscar/Samples/PETRA2014092513290026_exemplede fichier en sortie d'Alfa Angola.EDI");
		edi.parseEDI();
		System.out.println(edi.isTest);
		System.out.println(edi.time);
		System.out.println(edi.date);
		System.out.println(edi.edi_type);
		System.out.println(edi.edi_version);
		System.out.println(edi.syntax_id+"     "+edi.syntax_version_number);
		System.out.println(edi.controlling_agency);
		System.out.println(edi.messages.get(0).reference_number);
		System.out.println(edi.interchange_control_reference);
		

			
		}
		


		
	}


