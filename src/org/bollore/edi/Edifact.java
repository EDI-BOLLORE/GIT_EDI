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
	public Character space_character=' ';
	public Character segment_separator;
	public String message_reference;
	public ArrayList<org.bollore.edi.Segment> structure;
	public ArrayList<org.bollore.edi.Segment> segments;
	public Integer nb_segment=0;
	
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

	public Edifact(String filepath,String edi_type,String edi_year_version,String edi_letter_version,String message_reference) 
	{
		try
		{		
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
			this.structure = new ArrayList<org.bollore.edi.Segment>();
			this.segments = new ArrayList<org.bollore.edi.Segment>();
			
			this.BuildStructureSegment();
			//this.printStructure();
			
		} catch(FileNotFoundException fnfe){ fnfe.printStackTrace();}
	}
	
	
	
	public static void main(String[] args) throws JDOMException, IOException, EDIException
	{
		Edifact edi_cuscar = new Edifact("C:/Temp/Cuscar_Test.edi", "CUSCAR", "95", "B", "MOL32");  

		ArrayList<String> input=new ArrayList<String>();
		
		input.add("A");
		input.add("B");
		input.add("C");
		input.add("D");
		
		ArrayList<String> input2=new ArrayList<String>();
		
		input2.add("E");
		input2.add("F");
		input2.add("G");
		input2.add("H");

		ArrayList<String> input3=new ArrayList<String>();
		
		input3.add("I");
		input3.add("J");
		input3.add("K");
				
		org.bollore.edi.Segment segment = edi_cuscar.getSegment_Xav("RFF");
		edi_cuscar.segments.add(segment);
		
		org.bollore.edi.Segment segment3 = edi_cuscar.getSegment_Xav("DTM");
		edi_cuscar.segments.add(segment3);
		
		org.bollore.edi.Segment segment2 = edi_cuscar.getSegment_Xav("RFF");
		edi_cuscar.segments.add(segment2);
		
		segment.getElement_Xav("C506").addValue(input);
		segment.printSegment();
		segment3.getElement_Xav("C507").addValue(input3);
		segment3.printSegment();
		segment2.getElement_Xav("C506").addValue(input2);
		segment2.printSegment();
		segment.printSegment();
		
		
		edi_cuscar.print();
	}
	
	
	public void BuildStructureSegment()
	{
		SAXBuilder sxb = new SAXBuilder();
		Document document;
		org.jdom2.Element racine;
		
		try 
		{			
			//HashMap<String, org.bollore.edi.Segment> segments = this.buildEDISegmentDefinition();
			
			document = sxb.build("EDI_Definitions/D" + this.edi_year_version + this.edi_letter_version + "/" + this.edi_type + ".xml");
			
			racine = document.getRootElement();
			List<org.jdom2.Element> nodes = racine.getChildren("segments");
			
			for (int i = 0; i < nodes.size(); i++)
				BuildStructureSegmentRecursive(new ArrayList<org.bollore.edi.Segment>(), nodes.get(i));	
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
	
	
	public void BuildStructureSegmentRecursive(ArrayList<org.bollore.edi.Segment> _Segments, org.jdom2.Element _Node)
	{
		HashMap<String,org.bollore.edi.Segment> segments_definition = this.buildStructureSegmentDefinition();	
		List<org.jdom2.Element> nodes = _Node.getChildren();
		
		for (int i = 0; i < nodes.size(); i++) 
		{   
			if("segment".equals(nodes.get(i).getName()) || "segmentGroup".equals(nodes.get(i).getName()))
			{
				if("segment".equals(nodes.get(i).getName()))
					_Segments.add(segments_definition.get(nodes.get(i).getAttributeValue("segcode")));
				
				if("segmentGroup".equals(nodes.get(i).getName()))
				{
					org.bollore.edi.Segment segment = new org.bollore.edi.Segment
							(
								"GRP" + nodes.get(i).getAttributeValue("xmltag").split("_")[2],
								nodes.get(i).getAttributeValue("xmltag"),
								Integer.parseInt(nodes.get(i).getAttributeValue("minOccurs")),
								Integer.parseInt(nodes.get(i).getAttributeValue("maxOccurs")),
								""
							); 
					BuildStructureSegmentRecursive(segment.segments, nodes.get(i));
					_Segments.add(segment);
				}
			}
		}
		
		this.structure = _Segments;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public void print()
	{	
		this.printHeader();
		this.printSegment();
		this.printfooter();
		this.close();
	}
		
	public void printHeader()
	{
		this.printwriter.append("UNA"+this.element_separator+this.component_separator+this.decimal_separator+this.escape_character+this.space_character+this.segment_separator+"\n");
	}
	
	public void printSegment()
	{
		for (int i = 0; i < this.segments.size(); i++) 
		{
			org.bollore.edi.Segment segment = this.segments.get(i);
			this.printwriter.append(segment.code);
			
			ArrayList<org.bollore.edi.Element> elements=segment.elements;	
			for (int j = 0; j < elements.size(); j++) 
			{
				org.bollore.edi.Element element = elements.get(j);
				ArrayList<org.bollore.edi.Component> components = element.components;
				
				if(components.size() == 0)
				{
					this.printwriter.append(element_separator + element.value);
				} 
				else 
				{
					for (int k = 0; k < components.size(); k++) 
					{
						org.bollore.edi.Component component = components.get(k);			
						this.printwriter.append(this.component_separator + component.value);
					}
				
				}
				
				this.printwriter.append("\n");
				this.nb_segment++;
			}
		}
	}
	
	public void printfooter()
	{
		this.printwriter.append("UNT"+this.element_separator+this.nb_segment+this.element_separator+this.message_reference+this.segment_separator+"\n");
		this.printwriter.append("UNZ"+this.element_separator+"1"+this.element_separator+this.segment_separator+"\n");	
	}
	
	public void printStructure()
	{	
		if (this.structure==null) {
			System.out.println(" Y a pas de structure");
		} else{
			for (int i = 0; i < this.structure.size(); i++) 
			{
				((org.bollore.edi.Segment)this.structure.get(i)).printSegment();
			}					
		}
	}
	
	
	public void close()
	{
		this.printwriter.close();
	}
	

	

	
	public org.bollore.edi.Segment getSegment_Xav(String _CodeSegment)
	{
		org.bollore.edi.Segment segment = new org.bollore.edi.Segment();
		for (int i = 0; i < this.structure.size(); i++) 
		{
			if (this.structure.get(i).code.equals(_CodeSegment))
				segment = this.structure.get(i);
		}
		
		return segment;
	}
	

	public HashMap<String, org.bollore.edi.Segment> buildStructureSegmentDefinition()
	{
		SAXBuilder sxb = new SAXBuilder();
		Document document;
		org.jdom2.Element racine;
	
		HashMap<String, org.bollore.edi.Segment> segments=new HashMap<String, org.bollore.edi.Segment>();
	
		try
		{
			document = sxb.build("EDI_Definitions/D" + this.edi_year_version + this.edi_letter_version + "/__modelset_definitions.xml");       
       
	       racine = document.getRootElement();
	       List<org.jdom2.Element> nodes = racine.getChildren("segments");
	
	
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
								elements,new ArrayList<Segment>())					
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
	
	
	
	
	

	
//	public void setValue(String element_path,ArrayList<String> values) throws EDIException{
//
//		
//		// On ajoute le segment correspondant depuis structure vers segments
//		//org.bollore.edi.Segment segment=this.structure.get(this.buildHashSegment()).get(element_path.substring(0, element_path.indexOf("/")));
//		
//		// Si le segment que l'on traite 
//		if(this.segments==null){
//		this.segments=new ArrayList<org.bollore.edi.Segment>();		
//		}
//		this.segments.add(segment);
//		
//		// On affecte la valeur de l'�l�ment contenu dans le segment
//		ArrayList<org.bollore.edi.Component> components=this.getElement(element_path).components;
//		if(components==null&&values.size()==1){
//			//L'�l�ment n'est pas compos�
//			System.out.println("L'�l�ment n'est pas compos�");
//			this.getElement(element_path).value=values.get(0);
//		} else if(components.size()==values.size()&&values.size()>1){
//			System.out.println("L'�l�ment est compos�");
//			// L'�l�ment est compos�. On doit affacter toutes les valeurs � tous les composants
//			for (int i = 0; i < components.size(); i++) {
//				this.getElement(element_path).components.get(i).value=values.get(i);
//			}
//			
//			
//		} else {
//			throw new EDIException("Le nombre d'arguments ne correspond pas au nombre de composants pour l'�l�ment "+element_path);
//		}
//		
//		//this.getElement(element_path).value=value;
//
//	}
	
	public void setValue(String segment_name,String element_name,ArrayList<String> values){
		ArrayList<org.bollore.edi.Segment> structure=this.structure;
		
	}
	
	public HashMap<String, Integer> buildHashSegment(){
		
		HashMap<String, Integer> result=new HashMap<String, Integer>();
		ArrayList<org.bollore.edi.Segment> structure=this.structure;
		
		for (int i = 0; i < structure.size(); i++) 
		{
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
	

	public org.bollore.edi.Element getElement(String element_path) throws EDIException{
		String[] split=element_path.split("/");
		
	ArrayList<org.bollore.edi.Segment> structure = this.structure;
	org.bollore.edi.Element result=new org.bollore.edi.Element();
	HashMap<String, Integer> hash_element=this.buildHashElement(split[0]);
	
	if(split[1]==null){
	throw new EDIException("L'�l�ment a r�cup�rer doit avoir un nom non null");
	}

	
	else if(!hash_element.containsKey(split[1])){
		throw new EDIException("L'�l�ment "+split[1]+" dans le segment "+split[0]+" n'existe pas dans la d�finition du "+this.edi_type+" "+this.edi_year_version+this.edi_letter_version);
	} else{
				
		result=((org.bollore.edi.Segment)this.getSegment(split[0])).elements.get(hash_element.get(split[1]));

	}
	
	return result;		

}

	
	
	
	public org.bollore.edi.Segment getSegment(String segment_name) throws EDIException{

		org.bollore.edi.Segment result=new org.bollore.edi.Segment();
		
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
		

		
}

