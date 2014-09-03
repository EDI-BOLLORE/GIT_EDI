package org.bollore.edi;

import java.util.ArrayList;
import java.util.HashSet;

public class Element implements Cloneable{

	String code;
	Boolean required;
	Boolean truncatable;
	String label;
	String documentation;
	String value;
	ArrayList<org.bollore.edi.Component> components;

	//
	// Cette classe permet de modéliser la notion d'élément qui constitue les segments
	// Il peut s'agir d'un élément simple si components est vide ou un group si des composants 
	// Exemple d'élément simple dans le CUSCAR 95B: 3035 PARTY QUALIFIER dans le segment NAD
	// Exemple d'élément groupe dans le CUSCAR 95B: C002 dans le segment BGM

	public Element(){
		super();
		this.code = null;
		this.required = null;
		this.truncatable = null;
		this.label = null;
		this.documentation = null;
		this.components = new ArrayList<Component>();

	}

	public Element(String code, Boolean required, Boolean truncatable,
			String label, String documentation,ArrayList<Component> components) {
		super();
		this.code = code;
		this.required = required;
		this.truncatable = truncatable;
		this.label = label;
		this.documentation = documentation;
		this.components = components;
	}

	Element(String code, Boolean required, Boolean truncatable,
			String label, String documentation,String value){
		super();
		this.code = code;
		this.required = required;
		this.truncatable = truncatable;
		this.label = label;
		this.documentation = documentation;
		this.value=value;
	}
	
	Element(String code, Boolean required, Boolean truncatable,
			String label, String documentation){
		super();
		this.code = code;
		this.required = required;
		this.truncatable = truncatable;
		this.label = label;
		this.documentation = documentation;
	}

	public org.bollore.edi.Component getComponent(String _LabelComponent)
	{
		org.bollore.edi.Component component = null;
		for (int i = 0; i < this.components.size(); i++) 
		{
			if (this.components.get(i).label.equals(_LabelComponent))
				component = (org.bollore.edi.Component)this.components.get(i).clone();
		}

		return component;
	}



	public void printElement(){

		System.out.println("\n\ttype_ref ="+this.code+" label = "+this.label+" value = "+this.value);

		System.out.println("\tL'élément possède les composants suivants : \n");

		for (int i = 0; i < this.components.size(); i++) {
			System.out.println("\t\tAffichage du composant " + i);
			((org.bollore.edi.Component)this.components.get(i)).printComponent();
		}
	}

	public org.bollore.edi.Element clone() {
		org.bollore.edi.Element result=null;

		try {

			ArrayList<Component> components=new ArrayList<Component>();
			result=(org.bollore.edi.Element)super.clone();

			result=new Element(this.code, this.required, this.truncatable, this.label, this.documentation);

			//			result.code=this.code;
			//			result.required=this.required;
			//			result.truncatable=this.truncatable;
			//			result.label=this.label;
			//			result.documentation=this.documentation;
			//			result.value=this.value;

			for (int i = 0; i < this.components.size(); i++) {
				components.add(this.components.get(i).clone());
			}

			result.components=components;
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static void main(String[] args){

		ArrayList<Component> components = new ArrayList<Component>();

		Component c1=new Component("datatype", "1", "2", true, true, "label", "documentation","value1");
		Component c2=new Component("datatype2", "1", "2", false, false, "label2", "documentation2","value2");

		components.add(c1);
		components.add(c2);

		Element original=new Element("type_ref",true,true,"label","documentation", components);

		Element cloned=(Element)original.clone();

		//		System.out.println(cloned.type_ref);
		//		
		//		System.out.println(original!=cloned); // doit renvoyer true
		//		System.out.println(original.getClass() == cloned.getClass()); // doit renvoyer true
		//		System.out.println(original.equals(cloned)); // doit renvoyer false

		ArrayList<Component> original_components=original.components;
		ArrayList<Component> cloned_components=cloned.components;

		for (int i = 0; i < original_components.size(); i++) {
			System.out.println(original_components.get(i)!=cloned_components.get(i));
			System.out.println(original_components.get(i).getClass() == cloned_components.get(i).getClass());
			System.out.println(original_components.get(i).equals(cloned_components.get(i)));
		}


	}









}
