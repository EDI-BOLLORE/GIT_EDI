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
	
	public Boolean isEmpty(){
		Boolean result=true;
		
		for (int i = 0; i < this.elements.size(); i++) {
			if(this.elements.get(i).value!=null){
				result=false;
			}
		}
		for (int j = 0; j < this.segments.size(); j++) {
			//return
		}
		
		return result;
		
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
		
		//System.out.println(original.name+"   "+cloned.name);
		
		System.out.println(original!=cloned); // doit renvoyer true
		System.out.println(original.getClass() == cloned.getClass()); // doit renvoyer true
		System.out.println(original.equals(cloned)); // doit renvoyer false

	}
 
	
}
