package org.bollore.edi;

public class Component implements Cloneable {

	public String dataType;
	public String maxLength;
	public String minLength;
	public Boolean required;
	public Boolean truncatable;
	public String label;
	public String documentation;
	public String value;

	//
	// Cette classe permet de modéliser la notion de composant à savoir ce qui
	// compose un élément de groupe d'un segment
	//

	public Component() {
		super();
		this.dataType = "";
		this.maxLength = "";
		this.minLength = "";
		this.required = false;
		this.truncatable = false;
		this.label = "";
		this.documentation = "";
	}
	
	public Component(String value) {
		super();
		this.value=value;
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
			String documentation, String value) {
		super();
		this.dataType = dataType;
		this.maxLength = maxLength;
		this.minLength = minLength;
		this.required = required;
		this.truncatable = truncatable;
		this.label = label;
		this.documentation = documentation;
		this.value = value;
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
		this.value = component.value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public org.bollore.edi.Component clone() {
		org.bollore.edi.Component result = null;

		try {

			//result=new org.bollore.edi.Component();
			result = (org.bollore.edi.Component) super.clone();

			result.dataType = this.dataType;
			result.maxLength = this.maxLength;
			result.minLength = this.minLength;
			result.required = this.required;
			result.truncatable = this.truncatable;
			result.label = this.label;
			result.documentation = this.documentation;
			result.value = this.value;

		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public void mapComponent(){}

	public void printComponent() {

		System.out.println("\t\tlabel = " + this.label + "  valeur "
				+ this.value + "\n");
	}

	public static void main(String[] args) {



	}

}
