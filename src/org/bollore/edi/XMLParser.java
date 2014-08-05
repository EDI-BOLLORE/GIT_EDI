package org.bollore.edi;

import java.util.Iterator;
import java.util.List;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.filter.ElementFilter;
import org.jdom2.input.SAXBuilder;
import org.jdom2.util.IteratorIterable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.xml.sax.InputSource;

public class XMLParser {
	
	String filepath;
	static Document document;
	static org.jdom2.Element racine;
	
	
	
	public XMLParser(String filepath, Document document, Element racine) {
		super();
		this.filepath = filepath;
		this.document = document;
		this.racine = racine;
	}
	
	public XMLParser(String filepath) {
		super();
		this.filepath = filepath;
		this.document = new Document();
		this.racine = new org.jdom2.Element("edimap");
	}


	
	public static String getElementDef(String segcode) throws XPathExpressionException,FileNotFoundException{
		
		File definitions=new File("EDI_Definitions/D95B/__modelset_definitions.xml");
		//definitions
		
		InputStream is=new FileInputStream("EDI_Definitions/D95B/__modelset_definitions.xml");
		InputSource source = new InputSource(is);
		
		  // creation de la requete XPTAH
		  XPathFactory xPathFactory = XPathFactory.newInstance();
		  XPath xpath = xPathFactory.newXPath();
		  XPathExpression xPathExpression = xpath.compile("/edimap/segments/segment[@segcode='"+segcode+"']"); //"//segment[@segcode='DTM']"

		  /* execution de la requete xPath sur le fichier XML */
		  String resultat = (String) xPathExpression.evaluate(source,XPathConstants.STRING);
		  
		  return resultat;
	}
	
	public static void main(String[] args) throws XPathExpressionException, FileNotFoundException{
		//XMLParser xmlparser = new XMLParser("C:/Bollore/Projets/EDI/Smooks/Mappings/org_milyn_edi_unedifact/d95b-mapping/1_4/CUSCAR.xml");
		
		//XMLParser xmlparser = new XMLParser("EDI_Definitions/D95B/CUSCAR.xml");
		//XMLParser.buildEDISegment();
		//System.out.println(getElementDef("tata"));
	}
}
