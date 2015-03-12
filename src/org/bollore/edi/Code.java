package org.bollore.edi;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

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
	
	public static void main(String[] args) throws IOException {
		 OutputStream fout= new FileOutputStream("C:/Temp/test.xml");
		 Utils.CreateDir("C:/Temp/test.xml");
	        OutputStream bout= new BufferedOutputStream(fout);
	        OutputStreamWriter out = new OutputStreamWriter(bout, "Cp1252"); // Encodage ANSI
	        out.write("Blabla");
	        out.write("\r\n");
	        out.write("Blabla2");
	        out.flush();
	        out.close();
	        
	        PrintWriter pw=new PrintWriter("C:/Temp/test.xml","Cp1252");
	        pw.write("Tata");
	        pw.flush();
	        pw.close();

	}

}
