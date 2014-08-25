package org.bollore.edi;

import java.io.IOException;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

public class Component implements Cloneable{
	
	String dataType;
	String maxLength; 
	String minLength;
	Boolean required;
	Boolean truncatable;
	String label;
    String documentation;
    String value;
    
    //
    // Cette classe permet de modéliser la notion de composant à savoir ce qui compose un élément de groupe d'un segment
    //
    

	public Component(String dataType, String maxLength, String minLength,
			Boolean required, Boolean truncatable, String label,
			String documentation) {
		super();
		this.dataType = dataType;
		this.maxLength = maxLength;
		this.minLength = minLength;
		this.required = required;
		this.truncatable = truncatable;
		this.label = label;
		this.documentation = documentation;
	}
	
	public void setValue(String _Value)
	{
		this.value = _Value;		
	}
	
	public Component clone() {
		Component o = null;
		try {
			// On récupère l'instance à renvoyer par l'appel de la 
			// méthode super.clone()
			o = (Component)super.clone();
		} catch(CloneNotSupportedException cnse) {
			// Ne devrait jamais arriver car nous implémentons 
			// l'interface Cloneable
			cnse.printStackTrace(System.err);
		}
		// on renvoie le clone
		return o;
	}
	
	public void printComponent(){
		
		System.out.println("\t\tlabel = "+this.label + "  valeur "+this.value+"\n");
	}

	public static void main(String[] args){
	}
	
}
