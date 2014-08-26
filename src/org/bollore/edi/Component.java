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
    
    public Component(){
    	super();
		this.dataType = "";
		this.maxLength = "";
		this.minLength = "";
		this.required = false;
		this.truncatable = false;
		this.label = "";
		this.documentation = "";
    }

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
	
	public Component(String dataType, String maxLength, String minLength,
			Boolean required, Boolean truncatable, String label,
			String documentation,String value) {
		super();
		this.dataType = dataType;
		this.maxLength = maxLength;
		this.minLength = minLength;
		this.required = required;
		this.truncatable = truncatable;
		this.label = label;
		this.documentation = documentation;
		this.value=value;
	}
	
	public Component(Component component) {
		super();
		this.dataType = component.dataType;
		this.maxLength = component.maxLength;
		this.minLength = component.minLength;
		this.required = component.required;
		this.truncatable = component.truncatable;
		this.label = component.label;
		this.documentation = component.documentation;
		this.value=component.value;
	}

	public void setValue(String _Value)
	{
		this.value = _Value;		
	}
	
	public org.bollore.edi.Component clone() {
		
		//org.bollore.edi.Component result=new org.bollore.edi.Component();
		org.bollore.edi.Component result=null;
		
		try {
			result=(org.bollore.edi.Component)super.clone();
			
			result.dataType=this.dataType;
			result.maxLength=this.maxLength;
			result.minLength=this.minLength;
			result.required=this.required;
			result.truncatable=this.truncatable;
			result.label=this.label;
			result.documentation=this.documentation;
			result.value=this.value;

					
					
			
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public void printComponent(){
		
		System.out.println("\t\tlabel = "+this.label + "  valeur "+this.value+"\n");
	}
	
	


	public static void main(String[] args) {
		
		Component original=new Component("datatype", "1", "2", true, true, "label", "documentation");
		
		Component cloned=(Component)original.clone();
		
		System.out.println(cloned.minLength);
		
		System.out.println(original!=cloned); // doit renvoyer true
		System.out.println(original.getClass() == cloned.getClass()); // doit renvoyer true
		System.out.println(original.equals(cloned)); // doit renvoyer false
		

		
		
	}
	
}
