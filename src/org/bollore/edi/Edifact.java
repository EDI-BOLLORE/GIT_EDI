package org.bollore.edi;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

public class Edifact {
	
	
	public String filepath;
	public String edi_type;
	public String edi_year_version;
	public String edi_letter_version;
	public PrintWriter printwriter;	
	public Character component_separator;
	public Character element_separator;
	public Character decimal_separator;
	public Character escape_character;
	public static Character space_character=' ';
	public Character segment_separator;
	public String message_reference;
	public ArrayList<org.bollore.edi.Segment> structure;
	public ArrayList<org.bollore.edi.Segment> segments;
	
/*********************************************
 * 
 *  Constructeurs de la classe Edifact
 *  
 * *******************************************/
	
	public Edifact(String filepath,String edi_type,String edi_year_version,String edi_letter_version,Character element_separator,Character component_separator,
			Character decimal_separator,Character escape_character,Character segment_separator,String message_reference)  {
		
		super();
		try {
			
			this.filepath = filepath;
			this.edi_type=edi_type;
			this.edi_year_version=edi_year_version;
			this.printwriter=new PrintWriter(new File(filepath));
			if(element_separator==null){this.element_separator='+';} else {this.element_separator = element_separator;}
			if(decimal_separator==null){this.decimal_separator='.';} else {this.decimal_separator = decimal_separator;}
			if(component_separator==null){this.component_separator=':';} else {this.component_separator = component_separator;}
			if(escape_character==null){this.escape_character='?';} else {this.escape_character = decimal_separator;}
			if(segment_separator==null){this.segment_separator='\'';} else {this.segment_separator = segment_separator;}
			this.message_reference=message_reference;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public Edifact(String filepath,String edi_type,String edi_year_version,String edi_letter_version,String message_reference)  {
		try{
			
		this.filepath = filepath;
		this.edi_type=edi_type;
		this.edi_year_version=edi_year_version;
		this.edi_letter_version=edi_letter_version;
		this.printwriter=new PrintWriter(new File(filepath));
		this.element_separator='+';
		this.decimal_separator='.';
		this.component_separator=':';
		this.escape_character='?';
		this.segment_separator='\'';
		this.message_reference=message_reference;
		
		} catch(FileNotFoundException fnfe){ fnfe.printStackTrace();}
		}
	
	public void printHeader(){
		this.printwriter.append("UNA"+this.element_separator+this.component_separator+this.decimal_separator+this.escape_character+this.space_character+this.segment_separator+"\n");
	}
	
	
//	public void printfooter(){
//		this.printwriter.append("UNT"+this.element_separator+this.nb_segments+this.element_separator+this.message_reference+this.message_reference_number+this.segment_separator+"\n");
//		this.printwriter.append("UNZ"+this.element_separator+"1"+this.element_separator+this.message_reference_number+this.segment_separator+"\n");
//		
//	}
	
	public void close(){
		this.printwriter.close();
	}
	
	public void printStructure(){
		
		for (int i = 0; i < this.structure.size(); i++) {
			((org.bollore.edi.Segment)this.structure.get(i)).printSegment();
		}
		
	}
	

	
	public static HashMap<String, org.bollore.edi.Segment> buildEDISegmentDefinition(String edi_year_version,String edi_letter_version){
	SAXBuilder sxb = new SAXBuilder();
	Document document;
	org.jdom2.Element racine;
	
	HashMap<String, org.bollore.edi.Segment> segments=new HashMap<String, org.bollore.edi.Segment>();
	
	try
    {
       document = sxb.build("EDI_Definitions/D"+edi_year_version+edi_letter_version+"/__modelset_definitions.xml");       
       
       racine = document.getRootElement();
       List<org.jdom2.Element> nodes=racine.getChildren("segments");


       for (int i = 0; i < nodes.size(); i++) {    	   
    	   
    	  // On r�cup�re la liste de tous les segments
    	   List<org.jdom2.Element> segts=((org.jdom2.Element)nodes.get(i)).getChildren("segment");
    	   
    	  for (int j = 0; j < segts.size(); j++) {		
			
    		 //Pour chaque segments on r�cup�re la liste des �l�ments
    		   List<org.jdom2.Element> field=((org.jdom2.Element)segts.get(j)).getChildren("field");
    		   
    		 ArrayList<org.bollore.edi.Element> elements = new ArrayList<org.bollore.edi.Element>();  
			 
    		 for (int l = 0; l < field.size(); l++) {
				 
				 List<org.jdom2.Element> documentation=((org.jdom2.Element)field.get(l)).getChildren("documentation");
				 				 
				 String doc=(documentation.size()>0)?documentation.get(0).getText():null;
				 
					 // Je r�cup�re la doc de l'�l�ment
					 
					 List<org.jdom2.Element> component=((org.jdom2.Element)field.get(l)).getChildren("component");
					 ArrayList<Component> components=new ArrayList<Component>();
					 // On travaille avec un �l�ment qui poss�de des composants
					 if(component.size()>0){						 
						 
						 for (int m = 0; m < component.size(); m++) {
							 
							 components.add(new Component(((org.jdom2.Element)component.get(m)).getAttributeValue("dataType"), ((org.jdom2.Element)component.get(m)).getAttributeValue("maxLength"),((org.jdom2.Element)component.get(m)).getAttributeValue("minLength"),
									 "true".equals(((org.jdom2.Element)component.get(m)).getAttributeValue("required")),"true".equals(((org.jdom2.Element)component.get(m)).getAttributeValue("truncatable")), ((org.jdom2.Element)component.get(m)).getAttributeValue("xmltag"),
									 (String)((org.jdom2.Element)component.get(m)).getChildren("documentation").get(0).getText()));							
						}						 
						 
						// On travaille avec un �l�ment qui ne poss�de pas de composants 
					 } else {
					 
					 
					 }					 

					 elements.add(new org.bollore.edi.Element(((org.jdom2.Element)field.get(l)).getAttributeValue("nodeTypeRef"),
							 "true".equals(((org.jdom2.Element)field.get(l)).getAttributeValue("required")),
							 "true".equals(((org.jdom2.Element)field.get(l)).getAttributeValue("truncatable")),
							 ((org.jdom2.Element)field.get(l)).getAttributeValue("xmltag"),
							 doc,
							 components));				 
					 
				
				 
			 }
			 
			segments.put(((org.jdom2.Element)segts.get(j)).getAttributeValue("segcode"),
					new org.bollore.edi.Segment(((org.jdom2.Element)segts.get(j)).getAttributeValue("xmltag"),
							((org.jdom2.Element)segts.get(j)).getAttributeValue("segcode"),
							Integer.parseInt(((org.jdom2.Element)segts.get(j)).getAttributeValue("minOccurs")),
							Integer.parseInt(((org.jdom2.Element)segts.get(j)).getAttributeValue("maxOccurs")),
							((org.jdom2.Element)segts.get(j)).getAttributeValue("description"),
							elements,null)					
					);		 
			 
    	   }


       }        
    }
    catch(JDOMException  jdome){
    	jdome.printStackTrace();
		} 
	catch(IOException ioe){
		ioe.printStackTrace();
	}

		return segments;
	
	}
	
	public void BuildEdifactSegments()
	{
		SAXBuilder sxb = new SAXBuilder();
		Document document;
		org.jdom2.Element racine;
		org.bollore.edi.Edifact edi;
		ArrayList<org.bollore.edi.Segment> result 	= new ArrayList<org.bollore.edi.Segment>();
		ArrayList<org.bollore.edi.Segment> segments = new ArrayList<org.bollore.edi.Segment>();
		
		try 
		{			
			HashMap<String,org.bollore.edi.Segment> segments_definition = Edifact.buildEDISegmentDefinition(this.edi_year_version,this.edi_letter_version);
			
			document = sxb.build("EDI_Definitions/D"+this.edi_year_version+this.edi_letter_version+"/"+this.edi_type+".xml");
			
			racine = document.getRootElement();
			List<org.jdom2.Element> nodes = racine.getChildren("segments");
			
			for (int i = 0; i < nodes.size(); i++)
			{
				segments = buildSegment(new ArrayList<org.bollore.edi.Segment>(), nodes.get(i),this.edi_year_version,this.edi_letter_version);
				
				for (int j = 0; j < segments.size(); j++) 
					result.add(segments.get(j));		
			}
			
//			for (int k = 0; k < result.size(); k++) 
//				((org.bollore.edi.Segment) result.get(k)).printSegment();
			
		}  
		catch(JDOMException  jdome)
		{
	    	jdome.printStackTrace();
		} 
		catch(IOException ioe)
		{
			ioe.printStackTrace();
		}
		
		this.structure=result;
	}
	
	
	public static ArrayList<org.bollore.edi.Segment> buildSegment( ArrayList<org.bollore.edi.Segment> segments, org.jdom2.Element node,String edi_year_version,String edi_letter_version)
	{
		//ArrayList<org.bollore.edi.Segment> result = new ArrayList<org.bollore.edi.Segment>();
		
		HashMap<String,org.bollore.edi.Segment> segments_definition=Edifact.buildEDISegmentDefinition(edi_year_version,edi_letter_version);
		List<org.jdom2.Element> nodes = node.getChildren();
		
		for (int i = 0; i < nodes.size(); i++) 
		{   
			if("segment".equals(nodes.get(i).getName()) || "segmentGroup".equals(nodes.get(i).getName()))
			{

				if("segment".equals(nodes.get(i).getName()))
					segments.add(segments_definition.get(nodes.get(i).getAttributeValue("segcode")));
				
				if("segmentGroup".equals(nodes.get(i).getName()))
					buildSegment(segments, nodes.get(i),edi_year_version,edi_letter_version);
			}
		}

		return segments;
	}
	
	public void setValue(String segment_name,String element_name,ArrayList<String> values){
		ArrayList<org.bollore.edi.Segment> structure=this.structure;
		
	}
	
	public HashMap<String, Integer> buildHashSegment(){
		
		HashMap<String, Integer> result=new HashMap<String, Integer>();
		ArrayList<org.bollore.edi.Segment> structure=this.structure;
		
		for (int i = 0; i < structure.size(); i++) {
			//System.out.println(i+"   "+((org.bollore.edi.Segment)structure.get(i)).name);
			result.put(((org.bollore.edi.Segment)structure.get(i)).code, i);
		}
		
		return result;
		
	}
	
	public HashMap<String, Integer> buildHashElement (String segment_name) throws EDIException{
		
		HashMap<String, Integer> result=new HashMap<String, Integer>();
		

			org.bollore.edi.Segment segment=getSegment(segment_name);
			ArrayList<org.bollore.edi.Element> elements=segment.elements;
			
			for (int i = 0; i < elements.size(); i++) {
				//System.out.println(i+"   "+((org.bollore.edi.Element)elements.get(i)).type_ref);
				result.put(((org.bollore.edi.Element)elements.get(i)).type_ref, i);
			}
		
		return result;
		
	}
	

	public void setValue(String element_path,String value) throws EDIException{
		this.getElement(element_path).value=value;
	}
	

	public org.bollore.edi.Element getElement(String segment_name,String element_name) throws EDIException{
	ArrayList<org.bollore.edi.Segment> structure=this.structure;
	org.bollore.edi.Element result=new org.bollore.edi.Element();
	HashMap<String, Integer> hash_element=this.buildHashElement(segment_name);
	
	if(element_name==null){
	throw new EDIException("L'�l�ment a r�cup�rer doit avoir un nom non null");
	}

	
	else if(!hash_element.containsKey(element_name)){
		throw new EDIException("L'�l�ment "+element_name+" dans le segment "+segment_name+" n'existe pas dans la d�finition du "+this.edi_type+" "+this.edi_year_version+this.edi_letter_version);
	} else{
				
		result=((org.bollore.edi.Segment)this.getSegment(segment_name)).elements.get(hash_element.get(element_name));

	}
	
	return result;		

}
	
	public org.bollore.edi.Element getElement(String element_path) throws EDIException{
		
		String[] split=element_path.split("/");
		
		return getElement(split[0],split[1]);
	}
	
	
	
	public org.bollore.edi.Segment getSegment(String segment_name) throws EDIException{
		ArrayList<org.bollore.edi.Segment> structure=this.structure;
		org.bollore.edi.Segment result=new Segment();
		HashMap<String, Integer> hash_segment=this.buildHashSegment();
		
		if(segment_name==null){
		throw new EDIException("Le segment a r�cup�rer doit avoir un nom non null");
		}
		else if(structure.size()==0){
		throw new EDIException("L'EDI "+this.edi_type+this.edi_letter_version+" ne contient pas de segments "+segment_name);
		}
		
		else if(!hash_segment.containsKey(segment_name)){
			throw new EDIException("Le segment "+segment_name+" n'existe pas dans la d�finition du "+this.edi_type+" "+this.edi_year_version+this.edi_letter_version);
		} else{
			result=this.structure.get(this.buildHashSegment().get(segment_name));
		}
		
		return result;				
	
	}
		

	public static void main(String[] args) throws JDOMException, IOException, EDIException
	{
		Edifact edi_cuscar=new Edifact("C:/Temp/Cuscar_Test.edi","CUSCAR","95","B","MOL32");
		
		edi_cuscar.BuildEdifactSegments();
//		edi_cuscar.printStructure();
		
		
		edi_cuscar.buildHashSegment();
		//System.out.println(edi_cuscar.getSegment("Tata").name);
		
		edi_cuscar.buildHashElement("BGM");
		
		//org.bollore.edi.Element element=edi_cuscar.getElement("BGM","1004");
		org.bollore.edi.Element element=edi_cuscar.getElement("BGM/1004");
		edi_cuscar.setValue("BGM/1004", "Tata");
		System.out.println(element.type_ref);
		System.out.println(element.label);
		System.out.println(element.value);
	}	
}
