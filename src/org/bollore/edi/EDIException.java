package org.bollore.edi;

public class EDIException extends Exception {

	public EDIException(){
		System.err.println("Une erreur est survenue lors de la g�n�ration de l'EDI");
	}

	public EDIException(String message){
		System.err.println(message+"\n");
	}

}
