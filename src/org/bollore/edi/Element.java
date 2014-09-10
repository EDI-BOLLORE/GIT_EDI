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
	// Cette classe permet de mod�liser la notion d'�l�ment qui constitue les segments
	// Il peut s'agir d'un �l�ment simple si components est vide ou un group si des composants 
	// Exemple d'�l�ment simple dans le CUSCAR 95B: 3035 PARTY QUALIFIER dans le segment NAD
	// Exemple d'�l�ment groupe dans le CUSCAR 95B: C002 dans le segment BGM

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
			String label, String documentation){
		super();
		this.code = code;
		this.required = required;
		this.truncatable = truncatable;
		this.label = label;
		this.documentation = documentation;
	}	



	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
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

		System.out.println("\tL'�l�ment poss�de les composants suivants : \n");

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

			for (int i = 0; i < this.components.size(); i++) {
				components.add(this.components.get(i).clone());
			}

			result.components=components;
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
	// Cette m�thode permet de renvoyer le rang de la derni�re valeur affect�e � un composant
	// Elle permet de ne pas afficher les ':' inutiles dans l'EDI quand les valeurs ne sont pas affect�es
	public Integer MaxRankComponentNonNull() {
		
		Integer result=null;
		
		
		if(this.components==null){
			result=0;	
		} else if(this.components.size()<=0) {
			result=0;	
		} else if(this.components.size()>0) {
			result = this.components.size();
			// On d�termine le rang � partir duquel tous les
			// composants n'ont pas de valeur renseign�e
			for (int l = components.size() - 1; l >= 0; l--) {
				
				org.bollore.edi.Component component = components
						.get(l);
				
				if (component.value == null) {
					
					result--;

				} else if(component.value.trim().equals("")){									
						
					result--;
					} else {
						// Si la valeur est renseign�e, on sort de la boucle
						break;
					}
			}
		}
		
		return result;
		
	}
	
	public Boolean HasEmptyComponents() {
		Boolean result=true;
		
		if(this.components!=null) {
			
			if(this.components.size()>0){

				for (int i = 0; i < this.components.size(); i++) {
					if(this.components.get(i)!=null) {
						
						if(this.components.get(i).value!=null) {
							if(!this.components.get(i).value.trim().equals("")) {
								result=false;
								break;
							}

						}
							
						}
							
					} 
					} 
				}
		
		
	return result;		
	}


	public static void main(String[] args){

		ArrayList<org.bollore.edi.Component> components=new ArrayList<org.bollore.edi.Component>();
		
		org.bollore.edi.Component c1=new Component("String","10","1",true,true,"label1","doc1","value2");
		org.bollore.edi.Component c2=new Component("String","10","1",true,true,"label2","doc2",null);
		org.bollore.edi.Component c3=new Component("String","10","1",true,true,"label3","doc3","value3");
		
//		components.add(c1);
		components.add(c2);
//		components.add(c3);
		
		org.bollore.edi.Element element=new Element("code_element",true,true,"label_element","doc_element",components);
		//org.bollore.edi.Element element=new Element("code_element",true,true,"label_element","doc_element",new ArrayList<org.bollore.edi.Component>());
		//org.bollore.edi.Element element=new Element("code_element",true,true,"label_element","doc_element",null);
		
		//System.out.println(element.MaxRankComponentNonNull());
		System.out.println(element.HasEmptyComponents());

		}


	}


