package org.bollore.edi.models;

import java.io.*;

import org.jdom2.*;
import org.jdom2.input.*;
import org.jdom2.filter.*;

import java.util.List;
import java.util.Iterator;

public class ALFA {
	
	String filepath;
	static Document document;
	static org.jdom2.Element racine;
	
	
	public ALFA(String filepath) {
		super();
		this.filepath = filepath;
	}

	public void parseXMLALFA(){
		
		SAXBuilder sxb = new SAXBuilder();
		
		  try
	      {
	         //On crée un nouveau document JDOM avec en argument le fichier XML
	         //Le parsing est terminé ;)
	         document = sxb.build(new File("Exercice2.xml"));
	      }
	      catch(Exception e){}
		  
		  //On initialise un nouvel élément racine avec l'élément racine du document.
	      racine = document.getRootElement();
	}
	
	public void getVessel(){
		SAXBuilder sxb = new SAXBuilder();
		
		try
	      {
	         //On crée un nouveau document JDOM avec en argument le fichier XML
	         //Le parsing est terminé ;)
	         document = sxb.build(this.filepath);
	         racine = document.getRootElement();
	         org.jdom2.Element manifest = racine.getChild("Manifest");
	         //document.setRootElement(manifest);
	         System.out.println(manifest.getName());
	         org.jdom2.Element vessel=manifest.getChild("vessel");
	         System.out.println(vessel.getName());
	         System.out.println(vessel.getText());
	      }
	      catch(Exception e){}
	}
	
	public static void main(String[] args){
		ALFA alfa=new ALFA("C:/Bollore/Projets/EDI/Sample/Cuscar/Sekou/Cuscar_Input_OK.xml");
		
		alfa.getVessel();
	}

}
