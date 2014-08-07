package org.bollore.edi;

import java.util.ArrayList;
import java.util.HashSet;

public class Element {
	
	String type_ref;
	Boolean required;
	Boolean truncatable;
	String label;
	String documentation;
	String value;
	ArrayList<String> values;
	ArrayList<org.bollore.edi.Component> components;

	//
	// Cette classe permet de mod�liser la notion d'�l�ment qui constitue les segments
	// Il peut s'agir d'un �l�ment simple si components est vide ou un group si des composants 
	// Exemple d'�l�ment simple dans le CUSCAR 95B: 3035 PARTY QUALIFIER dans le segment NAD
	// Exemple d'�l�ment groupe dans le CUSCAR 95B: C002 dans le segment BGM
	
	public Element(){
		super();
		this.type_ref = null;
		this.required = null;
		this.truncatable = null;
		this.label = null;
		this.documentation = null;
		this.components = new ArrayList<Component>();
		
	}
	
	public Element(String type_ref, Boolean required, Boolean truncatable,
			String label, String documentation,ArrayList<Component> components) {
		super();
		this.type_ref = type_ref;
		this.required = required;
		this.truncatable = truncatable;
		this.label = label;
		this.documentation = documentation;
		this.components = components;
	}
	
	

	public org.bollore.edi.Component getComponent_Xav(String _LabelComponent)
	{
		org.bollore.edi.Component component = null;
		for (int i = 0; i < this.components.size(); i++) 
		{
			if (this.components.get(i).label.equals(_LabelComponent))
				component = this.components.get(i);
		}
		
		return component;
	}
	

	
	public void printElement(){
		
		System.out.println("\n\ttype_ref ="+this.type_ref+" label = "+this.label+" value = "+this.value);
		
		System.out.println("\tL'�l�ment poss�de les composants suivants : \n");
		
		for (int i = 0; i < this.components.size(); i++) {
			System.out.println("\t\tAffichage du composant " + i);
			((org.bollore.edi.Component)this.components.get(i)).printComponent();
		}
	}
	
	
	public void setValue(String _Value)
	{
		this.value = _Value;		
	}
	
	public void addValue(ArrayList<String> _Values)
	{
		for (int i = 0; i < this.components.size(); i++) 
		{
			this.components.get(i).setValue(_Values.get(i));
		}
		this.values = _Values;		
	}

	
	
	
	
	

}
