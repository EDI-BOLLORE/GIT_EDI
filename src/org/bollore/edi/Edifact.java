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
	String edi_type;
	String edi_year_version;
	String edi_letter_version;
	public PrintWriter printwriter;	
	public Character component_separator;
	public Character element_separator;
	public Character decimal_separator;
	public Character escape_character;
	public static Character space_character=' ';
	public Character segment_separator;
	public Integer nb_segments;
	public String message_reference;
	public String message_reference_number;
	public ArrayList<org.bollore.edi.Segment> segments;
	
	
	
	public ArrayList<Segment> getSegments() {
		return segments;
	}

	public void setSegments(ArrayList<Segment> segments) {
		this.segments = segments;
	}

	public void increments_nbsegments(){
		this.nb_segments=this.nb_segments+1;
	}
	
	public Edifact(String filepath,String edi_type,String edi_year_version,String edi_letter_version,Character element_separator,Character subsegment_separator,
			Character decimal_separator,Character escape_character,Character segment_separator,String message_reference,
			String message_reference_number
			 
			)  {
		
		super();
		try {
			
			this.filepath = filepath;
			this.edi_type=edi_type;
			this.edi_year_version=edi_year_version;
			this.printwriter=new PrintWriter(new File(filepath));
			if(element_separator==null){this.element_separator='+';} else {this.element_separator = element_separator;}
			if(decimal_separator==null){this.decimal_separator='.';} else {this.decimal_separator = decimal_separator;}
			if(subsegment_separator==null){this.component_separator=':';} else {this.component_separator = decimal_separator;}
			if(escape_character==null){this.escape_character='?';} else {this.escape_character = decimal_separator;}
			if(segment_separator==null){this.segment_separator='\'';} else {this.segment_separator = segment_separator;}
			this.nb_segments=0;
			this.message_reference=message_reference;
			this.message_reference_number=message_reference_number;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public Edifact(String filepath,String message_reference,String message_reference_number)  {
		try{
		this.filepath = filepath;
		this.printwriter=new PrintWriter(new File(filepath));
		this.element_separator='+';
		this.decimal_separator='.';
		this.component_separator=':';
		this.escape_character='?';
		this.segment_separator='\'';
		this.nb_segments=0;
		this.message_reference=message_reference;
		this.message_reference_number=message_reference_number;
		
		} catch(FileNotFoundException fnfe){ fnfe.printStackTrace();}
		}
	
	public void printHeader(){
		this.printwriter.append("UNA"+this.element_separator+this.component_separator+this.decimal_separator+this.escape_character+this.space_character+this.segment_separator+"\n");
	}
	
	
	public void printfooter(){
		this.printwriter.append("UNT"+this.element_separator+this.nb_segments+this.element_separator+this.message_reference+this.message_reference_number+this.segment_separator+"\n");
		this.printwriter.append("UNZ"+this.element_separator+"1"+this.element_separator+this.message_reference_number+this.segment_separator+"\n");
		
	}
	
	public void close(){
		this.printwriter.close();
	}
	
	public void printStructure(){
		
		for (int i = 0; i < this.segments.size(); i++) {
			((org.bollore.edi.Segment)this.segments.get(i)).printSegment();
		}
		
	}
	

	
	public static HashMap<String, org.bollore.edi.Segment> buildEDISegmentDefinition(){
	SAXBuilder sxb = new SAXBuilder();
	Document document;
	org.jdom2.Element racine;
	
	HashMap<String, org.bollore.edi.Segment> segments=new HashMap<String, org.bollore.edi.Segment>();
	
	
	try
    {
       document = sxb.build("EDI_Definitions/D95B/__modelset_definitions.xml");
       
       
       racine = document.getRootElement();
       List<org.jdom2.Element> nodes=racine.getChildren("segments");


       for (int i = 0; i < nodes.size(); i++) {    	   
    	   
    	  // On récupère la liste de tous les segments
    	   List<org.jdom2.Element> segts=((org.jdom2.Element)nodes.get(i)).getChildren("segment");
    	   
    	  for (int j = 0; j < segts.size(); j++) {		
			
    		 //Pour chaque segments on récupère la liste des éléments
    		   List<org.jdom2.Element> field=((org.jdom2.Element)segts.get(j)).getChildren("field");
    		   
    		 ArrayList<org.bollore.edi.Element> elements = new ArrayList<org.bollore.edi.Element>();  
			 
    		 for (int l = 0; l < field.size(); l++) {
				 
				 List<org.jdom2.Element> documentation=((org.jdom2.Element)field.get(l)).getChildren("documentation");
				 				 
				 String doc=(documentation.size()>0)?documentation.get(0).getText():null;
				 
					 // Je récupère la doc de l'élément
					 
					 List<org.jdom2.Element> component=((org.jdom2.Element)field.get(l)).getChildren("component");
					 ArrayList<Component> components=new ArrayList<Component>();
					 // On travaille avec un élément qui possède des composants
					 if(component.size()>0){						 
						 
						 for (int m = 0; m < component.size(); m++) {
							 
							 components.add(new Component(((org.jdom2.Element)component.get(m)).getAttributeValue("dataType"), ((org.jdom2.Element)component.get(m)).getAttributeValue("maxLength"),((org.jdom2.Element)component.get(m)).getAttributeValue("minLength"),
									 "true".equals(((org.jdom2.Element)component.get(m)).getAttributeValue("required")),"true".equals(((org.jdom2.Element)component.get(m)).getAttributeValue("truncatable")), ((org.jdom2.Element)component.get(m)).getAttributeValue("xmltag"),
									 (String)((org.jdom2.Element)component.get(m)).getChildren("documentation").get(0).getText()));							
						}						 
						 
						// On travaille avec un élément qui ne possède pas de composants 
					 } else {
					 
					 
					 }					 

					 elements.add(new org.bollore.edi.Element(((org.jdom2.Element)field.get(l)).getAttributeValue("nodeTypeRef"),
							 "true".equals(((org.jdom2.Element)field.get(l)).getAttributeValue("required")),
							 "true".equals(((org.jdom2.Element)field.get(l)).getAttributeValue("truncatable")),
							 ((org.jdom2.Element)field.get(l)).getAttributeValue("xmltag"),
							 doc,
							 components));				 
					 
				
				 
			 }
			 
			//System.out.println(((org.jdom2.Element)segts.get(j)).getAttributeValue("segcode")+" "+((org.jdom2.Element)segts.get(j)).getAttributeValue("xmltag")+" "+((org.jdom2.Element)segts.get(j)).getAttributeValue("minOccurs")+" "+((org.jdom2.Element)segts.get(j)).getAttributeValue("maxOccurs")+" "+((org.jdom2.Element)segts.get(j)).getAttributeValue("description"));
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
	
	public static void BuildEdifact(String edi_type,String edi_year_version,String edi_letter_version)
	{
		SAXBuilder sxb = new SAXBuilder();
		Document document;
		org.jdom2.Element racine;
		org.bollore.edi.Edifact edi;
		
		try 
		{
			ArrayList<org.bollore.edi.Segment> result 	= new ArrayList<org.bollore.edi.Segment>();
			ArrayList<org.bollore.edi.Segment> segments = new ArrayList<org.bollore.edi.Segment>();
			
			HashMap<String,org.bollore.edi.Segment> segments_definition = Edifact.buildEDISegmentDefinition();
			
			document = sxb.build("EDI_Definitions/D"+edi_year_version+edi_letter_version+"/"+edi_type+".xml");
			
			racine = document.getRootElement();
			List<org.jdom2.Element> nodes = racine.getChildren("segments");
			
			for (int i = 0; i < nodes.size(); i++)
			{
				segments = buildSegment(new ArrayList<org.bollore.edi.Segment>(), nodes.get(i));
				
				for (int j = 0; j < segments.size(); j++) 
					result.add(segments.get(j));		
			}
			
			for (int k = 0; k < result.size(); k++) 
				((org.bollore.edi.Segment) result.get(k)).printSegment();
			
		}  
		catch(JDOMException  jdome)
		{
	    	jdome.printStackTrace();
		} 
		catch(IOException ioe)
		{
			ioe.printStackTrace();
		}
	}
	
	
	public static ArrayList<org.bollore.edi.Segment> buildSegment( ArrayList<org.bollore.edi.Segment> segments, org.jdom2.Element node)
	{
		//ArrayList<org.bollore.edi.Segment> result = new ArrayList<org.bollore.edi.Segment>();
		
		HashMap<String,org.bollore.edi.Segment> segments_definition=Edifact.buildEDISegmentDefinition();
		List<org.jdom2.Element> nodes = node.getChildren();
		
		for (int i = 0; i < nodes.size(); i++) 
		{   
			if("segment".equals(nodes.get(i).getName()) || "segmentGroup".equals(nodes.get(i).getName()))
			{

				if("segment".equals(nodes.get(i).getName()))
					segments.add(segments_definition.get(nodes.get(i).getAttributeValue("segcode")));
				
				if("segmentGroup".equals(nodes.get(i).getName()))
					buildSegment(segments, nodes.get(i));
			}
		}

		return segments;
	}
		

	public static void main(String[] args) throws JDOMException, IOException
	{
		BuildEdifact("CUSCAR", "95","B");
	}
	
	
}
