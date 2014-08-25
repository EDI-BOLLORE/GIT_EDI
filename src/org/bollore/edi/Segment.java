package org.bollore.edi;



import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.xpath.XPathExpressionException;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

public class Segment implements Cloneable{

	String name;
	String code;
	Integer min_occurence;
	Integer max_occurence;
	String description;
	//Integer order;
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
	
	
	public Segment clone() {
		Segment o = new Segment();
		try {
			// On récupère l'instance à renvoyer par l'appel de la 
			// méthode super.clone()
			o = (Segment)super.clone();
			
			//ArrayList<org.bollore.edi.Element> cur_elements=this.elements;
			//ArrayList<org.bollore.edi.Segment> cur_segments=this.segments;
			
			for (int i = 0; i < this.segments.size(); i++) {
				o.segments.add(this.segments.get(i).clone());
				//=this.segments.get(i).clone();
				
				//(Segment)cur_segments.get(i)=o.segments.get(i).clone();
				
			}
			
			for (int j = 0; j < this.elements.size(); j++) {
				o.elements.add(this.elements.get(j).clone());
				
			}
			
			
			
			/**
			 * 	String name;
	String code;
	Integer min_occurence;
	Integer max_occurence;
	String description;
	//Integer order;
	ArrayList<org.bollore.edi.Element> elements;
	ArrayList<org.bollore.edi.Segment> segments;
			 * 
			 */
		} catch(CloneNotSupportedException cnse) {
			// Ne devrait jamais arriver car nous implémentons 
			// l'interface Cloneable
			cnse.printStackTrace(System.err);
		}
		// on renvoie le clone
		return o;
	}
	

	public org.bollore.edi.Element getElement_Xav(String _CodeElement)
	{
		org.bollore.edi.Element element = new org.bollore.edi.Element();
		for (int i = 0; i < this.elements.size(); i++) 
		{
			if (this.elements.get(i).type_ref.equals(_CodeElement))
				element = this.elements.get(i).clone();
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
		

		
		

	}
 
	
}
