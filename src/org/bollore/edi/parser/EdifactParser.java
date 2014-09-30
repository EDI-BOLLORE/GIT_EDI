package org.bollore.edi.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.Semaphore;

import org.bollore.edi.*;

public class EdifactParser {

	public String filepath;
	public Character element_separator;
	public Character component_separator;
	public Character decimal_separator;
	public Character escape_char;
	public Character space_char;
	public Character segment_char;
	
	public EdifactParser(String filepath, Character element_separator,
			Character component_separator, Character decimal_separator,
			Character escape_char, Character space_char, Character segment_char) {
		super();
		this.filepath = filepath;
		this.element_separator = element_separator;
		this.component_separator = component_separator;
		this.decimal_separator = decimal_separator;
		this.escape_char = escape_char;
		this.space_char = space_char;
		this.segment_char = segment_char;
	}


	public EdifactParser(String filepath) {
		super();
		this.filepath = filepath;
	}
	
	
	
	
	public Edifact readEDI() throws IOException, EDIException, EDIParseException{
		
		Edifact result=new Edifact(this.filepath);
		File edi=new File(this.filepath);		
		
		InputStream ips=new FileInputStream(this.filepath); 
		InputStreamReader ipsr=new InputStreamReader(ips);
		
		BufferedReader br=new BufferedReader(ipsr);

		String una=br.readLine();
		this.element_separator=una.charAt(3);
		this.component_separator=una.charAt(4);
		this.decimal_separator=una.charAt(5);
		this.escape_char=una.charAt(6);
		this.space_char=una.charAt(7);
		this.segment_char=una.charAt(8);
		
		String unb_line=br.readLine();
		String unh_line=br.readLine();
		
		Segment seg_unb= SegmentParser.parse(null,unb_line, String.valueOf(this.element_separator), String.valueOf(this.component_separator), String.valueOf(this.escape_char));
		Segment seg_unh= SegmentParser.parse(null,unh_line, String.valueOf(this.element_separator), String.valueOf(this.component_separator), String.valueOf(this.escape_char));
		
		ArrayList<Element> unb_elements=seg_unb.elements;
		ArrayList<Element> unh_elements=seg_unh.elements;

		
		String edi_version=unh_elements.get(1).components.get(1).value+unh_elements.get(1).components.get(2).value.substring(0,3);
		String edi_type=unh_elements.get(1).components.get(0).value;
		
		result=new Edifact(filepath,edi_type,edi_version);
		result.BuildStructureSegment();
		
		HashMap<String,org.bollore.edi.Segment> seg_def=result.buildStructureSegmentDefinition();
		
		String message_ref_num=unh_elements.get(0).value;
		
		
		if(unb_elements.get(0).components!=null&&unb_elements.get(0).components.size()==2) {
		//UNOC
		result.syntax_id=unb_elements.get(0).components.get(0).value;
		// 2
		result.syntax_version_number=unb_elements.get(0).components.get(1).value;
		} else {
			result.syntax_id=unb_elements.get(0).value;
		}
		
		if(unb_elements.get(1).components!=null&&unb_elements.get(1).components.size()==2){
			//GRIMALDI
			result.interchange_sender_id=unb_elements.get(1).components.get(0).value;
			// ABC comme GRIMALDI:ABC
			result.id_code_qualifier=unb_elements.get(1).components.get(1).value;
		} else {
			result.interchange_sender_id=unb_elements.get(1).value;
		}

		//UN
		result.controlling_agency=unh_elements.get(1).components.get(3).value;
		//140917
		result.date=unb_elements.get(3).components.get(0).value;
		//1633
		result.time=unb_elements.get(3).components.get(1).value;
		//0214
		result.interchange_control_reference=unh_elements.get(0).value;
		// SNCUSTOMS
		result.interchange_recipient_id=unb_elements.get(2).value;
		
		// On ajoute un nouveau message lié à la référence contenue dans UNH
		Message cur_message=new Message(message_ref_num);
		result.messages.add(cur_message);
		
		// Récupération des segments de données
		String cur_seg;
		while(!"UNT".equals(SegmentParser.getSegmentCode(cur_seg=br.readLine()))) {
		cur_message.addSegment(SegmentParser.parse(seg_def,cur_seg, String.valueOf(this.element_separator), String.valueOf(this.component_separator), String.valueOf(this.escape_char)));
		
		}		

	return result;

	}
	

	
	public static void main(String[] args) throws IOException,EDIException, EDIParseException {

		EdifactParser edi=new EdifactParser("TestFiles/TestCuscar.edi");
		
		Edifact edifact=edi.readEDI();
	
		
		Segment bgm=edifact.getSegmentStructure("BGM");
		System.out.println(bgm.code+bgm.name+bgm.max_occurence);
		
		//edifact.filepath="C:/Temp/test.edi";
		edifact.printEDI();
//		System.out.println(edifact.structure.get(2).code);
//		System.out.println(edifact.structure.get(2).description);
//		System.out.println(edifact.structure.get(2).name);
//		System.out.println(edifact.structure.get(2).min_occurence);
//		System.out.println(edifact.structure.get(2).max_occurence);
//		
//		System.out.println(edifact.messages.get(0).segments.get(1).code);
//		System.out.println(edifact.messages.get(0).segments.get(1).description);
//		System.out.println(edifact.messages.get(0).segments.get(1).name);
//		System.out.println(edifact.messages.get(0).segments.get(1).min_occurence);
//		System.out.println(edifact.messages.get(0).segments.get(1).max_occurence);
	}

}
