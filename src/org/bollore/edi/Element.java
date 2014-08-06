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
	ArrayList<Component> components;

	//
	// Cette classe permet de modéliser la notion d'élément qui constitue les segments
	// Il peut s'agir d'un élément simple si components est vide ou un group si des composants 
	// Exemple d'élément simple dans le CUSCAR 95B: 3035 PARTY QUALIFIER dans le segment NAD
	// Exemple d'élément groupe dans le CUSCAR 95B: C002 dans le segment BGM
	
	public Element(){
		super();
		this.type_ref = null;
		this.required = null;
		this.truncatable = null;
		this.label = null;
		this.documentation = null;
		this.components = null;
		
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
	
	

	
	

	
	public void printElement(){
		
		System.out.println("\n\ttype_ref ="+this.type_ref+" label = "+this.label);
		
		System.out.println("\tL'élément possède les composants suivants : \n");
		
		for (int i = 0; i < this.components.size(); i++) {
			System.out.println("\t\tAffichage du composant " + i);
			((org.bollore.edi.Component)this.components.get(i)).printComponent();
		}
	}
	

	
	
	
	
	

}
