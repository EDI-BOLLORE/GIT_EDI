package org.bollore.edi.parser;

import java.util.ArrayList;
import org.bollore.edi.*;

public class ElementParser {
	
	public static ArrayList<Element> parseElement(String elements,String component_separator){
		ArrayList<Element> result=new ArrayList<Element>();
		
		// L'�l�ment est vide
		if(elements==null||component_separator==null){
			result.add(new Element());
		// L'�l�ment n'est pas vide
		} else {
			//L'�l�ment n'est pas renseign�
			if(elements.trim().equals("")) {
				result.add(new Element());
			}
			// L'�l�ment est renseign�
			else {
				// Si il n'y a pas de s�parateur de composants c'est un �l�ment simple
				if(!elements.contains(component_separator)) {
				result.add(new Element(elements));
				
				// L'�l�ment est compos� de composants
				} else {
					// On cr�e un �l�ment temporaire
					Element t=new Element();
					
					ArrayList<String> temp=Utils.StringToArray(elements, component_separator);
					// On instantie les composants de l'�l�ment
					for (int i = 0; i < temp.size(); i++) {
						t.components.add(new Component(temp.get(i)));
						}
					// On ajoute finalement l'�l�ment
					result.add(t);
							}
				
				}
			
		}
		
		
		
		return result;
	}
	
	public static void main(String[] args){
		
		//ArrayList<Element> elements1=parseElement(null,null);
		
		//ArrayList<Element> elements1=parseElement("ABC",":");
		
		
		ArrayList<Element> elements1=parseElement(":A:B:C::D::",":");
		
		for (int i = 0; i < elements1.size(); i++) {
			System.out.println(elements1.get(i).value);
			elements1.get(i).printElement();
		}
	}
	
}
