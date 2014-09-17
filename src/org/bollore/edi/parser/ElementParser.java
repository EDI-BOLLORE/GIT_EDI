package org.bollore.edi.parser;

import java.util.ArrayList;
import org.bollore.edi.*;

public class ElementParser {
	
	public static ArrayList<Element> parseElement(String elements,String component_separator){
		ArrayList<Element> result=new ArrayList<Element>();
		
		// L'élément est vide
		if(elements==null||component_separator==null){
			result.add(new Element());
		// L'élément n'est pas vide
		} else {
			//L'élément n'est pas renseigné
			if(elements.trim().equals("")) {
				result.add(new Element());
			}
			// L'élément est renseigné
			else {
				// Si il n'y a pas de séparateur de composants c'est un élément simple
				if(!elements.contains(component_separator)) {
				result.add(new Element(elements));
				
				// L'élément est composé de composants
				} else {
					// On crée un élément temporaire
					Element t=new Element();
					
					ArrayList<String> temp=Utils.StringToArray(elements, component_separator);
					// On instantie les composants de l'élément
					for (int i = 0; i < temp.size(); i++) {
						t.components.add(new Component(temp.get(i)));
						}
					// On ajoute finalement l'élément
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
