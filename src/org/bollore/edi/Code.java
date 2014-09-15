package org.bollore.edi;

public class Code {

	String code;
	String name;
	String description;

	public Code(String code, String name, String description) {
		super();
		this.code = code;
		this.name = name;
		this.description = description;
	}

	public Code(String code, String name) {
		super();
		this.code = code;
		this.name = name;
	}

	public Code(String code) {
		super();
		this.code = code;
	}

}
