package org.bollore.edi;

import org.jdom2.JDOMException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.xpath.XPathExpressionException;

public class Segment implements Cloneable{

	String name;
	String code;
	Integer min_occurence;
	Integer max_occurence;
	String description;
	ArrayList<org.bollore.edi.Element> elements;
	ArrayList<org.bollore.edi.Segment> segments;
	
	//Constructeur par copie
//	public Segment(Segment segment) {
//		super();
//		this.name = segment.name;
//		this.code = segment.code;
//		this.min_occurence = segment.min_occurence;
//		this.max_occurence = segment.max_occurence;
//		this.description = segment.description;
//		this.elements = segment.elements;
//		this.segments = segment.segments;
//	}
	
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
			Integer max_occurence, String description) 
	{
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
	
	
	public org.bollore.edi.Segment clone() {
		org.bollore.edi.Segment result=null;
		
		try {
			ArrayList<org.bollore.edi.Element> elements=new ArrayList<org.bollore.edi.Element>();
			ArrayList<org.bollore.edi.Segment> segments=new ArrayList<org.bollore.edi.Segment>();
			
			result=(org.bollore.edi.Segment)super.clone();
			result.code=this.code;
			result.min_occurence=this.min_occurence;
			result.max_occurence=this.max_occurence;
			result.description=this.description;
			
			//On clone les éléments
			for (int i = 0; i < this.elements.size(); i++) {
				elements.add(this.elements.get(i).clone());
			}
			
			//On clone les segments
			for (int j = 0; j < this.segments.size(); j++) {
				segments.add(this.segments.get(j).clone());
			}
			result.elements=elements;
			result.segments=segments;
			
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return result;
	}
	
//	public org.bollore.edi.Segment setValue(Integer element_rank,ArrayList<String> values,Boolean create_new_segment) throws EDIException{
//		
//		
//		
//		if(element_rank<=0){
//			throw new EDIException("Le rang de l'élément ("+element_rank+") d'un segment doit être strictement positif");
//		}
//		
//		else if(element_rank>this.elements.size()){
//			throw new EDIException("Le rang de l'élément ("+element_rank+") est supérieur au nombre d'élément du segment");
//		}
//		
//		else {
//			
//			org.bollore.edi.Element element=this.elements.get(element_rank-1);
//			
//			// Si l'arraylist ne comporte qu'un seul élément, on doit affecter la valeur à l'élément et non au composant
//			if(values.size()==1&&element.components.size()==0){
//				element.value=values.get(0);
//			}
//			
//			else if(element.components.size()!=values.size()){
//				throw new EDIException("Le nombre de valeur ("+values.size()+") à affecter au segment "+this.code+"pour le "+element_rank+"ème élément ne correspond pas au nombre de composants "+element.components.size());
//			}
//			else{
//				
//				// Si l'élément est simple et non composé de composants
//				if(element.components.size()==0){
//					
//					// Catcher le fait que l'arraylist puisse contenir plusieurs éléments
//					element.value=values.get(0);
//				} else {
//					for (int j = 0; j < element.components.size(); j++) {
//						element.components.get(j).value=values.get(j);
//					}		
//				}				
//			}
//			
//		}
//			return this;
//	}
	
	
	public org.bollore.edi.Element getElement(String _CodeElement)
	{
		org.bollore.edi.Element element = new org.bollore.edi.Element();
		for (int i = 0; i < this.elements.size(); i++) 
		{
			if (this.elements.get(i).label.equals(_CodeElement))
				System.out.println("OK");
				element = (org.bollore.edi.Element)this.elements.get(i).clone();
		}
		
		return element;
	}

//	public org.bollore.edi.Element getElement(String _CodeElement)
//	{
//		org.bollore.edi.Element element = new org.bollore.edi.Element();
//		for (int i = 0; i < this.elements.size(); i++) 
//		{
//			if (this.elements.get(i).label.equals(_CodeElement))
//				System.out.println("OK");
//				element = (org.bollore.edi.Element)this.elements.get(i).clone();
//		}
//		
//		return element;
//	}
	
	
	public void printSegment()
	{
		System.out.println(this.code + " - Le segment possède les éléments suivants : ");
		
		if (this.segments.size() > 0)
		{
			for (int i = 0; i < this.segments.size(); i++) 
				this.segments.get(i).printSegment();
		}
		
		for (int i = 0; i < this.elements.size(); i++) {
			((org.bollore.edi.Element)this.elements.get(i)).printElement();
			
		}
	}
	
	public static void main(String[] args) throws XPathExpressionException, JDOMException, IOException{
		
		Segment original=new Segment("name","code",1,2,"description");
		Segment cloned=(Segment)original.clone();
		
		System.out.println(original.name+"   "+cloned.name);
		
		System.out.println(original!=cloned); // doit renvoyer true
		System.out.println(original.getClass() == cloned.getClass()); // doit renvoyer true
		System.out.println(original.equals(cloned)); // doit renvoyer false

	}
 
	
}
