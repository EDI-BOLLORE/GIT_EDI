package org.bollore.edi.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.Semaphore;

import org.bollore.edi.*;

public class EdifactParser {

	public String filepath;
	public Character element_separator;
	public Character component_separator;
	public Character decimal_separator;
	public Character escape_char;
	public Character space_char;
	public Character segment_separator;

	public EdifactParser(String filepath, Character element_separator,
			Character component_separator, Character decimal_separator,
			Character escape_char, Character space_char, Character segment_separator) {
		super();
		this.filepath = filepath;
		this.element_separator = element_separator;
		this.component_separator = component_separator;
		this.decimal_separator = decimal_separator;
		this.escape_char = escape_char;
		this.space_char = space_char;
		this.segment_separator = segment_separator;
	}

	public EdifactParser(String filepath) {
		super();
		this.filepath = filepath;
		this.decimal_separator='.';
		this.segment_separator='\'';
		this.element_separator='+';
		this.component_separator=':';
		this.escape_char='?';
		this.space_char=' ';
	}

	
	




	public static void main(String[] args) throws IOException, EDIException,
			EDIParseException {
		
		String filepath="C:/Bollore/Projets/EDI/Coreor/COREOR_1_line.txt";
		
		//String filepath="C:/Bollore/Projets/EDI/Coreor/COREOR.txt";

		// EdifactParser edi=new EdifactParser("TestFiles/TestCuscar.edi");
		EdifactParser edi = new EdifactParser("C:/Bollore/Projets/EDI/Coreor/COREOR_1_line.txt");
		//EdifactParser edi = new EdifactParser("C:/Bollore/Projets/EDI/Coreor/COREOR.txt");
		
		//edi.buildEDI();
		 
		 
		 
		 


	}

}
