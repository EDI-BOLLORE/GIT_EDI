package org.bollore.edi;

import java.io.IOException;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

public class Component {
	
	String dataType;
	String maxLength; 
	String minLength;
	Boolean required;
	Boolean truncatable;
	String label;
    String documentation;
    String value;
    
    //
    // Cette classe permet de mod�liser la notion de composant � savoir ce qui compose un �l�ment de groupe d'un segment
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
	

	
	public void printComponent(){
		
		System.out.println("\t\tlabel = "+this.label + "  valeur "+this.value+"\n");
	}

	public static void main(String[] args){
	}
	
}
