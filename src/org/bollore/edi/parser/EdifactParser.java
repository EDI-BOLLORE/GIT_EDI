package org.bollore.edi.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;

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
		
		//String chaine="";
		
		
		
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
		

		Segment seg_unb= SegmentParser.parse(br.readLine(), String.valueOf(this.element_separator), String.valueOf(this.component_separator));
		Segment seg_unh= SegmentParser.parse(br.readLine(), String.valueOf(this.element_separator), String.valueOf(this.component_separator));
		
		System.out.println(seg_unb.elements.size());
		
		ArrayList<Element> unb_elements=seg_unb.elements;
		ArrayList<Element> unh_elements=seg_unh.elements;
		
		for (int i = 0; i < unb_elements.size(); i++) {
			System.out.println(unb_elements.get(i).value);
			ArrayList<org.bollore.edi.Component> components=unb_elements.get(i).components;
			
			for (int j = 0; j < components.size(); j++) {
				System.out.println(i+" "+j+"  "+components.get(j).value);
			}
		}
		
		
		System.out.println("Coucou");
		Integer isTest=null;
		if(unb_elements.size()>=6){
			System.out.println("Coucou");
			String temp=unb_elements.get(6).value;
			if(Utils.PatternCheck(temp,"[0-9]{1}")){
				System.out.println("Coucou");
				isTest=Integer.parseInt(temp);
			} 
		}
		System.out.println(isTest);
		
		//System.out.println(unb_elements.get(4).components.size());
		//System.out.println(unb_elements.get(4).components.get(0)+""+unb_elements.get(4).components.get(1));
		//Date date_edi=Utils.parseDate("yyMMddHHmm",unb_elements.get(4).components.get(0)+""+unb_elements.get(4).components.get(1));
		//System.out.println(date_edi);
		for (int i = 0; i < unb_elements.size(); i++) {
			System.out.println(i+" value " +unb_elements.get(i).value);
		}
		
//		String edi_version_number=edi_infos[1];
//		String edi_type=edi_infos[0];
//		String edi_year_version=edi_infos[2].substring(0,2);
//		String edi_letter_version=edi_infos[2].substring(2,3);
//		String controlling_agency=edi_infos[3];
//		
//		String syntax=unb_segments.get(1);
//		System.out.println("syntax :"+syntax);
//		String syntax_id=unb_segments.get(1); //UNOC
		String syntax_version_number; // 2
		String interchange_sender_id; //GRIMALDI
		String id_code_qualifier; // ABC comme GRIMALDI:ABC
		String interchange_recipient_id; // SNCUSTOMS
		String interchange_control_reference; //0214
		
		//System.out.println("controlling_agency"+controlling_agency);
		
		
		//System.out.println(Utils.PatternExtract(unb,"([A-Z0-9\\:]+)", 1).get(3));
		//System.out.println(unh);
//		System.out.println(una.substring(3,4));
//		System.out.println(una.substring(4,5));
//		System.out.println(una.substring(5,6));
//		System.out.println(una.substring(6,7));
//		System.out.println(una.substring(7,8));
//		System.out.println(una.substring(8,9));
//		while ((ligne=br.readLine())!=null){
//			System.out.println(ligne);
//			chaine+=ligne+"\n";
//		}
		
		
		/**
		 * Edifact(String filepath, Integer isTest,
			Character element_separator, Character component_separator,
			Character space_character, Character decimal_separator,
			Character escape_character, Character segment_separator,

			String edi_version_number, String edi_type,
			String edi_year_version, String edi_letter_version,
			String controlling_agency,

			String syntax_id, String syntax_version_number,
			String interchange_sender_id, String id_code_qualifier,
			String interchange_recipient_id, Date date,
			String interchange_control_reference)
		 */
//		Edifact edifact=new Edifact(filepath, isTest, element_separator, component_separator, space_char, decimal_separator,
//				escape_char, segment_char, edi_version_number, edi_type, edi_year_version, edi_letter_version,
//				controlling_agency, syntax_id, syntax_version_number, interchange_sender_id, id_code_qualifier,
//				interchange_recipient_id, date_edi, interchange_control_reference);
	
	return result;
	}
	


	
	
	
	
	public static void main(String[] args) throws IOException,EDIException, EDIParseException {

		EdifactParser edi=new EdifactParser("C:/Bollore/Projets/EDI/Talend/Cuscar_Test_Sekou.edi");
		
		edi.readEDI();

	}

}
