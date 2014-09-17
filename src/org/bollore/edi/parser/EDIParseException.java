package org.bollore.edi.parser;

public class EDIParseException extends Exception{

	
	public EDIParseException() {
		System.err
				.println("Une erreur est survenue lors du parcours de l'EDI");
	}
	
	public EDIParseException(String message) {
		System.err.println("EDIParseException : "+message + "\n");
	}
}
