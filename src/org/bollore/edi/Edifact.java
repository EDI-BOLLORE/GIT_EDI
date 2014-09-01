package org.bollore.edi;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
	public HashMap<String, Integer> segments_rank;
	
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
			this.structure = new ArrayList<org.bollore.edi.Segment>();
			this.segments = new ArrayList<org.bollore.edi.Segment>();
			
			this.BuildStructureSegment();
			this.segments_rank=this.buildHashSegment();
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
			this.segments_rank=this.buildHashSegment();
			//this.printStructure();
			
		} catch(FileNotFoundException fnfe){ fnfe.printStackTrace();}
	}
	
	
	
	public static void main(String[] args) throws JDOMException, IOException, EDIException
	{
		Edifact edi_cuscar = new Edifact("C:/Temp/Cuscar_Test.edi", "CUSCAR", "95", "B", "MOL32");
		//System.out.println(edi_cuscar.getElement("DTM/C507", null));
		
		System.out.println(edi_cuscar.getSegment("DTM", null)==null);
		
		ArrayList<String> rff1=new ArrayList<String>();
		
		rff1.add("A");
		rff1.add("B");
		rff1.add("C");
		rff1.add("D");
		
		ArrayList<String> rff2=new ArrayList<String>();
		
		rff2.add("E");
		rff2.add("F");
		rff2.add("G");
		rff2.add("H");

		ArrayList<String> dtm=new ArrayList<String>();
		
		dtm.add("I");
		dtm.add("J");
		dtm.add("K");
		
		ArrayList<String> nad1=new ArrayList<String>();
		
		nad1.add("NAD1");
		

		ArrayList<String> nad2=new ArrayList<String>();
		
		nad2.add("NAD21");
		nad2.add("NAD22");
		nad2.add("NAD23");
		
		ArrayList<String> rng=new ArrayList<String>();
		
		rng.add("RNG1");
		rng.add("RNG2");
		rng.add("RNG3");

		edi_cuscar.setValueElement("DTM/C507", dtm, true);
		edi_cuscar.setValueElement("RFF/C506", rff1, true);
		edi_cuscar.setValueElement("RFF/C506", rff2, true);
		edi_cuscar.setValueElement("NAD/3035", nad1, true);
		edi_cuscar.setValueElement("NAD/C082", nad2, false);
		edi_cuscar.setValueElement("GRP2/GRP3/RNG/C280", rng, true);

		
		edi_cuscar.printEDI();
		
		edi_cuscar.print();	

	}
	
	public void setValueElement(String element_path,ArrayList<String> values,Boolean create_new_segment) throws EDIException{
		// On r�cup�re le segment � traiter
		org.bollore.edi.Segment segment=null;
		//System.out.println("setValueElement on a "+this.segments);
		// Si l'on doit cr�er un nouveau segment
		
		//System.out.println(this.segments.get(0).elements.size());
		if(create_new_segment){
			// Si l'on doit cr�� le segment, on l'importe depuis la structure et on le met dans segments
			this.segments.add(this.getSegmentStructure(element_path.split("/")[0]));

		}

		// Sinon on r�cup�re le segment depuis la structure que l'on ajoutera ensuite � l'instance
		
		// On r�cup�re le dernier segment en cours de traitement de l'EDI
		segment=this.segments.get(segments.size()-1);
		// On r�cup�re l'�l�ment que l'on souhaite instancier
		org.bollore.edi.Element element=this.getElement(element_path);
		//L'�l�ment n'a pas de composants
		if((element.components==null||element.components.size()<=0)&&values.size()==1){
			
			element.value=values.get(0);

		} else {
			for (int i = 0; i<element.components.size() ; i++) {				

				element.components.get(i).value=values.get(i);
			}
		}
	}
	
	public org.bollore.edi.Segment getSegment(String segment_path,ArrayList<org.bollore.edi.Segment> segments) throws EDIException{
		org.bollore.edi.Segment result=null;
		
		// On r�cup�re les segments et l'�l�ment en d�coupant la cha�ne
		String[] split=segment_path.split("/");
		
		// Si les segments ne sont pas renseign�s, il s'agit de la premi�re ex�cution
		if(segments==null){	
			segments=this.segments;
		}
		
		// Si le chemin poss�de au moins 2 segments
		if(split.length>1){
			// On cherche le segment correspondant
			for (int i = 0; i < segments.size(); i++) {
				// On a trouv� le bon segment on itere sur l'arraylist du segment correspondant en rappelant la m�thode en tronquant le chemin du composant GRP2/GRP3/RNG/C280 devient GRP3/RNG/C280
				// et en passant en param�tre l'arraylist de segments du segment trouv�
				if(segments.get(i).code.equals(split[0])){

					return this.getSegment(segment_path.substring(segment_path.indexOf("/")+1),segments.get(i).segments);
				}
			}
		// Le chemin poss�de 2 segments s�par�s par un /: On r�cup�re le segment suivant
		} else {	
				// On boucle une derni�re fois pour trouver le segment
				// La boucle se fait � l'envers pour r�cup�rer le dernier segment avec le code recherch�
				for (int j = segments.size()-1; j >=0; j--) {
					// D�s qu'on le trouve, on sort de la boucle
					if(segments.get(j).code.equals(split[0])){
					
						result=segments.get(j);					
						break;					
						}
					}			
				}			
		return result;		
	}
	
	public org.bollore.edi.Element getElement(String element_path) throws EDIException{
		return getElement(element_path,null);
	}
	
	public org.bollore.edi.Element getElement(String element_path,ArrayList<org.bollore.edi.Segment> segments) throws EDIException{
		// Instantiation du retour de la m�thode
		org.bollore.edi.Element result=null;		
		
		// On r�cup�re les segments et l'�l�ment en d�coupant la cha�ne
		String[] split=element_path.split("/");
		
		// Si les segments ne sont pas renseign�s, il s'agit de la premi�re ex�cution
		if(segments==null){
			segments=this.segments;
		}
		
		// Le chemin ne comporte pas a minima SEGMENT/ELEMENT
		if(split.length<2){
			throw new EDIException("Pour r�cup�rer un �l�ment la structure minimale est SEGMENT/ELEMENT. Au moins un '/' doit appara�tre dans le chemin.");
		}
		// Le chemin est de la forme SEGMENT/ELEMENT
		else if(split.length==2){

			// On cherche le segment correspondant
			for (int i = 0; i<segments.size(); i++) {	
				if(segments.get(i).code.equals(split[0])){
					org.bollore.edi.Segment segment=segments.get(i);
					
					ArrayList<org.bollore.edi.Element> elements=segment.elements;
					
					// Si la partie droite correspond � un �l�ment:
					for (int j = elements.size()-1; j>=0; j--) {
						
						if(elements.get(j).code.equals(split[1])){
							
						result = elements.get(j);
						}
					}			
				}
			}
		// Cas ou split.length>2 on it�re sur la fonction
		} else {
			// On cherche le segment correspondant
			for (int i = segments.size()-1; i>=0; i--) {
				// On a trouv� le bon segment on itere sur l'arraylist du segment correspondant en rappelant la m�thode en tronquant le chemin du composant GRP2/GRP3/RNG/C280 devient GRP3/RNG/C280
				// et en passant en param�tre l'arraylist de segments du segment trouv�
				if(segments.get(i).code.equals(split[0])){

					return this.getElement(element_path.substring(element_path.indexOf("/")+1),segments.get(i).segments);
				}
			}
		}
		
		if(result==null){
			throw new EDIException("L'�l�ment '"+element_path+"' pour l'EDI "+this.edi_type+this.edi_year_version+this.edi_letter_version+" n'a pas �t� trouv�");
		}
		return result;		
	}
	
	
	public org.bollore.edi.Segment getSegmentStructure(String segment_name) throws EDIException{

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
			result=(org.bollore.edi.Segment)(this.structure.get(hash_segment.get(segment_name))).clone();
			
		}
		
		return result;				
	
	}

	public void BuildStructureSegment()
	{
		SAXBuilder sxb = new SAXBuilder();
		Document document;
		org.jdom2.Element racine;
		
		try 
		{			
			document = sxb.build("EDI_Definitions/D" + this.edi_year_version + this.edi_letter_version + "/" + this.edi_type + ".xml");
			
			racine = document.getRootElement();
			List<org.jdom2.Element> nodes = racine.getChildren("segments");
			
			for (int i = 0; i < nodes.size(); i++){
				BuildStructureSegmentRecursive(new ArrayList<org.bollore.edi.Segment>(), (nodes.get(i)));

			}
			
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
					//_Segments.add(segments_definition.get(nodes.get(i).getAttributeValue("segcode")));
					_Segments.add(segments_definition.get(nodes.get(i).getAttributeValue("segcode")).clone());
				
				if("segmentGroup".equals(nodes.get(i).getName()))
				{
					org.bollore.edi.Segment segment = new org.bollore.edi.Segment
							(
								
								nodes.get(i).getAttributeValue("xmltag"),"GRP" + (String)nodes.get(i).getAttributeValue("xmltag").split("_")[2],
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
		try {
			this.printHeader();
			this.printSegments();
			this.printfooter();
			this.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		} finally{

			try {
				if(this.printwriter!=null){
					this.printwriter.close();
				}
				
			} catch (Exception e2) {
				System.err.println(e2.getMessage());
				e2.printStackTrace();
			}
		}

	}
		
	public void printHeader()
	{
		this.printwriter.append("UNA"+this.element_separator+this.component_separator+this.decimal_separator+this.escape_character+this.space_character+this.segment_separator+"\n");
	}
	
	public void printSegments(){
		printSegments(this.segments);
	}
	
	public void printSegments(ArrayList<org.bollore.edi.Segment> segments){
		
		// On boucle sur tous les segments � imprimer dans le fichier
		for (int i = 0; i < segments.size(); i++) 
		{
			
			org.bollore.edi.Segment segment=segments.get(i);
			
			if(!segment.isEmpty(segment)){
			
				// Si le segment est un segment de groupe
			if(segment.code.substring(0,3).equals("GRP")){
				printSegments(segment.segments);			
			} 
			// Il s'agit d'un segment simple
			else {
				
				this.printwriter.append(segment.code);

				ArrayList<org.bollore.edi.Element> elements=segment.elements;
				
				for (int j = 0; j<elements.size(); j++) 

				{
					org.bollore.edi.Element element = elements.get(j);
					ArrayList<org.bollore.edi.Component> components = element.components;
					
					//S'il s'agit d'un �l�ment simple on l'�crit dans le fichier
					if(components==null||components.size() <= 0)
					{
						String value=(element.value==null)?"":element.value;

						this.printwriter.append(element_separator + value);
					}
					// L'�l�ment poss�de des composants
					else 
					{
						for (int k = 0; k < components.size(); k++)
						{
							org.bollore.edi.Component component = components.get(k);
							
							String value2=(component.value==null)?"":component.value;
					
							if(k==0){

								this.printwriter.append(this.element_separator +value2 );
								
								} else {
								
								this.printwriter.append(this.component_separator +value2 );
								
								}				
							}					
						}					
					}
				this.printwriter.append(this.segment_separator+"\n");
				
				}
			
			} // if
		}	
	}
	
	public void printfooter()
	{	
		this.printwriter.append("UNT"+this.element_separator+this.segments.size()+this.element_separator+this.message_reference+this.segment_separator+"\n");
		this.printwriter.append("UNZ"+this.element_separator+"1"+this.element_separator+this.segment_separator+"\n");
	}
	
	public void printEDIStructure()
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
	
	public void printEDI()
	{	
		if (this.structure==null) {
			System.out.println(" Y a pas de structure");
		} else{
			for (int i = 0; i < this.segments.size(); i++) 
			{
				((org.bollore.edi.Segment)this.segments.get(i)).printSegment();
			}					
		}
	}	
	
	public void close()
	{
		try {
			if(this.printwriter!=null){
				this.printwriter.close();
			}
			
		} catch (Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}		
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
	
	public HashMap<String, Integer> buildHashSegment(){
		
		HashMap<String, Integer> result=new HashMap<String, Integer>();
		ArrayList<org.bollore.edi.Segment> structure=this.structure;
		
		for (int i = 0; i < structure.size(); i++) 
		{
			result.put((((org.bollore.edi.Segment)structure.get(i)).clone().code), i);
		}
		
		return result;
		
	}
	
	public HashMap<String, Integer> buildHashElement (String segment_name) throws EDIException{
		
		HashMap<String, Integer> result=new HashMap<String, Integer>();
		

			org.bollore.edi.Segment segment=getSegmentStructure(segment_name);
			ArrayList<org.bollore.edi.Element> elements=segment.elements;
			
			for (int i = 0; i < elements.size(); i++) {

				result.put(((org.bollore.edi.Element)elements.get(i)).code, i);
			}
		
		return result;
	}
}

